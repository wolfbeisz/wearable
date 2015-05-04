package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.vuzix.speech.VoiceControl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends Activity {


    protected VoiceControl vc;

    private MyDBHelper dbh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Activity that = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            final Button bb = (Button) findViewById(R.id.buttonBack);
            bb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    that.finish();
                }
            });
        } catch( NullPointerException e){
            //This is ok, no back button on page
        }

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

                        bb.callOnClick();
                    } catch (Exception e) {

                    }
                }


            }
        };


        if ((Node.getNodeById(Node.activeNode) == null)) {
            if (isExternalStorageReadable()) {
                File directory;
                final File files[] = getStorageDir("example-db.sqlite");
                if (files != null && files.length > 0) {
                    directory = files[0];

                    final ListView listView = (ListView) findViewById(R.id.filesListView);
                    final ArrayList<String> fileNameStringList = new ArrayList<String>();
                    for(File file : files){
                        fileNameStringList.add(file.getName());
                    }
                    final ArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, fileNameStringList);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view,
                                                int position, long id) {

                            try {
                                dbh = openDatase(files[position]);
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
                        }

                    });

            /*
            File choose should be implemented later
             */


                } else {
                    Log.i("Files", "No files found.");
                }

            } else {
                Log.i("Info", "Storage not available for any reason.");
            /*
            User should be informed.
             */
            }
        }

    }

    protected void checkCheckBox(int id, int nr) {
        try {
            View v = findViewById(id);
            if (v.isEnabled()) {
                ((CompoundButton) v).setChecked(true);
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
        switch (oActiveNode.getTypeId()) {
            case 0:
                Node.activeNode++;
                break;
            case 1:
                int selectedEdgeNr = ((SingleChoiceActivity) this).getCheckedSelection();
                Node.activeNode = ((SingleChoiceNode) oActiveNode).getNextNodeIdByEdgeNr(selectedEdgeNr);
                break;
            case 2:
                Node.activeNode = ((MultipleChoiceNode) oActiveNode).getNextNodeId(-1);
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

    public File[] getStorageDir(String folderName) {
        String path = Environment.getExternalStorageDirectory().toString() + "/MobileDemo";
        Log.d("Files", "Path: " + path);
        File file = new File(path);
        Log.d("Files", "File object created.");
        if (!file.mkdirs()) {
            Log.d("Files", "Directory exists. Continuing reading files.");
            File files[] = file.listFiles();
            int nrOfFiles = files.length;
            Log.d("Files", "Found " + nrOfFiles + "files in directory.");
            return files;
        } else {
            Log.d("Files", "Directory did not exist. Created directory at: " + path);
            //TODO: Notify User where to put files.
            Toast.makeText(getApplicationContext(),"Please put your sql files under /MobileDemo/!",Toast.LENGTH_LONG);
            return null;
        }
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

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}
