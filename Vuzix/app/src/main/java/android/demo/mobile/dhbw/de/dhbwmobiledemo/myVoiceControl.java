package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.content.Context;
import android.util.Log;
import android.widget.Button;

import com.vuzix.speech.VoiceControl;

/**
 * Created by Silke on 09.04.2015.
 */


public class myVoiceControl extends VoiceControl {
    private static VoiceControl vc;

    public myVoiceControl(Context context) {
        super(context);
        vc = this;
    }

    public static VoiceControl getVC() {
        return vc;
    }

    @Override
    protected void onRecognition(String result) {


    }

}
