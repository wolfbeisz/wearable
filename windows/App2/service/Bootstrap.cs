using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.Storage;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;

namespace App2.service
{
    public class Bootstrap
    {
        public async static void DisplayMockup(StorageFile file, Frame display)
        {
            //enable access to the specified file using a path
            //sqlite needs to access the database by a path. Because of restrictions of WIndows 8 apps, 
            //it fails because the app is not allowed to open files except they are in specific locations.
            //solution: copy the file to a location where the app is able to access files (ApplicationData).
            var tempFolder = Windows.Storage.ApplicationData.Current.TemporaryFolder;
            var copy = await file.CopyAsync(tempFolder, file.Name, NameCollisionOption.ReplaceExisting);

            dao.IScreenGraphLoader loader = new dao.ScreenGraphLoader();
            model.ScreenGraph sg = await loader.LoadFromFile(copy);
            service.ScreenGraphTraverser traverser = new service.ScreenGraphTraverser(sg, sg.Nodes.Find(screen => { return screen.Id == 0; }));

            service.ScreenGraphTraverser.NavigateToView(display, traverser.CurrentScreen, traverser);
        }
    }
}
