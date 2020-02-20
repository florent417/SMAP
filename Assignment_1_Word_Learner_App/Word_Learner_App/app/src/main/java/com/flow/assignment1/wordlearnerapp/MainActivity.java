package com.flow.assignment1.wordlearnerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Following", "On create");

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

        recyclerView = findViewById(R.id.wordItemList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WordListAdapter(this, wordListItems);
        recyclerView.setAdapter(adapter);
    }
}
