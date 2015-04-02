using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Windows.UI;
using Windows.UI.Xaml.Media.Imaging;

namespace App2.model
{
    // create subclasses for the corresponding Views (Image, SingleChoice, MultipleChoice)
    public class Screen
    {
        public int Id { get;  set; }
        public string Title { get;  set; }
        public string ForwardText { get; set; }
        public BitmapSource BackgroundImage { get;  set; }
    }
}
