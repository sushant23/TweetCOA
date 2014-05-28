package com.coa.tweetcoa.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by sushant on 5/26/14.
 */
public class TwitterActivity extends Activity implements View.OnClickListener {
    TextView username;
    List<Status> statuses = new ArrayList<Status>();
    ListView listView;
    ProgressDialog progressDialog;
    List<twitter4j.Status> searchedResults;
    ImageButton imgBtnSearch;
    EditText etSearch;
    Button btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_activity);
        username = (TextView) findViewById(R.id.username);
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(Constants.OAUTH_CALLBACK_URL)) {
            String verifier = uri.getQueryParameter(Constants.URL_TWITTER_OAUTH_VERIFIER);

            System.out.println("BACK FROM TWITTER!!!");

            new TwitterGetAccessTokenTask().execute(verifier);
        } else {
            new TwitterGetAccessTokenTask().execute("");
        }

        listView = (ListView) findViewById(R.id.lvTimeline);
        imgBtnSearch = (ImageButton) findViewById(R.id.imgBtnSearch);
        etSearch = (EditText) findViewById(R.id.etSearch);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(this);
        imgBtnSearch.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.imgBtnSearch:
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        publishProgress(null);
                        QueryResult queryResult;
                        try {
                            if(!etSearch.getText().toString().equalsIgnoreCase("")) {
                                queryResult = TwitterUtil.getInstance().getTwitter().search(new Query(etSearch.getText().toString()));
                                searchedResults = queryResult.getTweets();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        listView.setAdapter(new CustomAdapter(TwitterActivity.this, searchedResults, TwitterActivity.this.getResources()));

                                    }
                                });
                            }
                        } catch (TwitterException e) {
                            e.printStackTrace();
                        }

                        return null;

                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog = ProgressDialog.show(TwitterActivity.this, "", "Connecting to twitter...", true);
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        progressDialog.dismiss();
                    }
                }.execute();

                break;
            case R.id.btnLogout:
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.PREF_KEY_OAUTH_TOKEN, "");
                editor.putString(Constants.PREF_KEY_OAUTH_SECRET, "");
                editor.putBoolean(Constants.PREF_KEY_TWITTER_LOGIN, false);
                editor.commit();
                TwitterUtil.getInstance().reset();
                Intent intent = new Intent(TwitterActivity.this, TweetCOAMainActivity.class);
                startActivity(intent);
                break;
        }
    }

    class TwitterGetAccessTokenTask extends AsyncTask<String, String, String> {

        String name;

        @Override
        protected void onPostExecute(String userName) {
            progressDialog.dismiss();
            Log.i("verifier", "onpostexecute");
            if(userName != null)
                TwitterActivity.this.username.setText(TwitterActivity.this.username.getText() + " " + userName);
            else {
                TwitterActivity.this.username.setText("Sorry something went wrong");
                TwitterActivity.this.username.setCompoundDrawables(null,null,TwitterActivity.this.getResources().getDrawable(android.R.drawable.ic_dialog_info),null);
            }

        }

        @Override
        protected String doInBackground(String... params) {

            System.out.println("verifier  " + params[0]);
            Twitter twitter = TwitterUtil.getInstance().getTwitter();
            RequestToken requestToken = TwitterUtil.getInstance().getRequestToken();
            publishProgress(null);

            if (!TextUtils.isEmpty(params[0])) {
                Log.i("verifier", "verifier not empty");
                try {

                    AccessToken accessToken = null;
                    try {
                        accessToken = twitter.getOAuthAccessToken(requestToken, params[0]);
                        System.out.println("GOT ACCESS TOKEN:: " + accessToken);
                    } catch (TwitterException e) {
                        System.out.println("GOT ACCESS TOKEN EXCEPTION");
                        e.printStackTrace();
                    }

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constants.PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
                    editor.putString(Constants.PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
                    editor.putBoolean("LOGGED_IN", true);
                    editor.commit();

                    System.out.println("NAME FROM TWITTER:: " + (twitter.showUser(accessToken.getUserId()).getName()));

                    name = twitter.showUser(accessToken.getUserId()).getName();

                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            } else {
                Log.i("verifier", "verifier empty");

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String accessTokenString = sharedPreferences.getString(Constants.PREF_KEY_OAUTH_TOKEN, "");
                String accessTokenSecret = sharedPreferences.getString(Constants.PREF_KEY_OAUTH_SECRET, "");
                AccessToken accessToken = new AccessToken(accessTokenString, accessTokenSecret);

                try {
                    TwitterUtil.getInstance().setTwitterFactory(accessToken);
                    name = TwitterUtil.getInstance().getTwitter().showUser(accessToken.getUserId()).getName();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }

            try {
                Paging paging = new Paging(1, 50);
                statuses = TwitterUtil.getInstance().getTwitter().getHomeTimeline(paging);
                Log.i("statuseslength", String .valueOf(statuses.size()));
                for (int i = statuses.size() - 1 ; i >=0; i--) {
                    if (statuses.get(i).getMediaEntities().length <= 0) {
                        statuses.remove(i);
                    }
                }
                Log.i("statuseslengthafter", String .valueOf(statuses.size()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("forloopmelength","runonuithread");
                        listView.setAdapter(new CustomAdapter(TwitterActivity.this, statuses, TwitterActivity.this.getResources()));

                    }
                });


            } catch (TwitterException e) {

                e.printStackTrace();
                Log.i("exception", "TwitterTimeline");

            }

            return name;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(TwitterActivity.this, "", "Connecting to twitter...", true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        progressDialog.dismiss();
     }
}
