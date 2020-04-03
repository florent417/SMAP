package SMAP.au523923Flow.assignment2.wordlearnerapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import SMAP.au523923Flow.assignment2.wordlearnerapp.R;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Word;
import SMAP.au523923Flow.assignment2.wordlearnerapp.service.WordLearnerService;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.Globals;

public class EditActivity extends AppCompatActivity {
    private static final String TAG = "EditActivity";

    // ########## UI variables ##########
    private TextView wordName, wordRating, wordNotes;
    private Button cancelBtn, updateBtn;
    private SeekBar rater;

    // ########## Service Binding Variables ##########
    ServiceConnection connection;
    private WordLearnerService wordLearnerService;
    boolean boundToService = false;

    // ########## Word object for details activity ##########
    private Word wordObj;

    private Bundle savedInstance;

    private final String PARCELABLE_KEY = "parcelableWordObject";

    // ########## Lifecycle methods ##########
    //region Lifecycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        savedInstance = savedInstanceState;

        setupUI();
        setupServiceConn();
    }

    @Override
    protected void onStart() {
        bindToWordLearnerService();
        super.onStart();
    }

    @Override
    protected void onStop() {
        wordLearnerService = null;
        unbindService(connection);
        boundToService = false;
        super.onStop();
    }
    //endregion

    // ########## Service functionality ##########
    //region Service functionality
    // Ref: SMAP L5 ServicesDemo code
    private void setupServiceConn() {
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName className,
                                           IBinder service) {
                Log.d(TAG, "Connected to service");
                // We've bound to LocalService, cast the IBinder and get LocalService instance
                WordLearnerService.LocalBinder binder = (WordLearnerService.LocalBinder) service;
                wordLearnerService = binder.getService();
                boundToService = true;

                updateUI();
            }

            @Override
            public void onServiceDisconnected(ComponentName className) {
                boundToService = false;
                wordLearnerService = null;
                Log.d(TAG, "Disconnected from service");
            }
        };
    }

    private void bindToWordLearnerService() {
        Intent bindServiceIntent = new Intent(EditActivity.this, WordLearnerService.class);
        bindService(bindServiceIntent, connection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "Binded to Word Learner Service");
    }
    //endregion


    // ########## UI setup and implementation ##########
    //region UI setup and implementation
    private void setupUI(){
        wordRating = findViewById(R.id.editWordRating);
        wordName = findViewById(R.id.editWord);
        rater = findViewById(R.id.wordRater);
        wordNotes = findViewById(R.id.notesInput);
        cancelBtn = findViewById(R.id.editCancelBtn);
        updateBtn = findViewById(R.id.updateBtn);

        cancelBtn.setOnClickListener(cancelOnClickListener);
        updateBtn.setOnClickListener(okOnClickListener);

        rater.setOnSeekBarChangeListener(ratingOnChangeListener);
    }

    private void updateUI(){
        // See if wordObj is saved in savedInstance
        if(savedInstance != null){
            wordObj = savedInstance.getParcelable(PARCELABLE_KEY);
            // For some odd reason the text (word) does not get saved when flipped
            // although all content in the other views are saved. Therefore we
            // set it manually each time.
            wordName.setText(wordObj.getWord());
        }

        // Get it from DetailsActivity if not in savedInstance
        if (wordObj == null) {
            Bundle extras = getIntent().getExtras();

            if (extras != null) {
                String word = extras.getString(Globals.CHOSEN_WORD);
                Word wordLearnerServiceWord = wordLearnerService.getWord(word);
                wordObj = new Word(wordLearnerServiceWord);

                wordName.setText(wordObj.getWord());
                wordRating.setText(wordObj.getRating());
                String ratingStrVal = wordRating.getText().toString();
                float ratingFloatVal = Float.parseFloat(ratingStrVal) * 10;
                int ratingIntVal = (int) ratingFloatVal;
                rater.setProgress(ratingIntVal);
                wordNotes.setText(wordObj.getNotes());
            }
        }
    }

    // ########## Listener Implementations ##########
    //region Listener Implementations
    private View.OnClickListener okOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean areNotesTheSame = wordNotes.getText().toString().equals(wordObj.getNotes());
            boolean isRatingTheSame = wordRating.getText().toString().equals(wordObj.getRating());

            if (!isRatingTheSame){
                wordObj.setRating(wordRating.getText().toString());
            }

            if (!areNotesTheSame){
                wordObj.setNotes(wordNotes.getText().toString());
            }

            if (!areNotesTheSame || !isRatingTheSame ){
                wordLearnerService.updateWord(wordObj);
            }

            setResult(RESULT_OK);
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
    //endregion

    //endregion

    // ########## Save instance state ##########
    //region Save instance state
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Only save the object, updating on the object is done when we send it
        outState.putParcelable(PARCELABLE_KEY, wordObj);
    }
    //endregion
}
