using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SQLite
{
    class sql_Type
    {
        [SQLite.PrimaryKey]
        public int TYPEID { get; set; }
        public string DESCRIPTION { get; set; }
    }
}
