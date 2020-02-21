package com.flow.viewactivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button editBtn;
    TextView editUsrInput;
    private int editActivityReqCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editUsrInput = findViewById(R.id.editUsrInput);
        if (savedInstanceState != null){
            editUsrInput.setText(savedInstanceState.getString("user saved message"));
        }

        editBtn = findViewById(R.id.editBtn);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("user saved message",editUsrInput.getText().toString());
                startActivityForResult(intent, editActivityReqCode);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == editActivityReqCode && resultCode == RESULT_OK && data != null){
            editUsrInput.setText(data.getStringExtra("user message"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(editUsrInput.getText().length() > 0){
            outState.putString("user saved message", editUsrInput.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }
}
