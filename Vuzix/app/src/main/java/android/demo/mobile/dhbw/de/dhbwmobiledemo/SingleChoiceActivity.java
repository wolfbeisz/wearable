package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

/**
 * Created by Silke on 14.04.2015.
 */
public class SingleChoiceActivity extends MainActivity{
    private static SingleChoiceNode node;
    private Edge edge;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlechoice);
        setNode((SingleChoiceNode)Node.getNodeById(Node.activeNode));
        setContentView(R.layout.activity_singlechoice);
        setData();
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


        try {
            r = (RadioButton)findViewById(R.id.radioButton3);
            r.setText(node.getEdgeList().get(2).text);
        } catch (Exception e) {
            ((RadioButton)findViewById(R.id.radioButton3)).setVisibility(View.INVISIBLE);
        }

        Button b = (Button) findViewById(R.id.buttonForward);
        b.setText(node.getForwardText());
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNextNode();
            }
        });

        Button bb = (Button) findViewById(R.id.buttonBack);
        if (Node.activeNode == 0){
            bb.setVisibility(View.INVISIBLE);
        }

    }
}
