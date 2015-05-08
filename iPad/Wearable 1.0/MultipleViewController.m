//
//  MultipleViewController.m
//  Wearable 1.0
//
//  Created by Michael Gutbier on 04.05.15.
//  Copyright (c) 2015 Michael Gutbier. All rights reserved.
//

#import "MultipleViewController.h"
#import "DBController.h"
#import "DataTable.h"
#import "ImageViewController.h"
#import "SingleViewController.h"
#import "MultipleViewController.h"

@interface MultipleViewController ()
@property (strong, nonatomic) UIPopoverController *masterPopoverController;

@property (nonatomic, strong) NSDictionary *dictionary;
@property (nonatomic, strong) NSArray *edges;
@property (nonatomic, strong) NSArray *nodeDetails;
@property (nonatomic, strong) NSString *db_name;
@property int nodeID, prevNode;

@end

@implementation MultipleViewController

@synthesize labelTest;
@synthesize table;
@synthesize db;
@synthesize titleNode;
@synthesize firstEdge;
@synthesize secondEdge;
@synthesize thirdEdge;
@synthesize selectionEdges;
@synthesize nextButton;

- (instancetype)initWithNodeObject:(NSDictionary *)node {
    
    self = [super init];
    if (self) {
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle: nil];
        self = [storyboard instantiateViewControllerWithIdentifier:@"Multiple"];
        //self.edges = [DetailViewController getEdgesForNode:[node objectForKey:@"id"]];
        self.dictionary = node;
    }
    return self;
}

- (instancetype)initWithNodeID:(int) nID andDB: (NSString*) dbName andPrev:(int)prev{
    
    self = [super init];
    if (self) {
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle: nil];
        self = [storyboard instantiateViewControllerWithIdentifier:@"Multiple"];
        self.nodeID = nID;
        self.db_name = dbName;
        self.db = [DBController sharedDatabaseController:dbName];
        self.edges = [self getEdgesForNode:self.nodeID];
        self.nodeDetails = [self getNodeDetails:self.nodeID];
        self.prevNode = prev;
    }
    return self;
}

-(NSArray *)getEdgesForNode:(int) nodeID{
    NSLog(@"Insert values in edges array.");
    DataTable *tableTemp = nil;
    NSString *edgeQuery = [NSString stringWithFormat: @"SELECT NODEID, SUCCESSORID, DESCRIPTION, EDGEID FROM EDGE WHERE NODEID = %d ORDER BY EDGEID", nodeID];
    tableTemp = [self.db  ExecuteQuery:edgeQuery];
    return tableTemp.rows;
    //return NULL;
}

-(NSArray *)getNodeDetails:(int) nodeID{
    NSLog(@"Insert values in node details array.");
    DataTable *tableTemp = nil;
    NSString *tempQuery = [NSString stringWithFormat: @"SELECT TITLE, IMAGEID, VIEWID, FORWARDTEXT FROM NODE WHERE NODEID = %d", nodeID];
    tableTemp = [self.db  ExecuteQuery:tempQuery];
    return tableTemp.rows;
    //return NULL;
}

-(int)getSuccessorForEdge:(int) edgeID{
    return [[[self.edges objectAtIndex:edgeID] objectAtIndex:1] intValue];
}

-(IBAction) nextNav:(id)sender{
    /*if(self.selected == -1){
        UIAlertView *message = [[UIAlertView alloc] initWithTitle:@"Failure"
                                                          message:@"Please select a item!"
                                                         delegate:self
                                                cancelButtonTitle:@"Ok"
                                                otherButtonTitles: nil];
        [message show];
    }else{*/
        [self createNewView:[self getSuccessorForEdge: [[[self.edges objectAtIndex:0] objectAtIndex:0] intValue]]];
    //}
    //[self createNewView:1]; //STATIC TEST
}

-(void)showNextNode{
    [self createNewView:[self getSuccessorForEdge: [[[self.edges objectAtIndex:0] objectAtIndex:0] intValue]]];
}
-(void)getBack{
    [self createNewView:self.prevNode];
}

