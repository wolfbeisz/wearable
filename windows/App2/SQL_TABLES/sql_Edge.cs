using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SQLite
{
    public class sql_Edge
    {
        public int NODEID { get; set; }
        public int SUCCESSORID { get; set; }
        public string DESCRIPTION { get; set; }
        [SQLite.PrimaryKey]
        public int EDGEID { get; set; }
    }
}
