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

namespace App2.model.viewmodel
{
    public interface IBaseViewModel
    {
        string Title { get; }
        Brush FontColor { get;  }
        BitmapSource BackgroundImage { get;  }
//        ObservableCollection<string> Actions { get; }
        Dictionary<string, RelayCommand> Outcomes { get; }
        ICommand NextCommand { get; }
    }
}
