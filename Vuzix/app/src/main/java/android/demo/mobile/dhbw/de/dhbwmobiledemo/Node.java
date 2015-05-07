package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Picture;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Manuel on 11.04.2015.
 */
public abstract class Node {
    private static List<Node> nodesList;
    private static HashMap<Integer, byte[]> logoBlobMap = new HashMap<>();
    private static MyDBHelper mdh;
    public static int activeNode;
    public static Stack<Integer> listOfNodesVisited;
    public static int highestNodeId = 0;
    public static int lowestNodeId = Integer.MAX_VALUE;

    public static Activity getActiveActivity() {
        return activeActivity;
    }

    public static void setActiveActivity(MainActivity activeActivity) {
        Node.activeActivity = activeActivity;
    }

    private static MainActivity activeActivity;

    public static void init(MyDBHelper mdh) {
        listOfNodesVisited = new Stack<>();

            Node.mdh = mdh;

            int nodeId;
            String title;
            String logoId;

            int intLogoId;
            int typeId;
            int viewId;
            String forwardText;
            List<Edge> edgeList;
            nodesList = new LinkedList<Node>();
            Cursor c = null;
            try {
                c = mdh.getNodes();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

                if (logoId != null) {
                    intLogoId = Integer.parseInt(logoId);
                }


                Node tmp = null;
                /*
                If typeId == 0 an additional imageId and text has to selected from another table
                 */
                if (typeId == 0) {
                    try {
                        Cursor viewCursor = mdh.executeRawQuery("SELECT IMAGEID, TEXT FROM VIEW WHERE VIEWID = '"
                                + viewId + "';");
                        viewCursor.moveToFirst();
                        int imageId = viewCursor.getInt(0);
                        String text = viewCursor.getString(1);

                        Cursor edgeCursor = mdh.executeRawQuery("SELECT SUCCESSORID FROM EDGE WHERE NODEID = '" + nodeId + "';");
                        String succIdString = null;
                        try{
                            edgeCursor.moveToFirst();
                            succIdString = edgeCursor.getString(0);
                        } catch (Exception e){

                        }
                        edgeCursor.close();

                        tmp = new ImageTextNode(nodeId, typeId, title, logoId, imageId, text, forwardText, succIdString);
                        nodesList.add(tmp);
                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    /*
                    If typeId == 1 || typeId == 2 => Code and data is in the same format.
                    Select edges from Table EDGES
                     */
                    edgeList = new LinkedList<>();
                    Cursor edgeCursor = mdh.executeRawQuery("SELECT SUCCESSORID, DESCRIPTION, EDGEID FROM EDGE WHERE NODEID = '" + nodeId + "';");
                    edgeCursor.moveToFirst();

                    while (edgeCursor.isAfterLast() == false) {
                        int succId = edgeCursor.getInt(0);
                        String edgeText = edgeCursor.getString(1);
                        int edgeId = edgeCursor.getInt(2);
                        edgeList.add(new Edge(succId, edgeText, edgeId));
                        edgeCursor.moveToNext();
                    }
                    if (typeId == 1) {
                        tmp = new SingleChoiceNode(nodeId, typeId, title, logoId, forwardText, edgeList);
                        nodesList.add(tmp);
                    } else if (typeId == 2) {
                        tmp = new MultipleChoiceNode(nodeId, typeId, title, logoId, forwardText, edgeList);
                        nodesList.add(tmp);
                    }
                }
                if(tmp != null){
                    if(tmp.nodeId > highestNodeId) {
                        highestNodeId = tmp.nodeId;
                    }
                    if(tmp.nodeId < lowestNodeId){
                        lowestNodeId = tmp.nodeId;
                    }
                }

                c.moveToNext();
            }
            c.close();
    }


    public static byte[] getImageBlobById(int id) {
        if (!logoBlobMap.containsKey(id)) {
            Cursor imageCursor = mdh.executeRawQuery("SELECT IMAGE FROM IMAGE WHERE IMAGEID = '" + id + "';");
            imageCursor.moveToFirst();
            logoBlobMap.put(id, imageCursor.getBlob(0));
            imageCursor.close();
        }
        return logoBlobMap.get(id);
    }

    public Picture getLogo() {
        if (logo != null && logo != "null") {
            return Picture.createFromStream(new ByteArrayInputStream(getImageBlobById(Integer.parseInt(logo))));
        } else {
            return null;
        }
    }

    public static Node getNodeById(int id) {
        try{
            for (Node node : nodesList) {
                if (node.getNodeId() == id) {
                    return node;
                }
            }
        }catch(java.lang.NullPointerException e) {
            return null;
        }
        return null;
    }

    /*

     */
    public int getNextNodeId(int id) {
        return this.getNodeId() + 1;
    }

    public int getNodeId() {
        return nodeId;
    }

    public String getTitle() {
        return title;
    }

    public String getForwardText() {
        return forwardText;
    }
    public int getTypeId() {
        return typeId;
    }

    public static void destroy(){
        nodesList = null;
    }

    private int nodeId;
    private String title;
    private String logo;
    private String forwardText;
    public int typeId;

    protected Node(int nodeId, int typeId, String title, String logo, String forwardText) {
        this.nodeId = nodeId;
        this.title = title;
        this.logo = logo;
        this.forwardText = forwardText;
        this.typeId = typeId;
    }





}
