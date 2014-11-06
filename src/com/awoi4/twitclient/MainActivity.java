package com.awoi4.twitclient;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.image.SmartImageView;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ListActivity{

	private TweetAdapter mAdapter;
	private Twitter mTwitter;
	private PullToRefreshListView mListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);

		if(!TwitUtils.hasAccessToken(this)){
			Intent intent = new Intent(this, TwitterOAuthActivity.class);
			startActivity(intent);
			finish();
		}else{
			mAdapter = new TweetAdapter(this);
			setListAdapter(mAdapter);
			
			mTwitter = TwitUtils.getTwitterInstance(this);
			reloadTimeLine();
		}
		
		//項目初期化
		//initializeItems();
	}
	
	//TL表示
	private class TweetAdapter extends ArrayAdapter<Status>{
		
		private LayoutInflater mInflater;
		
		public TweetAdapter(Context context){
			super(context, android.R.layout.simple_list_item_1);
			mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.list_item_tweet, null);
			}
			Status item = getItem(position);
			/*
			 * 名前取得
			 */
			TextView name = (TextView)convertView.findViewById(R.id.name);
			name.setText(item.getUser().getName());
			
			/*
			 * スクリーンネーム取得
			 */
			TextView screenName = (TextView)convertView.findViewById(R.id.screen_name);
			screenName.setText("@"+item.getUser().getScreenName());
			
			/*
			 * 本文取得
			 */
			TextView text = (TextView)convertView.findViewById(R.id.text);
			text.setText(item.getText());
			
			/*
			 * アイコン取得
			 */
			SmartImageView icon = (SmartImageView)convertView.findViewById(R.id.icon);
			icon.setImageUrl(item.getUser().getProfileImageURLHttps());
			
			return convertView;
		}
	}
	
	//TL再読み込み
	private void reloadTimeLine(){
		AsyncTask<Void, Void, List<twitter4j.Status>> task = new AsyncTask<Void, Void, List<twitter4j.Status>>(){
			
			@Override
			protected List<twitter4j.Status> doInBackground(Void... params){
				try{
					return mTwitter.getHomeTimeline();
				}catch (TwitterException e){
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(List<twitter4j.Status> result){
				if(result != null){
					mAdapter.clear();
					for(twitter4j.Status status : result){
						mAdapter.add(status);
					}
					getListView().setSelection(0);
				}else{
					showToast("接続エラー");
				}
			}
		};
		task.execute();
	}
	

	
	private void showToast(String text){
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		 Handle action bar item clicks here. The action bar will
//		 automatically handle clicks on the Home/Up button, so long
//		 as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		switch(item.getItemId()){
		
		//更新ボタン
		case R.id.menu_refresh:
			reloadTimeLine();
			return true;
		
		//ツイートボタン
		case R.id.menu_tweet:
			Intent intent = new Intent(this, TweetActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	

}
