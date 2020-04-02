package com.flow.lab6.arnoldmoviecollection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import model.Movie;

public class MainActivity extends AppCompatActivity {

    private final String OVERVIEW_FRAG = "overview_fragment";

    private OverviewFragment movieList;

    private ListView listContainer;

    private int selectedMovieIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listContainer = findViewById(R.id.OverviewList);

        selectedMovieIndex = 0;

        movieList = new OverviewFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.MainLayout, movieList, OVERVIEW_FRAG).commit();

    }


}
