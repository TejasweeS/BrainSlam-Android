package com.android.brainslam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.brainslam.NetworkHandler.AsyncCallBack;
import com.android.brainslam.NetworkHandler.AsyncHandler;
import com.android.brainslam.constants.Constants;
import com.android.listdapters.PlaylistItemsAdapter;
import com.android.utils.Utils;

public class Playlistitems extends Activity implements AsyncCallBack{
	String TAG = "SharingCircularDialog";
	String secretKey,userID;
	ListView lv;
	ArrayList<HashMap<String, String>> userPlaylist; 
	public static final int PLAYLIST = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  setContentView(R.layout.activity_playlist_items);
		
		 lv=(ListView) findViewById(R.id.playlist_items_listview);
		
//	http://10.0.0.201:8888/BrainSlam_API/media.php?operation=getMediaListByType&
//	Secret_Key=2ca45896e7ed01b20cbbcb96e37460e4&User_Id=1&list_Type=Playlist&list_Type_Id=1
		
		
			
		secretKey = Utils.getDataString(Playlistitems.this,
				Constants.PREFS_NAME, Constants.SP_SECRET_KEY);
		userID = Utils.getDataString(Playlistitems.this,
				Constants.PREFS_NAME, Constants.SP_USER_ID);
	
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("operation","getMediaListByType"));
		params.add(new BasicNameValuePair("list_Type","Playlist"));
		
		params.add(new BasicNameValuePair("list_Type_Id","1")); // 1
		
		params.add(new BasicNameValuePair("User_Id",userID));		
		params.add(new BasicNameValuePair("Secret_Key",secretKey));

		new AsyncHandler(Playlistitems.this, Constants.SERVER_URL+"media.php?",PLAYLIST, false, params).execute();
	}

	
	public void sharedots(View v)
	{
		/*
			LayoutInflater inflater = (LayoutInflater) Playlistitems.this
			.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			final View categoryView = inflater.inflate(R.layout.text, null);
			// layout.addView();

			final LinearLayout catDefaultRow = (LinearLayout) categoryView
			.findViewById(R.id.category_row);    
		*/
		
	
		LinearLayout ll=(LinearLayout)v.getParent();
		LinearLayout lll=(LinearLayout)ll.getParent();
		LinearLayout ll2=(LinearLayout)lll.getChildAt(1);
		
		ll.setVisibility(View.GONE);
		ll2.setVisibility(View.VISIBLE);
		
		//ll.setVisibility(View.INVISIBLE);
		
		animate(ll2, R.anim.in_from_right, ll, ll2);
		
		
	}
	public void arrowclick(View v)
	{
	    LinearLayout ll2=(LinearLayout)v.getParent();
		LinearLayout lll=(LinearLayout)ll2.getParent();
		LinearLayout ll=(LinearLayout)lll.getChildAt(0);
	
		//ll.setVisibility(View.VISIBLE);
		animate(ll2, R.anim.out_to_right, ll2, ll);
	}
	
	
	private void animate(final View view, final int animId, final View viewToRemove, final View viewToShow)
	{
		Animation animation = AnimationUtils.loadAnimation(Playlistitems.this, animId);
		view.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener()
		{
			@Override
			public void onAnimationStart(Animation animation) 
			{
			
		
			}
			@Override
			public void onAnimationRepeat(Animation animation) 
			{
			

			}
			@Override
			public void onAnimationEnd(Animation animation) 
			{
				viewToRemove.setVisibility(View.GONE);
				viewToShow.setVisibility(View.VISIBLE);
			
			}
		});
	}
	
	public void backarrowclick(View v)
	{
		
		Playlistitems.this.finish();
		startActivity(new Intent(Playlistitems.this,VideoDetailsActivity.class));
		
	}

	@Override
	public void onReceive(JSONObject jsonObj, int id)
	{
	Log.i("jsonObj--->>>",""+jsonObj);	
	
	try {
		JSONArray jarr=jsonObj.getJSONArray("data");
	
		userPlaylist = new ArrayList<HashMap<String, String>>();
		
		for (int i = 0; i < jarr.length(); i++)
		{
			JSONObject jobject1=jarr.getJSONObject(i);
			
			HashMap<String, String> map = new HashMap<String, String>();
			
			String Media_Id=jobject1.getString("Media_Id");
			String Media_Owner=jobject1.getString("Media_Owner");
			String Name=jobject1.getString("Name");
		
			Log.i("log-->>",Media_Id+" "+Media_Owner+" "+Name);
		 
			map.put("Media_Id", Media_Id);
			map.put("Media_Owner", Media_Owner);
			map.put("Name", Name);
			userPlaylist.add(map);
		}
		
		Log.i("mylistsize--->",""+userPlaylist.size());
		
		lv.setAdapter(new PlaylistItemsAdapter(Playlistitems.this, userPlaylist));

	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}
	
	
}
