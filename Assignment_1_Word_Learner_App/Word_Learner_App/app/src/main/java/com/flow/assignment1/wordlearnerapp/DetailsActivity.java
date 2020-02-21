package com.flow.assignment1.wordlearnerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    private ImageView wordImage;
    private TextView word, pronunciation, rating;


    private Button cancelBtn, editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        wordImage = findViewById(R.id.detailsWordImg);
        word = findViewById(R.id.detailsWord);
        pronunciation = findViewById(R.id.detailsPronunciation);
        rating = findViewById(R.id.detailsWordRating);



        Log.d("ONCREATE", "savedInstance state is not null " + (savedInstanceState!=null));

        if(savedInstanceState != null){
            String wordKey = "wordExtra";
            String pronunciationKey = "pronunciationExtra";
            String ratingKey = "ratingExtra";
            String imgResNbrKey = "imgResNbrExtra";

            Log.d("SAVEDINSTANCETEST", savedInstanceState.getString(wordKey));
            Log.d("SAVEDINSTANCETEST", savedInstanceState.getString(pronunciationKey));
            Log.d("SAVEDINSTANCETEST", savedInstanceState.getString(ratingKey));
            Log.d("SAVEDINSTANCETEST", Integer.toString(savedInstanceState.getInt(imgResNbrKey)));

            word.setText(savedInstanceState.getString(wordKey));
            pronunciation.setText(savedInstanceState.getString(pronunciationKey));
            rating.setText(savedInstanceState.getString(ratingKey));
            wordImage.setImageResource(savedInstanceState.getInt(imgResNbrKey));



            //word.setText(savedInstanceState.getString(getString(R.string.wordExtra)));
            //pronunciation.setText(savedInstanceState.getString(getString(R.string.pronunciationExtra)));
            //rating.setText(savedInstanceState.getString(getString(R.string.ratingExtra)));
            //wordImage.setImageResource(savedInstanceState.getInt(getString(R.string.imgResNbrExtra)));
        }

        cancelBtn = findViewById(R.id.detailsCancelBtn);
        editBtn = findViewById(R.id.editBtn);

        cancelBtn.setOnClickListener(cancelOnClickListener);
        //editBtn.setOnClickListener();
    }

    private View.OnClickListener cancelOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setResult(RESULT_CANCELED);
            finish();
        }
    };
}
