package SMAP.au523923Flow.assignment2.wordlearnerapp;

import androidx.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.TextView;

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

    private ImageView wordImage;
    private TextView word, pronunciation, rating, description, notes;
    private Button cancelBtn, editBtn;
    // Should this be a resource?
    private final int EDIT_REQ = 1;

    // ########## Service Binding Variables ##########
    ServiceConnection connection;
    private WordLearnerService wordLearnerService;
    boolean boundToService = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        setupUI();
        setupServiceConn();
        bindToWordLearnerService();


    }

    @Override
    protected void onStop() {
        wordLearnerService = null;
        unbindService(connection);
        boundToService = false;
        super.onStop();
    }

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

                // TODO: implement get word
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

    // ########## UI Setup ##########
    private void setupUI(){
        wordImage = findViewById(R.id.detailsWordImg);
        word = findViewById(R.id.detailsWord);
        pronunciation = findViewById(R.id.detailsPronunciation);
        rating = findViewById(R.id.detailsWordRating);
        description = findViewById(R.id.descriptionText);
        notes = findViewById(R.id.detailsNotesText);

        cancelBtn = findViewById(R.id.detailsCancelBtn);
        editBtn = findViewById(R.id.editBtn);

        cancelBtn.setOnClickListener(cancelOnClickListener);
        editBtn.setOnClickListener(editOnClickListener);
    }

    public void setUpDetails(){
        Intent intent = getIntent();
        Bundle intentBundle = intent.getExtras();

        if(intentBundle != null){

            String wordStr = intentBundle.getString(Globals.CHOSEN_WORD);
            Log.d(TAG, "setUpDetails: " + wordStr);
            Word wordObj = wordLearnerService.getWord(wordStr);

            if (wordObj != null){
                word.setText(wordObj.getWord());
                pronunciation.setText(wordObj.getPronunciation());
                rating.setText(wordObj.getRating());
                String notesTxt = wordObj.getNotes();
                if (notesTxt != null){
                    notes.setText(notesTxt);
                }
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



    private View.OnClickListener editOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View wordItemView) {
            Intent intentToNewActivity = new Intent(DetailsActivity.this, EditActivity.class);
            Intent intentFromListActivity = getIntent();

            // Send the same data object, since nothing can be changed in this activity
            WordListItem dataToSend = intentFromListActivity.getParcelableExtra(Globals.CHOSEN_WORD);
            intentToNewActivity.putExtra(Globals.CHOSEN_WORD,dataToSend);

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

    // TODO: Delete this??
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
