package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.graphics.Picture;

import java.io.ByteArrayInputStream;

/**
 * Created by Manuel on 11.04.2015.
 */
public class ImageTextNode extends Node {

    private int pictureId;
    private String text;
    private String succId;

    public Picture getPicture() {
        return Picture.createFromStream(new ByteArrayInputStream(getImageBlobById(pictureId)));
    }

    public byte[] getByteArray() {
        return getImageBlobById(pictureId);
    }

    public String getText() {
        return text;
    }

    public ImageTextNode(int nodeId, int typeId, String title, String logoId, int pictureId, String text, String forwardText, String succId){
        super(nodeId, typeId, title, logoId, forwardText);

        this.pictureId = pictureId;
        this.text = text;
        this.succId = succId;
    }

    @Override
    public int getNextNodeId(int id){
        if(succId != null) {
            return Integer.parseInt(succId);
        } else {
            return ++Node.activeNode;
        }
    }



    @Override
    public void show() {
    }
}
