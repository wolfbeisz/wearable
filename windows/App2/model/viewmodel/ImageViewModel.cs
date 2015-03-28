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

namespace App2.model.viewmodel
{
    class ImageViewModel : IImageViewModel
    {
        public string Title { get; set; }
        public Color FontColor { get; set; }
        public BitmapSource BackgroundImage { get; set; }
        public Dictionary<string, RelayCommand> Outcomes { get; set;  }
        public ICommand NextCommand { get; set; }

        public BitmapSource Image { get; set; }
        public string Text { get; set; }



        public ImageViewModel()
        {
            NextCommand = new RelayCommand(() => { Next(); });
        }

        private void Next()
        {
            if (Outcomes == null || Outcomes.Values.Count == 0 || Outcomes.Values.Count > 1)
            {
                throw new InvalidOperationException(); //TODO: actually invalid state
            }

            var outcome = Outcomes.Values.First();
            outcome.Execute(null);
        }
    }
}
