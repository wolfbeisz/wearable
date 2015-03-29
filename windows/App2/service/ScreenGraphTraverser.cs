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
            if (!canProceed())
                throw new InvalidOperationException();

            Edge e = graph.Edges.Find(edge => { return edge.From == CurrentScreen && edge.Action == action; });
            history.Push(CurrentScreen);
            CurrentScreen = e.To;
            return CurrentScreen;
        }

        internal bool canProceed()
        {
            return graph.Edges.Find(edge => { return edge.From == CurrentScreen; }) != null;
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
            if (!canGoBack())
                return CurrentScreen;

            CurrentScreen = history.Pop();
            return CurrentScreen;
        }

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

        internal bool canGoBack()
        {
            return history.Count > 1;
        }
    }
}
