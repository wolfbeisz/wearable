//
//  MasterViewController.m
//  Wearable 1.0
//
//  Created by Michael Gutbier on 01.05.15.
//  Copyright (c) 2015 Michael Gutbier. All rights reserved.
//

#import "MasterViewController.h"
#import "DetailViewController.h"

@interface MasterViewController ()

@property NSMutableArray *objects;
@end

@implementation MasterViewController

- (void)awakeFromNib {
    [super awakeFromNib];
    self.clearsSelectionOnViewWillAppear = NO;
    self.preferredContentSize = CGSizeMake(320.0, 600.0);
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    self.navigationItem.leftBarButtonItem = self.editButtonItem;
    self.objects = [self dumpDocumentsDirectoryContents];
    UIBarButtonItem *addButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(insertNewObject:)];
    self.navigationItem.rightBarButtonItem = addButton;
    self.detailViewController = (DetailViewController *)[[self.splitViewController.viewControllers lastObject] topViewController];
    // Initialize the refresh control.
    UIRefreshControl *refresh = [[UIRefreshControl alloc] init];
    refresh.attributedTitle = [[NSAttributedString alloc] initWithString:@"Refresh"];
    [refresh addTarget:self action:@selector(reloadData)
     
      forControlEvents:UIControlEventValueChanged];
    self.refreshControl = refresh;
    /*
    self.refreshControl = [[UIRefreshControl alloc] init];
    self.refreshControl.backgroundColor = [UIColor redColor];
    self.refreshControl.tintColor = [UIColor whiteColor];*/
    /*[self.refreshControl addTarget:self
                            action:@selector(getLatestLoans)
                  forControlEvents:UIControlEventValueChanged];*/

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)stopRefresh{
    [self.refreshControl endRefreshing];
}

- (void)reloadData{
    // Reload table data
    self.objects = [self dumpDocumentsDirectoryContents];
    [self.tableView reloadData];
    [self stopRefresh];
    
    // End the refreshing
    /*if (self.refreshControl) {
        
        NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
        [formatter setDateFormat:@"MMM d, h:mm a"];
        NSString *title = [NSString stringWithFormat:@"Last update: %@", [formatter stringFromDate:[NSDate date]]];
        NSDictionary *attrsDictionary = [NSDictionary dictionaryWithObject:[UIColor whiteColor]
                                                                    forKey:NSForegroundColorAttributeName];
        NSAttributedString *attributedTitle = [[NSAttributedString alloc] initWithString:title attributes:attrsDictionary];
        self.refreshControl.attributedTitle = attributedTitle;
        
        [self.refreshControl endRefreshing];
        self.objects = [self dumpDocumentsDirectoryContents];
        [self.tableView reloadData];
        
        
    }*/
}

-(NSArray *)listFileAtPath:(NSString *)path
{
    //-----> LIST ALL FILES <-----//
    NSLog(@"LISTING ALL FILES FOUND");
    
    int count;
    
    NSArray *directoryContent = [[NSFileManager defaultManager] contentsOfDirectoryAtPath:path error:NULL];
    for (count = 0; count < (int)[directoryContent count]; count++)
    {
        NSLog(@"File %d: %@", (count + 1), [directoryContent objectAtIndex:count]);
    }
    return directoryContent;
}

- (void)insertNewObject:(id)sender {
    
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Add new database"
                                                    message:@"Please enter URL"
                                                   delegate:self
                                          cancelButtonTitle:@"Cancel"
                                          otherButtonTitles:@"Add", nil];
    alert.alertViewStyle = UIAlertViewStylePlainTextInput;
    UITextField * alertTextField = [alert textFieldAtIndex:0];
    alertTextField.keyboardType = UIKeyboardTypeNumberPad;
    alertTextField.placeholder = @"Enter URL...";
    [alert show];
    //[alert release];
    
    /*
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Add new database"
                                                    message:@"this is a very important info!"
                                                   delegate:nil
                                          cancelButtonTitle:@"OK"
                                          otherButtonTitles: nil];
    [alert show];*/
    //[alert release];
    
    /*
    if (!self.objects) {
        self.objects = [[NSMutableArray alloc] init];
    }
    [self.objects insertObject:[NSDate date] atIndex:0];
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:0 inSection:0];
    [self.tableView insertRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationAutomatic];*/
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == 1) {
        NSString *nameURL = [alertView textFieldAtIndex:0].text;
        NSLog(@"Value of URL = %@", nameURL);
        if ([nameURL hasSuffix:@".sqlite"]){
            [self downloadingNewDatabase:nameURL];
        }else{
            UIAlertView *message = [[UIAlertView alloc] initWithTitle:@"Failure"
                                                            message:@"No sqlite file found!"
                                                           delegate:self
                                                  cancelButtonTitle:@"Ok"
                                                  otherButtonTitles: nil];
            [message show];
        }
        
        //[alertView dissmissWithClickedButtonIndex:1 animated:YES];
        //NSLog(@"URL - %@",name);
        // name contains the entered value
    }
}

