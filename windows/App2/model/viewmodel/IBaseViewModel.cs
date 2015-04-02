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
using System.ComponentModel;

namespace App2.model.viewmodel
{
    public abstract class BaseViewModel : INotifyPropertyChanged
    {
        private string title = "";
        public string Title {
            get {
                return title;
            }
            set {
                title = value;
                OnPropertyChanged(new PropertyChangedEventArgs("Title"));
            } 
        }

        private string forwardText = "";
        public string ForwardText
        {
            get {
                return forwardText;
            }
            set {
                forwardText = value;
                OnPropertyChanged(new PropertyChangedEventArgs("ForwardText"));
            }
        }
        private BitmapSource backgroundImage;
        public BitmapSource BackgroundImage { get { return backgroundImage; } set { backgroundImage = value; OnPropertyChanged(new PropertyChangedEventArgs("BackgroundImage")); } }
        private ICommand nextCommand;
        public ICommand NextCommand { get { return nextCommand; } set { nextCommand = value; OnPropertyChanged(new PropertyChangedEventArgs("NextCommand")); } }
        private ICommand backCommand;
        public ICommand BackCommand { get { return backCommand; } set { backCommand = value; OnPropertyChanged(new PropertyChangedEventArgs("BackCommand")); } }

        protected ScreenGraphTraverser screenGraphTraverser;

        public virtual void Init(ScreenGraphTraverser screenGraphTraverser)
        {
            this.screenGraphTraverser = screenGraphTraverser;
            Screen screen = screenGraphTraverser.CurrentScreen;
            Title = screen.Title;
            ForwardText = screen.ForwardText;
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

        public event PropertyChangedEventHandler PropertyChanged;
        public void OnPropertyChanged(PropertyChangedEventArgs e)
        {
            var handler = PropertyChanged;
            if (handler != null)
            {
                handler(this, e);
            }
        }
    }
}
