package com.flow.uidemos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class SlidinInYoDMS extends AppCompatActivity {

    private SeekBar redSlider, greenSlider, blueSlider;
    private int red = 0, green = 0, blue = 0;
    private Button okBtn, cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidin_in_yo_dms);

        redSlider = (SeekBar) findViewById(R.id.redSlider);
        greenSlider = (SeekBar)findViewById(R.id.greenSlider);
        blueSlider = (SeekBar) findViewById(R.id.blueSlider);

        okBtn = findViewById(R.id.okBtnSlider);
        cancelBtn = findViewById(R.id.cancelBtnSlider);

        okBtn.setOnClickListener(okBtnListener);
        cancelBtn.setOnClickListener(cancelBtnListener);

        redSlider.setOnSeekBarChangeListener(seekBarChangeListener);
        greenSlider.setOnSeekBarChangeListener(seekBarChangeListener);
        blueSlider.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    public SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            red = redSlider.getProgress();
            blue = blueSlider.getProgress();
            green = greenSlider.getProgress();
            getWindow().getDecorView().setBackgroundColor(Color.rgb(red,green,blue));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

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
        String rgbMsg = "RGB(" + red + ", " + green + ", " + blue + ")";
        output.putExtra("rgb", rgbMsg);
        setResult(RESULT_OK, output);
        finish();
    }

    private void setCancelBtnListener(){
        setResult(RESULT_CANCELED);
        finish();
    }
}
