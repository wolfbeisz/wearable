package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.app.Activity;
import android.content.Context;
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
import android.widget.CheckBox;
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

    protected static final String nextWords[] = {"move right", "forward", "next", "go right"};
    protected static final String selectWords[] = {"select", "choose"};
    protected static final String backWords[] = {"go back", "previous", "move left", "go left"};
    protected static final String scrollUpWords[] = {"scroll up", "move up"};
    protected static final String scrollDownWords[] = {"scroll down", "move down"};
    private static final String folderName = "MobileDemo";


    public static MainActivity getActiveActivity() {
        return activeActivity;
    }

    public static void setActiveActivity(MainActivity activeActivity) {
        MainActivity.activeActivity = activeActivity;
    }

    private static MainActivity activeActivity;


    protected static VoiceControl vc;

    private MyDBHelper dbh;

    protected ArrayList<File> cFiles;

    private int scrollPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initializeVoiceControl();

        initializeFiles();

        setActiveActivity(this);
    }

    public void initializeFiles() {
        /*

        Read files from storage and initialize the ListView

        Set listener for click on ListItems that will load the database file and start
         */
        if (cFiles == null) {
            if (isExternalStorageReadable()) {
                cFiles = new ArrayList<>();
                File tFiles[] = getStorageDir(folderName);
                if (tFiles != null && tFiles.length > 0) {
                    for (int i = 0; i < tFiles.length; i++) {
                        if (tFiles[i].getName().endsWith(".sqlite")) {
                            cFiles.add(tFiles[i]);
                        }
                    }

                    final File files[] = cFiles.toArray(new File[cFiles.size()]);
                    if (files.length > 0) {

                        final ListView listView = (ListView) findViewById(R.id.filesListView);
                        final ArrayList<String> fileNameStringList = new ArrayList<String>();
                        for (int i = 0; i < files.length; i++) {
                            fileNameStringList.add("" + (i + 1) + ": " + files[i].getName());
                        }
                        final ArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, fileNameStringList);
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, final View view,
                                                    int position, long id) {

                                initializeDBFile(files[position]);
                            }

                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "No sqlite files found in folder " + folderName + ".", Toast.LENGTH_LONG);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No files found in folder " + folderName + ".", Toast.LENGTH_LONG);
                }
            } else {
                Log.i("Info", "Storage not available for any reason.");
                Toast.makeText(getApplicationContext(), "Please ensure storage is mounted and available for device.", Toast.LENGTH_LONG);
            }
        }
    }

    public void initializeVoiceControl() {
        /*
        * anonymous class extending VoiceControl
        * Create a new object for voice control
         */
        vc = new VoiceControl(this) {
            @Override
            protected void onRecognition(String result) {
                MainActivity.getActiveActivity().myOnRecognition(result);
            }
        };
    }

    protected void myOnRecognition(String result) {
        try {
            Log.i("Recognition", "Recognition: " + result);
            ListView v;
            v = (ListView) findViewById(R.id.filesListView);
                /*
                Checking for recognized keyword to select a line
                 */
            for (String selectWord : selectWords) {
                if (result.contains(selectWord)) {
                    int count = v.getCount();
                    if (count > 0) {
                        for (int i = 1; i <= count; i++) {
                            if (result.contains("" + i)) {
                                initializeDBFile(cFiles.get(i - 1));
                                return;
                            }
                        }
                    }
                }
            }

            int wordsMaxLength = (scrollUpWords.length >= scrollUpWords.length)? scrollUpWords.length : scrollDownWords.length;
            for (int i = 0; i < wordsMaxLength; i++) {
                if (scrollUpWords.length > i && result.contains(scrollUpWords[i])) {
                    scroll(v,-3);
                    break;
                } else if (scrollDownWords.length > i && result.contains(scrollDownWords[i])) {
                    scroll(v,3);
                    break;
                }
            }
        } catch (Exception e) {
        }
    }

    private void scroll(ListView v, int step){
        int maxPosition = v.getCount() - 1;
        if(scrollPosition == 0 && step < 0){
            scrollPosition = maxPosition;
        } else if(scrollPosition == maxPosition && step > 0){
            scrollPosition = 0;
        } else {
            scrollPosition += step;
            if(scrollPosition < 0){
                scrollPosition = 0;
            } else if(scrollPosition > maxPosition){
                scrollPosition = maxPosition;
            }
        }
        v.setSelection(scrollPosition);
    }

    protected void initializeDBFile(File myFile) {

        try {
            if(dbh != null){
                dbh.close();
            }
            dbh = openDatase(myFile);
            Log.i("Log", "Database opened successfully");
        } catch (IOException e) {
            Log.e("Error", "Database could not be copied: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Database could not be copied: Access problem. " + e.getMessage() + e.getMessage(), Toast.LENGTH_LONG);
        } catch (SQLException e) {
            Log.e("Error", "Database could not be opened: File does not fit to application. " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Database could not be opened: File does not fit to application. " + e.getMessage(), Toast.LENGTH_LONG);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Database could not be opened: File does not fit to application. " + e.getMessage(), Toast.LENGTH_LONG);
        }
        try {
            Node.init(dbh);
            Node.activeNode = Node.lowestNodeId;
            displayNode();

        } catch (SQLException e) {
            Log.e("Error", "Database could not be opened: File does not fit to application. " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Database could not be opened: File does not fit to application. " + e.getMessage(), Toast.LENGTH_LONG);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", "Database could not be opened: File does not fit to application. " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Database could not be opened: File does not fit to application. " + e.getMessage(), Toast.LENGTH_LONG);
        }
    }

    protected void checkCheckBox(int id, int nr, String type) {
        try {
            View v = findViewById(id);
            if (v.isEnabled()) {
                if (type =="Multiple"){
                    CheckBox c = (CheckBox)findViewById(id);
                    if(c.isChecked()){
                        c.setChecked(false);
                    } else {c.setChecked(true);}
                }
                else if (type =="Single"){
                    ((CompoundButton) v).setChecked(true);
                }
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
                Node.activeNode = oActiveNode.getNextNodeId(-1);
                break;
            case 1:
                int selectedEdgeNr = ((SingleChoiceActivity) this).getCheckedSelection();
                Node.activeNode = ((SingleChoiceNode) oActiveNode).getNextNodeIdByEdgeNr(selectedEdgeNr);
                break;
            case 2:
                Node.activeNode = oActiveNode.getNextNodeId(-1);
        }
        if ((Node.activeNode < Node.lowestNodeId || Node.activeNode > Node.highestNodeId)) {
            Node.activeNode = Node.lowestNodeId;
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
        String path = Environment.getExternalStorageDirectory().toString() + "/" + folderName;
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
            return null;
        }
    }

    @Override
    protected void onResume() {
        if(vc != null) {
            try {
                vc.on();
            } catch (Exception e) {

            }
        }
        MainActivity.setActiveActivity(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (vc != null) {try {
            vc.on();
        } catch (Exception e) {

        }
        }
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        if (vc != null) {
            vc.off();
            vc.destroy();
            vc = null;
        }
        if(dbh != null)
            {
                dbh.close();
            }
        super.onDestroy();
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
