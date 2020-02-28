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

    public ArrayList<WordListItem> InitWordList(){
        String descrTest = "a large, long-necked ungulate mammal of arid country, with long slender legs, broad cushioned feet, and either one or two humps on the back. Camels can survive for long periods without food or drink, chiefly by using up the fat reserves in their humps.";
        ArrayList<WordListItem> wordListItems = new ArrayList<WordListItem>();
        WordListItem buffalo = new WordListItem("buffalo","something", R.drawable.buffalo);
        buffalo.setDescription(descrTest);
        wordListItems.add(buffalo);
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

        for (WordListItem item: wordListItems) {
            item.setDescription(descrTest);
        }
        return wordListItems;
    }
}
