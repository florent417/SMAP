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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import SMAP.au523923Flow.assignment2.wordlearnerapp.data.WordLearnerDatabase;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Word;
import SMAP.au523923Flow.assignment2.wordlearnerapp.service.WordLearnerService;
import SMAP.au523923Flow.assignment2.wordlearnerapp.service.WordLearnerService.LocalBinder;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.ApplicationFirstRunChecker;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.DataHelper;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.WordListItem;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.Globals;

import java.util.ArrayList;
import java.util.List;

// Do not count on Onstop for data clean up since it never gets called
public class ListActivity extends AppCompatActivity {
    private static final String TAG = "ListActivity";

    // ----- UI Stuff ------
    private RecyclerView recyclerView;
    private WordListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button exitBtn;
    private Button testAddBtn;
    private Button testDelBtn;
    // Should this be a resource?
    private final int EDIT_REQ = 1;

    List<Word> wordListItems = new ArrayList<>();

    // ----- Service Binding ------
    ServiceConnection connection;
    private WordLearnerService wordLearnerService;
    boolean boundToService = false;

    //region Activity life cycles

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        if (savedInstanceState != null){
            wordListItems = savedInstanceState.getParcelableArrayList(getString(R.string.WORD_LIST_ARRAY));
        }

        // THIS WORKS!!
        // TODO: Create function, and check if service is already started
        Intent startServiceIntent = new Intent(ListActivity.this, WordLearnerService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(startServiceIntent);
        }
        else {
            startService(startServiceIntent);
        }

        initUI();
        setupServiceConn();
        registerBroadcastWordsUpdateListener();
        bindToWordLearnerService();

        // If Service is running (check) getallwords
    }

    // TODO: Maybe move to unpaused?
    @Override
    protected void onStop() {
        wordLearnerService = null;
        unbindService(connection);
        boundToService = false;
        super.onStop();
    }


    //endregion

    
    //region Service functionality
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
                //TODO: probably a good place to update UI after data loading
                // E.g. get from database

                //wordListItems = wordLearnerService.getAllWordsFromDB();
                //for (Word word : wordListItems){ wordLearnerService.deleteWord(word.getWord());}
                //updateAdapterWithWords(wordListItems);
            }

            @Override
            public void onServiceDisconnected(ComponentName className) {
                boundToService = false;
                Log.d(TAG, "Disconnected from service");
            }
        };
    }

    private void registerBroadcastWordsUpdateListener (){
        IntentFilter filter = new IntentFilter();
        filter.addAction(Globals.BROADCAST_WORDLEARNERSERVICE);

        LocalBroadcastManager.getInstance(this).registerReceiver(onBackgroundServiceResult, filter);
    }

    private void bindToWordLearnerService() {
        Intent bindServiceIntent = new Intent(ListActivity.this, WordLearnerService.class);
        bindService(bindServiceIntent, connection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "Binded to Word Learner Service");
    }

    //define our broadcast receiver for (local) broadcasts.
    // Registered and unregistered in onStart() and onStop() methods
    private BroadcastReceiver onBackgroundServiceResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // List<Word> wordListItems = initWordListData(savedInstanceState);
            // prob db words instead
            wordListItems = wordLearnerService.getWords();
            
            if (wordListItems == null) {
                Log.d(TAG, "Failed to get wordListItems. Reason: Null reference ");
                return;
            }

            Log.d(TAG, "Got wordlist - Length: " + wordListItems.size());

            // Update adapter to show new words
            updateAdapterWithWords(wordListItems);
        }
    };

    private void updateAdapterWithWords(List<Word> words){
        adapter.setWordList(words);
        adapter.notifyDataSetChanged();
    }
    //endregion
    /*
    private List<Word> initWordListData(Bundle savedInstanceState){
        if (savedInstanceState != null)
            return savedInstanceState
                    .getParcelableArrayList(getString(R.string.WORD_LIST_ARRAY));
        else{
            //DataHelper data = new DataHelper(this);
            return wordLearnerService.getAllWordsFromDB();
        }
    }

     */

    //region UI

    private void initUI() {
        exitBtn = findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(exitBtnListener);
        /*
        testAddBtn = findViewById(R.id.testAddBtn);
        testDelBtn = findViewById(R.id.testDelBtn);
        testAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordLearnerService.addWord("vial");
            }
        });
        testDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordLearnerService.deleteWord("giraffe");
            }
        });
        */
        // Move this
        setUpRecyclerView();
    }

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

    private View.OnClickListener exitBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finishAffinity(); // Finish activities
            System.exit(0); // Terminates the process/app
            //Runtime.getRuntime().exit(0); // the same as System, but better description of method
        }
    };

    // Making sure the on click implementation is in activity, and not adapter
    private WordListAdapter.OnItemListClickListener onItemListClickListener = new WordListAdapter.OnItemListClickListener() {
        @Override
        public void onItemListClick(int position) {
            Intent intent = new Intent(ListActivity.this, DetailsActivity.class);

            Word wordItem = adapter.getWordListItem(position);
            // Updating the position on the model itself, makes it separate concern for other classes
            // because the position is needed onActivityResult, to update the single item
            //wordItem.setWordPosition(position);

            intent.putExtra(getString(R.string.WORD_LIST_ITEM),wordItem);

            startActivityForResult(intent, EDIT_REQ);
        }
    };
    //endregion

    //region Saved instance state
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Word> savedInstanceStateList = new ArrayList<Word>(adapter.getWordListItems());
        outState.putParcelableArrayList(getString(R.string.WORD_LIST_ARRAY),savedInstanceStateList);
    }
    //endregion
}
