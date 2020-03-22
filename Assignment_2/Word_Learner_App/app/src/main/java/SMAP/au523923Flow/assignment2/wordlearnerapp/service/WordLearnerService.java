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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import SMAP.au523923Flow.assignment2.wordlearnerapp.data.WordLearnerDatabase;
import SMAP.au523923Flow.assignment2.wordlearnerapp.data.WordRepository;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Word;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.ApplicationFirstRunChecker;
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
    private List<Word> allwords;
    // Flag to signal to ListActivity that words are initialized
    private boolean wordsReady = false;

    private static boolean serviceIsRunning =  false;

    @Override
    public void onCreate() {
        super.onCreate();
        Context applicationContext = getApplicationContext();

        enableStethos();

        WordAPIHelper.getInstance(applicationContext);
        wordRepository = new WordRepository(applicationContext);

        // To check database and check for first run of app
        boolean firstRun = ApplicationFirstRunChecker.getFirstTimeRun(getApplicationContext(),Globals.IS_FIRST_RUN);
        if(firstRun){
            Log.d(TAG, "First app run");
            ApplicationFirstRunChecker.setFirstTimeRun(getApplicationContext(),Globals.IS_FIRST_RUN,false);
            addStartWordsToDb();
        }
        else {
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    allwords = wordRepository.getAllWords();
                    broadcastTaskResult("Getting all words from db");
                }
            });

        }

        // If still no entries in db, return an empty list
        if (allwords == null)
            allwords = new ArrayList<>();

        serviceIsRunning = true;
        
        Log.d(TAG, "Service is running? : " + serviceIsRunning);
    }

    // To keep it running forever? or make it as a foreground service
    // onDestroy still called when leaving listActivity
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_NOT_STICKY;
        // return START_STICKY;
    }

    public List<Word> getWords(){
        return allwords;
    }

    public List<Word> getAllWordsFromDB(){
        return wordRepository.getAllWords();
    }

    public boolean areWordsReady() {
        return wordsReady;
    }

    //region Binder implementation

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
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
    //endregion

    // Check if word is already added
    public void addWord(String word){
        WordAPIHelper.getInstance().getWordFromOWLBOT(word, new OWLBOTResponseListener<Word>() {
            @Override
            public void getResult(Word wordObject) {
                if (wordObject != null){
                    wordRepository.addWord(wordObject);
                    allwords.add(wordObject);
                    broadcastTaskResult(wordObject.getWord() + " added");
                }
            }
        });
    }
    /*
    public void deleteWord(String word){
        // Get from list
        Word wordObj = new Word();
        wordObj.setWord(word);
        // Does this work? dont think the objects are the same
        //allwords.remove(word);
        Word wordToBeRemoved = wordRepository.getWord(wordObj);
        wordRepository.deleteWord(wordToBeRemoved);
        allwords.remove(wordToBeRemoved);
        allwords = wordRepository.getAllWords();
        broadcastTaskResult("Deleted word: " + word);
    }

     */

    @Override
    public void onDestroy() {
        serviceIsRunning = false;
        Log.d(TAG, "onDestroy: called, service is running? " + serviceIsRunning);
        super.onDestroy();
    }

    //send local broadcast
    // Add a nullable string to send back to user as a toast
    private void broadcastTaskResult(@Nullable String message){
        Log.d(TAG, "WordLearnerService: Broadcasting result");

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(Globals.BROADCAST_WORDLEARNERSERVICE);

        if (message != null)
            broadcastIntent.putExtra(Globals.MSG_TO_USER, message);

        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    private void addStartWordsToDb() {
        Log.d(TAG, "First app run. Populating db with start words from original csv file");
        for (String word: Globals.START_WORDS) {
            WordAPIHelper.getInstance().getWordFromOWLBOT(word, new OWLBOTResponseListener<Word>() {
                @Override
                public void getResult(Word wordObject) {
                    wordRepository.addWord(wordObject);
                    allwords.add(wordObject);
                    // Notify for each word added
                    broadcastTaskResult("Got new start word");
                }
            });
        }
    }

    // From L4 persistence TheSituationRoom demo from SMAP
    //enable stethos tool for inspecting database on device / emulator through chrome
    private void enableStethos(){

           /* Stetho initialization - allows for debugging features in Chrome browser
           See http://facebook.github.io/stetho/ for details
           1) Open chrome://inspect/ in a Chrome browse
           2) select 'inspect' on your app under the specific device/emulator
           3) select resources tab
           4) browse database tables under Web SQL
         */
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(
                        Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(
                        Stetho.defaultInspectorModulesProvider(this))
                .build());
        /* end Stethos */
    }
}
