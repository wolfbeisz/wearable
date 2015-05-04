package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vuzix.speech.VoiceControl;

/**
 * Created by Silke on 14.04.2015.
 */
public class ImageTextActivity extends MainActivity {

    private static ImageTextNode node;
    VoiceControl vc;

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

        Bitmap bmp = BitmapFactory.decodeByteArray(node.getByteArray(),0,node.getByteArray().length);
        ImageView i = (ImageView) findViewById(R.id.imageViewImage);
        i.setImageBitmap(bmp);

        Button b = (Button) findViewById(R.id.buttonForward);
        b.setText(node.getForwardText());
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNextNode();
            }
        });

        Button bb = (Button) findViewById(R.id.buttonBack);
        final Activity that = this;
        try {
            bb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Node.activeNode = Node.listOfNodesVisited.pop();
                    that.finish();
                }
            });
        } catch( NullPointerException e){
            //This is ok, no back button on current page
        }

    }
}
