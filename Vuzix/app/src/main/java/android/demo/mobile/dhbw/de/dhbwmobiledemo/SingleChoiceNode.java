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

    protected SingleChoiceNode(int nodeId, String title, String logoId, String forwardText, List<Edge> edgeList) {
        super(nodeId, title, logoId, forwardText);
        this.edgeList = edgeList;
    }
}
