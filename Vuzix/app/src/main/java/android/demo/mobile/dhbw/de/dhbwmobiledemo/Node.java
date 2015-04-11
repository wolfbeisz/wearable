package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.database.Cursor;
import android.graphics.Picture;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Manuel on 11.04.2015.
 */
public abstract class Node {
    private static List<Node> nodesList;

    public static void init(MyDBHelper mdh) {
        try {
            int nodeId;
            String title;
            String logoId;
            HashMap<Integer, byte[]> logoBlobMap = new HashMap<>();
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
                    if(!logoBlobMap.containsKey(intLogoId)) {
                        Cursor imageCursor = mdh.executeRawQuery("SELECT IMAGE FROM IMAGE WHERE IMAGEID == '" + logoId + "';");
                        imageCursor.moveToFirst();
                        logoBlobMap.put(intLogoId, imageCursor.getBlob(0));
                        imageCursor.close();
                    }
                }



                c.moveToNext();
            }
            c.close();
        } catch (Exception e) {
            Log.e("Error", "Database not initialized");
        }
    }


    private static void addNewNode(int nodeId, String title, Picture logo, int typeId, Picture picture, String text, String forwardText) throws Exception {
        Node tmp;
        switch (typeId) {
            case 0:
                tmp = new ImageTextNode(nodeId, title, logo, picture, text, forwardText);
                break;
            case 1:
                tmp = new SingleChoiceNode(nodeId,title,logo,forwardText);
                break;
            case 2:
                tmp = new MultipleChoiceNode(nodeId,title,logo,forwardText);
                break;
            default:
                Log.e("Error", "Inconsistent Data, read error, no such node type");
                throw new Exception("Inconsistent Data, read error, no such node type");
        }
        nodesList.add(tmp);
    }

    private int nodeId;
    private String title;
    private Picture logo;
    private String forwardText;

    protected Node(int nodeId, String title, Picture logo, String forwardText) {
        this.nodeId = nodeId;
        this.title = title;
        this.logo = logo;
        this.forwardText = forwardText;
    }


}
