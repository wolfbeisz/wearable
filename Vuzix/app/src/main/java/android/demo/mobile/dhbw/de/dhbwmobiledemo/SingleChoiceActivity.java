package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

/**
 * Created by Silke on 14.04.2015.
 */
public class SingleChoiceActivity extends MainActivity {
    private static SingleChoiceNode node;
    private int checkedSelection = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlechoice);
        setNode((SingleChoiceNode) Node.getNodeById(Node.activeNode));
        setContentView(R.layout.activity_singlechoice);
        setData();
    }


    public void setNode(SingleChoiceNode node) {
        this.node = node;
    }


    public void setData() {
        final Button b = (Button) findViewById(R.id.buttonForward);
        b.setText(node.getForwardText());
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNextNode();
            }
        });
        b.setEnabled(false);


        RadioButton r = null;
        try {
            r = (RadioButton) findViewById(R.id.selection1);
            r.setText(node.getEdgeList().get(0).text);
            setSelectionOnClicklistener(r, b, 0);
        } catch (Exception e) {
            ((RadioButton) findViewById(R.id.selection1)).setVisibility(View.INVISIBLE);
        }


        try {
            r = (RadioButton) findViewById(R.id.selection2);
            r.setText(node.getEdgeList().get(1).text);
            setSelectionOnClicklistener(r, b, 1);
        } catch (Exception e) {
            ((RadioButton) findViewById(R.id.selection2)).setVisibility(View.INVISIBLE);
        }


        try {
            r = (RadioButton) findViewById(R.id.selection3);
            r.setText(node.getEdgeList().get(2).text);
            setSelectionOnClicklistener(r, b, 2);
        } catch (Exception e) {
            ((RadioButton) findViewById(R.id.selection3)).setVisibility(View.INVISIBLE);
        }

        try {
            r = (RadioButton) findViewById(R.id.selection4);
            r.setText(node.getEdgeList().get(3).text);
            setSelectionOnClicklistener(r, b, 3);
        } catch (Exception e) {
            ((RadioButton) findViewById(R.id.selection4)).setVisibility(View.INVISIBLE);
        }


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

    public int getCheckedSelection() {
        return checkedSelection;
    }

    public void setCheckedSelection(int checkedSelection) {
        this.checkedSelection = checkedSelection;
    }

    @Override
    protected void checkCheckBox(int id, int nr,String type) {
        super.checkCheckBox(id, nr,type);
        this.setCheckedSelection(nr);
    }

    public void setSelectionOnClicklistener(View r, final View b, final int nr) {
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.setEnabled(true);
                setCheckedSelection(nr);
            }
        });
    }

    @Override
    public void myOnRecognition(String result) {
        try {
            Log.i("Recognition", "Recognition: " + result);


                /*
                Checking for recognized keyword to select a line
                 */
            for (String selectWord : selectWords) {
                if (result.contains(selectWord)) {

                    Log.i("Recognition", "Select: " + result);
                    if (result.contains("1")) {
                        checkCheckBox(R.id.selection1, 0, "Single");
                    } else if (result.contains("2")) {
                        checkCheckBox(R.id.selection2, 1, "Single");
                    } else if (result.contains("3")) {
                        checkCheckBox(R.id.selection3, 2, "Single");
                    } else if (result.contains("4")) {
                        checkCheckBox(R.id.selection4, 3, "Single");
                    }
                    return;
                }
            }

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
        Log.d("Back", "SingleChoice, NodeId: " + Node.activeNode);
        if (!Node.listOfNodesVisited.empty()) {
            Node.activeNode = Node.listOfNodesVisited.pop();
        } else {
            Node.activeNode = 0;
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
