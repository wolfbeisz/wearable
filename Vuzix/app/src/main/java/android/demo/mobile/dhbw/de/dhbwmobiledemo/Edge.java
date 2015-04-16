package android.demo.mobile.dhbw.de.dhbwmobiledemo;

/**
 * Created by Manuel on 14.04.2015.
 */
public class Edge {
    public int successor;
    public String text;
    public int edgeId;
    public Edge(int successor, String text, int edgeId) {
        this.successor = successor;
        this.text = text;
        this.edgeId = edgeId;
    }
}
