package com.flow.lab6.arnoldmoviecollection.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.flow.lab6.arnoldmoviecollection.R;

import java.util.List;

import model.Movie;

public class OverviewListAdapter extends BaseAdapter {
    private List<Movie> movies;
    private Context context;

    public OverviewListAdapter(List<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (movies == null)
            return 0;

        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        if (movies != null && movies.size() > position)
            return movies.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public List<Movie> getMovies(){
        return movies;
    }

    public void setMovies(List<Movie> movies){
        this.movies = movies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater;
            inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.movie_list_item, null);
        }

        if (movies != null && movies.size() > position){
            Movie tempMovie = movies.get(position);
            // Update views

            TextView name = convertView.findViewById(R.id.movName);
            name.setText(tempMovie.getName());
            TextView year = convertView.findViewById(R.id.movYear);
            year.setText(Integer.toString(tempMovie.getYear()));
            TextView role = convertView.findViewById(R.id.movRole);
            role.setText(tempMovie.getRole());
            return convertView;
        }
        return null;
    }
}
