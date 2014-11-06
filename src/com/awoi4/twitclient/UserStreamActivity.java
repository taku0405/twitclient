package com.awoi4.twitclient;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.os.Bundle;

public class UserStreamActivity extends Activity{
	
	private MyUserStreamAdapter myUserStreamAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		myUserStreamAdapter = new MyUserStreamAdapter();
		
		/*
		 * OAuth認証して情報取得
		 */
		String oAuthConsumerKey = "decE1sYNVSe1ZcKhc0UBz0fdl";
		String oAuthConsumerSecret = "WdJW1Bopc6P6PHJJJnYjP3ABLwCA0eeyQTnZQhOPbEWgbSUhbA";
		String oAuthAccessToken = "2459399880-JWaQPtI3wrYymIpHM4yCrLBHYDLNjhJKpOLOLsQ";
		String oAuthAccessTokenSecret = "PFXC8LSeyUGsxkOtwdKORIZvvIcd9e6F9BBdQAeGGjsJb";
		
		//Twitter4Jに対してOAuth情報を設定
		ConfigurationBuilder builder = new ConfigurationBuilder();
		{
			//アプリ固有の情報
			builder.setOAuthConsumerKey(oAuthConsumerKey);
			builder.setOAuthConsumerSecret(oAuthConsumerSecret);
			//アプリ+ユーザ固有の情報
			builder.setOAuth2AccessToken(oAuthAccessToken);
			builder.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
		}
		builder.setUserStreamBaseURL("https://userstream.twitter.com/2/");
		//1.TwitterStreamFactoryをインスタンス化
		Configuration conf = builder.build();
		TwitterFactory twitterStreamFactory = new TwitterFactory(conf);
		//2.TwitterStreamをインスタンス化
		Twitter twitterStream = twitterStreamFactory.getInstance();
	}

	public void onStatus(Status status) {
		// TODO Auto-generated method stub
		
	}

}
