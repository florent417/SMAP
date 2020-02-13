package com.flow.uidemos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button pickerBtn;
    private Button editTxtBtn;
    private Button slideBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickerBtn = findViewById(R.id.pickerDemo);
        editTxtBtn = findViewById(R.id.editTxtDemo);
        slideBtn = findViewById(R.id.slidersDemo);


    }

    private View.OnClickListener editTxtBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setEditTxtBtnListener();
        }
    };

    private void setEditTxtBtnListener() {

    }
}
