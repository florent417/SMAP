package com.flow.assignment1.wordlearnerapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    private ImageView wordImage;
    private TextView word, pronunciation, rating, description, notes;
    private Button cancelBtn, editBtn;
    private int wordItemPos;
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

            WordListItem wordListItem = (WordListItem) intentBundle.getParcelable(getString(R.string.WORD_LIST_ITEM));

            word.setText(wordListItem.getWord());
            pronunciation.setText(wordListItem.getPronunciation());
            rating.setText(wordListItem.getRating());
            String notesTxt = wordListItem.getNotes();
            if (notesTxt != null){
                notes.setText(notesTxt);
            }
            // Set a sample photo? Value for that?
            wordItemPos = wordListItem.getWordPosition();
            wordImage.setImageResource(wordListItem.getImgResNbr());
            /*
            word.setText(intentBundle.getString(getString(R.string.WORD_EXTRA)));
            pronunciation.setText(intentBundle.getString(getString(R.string.PRONUNCIATION_EXTRA)));
            rating.setText(intentBundle.getString(getString(R.string.RATING_EXTRA)));
            String notesTxt = intentBundle.getString(getString(R.string.NOTES_EXTRA));
            if (notesTxt != null){
                notes.setText(notesTxt);
            }
            // Set a sample photo? Value for that?
            wordItemPos = intentBundle.getInt(getString(R.string.POSITION_EXTRA));
            wordImage.setImageResource(intentBundle.getInt(getString(R.string.IMGRESNBR_EXTRA)));
             */
        }

        // for later
        if(savedInstanceState != null){
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

            WordListItem dataToSend = (WordListItem) intentFromListActivity.getParcelableExtra(getString(R.string.WORD_LIST_ITEM));
            intentToNewActivity.putExtra(getString(R.string.WORD_LIST_ITEM),dataToSend);
            /*
            intent.putExtra(getString(R.string.WORD_EXTRA), word.getText().toString());
            intent.putExtra(getString(R.string.RATING_EXTRA), rating.getText().toString());
            intent.putExtra(getString(R.string.POSITION_EXTRA), wordItemPos);
            intent.putExtra(getString(R.string.NOTES_EXTRA), notes.getText().toString());
            // also for notes
             */
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

        if(requestCode == EDIT_REQ && resultCode == RESULT_OK ){
            if(data != null){
                setResult(RESULT_OK, data);
                finish();
            }
            else{
                //Skal der være et set result her?
                finish();
            }
        }
    }
}
