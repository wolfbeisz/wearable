//
//  DetailViewController.m
//  Wearable 1.0
//
//  Created by Michael Gutbier on 01.05.15.
//  Copyright (c) 2015 Michael Gutbier. All rights reserved.
//

#import "DetailViewController.h"
#import "DBController.h"
#import "DataTable.h"
#import "ImageViewController.h"
#import "SingleViewController.h"
#import "MultipleViewController.h"

@interface DetailViewController ()
//@property NSMutableArray *nodeTable;
@property DataTable *objectNodes;
@property (nonatomic, strong) NSArray *nodes;
@end

@implementation DetailViewController

@synthesize tableviewNodes;
@synthesize nodeTable=_nodeTable;
@synthesize db=_db;
@synthesize imageViewController=_imageViewController;


#pragma mark - Managing the detail item

- (void)setDetailItem:(id)newDetailItem {
    if (_detailItem != newDetailItem) {
        _detailItem = newDetailItem;
        
        self.db = [self.db init:newDetailItem];
        NSLog(@"New Database selected");
        // Update the view.
        [self configureView];
    }
}

- (void)configureView {
    // Update the user interface for the detail item.
    if (self.detailItem) {
        self.detailDescriptionLabel.text = [self.detailItem description];
        self.db = [DBController sharedDatabaseController:[self.detailItem description]];
            //self.db = [self.db init:[self.detailItem description]];
        
       
        NSLog(@"New Database flushed:""%@",[self.detailItem description]);
        //[self ReadeNodeTableFromDB];
        //self.objectNodes = [self setupTableviewData];
        //self.nodes = nil;
        //self.nodes = [[NSArray alloc] init];
        self.nodes = [[NSArray alloc] initWithObjects:nil];
        self.nodes = [self getNodes];
        if(self.nodes.firstObject != nil && [[[self.nodes objectAtIndex:0] objectAtIndex:0] intValue] == 0){
            int nodeType = [[[self.nodes objectAtIndex:0] objectAtIndex:3] intValue];
            NSLog(@"First Node type:""%d",nodeType);
            if(nodeType == 0){
                //Lade Image VC
                //ImageViewController *vc = [[ImageViewController alloc] initWithNodeObject:nil];
                ImageViewController *viewController = [[ImageViewController alloc] initWithNodeID:[[[self.nodes objectAtIndex:0] objectAtIndex:0] intValue] andDB: [self.detailItem description] andPrev:-1];
                
                UINavigationController *navigationController;
                id vc = self.splitViewController.viewControllers[1];
                if([vc isKindOfClass:[UINavigationController class]]){
                    navigationController = (UINavigationController *) vc;
                    [navigationController setViewControllers:@[viewController] animated:NO];
                }
                //navigationController = [[UINavigationController alloc] init];
                //[navigationController setViewControllers:@[vc] animated:NO];
                
                [self.splitViewController showDetailViewController:navigationController sender:self];
            }else{
                if(nodeType == 1){
                    //Lade Single VC
                    //SingleViewController *vc = [[SingleViewController alloc] initWithNodeObject:nil];
                    SingleViewController *viewController = [[SingleViewController alloc] initWithNodeID:[[[self.nodes objectAtIndex:0] objectAtIndex:0] intValue] andDB: [self.detailItem description] andPrev:-1];
                    
                    UINavigationController *navigationController;
                    id vc = self.splitViewController.viewControllers[1];
                    if([vc isKindOfClass:[UINavigationController class]]){
                        navigationController = (UINavigationController *) vc;
                        [navigationController setViewControllers:@[viewController] animated:NO];
                    }
                    //navigationController = [[UINavigationController alloc] init];
                    //[navigationController setViewControllers:@[vc] animated:NO];
                    
                    [self.splitViewController showDetailViewController:navigationController sender:self];
                }else{
                    if(nodeType == 2){
                        //Lade Multiple VC
                        //MultipleViewController *vc = [[MultipleViewController alloc] initWithNodeObject:nil];
                        MultipleViewController *viewController = [[MultipleViewController alloc] initWithNodeID:[[[self.nodes objectAtIndex:0] objectAtIndex:0] intValue] andDB: [self.detailItem description] andPrev:-1];
                        
                        UINavigationController *navigationController;
                        id vc = self.splitViewController.viewControllers[1];
                        if([vc isKindOfClass:[UINavigationController class]]){
                            navigationController = (UINavigationController *) vc;
                            [navigationController setViewControllers:@[viewController] animated:NO];
                        }
                        //navigationController = [[UINavigationController alloc] init];
                        //[navigationController setViewControllers:@[vc] animated:NO];
                        
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
            
      /*
            
            ImageViewController *vc = [[ImageViewController alloc] initWithNodeObject:nil];
            
            UINavigationController *navigationController;
            navigationController = [[UINavigationController alloc] init];
            [navigationController setViewControllers:@[vc] animated:NO];
            
            [self.splitViewController showDetailViewController:navigationController sender:self];
       */

            
        }else{
            NSLog(@"Error2");
            UIAlertView *message = [[UIAlertView alloc] initWithTitle:@"Failure"
                                                              message:@"Faulty database!"
                                                             delegate:self
                                                    cancelButtonTitle:@"Ok"
                                                    otherButtonTitles: nil];
            [message show];
        }
        
        
        /*
        self.imageViewController =
        [[ImageViewController alloc] initWithNibName:@"ImageViewController"
                                              bundle:nil];
        [self presentViewController:self.imageViewController animated:YES completion:nil];*/
        /*[self.navigationController pushViewController:secondViewController animated:YES];
        [self.splitViewController showDetailViewController:_imageViewController animated: YES]*/
        
        //[self.splitViewController showDetailViewController:self.imageViewController animated:YES];

        [tableviewNodes reloadData];
        //SingleViewController* svc;
        //[self.splitViewController performSelector:@selector(toggleMasterVisible:)];
        //[self.splitViewController showDetailViewController:svc animated:YES];
    }
}

/*-(IBAction)buttonClicked:(id)sender{
    self.imageViewController =
    [[ImageViewController alloc] initWithNibName:@"ImageViewController"
                                           bundle:nil];
    [self presentViewController:self.imageViewController animated:YES completion:nil];
    
}*/

- (void)viewDidLoad {
    [super viewDidLoad];
        self.navigationItem.title = @"No Selection";
    //self.objects = [self fetchTableNames: [self.detailItem description]];
    //UITableView *tableView = [[UITableView alloc] initWithFrame:[[UIScreen mainScreen] applicationFrame] style:UITableViewStylePlain];
    /*tableviewNodes.autoresizingMask =
    UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleWidth;
    tableviewNodes.delegate = self.tableviewNodes.delegate;
    tableviewNodes.dataSource = self.tableviewNodes.dataSource;*/
    //[_tableview reloadData];
    //self.view = tableView;
    //self.objectNodes = [self setupTableviewData];
    //tableviewNodes.delegate = (id)self;
    //tableviewNodes.dataSource = (id)self;
//NSLog(@"%@,%@",tableviewNodes.delegate, tableviewNodes.dataSource);
    
        //OPEN FIRST VIEW.
    //self.db = [DBController sharedDatabaseController];
    
    // Do any additional setup after loading the view, typically from a nib.
    [self configureView];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(NSArray *)getNodes{
    NSLog(@"Insert values in node array.");
    DataTable *table = nil;
    table = [self.db  ExecuteQuery:@"SELECT NODEID, TITLE, IMAGEID, TYPEID, VIEWID, FORWARDTEXT FROM NODE ORDER BY NODEID"];
    return table.rows;
    //return NULL;
}

-(NSArray *)getEdgesForNode:(int) nodeID{
    NSLog(@"Insert values in edges array.");
    DataTable *table = nil;
    NSString *edgeQuery = [NSString stringWithFormat: @"SELECT NODEID, SUCCESSORID, DESCRIPTION, EDGEID FROM EDGE WHERE NODEID = %d ORDER BY EDGEID", nodeID];
    table = [self.db  ExecuteQuery:edgeQuery];
    return table.rows;
    //return NULL;
}

-(void)ReadeNodeTableFromDB
{
    
    NSLog(@"Reading node table.");
    
    DataTable* table = [_db  ExecuteQuery:@"SELECT NODEID, TITLE FROM NODE"];
    
    NSLog(@"Columns in query");
    for (NSString* colName in table.columns)
    {
        NSLog(@"%@",colName);
    }
    
    
    for (NSArray* row in table.rows)
    {
        for(NSString* column in table.columns)
        {
            NSLog(@"row:%lu %@=%@",
                  [table.rows indexOfObject:row]+1, /*zero based*/
                  column,
                  row[[table colIndex:column]]);
        }
    }
    
    
}

-(void)ReadeNodeTableFromDB_old
{
    
    NSLog(@"Reading node table.");
    
    DataTable* table = [_db  ExecuteQuery:@"SELECT NODEID, TITLE FROM NODE"];
    
    NSString* NodeID =   table.rows[1][0]; // row 2 col 1 - zero based - if earlier than xcode 4.5 change to [table.rows[1] objectAtIndex:0]
    NSString* Title =    table.rows[1][1]; // row 2 col 2
    
    NSLog(@"row:2 %@ %@",
          NodeID,
          Title);
    
    
    
}

/*
-(NSMutableArray *)fetchTableNames:(NSString*)db_name{
    sqlite3_stmt* statement;
    NSString *query = @"SELECT * FROM sqlite_master where type=\'table\'";
    int retVal = sqlite3_prepare_v2((__bridge sqlite3 *)(db_name),
                                    [query UTF8String],
                                    -1,
                                    &statement,
                                    NULL);
    
    NSMutableArray *selectedRecords = [NSMutableArray array];
    if ( retVal == SQLITE_OK )
    {
        while(sqlite3_step(statement) == SQLITE_ROW )
        {
            NSString *value = [NSString stringWithCString:(const char *)sqlite3_column_text(statement, 0)
                                                 encoding:NSUTF8StringEncoding];
            [selectedRecords addObject:value];
        }
    }
    
    sqlite3_clear_bindings(statement);
    sqlite3_finalize(statement);
    
    return selectedRecords;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"Cell" forIndexPath:indexPath];
    
    NSDate *object = self.objects[indexPath.row];
    cell.textLabel.text = [object description];
    return cell;
}*/

#pragma mark - Helper Functions
-(DataTable *)setupTableviewData {
    
    DataTable *query = [_db  ExecuteQuery:@"SELECT NODEID, TITLE FROM NODE"];
    return query;
}



- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.objectNodes.rows.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier] ;
        
    }
    
    NSArray* row= self.objectNodes.rows[indexPath.row];

    cell.textLabel.text = row[[self.objectNodes colIndex:@"TITLE"]];
    
    cell.detailTextLabel.text = [NSString stringWithFormat:@"%@",row[[self.objectNodes colIndex:@"NODEID"]]];
    
    return cell;
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSLog(@"joo");
        
        ImageViewController *vc = [[ImageViewController alloc] initWithNodeObject:nil];
    
        UINavigationController *navigationController;
        navigationController = [[UINavigationController alloc] init];
        [navigationController setViewControllers:@[vc] animated:NO];

    [self.splitViewController showDetailViewController:navigationController sender:self];
}


- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the specified item to be editable.
    return NO;
}

@end
