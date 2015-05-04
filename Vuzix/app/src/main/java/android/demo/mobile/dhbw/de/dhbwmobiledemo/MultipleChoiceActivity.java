package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * Created by Silke on 14.04.2015.
 */
public class MultipleChoiceActivity extends MainActivity{
    private static MultipleChoiceNode node;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplechoice);
        setNode((MultipleChoiceNode)Node.getNodeById(Node.activeNode));
        setContentView(R.layout.activity_multiplechoice);
        setData();
/*

*/
    }

    public void setNode(MultipleChoiceNode node){
        this.node = node;
    }

    public void setData(){
        CheckBox c = (CheckBox)findViewById(R.id.selection1);
        c.setText(node.getEdgeList().get(0).text);

        c = (CheckBox)findViewById(R.id.selection2);
        c.setText(node.getEdgeList().get(1).text);

        c = (CheckBox)findViewById(R.id.selection3);
        c.setText(node.getEdgeList().get(2).text);

        Button b = (Button) findViewById(R.id.buttonForward);
        b.setText(node.getForwardText());
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNextNode();
            }
        });
    }
}
