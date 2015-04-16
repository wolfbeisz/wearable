package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import java.util.List;

/**
 * Created by Manuel on 11.04.2015.
 */
public class MultipleChoiceNode extends Node {
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
    public int getNextNodeId(int id){
        id = getEdgeList().get(0).successor;
        return id;
    }

    public List<Edge> edgeList;

    protected MultipleChoiceNode(int nodeId, String title, String logoId, String forwardText, List<Edge> edgeList) {
        super(nodeId, title, logoId, forwardText);
        this.edgeList = edgeList;
    }


}
