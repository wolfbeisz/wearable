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
    public class ImageViewModel : BaseViewModel
    {
        private BitmapSource image;
        public BitmapSource Image { get { return image; } set { image = value; OnPropertyChanged(new PropertyChangedEventArgs("Image")); } }
        private string text;
        public string Text { get { return text; } set { text = value; OnPropertyChanged(new PropertyChangedEventArgs("Text")); } }

        public override void Init(ScreenGraphTraverser screenGraphTraverser)
        {
            base.Init(screenGraphTraverser);
            Image = (this.screenGraphTraverser.CurrentScreen as ImageScreen).Image;
            Text = (this.screenGraphTraverser.CurrentScreen as ImageScreen).Text;
        }

        protected override void Next()
        {
            List<string> outcomes = screenGraphTraverser.getOutcomes();
            if (outcomes == null || outcomes.Count == 0 || outcomes.Count > 1)
            {
                throw new InvalidOperationException("invalid number of successors for node: NodeId=" + screenGraphTraverser.CurrentScreen.Id);
            }
            else
            {
                var command = outcomes.First();
                Screen successor = screenGraphTraverser.goToSucessor(command);
                ScreenGraphTraverser.NavigateToView(Window.Current.Content as Frame, successor, screenGraphTraverser);
            }
        }
    }
}
