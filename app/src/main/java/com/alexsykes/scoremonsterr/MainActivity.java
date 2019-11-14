package com.alexsykes.scoremonsterr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

public class MainActivity extends FragmentActivity {
    public static final int TEXT_REQUEST = 1;
    SharedPreferences prefs;
    TextView statusLine, scoreView;

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

        boolean iSobserver = sharedPreferences.getBoolean("isObserver", false);
        boolean useScorePad = sharedPreferences.getBoolean("useScorePad", false);

        // If trialId not set, then go to SettingsActivity
        if (trialId.equals("")) {
            goSetup();
        }

        // If observer, then check for observer name and section
        else if (iSobserver && ((section.equals("")) || (name.equals("")))) {
            goSetup();
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.top, new NumberPadFragment());

        if (useScorePad) {
            ft.replace(R.id.bottom, new ScorePadFragment());
        } else {
            ft.replace(R.id.bottom, new TouchFragment());
        }
        ft.commit();


        String status = "Trial ID: " + trialId;
        statusLine.setText(status);
    }

    private void goSetup() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, TEXT_REQUEST);
    }

    public void addDigit(View view) {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
    }



    public int countDabs(View view) {
        scoreView = findViewById(R.id.scoreView);
        int score = 0;
        int intID = view.getId();
        Button button = view.findViewById(intID);
        String digit = button.getText().toString();

        switch (digit) {
            case "Clean":
                score = 0;
                break;
            case "Ten":
                score = 10;
                break;
            case "Five":
                score = 5;
                break;
            case "Dab":
                if (score < 3)
                    score++;
                break;
        }
        scoreView.setText(String.valueOf(score));
        return score;
    }
}