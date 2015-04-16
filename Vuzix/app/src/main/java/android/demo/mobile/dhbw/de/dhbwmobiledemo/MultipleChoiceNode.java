package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.graphics.Picture;

import java.util.List;

/**
 * Created by Manuel on 11.04.2015.
 */
public class MultipleChoiceNode extends Node {
    public List<Edge> edgeList;

    protected MultipleChoiceNode(int nodeId, String title, String logoId, String forwardText, List<Edge> edgeList) {
        super(nodeId, title, logoId, forwardText);
        this.edgeList = edgeList;
    }


}
