package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.app.Activity;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.silke.vuzix.R;
import com.vuzix.speech.VoiceControl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends Activity {

    myVoiceControl myVc;

    VoiceControl vc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vc = new VoiceControl(this) {
            @Override
            protected void onRecognition(String result) {
                ((TextView)findViewById(R.id.voice_text)).setText(result);

            }
        };

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

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        Log.e("Log", "DIRECTORY_PICTURES");
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


    void checkDB() throws Exception {
        try {
            SQLiteDatabase dbe = SQLiteDatabase
                    .openDatabase(
                            getAssets().openFd("example-db.sqlite").toString(),
                            null, 1
                    );
            Log.d("opendb", "EXIST");
            dbe.close();
        } catch (Exception e) {

            AssetManager am = getApplicationContext().getAssets();
            OutputStream os = new FileOutputStream(
                    "/res/sqlite/example-db.sqlite");
            byte[] b = new byte[100];

            int r;
            InputStream is = am.open("yourfile.sqlite");
            while ((r = is.read(b)) != -1) {
                os.write(b, 0, r);
            }
            Log.i("DATABASE_HELPER", "Copying the database ");
            is.close();
            os.close();
        }

    }
}
