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
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;

namespace App2.model.viewmodel
{
    public abstract class BaseViewModel
    {
        public string Title { get; set;  }
        public Brush FontColor { get; set; }
        public BitmapSource BackgroundImage { get; set; }
        public ICommand NextCommand { get; set; }
        public ICommand BackCommand { get; set; }

        protected ScreenGraphTraverser screenGraphTraverser;

        protected virtual void Init()
        {
            Screen screen = screenGraphTraverser.CurrentScreen;
            Title = screen.Title;
            FontColor = new SolidColorBrush(screen.FontColor);
            BackgroundImage = screen.BackgroundImage;

            BackCommand = new RelayCommand(() => { Back(); }, () => { return screenGraphTraverser.canGoBack(); });
            NextCommand = new RelayCommand(() => { Next(); }, () => { return screenGraphTraverser.canProceed(); });
        }

        protected virtual void Back()
        {
            Screen previous = screenGraphTraverser.Back();
            ScreenGraphTraverser.NavigateToView(Window.Current.Content as Frame, previous, screenGraphTraverser);
        }

        protected abstract void Next();
    }
}
