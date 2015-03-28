using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.UI.Xaml.Media.Imaging;

namespace App2.model.viewmodel
{
    interface IImageViewModel : IBaseViewModel
    {
        /// <summary>
        /// Use BitmapSource.SetSourceAsync to load an image from a binary stream.
        /// </summary>
        BitmapSource Image { get; set; }
        string Text { get; set; }
    }
}
