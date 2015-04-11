package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.app.Activity;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.vuzix.speech.VoiceControl;

import java.io.File;
import java.io.IOException;


public class MainActivity extends Activity {

    myVoiceControl myVc;

    VoiceControl vc;

    MyDBHelper dbh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vc = new VoiceControl(this) {
            @Override
            protected void onRecognition(String result) {
                //((TextView)findViewById(R.id.voice_text)).setText(result);

            }
        };
        if(isExternalStorageReadable()){
            File directory = getStorageDir("example-db.sqlite");

            /*
            File choose should be implemented later
             */

            try {
                dbh = openDatase(directory);
                Log.i("Log", "Database opened successfully");
            } catch(IOException e){
                Log.e("Error", "Database could not be copied: " + e.getMessage());
            } catch (SQLException e){
                Log.e("Error", "Database could not be opened: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Node.init(dbh);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }


        }
        else{
            Log.i("Info", "Storage not available for any reason.");
            /*
            User should be informed.
             */
        }

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
        vc.on();
    }

    @Override
    protected void onPause(){
        super.onPause();
        vc.off();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        vc.destroy();
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
