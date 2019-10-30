package com.alexsykes.scoremonsterr;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        // Add title bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            // Ref - https://stackoverflow.com/questions/6474707/how-to-fill-listpreference-dynamically-when-onpreferenceclick-is-triggered
            ListPreference lp = findPreference("trial_list");
            CharSequence[] entries = { "Christmas Trial", "New Year Trial" };
            CharSequence[] entryValues = {"1" , "2"};
            lp.setEntries(entries);
            lp.setEntryValues(entryValues);
        }
    }



}