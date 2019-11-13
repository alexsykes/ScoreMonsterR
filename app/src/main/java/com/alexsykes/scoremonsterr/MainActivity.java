package com.alexsykes.scoremonsterr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

public class MainActivity extends FragmentActivity {
    public static final int TEXT_REQUEST = 1;
    SharedPreferences prefs;
    TextView statusLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusLine = findViewById(R.id.statusView);
        // Check for initialisation of prefs
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String trialId = sharedPreferences.getString("trial_id", "");
        String name = sharedPreferences.getString("name", "");
        String section = sharedPreferences.getString("section", "");

        boolean observer = sharedPreferences.getBoolean("observer", false);

        // If trialId not set, then go to SettingsActivity
        if (trialId.equals("")) {
            goSetup();
        }

        // If observer, then check for observer name and section
        else if (observer && ((section.equals("")) || (name.equals("")))) {
            goSetup();
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.top, new NumberPadFragment());
        ft.replace(R.id.bottom, new TouchFragment());
        ft.commit();


        String status = "Trial ID: " + trialId;
        statusLine.setText(status);
    }

    private void goSetup() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, TEXT_REQUEST);
    }

    public void addDigit(View view) {
    }
}