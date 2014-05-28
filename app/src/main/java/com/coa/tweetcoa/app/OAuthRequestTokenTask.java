package com.coa.tweetcoa.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by sushant on 5/25/14.
 */
public class OAuthRequestTokenTask extends AsyncTask<Void, Void, Void> {
    Activity activity;
    RequestToken requestToken;
    Twitter twitter;
    public OAuthRequestTokenTask(Activity activity, Twitter twitter) {
        this.activity = activity;
        this.twitter = twitter;

    }

    @Override
    protected Void doInBackground(Void... voids) {
        /*if (TwitterUtils.isTwitterLoggedIn()) {*/
        try {
            requestToken = twitter.getOAuthRequestToken(Constants.OAUTH_CALLBACK_URL);
            activity.startActivity(new Intent(new Intent(Intent.ACTION_VIEW, Uri
                    .parse(requestToken.getAuthenticationURL()))));

        } catch (TwitterException e) {
            e.printStackTrace();
            Toast.makeText(activity, "Already Logged into twitter", Toast.LENGTH_LONG).show();
        }
       /* }*/

        return null;
    }
}
