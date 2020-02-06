package com.flow.viewactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity {

    Button okBtn, cancelBtn;
    TextView usrMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        usrMsg = findViewById(R.id.usrInput);
        if (savedInstanceState != null){
            usrMsg.setText(savedInstanceState.getString("user saved message"));
        }

        cancelBtn = findViewById(R.id.cancelBtn);
        okBtn = findViewById(R.id.okBtn);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent output = new Intent();
                output.putExtra("user message", usrMsg.getText().toString());
                setResult(RESULT_OK,output);
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("user saved message", usrMsg.getText().toString());
        super.onSaveInstanceState(outState);
    }
}
