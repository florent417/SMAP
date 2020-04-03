package SMAP.au523923Flow.assignment2.wordlearnerapp.adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import SMAP.au523923Flow.assignment2.wordlearnerapp.R;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Word;

import java.util.List;

// The idea on how to implement adapter with recycler view is influenced by this playlist on YT
// https://www.youtube.com/watch?v=5T144CbTwjc&list=PLk7v1Z2rk4hjHrGKo9GqOtLs1e2bglHHA&index=2&fbclid=IwAR16HBg3NMwz2uDT9gbiUgP6QquDEVK5S1UEx3nz49kTvtU_Wisl9XpowUc*/
public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder> {
    private String TAG = "WordListAdapter";

    private List<Word> wordListItems;
    private Context context;

    // The implementation on onClickListeners with adapters and RecyclerViews is influenced by:
    // https://www.youtube.com/watch?v=WtLZK1kh-yM
    private OnItemListClickListener mOnItemListClick;
    public interface OnItemListClickListener {
        void onItemListClick(int position);
    }

    public WordListAdapter(Context context, List<Word> wordListItems) {
        this.wordListItems = wordListItems;
        this.context = context;
    }

    // ########## Getters and setters ##########
    //region Getters and Setters
    public void setOnItemListClickListener(OnItemListClickListener itemListClickListener){
        mOnItemListClick = itemListClickListener;
    }

    public Word getWordListItem(int position){
        return wordListItems.get(position);
    }

    public void setWordList(List<Word> wordList) {
        Log.d(TAG, "Adapter: WordList updated");
        wordListItems = wordList;
    }

    @Override
    public int getItemCount() {
        return wordListItems.size();
    }
    //endregion

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View wordListItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.word_item,parent,false);

        return new ViewHolder(wordListItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Word wordListItem = wordListItems.get(position);

        holder.word.setText(wordListItem.getWord());
        holder.pronunciation.setText(wordListItem.getPronunciation());
        holder.wordRating.setText(wordListItem.getRating());
        // Get img url to set img in ImageView
        String imageUrl = wordListItem.getFirstDefinition().getImageUrl();
        // By calling load() with Picasso, it gets executed asynchronously
        // and the ImageView gets the image after it gets loaded.
        // If an error occurs or there is no imageURL, the image is set to a default drawable.
        // The images get cached as well so its not necessary to get the images from the url
        // all the time
        // Ref : https://guides.codepath.com/android/Displaying-Images-with-the-Picasso-Library
        Picasso.with(context)
                .load(imageUrl)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(holder.imgView);
    }

    // ########## ViewHolder implementation ##########
    //region ViewHolder implementation
    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView word, pronunciation, wordRating;
        private ImageView imgView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            word = itemView.findViewById(R.id.word);
            pronunciation = itemView.findViewById(R.id.pronounciation);
            wordRating = itemView.findViewById(R.id.wordRating);
            imgView = itemView.findViewById(R.id.wordImage);

            itemView.setOnClickListener(itemOnClickListener);
        }

        private View.OnClickListener itemOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemListClick != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        mOnItemListClick.onItemListClick(position);
                    }
                }
            }
        };
    }
    //endregion
}
