package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import java.util.List;

/**
 * Created by Manuel on 11.04.2015.
 */
public class SingleChoiceNode extends Node {
    private List<Edge> edgeList;

    public List<Edge> getEdgeList() {
        return edgeList;
    }

    public Edge getEdgeById(int id){
        for(Edge edge : edgeList){
            if(edge.edgeId == id){
                return edge;
            }
        }
        return null;
    }

    @Override
    public void show() {

    }

    /*
        @param edgeId
         */
    @Override
    public int getNextNodeId(int id){
        return getNextNodeIdByEdgeId(id);
    }

    public int getNextNodeIdByEdgeId(int id){
        return getEdgeById(id).successor;
    }

    public int getNextNodeIdByEdgeNr(int id){
        List<Edge> el = getEdgeList();
        Edge e = el.get(id);
        return e.successor;
    }



    protected SingleChoiceNode(int nodeId, int typeId, String title, String logoId, String forwardText, List<Edge> edgeList) {
        super(nodeId, typeId, title, logoId, forwardText);
        this.edgeList = edgeList;
    }
}
