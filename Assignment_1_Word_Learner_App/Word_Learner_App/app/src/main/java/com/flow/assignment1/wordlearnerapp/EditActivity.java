package com.flow.assignment1.wordlearnerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity {
    private TextView wordName, wordRating, wordNotes;
    private Button cancelBtn, okBtn;
    private SeekBar rater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        wordName = findViewById(R.id.editWord);
        wordRating = findViewById(R.id.editWordRating);
        rater = findViewById(R.id.wordRater);
        wordNotes = findViewById(R.id.notesInput);

        Intent intent = getIntent();
        Bundle intentBundle = intent.getExtras();

        if(intentBundle != null){
            WordListItem wordListItem = (WordListItem) intentBundle.getParcelable(getString(R.string.WORD_LIST_ITEM));
            wordName.setText(wordListItem.getWord());
            wordRating.setText(wordListItem.getRating());
            String ratingStrVal = wordRating.getText().toString();
            float ratingFloatVal = Float.parseFloat(ratingStrVal) * 10;
            int ratingIntVal = (int) ratingFloatVal;
            rater.setProgress(ratingIntVal);
            // Check if there are any notes
            wordNotes.setText(wordListItem.getNotes());
        }

        cancelBtn = findViewById(R.id.editCancelBtn);
        okBtn = findViewById(R.id.okBtn);

        cancelBtn.setOnClickListener(cancelOnClickListener);
        okBtn.setOnClickListener(okOnClickListener);

        rater.setOnSeekBarChangeListener(ratingOnChangeListener);
    }

    private View.OnClickListener okOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intentFromDetailsActivity = getIntent();
            WordListItem dataToSend = (WordListItem) intentFromDetailsActivity.getParcelableExtra(getString(R.string.WORD_LIST_ITEM));
            dataToSend.setRating(wordRating.getText().toString());
            dataToSend.setNotes(wordNotes.getText().toString());
            Intent output = new Intent();

            output.putExtra(getString(R.string.WORD_LIST_ITEM),dataToSend);

            setResult(RESULT_OK, output);
            finish();
        }
    };

    private View.OnClickListener cancelOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View cancelBtn) {
            setResult(RESULT_CANCELED);
            finish();
        }
    };

    // float seekbar display implementation influenced by this post:
    // https://stackoverflow.com/questions/40263777/android-seekbar-float-with-values
    private SeekBar.OnSeekBarChangeListener ratingOnChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            String decimalRating = (progress/10) + "." + (progress%10);
            wordRating.setText(decimalRating);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
