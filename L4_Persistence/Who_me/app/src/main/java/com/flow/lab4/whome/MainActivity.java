package com.flow.lab4.whome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView fName, lName, age, phoneNbr;
    private String fNameKey ="firstNameKey", lNameKey ="lastNameKey", ageKey ="ageKey",
            phoneNbrKey ="phoneNbrKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fName = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        age = findViewById(R.id.age);
        phoneNbr = findViewById(R.id.phoneNumb);

        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        if (sharedPreferences.contains(fNameKey)){
            fName.setText(sharedPreferences.getString(fNameKey,null));

        }
        if(sharedPreferences.contains(lNameKey)){
            lName.setText(sharedPreferences.getString(lNameKey,null));

        }
        if(sharedPreferences.contains(ageKey)){
            age.setText(sharedPreferences.getString(ageKey,null));

        }
        if (sharedPreferences.contains(phoneNbrKey)){
            phoneNbr.setText(sharedPreferences.getString(phoneNbrKey,null));

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(fNameKey, fName.getText().toString());
        editor.putString(lNameKey, lName.getText().toString());
        editor.putString(ageKey, age.getText().toString());
        editor.putString(phoneNbrKey, phoneNbr.getText().toString());
        editor.commit();
    }
}
