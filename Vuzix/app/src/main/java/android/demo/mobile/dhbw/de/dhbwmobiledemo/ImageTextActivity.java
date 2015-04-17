package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
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
        setNode((ImageTextNode)Node.getNodeById(Node.activeNode));
        setContentView(R.layout.activity_imagetext);
        setData();
    }

    public void setNode(ImageTextNode node){
        this.node = node;
    }

    public void setData(){
        TextView t = (TextView) findViewById(R.id.textViewText);
        t.setText(node.getText());
        ImageView i = (ImageView) findViewById(R.id.imageViewImage);
        //i.setImageResource();

        Button b = (Button) findViewById(R.id.buttonForward);
        b.setText(node.getForwardText());
    }
}
