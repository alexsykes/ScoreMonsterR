package com.alexsykes.scoremonsterr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
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

        TouchFragment touchFragment = new TouchFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // fragmentTransaction.add(new TouchFragment());
        fragmentTransaction.commit();



        statusLine.setText("Trial id: " + trialId);
    }

    private void goSetup() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, TEXT_REQUEST);
    }

    public void countDabs(View view) {
    }

    public static class TouchFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_score_pad, container, false);
        }
    }

}
