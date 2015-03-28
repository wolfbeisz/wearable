using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.Storage;
using Windows.Storage.Pickers;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;

// Die Elementvorlage "Leere Seite" ist unter http://go.microsoft.com/fwlink/?LinkId=234238 dokumentiert.

namespace App2
{
    /// <summary>
    /// Eine leere Seite, die eigenständig verwendet werden kann oder auf die innerhalb eines Rahmens navigiert werden kann.
    /// </summary>
    public sealed partial class StartPage : Page
    {
        public StartPage()
        {
            this.InitializeComponent();
        }

        private async void Load_Database(object sender, RoutedEventArgs e)
        {
            FileOpenPicker picker = new FileOpenPicker();
            picker.FileTypeFilter.Add(".txt");
            picker.FileTypeFilter.Add(".sqlite");
            picker.SuggestedStartLocation = PickerLocationId.ComputerFolder;

            StorageFile file = await picker.PickSingleFileAsync();

            
            //enable access to the specified file using a path
            //sqlite needs to access the database by a path. Because of restrictions of WIndows 8 apps, 
            //it fails because the app is not allowed to open files except they are in specific locations.
            //solution: copy the file to a location where the app is able to access files (ApplicationData).
            var tempFolder = Windows.Storage.ApplicationData.Current.TemporaryFolder;
            var copy = await file.CopyAsync(tempFolder, file.Name, NameCollisionOption.ReplaceExisting);
            
            dao.IScreenGraphLoader loader = new dao.ScreenGraphLoader();
            model.ScreenGraph sg = loader.LoadFromFile(copy);
            service.ScreenGraphTraverser traverser = new service.ScreenGraphTraverser(sg, sg.Nodes.Find(screen => { return screen.Id == 0; }));

            service.ScreenGraphTraverser.NavigateToView(Window.Current.Content as Frame, traverser.CurrentScreen, traverser);
        }

    }
}
