//
//  DetailViewController.h
//  Wearable 1.0
//
//  Created by Michael Gutbier on 01.05.15.
//  Copyright (c) 2015 Michael Gutbier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <sqlite3.h>
@class DBController,DataTable,SingleViewController,ImageViewController;

@interface DetailViewController : UIViewController <UISplitViewControllerDelegate> {
    UIViewController* topController;
}


@property (strong, nonatomic) id detailItem;
@property (weak, nonatomic) IBOutlet UILabel *detailDescriptionLabel;
@property (weak, nonatomic) IBOutlet UITableView *formTable;
@property (strong, nonatomic) DataTable* nodeTable;
@property (strong, nonatomic) DBController* db;
//@property (strong, nonatomic) IBOutlet UITableView *tableview;
@property (weak, nonatomic) IBOutlet UITableView *tableviewNodes;
//@property(strong,nonatomic)ImageViewController *imageViewController;


@property (strong, nonatomic) DetailViewController *imageViewController;
@property (strong, nonatomic) DetailViewController *singleViewController;
@property (strong, nonatomic) DetailViewController *multipleViewController;


@end

