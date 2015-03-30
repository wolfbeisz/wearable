using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;
using App2.model.viewmodel;
using System.Collections.ObjectModel;
using App2.service;

// Die Elementvorlage "Leere Seite" ist unter http://go.microsoft.com/fwlink/?LinkId=234238 dokumentiert.

namespace App2
{
    /// <summary>
    /// Eine leere Seite, die eigenständig verwendet werden kann oder auf die innerhalb eines Rahmens navigiert werden kann.
    /// </summary>
    public sealed partial class MultipleChoiceView : Page
    {
        public MultipleChoiceView()
        {
            this.InitializeComponent();
        }

        private SelectMultipleViewModel viewModel = new SelectMultipleViewModel();
        public SelectMultipleViewModel ViewModel { get { return viewModel; } }

        protected override void OnNavigatedTo(NavigationEventArgs e) {
            ViewModel.Init(e.Parameter as ScreenGraphTraverser);
        }
    }
}
