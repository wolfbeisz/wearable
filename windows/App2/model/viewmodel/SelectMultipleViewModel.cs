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

namespace App2.model.viewmodel
{
    public class SelectMultipleViewModel : BaseViewModel
    {
        private List<string> outcomes = new List<string>();
        public List<string> Outcomes { get { return outcomes; } set { outcomes = value; OnPropertyChanged(new System.ComponentModel.PropertyChangedEventArgs("Outcomes")); } }

        public override void Init(ScreenGraphTraverser screenGraphTraverser)
        {
            base.Init(screenGraphTraverser);
            Outcomes = screenGraphTraverser.getOutcomes();
        }

        protected override void Next()
        {
            List<string> outcomes = screenGraphTraverser.getOutcomes();
            if (outcomes == null || outcomes.Count == 0)
            {
                throw new InvalidOperationException("no successor found for node: NodeId=" + screenGraphTraverser.CurrentScreen.Id);
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
