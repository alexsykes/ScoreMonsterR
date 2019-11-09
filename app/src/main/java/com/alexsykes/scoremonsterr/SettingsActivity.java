package com.alexsykes.scoremonsterr;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    // Set up data fields
    private static final String BASE_URL = "https://android.trialmonster.uk/getTrialList.php";
    ArrayList<HashMap<String, String>> theTrialList;

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

        // String URL = BASE_URL + "getTrialList.php";

        try {
            getTrialList(BASE_URL);
            Toast.makeText(SettingsActivity.this, "Some data", Toast.LENGTH_LONG).show();

        } catch (NullPointerException e) {
            Toast.makeText(SettingsActivity.this, "Empty data", Toast.LENGTH_LONG).show();
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

        public void setList(){}
    }

    private void getTrialList(final String urlWebService) {
        /*
         * As fetching the json string is a network operation
         * And we cannot perform a network operation in main thread
         * so we need an AsyncTask
         * The constrains defined here are
         * Void -> We are not passing anything
         * Void -> Nothing at progress update as well
         * String -> After completion it should return a string and it will be the json string
         * */
        class GetData extends AsyncTask<Void, Void, String> {

            String[] theTrials, theIDs;

            //this method will be called before execution

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
                // Show dialog during server transaction
                // dialog = ProgressDialog.show(SetupActivity.this, "Scoremonster", "Getting trial list", true);
/*                dialog = new ProgressDialog(SettingsActivity.this);
                dialog.setMessage("Loading…");
                dialog.setCancelable(false);
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();*/
            }


            /* this method will be called after execution

                s contains trial details in JSON string
             */

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                // Populate ArrayList with JSON data
                 theTrialList = populateResultArrayList(s);

                theTrials = new String[theTrialList.size()];
                theIDs = new String[theTrialList.size()];

                for (int index = 0; index < theTrialList.size(); index++) {
                    theTrials[index] = theTrialList.get(index).get("name");
                    theIDs[index] = theTrialList.get(index).get("id");
                }
            }

            /*
            @param String json JSON string returned from MySQL
            @return ArrayList of trials data
             */
            private ArrayList<HashMap<String, String>> populateResultArrayList(String json) {
                ArrayList<HashMap<String, String>> theTrialList = new ArrayList<>();
                String  name, id;

                try {
                    // Parse string data into JSON
                    JSONArray jsonArray = new JSONArray(json);

                    for (int index = 0; index < jsonArray.length(); index++) {
                        HashMap<String, String> theTrial = new HashMap<>();
                        id = jsonArray.getJSONObject(index).getString("id");
                        name = jsonArray.getJSONObject(index).getString("name");

                        // trial = club + " - " + name;
                        theTrial.put("id", id);
                        theTrial.put("name", name);
                        theTrialList.add(theTrial);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return theTrialList;
            }

            //in this method we are fetching the json string
            @Override
            protected String doInBackground(Void... voids) {
                int TIMEOUT_VALUE = 1000;
                try {
                    //creating a URL
                    URL url = new URL(urlWebService);

                    //Opening the URL using HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setConnectTimeout(TIMEOUT_VALUE);
                    con.setReadTimeout(TIMEOUT_VALUE);
                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    //A simple string to read values from each line
                    String json;

                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {
                        json = json + "\n";
                        //appending it to string builder
                        sb.append(json);
                    }

                    //finally returning the read string
                    String data = sb.toString().trim();

                    return data;
                } catch (SocketTimeoutException e) {
                    Toast.makeText(SettingsActivity.this, "SocketTimeoutException", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        //creating asynctask object and executing it
        GetData getJSON = new GetData();
        getJSON.execute();
    }

}