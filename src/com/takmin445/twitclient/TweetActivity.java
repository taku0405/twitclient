package com.takmin445.twitclient;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TweetActivity extends Activity {
	private EditText mInputText;//tweet input form
	private Twitter mTwitter;
	private TextView wordCount;//文字数
	
	public static int MAX_INPUT_LENGTH = 140;//最大文字数(Twitterなので140文字)
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet);
		
		mTwitter = TwitUtils.getTwitterInstance(this);
		
		mInputText = (EditText)findViewById(R.id.input_text);
		
		findViewById(R.id.tweet).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tweet();
			}
		});
		wordCount = (TextView)findViewById(R.id.textView1);
		wordCount.setText("文字数:" + "0");
	}
	
	private void tweet(){
		AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>(){
			@Override
			protected Boolean doInBackground(String... params){
				try{
					mTwitter.updateStatus(params[0]);
					return true;
				}catch(TwitterException e){
					e.printStackTrace();
					return false;
				}
			}
			@Override
			protected void onPostExecute(Boolean result){
				if(result){
					showToast("ツイートしました.");
					finish();
				}else{
					showToast("ツイート失敗しました. 再度ツイートしてください.");
				}
			}
		};
		task.execute(mInputText.getText().toString());
	}

//	mInputText = ((EditText)findViewById(R.id.input_text));
//	wordCount = ((TextView)findViewById(R.id.wordCount));
//	
//	mEditText.addTextChangedListener(new TextWatcher(){
//		@Override
//		public void onTextChanged(CharSequence s, int start, int before, int count){
//			int textColor = Color.GRAY;
//			
//			int txtLength = s.length();
//			textCount.setText(Integer.toString(txtLength) + "/140");
//			
//			if(txtLength > 140){
//				textColor = Color.RED;
//			}
//			textCount.setTextColor(textColor);
//		}
//		@Override
//		public void afterTextChanged(Editable s){
//			
//		}
//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int count, int after){
//			
//		}
//	});
	
	/*
	 * 残り文字数
	 */
	class TweetWordsCount implements TextWatcher{
		public void onTextChanged(CharSequence s, int start, int before, int count){
			int remain = MAX_INPUT_LENGTH - mInputText.getText().length();
			wordCount.setText(Integer.toString(remain));
			if(remain < 0){
				wordCount.setTextColor(Color.RED);
			}else if(remain < 10){
				wordCount.setTextColor(Color.RED);
			}else{
				wordCount.setTextColor(Color.BLACK);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private void showToast(String text){
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
}
