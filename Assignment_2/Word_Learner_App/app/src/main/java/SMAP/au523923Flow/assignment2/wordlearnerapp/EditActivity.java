package SMAP.au523923Flow.assignment2.wordlearnerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import SMAP.au523923Flow.assignment2.wordlearnerapp.model.WordListItem;

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

        if(intent != null){
            WordListItem wordListItem = intent.getParcelableExtra(getString(R.string.WORD_LIST_ITEM));
            if (wordListItem != null){
                wordName.setText(wordListItem.getWord());
                wordRating.setText(wordListItem.getRating());
                String ratingStrVal = wordRating.getText().toString();
                float ratingFloatVal = Float.parseFloat(ratingStrVal) * 10;
                int ratingIntVal = (int) ratingFloatVal;
                rater.setProgress(ratingIntVal);
                wordNotes.setText(wordListItem.getNotes());
            }
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

            WordListItem dataToSend = intentFromDetailsActivity.getParcelableExtra(getString(R.string.WORD_LIST_ITEM));
            // Update the data
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
            // To display it correctly
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
