package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.graphics.Picture;

import java.io.ByteArrayInputStream;

/**
 * Created by Manuel on 11.04.2015.
 */
public class ImageTextNode extends Node {

    private int pictureId;
    private String text;

    public Picture getPicture() {
        return Picture.createFromStream(new ByteArrayInputStream(getImageBlobById(pictureId)));
    }


    public String getText() {
        return text;
    }

    public ImageTextNode(int nodeId, int typeId, String title, String logoId, int pictureId, String text, String forwardText){
        super(nodeId, typeId, title, logoId, forwardText);

        this.pictureId = pictureId;
        this.text = text;
    }


    @Override
    public void show() {
    }
}