- (UIImage*)getBackground{
    sqlite3_stmt *compiledStmt;
    sqlite3 *dataBase;
    //NSString *path;
    /* NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
     path = [[paths objectAtIndex:0] stringByAppendingPathComponent:@"My Directory"];
     path = [path stringByAppendingPathComponent:self.db_name];
     NSLog(@"DB Path: %@", path);*/
    
    NSArray* paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString* documentsDirectory = [paths lastObject];
    NSString* databasePath = [documentsDirectory stringByAppendingPathComponent:self.db_name];
    
    if(sqlite3_open([databasePath UTF8String], &dataBase) == SQLITE_OK) {
        NSLog(@"Opened sqlite database at %@", databasePath);
        // NSLog(@"DB Open and read Image for ID: %d", [[[self.viewDetails objectAtIndex:0] objectAtIndex:0] intValue]);
        NSString *insertSQL = [NSString stringWithFormat:@"SELECT IMAGE FROM IMAGE WHERE IMAGEID = %d",[[[self.nodeDetails objectAtIndex:0] objectAtIndex:1] intValue]];
        if(sqlite3_prepare_v2(dataBase,[insertSQL cStringUsingEncoding:NSUTF8StringEncoding], -1, &compiledStmt, NULL) == SQLITE_OK) {
            while(sqlite3_step(compiledStmt) == SQLITE_ROW) {
                
                int length = sqlite3_column_bytes(compiledStmt, 0);
                NSData *imageData = [NSData dataWithBytes:sqlite3_column_blob(compiledStmt, 0) length:length];
                
                NSLog(@"Length : %lu", (unsigned long)[imageData length]);
                
                if(imageData == nil)
                    return nil;
                else
                    return [UIImage imageWithData:imageData];
            }
        }
        sqlite3_finalize(compiledStmt);
        
    } else {
        NSLog(@"Failed to open database at %@ with error %s", databasePath, sqlite3_errmsg(dataBase));
        sqlite3_close (dataBase);
        return nil;
    }
    return nil;
}

- (void)viewDidLoad
{
    UIImageView *backgroundView = [[UIImageView alloc] initWithImage:[self getBackground]];
    [self.view addSubview:backgroundView];
    backgroundView.center = self.view.center;
    backgroundView.alpha = 0.25;
    
    [super viewDidLoad];
    
    [self.selectionEdges setDelegate:self];
    [self.selectionEdges setDataSource:self];
    SEL aSelector = @selector(showNextNode);
    SEL bSelector = @selector(getBack);
    self.navigationItem.title = [[self.nodeDetails objectAtIndex:0] objectAtIndex:0];
    if (![[[self.nodeDetails objectAtIndex:0] objectAtIndex:2]  isEqual: @"Not shown"]){
        UIBarButtonItem *anotherButton = [[UIBarButtonItem alloc] initWithTitle:[[self.nodeDetails objectAtIndex:0] objectAtIndex:2] style:UIBarButtonItemStylePlain
                                                                         target:self action:aSelector];
        self.navigationItem.rightBarButtonItem = anotherButton;
        self.navigationController.navigationItem.rightBarButtonItem = anotherButton;
        self.navigationController.navigationBar.topItem.rightBarButtonItem = anotherButton;
    }
    if(self.nodeID != 0){
        UIBarButtonItem *btnBack = [[UIBarButtonItem alloc] initWithTitle: @"Back" style: UIBarButtonItemStylePlain target:self action:bSelector];
        self.navigationItem.leftBarButtonItem = btnBack;
        self.navigationController.navigationItem.leftBarButtonItem = btnBack;
        self.navigationController.navigationBar.topItem.leftBarButtonItem = btnBack;
    }
    self.navigationController.navigationBar.topItem.title = [[self.nodeDetails objectAtIndex:0] objectAtIndex:0];
    /*
    NSString* string = @"Multiple: ";
    
    for(int i=0; i<[self.edges count]; i++){
        string = [string stringByAppendingString:[[self.edges objectAtIndex:i] objectAtIndex:2]];
    }
    labelTest.text = string;
    */
    titleNode.text = [[self.nodeDetails objectAtIndex:0] objectAtIndex:0];
    NSArray *edgeTitles = @[firstEdge, secondEdge, thirdEdge];

    for(int i = 0; i < self.edges.count-1; i++){
        UILabel *temp = [edgeTitles objectAtIndex:i];
        temp.text = [[self.edges objectAtIndex:i] objectAtIndex:2];
    }
    
    //firstEdge.text = [[self.edges objectAtIndex:0] objectAtIndex:2];
    //testNext. = @"NEXT1";
    //secondEdge.text = [[self.edges objectAtIndex:1] objectAtIndex:2];
    //thirdEdge.text = [[self.edges objectAtIndex:2] objectAtIndex:2];
    
    [nextButton setTitle: [[self.nodeDetails objectAtIndex:0] objectAtIndex:2] forState: UIControlStateNormal];
    
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)showView{
    UIViewController *viewController;
    viewController = self.multipleViewController;
    [self showChildViewController:viewController];
    
}

