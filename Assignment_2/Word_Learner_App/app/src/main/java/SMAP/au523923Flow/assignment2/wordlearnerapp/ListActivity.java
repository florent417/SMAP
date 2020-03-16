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
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import SMAP.au523923Flow.assignment2.wordlearnerapp.data.WordLearnerDatabase;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Definition;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Word;
import SMAP.au523923Flow.assignment2.wordlearnerapp.service.WordLearnerService;
import SMAP.au523923Flow.assignment2.wordlearnerapp.service.WordLearnerService.LocalBinder;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.DataHelper;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.WordListItem;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.WordJsonParser;

import java.util.ArrayList;

// Do not count on Onstop for data clean up since it never gets called

public class ListActivity extends AppCompatActivity {
    private static final String TAG = "ListActivity";
    private WordLearnerService wordLearnerService;
    boolean mBound = false;
    private RecyclerView recyclerView;
    private WordListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button exitBtn;
    private Button testBtn;
    // Should this be a resource?
    private final int EDIT_REQ = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ArrayList<WordListItem> wordListItems = initWordListData(savedInstanceState);

        setUpRecyclerView(wordListItems);

        exitBtn = findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(exitBtnListener);
        testBtn = findViewById(R.id.testBtn);
        testBtn.setOnClickListener(testBtnListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(ListActivity.this, WordLearnerService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(WordLearnerService.BROADCAST_WORDLEARNERSERVICE);

        //can use registerReceiver(...)
        //but using local broadcasts for this service:
        LocalBroadcastManager.getInstance(this).registerReceiver(onBackgroundServiceResult, filter);
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            wordLearnerService = binder.getService();
            mBound = true;
            //TODO: probably a good place to update UI after data loading
            // E.g. get from database

        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            mBound = false;
        }
    };

    private void setUpRecyclerView(ArrayList<WordListItem> wordListItems){
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

    private ArrayList<WordListItem> initWordListData(Bundle savedInstanceState){
        if (savedInstanceState != null)
            return savedInstanceState
                    .getParcelableArrayList(getString(R.string.WORD_LIST_ARRAY));
        else{
            DataHelper data = new DataHelper(this);
            return data.addDataFromFile();
        }
    }

    // Parcelable simplifies saving all list items
    // Probably still usefull since we dont want to get from database everytime we turn
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.WORD_LIST_ARRAY),adapter.getWordListItems());
    }

    // Making sure the on click implementation is in activity, and not adapter
    private WordListAdapter.OnItemListClickListener onItemListClickListener = new WordListAdapter.OnItemListClickListener() {
        @Override
        public void onItemListClick(int position) {
            Intent intent = new Intent(ListActivity.this, DetailsActivity.class);

            WordListItem wordItem = adapter.getWordListItem(position);
            // Updating the position on the model itself, makes it separate concern for other classes
            // because the position is needed onActivityResult, to update the single item
            wordItem.setWordPosition(position);

            intent.putExtra(getString(R.string.WORD_LIST_ITEM),wordItem);

            startActivityForResult(intent, EDIT_REQ);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_REQ && resultCode == RESULT_OK){
            if (data != null){
                WordListItem wordListItem = data.getParcelableExtra(getString(R.string.WORD_LIST_ITEM));
                adapter.updateWordListItem(wordListItem.getWordPosition(), wordListItem);
                // Makes sure the list/adapter updates
                adapter.notifyDataSetChanged();
            }
        }
    }

    private View.OnClickListener exitBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finishAffinity(); // Finish activities
            System.exit(0); // Terminates the process/app
            //Runtime.getRuntime().exit(0); // the same as System, but better description of method
        }
    };
    private View.OnClickListener testBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            wordLearnerService.testAddWord("lion");
        }
    };

    @Override
    protected void onStop() {
        wordLearnerService = null;
        unbindService(connection);
        mBound = false;
        super.onStop();
    }

    //define our broadcast receiver for (local) broadcasts.
    // Registered and unregistered in onStart() and onStop() methods
    private BroadcastReceiver onBackgroundServiceResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Word test = wordLearnerService.testGetWord("lion");
            // Inspired by and some stuff copied from servicesdemo code from L5 in SMAP
            new AsyncTask<String, Void, Word>() {
                @Override
                protected Word doInBackground(String... strings) {
                    // Check if word is already in database orrrr in the list that is in the service
                    Word wordObj = WordLearnerDatabase.getWordDbInstance(getApplicationContext())
                            .wordDAO().getWord(strings[0]);
                    return wordObj;
                }

                @Override
                protected void onPostExecute(Word word) {
                    Toast.makeText(ListActivity.this, word.getWord(), Toast.LENGTH_LONG).show();
                }
            }.execute("lion");
        }
    };


}

// Changed the file animal_list.csv according to the info on this post
// https://stackoverflow.com/questions/8432584/how-to-make-notepad-to-save-text-in-utf-8-without-bom
// and it worked, since it didn't read the lion correctly.
// Also kudo picture and word were not the same string so changed it to kudo, to match the drawable
