//
//  SingleViewController.h
//  Wearable 1.0
//
//  Created by Michael Gutbier on 04.05.15.
//  Copyright (c) 2015 Michael Gutbier. All rights reserved.
//


#import <UIKit/UIKit.h>
#import "DetailViewController.h"
#import <sqlite3.h>
#import "RadioButton.h"
@class DBController,DataTable;


@interface SingleViewController : UIViewController <UISplitViewControllerDelegate> {
    UIViewController* topController;
}

- (instancetype)initWithNodeObject:(NSDictionary *)node;
- (instancetype)initWithNodeID:(int)nID andDB: (NSString*)dbName andPrev: (int) prev;

@property (strong, nonatomic) DetailViewController *singleViewController;
@property (strong, nonatomic) DataTable* table;
@property (strong, nonatomic) DBController* db;
@property (weak, nonatomic) IBOutlet UILabel *labelTest;
@property (weak, nonatomic) IBOutlet UIButton *prevButton;
@property (weak, nonatomic) IBOutlet UILabel *titleNode;
@property (weak, nonatomic) IBOutlet UILabel *firstEdge;
@property (weak, nonatomic) IBOutlet UILabel *secondEdge;
@property (weak, nonatomic) IBOutlet UILabel *thirdEdge;
@property (weak, nonatomic) IBOutlet UIButton *testNext;
@property (weak, nonatomic) IBOutlet UIButton *testNext2;
- (IBAction)nextNav:(id)sender;
- (IBAction)onRadioBtn:(id)sender;
@property (weak, nonatomic) IBOutlet RadioButton *textRadio1;
@property (weak, nonatomic) IBOutlet UIButton *nextButton;

@end
