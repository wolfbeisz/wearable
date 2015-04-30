package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.vuzix.speech.VoiceControl;

import java.io.File;
import java.io.IOException;


public class MainActivity extends Activity {


    VoiceControl vc;

    MyDBHelper dbh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vc = new VoiceControl(this) {

            @Override
            protected void onRecognition(String result) {
                Log.i("Recognition", "Recognition: " + result);
                //int typeID = Node.getNodeById(Node.activeNode).getTypeId();
                if (result.contains("move right") || result.contains("forward") || result.contains("next") || result.contains("go right")) {
                    Log.i("Recognition", "Button: " + result);
                    Button b = (Button) findViewById(R.id.buttonForward);
                    if(b.isEnabled()) {
                        b.callOnClick();
                    }
                } else if (result.contains("select")){
                    Log.i("Recognition", "Select: " + result);
                        if (result.contains("1")){
                            Log.i("Recognition", "Select 1: " + result);
                            try {
                                CheckBox c = (CheckBox) findViewById(R.id.checkBox);
                                if (c.isEnabled()) {
                                    Log.i("Recognition", "Checkbox enabled: " + result);
                                    //c.callOnClick();
                                    c.setChecked(true);
                                }
                            }catch (NullPointerException e){
                                Log.i("Recognition", "No Checkbox available.");
                            }

                            try {
                                RadioButton r = (RadioButton)findViewById(R.id.radioButton);
                                if (r.isEnabled()){
                                    r.setChecked(true);
                                }
                            } catch (NullPointerException e){
                                Log.i("Recognition", "No radioButton available.");
                            }
                        } else if(result.contains("2")){
                            try {
                                CheckBox c = (CheckBox) findViewById(R.id.checkBox2);
                                if (c.isEnabled()) {
                                    c.callOnClick();
                                }
                            }catch (Exception e){

                            }

                            try {
                                RadioButton r = (RadioButton)findViewById(R.id.radioButton2);
                                if (r.isEnabled()){
                                    r.callOnClick();
                                }
                            } catch (Exception e) {
                            }
                        }


                    } else if(result.contains("3")){
                            try {
                                CheckBox c = (CheckBox) findViewById(R.id.checkBox3);
                                if (c.isEnabled()) {
                                    c.callOnClick();
                                }
                            }catch (Exception e){

                            }

                            try {
                                RadioButton r = (RadioButton)findViewById(R.id.radioButton3);
                                if (r.isEnabled()){
                                    r.callOnClick();
                                }
                            } catch (Exception e) {

                            }
                } else if(result.contains("back") || result.contains("previous")){
                    finish();
                }


            }
        };


        if((Node.getNodeById(Node.activeNode) == null)) {
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

    public void displayNode(){
        switch(Node.getNodeById(Node.activeNode).getTypeId()){
            case 0 :
                startActivity(new Intent(MainActivity.this, ImageTextActivity.class));
                break;
            case 1 :
                startActivity(new Intent(MainActivity.this, SingleChoiceActivity.class));
                break;
            case 2 :
                startActivity(new Intent(MainActivity.this, MultipleChoiceActivity.class));
                break;
            default :
        }
    }

    public void displayNextNode(){
        Node.activeNode++;
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
    protected void onResume(){
        super.onResume();
        if(vc != null) {
            vc.on();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(vc != null) {
            vc.off();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Node.destroy();
        if(vc != null) {
            vc.destroy();
        }
    }


    private MyDBHelper openDatase(File file) throws IOException, SQLException {
        MyDBHelper myDBHelper = new MyDBHelper(this);

        try {

            myDBHelper.createDataBase(file);

        } catch (IOException ioe) {

            throw  ioe;

        }

        try {

            myDBHelper.openDataBase();

        }catch(SQLException sqle){

            throw sqle;

        }
        return myDBHelper;
    }

}
