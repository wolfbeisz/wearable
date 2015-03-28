using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using App2.model;
using Windows.UI.Xaml.Media.Imaging;

namespace App2.dao
{
    public class DummyScreenGraphLoader : IScreenGraphLoader
    {
        public ScreenGraph LoadFromFile(Windows.Storage.StorageFile file)
        {
            List<Screen> screens = new List<Screen>();
            BitmapImage image = new BitmapImage();
            image.UriSource = new Uri("https://www.google.de/images/srpr/logo11w.png");

            var screen1 = new ImageScreen() { Id = 0, Title = "Image With Text", Text = "this text should be shown below the displayed image", Image = image };
            //var screen1 = new SingleChoiceScreen() { Id = 0, Title = "Single Choice", Options = new List<string>() { "Cobol", "Pascal", "Java", "F#" } };
            //var screen1 = new MultipleChoiceScreen() { Id = 0, Title = "Hello World2", Options = new List<string>() { "G", "H", "C", "D" } };
            var screen2 = new SingleChoiceScreen() { Id = 1, Title = "Wunschliste"};
            var screen3 = new MultipleChoiceScreen() { Id = 2, Title = "Einkaufsliste" };
            var iMacDisplay = new ImageScreen() { Id = 3, Title = "Der neue iMac", Text = "Das ist der neue iMac.", Image = new BitmapImage() { UriSource = new Uri("https://www.apple.com/de/imac/static/index/hero/hero.jpg")} };
            screens.Add(screen1);
            screens.Add(screen2);
            screens.Add(screen3);
            screens.Add(iMacDisplay);
            List<Edge> actions = new List<Edge>();
            actions.Add(new Edge(screen1, screen2, "does not matter"));

            actions.Add(new Edge(screen2, iMacDisplay, "iMac"));

            actions.Add(new Edge(screen2, screen3, "iPhone"));
            actions.Add(new Edge(screen2, screen3, "Apple Watch"));
            actions.Add(new Edge(screen2, screen3, "Mac"));
            actions.Add(new Edge(screen2, screen3, "MacBook"));
            actions.Add(new Edge(screen2, screen3, "MacBook Air"));
            actions.Add(new Edge(screen2, screen3, "iPad"));

            actions.Add(new Edge(screen3, screen1, "Fleisch"));
            actions.Add(new Edge(screen3, screen1, "Brot"));
            actions.Add(new Edge(screen3, screen1, "Cola"));

            actions.Add(new Edge(iMacDisplay, screen1, "does not matter"));
            return new ScreenGraph(screens, actions);
        }
    }
}