- (NSMutableArray *)dumpDocumentsDirectoryContents {
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsPath = [paths objectAtIndex:0];
    
    NSError *error;
    NSArray *directoryContents = [[NSFileManager defaultManager] contentsOfDirectoryAtPath:documentsPath error:&error];
    
    NSLog(@"%@", directoryContents);
    [self.refreshControl endRefreshing];
    return (NSMutableArray*)directoryContents;

}


-(void) downloadingNewDatabase:(NSString*)urlSource;{
    //download the file in a seperate thread.
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        NSLog(@"Downloading Started");
        NSString *urlToDownload = urlSource;
        NSURL  *url = [NSURL URLWithString:urlToDownload];
        NSData *urlData = [NSData dataWithContentsOfURL:url];
        if ( urlData )
        {
            NSArray       *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
            NSString  *documentsDirectory = [paths objectAtIndex:0];
            
            NSString* fileName = [urlSource stringByDeletingPathExtension];
            //NSString* extension = [urlSource pathExtension];
            //NSString* theFilename = [[urlSource lastPathComponent] stringByDeletingPathExtension];
            NSString  *filePath = [NSString stringWithFormat:@"%@/%@.sqlite", documentsDirectory,fileName];
            
            //saving is done on main thread
            dispatch_async(dispatch_get_main_queue(), ^{
                [urlData writeToFile:filePath atomically:YES];
                NSLog(@"File Saved !");
            });
        }
        
    });
    self.objects = [self dumpDocumentsDirectoryContents];

    [self.tableView reloadData];
    
}


/*
- (void)startDownloadingURL:sender
{
    // Create the request.
    NSURLRequest *theRequest = [NSURLRequest requestWithURL:[NSURL URLWithString:@"http://www.apple.com"]
                                                cachePolicy:NSURLRequestUseProtocolCachePolicy
                                            timeoutInterval:60.0];
    
    // Create the connection with the request and start loading the data.
    NSURLDownload  *theDownload = [[NSURLDownload alloc] initWithRequest:theRequest
                                                                delegate:self];
    if (theDownload) {
        // Set the destination file.
        [theDownload setDestination:@"/tmp" allowOverwrite:YES];
    } else {
        // inform the user that the download failed.
    }
}


- (void)download:(NSURLDownload*)download didFailWithError:(NSError *)error
{
    // Dispose of any references to the download object
    // that your app might keep.
    
    // Inform the user.
    NSLog(@"Download failed! Error - %@ %@",
          [error localizedDescription],
          [[error userInfo] objectForKey:NSURLErrorFailingURLStringErrorKey]);
}

- (void)downloadDidFinish:(NSURLDownload *)download
{
    // Dispose of any references to the download object
    // that your app might keep.
    
    // Do something with the data.
    NSLog(@"%@",@"downloadDidFinish");
}*/


- (void)removeDB:(NSString *)fileName{
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSString *documentsPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    
    NSString *filePath = [documentsPath stringByAppendingPathComponent:fileName];
    NSError *error;
    BOOL success = [fileManager removeItemAtPath:filePath error:&error];
    if (success) {
        UIAlertView *removeSuccessFulAlert=[[UIAlertView alloc]initWithTitle:@"Remove database" message:@"Successfully removed!" delegate:self cancelButtonTitle:@"Close" otherButtonTitles:nil];
        [removeSuccessFulAlert show];
    }
    else{
        NSLog(@"Could not delete file -:%@ ",[error localizedDescription]);
    }
}


#pragma mark - Segues

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([[segue identifier] isEqualToString:@"showDetail"]) {
        NSIndexPath *indexPath = [self.tableView indexPathForSelectedRow];
        NSDate *object = self.objects[indexPath.row];
        DetailViewController *controller = (DetailViewController *)[[segue destinationViewController] topViewController];
         NSLog(@"Load new DB, name:%@ ",self.objects[indexPath.row]);
        [controller setDetailItem:object];
        controller.navigationItem.leftBarButtonItem = self.splitViewController.displayModeButtonItem;
        controller.navigationItem.leftItemsSupplementBackButton = YES;
    }
}

#pragma mark - Table View

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.objects.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"Cell" forIndexPath:indexPath];

    NSDate *object = self.objects[indexPath.row];
    cell.textLabel.text = [object description];
    return cell;
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the specified item to be editable.
    return YES;
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
        NSString *deleteFile = cell.textLabel.text;
        [self removeDB: deleteFile];
        [self.objects removeObjectAtIndex:indexPath.row];
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    } else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view.
    }
}

@end
