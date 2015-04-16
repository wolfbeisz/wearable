package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Silke on 14.04.2015.
 */
public class MultipleChoiceActivity extends Activity{
    private static MultipleChoiceNode node;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplechoice);

    }

    public void setNode(MultipleChoiceNode node){
        this.node = node;
    }

    public void setData(){
        CheckBox c = (CheckBox)findViewById(R.id.checkBox);
        c.setText(node.getEdgeList().get(0).toString());

        c = (CheckBox)findViewById(R.id.checkBox2);
        c.setText(node.getEdgeList().get(1).toString());

        c = (CheckBox)findViewById(R.id.checkBox3);
        c.setText(node.getEdgeList().get(2).toString());

        Button b = (Button) findViewById(R.id.buttonForward);
        b.setText(node.getForwardText());

    }
}
