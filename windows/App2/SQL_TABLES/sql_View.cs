using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SQLite.SQL_TABLES
{
    class sql_View
    {
        [SQLite.PrimaryKey]
        public int VIEWID { get; set; }
        public int IMAGEID { get; set; }
        public string TEXT { get; set; }
    }
}