-(void)createNewView: (int) nodeID{
    DataTable *tableTemp = nil;
    NSString *tempQuery = [NSString stringWithFormat: @"SELECT TYPEID FROM NODE WHERE NODEID = %d", nodeID];
    tableTemp = [self.db  ExecuteQuery:tempQuery];
    int typeOfNewNode = [[[tableTemp.rows objectAtIndex:0] objectAtIndex:0] intValue];
    
    if(typeOfNewNode == 0){
        //Lade Image VC
        //ImageViewController *vc = [[ImageViewController alloc] initWithNodeObject:nil];
        ImageViewController *vc = [[ImageViewController alloc] initWithNodeID: nodeID andDB: self.db_name andPrev:self.nodeID];
        
        UINavigationController *navigationController;
        navigationController = [[UINavigationController alloc] init];
        [navigationController setViewControllers:@[vc] animated:NO];
        
        [self.splitViewController showDetailViewController:navigationController sender:self];
    }else{
        if(typeOfNewNode == 1){
            //Lade Single VC
            //SingleViewController *vc = [[SingleViewController alloc] initWithNodeObject:nil];
            SingleViewController *vc = [[SingleViewController alloc] initWithNodeID:nodeID andDB: self.db_name andPrev:self.nodeID];
            
            UINavigationController *navigationController;
            navigationController = [[UINavigationController alloc] init];
            [navigationController setViewControllers:@[vc] animated:NO];
            
            [self.splitViewController showDetailViewController:navigationController sender:self];
        }else{
            if(typeOfNewNode == 2){
                //Lade Multiple VC
                //MultipleViewController *vc = [[MultipleViewController alloc] initWithNodeObject:nil];
                MultipleViewController *vc = [[MultipleViewController alloc] initWithNodeID:nodeID andDB: self.db_name andPrev:self.nodeID];
                
                UINavigationController *navigationController;
                navigationController = [[UINavigationController alloc] init];
                [navigationController setViewControllers:@[vc] animated:NO];
                
                [self.splitViewController showDetailViewController:navigationController sender:self];
            }else{
                NSLog(@"Error1");
                UIAlertView *message = [[UIAlertView alloc] initWithTitle:@"Failure"
                                                                  message:@"Faulty database!"
                                                                 delegate:self
                                                        cancelButtonTitle:@"Ok"
                                                        otherButtonTitles: nil];
                [message show];
            }
            
        }
    }
}

-(void)showChildViewController:(UIViewController*)content {
    if(topController != content) {
        content.view.frame = [self.view frame]; // 2
        [self.view addSubview:content.view];
        [content didMoveToParentViewController:self];
        topController = content;
    }
}



#pragma mark - Split view

