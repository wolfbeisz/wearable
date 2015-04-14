package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.database.Cursor;
import android.graphics.Picture;
import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Manuel on 11.04.2015.
 */
public abstract class Node {
    private static List<Node> nodesList;
    private static HashMap<Integer, byte[]> logoBlobMap = new HashMap<>();
    private static MyDBHelper mdh;

    public static byte[] getImageBlobById(int id){
        if(!logoBlobMap.containsKey(id)) {
            Cursor imageCursor = mdh.executeRawQuery("SELECT IMAGE FROM IMAGE WHERE IMAGEID == '" + id + "';");
            imageCursor.moveToFirst();
            logoBlobMap.put(id, imageCursor.getBlob(0));
            imageCursor.close();
        }
        return logoBlobMap.get(id);
    }

    public static void init(MyDBHelper mdh) {
        try {
            Node.mdh = mdh;

            int nodeId;
            String title;
            String logoId;

            int intLogoId;
            int typeId;
            int viewId;
            String forwardText;

            Cursor c = mdh.getNodes();
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                nodeId = c.getInt(0);
                title = c.getString(1);

                /*
                Get value as String to be able to recognise null values.
                 */
                logoId = c.getString(2);
                typeId = c.getInt(3);
                viewId = c.getInt(4);
                forwardText = c.getString(5);

                if(logoId != null){
                    intLogoId = Integer.parseInt(logoId);

                }

                nodesList = new LinkedList<Node>();
                Node tmp;
                switch (typeId) {
                    case 0:
                        try {
                            Cursor viewCursor = mdh.executeRawQuery("SELECT IMAGEID, TEXT FROM VIEW WHERE VIEWID = '"
                                    + viewId + "';");
                            viewCursor.moveToFirst();
                            int imageId = viewCursor.getInt(0);
                            String text = viewCursor.getString(1);

                            tmp = new ImageTextNode(nodeId, title, logoId, imageId, text, forwardText);
                            nodesList.add(tmp);
                        } catch(Exception e){
                            Log.e("Error", e.getMessage());
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        tmp = new SingleChoiceNode(nodeId,title,logoId,forwardText);
                        break;
                    case 2:
                        tmp = new MultipleChoiceNode(nodeId,title,logoId,forwardText);
                        break;
                    default:
                        Log.e("Error", "Inconsistent Data, read error, no such node type");
                        throw new Exception("Inconsistent Data, read error, no such node type");
                }



                c.moveToNext();
            }
            c.close();
        } catch (Exception e) {
            Log.e("Error", "Database not initialized");
        }
    }




    private int nodeId;
    private String title;
    private String logo;
    private String forwardText;

    protected Node(int nodeId, String title, String logo, String forwardText) {
        this.nodeId = nodeId;
        this.title = title;
        this.logo = logo;
        this.forwardText = forwardText;
    }


}
