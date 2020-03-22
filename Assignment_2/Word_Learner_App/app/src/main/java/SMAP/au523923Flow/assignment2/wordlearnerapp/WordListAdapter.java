package SMAP.au523923Flow.assignment2.wordlearnerapp;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Word;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.WordListItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

// The idea on how to implement adapter with recycler view is influenced by this playlist on YT
// https://www.youtube.com/watch?v=5T144CbTwjc&list=PLk7v1Z2rk4hjHrGKo9GqOtLs1e2bglHHA&index=2&fbclid=IwAR16HBg3NMwz2uDT9gbiUgP6QquDEVK5S1UEx3nz49kTvtU_Wisl9XpowUc*/
public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder> {
    private List<Word> wordListItems;
    private Context context;
    private OnItemListClickListener mOnItemListClick;
    private String TAG = "WordListAdapter";

    WordListAdapter(Context context, List<Word> wordListItems) {
        this.wordListItems = wordListItems;
        this.context = context;
    }

    // The implementation on onclicklisteners with adapters and recycleviews is influenced by:
    // https://www.youtube.com/watch?v=WtLZK1kh-yM
    public interface OnItemListClickListener {
        void onItemListClick(int position);
    }
    
    void setOnItemListClickListener(OnItemListClickListener itemListClickListener){
        mOnItemListClick = itemListClickListener;
    }

    

    Word getWordListItem(int position){
        return wordListItems.get(position);
    }
    void updateWordListItem(int position, Word wordItem){
        wordListItems.set(position,wordItem);
    }

    public void setWordList(List<Word> wordlist) {
        Log.d(TAG, "Adapter: Wordlist updated");
        wordListItems = wordlist;
    }

    List<Word> getWordListItems() {
        return wordListItems;
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
        Word wordListItem = wordListItems.get(position);

        holder.word.setText(wordListItem.getWord());
        holder.pronunciation.setText(wordListItem.getPronunciation());
        holder.wordRating.setText(wordListItem.getRating());
        try {
            ImageView i = holder.imgView;
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)
                    new URL(wordListItem.getFirstDefinition().getImageUrl()).getContent());
            i.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //holder.imgView.setImageResource(wordListItem.getImgResNbr());
    }

    @Override
    public int getItemCount() {
        return wordListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView word, pronunciation, wordRating;
        private ImageView imgView;

        public ViewHolder(@NonNull View itemView) {
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
}
