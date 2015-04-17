package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * Created by Silke on 14.04.2015.
 */
public class MultipleChoiceActivity extends Activity{
    private static MultipleChoiceNode node;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplechoice);
        setNode((MultipleChoiceNode)Node.getNodeById(Node.activeNode));
        setContentView(R.layout.activity_multiplechoice);
        setData();

    }

    public void setNode(MultipleChoiceNode node){
        this.node = node;
    }

    public void setData(){
        CheckBox c = (CheckBox)findViewById(R.id.checkBox);
        c.setText(node.getEdgeList().get(0).text);

        c = (CheckBox)findViewById(R.id.checkBox2);
        c.setText(node.getEdgeList().get(1).text);

        c = (CheckBox)findViewById(R.id.checkBox3);
        c.setText(node.getEdgeList().get(2).text);

        Button b = (Button) findViewById(R.id.buttonForward);
        b.setText(node.getForwardText());

    }
}
