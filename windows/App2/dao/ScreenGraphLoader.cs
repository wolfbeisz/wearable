using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SQLite;
using App2.model;
using Windows.UI;
using Windows.UI.Xaml.Media.Imaging;
using Windows.Storage.Streams;
using SQLite.SQL_TABLES;
using App2.Common;

namespace App2.dao
{
    class ScreenGraphLoader : IScreenGraphLoader
    {
        public model.ScreenGraph LoadFromFile(Windows.Storage.StorageFile file)
        {
            SQLiteConnector s = new SQLiteConnector(file.Path);
            //SQLiteConnector s = new SQLiteConnector(null);
            List<Screen> screens = loadScreens(s);
            List<Edge> edges = loadEdges(s, screens);

            return new model.ScreenGraph(screens, edges);
        }

        private List<Edge> loadEdges(SQLiteConnector s, List<Screen> screens)
        {
            List<Edge> edges = new List<Edge>();
            foreach (sql_Edge edge in s.sql_Edges)
            {
                Screen from = screens.Find((screen) => {return screen.Id == edge.NODEID;});
                Screen to = screens.Find((screen) => {return screen.Id == edge.SUCCESSORID;});
                edges.Add(new Edge(from, to, edge.DESCRIPTION));

                //System.Diagnostics.Debug.WriteLine("edge: " + from.Id + " ; " + to.Id + " ; " + edge.DESCRIPTION);
            }
            return edges;
        }

        private List<Screen> loadScreens(SQLiteConnector s)
        {
            List<Screen> screens = new List<Screen>();
            foreach (var node in s.sql_Nodes)
            {
                Screen current = null;
                switch (node.TYPEID) {
                    case 0: //image + text
                        sql_View imageView = loadImageView(s, node.VIEWID);
                        current = new ImageScreen() { Id = node.NODEID, Title = node.TITLE, FontColor = convertColorLiteral(node.FONTCOLOR), BackgroundImage = loadImage(s, node.IMAGEID), Image = loadImage(s, imageView.IMAGEID), Text = imageView.TEXT };
                        break;
                    case 1: //single
                        current = new SingleChoiceScreen() { Id = node.NODEID, Title = node.TITLE, FontColor = convertColorLiteral(node.FONTCOLOR), BackgroundImage = loadImage(s, node.IMAGEID)};
                        break;
                    case 2: //multiple choice
                        current = new MultipleChoiceScreen() { Id = node.NODEID, Title = node.TITLE, FontColor = convertColorLiteral(node.FONTCOLOR), BackgroundImage = loadImage(s, node.IMAGEID) };
                        break;
                    default:
                        throw new InvalidOperationException("node type " + node.TYPEID + " is not supported");
                }
                screens.Add(current);
                //System.Diagnostics.Debug.WriteLine("node: " + current.Id);
            }
            return screens;
        }

        private BitmapSource loadImage(SQLiteConnector s, int p)
        {
            sql_Image imageRecord = s.sql_Images.FirstOrDefault((img) => { return img.IMAGEID == p; });
            if (imageRecord == null)
                return null;
            BitmapImage image = new BitmapImage();
            image.SetSource(new MemoryRandomAccessStream(imageRecord.IMAGE));
            return image;
        }

        private Color convertColorLiteral(string colorAsLiteral)
        {
            //throw new NotImplementedException();
            return Colors.Black;
        }

        private sql_View loadImageView(SQLiteConnector s, int viewId)
        {
            return s.sql_Views.FirstOrDefault((view) => { return view.VIEWID == viewId; });
        }
    }
}
