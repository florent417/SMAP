package com.flow.assignment1.wordlearnerapp;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

/*
* The idea on how to implement adapter with recycler view is influenced by this playlist on YT
* https://www.youtube.com/watch?v=5T144CbTwjc&list=PLk7v1Z2rk4hjHrGKo9GqOtLs1e2bglHHA&index=2&fbclid=IwAR16HBg3NMwz2uDT9gbiUgP6QquDEVK5S1UEx3nz49kTvtU_Wisl9XpowUc*/

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
        View wordListItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.word_item,parent,false);

        return new ViewHolder(wordListItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WordListItem wordListItem = wordListItems.get(position);

        holder.word.setText(wordListItem.getWord());
        holder.pronunciation.setText(wordListItem.getPronunciation());
        holder.wordRating.setText(wordListItem.getRating());
        holder.imgView.setImageResource(wordListItem.getImgResNbr());
        holder.imageResNbr = wordListItem.getImgResNbr();
    }

    @Override
    public int getItemCount() {
        return wordListItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView word, pronunciation, wordRating;
        private ImageView imgView;
        String wordKey, pronunciationKey, imgResNbrKey, ratingKey;
        // Should this be a resource?
        private final int EDIT_REQ = 1;
        // for image resource number
        private int imageResNbr;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            word = itemView.findViewById(R.id.word);
            pronunciation = itemView.findViewById(R.id.pronounciation);
            wordRating = itemView.findViewById(R.id.wordRating);
            imgView = itemView.findViewById(R.id.wordImage);

            // Resources implementation influenced from here
            // https://stackoverflow.com/questions/41007837/how-to-use-getresources-on-a-adapter-java
            wordKey = itemView.getContext().getString(R.string.wordExtra);
            pronunciationKey = itemView.getContext().getString(R.string.pronunciationExtra);
            imgResNbrKey = itemView.getContext().getString(R.string.imgResNbrExtra);
            ratingKey = itemView.getContext().getString(R.string.ratingExtra);

            // The implementation for onclicklisteners is influenced by this
            // https://medium.com/@CodyEngel/4-ways-to-implement-onclicklistener-on-android-9b956cbd2928
            itemView.setOnClickListener(itemViewClickListener);
        }
        // Code was inspired by this post:
        // https://stackoverflow.com/questions/48696477/how-to-implement-startactivityforresult-in-recyclerview
        private View.OnClickListener itemViewClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View wordItemView) {
                Intent intent = new Intent(wordItemView.getContext(), DetailsActivity.class);

                intent.putExtra(wordKey, word.getText().toString());
                intent.putExtra(pronunciationKey, pronunciation.getText().toString());
                intent.putExtra(imgResNbrKey, imageResNbr);
                intent.putExtra(ratingKey, wordRating.getText().toString());

                ((Activity) wordItemView.getContext()).startActivityForResult(intent,EDIT_REQ);
            }
        };
    }
}
