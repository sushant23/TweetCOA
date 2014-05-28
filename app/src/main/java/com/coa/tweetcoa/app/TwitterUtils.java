package com.coa.tweetcoa.app;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: ServusKevin
 * Date: 5/4/13
 * Time: 1:47 PM
 * To change this template use File | Settings | File Templates.
 */
class TwitterUtil {

    private RequestToken requestToken = null;
    private TwitterFactory twitterFactory = null;
    private Twitter twitter;

    public TwitterUtil() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(Constants.CONSUMER_KEY);
        configurationBuilder.setOAuthConsumerSecret(Constants.CONSUMER_SECRET_KEY);

        Configuration configuration = configurationBuilder.build();
        twitterFactory = new TwitterFactory(configuration);
        twitter = twitterFactory.getInstance();
    }

    public TwitterFactory getTwitterFactory() {
        return twitterFactory;
    }

    public void setTwitterFactory(AccessToken accessToken) {
        twitter = twitterFactory.getInstance(accessToken);
    }

    public Twitter getTwitter() {
        return twitter;
    }

    public RequestToken getRequestToken() {
        if (requestToken == null) {
            try {
                requestToken = twitterFactory.getInstance().getOAuthRequestToken(Constants.OAUTH_CALLBACK_URL);
                System.out.println("REQUEST TOKEN:: " + requestToken);
            } catch (TwitterException e) {
                System.out.println("REQUEST TOKEN EXCEPTION");
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return requestToken;
    }

    static TwitterUtil instance = new TwitterUtil();

    public static TwitterUtil getInstance() {
        return instance;
    }


    public void reset() {
        instance = new TwitterUtil();
    }
}
