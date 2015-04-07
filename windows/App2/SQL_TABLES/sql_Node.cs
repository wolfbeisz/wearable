using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SQLite
{
    class sql_Node
    {
        [SQLite.PrimaryKey]
        public int NODEID { get; set; }
        public string TITLE { get; set; }
        public int? IMAGEID { get; set; }
        public int TYPEID { get; set; }
        public int VIEWID { get; set; }
        public string FORWARDTEXT { get; set; }

    }
}
