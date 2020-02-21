package com.flow.assignment1.wordlearnerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button exitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ArrayList<WordListItem> wordListItems = new ArrayList<WordListItem>();
        wordListItems.add(new WordListItem("buffalo","something", "2",R.drawable.buffalo));
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
        // The implementation for adding a layout manager and adapter to recycler view,
        // is influenced byt this site
        // https://www.tutorialspoint.com/how-to-use-constraint-layout-with-recyclerview
        recyclerView = findViewById(R.id.wordItemList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WordListAdapter(this, wordListItems);
        recyclerView.setAdapter(adapter);

        exitBtn = findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(exitBtnListener);


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
}
