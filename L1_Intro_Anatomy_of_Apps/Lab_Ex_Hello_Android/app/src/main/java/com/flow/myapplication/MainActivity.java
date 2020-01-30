package com.flow.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView _helloText;
    Button _textChanger;
    Button exitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _helloText = findViewById(R.id.helloText);
        _textChanger = findViewById(R.id.helloBtn);
        exitBtn = findViewById(R.id.exitBtn);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        _textChanger.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                _helloText.setText("Hello Android");
            }
        });
    }

}
