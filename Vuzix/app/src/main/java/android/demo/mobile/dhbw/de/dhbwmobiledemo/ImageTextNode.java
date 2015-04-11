package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.graphics.Picture;

/**
 * Created by Manuel on 11.04.2015.
 */
public class ImageTextNode extends Node {

    private Picture picture;
    private String text;

    public ImageTextNode(int nodeId, String title, Picture logo, Picture picture, String text, String forwardText){
        super(nodeId, title, logo, forwardText);
        this.picture = picture;
        this.text = text;
    }
}
