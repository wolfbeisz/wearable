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
using System.ComponentModel;

namespace App2.model.viewmodel
{
    public class SelectSingleViewModel : BaseViewModel
    {
        //TODO: by default none is selected
        private string selectedOutcome;
        public string SelectedOutcome { get { return selectedOutcome; } set { selectedOutcome = value; OnPropertyChanged(new PropertyChangedEventArgs("SelectedOutcome")); } }
        private List<string> outcomes = new List<string>();
        public List<string> Outcomes { get { return outcomes; } set { outcomes = value; OnPropertyChanged(new System.ComponentModel.PropertyChangedEventArgs("Outcomes")); } }

        public override void Init(ScreenGraphTraverser screenGraphTraverser)
        {
            base.Init(screenGraphTraverser);
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
