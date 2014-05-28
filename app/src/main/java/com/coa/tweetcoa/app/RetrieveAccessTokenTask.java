package com.coa.tweetcoa.app;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by sushant on 5/25/14.
 */
public class RetrieveAccessTokenTask extends AsyncTask<Uri, Void, Void> {
    private Twitter twitter;
    private Context context;
    private AccessToken accessToken;
    private RequestToken requestToken;

    public RetrieveAccessTokenTask(Context context, Twitter twitter) {
        this.context = context;
        this.twitter = twitter;

    }

    @Override
    protected Void doInBackground(Uri... uris) {
        Uri uri = uris[0];
        if (uri != null && uri.toString().startsWith(Constants.OAUTH_CALLBACK_URL)) {
            final String verifier = uri.getQueryParameter(Constants.URL_TWITTER_OAUTH_VERIFIER);
            Log.d("verifier",verifier);
            try {
                accessToken = twitter.getOAuthAccessToken();
                Log.d("requesttoken",accessToken.toString());
                /*accessToken = twitter.getOAuthAccessToken(
                        requestToken, verifier);*/
               /* Log.i("user Id", twitter.showUser(accessToken.getUserId()).getName());*/
            } catch (TwitterException e) {
                e.printStackTrace();
                Log.e("exception accessToken", e.toString());
            }


        }
        return null;
    }
}
