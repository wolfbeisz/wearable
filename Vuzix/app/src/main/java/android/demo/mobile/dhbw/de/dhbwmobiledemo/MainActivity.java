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

    private static final String nextWords[] = {"move right", "forward","next","go right"};
    private static final String selectWords[] = {"select", "choose"};
    private static final String backWords[] = {"back", "previous","move left", "go left"};



    protected static VoiceControl vc;

    private MyDBHelper dbh;

    protected File cFiles[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (vc != null) {
            vc.destroy();
        }

        /*
        * anonymous class extending VoiceControl
        * Create a new object for voice control
         */
        vc = new VoiceControl(this) {
            @Override
            protected void onRecognition(String result) {
                Log.i("Recognition", "Recognition: " + result);
                /*
                Checking for recognized keyword to navigate forward
                 */
                for(String nextWord : nextWords){
                    if(result.contains(nextWord)){
                        Log.i("Recognition", "Button: " + result);
                        Button b = (Button) findViewById(R.id.buttonForward);
                        if (b.isEnabled()) {
                            b.callOnClick();
                        }
                        return;
                    }
                }
                /*
                Checking for recognized keyword to select a line
                 */
                for(String selectWord : selectWords){
                    if(result.contains(selectWord)){
                        ListView v;
                        try{
                            v = (ListView)findViewById(R.id.filesListView);
                            int count = v.getCount();
                            if(count > 0){
                                for(int i = 1; i <= count; i++){
                                    if(result.contains("" + i)){
                                        initializeDBFile(cFiles, i-1);
                                    }
                                }
                            }
                        } catch (Exception e){

                        }

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
                /*
                Checking for recognized keyword to navigate back
                 */
                for(String backWord : backWords){
                    if(result.contains(backWord)){
                        try {
                            Button bb = (Button) findViewById(R.id.buttonBack);
                            if(bb.getVisibility() == View.VISIBLE) {
                                bb.callOnClick();
                            }
                        } catch (NullPointerException e) {
                            //this is ok, no back button to click on current page
                        }
                    }
                }
            }
        };

        /*
        Read files from storage and initialize the ListView

        Set listener for click on ListItems that will load the database file and start
         */
        if ((Node.getNodeById(Node.activeNode) == null)) {
            if (isExternalStorageReadable()) {
                File directory;
                final File files[] = getStorageDir("example-db.sqlite");
                cFiles = files;
                if (files != null && files.length > 0) {
                    directory = files[0];

                    final ListView listView = (ListView) findViewById(R.id.filesListView);
                    final ArrayList<String> fileNameStringList = new ArrayList<String>();
                    for(int i = 0; i < files.length; i++){
                        fileNameStringList.add("" + (i+1) + ": " + files[i].getName());
                    }
                    final ArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, fileNameStringList);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view,
                                                int position, long id) {

                            initializeDBFile(files, position);
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
            Toast.makeText(getApplicationContext(),"Please ensure storage is mounted and available for device.",Toast.LENGTH_LONG);
            }
        }

    }

    public void initializeVoiceControl(){

    }

    protected void initializeDBFile(File[] files, int position){

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
        Node.listOfNodesVisited.push(Node.activeNode);
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

        }
        else {
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
        //Node.destroy();
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
