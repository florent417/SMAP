package SMAP.au523923Flow.assignment2.wordlearnerapp;

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
    private Word wordObj = null;

    // ########## Lifecycle methods ##########
    //region Lifecycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

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
        wordName = findViewById(R.id.editWord);
        wordRating = findViewById(R.id.editWordRating);
        rater = findViewById(R.id.wordRater);
        wordNotes = findViewById(R.id.notesInput);

        cancelBtn = findViewById(R.id.editCancelBtn);
        updateBtn = findViewById(R.id.updateBtn);

        cancelBtn.setOnClickListener(cancelOnClickListener);
        updateBtn.setOnClickListener(okOnClickListener);

        rater.setOnSeekBarChangeListener(ratingOnChangeListener);
    }

    private void updateUI(){
        Bundle extras = getIntent().getExtras();

        if(extras != null){
            String word = extras.getString(Globals.CHOSEN_WORD);
            wordObj = wordLearnerService.getWord(word);
            if (wordObj != null){
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
}
