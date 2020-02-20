package com.flow.assignment1.wordlearnerapp;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder> {
    private ArrayList<WordListItem> wordListItems;
    private Context context;

    public WordListAdapter(Context context, ArrayList<WordListItem> wordListItems) {
        this.wordListItems = wordListItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View newView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.word_item,parent,false);

        return new ViewHolder(newView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WordListItem wordListItem = wordListItems.get(position);

        holder.word.setText(wordListItem.getWord());
        holder.pronounciation.setText(wordListItem.getPronounciation());
        holder.wordRating.setText(wordListItem.getRating());
        holder.imgView.setImageResource(wordListItem.getImgResNbr());
    }

    @Override
    public int getItemCount() {
        return wordListItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView word, pronounciation, wordRating;
        private ImageView imgView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            word = (TextView) itemView.findViewById(R.id.word);
            pronounciation = (TextView) itemView.findViewById(R.id.pronounciation);
            wordRating = (TextView) itemView.findViewById(R.id.wordRating);
            imgView = (ImageView) itemView.findViewById(R.id.wordImage);
        }
    }
}
