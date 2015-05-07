package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * Created by Silke on 14.04.2015.
 */
public class MultipleChoiceActivity extends MainActivity {
    private static MultipleChoiceNode node;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplechoice);
        setNode((MultipleChoiceNode) Node.getNodeById(Node.activeNode));
        setContentView(R.layout.activity_multiplechoice);
        setData();
/*

*/
    }

    public void setNode(MultipleChoiceNode node) {
        this.node = node;
    }

    public void setData() {
        CheckBox c = (CheckBox) findViewById(R.id.selection1);
        c.setText(node.getEdgeList().get(0).text);

        c = (CheckBox) findViewById(R.id.selection2);
        c.setText(node.getEdgeList().get(1).text);

        c = (CheckBox) findViewById(R.id.selection3);
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

//
//    @Override
//    public void initializeVoiceControl() {
//        if (vc != null) {
//            vc.off();
//            vc.destroy();
//        }
//
//        /*
//        * anonymous class extending VoiceControl
//        * Create a new object for voice control
//         */
//        vc = new VoiceControl(this) {
//            @Override
//            protected void onRecognition(String result) {
//                try {
//                    Log.i("Recognition", "Recognition: " + result);
//
//
//                /*
//                Checking for recognized keyword to select a line
//                 */
//                    for (String selectWord : selectWords) {
//                        if (result.contains(selectWord)) {
//
//                            Log.i("Recognition", "Select: " + result);
//                            if (result.contains("1")) {
//                                checkCheckBox(R.id.selection1, 0);
//                            } else if (result.contains("2")) {
//                                checkCheckBox(R.id.selection2, 1);
//                            } else if (result.contains("3")) {
//                                checkCheckBox(R.id.selection3, 2);
//                            }
//                            return;
//                        }
//                    }
//
//                    for (String nextWord : nextWords) {
//                        if (result.contains(nextWord)) {
//                            Log.i("Recognition", "Button: " + result);
//                            try {
//                                Button b = (Button) findViewById(R.id.buttonForward);
//                                if (b.isEnabled()) {
//                                    b.callOnClick();
//                                }
//                            } catch (Exception e) {
//
//                            }
//                            return;
//                        }
//                    }
//                    for (String backWord : backWords) {
//                        if (result.contains(backWord)) {
//                            try {
//                                Button bb = (Button) findViewById(R.id.buttonBack);
//                                if (bb.getVisibility() == View.VISIBLE) {
//                                    bb.callOnClick();
//                                }
//                            } catch (NullPointerException e) {
//                                //this is ok, no back button to click on current page
//                            }
//                        }
//                    }
//
//
//                } catch (Exception e) {
//                }
//            }
//        };
//    }

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
                        checkCheckBox(R.id.selection1, 0);
                    } else if (result.contains("2")) {
                        checkCheckBox(R.id.selection2, 1);
                    } else if (result.contains("3")) {
                        checkCheckBox(R.id.selection3, 2);
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
        Log.d("Back", "MultipleChoice, NodeId: " + Node.activeNode);
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
