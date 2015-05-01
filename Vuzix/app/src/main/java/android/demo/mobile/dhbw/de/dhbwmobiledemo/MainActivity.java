package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import com.vuzix.speech.VoiceControl;

import java.io.File;
import java.io.IOException;


public class MainActivity extends Activity {


    protected VoiceControl vc;

    private MyDBHelper dbh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (vc != null) {
            vc.destroy();
        }

        /*
        * Nested class
         */
        vc = new VoiceControl(this) {
            @Override
            protected void onRecognition(String result) {
                Log.i("Recognition", "Recognition: " + result);
                //int typeID = Node.getNodeById(Node.activeNode).getTypeId();
                if (result.contains("move right") || result.contains("forward") || result.contains("next") || result.contains("go right")) {
                    Log.i("Recognition", "Button: " + result);
                    Button b = (Button) findViewById(R.id.buttonForward);
                    if (b.isEnabled()) {
                        b.callOnClick();
                    }
                } else if (result.contains("select") || result.contains("choose")) {
                    Log.i("Recognition", "Select: " + result);
                    if (result.contains("1")) {
                        checkCheckBox(R.id.selection1, 0);
                    } else if (result.contains("2")) {
                        checkCheckBox(R.id.selection2, 1);
                    } else if (result.contains("3")) {
                        checkCheckBox(R.id.selection3, 2);
                    }
                } else if (result.contains("back") || result.contains("previous") || result.contains("move left") || result.contains("go left")) {
                    try {
                        Button bb = (Button) findViewById(R.id.buttonBack);
                        bb.callOnClick();
                    } catch (Exception e) {

                    }
                }


            }
        };


        if ((Node.getNodeById(Node.activeNode) == null)) {
            if (isExternalStorageReadable()) {
                File directory = getStorageDir("example-db.sqlite");

            /*
            File choose should be implemented later
             */

                try {
                    dbh = openDatase(directory);
                    Log.i("Log", "Database opened successfully");
                } catch (IOException e) {
                    Log.e("Error", "Database could not be copied: " + e.getMessage());
                } catch (SQLException e) {
                    Log.e("Error", "Database could not be opened: " + e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Node.init(dbh);
                    Node.activeNode = 0;
                    displayNode();

                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }


            } else {
                Log.i("Info", "Storage not available for any reason.");
            /*
            User should be informed.
             */
            }
        }

    }

    protected void checkCheckBox(int id, int nr){
        try {
            View v = findViewById(id);
            if (v.isEnabled()) {
                ((CompoundButton)v).setChecked(true);
            }
            View f = findViewById(R.id.buttonForward);
            f.setEnabled(true);
        } catch (NullPointerException e) {
            Log.i("Recognition", "No box available, recognized " + nr);
        }
    }

    public void displayNode() {
        switch (Node.getNodeById(Node.activeNode).getTypeId()) {
            case 0:
                startActivity(new Intent(MainActivity.this, ImageTextActivity.class));
                break;
            case 1:
                startActivity(new Intent(MainActivity.this, SingleChoiceActivity.class));
                break;
            case 2:
                startActivity(new Intent(MainActivity.this, MultipleChoiceActivity.class));
                break;
            default:
        }
    }

    public void displayNextNode() {
        Node oActiveNode = Node.getNodeById(Node.activeNode);
        switch(oActiveNode.getTypeId()){
            case 0:
                Node.activeNode++;
                break;
            case 1:
                int succ = ((SingleChoiceActivity)this).getCheckedSelection();
                Node.activeNode = ((SingleChoiceNode)oActiveNode).getNextNodeIdByEdgeNr(succ);
                break;
            case 2:
                Node.activeNode = ((MultipleChoiceNode)oActiveNode).getNextNodeId(-1);
        }
        displayNode();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public File getStorageDir(String folderName) {
        // Get the directory for the user's public Downloads directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), folderName);
        Log.e("Log", "DIRECTORY");
        if (!file.mkdirs()) {
            Log.e("Error", "Directory not created");
        }
        return file;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (vc != null) {
            vc.on();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (vc != null) {
            vc.off();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Node.destroy();
        if (vc != null) {
            vc.destroy();
        }
    }


    private MyDBHelper openDatase(File file) throws IOException, SQLException {
        MyDBHelper myDBHelper = new MyDBHelper(this);

        try {

            myDBHelper.createDataBase(file);

        } catch (IOException ioe) {

            throw ioe;

        }

        try {

            myDBHelper.openDataBase();

        } catch (SQLException sqle) {

            throw sqle;

        }
        return myDBHelper;
    }

}
