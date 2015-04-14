package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Silke on 14.04.2015.
 */
public class ImageTextActivity extends Activity {

    private static ImageTextNode node;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagetext);
    }

    public void setNode(ImageTextNode node){
        this.node = node;

    }
}
