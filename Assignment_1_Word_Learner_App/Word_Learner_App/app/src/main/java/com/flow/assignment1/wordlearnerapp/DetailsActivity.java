package com.flow.assignment1.wordlearnerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

        Intent intent = getIntent();
        Bundle intentBundle = intent.getExtras();

        if(intentBundle != null){
            word.setText(intentBundle.getString(getString(R.string.wordExtra)));
            pronunciation.setText(intentBundle.getString(getString(R.string.pronunciationExtra)));
            rating.setText(intentBundle.getString(getString(R.string.ratingExtra)));
            // Set a sample photo? Value for that?
            wordImage.setImageResource(intentBundle
                    .getInt(getString(R.string.imgResNbrExtra),0));
        }

        // for later
        if(savedInstanceState != null){
        }

        cancelBtn = findViewById(R.id.detailsCancelBtn);
        editBtn = findViewById(R.id.editBtn);

        cancelBtn.setOnClickListener(cancelOnClickListener);
        //editBtn.setOnClickListener();
    }

    private View.OnClickListener cancelOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View cancelBtn) {
            setResult(RESULT_CANCELED);
            finish();
        }
    };
}
