package com.flow.lab6.arnoldmoviecollection;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.flow.lab6.arnoldmoviecollection.adapters.OverviewListAdapter;

import java.util.ArrayList;

import model.Movie;

public class OverviewFragment extends Fragment {


    private ArrayList<Movie> movies;
    private View view;
    private ListView list_movies;
    //private TextView name, year, role;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_overview, container, false);

        setupMovies();

        list_movies = view.findViewById(R.id.OverviewList);

        OverviewListAdapter adapter = new OverviewListAdapter(movies, getActivity());

        list_movies.setAdapter(adapter);
        return view;

    }

    private void setupMovies(){
        movies = new ArrayList<Movie>();
        for(int i = 0; i < 10; i++){
            Movie newMov = new Movie("Test", 1995, "Main Actor");
            movies.add(newMov);
        }
    }
}
