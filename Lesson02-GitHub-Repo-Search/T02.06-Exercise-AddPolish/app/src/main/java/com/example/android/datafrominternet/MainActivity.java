/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.datafrominternet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.datafrominternet.utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchBoxEditText;

    private TextView mUrlDisplayTextView;

    private TextView mSearchResultsTextView;

    // Completed (12) Create a variable to store a reference to the error message TextView
    private TextView mErrorMessageTextView;
    // Completed (24) Create a ProgressBar variable to store a reference to the ProgressBar
    private ProgressBar mLoadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);

        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_github_search_results_json);

        // Completed (13) Get a reference to the error TextView using findViewById
        mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_message_display);
        // Completed (25) Get a reference to the ProgressBar using findViewById
        mLoadingProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
    }

    /**
     * This method retrieves the search text from the EditText, constructs the
     * URL (using {@link NetworkUtils}) for the github repository you'd like to find, displays
     * that URL in a TextView, and finally fires off an AsyncTask to perform the GET request using
     * our {@link GithubQueryTask}
     */
    private void makeGithubSearchQuery() {
        String githubQuery = mSearchBoxEditText.getText().toString();
        if (githubQuery.equals("")||githubQuery==null) {
            try {
                new GithubQueryTask().execute(new URL("https://api.github.com/user/repos?page=1&per_page=10"));
                Toast.makeText(this, "Showing Repositories of user " + getResources().getString(R.string.DEV).split(" ")[0], Toast.LENGTH_LONG).show();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }else {
            URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
            mUrlDisplayTextView.setText(githubSearchUrl.toString());
            new GithubQueryTask().execute(githubSearchUrl);
        }
    }

    // Completed (14) Create a method called showJsonDataView to show the data and hide the error
    private void showJsonDataView() {
        mSearchResultsTextView.setVisibility(TextView.VISIBLE);
        mErrorMessageTextView.setVisibility(TextView.INVISIBLE);
    }

    // Completed (15) Create a method called showErrorMessage to show the error and hide the data
    private void showErrorMessage() {
        mSearchResultsTextView.setVisibility(TextView.INVISIBLE);
        mErrorMessageTextView.setText(getResources().getText(R.string.error_msg));
        mErrorMessageTextView.setVisibility(TextView.VISIBLE);
    }

    private void showErrorMessage(String nString) {
        mSearchResultsTextView.setVisibility(TextView.INVISIBLE);
        mErrorMessageTextView.setText(nString);
        mErrorMessageTextView.setVisibility(TextView.VISIBLE);
    }

    public class GithubQueryTask extends AsyncTask<URL, Void, String> {

        // Completed (26) Override onPreExecute to set the loading indicator to visible
        @Override
        protected void onPreExecute() {
            mLoadingProgressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String githubSearchResults = null;
            try {
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (UnknownHostException uhe) {
                uhe.printStackTrace();
                githubSearchResults = getResources().getText(R.string.network_error).toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return githubSearchResults;
        }

        @Override
        protected void onPostExecute(String githubSearchResults) {
            // Completed (27) As soon as the loading is complete, hide the loading indicator
            mLoadingProgressBar.setVisibility(ProgressBar.INVISIBLE);
            if (githubSearchResults != null && !githubSearchResults.equals("")) {
                // Completed (17) Call showJsonDataView if we have valid, non-null results
                showJsonDataView();
                mSearchResultsTextView.setText(githubSearchResults);
            } else {
                if (githubSearchResults.equals(getResources().getText(R.string.network_error).toString())) {
                    Toast.makeText(MainActivity.this, getResources().getText(R.string.network_error), Toast.LENGTH_LONG).show();
                    showErrorMessage(githubSearchResults);
                    return;
                }
                // Completed (16) Call showErrorMessage if the result is null in onPostExecute
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            makeGithubSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