- (void)splitViewController:(UISplitViewController *)splitController willHideViewController:(UIViewController *)viewController withBarButtonItem:(UIBarButtonItem *)barButtonItem forPopoverController:(UIPopoverController *)popoverController
{
    barButtonItem.title = NSLocalizedString(@"Master", @"Master");
    [self.navigationItem setLeftBarButtonItem:barButtonItem animated:YES];
    self.masterPopoverController = popoverController;
}

- (void)splitViewController:(UISplitViewController *)splitController willShowViewController:(UIViewController *)viewController invalidatingBarButtonItem:(UIBarButtonItem *)barButtonItem
{
    // Called when the view is shown again in the split view, invalidating the button and popover controller.
    [self.navigationItem setLeftBarButtonItem:nil animated:YES];
    self.masterPopoverController = nil;
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.edges.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier] ;
        
    }
    
    if(indexPath.row % 2){
        [cell setBackgroundColor:[UIColor whiteColor]];
    }
    else{
        [cell setBackgroundColor:[[UIColor alloc] initWithRed:((float)(222.0 / 255.0)) green:((float)(227.0 / 255.0)) blue:((float)(231.0 / 255.0)) alpha:1.0f]];
        
    }
    
    
    
    //NSArray* row= self.objectNodes.rows[indexPath.row];
    
    cell.textLabel.text = [[self.edges objectAtIndex:indexPath.row] objectAtIndex:2];
    cell.textLabel.font=[UIFont boldSystemFontOfSize:23];
    //cell.
    
    //cell.detailTextLabel.text = [NSString stringWithFormat:@"%@",row[[self.objectNodes colIndex:@"NODEID"]]];
    
    return cell;
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    //[self.selectionEdges cellForRowAtIndexPath:indexPath].accessoryType = UITableViewCellAccessoryCheckmark;
    
    //[self.selectionEdges deselectRowAtIndexPath:indexPath animated:YES];
    
     UITableViewCell *selectedCell = [self.selectionEdges cellForRowAtIndexPath:indexPath];
    
    //if (selectedCell.accessoryType == UITableViewCellAccessoryNone) {
        selectedCell.accessoryType = UITableViewCellAccessoryCheckmark;
        selectedCell.backgroundView = nil;
        selectedCell.backgroundColor = [[UIColor alloc] initWithRed:((float)(153.0 / 255.0)) green:((float)(204.0 / 255.0)) blue:((float)(255.0 / 255.0)) alpha:1.0f];
        
    //}else{
      //  selectedCell.accessoryType = UITableViewCellAccessoryNone;
    //}

   /*
    ImageViewController *vc = [[ImageViewController alloc] initWithNodeObject:nil];
    
    UINavigationController *navigationController;
    navigationController = [[UINavigationController alloc] init];
    [navigationController setViewControllers:@[vc] animated:NO];
    
    [self.splitViewController showDetailViewController:navigationController sender:self];*/
}

- (void)tableView:(UITableView *)tableView didDeselectRowAtIndexPath:(NSIndexPath *)indexPath{
    UITableViewCell *selectedCell = [self.selectionEdges cellForRowAtIndexPath:indexPath];
    selectedCell.accessoryType = UITableViewCellAccessoryNone;
    if(indexPath.row % 2){
        [selectedCell setBackgroundColor:[UIColor whiteColor]];
    }
    else{
        [selectedCell setBackgroundColor:[[UIColor alloc] initWithRed:((float)(222.0 / 255.0)) green:((float)(227.0 / 255.0)) blue:((float)(231.0 / 255.0)) alpha:1.0f]];
        
    }
}

    
/*
-(NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSIndexPath *oldIndex = [self.selectionEdges indexPathForSelectedRow];
    [self.selectionEdges cellForRowAtIndexPath:oldIndex].accessoryType = UITableViewCellAccessoryNone;
    [self.selectionEdges cellForRowAtIndexPath:indexPath].accessoryType = UITableViewCellAccessoryCheckmark;
    return indexPath;
}*/

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the specified item to be editable.
    return NO;
}

@end