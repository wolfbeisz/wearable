using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.UI;
using System.Collections.ObjectModel;
using Windows.UI.Xaml.Media.Imaging;
using App2.Common;
using System.Windows.Input;
using Windows.UI.Xaml.Media;
using App2.service;

namespace App2.model.viewmodel
{
    public abstract class BaseViewModel
    {
        public string Title { get; set;  }
        public Brush FontColor { get; set; }
        public BitmapSource BackgroundImage { get; set; }
        public ICommand NextCommand { get; set; }

        protected ScreenGraphTraverser screenGraphTraverser;

        protected virtual void Init()
        {
            Screen screen = screenGraphTraverser.CurrentScreen;
            Title = screen.Title;
            FontColor = new SolidColorBrush(screen.FontColor);
            BackgroundImage = screen.BackgroundImage;
        }
    }
}
