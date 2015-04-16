package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;

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
        r.setText(node.getEdgeList().get(0).text);

        r = (RadioButton)findViewById(R.id.radioButton2);
        r.setText(node.getEdgeList().get(1).text);

        r = (RadioButton)findViewById(R.id.radioButton3);
        r.setText(node.getEdgeList().get(2).text);

        Button b = (Button) findViewById(R.id.buttonForward);
        b.setText(node.getForwardText());

    }
}
