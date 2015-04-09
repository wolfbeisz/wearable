package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.content.Context;
import android.util.Log;

import com.vuzix.speech.VoiceControl;

/**
 * Created by Silke on 09.04.2015.
 */



public class myVoiceControl extends VoiceControl {
    private VoiceControl vc;

    public myVoiceControl(Context context){
        super(context);
    }

    @Override
    protected void onRecognition(String result){
        Log.w("Vuzix Speech: ", "result"+ result);
    }

}
