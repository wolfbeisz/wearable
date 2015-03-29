using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.UI;
using System.Collections.ObjectModel;
using Windows.UI.Xaml.Media.Imaging;
using App2.model.viewmodel;
using App2.model;
using App2.Common;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Media;


namespace App2.service
{
    /// <summary>
    /// Navigates within a Screengraph.
    /// The state contains the currently selected screen of a Screengraph.
    /// </summary>
    public class ScreenGraphTraverser
    {
        private ScreenGraph graph;
        public Screen CurrentScreen { get; private set; }
        private Stack<Screen> history = new Stack<Screen>();

        public ScreenGraphTraverser(ScreenGraph graph, Screen startScreen)
        {
            this.graph = graph;
            this.CurrentScreen = startScreen; //TODO: check whether startScreen is present in graph
            this.history.Push(startScreen);
        }

        public Screen goToSucessor(string action) 
        {
            Edge e = graph.Edges.Find(edge => { return edge.From == CurrentScreen && edge.Action == action; });
            history.Push(CurrentScreen);
            CurrentScreen = e.To;
            return CurrentScreen;
        }

        public List<string> getOutcomes()
        {
            List<string> outcomes = new List<string>();
            List<Edge> outgoingEdges = graph.Edges.FindAll(edge => { return edge.From == CurrentScreen; });
            foreach (Edge e in outgoingEdges)
            {
                outcomes.Add(e.Action);
            }
            return outcomes;
        }

        public Screen Back()
        {
            if (history.Count == 1)
                return CurrentScreen;

            CurrentScreen = history.Pop();
            return CurrentScreen;
        }

        /*
        internal SelectMultipleViewModel getMultipleChoiceViewModel()
        {
            if (!(CurrentScreen is MultipleChoiceScreen))
                throw new InvalidOperationException();

            MultipleChoiceScreen screen = CurrentScreen as MultipleChoiceScreen;
            var vm = new SelectMultipleViewModel();
            vm.Title = screen.Title;
            vm.FontColor = new SolidColorBrush(screen.FontColor);
            vm.Outcomes = buildOutcomes();
            return vm;
        }

        internal SelectSingleViewModel getSingleChoiceViewModel()
        {
            if (!(CurrentScreen is SingleChoiceScreen))
                throw new InvalidOperationException();

            SingleChoiceScreen screen = CurrentScreen as SingleChoiceScreen;
            var vm = new SelectSingleViewModel();
            vm.Title = screen.Title;
            vm.FontColor = new SolidColorBrush(screen.FontColor);
            vm.Outcomes = buildOutcomes();
            return vm;
        }

        internal IImageViewModel getImageViewModel()
        {
            if (!(CurrentScreen is ImageScreen))
                throw new InvalidOperationException();

            ImageScreen screen = CurrentScreen as ImageScreen;
            var vm = new ImageViewModel();
            vm.Title = screen.Title;
            vm.FontColor = new SolidColorBrush(screen.FontColor);
            vm.Image = screen.Image;
            vm.Text = screen.Text;
            vm.Outcomes = buildOutcomes();
            return vm;
        }

        private Dictionary<string, RelayCommand> buildOutcomes()
        {
            Dictionary<string, RelayCommand> actions = new Dictionary<string, RelayCommand>();
            List<Edge> outgoingEdges = graph.Edges.FindAll(edge => { return edge.From == CurrentScreen; });
            foreach (Edge e in outgoingEdges)
            {
                RelayCommand command = new RelayCommand(
                    () => {
                        this.CurrentScreen = e.To;
                        Frame current = Window.Current.Content as Frame;
                        ScreenGraphTraverser.NavigateToView(current, e.To, this);
                    });
                actions.Add(e.Action, command);
            }

            return actions;
        }*/

        public static void NavigateToView(Frame frame, Screen screen, ScreenGraphTraverser traverser)
        {
            if (screen is ImageScreen)
                frame.Navigate(typeof(ImageView), traverser);
            else if (screen is SingleChoiceScreen)
                frame.Navigate(typeof(SingleChoiceView), traverser);
            else if (screen is MultipleChoiceScreen)
                frame.Navigate(typeof(MultipleChoiceView), traverser);
            else
                throw new InvalidOperationException();
        }
    }
}
