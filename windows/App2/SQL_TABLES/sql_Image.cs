using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SQLite.SQL_TABLES
{
    class sql_Image
    {
        [SQLite.PrimaryKey]
        public int IMAGEID { get; set; }
        public byte[] IMAGE { get; set; }
    }
}
