using App2.Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;
using Windows.UI;
using Windows.UI.Xaml.Media.Imaging;

namespace App2.model.viewmodel
{
    public class SelectMultipleViewModel : IBaseViewModel
    {
        public string Title { get; set; }
        public Color FontColor { get; set; }
        public BitmapSource BackgroundImage { get; set; }
        public Dictionary<string, RelayCommand> Outcomes { get; set; }
        public ICommand NextCommand { get; set; }

        public SelectMultipleViewModel()
        {
            NextCommand = new RelayCommand(() => { Next(); });
        }

        private void Next()
        {
            if (Outcomes == null)
            {
                throw new InvalidOperationException(); //TODO: actually invalid state
            }
            else if (Outcomes.Values.Count == 0)
            {
                //TODO: handle this case properly
            }
            else
            {
                var command = Outcomes.Values.First();
                command.Execute(null);
            }
        }
    }
}
