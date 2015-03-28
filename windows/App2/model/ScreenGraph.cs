using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using App2.model;

namespace App2.model
{
    public class ScreenGraph
    {

        public List<Screen> Nodes { get; private set; }
        public List<Edge> Edges { get; private set; }

        public ScreenGraph(List<Screen> nodes, List<Edge> edges)
        {
            Nodes = nodes;
            Edges = edges;
        }
    }
}
