package com.flow.lifeofandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Lifecycle","onCreate() called");
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("Lifecycle","onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("Lifecycle","onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("Lifecycle","onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("Lifecycle","onStop() called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d("Lifecycle","onRestart() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("Lifecycle","onDestroy() called");
    }
}
