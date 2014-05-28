package com.coa.tweetcoa.app;

import android.content.SharedPreferences;

/**
 * Created by sushant on 5/25/14.
 */
public class Constants {
    /*Consumer and consumer secret key*/
    public static final String CONSUMER_KEY = "9mP6O8DtK0dVlrBxpvXG97Spx";
    public static final String CONSUMER_SECRET_KEY = "ojuk6Kwrgyvd3NaSL8qgMy1yisiig2ljmMTHm2GKPBPGT12nqb";

    /*SharedPreferences KEY names*/
    public static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    public static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    public static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLoggedIn";


    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    public static final String OAUTH_CALLBACK_SCHEME = "x-oauthflow-twitter";
    public static final String OAUTH_CALLBACK_HOST = "twitter_callback";
    public static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;

}
