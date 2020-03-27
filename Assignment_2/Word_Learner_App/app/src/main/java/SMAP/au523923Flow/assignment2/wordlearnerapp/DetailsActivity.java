package SMAP.au523923Flow.assignment2.wordlearnerapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import SMAP.au523923Flow.assignment2.wordlearnerapp.model.Word;
import SMAP.au523923Flow.assignment2.wordlearnerapp.model.WordListItem;
import SMAP.au523923Flow.assignment2.wordlearnerapp.service.WordLearnerService;
import SMAP.au523923Flow.assignment2.wordlearnerapp.utils.Globals;

// Since the system by default uses the Bundle instance to save information about the view
// in the activity, onSaveInstanceState is not implemented for this activity, neither the
// editactivity, since all the information can be found in the views of the activity.
// Inspiration:
// https://stackoverflow.com/questions/45314262/android-view-what-is-automatically-saved-and-restored-in-an-activity
public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = "DetailsActivity";

    // ############ UI variables ##########
    private ImageView wordImage;
    private TextView word, pronunciation, rating, description, notes;
    private Button cancelBtn, editBtn, deleteBtn;

    // ########## Service Binding Variables ##########
    ServiceConnection connection;
    private WordLearnerService wordLearnerService;
    boolean boundToService = false;

    // ########## Word object for details activity ##########
    private Word wordObj = null;

    // ########## Lifecycle methods ##########
    //region Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

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

                setUpDetails();
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
        Intent bindServiceIntent = new Intent(DetailsActivity.this, WordLearnerService.class);
        bindService(bindServiceIntent, connection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "Binded to Word Learner Service");
    }
    //endregion

    // ########## UI Setup and UI update ##########
    //region UI Setup
    private void setupUI(){
        wordImage = findViewById(R.id.detailsWordImg);
        word = findViewById(R.id.detailsWord);
        pronunciation = findViewById(R.id.detailsPronunciation);
        rating = findViewById(R.id.detailsWordRating);
        description = findViewById(R.id.descriptionText);
        notes = findViewById(R.id.detailsNotesText);

        cancelBtn = findViewById(R.id.detailsCancelBtn);
        editBtn = findViewById(R.id.editBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        cancelBtn.setOnClickListener(cancelOnClickListener);
        editBtn.setOnClickListener(editOnClickListener);
        deleteBtn.setOnClickListener(deleteOnClickListener);
    }

    public void setUpDetails(){
        // Get extras from ListActivity
        Bundle intentBundle = getIntent().getExtras();

        if(intentBundle != null){
            String wordStr = intentBundle.getString(Globals.CHOSEN_WORD);
            wordObj = wordLearnerService.getWord(wordStr);

            if (wordObj != null){
                word.setText(wordObj.getWord());
                pronunciation.setText(wordObj.getPronunciation());
                rating.setText(wordObj.getRating());

                // If notes exist, set notesViewText
                String notesTxt = wordObj.getNotes();
                if (notesTxt != null){
                    notes.setText(notesTxt);
                }

                // Load image with Picasso. See explanation in Adapter
                String imageUrl = wordObj.getFirstDefinition().getImageUrl();
                Picasso.with(DetailsActivity.this)
                        .load(imageUrl)
                        .placeholder(android.R.drawable.sym_def_app_icon)
                        .error(android.R.drawable.sym_def_app_icon)
                        .into(wordImage);

                String definition = wordObj.getFirstDefinition().getDefinition();
                if (definition != null) {
                    description.setText(definition);
                }
            }
        }
    }
    //endregion

    // ########## onClick implementations ##########
    //region onClickImplementations
    private View.OnClickListener editOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View wordItemView) {
            Intent intentToNewActivity = new Intent(DetailsActivity.this, EditActivity.class);
            // Put the word string in the intent
            intentToNewActivity.putExtra(Globals.CHOSEN_WORD, wordObj.getWord());
            startActivityForResult(intentToNewActivity, Globals.EDIT_REQ);
        }
    };

    private View.OnClickListener deleteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (wordObj != null){
                wordLearnerService.deleteWord(wordObj);
                // Not sure if this is needed, but we go back to ListActivity
                setResult(RESULT_OK);
                finish();
            }
        }
    };

    private View.OnClickListener cancelOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View cancelBtn) {
            setResult(RESULT_CANCELED);
            finish();
        }
    };
    //endregion

    // ########## OnActivityResult implementation ##########
    //region OnActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Globals.EDIT_REQ && resultCode == RESULT_OK ){
            setResult(RESULT_OK);
            finish();
        }
    }
    //endregion
}
