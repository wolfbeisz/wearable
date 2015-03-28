using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.UI.Xaml.Media.Imaging;

namespace App2.model
{
    public class ImageScreen : Screen
    {
        public BitmapSource Image { get; set; }
        public string Text { get; set; }
    }
}
