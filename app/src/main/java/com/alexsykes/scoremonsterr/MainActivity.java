package com.alexsykes.scoremonsterr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    public static final int TEXT_REQUEST = 1;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("monster", MODE_PRIVATE);
        boolean setup = prefs.getBoolean("setup", false);
        if(!setup){
            goSetup();
        }
    }

    private void goSetup() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, TEXT_REQUEST);
    }
}
