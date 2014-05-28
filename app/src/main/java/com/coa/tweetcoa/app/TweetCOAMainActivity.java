package com.coa.tweetcoa.app;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.auth.RequestToken;

public class TweetCOAMainActivity extends Activity implements View.OnClickListener {
    Twitter twitter;
    Button btnLoginTwitter;
    RequestToken requestToken;
    SharedPreferences mSharedPreferences;
    TwitterFactory twitterFactory;
    TwitterStream twitterStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_coamain);
        btnLoginTwitter = (Button) findViewById(R.id.btnLoginTwitter);
        btnLoginTwitter.setOnClickListener(this);
        if (!isNetworkAvailable(this)){
            btnLoginTwitter.setText("sorry no internet connection");
            btnLoginTwitter.setEnabled(false);
            return;

        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!sharedPreferences.getBoolean("LOGGED_IN",false)) {
        }
        else
        {
            System.out.println("system out :: logged in");
            Intent intent = new Intent(this, TwitterActivity.class);
            startActivity(intent);
            finish();
        }



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoginTwitter:
                new TwitterAuthenticateTask().execute();
                break;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();



    }

    class TwitterAuthenticateTask extends AsyncTask<String, String, RequestToken> {

        @Override
        protected void onPostExecute(RequestToken requestToken) {
            if (requestToken!=null){

                System.out.println("OPENING TWITTER WEB INTERFACE!!");
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
                    startActivity(intent);

            }
        }

        @Override
        protected RequestToken doInBackground(String... params) {
            return TwitterUtil.getInstance().getRequestToken();
        }
    }

    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

}