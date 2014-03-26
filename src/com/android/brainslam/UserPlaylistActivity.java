package com.android.brainslam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.brainslam.NetworkHandler.AsyncCallBack;
import com.android.brainslam.NetworkHandler.AsyncHandler;
import com.android.brainslam.constants.Constants;
import com.android.brainslam.horizonatlscrollview.HorizontalListView;
import com.android.brainslam.vo.BeanRelated;
import com.android.listdapters.PlaylistAdapter;
import com.android.listdapters.UserRelatedvideoAdapter;
import com.android.utils.ImageLoader;
import com.android.utils.ListSizeHelper;
import com.android.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class UserPlaylistActivity extends Activity implements AsyncCallBack
{
	int i=0;
	String Photo_Url;
	JSONObject User_Details;
	ArrayList<String> playlist;
	ArrayList<HashMap<String, String>> mylist ;
	public static final int  USER_PLAYLISTDATA=1;
	public static final int H_RATED=2;
	public static final int M_VIEWED=3;
	public static final int NEW=4;
	
	String TAG = "UserPlaylistActivity";
	String secretKey,userID;
	ListView playlistview;
	ImageView photo;
	public static String user;
	TextView tv1,tv2,tv3;
	TextView tvuname,tvscore,tvcrew,tvfriends,tvfollowings;
	ImageLoader loader;
	HorizontalListView Relatedvideolist;
	EditText searchEditText;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_userplaylist);
	    
	    loader=new ImageLoader(this);
	    playlistview=(ListView) findViewById(R.id.listView1);
	    tv1=(TextView) findViewById(R.id.tv1);
	    tv2=(TextView) findViewById(R.id.tv2);
	    tv3=(TextView) findViewById(R.id.tv3);
	    final LinearLayout llhide=(LinearLayout) findViewById(R.id.llhide);
	    tvuname=(TextView)findViewById(R.id.playlistusernm);
	    tvscore=(TextView)findViewById(R.id.userplaylist_IQScore);
	    tvcrew=(TextView)findViewById(R.id.userplalist_countcrew);
	    tvfriends=(TextView)findViewById(R.id.userplalist_countfriends);
	    tvfollowings=(TextView)findViewById(R.id.userplalist_countfollowers);
	    photo=(ImageView) findViewById(R.id.userphoto);
	    playlist = new ArrayList<String>();
	    Relatedvideolist = (HorizontalListView) findViewById(R.id.featured_videos_list_view);
	    
	    secretKey = Utils.getDataString(UserPlaylistActivity.this,
				Constants.PREFS_NAME, Constants.SP_SECRET_KEY);
		userID = Utils.getDataString(UserPlaylistActivity.this,
				Constants.PREFS_NAME, Constants.SP_USER_ID);
	
	
	//  -->byNaresh
		user = getIntent().getExtras().getString("Media_Owner_Id");
	
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("operation","getUserDetails"));
		params.add(new BasicNameValuePair("Details_Of",user));
		params.add(new BasicNameValuePair("User_Id",userID));
		params.add(new BasicNameValuePair("Secret_Key",secretKey));

		
		new AsyncHandler(UserPlaylistActivity.this, Constants.SERVER_URL+
				"user.php?",USER_PLAYLISTDATA, false, params).execute();
		
		
		photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				//------------------------------------------------
			if(i==0)
			{
				llhide.setVisibility(View.VISIBLE);
				photo.setBackgroundResource(R.drawable.upbluearrow);
				i=1;
				
			}else
			{
				llhide.setVisibility(View.GONE);		
				i=0;
				
				if(!Photo_Url.equals(""))
					try{
					loader.DisplayImage(Constants.SERVER_URL+Photo_Url, photo);
					}catch(Exception e)
					{
						photo.setBackgroundResource(R.drawable.blueusericon);
					}
				else
					photo.setBackgroundResource(R.drawable.blueusericon);
			}
			//------------------------------------------------	
			}
		});
		
		
		searchEditText = (EditText) findViewById(R.id.et_search);
		InputMethodManager imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0); 
		
		searchEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
				
				ArrayList<String> searchList = new ArrayList<String>();
				
				for(int i=0;i<playlist.size();i++)
				{
					String playerName = playlist.get(i);
					//compare the String in EditText with Names in the ArrayList
					if(Utils.searchData(searchEditText.getText().toString().trim(), playerName)){

						System.out.println("rutuja match found");
						searchList.add(playlist.get(i));
					}
				}
				
				if(searchEditText.getText().toString().trim().length() > 0)
					playlistview.setAdapter(new PlaylistAdapter(UserPlaylistActivity.this,searchList));
				else if(searchEditText.getText().toString().trim().length() == 0)
					playlistview.setAdapter(new PlaylistAdapter(UserPlaylistActivity.this,playlist));
				
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
		});
		
	
	}
	
	public void tabclk(View v)
	{
		switch (v.getId()) {
		case R.id.tv1:
			
			tv1.setBackgroundColor(getResources().getColor(R.color.blue_kaltura));
			tv1.setTextColor(getResources().getColor(R.color.white));
			tv2.setBackgroundColor(getResources().getColor(R.color.black));
			tv2.setTextColor(getResources().getColor(R.color.blue_kaltura));
			tv3.setBackgroundColor(getResources().getColor(R.color.black));
			tv3.setTextColor(getResources().getColor(R.color.blue_kaltura));
			
			
			List<BasicNameValuePair> params3 = new ArrayList<BasicNameValuePair>();
			params3.add(new BasicNameValuePair("operation","getMediaListByType"));
			//params.add(new BasicNameValuePair("Details_Of",user));
			params3.add(new BasicNameValuePair("User_Id",userID));
			params3.add(new BasicNameValuePair("Secret_Key",secretKey));
			
			params3.add(new BasicNameValuePair("list_Type","User"));
			params3.add(new BasicNameValuePair("list_Type_Id",user));   // owner id
			params3.add(new BasicNameValuePair("filter1","New"));

		//	list_Type=User&list_Type_Id=1&filter1=New
			new AsyncHandler(UserPlaylistActivity.this, Constants.SERVER_URL+
					"media.php?",NEW, false, params3).execute();
			
			
			break;
		case R.id.tv2:   // H_rated
			
			tv2.setBackgroundColor(getResources().getColor(R.color.blue_kaltura));
			tv2.setTextColor(getResources().getColor(R.color.white));
			
			tv1.setBackgroundColor(getResources().getColor(R.color.black));
			tv1.setTextColor(getResources().getColor(R.color.blue_kaltura));
			tv3.setBackgroundColor(getResources().getColor(R.color.black));
			tv3.setTextColor(getResources().getColor(R.color.blue_kaltura));
			
			
			List<BasicNameValuePair> params1 = new ArrayList<BasicNameValuePair>();
			params1.add(new BasicNameValuePair("operation","getMediaListByType"));
			//params.add(new BasicNameValuePair("Details_Of",user));
			params1.add(new BasicNameValuePair("User_Id",userID));
			params1.add(new BasicNameValuePair("Secret_Key",secretKey));
			
			params1.add(new BasicNameValuePair("list_Type","User"));
			params1.add(new BasicNameValuePair("list_Type_Id",user));   // owner id
			params1.add(new BasicNameValuePair("filter1","Most Rated"));

		//	list_Type=User&list_Type_Id=1&filter1=New
			new AsyncHandler(UserPlaylistActivity.this, Constants.SERVER_URL+
					"media.php?",H_RATED, false, params1).execute();
			
			
			break;
		case R.id.tv3:    // M_Viewed
			
			tv3.setBackgroundColor(getResources().getColor(R.color.blue_kaltura));
			tv3.setTextColor(getResources().getColor(R.color.white));
			
			tv1.setBackgroundColor(getResources().getColor(R.color.black));
			tv1.setTextColor(getResources().getColor(R.color.blue_kaltura));
			tv2.setBackgroundColor(getResources().getColor(R.color.black));
			tv2.setTextColor(getResources().getColor(R.color.blue_kaltura));
			
			
			List<BasicNameValuePair> params2 = new ArrayList<BasicNameValuePair>();
			params2.add(new BasicNameValuePair("operation","getMediaListByType"));
			//params.add(new BasicNameValuePair("Details_Of",user));
			params2.add(new BasicNameValuePair("User_Id",userID));
			params2.add(new BasicNameValuePair("Secret_Key",secretKey));
			
			params2.add(new BasicNameValuePair("list_Type","User"));
			params2.add(new BasicNameValuePair("list_Type_Id",user));   // owner id
			params2.add(new BasicNameValuePair("filter1","Most Rated"));

		//	list_Type=User&list_Type_Id=1&filter1=New
			new AsyncHandler(UserPlaylistActivity.this, Constants.SERVER_URL+
					"media.php?",M_VIEWED, false, params2).execute();
			
			break;
		default:
			break;
		}
		
	}	
	
	
		List<BeanRelated> categoryMediaList = new ArrayList<BeanRelated>();

		@Override
		public void onReceive(JSONObject jsonObj, int id) 
		{
			Gson gson = new Gson();
			switch(id)
			{
			case USER_PLAYLISTDATA:
			Log.i("json obj==>>",""+jsonObj);
			
			try {
				JSONObject jsondata = new JSONObject(jsonObj.toString());
				JSONObject data = jsondata.getJSONObject("data");
				System.out.println(">>>>"+data);
				User_Details = data.getJSONObject("User_Details");
				String User_Name=User_Details.getString("User_Name");
				
				String Crews_Count=User_Details.getString("Crews_Count");
				System.out.println(">>>>"+Crews_Count);
				
				String Friends_Count=User_Details.getString("Friends_Count");
				System.out.println(">>>>"+Friends_Count);
				
				Photo_Url=User_Details.getString("Photo_Url");
				
				System.out.println(">>>>"+"photo url--> "+Photo_Url);
				
				if(!User_Name.equals(""))
				tvuname.setText(User_Name);
				//if(!User_Name.equals(""))
				tvscore.setText("10");
				//if(!User_Name.equals(""))
				tvfollowings.setText("22");
				if(!Crews_Count.equals(""))
				tvcrew.setText(Crews_Count);
				if(!Friends_Count.equals(""))
				tvfriends.setText(Friends_Count);
				if(!Photo_Url.equals(""))
					try{
						loader.DisplayImage(Constants.SERVER_URL+Photo_Url, photo);
						}catch(Exception e)
						{
							photo.setBackgroundResource(R.drawable.blueusericon);
						}
				else
					photo.setBackgroundResource(R.drawable.blueusericon);
				
				
				//------------------  temp parsing ---------------------------
				JSONObject Playlists = data.getJSONObject("Playlists");
				JSONArray data1 = Playlists.getJSONArray("data");
				playlist = new ArrayList<String>();
				for (int i = 0; i < data1.length(); i++)
				{
					JSONObject jobject=data1.getJSONObject(i);
					String nm=jobject.getString("Playlist_Name");
					System.out.println(">>>>>>nm--"+nm);
					playlist.add(nm);
					
				}
				
				JSONObject Media = data.getJSONObject("Media");
				JSONArray data2 = Media.getJSONArray("data");
				
				 mylist = new ArrayList<HashMap<String, String>>();
				
				//ArrayList<String> data2=new ArrayList<String>();
				for (int i = 0; i < data2.length(); i++)
				{
					JSONObject jobject1=data2.getJSONObject(i);
					
					HashMap<String, String> map = new HashMap<String, String>();
					String Media_Id=jobject1.getString("Media_Id");
					String Media_Owner=jobject1.getString("Media_Owner");
					String Name=jobject1.getString("Name");
					String Average_IQ=jobject1.getString("Average_IQ");
					String Number_Of_Ratings=jobject1.getString("Number_Of_Ratings");
					String Thumbnail_Url=jobject1.getString("Thumbnail_Url");
					String Data_Url=jobject1.getString("Data_Url");
					
					Log.i("--->>",""+Media_Id+" "+Media_Owner+" "+Name+" "+Average_IQ+" "+Number_Of_Ratings+" "+Thumbnail_Url+" "+Data_Url);
					 
					map.put("Media_Id", Media_Id);
					map.put("Media_Owner", Media_Owner);
					map.put("Name", Name);
					map.put("Average_IQ", Average_IQ);
					map.put("Number_Of_Ratings", Number_Of_Ratings);
					map.put("Thumbnail_Url", Thumbnail_Url);
					map.put("Data_Url", Data_Url);
					 
			        mylist.add(map);
			        
				}
				
				Log.i("mylistsize--->",""+mylist.size());
				Relatedvideolist.setAdapter(new UserRelatedvideoAdapter(UserPlaylistActivity.this,mylist));
				
				
				//  {"message":6,"data":{"Playlists":{"data":[{"Description":"Playlist  2","Is_On_Home_Page":0,"Owner_Id":"2","Playlist_Id":"2","Playlist_Name":"Playlist  2",
		
				playlistview.setAdapter(new PlaylistAdapter(UserPlaylistActivity.this,playlist));
				ListSizeHelper.getListViewSize(playlistview);
			
		
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(JsonSyntaxException e)
			{
				e.printStackTrace();
			}
					System.out.println("size--->>"+categoryMediaList.size());
		  break;
			case H_RATED:
				Log.i("json obj==H_RATED>>",""+jsonObj);
				try{
				JSONArray data2 = jsonObj.getJSONArray("data");
				mylist.clear();
				 //mylist = new ArrayList<HashMap<String, String>>();
				
				//ArrayList<String> data2=new ArrayList<String>();
				for (int i = 0; i < data2.length(); i++)
				{
					JSONObject jobject1=data2.getJSONObject(i);
					
					HashMap<String, String> map = new HashMap<String, String>();
					String Media_Id=jobject1.getString("Media_Id");
					String Media_Owner=jobject1.getString("Media_Owner");
					String Name=jobject1.getString("Name");
					String Average_IQ=jobject1.getString("Average_IQ");
					String Number_Of_Ratings=jobject1.getString("Number_Of_Ratings");
					String Thumbnail_Url=jobject1.getString("Thumbnail_Url");
					String Data_Url=jobject1.getString("Data_Url");
					
					Log.i("--->>",""+Media_Id+" "+Media_Owner+" "+Name+" "+Average_IQ+" "+Number_Of_Ratings+" "+Thumbnail_Url+" "+Data_Url);
					map.put("Media_Id", Media_Id);
					map.put("Media_Owner", Media_Owner);
					map.put("Name", Name);
					map.put("Average_IQ", Average_IQ);
					map.put("Number_Of_Ratings", Number_Of_Ratings);
					map.put("Thumbnail_Url", Thumbnail_Url);
					map.put("Data_Url", Data_Url);
					 
			        mylist.add(map);
			        
				}
				
				Log.i("mylistsize--->",""+mylist.size());
				Relatedvideolist.setAdapter(new UserRelatedvideoAdapter(UserPlaylistActivity.this,mylist));
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			break;	
			case M_VIEWED:
				Log.i("json obj==M_VIEWED>>",""+jsonObj);
				try{
					JSONArray data2 = jsonObj.getJSONArray("data");
					mylist.clear();
					 //mylist = new ArrayList<HashMap<String, String>>();
					
					//ArrayList<String> data2=new ArrayList<String>();
					for (int i = 0; i < data2.length(); i++)
					{
						JSONObject jobject1=data2.getJSONObject(i);
						
						HashMap<String, String> map = new HashMap<String, String>();
						String Media_Id=jobject1.getString("Media_Id");
						String Media_Owner=jobject1.getString("Media_Owner");
						String Name=jobject1.getString("Name");
						String Average_IQ=jobject1.getString("Average_IQ");
						String Number_Of_Ratings=jobject1.getString("Number_Of_Ratings");
						String Thumbnail_Url=jobject1.getString("Thumbnail_Url");
						String Data_Url=jobject1.getString("Data_Url");
						
						Log.i("--->>",""+Media_Id+" "+Media_Owner+" "+Name+" "+Average_IQ+" "+Number_Of_Ratings+" "+Thumbnail_Url+" "+Data_Url);
						map.put("Media_Id", Media_Id);
						map.put("Media_Owner", Media_Owner);
						map.put("Name", Name);
						map.put("Average_IQ", Average_IQ);
						map.put("Number_Of_Ratings", Number_Of_Ratings);
						map.put("Thumbnail_Url", Thumbnail_Url);
						map.put("Data_Url", Data_Url);
						 
				        mylist.add(map);
				        
					}
					
					Log.i("mylistsize--->",""+mylist.size());
					Relatedvideolist.setAdapter(new UserRelatedvideoAdapter(UserPlaylistActivity.this,mylist));
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			break;	
				
			case NEW:
				Log.i("json obj==H_RATED>>",""+jsonObj);
				try{
				JSONArray data2 = jsonObj.getJSONArray("data");
				mylist.clear();
				 //mylist = new ArrayList<HashMap<String, String>>();
				
				//ArrayList<String> data2=new ArrayList<String>();
				for (int i = 0; i < data2.length(); i++)
				{
					JSONObject jobject1=data2.getJSONObject(i);
					
					HashMap<String, String> map = new HashMap<String, String>();
					String Media_Id=jobject1.getString("Media_Id");
					String Media_Owner=jobject1.getString("Media_Owner");
					String Name=jobject1.getString("Name");
					String Average_IQ=jobject1.getString("Average_IQ");
					String Number_Of_Ratings=jobject1.getString("Number_Of_Ratings");
					String Thumbnail_Url=jobject1.getString("Thumbnail_Url");
					String Data_Url=jobject1.getString("Data_Url");
					
					Log.i("--->>",""+Media_Id+" "+Media_Owner+" "+Name+" "+Average_IQ+" "+Number_Of_Ratings+" "+Thumbnail_Url+" "+Data_Url);
					map.put("Media_Id", Media_Id);
					map.put("Media_Owner", Media_Owner);
					map.put("Name", Name);
					map.put("Average_IQ", Average_IQ);
					map.put("Number_Of_Ratings", Number_Of_Ratings);
					map.put("Thumbnail_Url", Thumbnail_Url);
					map.put("Data_Url", Data_Url);
					 
			        mylist.add(map);
			        
				}
				
				Log.i("mylistsize--->",""+mylist.size());
				Relatedvideolist.setAdapter(new UserRelatedvideoAdapter(UserPlaylistActivity.this,mylist));
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			break;	
			
			}
		}
		
		public void backclick(View v)
		{
			playlist.clear();
			mylist.clear();
			UserPlaylistActivity.this.finish();
//			startActivity(new Intent(UserPlaylistActivity.this,VideoDetailsActivity.class));
			
		}
		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			if(playlist!=null)
			playlist.clear();
			if(mylist!=null)
			mylist.clear();
			
			UserPlaylistActivity.this.finish();
			
//			startActivity(new Intent(UserPlaylistActivity.this,VideoDetailsActivity.class));
			super.onBackPressed();
		}
		
	
	public void onClickFeaturedVideo(View v)
	{
		
	}
	
	public void arrowclk(View v) 
	{
		Toast.makeText(this,"work in progress",2000).show();		
	   // startActivity(new Intent(UserPlaylistActivity.this,Playlistitems.class));
	
	}
	
	
		
}
