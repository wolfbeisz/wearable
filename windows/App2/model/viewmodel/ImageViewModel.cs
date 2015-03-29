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
    class ImageViewModel : BaseViewModel
    {
        public BitmapSource Image { get; set; }
        public string Text { get; set; }

        public ImageViewModel(ScreenGraphTraverser screenGraphTraverser)
        {
            this.screenGraphTraverser = screenGraphTraverser;
            Init();
            NextCommand = new RelayCommand(() => { Next(); });
        }

        protected override void Init()
        {
            base.Init();
            Image = (this.screenGraphTraverser.CurrentScreen as ImageScreen).Image;
            Text = (this.screenGraphTraverser.CurrentScreen as ImageScreen).Text;
        }

        private void Next()
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
