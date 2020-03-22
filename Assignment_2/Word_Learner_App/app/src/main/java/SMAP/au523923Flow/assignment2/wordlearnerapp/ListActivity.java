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

    // Wordlist
    List<Word> wordListItems = new ArrayList<>();

    // ----- Service Binding ------
    ServiceConnection connection;
    private WordLearnerService wordLearnerService;
    boolean boundToService = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initUI();
        
        Intent backgroundServiceIntent = startService();
        setupServiceConn();
        bindToService(backgroundServiceIntent);
    }

    

    // ------------ Service functionality -------------------
    Intent startService() {
        // Start service
        Intent backgroundServiceIntent = new Intent(ListActivity.this, WordLearnerService.class);
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        } else {
            Log.d(TAG, "Shitty ass android version. Starting service explicity");
            startService(backgroundServiceIntent);
        }

        return backgroundServiceIntent;
    }

    
    private void setupServiceConn() {

        // TODO: Maybe delete?
        // IntentFilter filter = new IntentFilter();
        // filter.addAction(Globals.BROADCAST_WORDLEARNERSERVICE);

        //can use registerReceiver(...)
        //but using local broadcasts for this service:
        

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

                listenToWordUpdate();

            }

            @Override
            public void onServiceDisconnected(ComponentName className) {
                boundToService = false;
                Log.d(TAG, "Disconnected fromservice");
            }
        };
    }


    private void bindToService(Intent backgroundServiceIntent) {
        
        // bindService(backgroundServiceIntent, connection, Context.BIND_AUTO_CREATE);
        bindService(new Intent(ListActivity.this, WordLearnerService.class), connection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "Binded to Wordservice");
    }

    
    private void listenToWordUpdate() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Globals.BROADCAST_WORDLEARNERSERVICE);

        LocalBroadcastManager.getInstance(this).registerReceiver(onBackgroundServiceResult, filter);
    }
    
    

    //define our broadcast receiver for (local) broadcasts.
    // Registered and unregistered in onStart() and onStop() methods
    private BroadcastReceiver onBackgroundServiceResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // List<Word> wordListItems = initWordListData(savedInstanceState);
            List<Word> wordListItems = wordLearnerService.getAllWordsFromDB();
            
            if (wordListItems == null) {
                Log.d(TAG, "Failed to get wordListItems. Reason: Null reference ");
                return;
            }

            Log.d(TAG, "Got wordlist - Length: " + wordListItems.size());

            // Update adapter to show new words
            adapter.setWordList(wordListItems);
            adapter.notifyDataSetChanged();
        }
    };

    private List<Word> initWordListData(Bundle savedInstanceState){
        if (savedInstanceState != null)
            return savedInstanceState
                    .getParcelableArrayList(getString(R.string.WORD_LIST_ARRAY));
        else{
            //DataHelper data = new DataHelper(this);
            return wordLearnerService.getAllWordsFromDB();
        }
    }

    // -------------- Activity life cycles --------------
    //region Activity life cycles
    @Override
    protected void onStart() {
        super.onStart();
        // Intent intent = new Intent(ListActivity.this, WordLearnerService.class);
        // bindService(intent, connection, Context.BIND_AUTO_CREATE);

        // IntentFilter filter = new IntentFilter();
        // filter.addAction(Globals.BROADCAST_WORDLEARNERSERVICE);

        // //can use registerReceiver(...)
        // //but using local broadcasts for this service:
        // LocalBroadcastManager.getInstance(this).registerReceiver(onBackgroundServiceResult, filter);
    }
     
    
    @Override
    protected void onStop() {
        wordLearnerService = null;
        unbindService(connection);
        boundToService = false;
        super.onStop();
    }

    //endregion

    // ------------- UI STUFF -------------------
    //region UI
    
    // Initilize all the ui
    private void initUI() {
        exitBtn = findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(exitBtnListener);
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
                wordLearnerService.deleteWord("vial");
            }
        });

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
}

// Changed the file animal_list.csv according to the info on this post
// https://stackoverflow.com/questions/8432584/how-to-make-notepad-to-save-text-in-utf-8-without-bom
// and it worked, since it didn't read the lion correctly.
// Also kudo picture and word were not the same string so changed it to kudo, to match the drawable
