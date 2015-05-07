package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Silke on 14.04.2015.
 */
public class ImageTextActivity extends MainActivity {
    private static ImageTextNode node;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNode((ImageTextNode) Node.getNodeById(Node.activeNode));
        setContentView(R.layout.activity_imagetext);
        setData();


    }

    public void setNode(ImageTextNode node) {
        this.node = node;
    }

    public void setData() {
        TextView t = (TextView) findViewById(R.id.textViewText);
        t.setText(node.getText());

        Bitmap bmp = BitmapFactory.decodeByteArray(node.getByteArray(), 0, node.getByteArray().length);
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
        try {
            bb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } catch (NullPointerException e) {
            //This is ok, no back button on current page
        }

    }


    @Override
    protected void myOnRecognition(String result) {
        try {
            Log.i("Recognition", "Recognition: " + result);

            for (String nextWord : nextWords) {
                if (result.contains(nextWord)) {
                    Log.i("Recognition", "Button: " + result);
                    try {
                        Button b = (Button) findViewById(R.id.buttonForward);
                        if (b.isEnabled()) {
                            b.callOnClick();
                        }
                    } catch (Exception e) {

                    }
                    return;
                }
            }
            for (String backWord : backWords) {
                if (result.contains(backWord)) {
                    try {
                        Button bb = (Button) findViewById(R.id.buttonBack);
                        if (bb.getVisibility() == View.VISIBLE) {
                            bb.callOnClick();
                        }
                    } catch (NullPointerException e) {
                        //this is ok, no back button to click on current page
                    }
                }
            }


        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() {

        Log.d("Back", "ImageText, NodeId: " + Node.activeNode);
        if (!Node.listOfNodesVisited.empty()) {
            Node.activeNode = Node.listOfNodesVisited.pop();
        }
        if (vc != null) {
            vc.off();
            vc.destroy();
            vc = null;
        }
        super.onBackPressed();
    }

    @Override
    public void initializeFiles() {

    }
}
