package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by Silke on 14.04.2015.
 */
public class SingleChoiceActivity extends Activity{
    private static ImageTextNode node;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlechoice);

    }

    public void setNode(ImageTextNode node){
        this.node = node;
    }

    public void setData(){
        RadioButton r = (RadioButton)findViewById(R.id.radioButton);
        r.setText();

        RadioButton r = (RadioButton)findViewById(R.id.radioButton2);
        r.setText();

        RadioButton r = (RadioButton)findViewById(R.id.radioButton3);
        r.setText();

    }
}
