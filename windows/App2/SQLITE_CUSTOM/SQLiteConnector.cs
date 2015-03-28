using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using SQLite.SQL_TABLES;

namespace SQLite
{
    class SQLiteConnector
    {
        private String path;
        public IEnumerable<sql_Edge> sql_Edges;
        public IEnumerable<sql_Node> sql_Nodes;
        public IEnumerable<sql_View> sql_Views;
        public IEnumerable<sql_Type> sql_Types;
        public IEnumerable<sql_Image> sql_Images;

        public SQLiteConnector(string _path)
        {
            if (_path == null)
            {
                this.path = Path.Combine(
                    Windows.ApplicationModel.Package.Current.InstalledLocation.Path, @"Assets\a.sqlite");
            }
            else
                this.path = _path;

            getEverything();

        }
        public void getEverything() {

            var db = new SQLite.SQLiteConnection(path);

            sql_Edges = db.Query<sql_Edge>("SELECT * FROM EDGE");
            sql_Images = db.Query<sql_Image>("SELECT * FROM IMAGE");
            sql_Types = db.Query<sql_Type>("SELECT * FROM TYPE");
            sql_Views = db.Query<sql_View>("SELECT * FROM VIEW");
            sql_Nodes = db.Query<sql_Node>("SELECT * FROM NODE");

        }
    }
}
