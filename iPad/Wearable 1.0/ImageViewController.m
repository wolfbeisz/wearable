//
//  ImageViewController.m
//  Wearable 1.0
//
//  Created by Michael Gutbier on 04.05.15.
//  Copyright (c) 2015 Michael Gutbier. All rights reserved.
//

#import "ImageViewController.h"
#import "DBController.h"
#import "DataTable.h"
#import "ImageViewController.h"
#import "SingleViewController.h"
#import "MultipleViewController.h"
#import <SQLite3.h>

@interface ImageViewController ()
@property (strong, nonatomic) UIPopoverController *masterPopoverController;

@property (nonatomic, strong) NSDictionary *dictionary;
@property (nonatomic, strong) NSArray *edges;
@property (nonatomic, strong) NSArray *nodeDetails;
@property (nonatomic, strong) NSArray *viewDetails;
@property (nonatomic, strong) NSString *db_name;
@property int nodeID, prevNode;


@end

@implementation ImageViewController

@synthesize labelTest;
@synthesize table;
@synthesize db;
@synthesize imageText;
@synthesize imgSource;

- (instancetype)initWithNodeObject:(NSDictionary *)node {
    
    self = [super init];
    if (self) {
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle: nil];
        self = [storyboard instantiateViewControllerWithIdentifier:@"BildText"];
        
        self.dictionary = node;
    }
    return self;
}

- (instancetype)initWithNodeID:(int) nID andDB: (NSString*) dbName andPrev:(int)prev {
    
    self = [super init];
    if (self) {
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle: nil];
        self = [storyboard instantiateViewControllerWithIdentifier:@"BildText"];
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

-(NSString *)getTextForImage{
    DataTable *tableTemp = nil;
    NSString *tempQuery = [NSString stringWithFormat: @"SELECT IMAGEID, TEXT FROM VIEW WHERE VIEWID = %d", [[[self.nodeDetails objectAtIndex:0] objectAtIndex:2] intValue]];
    tableTemp = [self.db  ExecuteQuery:tempQuery];
    self.viewDetails = tableTemp.rows;
    return [[tableTemp.rows objectAtIndex:0] objectAtIndex:1];
}

-(UIImage*) getImageFromDB{
    
    
    DataTable *tableTemp = nil;
    NSString *query = [NSString stringWithFormat: @"SELECT IMAGE FROM IMAGE WHERE IMAGEID = %d", [[[self.viewDetails objectAtIndex:0] objectAtIndex:0] intValue]];
    tableTemp = [self.db  ExecuteQuery:query];
        UIImage *img = nil;
    if(tableTemp.rows.count>0){
        NSData *imgData = [[tableTemp.rows objectAtIndex:0] objectAtIndex:0];
        img = [[UIImage alloc] initWithData:imgData];
    }else{

    }
    
    return img;

}

- (void)showImage{
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
        NSString *insertSQL = [NSString stringWithFormat:@"SELECT IMAGE FROM IMAGE WHERE IMAGEID = %d",[[[self.viewDetails objectAtIndex:0] objectAtIndex:0] intValue]];
        if(sqlite3_prepare_v2(dataBase,[insertSQL cStringUsingEncoding:NSUTF8StringEncoding], -1, &compiledStmt, NULL) == SQLITE_OK) {
            while(sqlite3_step(compiledStmt) == SQLITE_ROW) {
                
                int length = sqlite3_column_bytes(compiledStmt, 0);
                NSData *imageData = [NSData dataWithBytes:sqlite3_column_blob(compiledStmt, 0) length:length];
                
                NSLog(@"Length : %lu", (unsigned long)[imageData length]);
                
                if(imageData == nil)
                    NSLog(@"No image found.");
                else
                    self.imgSource.image = [UIImage imageWithData:imageData];
            }
        }
        sqlite3_finalize(compiledStmt);

    } else {
        NSLog(@"Failed to open database at %@ with error %s", databasePath, sqlite3_errmsg(dataBase));
        sqlite3_close (dataBase);
    }
    
    /*
    
    if(sqlite3_open([path UTF8String], &dataBase)==SQLITE_OK){
        NSLog(@"DB Open and read Image for ID: %d", [[[self.viewDetails objectAtIndex:0] objectAtIndex:0] intValue]);
        NSString *insertSQL = [NSString stringWithFormat:@"SELECT IMAGE FROM IMAGE WHERE IMAGEID = %d",[[[self.viewDetails objectAtIndex:0] objectAtIndex:0] intValue]];
        if(sqlite3_prepare_v2(dataBase,[insertSQL cStringUsingEncoding:NSUTF8StringEncoding], -1, &compiledStmt, NULL) == SQLITE_OK) {
            while(sqlite3_step(compiledStmt) == SQLITE_ROW) {
                
                int length = sqlite3_column_bytes(compiledStmt, 0);
                NSData *imageData = [NSData dataWithBytes:sqlite3_column_blob(compiledStmt, 0) length:length];
                
                NSLog(@"Length : %lu", (unsigned long)[imageData length]);
                
                if(imageData == nil)
                    NSLog(@"No image found.");
                else
                    self.imgSource.image = [UIImage imageWithData:imageData];
            }
        }
        sqlite3_finalize(compiledStmt);
    }
    sqlite3_close(dataBase);*/
}


/*
-(NSData *)getImageSource{
    
    
    NSData *data = [[NSData alloc] initWithBytes:sqlite3_column_blob(init_statement, 6) length:sqlite3_column_bytes(init_statement, 6)];
    return data;
    //if(data == nil)
      //  NSLog(@"No image found.");
    //else
        //self.pictureImage = [UIImage imageWithData:data];
    
    
    DataTable *tableTemp = nil;
    NSString *tempQuery = [NSString stringWithFormat: @"SELECT IMAGEID, TEXT FROM VIEW WHERE VIEWID = %d", [[[self.nodeDetails objectAtIndex:0] objectAtIndex:2] intValue]];
    tableTemp = [self.db  ExecuteQuery:tempQuery];
    return [[tableTemp.rows objectAtIndex:0] objectAtIndex:1];
}*/

-(int)getSuccessorForEdge:(int) edgeID{
    return [[[self.edges objectAtIndex:edgeID] objectAtIndex:1] intValue];
}

-(void)showNextNode{
    [self createNewView:[self getSuccessorForEdge: [[[self.edges objectAtIndex:0] objectAtIndex:0] intValue]]];
}
-(void)getBack{
    [self createNewView:self.prevNode];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    SEL aSelector = @selector(showNextNode);
    SEL bSelector = @selector(getBack);
    self.navigationItem.title = [[self.nodeDetails objectAtIndex:0] objectAtIndex:0];
    if (![[[self.nodeDetails objectAtIndex:0] objectAtIndex:3]  isEqual: @"Not shown"]){
        UIBarButtonItem *anotherButton = [[UIBarButtonItem alloc] initWithTitle:[[self.nodeDetails objectAtIndex:0] objectAtIndex:3] style:UIBarButtonItemStylePlain
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
    
    NSString* string = @"Image: ";
    
    for(int i=0; i<[self.edges count]; i++){
        string = [string stringByAppendingString:[[self.edges objectAtIndex:i] objectAtIndex:2]];
    }
    labelTest.text = string;
    imageText.text = [self getTextForImage];
    [self showImage];
    //imgSource.image = [self getImageFromDB];

    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)showView{
    UIViewController *viewController;
    viewController = self.imageViewController;
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
@end