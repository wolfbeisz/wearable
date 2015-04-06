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
using System.Globalization;
using Windows.UI.Xaml.Media;
using System.Runtime.InteropServices.WindowsRuntime;

namespace App2.dao
{
    class ScreenGraphLoader : IScreenGraphLoader
    {
        public async Task<model.ScreenGraph> LoadFromFile(Windows.Storage.StorageFile file)
        {
            SQLiteConnector s = new SQLiteConnector(file.Path);
            //SQLiteConnector s = new SQLiteConnector(null);
            List<Screen> screens = await loadScreens(s);
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

        private async Task<List<Screen>> loadScreens(SQLiteConnector s)
        {
            List<Screen> screens = new List<Screen>();
            foreach (var node in s.sql_Nodes)
            {
                Screen current = null;
                switch (node.TYPEID) {
                    case 0: //image + text
                        sql_View imageView = loadImageView(s, node.VIEWID);
                        current = new ImageScreen() { Id = node.NODEID, Title = node.TITLE, ForwardText = node.FORWARDTEXT, BackgroundImage = await loadImage(s, node.IMAGEID), Image = await loadImage(s, imageView.IMAGEID), Text = imageView.TEXT };
                        break;
                    case 1: //single
                        current = new SingleChoiceScreen() { Id = node.NODEID, Title = node.TITLE, ForwardText = node.FORWARDTEXT, BackgroundImage = await loadImage(s, node.IMAGEID) };
                        break;
                    case 2: //multiple choice
                        current = new MultipleChoiceScreen() { Id = node.NODEID, Title = node.TITLE, ForwardText = node.FORWARDTEXT, BackgroundImage = await loadImage(s, node.IMAGEID) };
                        break;
                    default:
                        throw new InvalidOperationException("node type " + node.TYPEID + " is not supported");
                }
                screens.Add(current);
                //System.Diagnostics.Debug.WriteLine("node: " + current.Id);
            }
            return screens;
        }

        private async Task<BitmapSource> loadImage(SQLiteConnector s, int p)
        {
            foreach (sql_Image img in s.sql_Images)
            {
                if (img.IMAGEID == p) {
                    IBuffer buffer = img.IMAGE.AsBuffer();
                    var stream = new InMemoryRandomAccessStream();
                    await stream.WriteAsync(buffer);
                    stream.Seek(0);
                    BitmapImage image = new BitmapImage();
                    image.SetSource(stream);
                    return image;
                }
            }
            return null;
        }

        /*private Brush getBrush(string colorAsLiteral)
        {
            if (colorAsLiteral != null)
            {
                return new SolidColorBrush(convertColorLiteral(colorAsLiteral));
            }
            return new SolidColorBrush(Colors.Black);
        }*/

        private Color convertColorLiteral(string colorAsLiteral)
        {
            if (colorAsLiteral != null) 
            {
                Color color = new Color();
                color.R = byte.Parse(colorAsLiteral.Substring(1, 2), NumberStyles.AllowHexSpecifier);
                color.G = byte.Parse(colorAsLiteral.Substring(3, 2), NumberStyles.AllowHexSpecifier);
                color.B = byte.Parse(colorAsLiteral.Substring(5, 2), NumberStyles.AllowHexSpecifier);
                color.A = (byte)255;
                return color;
            }
            return Colors.Black;
            
        }

        private sql_View loadImageView(SQLiteConnector s, int viewId)
        {
            return s.sql_Views.FirstOrDefault((view) => { return view.VIEWID == viewId; });
        }
    }
}
