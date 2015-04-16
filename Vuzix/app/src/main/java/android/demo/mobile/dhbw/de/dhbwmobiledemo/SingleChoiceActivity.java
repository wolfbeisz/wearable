package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by Silke on 14.04.2015.
 */
public class SingleChoiceActivity extends Activity{
    private static SingleChoiceNode node;
    private Edge edge;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlechoice);

        
    }


    public void setNode(SingleChoiceNode node){
        this.node = node;
    }
    public void setEdge(Edge edge){this.edge=edge;}

    public void setData(){
        RadioButton r = (RadioButton)findViewById(R.id.radioButton);
        r.setText(node.getEdgeList().get(0).toString());

        r = (RadioButton)findViewById(R.id.radioButton2);
        r.setText(node.getEdgeList().get(1).toString());

        r = (RadioButton)findViewById(R.id.radioButton3);
        r.setText(node.getEdgeList().get(2).toString());

        Button b = (Button) findViewById(R.id.buttonForward);
        b.setText(node.getForwardText());

    }
}
