package com.flow.uidemos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button pickerBtn, editTxtBtn, slideBtn;
    private TextView msg, email, numb, pass;


    private final int PICKER_REQ = 1;
    private final int TEXT_EDIT_REQ = 2;
    private final int SLIDE_REQ = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        msg = findViewById(R.id.usrMsgMain);
        email = findViewById(R.id.usrMailMain);
        pass = findViewById(R.id.usrPassMain);
        numb = findViewById(R.id.usrFavNumbMain);

        pickerBtn = findViewById(R.id.pickerDemo);
        editTxtBtn = findViewById(R.id.editTxtDemo);
        slideBtn = findViewById(R.id.slidersDemo);

        pickerBtn.setOnClickListener(pickerBtnListener);
        editTxtBtn.setOnClickListener(editTxtBtnListener);
        slideBtn.setOnClickListener(sliderBtnListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null)
        {
            switch (requestCode) {
                case PICKER_REQ:
                    int sliderProgess = data.getIntExtra("slider message",0);
                    String sliderText = "Slider is at progress: " + sliderProgess;
                    makeToast(sliderText);
                    break;
                case TEXT_EDIT_REQ:
                    msg.setText(data.getStringExtra("user message"));
                    email.setText(data.getStringExtra("user email"));
                    numb.setText(data.getStringExtra("favourite number"));
                    pass.setText(data.getStringExtra("user pass"));
                    break;
                case SLIDE_REQ:
                    makeToast(data.getStringExtra("rgb"));
                    break;
                default:
                    break;
            }
        }
    }

    private void makeToast(String message){
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM,0,250);
        toast.show();
    }

    private View.OnClickListener pickerBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setPickerBtnListener();
        }
    };

    private View.OnClickListener editTxtBtnListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            setEditTxtBtnListener();
        }
    };

    private View.OnClickListener sliderBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setSliderBtnListener();
        }
    };

    private void setPickerBtnListener() {
        Intent pickerIntent = new Intent(MainActivity.this, PickSomethingDammit.class);
        startActivityForResult(pickerIntent, PICKER_REQ);
    }

    private void setEditTxtBtnListener() {
        Intent editTxtIntent = new Intent(MainActivity.this, WriteSomeShit.class);
        startActivityForResult(editTxtIntent, TEXT_EDIT_REQ);
    }

    private void setSliderBtnListener() {
        Intent sliderIntent = new Intent(MainActivity.this, SlidinInYoDMS.class);
        startActivityForResult(sliderIntent, SLIDE_REQ);
    }
}
