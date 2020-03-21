package SMAP.au523923Flow.assignment2.wordlearnerapp.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.stetho.Stetho;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import SMAP.au523923Flow.assignment2.wordlearnerapp.data.WordLearnerDatabase;
import SMAP.au523923Flow.assignment2.wordlearnerapp.data.WordRepository;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Word;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.Globals;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.OWLBOTResponseListener;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.WordAPIHelper;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.WordJsonParser;

// Ref : https://developer.android.com/guide/components/bound-services

/*
When the last client unbinds from the service,
the system destroys the service, unless the service was also started by startService().
*/

// OnDestroy gets called

public class WordLearnerService extends Service {
    private static final String TAG = "WordLearnerService";
    // Binder given to clients
    private final IBinder binder = new LocalBinder();
    private WordRepository wordRepository;
    private ArrayList<Word> allwords;

    @Override
    public void onCreate() {
        super.onCreate();
        Context applicationContext = getApplicationContext();
        // To check database
        Stetho.initializeWithDefaults(applicationContext);
        WordAPIHelper.getInstance(applicationContext);
        wordRepository = new WordRepository(applicationContext);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    // To keep it running forever? or make it as a foreground service
    // onDestroy still called when leaving listActivity
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    // use another name
    public class LocalBinder extends Binder {
        public WordLearnerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return WordLearnerService.this;
        }
    }

    // Check if word is already added
    public void addWord(String word){
        WordAPIHelper.getInstance().getWordFromOWLBOT(word, new OWLBOTResponseListener<Word>() {
            @Override
            public void getResult(Word object) {
                wordRepository.addWord(object);
            }
        });
    }

    public void deleteWord(String word){
        // Get from list
        Word wordObj = new Word();
        wordObj.setWord(word);
        wordRepository.deleteWord(wordObj);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: called");
    }

    //send local broadcast
    // Add a nullable string to send back to user as a toast
    private void broadcastTaskResult(@Nullable String message){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(Globals.BROADCAST_WORDLEARNERSERVICE);
        if (message != null)
            broadcastIntent.putExtra(Globals.MSG_TO_USER, message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
}
