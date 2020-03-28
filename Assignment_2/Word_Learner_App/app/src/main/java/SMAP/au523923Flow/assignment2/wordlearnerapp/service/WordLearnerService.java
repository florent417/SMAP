package SMAP.au523923Flow.assignment2.wordlearnerapp.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import SMAP.au523923Flow.assignment2.wordlearnerapp.R;
import SMAP.au523923Flow.assignment2.wordlearnerapp.data.WordRepository;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Word;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.ApplicationRunChecker;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.DbOperationsListener;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.Globals;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.OWLBOTResponseListener;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.WordAPIHelper;

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
    private List<Word> allWords = new ArrayList<>();
    // Executor service for notification update
    private ExecutorService notificationUpdateExecutor;
    // Notification manager to send updates
    private NotificationManagerCompat notificationManagerCompat;
    private static final String NOTIFICATION_CHANNEL_ID = "WordLearnerChannel";
    private static final String NOTIFICATION_CHANNEL_NAME = "Word Learner Channel";
    private static final int NOTIFICATION_ID = 1;

    private static boolean serviceIsRunning =  false;

    @Override
    public void onCreate() {
        super.onCreate();
        Context applicationContext = getApplicationContext();
        Log.d(TAG, "onCreate: is called");
        enableStethos();

        setupAndStartNotificationsInForeground();

        WordAPIHelper.getInstance(applicationContext);
        wordRepository = new WordRepository(applicationContext);

        // To check database and check for first run of app
        setupWords();

        serviceIsRunning = true;

        recursiveUpdateNotification();
        
        Log.d(TAG, "Service is running? : " + serviceIsRunning);
    }

    @Override
    public void onDestroy() {
        serviceIsRunning = false;
        Log.d(TAG, "onDestroy: called, service is running? " + serviceIsRunning);
        // If phone shuts down or something unexpected happens.
        // Makes it possible to start as a foreground service again
        ApplicationRunChecker.setForegroundServiceRunning(getApplicationContext(),
                Globals.WORD_LEARNER_SERVICE_RUNNING, false);
        super.onDestroy();
    }

    // ########## Binder Implementation ##########
    //region Binder implementation
    // TODO: Find reference
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

    // ########## Words operations ##########
    //region Words operations
    public List<Word> getAllWords(){
        return allWords;
    }

    public void addWord(String word){
        // If word already exists don't add it
        Word alreadyExistingWord = getWord(word.toLowerCase());
        if (alreadyExistingWord != null){
            broadcastTaskResult("Word (" + alreadyExistingWord.getWord() + ") already exists in the app");
        }
        else {
            WordAPIHelper.getInstance().getWordFromOWLBOT(word.toLowerCase(), new OWLBOTResponseListener<Word>() {
                @Override
                public void getResult(Word wordObject) {
                    if (wordObject != null) {
                        wordRepository.addWord(wordObject);
                        allWords.add(wordObject);
                        broadcastTaskResult(wordObject.getWord() + " added");
                    } else {
                        broadcastTaskResult("Word doesn't exist");
                    }
                }
            });
        }
    }

    // To prevent call from db. Matching the Assignment drawing
    public Word getWord(String word){
        Word wordToReturn = null;
        for (Word wordObj : allWords){
            if (wordObj.getWord().equals(word)){
                wordToReturn = wordObj;
                // early stop
                break;
            }
        }
        return wordToReturn;
    }

    public void deleteWord(Word word){
        wordRepository.deleteWord(word, new DbOperationsListener<Word>() {
            @Override
            public void DbOperationDone(Word word) {
                allWords.remove(word);
                broadcastTaskResult("Deleted word: " + word.getWord());
            }
        });
    }

    public void updateWord(Word word){
        wordRepository.updateWord(word, new DbOperationsListener<Word>() {
            @Override
            public void DbOperationDone(Word updatedWord) {
                // Since the object has been changed you can't find it in allWords variable
                // unless you use getWord(String word)
                Word wordToGetIndex = getWord(updatedWord.getWord());
                int i = allWords.indexOf(wordToGetIndex);
                allWords.set(i, updatedWord);
                broadcastTaskResult("Updated word: " + updatedWord.getWord());
            }
        });
    }
    //endregion

    // ########## Broadcast Implementation ##########
    //region Broadcast Implementation
    // send broadcast
    // Add a nullable string to send back to user as a toast
    // Ref: SMAP L5 ServicesDemo
    private void broadcastTaskResult(@Nullable String message){
        Log.d(TAG, "Broadcasting result");

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(Globals.BROADCAST_WORDLEARNERSERVICE);

        // Not all broadcasts need a message to send
        if (message != null)
            broadcastIntent.putExtra(Globals.MSG_TO_USER, message);

        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
    //endregion

    // ########## Setup words and populate db if need be ##########
    //region Populate db if first run else get words
    private void setupWords(){
        boolean appFirstRun = ApplicationRunChecker.getFirstTimeRun(getApplicationContext(),Globals.IS_FIRST_RUN);
        if(appFirstRun){
            Log.d(TAG, "First app run");
            addStartWordsToDb();
            // No longer first run
            ApplicationRunChecker.setFirstTimeRun(getApplicationContext(),Globals.IS_FIRST_RUN,false);
        }
        else {
            wordRepository.getAllWords(new DbOperationsListener<List<Word>>() {
                @Override
                public void DbOperationDone(List<Word> allWordsFromDb) {
                    if (allWordsFromDb != null){
                        allWords = allWordsFromDb;
                        broadcastTaskResult("Got all words from Db");
                    }
                    else {
                        broadcastTaskResult("No words are present in Db");
                    }
                }
            });
        }
    }

    private void addStartWordsToDb() {
        Log.d(TAG, "First app run. Populating db with start words from original csv file");
        for (String word: Globals.START_WORDS) {
            WordAPIHelper.getInstance().getWordFromOWLBOT(word, new OWLBOTResponseListener<Word>() {
                @Override
                public void getResult(Word wordObject) {
                    wordRepository.addWord(wordObject);
                    allWords.add(wordObject);
                    // Notify for each word added, no message needed
                    broadcastTaskResult(null);
                }
            });
        }
    }
    //endregion

    // ########## Notifications setup and Implementation ##########
    //region Notifications
    private void setupAndStartNotificationsInForeground(){
        notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());

        setUpNotificationChannel();

        Notification notification = setupNotification("Learn a new word!",
                "Word to learn will be displayed here");

        notificationManagerCompat.notify(NOTIFICATION_ID, notification);
        startForeground(NOTIFICATION_ID, notification);
    }

    private void setUpNotificationChannel(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) { //needed because channels are not supported on older versions

            NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    private Notification setupNotification(String title, String contentText){
        return new NotificationCompat.Builder(getApplicationContext(),
                NOTIFICATION_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.word_learner_round)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
    }

    // ########## Recursive Notification update implementation ##########
    // Ref SMAP L5 ServicesDemo code
    private void recursiveUpdateNotification(){
        if (notificationUpdateExecutor == null){
            notificationUpdateExecutor = Executors.newSingleThreadExecutor();
        }

        notificationUpdateExecutor.submit(waitAndUpdateNotification);
    }

    // Ref SMAP L5 ServicesDemo code
    private Runnable waitAndUpdateNotification = new Runnable(){
        @Override
        public void run() {
            Log.d(TAG, "waitAndUpdateNotification: called");
            try {
                Log.d(TAG, "Sleeping for 10 secs");
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Log.d(TAG, "run: did not finish, som error occurred");
                e.printStackTrace();
            }

            // Get an index no higher than the current size of the allWords list
            int randWordIndex = new Random().nextInt(allWords.size());
            Word randomWord = allWords.get(randWordIndex);

            Notification updatedNotification = setupNotification("New word to learn!",
                    randomWord.getWord());

            notificationManagerCompat.notify(NOTIFICATION_ID, updatedNotification);

            if (serviceIsRunning){
                recursiveUpdateNotification();
            }
        }
    };
    //endregion

    // ########## Stethos implementation ##########
    //region Stethos implementation
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
    //endregion
}
