package com.flow.uidemos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WriteSomeShit extends AppCompatActivity {

    TextView plain,email,favNumb,pass;
    Button okBtn, cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_some_shit);

        plain = findViewById(R.id.usrMsg);
        email = findViewById(R.id.email);
        favNumb = findViewById(R.id.favNumb);
        pass = findViewById(R.id.pass);

        okBtn = findViewById(R.id.okBtnTxt);
        cancelBtn = findViewById(R.id.cancelBtnTxt);

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
        output.putExtra("user message", plain.getText().toString());
        output.putExtra("favourite number", favNumb.getText().toString());
        output.putExtra("user email", email.getText().toString());
        output.putExtra("user pass", pass.getText().toString());
        setResult(RESULT_OK, output);
        finish();
    }

    private void setCancelBtnListener(){
        setResult(RESULT_CANCELED);
        finish();
    }
}
