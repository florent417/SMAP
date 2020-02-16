package com.flow.uidemos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class PickSomethingDammit extends AppCompatActivity {

    Button okBtn, cancelBtn;
    SeekBar slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_something_dammit);

        slider = findViewById(R.id.slider);
        if(savedInstanceState != null){
            slider.setProgress(savedInstanceState.getInt("saved progress"));
        }

        okBtn = findViewById(R.id.okBtn);
        cancelBtn = findViewById(R.id.cancelBtn);

        okBtn.setOnClickListener(okBtnListener);
        cancelBtn.setOnClickListener(cancelBtnListener);
    }

    private View.OnClickListener okBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setOkBtnListener();
        }
    };

    private View.OnClickListener cancelBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setCancelBtnListener();
        }
    };

    private void setOkBtnListener() {
        Intent output = new Intent();
        output.putExtra("slider message", slider.getProgress());
        setResult(RESULT_OK, output);
        finish();
    }

    private void setCancelBtnListener(){
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("saved progress", slider.getProgress());
        super.onSaveInstanceState(outState);
    }
}
