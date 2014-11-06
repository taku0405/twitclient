package com.awoi4.twitclient;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class TwitterOAuthActivity extends Activity {
	
	private String mCallbackURL;
	private Twitter mTwitter;
	private RequestToken mRequestToken;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter_oauth);
		
		mCallbackURL=getString(R.string.twitter_callback_url);
		mTwitter=TwitUtils.getTwitterInstance(this);
		
		findViewById(R.id.action_start_oauth).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startAuthorize();
			}

			private void startAuthorize() {
				// TODO Auto-generated method stub
				AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>(){
					@Override
					protected String doInBackground(Void... params){
						try{
							mRequestToken = mTwitter.getOAuthRequestToken(mCallbackURL);
							return mRequestToken.getAuthenticationURL();
						}catch(TwitterException e){
							e.printStackTrace();
						}
						return null;
					}
					
					@Override
					protected void onPostExecute(String url){
						if(url != null){
							Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
							startActivity(intent);
						}else{
							
						}
					}
				};
				task.execute();
			}
		});
	}
	
	@Override
	public void onNewIntent(Intent intent){
		if(intent == null || intent.getData() == null || !intent.getData().toString().startsWith(mCallbackURL)){
			return;
		}
		String verifier = intent.getData().getQueryParameter("oauth_verifier");
		
		AsyncTask<String, Void, AccessToken> task = new AsyncTask<String, Void, AccessToken>(){
			@Override
			protected AccessToken doInBackground(String... params){
				try{
					return mTwitter.getOAuthAccessToken(mRequestToken, params[0]);
				}catch(TwitterException e){
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(AccessToken accessToken){
				if(accessToken != null){
					showToast("認証成功");
					successOAuth(accessToken);
				}else{
					showToast("認証失敗");
				}
			}
		};
		task.execute(verifier);
	}
	
	private void successOAuth(AccessToken accessToken){
		TwitUtils.storeAccessToken(this, accessToken);
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
	
	private void showToast(String text){
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

}