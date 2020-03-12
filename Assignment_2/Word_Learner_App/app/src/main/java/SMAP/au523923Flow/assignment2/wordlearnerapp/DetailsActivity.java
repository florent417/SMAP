package SMAP.au523923Flow.assignment2.wordlearnerapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import SMAP.au523923Flow.assignment2.wordlearnerapp.model.WordListItem;

// Since the system by default uses the Bundle instance to save information about the view
// in the activity, onSaveInstanceState is not implemented for this activity, neither the
// editactivity, since all the information can be found in the views of the activity.
// Inspiration:
// https://stackoverflow.com/questions/45314262/android-view-what-is-automatically-saved-and-restored-in-an-activity
public class DetailsActivity extends AppCompatActivity {
    private ImageView wordImage;
    private TextView word, pronunciation, rating, description, notes;
    private Button cancelBtn, editBtn;
    // Should this be a resource?
    private final int EDIT_REQ = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        wordImage = findViewById(R.id.detailsWordImg);
        word = findViewById(R.id.detailsWord);
        pronunciation = findViewById(R.id.detailsPronunciation);
        rating = findViewById(R.id.detailsWordRating);
        description = findViewById(R.id.descriptionText);
        notes = findViewById(R.id.detailsNotesText);

        Intent intent = getIntent();
        Bundle intentBundle = intent.getExtras();

        if(intentBundle != null){

            WordListItem wordListItem = intentBundle.getParcelable(getString(R.string.WORD_LIST_ITEM));

            if (wordListItem != null){
                word.setText(wordListItem.getWord());
                pronunciation.setText(wordListItem.getPronunciation());
                rating.setText(wordListItem.getRating());
                String notesTxt = wordListItem.getNotes();
                if (notesTxt != null){
                    notes.setText(notesTxt);
                }
                wordImage.setImageResource(wordListItem.getImgResNbr());
                description.setText(wordListItem.getDescription());
            }
        }

        cancelBtn = findViewById(R.id.detailsCancelBtn);
        editBtn = findViewById(R.id.editBtn);

        cancelBtn.setOnClickListener(cancelOnClickListener);
        editBtn.setOnClickListener(editOnClickListener);
    }

    private View.OnClickListener editOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View wordItemView) {
            Intent intentToNewActivity = new Intent(DetailsActivity.this, EditActivity.class);
            Intent intentFromListActivity = getIntent();

            // Send the same data object, since nothing can be changed in this activity
            WordListItem dataToSend = intentFromListActivity.getParcelableExtra(getString(R.string.WORD_LIST_ITEM));
            intentToNewActivity.putExtra(getString(R.string.WORD_LIST_ITEM),dataToSend);

            startActivityForResult(intentToNewActivity,EDIT_REQ);
        }
    };

    private View.OnClickListener cancelOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View cancelBtn) {
            setResult(RESULT_CANCELED);
            finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // The data does not need to be set in the activity, since the updated object is going to
        // be sent anyway from the listactivity
        if(requestCode == EDIT_REQ && resultCode == RESULT_OK ){
            if(data != null){
                // pass the data on
                setResult(RESULT_OK, data);
                finish();
            }
            else{
                // SetResult?
                finish();
            }
        }
    }
}
