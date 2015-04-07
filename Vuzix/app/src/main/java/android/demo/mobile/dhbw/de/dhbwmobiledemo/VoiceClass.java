package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.vuzix.speech.VoiceControl;

/**
 * Created by Silke on 07.04.2015.
 */
public class VoiceClass extends VoiceControl{
    TextView tv;
    public VoiceClass(Context context){
        super(context);
    }

    @Override
    protected void onRecognition(String word){
        Log.w("M100Dev", "word: " + word);
    }
}
