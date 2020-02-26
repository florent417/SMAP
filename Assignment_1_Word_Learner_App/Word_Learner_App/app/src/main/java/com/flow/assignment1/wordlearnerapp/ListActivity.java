package com.flow.assignment1.wordlearnerapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private WordListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button exitBtn;

    private String wordKey, pronunciationKey, imgResNbrKey, ratingKey, positionKey;
    // Should this be a resource?
    private final int EDIT_REQ = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ArrayList<WordListItem> wordListItems = InitWordList();

        // The implementation for adding a layout manager and adapter to recycler view,
        // is influenced byt this site
        // https://www.tutorialspoint.com/how-to-use-constraint-layout-with-recyclerview
        recyclerView = findViewById(R.id.wordItemList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WordListAdapter(this, wordListItems);
        adapter.setOnItemListClickListener(onItemListClickListener);
        recyclerView.setAdapter(adapter);

        // Resources implementation influenced from here
        // https://stackoverflow.com/questions/41007837/how-to-use-getresources-on-a-adapter-java
        wordKey = getString(R.string.WORD_EXTRA);
        pronunciationKey = getString(R.string.PRONUNCIATION_EXTRA);
        imgResNbrKey = getString(R.string.IMGRESNBR_EXTRA);
        ratingKey = getString(R.string.RATING_EXTRA);
        positionKey = getString(R.string.POSITION_EXTRA);

        exitBtn = findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(exitBtnListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_REQ && resultCode == RESULT_OK){
            if (data != null){
                WordListAdapter.ViewHolder wordItem = (WordListAdapter.ViewHolder) recyclerView
                        .findViewHolderForAdapterPosition(data.getIntExtra
                                (getString(R.string.POSITION_EXTRA),0));

                if (wordItem != null){
                    wordItem.wordRating.setText(data.getStringExtra(getString(R.string.RATING_EXTRA)));
                }
                WordListItem wordListItem = adapter.getWordListItem(data.getIntExtra(getString(R.string.POSITION_EXTRA),0));
                wordListItem.setRating(data.getStringExtra(getString(R.string.RATING_EXTRA)));
                adapter.wordListItems.set(data.getIntExtra(getString(R.string.POSITION_EXTRA),0), wordListItem);
            }
        }
    }

    private View.OnClickListener exitBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // One of these maybe, but so far not one exits the app completely
            //finish();
            finishAffinity();
            //System.exit(1);
        }
    };

    private WordListAdapter.OnItemListClickListener onItemListClickListener = new WordListAdapter.OnItemListClickListener() {
        @Override
        public void onItemListClick(int position) {
            // Dont't know if this is clever or it should be updated to do update on the arraylist
            // or if it should be added from the viewholder
            Intent intent = new Intent(ListActivity.this, DetailsActivity.class);
            //adapter.
            WordListItem test = adapter.getWordListItem(position);
            intent.putExtra(wordKey, test.getWord());
            intent.putExtra(pronunciationKey, test.getPronunciation());
            intent.putExtra(imgResNbrKey, test.getImgResNbr());
            intent.putExtra(ratingKey, test.getRating());
            intent.putExtra(positionKey, position);

            startActivityForResult(intent, EDIT_REQ);
        }
    };

    public ArrayList<WordListItem> InitWordList(){
        ArrayList<WordListItem> wordListItems = new ArrayList<WordListItem>();
        wordListItems.add(new WordListItem("buffalo","something", "8.0",R.drawable.buffalo));
        wordListItems.add(new WordListItem("camel","something else", "2.3",R.drawable.camel));
        wordListItems.add(new WordListItem("cheetah","something 3", "1",R.drawable.cheetah));wordListItems.add(new WordListItem("buffalo","something", "2",R.drawable.buffalo));
        wordListItems.add(new WordListItem("camel","something else", "2.3",R.drawable.camel));
        wordListItems.add(new WordListItem("cheetah","something 3", "1",R.drawable.cheetah));wordListItems.add(new WordListItem("buffalo","something", "2",R.drawable.buffalo));
        wordListItems.add(new WordListItem("camel","something else", "2.3",R.drawable.camel));
        wordListItems.add(new WordListItem("cheetah","something 3", "1",R.drawable.cheetah));wordListItems.add(new WordListItem("buffalo","something", "2",R.drawable.buffalo));
        wordListItems.add(new WordListItem("camel","something else", "2.3",R.drawable.camel));
        wordListItems.add(new WordListItem("cheetah","something 3", "1",R.drawable.cheetah));wordListItems.add(new WordListItem("buffalo","something", "2",R.drawable.buffalo));
        wordListItems.add(new WordListItem("camel","something else", "2.3",R.drawable.camel));
        wordListItems.add(new WordListItem("cheetah","something 3", "1",R.drawable.cheetah));
        return wordListItems;
    };
}
