//
//  MultipleViewController.h
//  Wearable 1.0
//
//  Created by Michael Gutbier on 04.05.15.
//  Copyright (c) 2015 Michael Gutbier. All rights reserved.
//


#import <UIKit/UIKit.h>
#import "DetailViewController.h"
#import <sqlite3.h>
@class DBController,DataTable;


@interface MultipleViewController : UIViewController <UISplitViewControllerDelegate> {
    UIViewController* topController;
}

- (instancetype)initWithNodeObject:(NSDictionary *)node;
- (instancetype)initWithNodeID:(int)nID andDB: (NSString*)dbName andPrev: (int) prev;

@property (strong, nonatomic) DetailViewController *multipleViewController;
@property (strong, nonatomic) DataTable* table;
@property (strong, nonatomic) DBController* db;
@property (weak, nonatomic) IBOutlet UILabel *labelTest;
@property (weak, nonatomic) IBOutlet UIButton *prevButton;
@property (weak, nonatomic) IBOutlet UIButton *nextButton;
@property (weak, nonatomic) IBOutlet UILabel *titleNode;
@property (weak, nonatomic) IBOutlet UILabel *firstEdge;
@property (weak, nonatomic) IBOutlet UILabel *secondEdge;
@property (weak, nonatomic) IBOutlet UILabel *thirdEdge;
- (IBAction)nextNav:(id)sender;
@property (weak, nonatomic) IBOutlet UITableView *selectionEdges;

@end
