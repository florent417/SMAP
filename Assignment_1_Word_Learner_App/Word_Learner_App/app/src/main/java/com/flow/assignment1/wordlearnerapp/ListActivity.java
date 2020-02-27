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

        exitBtn = findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(exitBtnListener);
    }

    private WordListAdapter.OnItemListClickListener onItemListClickListener = new WordListAdapter.OnItemListClickListener() {
        @Override
        public void onItemListClick(int position) {
            Intent intent = new Intent(ListActivity.this, DetailsActivity.class);

            WordListItem wordItem = adapter.getWordListItem(position);
            wordItem.setWordPosition(position);

            intent.putExtra(getString(R.string.WORD_LIST_ITEM),wordItem);
            /*
            intent.putExtra(wordKey, wordItem.getWord());
            intent.putExtra(pronunciationKey, wordItem.getPronunciation());
            intent.putExtra(imgResNbrKey, wordItem.getImgResNbr());
            intent.putExtra(ratingKey, wordItem.getRating());
            intent.putExtra(positionKey, wordItem.getWordPosition());
            intent.putExtra(getString(R.string.NOTES_EXTRA), wordItem.getNotes());
            */

            startActivityForResult(intent, EDIT_REQ);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_REQ && resultCode == RESULT_OK){
            if (data != null){

                WordListItem wordListItem = (WordListItem) data.getParcelableExtra(getString(R.string.WORD_LIST_ITEM));
                adapter.updateWordListItem(wordListItem.getWordPosition(), wordListItem);

                /*
                WordListItem wordListItem = adapter.getWordListItem(data
                        .getIntExtra(getString(R.string.POSITION_EXTRA),0));

                wordListItem.setRating(data.getStringExtra(getString(R.string.RATING_EXTRA)));
                wordListItem.setNotes(data.getStringExtra(getString(R.string.NOTES_EXTRA)));

                adapter.updateWordListItem(data.getIntExtra(
                        getString(R.string.POSITION_EXTRA),0), wordListItem);

                 */

                adapter.notifyDataSetChanged();
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

    public ArrayList<WordListItem> InitWordList(){
        ArrayList<WordListItem> wordListItems = new ArrayList<WordListItem>();
        wordListItems.add(new WordListItem("buffalo","something", R.drawable.buffalo));
        wordListItems.add(new WordListItem("camel","something else", R.drawable.camel));
        wordListItems.add(new WordListItem("cheetah","something 3", R.drawable.cheetah));
        wordListItems.add(new WordListItem("buffalo","something", R.drawable.buffalo));
        wordListItems.add(new WordListItem("camel","something else", R.drawable.camel));
        wordListItems.add(new WordListItem("cheetah","something 3", R.drawable.cheetah));
        wordListItems.add(new WordListItem("buffalo","something", R.drawable.buffalo));
        wordListItems.add(new WordListItem("camel","something else", R.drawable.camel));
        wordListItems.add(new WordListItem("cheetah","something 3", R.drawable.cheetah));
        wordListItems.add(new WordListItem("buffalo","something", R.drawable.buffalo));
        wordListItems.add(new WordListItem("camel","something else", R.drawable.camel));
        wordListItems.add(new WordListItem("cheetah","something 3", R.drawable.cheetah));
        wordListItems.add(new WordListItem("buffalo","something", R.drawable.buffalo));
        wordListItems.add(new WordListItem("camel","something else", R.drawable.camel));
        wordListItems.add(new WordListItem("cheetah","something 3", R.drawable.cheetah));
        return wordListItems;
    }
}
