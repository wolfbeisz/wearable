using App2.Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;
using Windows.UI;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Media.Imaging;
using App2.service;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using System.Collections.ObjectModel;

namespace App2.model.viewmodel
{
    class SelectSingleViewModel : BaseViewModel
    {
        //TODO: by default none is selected
        public string SelectedOutcome { get; set; }
        public List<string> Outcomes { get; set; }

        public SelectSingleViewModel(ScreenGraphTraverser screenGraphTraverser)
        {
            this.screenGraphTraverser = screenGraphTraverser;
            Init();
        }

        protected override void Init()
        {
            base.Init();
            Outcomes = screenGraphTraverser.getOutcomes();
        }

        protected override void Next()
        {
            if (SelectedOutcome == null)
                return;

            List<string> outcomes = screenGraphTraverser.getOutcomes();
            if (outcomes == null || outcomes.Count == 0)
            {
                throw new InvalidOperationException("no successor found for node: NodeId=" + screenGraphTraverser.CurrentScreen.Id);
            }
            else
            {
                Screen successor = screenGraphTraverser.goToSucessor(SelectedOutcome);
                ScreenGraphTraverser.NavigateToView(Window.Current.Content as Frame, successor, screenGraphTraverser);
            }
        }
    }
}
