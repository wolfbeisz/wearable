using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace App2.model
{
    public class Edge
    {
        public Screen From { get; private set; }
        public Screen To { get; private set; }
        public string Action { get; private set; }
        public Edge(Screen from, Screen to, string action)
        {
            this.From = from;
            this.To = to;
            this.Action = action;
        }
    }
}
