package android.demo.mobile.dhbw.de.dhbwmobiledemo;

import android.content.Context;
import android.util.Log;

import com.vuzix.speech.VoiceControl;

/**
 * Created by Silke on 07.04.2015.
 */


class myVoiceControl extends VoiceControl {
    private VoiceControl vc;
    private String[] wordlist = {"back", "forward", "first", "second", "third"};

    public myVoiceControl(Context context,
                          String[] grammars,
                          String[] wordlist){
        super(context, grammars, wordlist);
    }

    private final String TAG = "Voice";
    @Override
    protected void onRecognition(String result){
        Log.i(TAG, result);
    }

    @Override
    protected  void onResume(){
        super.onResume();
        vc.on();
    }

    @Override
    protected void onPause(){
        super.onPause();
        vc.off();
    }

    protected void onDestroy(){
        if (vc != null) vc.destroy();
    }
}
