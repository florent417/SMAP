package SMAP.au523923Flow.assignment2.wordlearnerapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Word;
import SMAP.au523923Flow.assignment2.wordlearnerapp.service.WordLearnerService;
import SMAP.au523923Flow.assignment2.wordlearnerapp.service.WordLearnerService.LocalBinder;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.ApplicationRunChecker;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.Globals;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

// Do not count on Onstop for data clean up since it never gets called
public class ListActivity extends AppCompatActivity {
    private static final String TAG = "ListActivity";

    // ########## UI Variables ##########
    private RecyclerView recyclerView;
    private WordListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button exitBtn;
    private Button addBtn;
    private EditText userSearchWord;

    // ########## Words ###########
    List<Word> wordListItems = new ArrayList<>();

    // ########## Service Binding Variables ##########
    ServiceConnection connection;
    private WordLearnerService wordLearnerService;
    boolean boundToService = false;

    // ########## Activity life cycles ##########
    //region Activity life cycles
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        startServiceAsForegroundService();

        initUI();

        setupServiceConn();
    }

    // Works best if the methods run on start. Gives a little delay to be able to connect
    @Override
    protected void onStart() {
        registerBroadcastWordsUpdateListener();
        bindToWordLearnerService();
        super.onStart();
    }

    @Override
    protected void onStop() {
        wordLearnerService = null;
        unbindService(connection);
        boundToService = false;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onWordLearnerBroadcastResult);
        super.onStop();
    }
    //endregion

    // ########## Service functionality ##########
    //region Service functionality
    // Ref: SMAP L5 ServicesDemo code
    private void setupServiceConn() {
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName className,
                                        IBinder service) {
                Log.d(TAG, "Connected to service");
                // We've bound to LocalService, cast the IBinder and get LocalService instance
                LocalBinder binder = (LocalBinder) service;
                wordLearnerService = binder.getService();
                boundToService = true;

                List<Word> allWords = wordLearnerService.getAllWords();
                wordListItems = allWords != null ? allWords : new ArrayList<Word>();
                updateAdapterWithWords(wordListItems);
            }

            @Override
            public void onServiceDisconnected(ComponentName className) {
                boundToService = false;
                wordLearnerService = null;
                Log.d(TAG, "Disconnected from service");
            }
        };
    }

    private void bindToWordLearnerService() {
        Intent bindServiceIntent = new Intent(ListActivity.this, WordLearnerService.class);
        bindService(bindServiceIntent, connection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "Binded to Word Learner Service");
    }

    private void registerBroadcastWordsUpdateListener (){
        IntentFilter filter = new IntentFilter();
        filter.addAction(Globals.BROADCAST_WORDLEARNERSERVICE);

        LocalBroadcastManager.getInstance(this).registerReceiver(onWordLearnerBroadcastResult, filter);
    }

    public void startServiceAsForegroundService(){
        boolean serviceAlreadyRunning = ApplicationRunChecker.getForegroundServiceRunning(getApplicationContext(),
                Globals.WORD_LEARNER_SERVICE_RUNNING);

        // Only start foreground service if its not running
        if (!serviceAlreadyRunning) {
            Intent startServiceIntent = new Intent(ListActivity.this, WordLearnerService.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(startServiceIntent);
            } else {
                startService(startServiceIntent);
            }

            // Set to true when it is set to run as a foreground service
            ApplicationRunChecker.setForegroundServiceRunning(getApplicationContext(),
                    Globals.WORD_LEARNER_SERVICE_RUNNING, true);
        }
    }

    // ########## Broadcast receiver implementation ##########
    private BroadcastReceiver onWordLearnerBroadcastResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<Word> allWords = wordLearnerService.getAllWords();

            if (allWords == null) {
                Log.d(TAG, "Failed to get wordListItems. Reason: Null reference ");
                wordListItems = new ArrayList<>();
            }
            else{
                wordListItems = allWords;
            }

            Log.d(TAG, "Wordlist Length: " + wordListItems.size());

            // Show message to user if message exists
            String messageToUser = intent.getStringExtra(Globals.MSG_TO_USER);
            if (messageToUser != null)
                Toast.makeText(ListActivity.this, messageToUser, Toast.LENGTH_SHORT).show();

            // Update adapter to show new words
            updateAdapterWithWords(wordListItems);
        }
    };
    //endregion

    // ########## UI Implementation and functionality ##########
    //region UI
    private void initUI() {
        userSearchWord = findViewById(R.id.searchWordInput);
        addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(addBtnListener);
        exitBtn = findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(exitBtnListener);

        setUpRecyclerView();
    }

    // ########## Recycler view and Adapter implementation
    private void setUpRecyclerView(){
        // The implementation for adding a layout manager and adapter to recycler view,
        // is influenced by this site
        // https://www.tutorialspoint.com/how-to-use-constraint-layout-with-recyclerview
        recyclerView = findViewById(R.id.wordItemList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WordListAdapter(this, wordListItems);
        adapter.setOnItemListClickListener(onItemListClickListener);
        recyclerView.setAdapter(adapter);
    }

    private void updateAdapterWithWords(List<Word> words){
        adapter.setWordList(words);
        adapter.notifyDataSetChanged();
    }

    // ########## OnClickListener implementations ##########
    //region OnClickListener implementations
    private View.OnClickListener exitBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finishAffinity(); // Finish activities
            System.exit(0); // Terminates the process/app
            //Runtime.getRuntime().exit(0); // the same as System, but better description of method
        }
    };

    private View.OnClickListener addBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Ref: https://stackoverflow.com/questions/5238491/check-if-string-contains-only-letters
            // comment by shalamus
            // Do note, it only checks for the english alphabet!
            String searchWord = userSearchWord.getText().toString();
            if (Pattern.matches("[a-zA-Z]+", searchWord) && searchWord != ""){
                wordLearnerService.addWord(searchWord);
            }
            else{
                Toast.makeText(ListActivity.this,
                        "Search word is empty or contains non letters", Toast.LENGTH_LONG).show();
            }
        }
    };

    // Making sure that callback for onClickListener implementation is in activity, and not in adapter
    private WordListAdapter.OnItemListClickListener onItemListClickListener = new WordListAdapter.OnItemListClickListener() {
        @Override
        public void onItemListClick(int position) {
            Intent intent = new Intent(ListActivity.this, DetailsActivity.class);

            Word wordItem = adapter.getWordListItem(position);

            intent.putExtra(Globals.CHOSEN_WORD,wordItem.getWord());

            startActivityForResult(intent, Globals.EDIT_REQ);
        }
    };
    //endregion

    //endregion

    // TODO: Don't think this is needed
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}