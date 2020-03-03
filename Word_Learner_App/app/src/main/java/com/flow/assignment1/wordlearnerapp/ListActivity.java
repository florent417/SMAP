package com.flow.assignment1.wordlearnerapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private WordListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button exitBtn;
    // Should this be a resource?
    private final int EDIT_REQ = 1;
    ArrayList<WordListItem> wordListItems = new ArrayList<WordListItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        if (savedInstanceState != null)
            wordListItems = savedInstanceState
                    .getParcelableArrayList(getString(R.string.WORD_LIST_ARRAY));
        else{
            DataHelper data = new DataHelper(this);
            wordListItems = data.addDataFromFile();
        }

        // The implementation for adding a layout manager and adapter to recycler view,
        // is influenced by this site
        // https://www.tutorialspoint.com/how-to-use-constraint-layout-with-recyclerview
        recyclerView = findViewById(R.id.wordItemList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WordListAdapter(this, wordListItems);
        adapter.setOnItemListClickListener(onItemListClickListener);
        recyclerView.setAdapter(adapter);

        exitBtn = findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(exitBtnListener);
    }

    // Parcelable simplifies saving all list items
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
            // Updatiing the position on the model itself, makes it seperate concern for other classes
            // because the position is needed onActivityResult, to update the single item
            wordItem.setWordPosition(position);

            intent.putExtra(getString(R.string.WORD_LIST_ITEM),wordItem);

            startActivityForResult(intent, EDIT_REQ);
        }
    };

    // Getting the data back from editactivity and updating the list
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_REQ && resultCode == RESULT_OK){
            if (data != null){
                WordListItem wordListItem = (WordListItem) data.getParcelableExtra(getString(R.string.WORD_LIST_ITEM));
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


}

// Changed the file animal_list.csv according to the info on this post
// https://stackoverflow.com/questions/8432584/how-to-make-notepad-to-save-text-in-utf-8-without-bom
// and it worked, since it didn't read the lion correctly.
// Also kudo picture and word were not the same string so changed it to kudo, to match the drawable
