package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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

    public void setData(){
        TextView t = (TextView) findViewById(R.id.textViewText);
        t.setText();
        ImageView i = (ImageView) findViewById(R.id.imageViewImage);
        i.setImageResource();

    }

}
