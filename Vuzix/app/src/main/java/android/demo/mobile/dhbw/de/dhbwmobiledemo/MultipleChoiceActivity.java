package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.app.Activity;
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
