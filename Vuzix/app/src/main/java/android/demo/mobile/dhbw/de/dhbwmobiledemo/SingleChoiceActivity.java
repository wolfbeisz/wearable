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
    private int previousActiveNode;

    public int getCheckedSelection() {
        return checkedSelection;
    }

    public void setCheckedSelection(int checkedSelection) {
        this.checkedSelection = checkedSelection;
    }

    private int checkedSelection = -1;

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
        final Button b = (Button) findViewById(R.id.buttonForward);
        b.setText(node.getForwardText());
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNextNode();
            }
        });
        b.setEnabled(false);


        RadioButton r = (RadioButton)findViewById(R.id.selection1);
        r.setText(node.getEdgeList().get(0).text);
        setSelectionOnClicklistener(r,b, 0);


        try {
            r = (RadioButton)findViewById(R.id.selection2);
            r.setText(node.getEdgeList().get(1).text);
            setSelectionOnClicklistener(r,b, 1);
        } catch (Exception e) {
            ((RadioButton)findViewById(R.id.selection2)).setVisibility(View.INVISIBLE);
        }


        try {
            r = (RadioButton)findViewById(R.id.selection3);
            r.setText(node.getEdgeList().get(2).text);
            setSelectionOnClicklistener(r,b, 2);
        } catch (Exception e) {
            ((RadioButton)findViewById(R.id.selection3)).setVisibility(View.INVISIBLE);
        }


        Button bb = (Button) findViewById(R.id.buttonBack);
        final Activity that = this;
        try {
            bb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!Node.listOfNodesVisited.empty()) {
                        Node.activeNode = Node.listOfNodesVisited.pop();
                    }else{
                        Node.activeNode = 0;
                    }
                    that.finish();
                }
            });
        } catch( NullPointerException e){
            //This is ok, no back button on current page
        }
    }

    @Override
    protected void checkCheckBox(int id, int nr){
        super.checkCheckBox(id, nr);
        this.setCheckedSelection(nr);
    }

    public void setSelectionOnClicklistener(View r, final View b, final int nr){
        r.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                b.setEnabled(true);
                setCheckedSelection(nr);
            }
        });
    }



}
