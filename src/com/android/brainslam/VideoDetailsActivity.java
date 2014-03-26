package com.android.brainslam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.brainslam.NetworkHandler.AsyncCallBack;
import com.android.brainslam.NetworkHandler.AsyncHandler;
import com.android.brainslam.NetworkHandler.AsyncHandlerSansSpinner;
import com.android.brainslam.constants.Constants;
import com.android.brainslam.horizonatlscrollview.HorizontalListView;
import com.android.brainslam.twitterconnect.AlertDialogManager;
import com.android.brainslam.twitterconnect.ConnectionDetector;
import com.android.brainslam.vo.BeanRelated;
import com.android.brainslam.vo.FeaturedMediaListData;
import com.android.brainslam.vo.StreamItemDetails;
import com.android.listdapters.CommentListAdapter;
import com.android.listdapters.Relatedvideoadapter;
import com.android.utils.CustomAlertDialog;
import com.android.utils.ImageLoader;
import com.android.utils.ListSizeHelper;
import com.android.utils.Utils;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
public class VideoDetailsActivity extends Activity implements AsyncCallBack,OnTouchListener,OnClickListener {

	public static final int RELATED_MEDIA = 1;
	public static final int MEDIA_COMMENT = 2,POST_COMMENT=3,ADD_FRIEND=4;
	ScrollView sc;
    HorizontalListView Relatedvideolist;
	ImageLoader imgloader;
	ListView mListView ;
	EditText commentEditText;
	
	OrientationEventListener orientationListener;
	private int currentOrientation = -1;
	private static final int ORIENTATION_PORTRAIT_NORMAL = 1;
	private static final int ORIENTATION_PORTRAIT_INVERTED = 2;
	private static final int ORIENTATION_LANDSCAPE_NORMAL = 3;
	private static final int ORIENTATION_LANDSCAPE_INVERTED = 4;
	int fromDegree=0;
	int toDegree=0;
	
	List<Map> list;
	StreamItemDetails item;
	int size=0;int count;
	int scrollecount=0;
	String	sharemsg;
	int loopcount=0;
	// Twitter
	private static Twitter twitter;
	public static RequestToken requestToken;

	// Shared Preferences
	public static SharedPreferences mSharedPreferences;

	// Internet Connection detector
	private ConnectionDetector cd;
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();
	ProgressDialog pDialog;
	private UiLifecycleHelper uiHelper;
	String status = "",str="";
	String TAG = "SharingCircularDialog";
	String secretKey,userID;
	Button loadmore;
	
	SeekBar playerSeekBar;
	VideoView videoView;
	 StreamVideo streamvid;
	TextView videoDuration, currentDuration;
	ImageView playerPause,video_image;
	LinearLayout playerPanel, bufferView;
	 int t=0;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_video_detail);
		
		streamvid=new  StreamVideo();
		 
		
		imgloader=new ImageLoader(VideoDetailsActivity.this);
		list = new ArrayList<Map>();
		sc=(ScrollView) findViewById(R.id.scrollView1);
		Relatedvideolist = (HorizontalListView) findViewById(R.id.featured_videos_list_view);
		loadmore=(Button) findViewById(R.id.btloadmore);
		mListView = (ListView)findViewById(R.id.listcomment);
		
		videoView=(VideoView) findViewById(R.id.video_view);
		videoView.setOnTouchListener(this);
	
		
		
		playerSeekBar = (SeekBar)findViewById(R.id.sb_player_duration);
		//		playerSeekBar.setIndeterminate(true);

		videoDuration = (TextView) findViewById(R.id.tv_player_duration);
		currentDuration = (TextView) findViewById(R.id.tv_player_position);

		playerPause = (ImageView) findViewById(R.id.iv_pause_video);
	

		playerPanel = (LinearLayout) findViewById(R.id.ll_player_panel1);
		bufferView = (LinearLayout) findViewById(R.id.buffer_view);
		bufferView.setVisibility(View.GONE);
		
		//ImageView imgvideourl=(ImageView) findViewById(R.id.imgvideo);
		//ImageView imgshare=(ImageView) findViewById(R.id.video_details_shareicon);
		
		//ImageView imgplay=(ImageView) findViewById(R.id.imgplay);
		ImageView imguser=(ImageView) findViewById(R.id.video_details_username_icon);
		ImageView imgback=(ImageView) findViewById(R.id.video_details_back);
		ImageView imginfinite=(ImageView) findViewById(R.id.video_details_centerIcon);
		ImageView imgplaylist=(ImageView) findViewById(R.id.video_details_playlistIcon);
		//ImageView imgratingicon=(ImageView) findViewById(R.id.video_details_rating_icon);
		LinearLayout liq=(LinearLayout) findViewById(R.id.video_details_IQ_score_icon);
		Button commentButton=(Button) findViewById(R.id.btcomment);
		commentEditText=(EditText) findViewById(R.id.etcomment);
		TextView tvusernm=(TextView) findViewById(R.id.video_details_username);
		TextView tvrating=(TextView) findViewById(R.id.tv_type);
		
		//TextView tviqval=(TextView) findViewById(R.id.iv_above_seek_bar);
		
		TextView tvdescription=(TextView) findViewById(R.id.description);
		
		TextView tvview=(TextView) findViewById(R.id.video_details_noOfViews);
		TextView tvtitle=(TextView) findViewById(R.id.video_details_title);
		TextView tvtags=(TextView) findViewById(R.id.tvtags);
		TextView tvname=(TextView) findViewById(R.id.tv_name);
		
		TextView tviq=(TextView) findViewById(R.id.tviq);
		
		//******************************Getting from Intent **********************************
		
		 item = (StreamItemDetails)getIntent().getSerializableExtra("mediaObj");
//		String videourl = getIntent().getExtras().getString("videourl");
//		String user = getIntent().getExtras().getString("user");
//		String user_url = getIntent().getExtras().getString("user_url");
//		String type = getIntent().getExtras().getString("type");
//		String type_url = getIntent().getExtras().getString("type_url");
//		String tviq = getIntent().getExtras().getString("iq");
//		String tvdesc = getIntent().getExtras().getString("desc");
		
		
	//***************** take these value from main screen by explicitly  **********************************
		
		 Log.i("item.Media_Id==1=>>>",""+item.Media_Id);
		 Log.i("item.Media_Id==1=>>>",""+item.Media_Id);
		 //item
			
			
			
		 str=item.Data_Url;
		System.out.println("user-->>"+item.Data_Url);
		
		
		//--last	
		
		 video_image = (ImageView) findViewById(R.id.video_image);
		 videoView.setVisibility(View.GONE);
		 imgloader.DisplayImage(item.Thumbnail_Url, video_image);
	
		 
		 
		 playerPause.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				playerPause.setVisibility(View.GONE);
				videoView.setVisibility(View.VISIBLE);
				video_image.setVisibility(View.GONE);
				
				if(t==0){
				new StreamVideo().execute(str); 
				  t=1;
				}else{
					
				if(videoView.isPlaying())
				{

					Log.d("BSVP", "Video Paused here");
					videoView.pause();
					playerPause.setVisibility(View.VISIBLE);
					playerPanel.setVisibility(View.VISIBLE);

					
				}
				else
				{
				
					Log.d("BSVP", "Resume Video here");
					videoView.start();
					playerPause.setVisibility(View.GONE);
					bufferView.setVisibility(View.GONE);
					playerPanel.setVisibility(View.INVISIBLE);
				}
				}
				
			}
		});
		
		
		
		
		tvusernm.setText(item.Media_Owner);
	//
		tviq.setText(item.Average_IQ+"");
	   //	tviqval.setText(item.Average_IQ+"");
		
		
		tvdescription.setText(item.Description);
		tvview.setText("Views : "+item.Views);
			
		tvtitle.setText(item.Name);
		
		tvname.setText(item.Name);
		tvtags.setText(item.Tags);
		
		Log.i("--->>>",""+item.Views+" "+item.Name+" "+item.Tags+" -iq->"+item.Average_IQ);
		
		
		if(item.Average_IQ<=29 && item.Average_IQ>=0)
				tvrating.setText("Disturbed");
			else if(item.Average_IQ<=59 && item.Average_IQ>=30)
				tvrating.setText("Sick & Wrong");	
			else if(item.Average_IQ<=89 && item.Average_IQ>=60)
				tvrating.setText("Clunker");
			else if(item.Average_IQ<=119 && item.Average_IQ>=90)
				tvrating.setText("Intense");	
			else if(item.Average_IQ<=150 && item.Average_IQ>=120)
				tvrating.setText("Brain-Slam");	
		
		

		
//		imgloader.DisplayImage(item., imguser);
		//imgloader.DisplayImage(item.Thumbnail_Url, imgratingicon);
//		imgloader.DisplayImage("http://dev-kaltura.brain-slam.com/p/100/sp/10000/thumbnail/entry_id/0_2vyeh7e4/version/100000/height/300/width/300",imgvideourl);
		imguser.setBackgroundResource(R.drawable.user);
		
		
//		imgshare.setOnClickListener(this);
//		imgplay.setOnClickListener(this);
		commentButton.setOnClickListener(this);
		imguser.setOnClickListener(this);
		imgback.setOnClickListener(this);
		imginfinite.setOnClickListener(this);
		imgplaylist.setOnClickListener(this);
		liq.setOnClickListener(this);
		
		//sc.scrollBy(0,0);
		
		// mListView.setAdapter(adapter);
		
		uiHelper = new UiLifecycleHelper(VideoDetailsActivity.this, callback);
		uiHelper.onCreate(savedInstanceState);

	/*	Session.StatusCallback callback1 = new Session.StatusCallback() {
			@Override
			public void call(final Session session, final SessionState state, final Exception exception) {
				onSessionStateChange(session, state, exception);
			}
		};*/

		twitterpreprocesser();
			secretKey = Utils.getDataString(VideoDetailsActivity.this,
				Constants.PREFS_NAME, Constants.SP_SECRET_KEY);
		userID = Utils.getDataString(VideoDetailsActivity.this,
				Constants.PREFS_NAME, Constants.SP_USER_ID);
	
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("operation","getRelatedMedia"));
		params.add(new BasicNameValuePair("Media_Id",item.Media_Id));
		params.add(new BasicNameValuePair("User_Id",userID));
		params.add(new BasicNameValuePair("Secret_Key",secretKey));

				
	new AsyncHandlerSansSpinner(VideoDetailsActivity.this, Constants.SERVER_URL+
			"media.php?",RELATED_MEDIA, false, params).execute();
		//---------------------------------- api call for to get comment-----------------------------------------
		
		List<BasicNameValuePair> params1 = new ArrayList<BasicNameValuePair>();
		params1.add(new BasicNameValuePair("operation","getMediaComment"));
		params1.add(new BasicNameValuePair("Media_Id",item.Media_Id));   //0_w1dwjv7h"));
		params1.add(new BasicNameValuePair("User_Id",userID));
		params1.add(new BasicNameValuePair("Secret_Key",secretKey));
        scrollecount++;
        new AsyncHandlerSansSpinner(VideoDetailsActivity.this, Constants.SERVER_URL+
				"media.php?",MEDIA_COMMENT, false, params1).execute();
        
//  http://dev-kaltura.brain-slam.com/BrainSlam_API/media.php?operation=getMediaComment&Media_Id=0_2vyeh7e4&Secret_Key=2ca45896e7ed01b20cbbcb96e37460e4&User_Id=1
        	
        	
		// hidePanel();

		playerSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) 
			{
				if(fromUser) 
				{
					// this is when actually seekbar has been seeked to a new position
					videoView.seekTo(progress);
					playerSeekBar.setProgress(videoView.getCurrentPosition());
				}

				if(playerPause.getVisibility() != View.VISIBLE)
					videoView.start();
				
				playerSeekBar.postDelayed(onEverySecond, 1000);
			}
		});

		
		rotateAnimation(videoView,  fromDegree, toDegree);
		
		
	//--last
	//	new StreamVideo().execute(str);  
		
		
		
		//	http://10.0.0.201:8888/BrainSlam_API/media.php?
		//operation=getMediaComment&Media_Id=0_2vyeh7e4&User_Id=1&Secret_Key=2ca45896e7ed01b20cbbcb96e37460e4
			
		
		/*if (orientationListener == null) {
			orientationListener = new OrientationEventListener(this,
					SensorManager.SENSOR_DELAY_NORMAL) {

				@SuppressLint("NewApi")
				@Override
				public void onOrientationChanged(int orientation) {

					int lastOrientation = currentOrientation;

					if (orientation >= 315 || orientation < 45) {
						if (currentOrientation != ORIENTATION_PORTRAIT_NORMAL) {
							currentOrientation = ORIENTATION_PORTRAIT_NORMAL;
						}
					} else if (orientation < 315 && orientation >= 225) {
						if (currentOrientation != ORIENTATION_LANDSCAPE_NORMAL) {
							currentOrientation = ORIENTATION_LANDSCAPE_NORMAL;
						}
					} else if (orientation < 225 && orientation >= 135) {
						if (currentOrientation != ORIENTATION_PORTRAIT_INVERTED) {
							currentOrientation = ORIENTATION_PORTRAIT_INVERTED;
						}
					} else { // orientation <135 && orientation > 45
						if (currentOrientation != ORIENTATION_LANDSCAPE_INVERTED) {
							currentOrientation = ORIENTATION_LANDSCAPE_INVERTED;
						}
					}
					
					if (lastOrientation != currentOrientation) {
						
						switch (currentOrientation) {
						case ORIENTATION_PORTRAIT_NORMAL:			//1	

							Log.i("Oreintation", "Orientation = 90");
							if(toDegree==90)
							{
								fromDegree=90;
								toDegree=0;
								rotateAnimation(videoView,  fromDegree, toDegree);
								
							}
							else if(toDegree==270)
							{
								
								fromDegree=270;
								toDegree=360;
								rotateAnimation(videoView,  fromDegree, toDegree);
								
							}
							
							Log.i("Orientation","current Degree"+videoView.getRotation());
							
							
							break;
						case ORIENTATION_LANDSCAPE_NORMAL:    //3
							fromDegree=0;
							toDegree=90;
							rotateAnimation(videoView,  fromDegree, toDegree);
							
							
							Log.i("Oreintation", "Orientation = 0");
							Log.i("Orientation","current Degree"+videoView.getRotation());
							
							break;
						case ORIENTATION_PORTRAIT_INVERTED:  //2
							fromDegree=90;
							toDegree=180;
							rotateAnimation(videoView,  fromDegree, toDegree);
							
							Log.i("Oreintation", "Orientation = 270");
							Log.i("Orientation","current Degree"+videoView.getRotation());
							
							break;
						case ORIENTATION_LANDSCAPE_INVERTED: 		//4
							fromDegree=180;
							toDegree=270;
							rotateAnimation(videoView,  fromDegree, toDegree);
							
							
							
							Log.i("Oreintation", "Orientation = 180");
							Log.i("Orientation","current Degree"+videoView.getRotation());
							
							
							break;
						}
						
					}

				}
			};
		}
		if (orientationListener.canDetectOrientation()) {
			orientationListener.enable();
		}
		
		*/
		
		
		
		
	}
	List<BeanRelated> categoryMediaList = new ArrayList<BeanRelated>();

	@Override
	public void onReceive(JSONObject jsonObj, int id) 
	{
		Gson gson = new Gson();
		switch(id)
		{
		case RELATED_MEDIA:
		Log.i("json obj==>>",""+jsonObj);
			
		try
		{
		JSONObject mediaData = new JSONObject(jsonObj.toString());
		JSONArray jsonMediaArray = mediaData.getJSONArray("data");
		BeanRelated mediaDataArr[] = gson.fromJson(jsonMediaArray.toString(), BeanRelated[].class);
		Log.d("LandingAct", "Categories::" + mediaDataArr[0].Categories
					+ " Desc::" + mediaDataArr[0].Description);
		for (int i = 0; i < mediaDataArr.length; i++) 
			{
				BeanRelated stream = new BeanRelated();
				stream.Media_Id = mediaDataArr[i].Media_Id;
				stream.Media_Owner = mediaDataArr[i].Media_Owner;
				stream.Media_Type = mediaDataArr[i].Media_Type;
				stream.Data_Url = mediaDataArr[i].Data_Url;
				stream.Created_At = mediaDataArr[i].Created_At;
				stream.Updated_At = mediaDataArr[i].Updated_At;
				stream.Views = mediaDataArr[i].Views;
				stream.Width = mediaDataArr[i].Width;
				stream.Height = mediaDataArr[i].Height;
				stream.Duration = mediaDataArr[i].Duration;
				stream.Data_Url = mediaDataArr[i].Data_Url;
				stream.Name = mediaDataArr[i].Name;
				stream.Description = mediaDataArr[i].Description;
				stream.Tags = mediaDataArr[i].Tags;
				stream.Categories = mediaDataArr[i].Categories;
				stream.Average_IQ = mediaDataArr[i].Average_IQ;
				stream.Number_Of_Ratings = mediaDataArr[i].Number_Of_Ratings;
				stream.Tranding_Score = mediaDataArr[i].Tranding_Score;
				stream.Download_Url = mediaDataArr[i].Download_Url;
				stream.Thumbnail_Url = mediaDataArr[i].Thumbnail_Url;

				categoryMediaList.add(stream);
				
			}
			
			Relatedvideolist.setAdapter(new Relatedvideoadapter(VideoDetailsActivity.this,categoryMediaList));
			
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
			
		   	case MEDIA_COMMENT:
		   		
		   		Log.i("json obj comment ==>>",""+jsonObj);
		   		
		   		try
				{
				 JSONObject mediaData = new JSONObject(jsonObj.toString());
				 count=mediaData.getInt("count");
				    
				    if (count % 4 == 0){
					 loopcount=count/4;
				    }else
				    {
				     loopcount=(count/4)+1;
				    }
				    if(scrollecount<=loopcount)
					{}else
					{
					loadmore.setVisibility(View.GONE);
					}
				    
				    JSONArray jsonMediaArray = mediaData.getJSONArray("data");
				    //size=jsonMediaArray.length();
						
				    for (int i = 0; i < jsonMediaArray.length(); i++) 
				    {
					JSONObject  s=jsonMediaArray.getJSONObject(i);
					String comment=s.getString("Comment");
					String commentdt=s.getString("Comment_Datetime");
					//String commentatorpic=s.getString("Comment");
				
					Log.i("comment--"+comment,"commentdt--"+commentdt);
			
					Map map = new HashMap();
					// map.put("userIcon", R.drawable.scott);
					map.put("comment", comment);
					map.put("pic_url", "http://www.uwmbionlp.org/uwmbionlp/images/unknown.jpg");
					map.put("commentdt", commentdt);
					list.add(map);
				
				}
				mListView.setAdapter(new CommentListAdapter(VideoDetailsActivity.this,list));
				ListSizeHelper.getListViewSize(mListView);
				}catch(Exception e)
				{
					e.printStackTrace();
				}
		    		
		   		break;
		   		
			case POST_COMMENT:
				
				Log.i("json obj comment ==>>",""+jsonObj);
				String status = null;
			try {
				 status=jsonObj.getString("message");
				 Log.i("message ==>>",""+status);
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			   	if(status.equals("6"))
			   	{
			   		
			   	    Toast.makeText(this,"Comment has been posted",Toast.LENGTH_SHORT).show();	
			   
			   	    Map<String, String> map = new HashMap<String, String>();
					// map.put("userIcon", R.drawable.scott);
					map.put("comment", commentEditText.getText().toString());
					map.put("pic_url", "http://www.uwmbionlp.org/uwmbionlp/images/unknown.jpg");
					map.put("commentdt", "");
					list.add(0,map);
				 
				}
			   	commentEditText.setText("");
				mListView.setAdapter(new CommentListAdapter(VideoDetailsActivity.this,list));
				ListSizeHelper.getListViewSize(mListView);
			   	    
				
//===========================================================================================			   	    
//			   	    scrollecount=0;
			   	 
//			   	    List<BasicNameValuePair> params1 = new ArrayList<BasicNameValuePair>();
//					params1.add(new BasicNameValuePair("operation","getMediaComment"));
//					params1.add(new BasicNameValuePair("Media_Id","0_2vyeh7e4"));   //0_w1dwjv7h"));
//					params1.add(new BasicNameValuePair("User_Id",userID));
//					params1.add(new BasicNameValuePair("Secret_Key",secretKey));
//			        scrollecount++;
//			        new AsyncHandlerSansSpinner(VideoDetailsActivity.this, Constants.SERVER_URL+
//							"media.php?",MEDIA_COMMENT, false, params1).execute();
			        
//			        
//			   	}else
//			   	{
//			   		 Toast.makeText(this,"ERRRRR--->> "+jsonObj,Toast.LENGTH_SHORT).show();	
//			   	}
				break;
				
			case ADD_FRIEND:
				
					Log.i("json obj add friend ==>>",""+jsonObj);
				
					try 
					{
						String  msg=jsonObj.getString("message");
						Log.i("message ==>>",""+msg);
						if(msg.equals("6"))
						{
						 Toast.makeText(this,"Friend Added successfully",1500).show();
							 
					 }else if(msg.equals("5"))
					 {
						 Toast.makeText(this,"Friend Already Added ",1500).show(); 
					 }
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				break;
		}
	
	}
	
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) 
		{
		case R.id.video_details_back:

			startActivity(new Intent(VideoDetailsActivity.this, BrainSlamMainActivity.class));
			

//			startActivity(new Intent(VideoDetailsActivity.this, BrainSlamMainActivity.class));
	//		VideoDetailsActivity.this.finish();

			
			break;
			
		case R.id.video_details_centerIcon:   //infinite

			Toast.makeText(this,"work in progress",2000).show();
			
			
			break;
		case R.id.video_details_playlistIcon:
			
			if (!Utils.checkeInternetConnection(VideoDetailsActivity.this))
			{
				Log.e("network error","No Internet Connection");
				new CustomAlertDialog(VideoDetailsActivity.this,"No Internet Connection").showDialog();
			}else
			{
				//t=0;
				//startActivity(new Intent(VideoDetailsActivity.this,MainPlaylistActivity.class));
				Toast.makeText(this,"work in progress",2000).show();
			}
			
			//startActivity(new Intent(VideoDetailsActivity.this,MainPlaylistActivity.class));
			Toast.makeText(this,"work in progress",2000).show();
			
			break;
		case R.id.video_details_IQ_score_icon:
			
			Intent videoRatingIntent=new Intent(VideoDetailsActivity.this,VideoRatingActivity.class);
			videoRatingIntent.putExtra("FromUploadScreen", false);
			t=0;
			Log.v("Rutuja", "Sending media ID:: "+item.Media_Id);
			videoRatingIntent.putExtra("MediaID", item.Media_Id);
			videoRatingIntent.putExtra("ThumbnailUrl", item.Thumbnail_Url);
			startActivity(videoRatingIntent);
			
		break;
		case R.id.video_details_username_icon:
			
			
			if(videoView.isPlaying())
			{
				//videoView.pause();
				videoView.stopPlayback();
				playerPause.setVisibility(View.VISIBLE);
				videoView.setVisibility(View.GONE);
				imgloader.DisplayImage(item.Thumbnail_Url, video_image);
				Log.i("iiiddd==00=>>>","ooooooooooooo");
			}
				
			videoView.stopPlayback();
			videoView.clearFocus();
			playerPause.setVisibility(View.VISIBLE);
			videoView.setVisibility(View.GONE);
			imgloader.DisplayImage(item.Thumbnail_Url, video_image);
			t=0;
			if (!Utils.checkeInternetConnection(VideoDetailsActivity.this))
			{
				Log.e("network error","No Internet Connection");
				new CustomAlertDialog(VideoDetailsActivity.this,"No Internet Connection").showDialog();
			}else
			{
			Intent intent = new Intent(VideoDetailsActivity.this,UserPlaylistActivity.class);
			Log.i("iiiddd===>>>",""+item.Media_Owner_Id);
			intent.putExtra("Media_Owner_Id", item.Media_Owner_Id);
			startActivity(intent);
			}	
		break;
		
		
		
		case R.id.btcomment:
			
			//****************************** Add Comment **********************************
			String comment=commentEditText.getText().toString();
			Log.i("888--->>",userID+"  "+secretKey);//192  2ca055d66c9aac0f451b71f776ec2d12
			List<BasicNameValuePair> params1 = new ArrayList<BasicNameValuePair>();
			params1.add(new BasicNameValuePair("operation","addMediaComment"));
			params1.add(new BasicNameValuePair("Media_Id",item.Media_Id));   //0_w1dwjv7h"));
			params1.add(new BasicNameValuePair("User_Id",userID));
			params1.add(new BasicNameValuePair("Secret_Key",secretKey));
			params1.add(new BasicNameValuePair("Comment",comment));

			new AsyncHandler(VideoDetailsActivity.this, Constants.SERVER_URL+
					"media.php?",POST_COMMENT, false, params1).execute();
			
			
			break;
			
//		    case R.id.video_details_shareicon:
//		    
		    //	startActivity(new Intent(VideoDetailsActivity.this,SharingCircularDialog.class));
		    
		  /*  sharemsg="Hi Android,What's up...!";
		    	
		    final android.app.Dialog sharedialod = new android.app.Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		    sharedialod.setContentView(R.layout.circular_sharingpopup_new);
		    sharedialod.show();
		    	
		    ImageView imgemail=(ImageView)sharedialod.findViewById(R.id.circular_sharingpopup_chats);
		    ImageView imgmsg=(ImageView)sharedialod.findViewById(R.id.circular_sharingpopup_message);
		    ImageView imgfb=(ImageView)sharedialod.findViewById(R.id.circular_sharingpopup_facebook);
		    ImageView imgtwitter=(ImageView)sharedialod.findViewById(R.id.circular_sharingpopup_twitter);
		    ImageView imggoogleplus=(ImageView)sharedialod.findViewById(R.id.circular_sharingpopup_googleplus);
		    
		    imgemail.setOnClickListener(new OnClickListener()
		    {
				@Override
				public void onClick(View arg0) {
					
					Intent email = new Intent(Intent.ACTION_SEND);
					email.putExtra(Intent.EXTRA_EMAIL, new String[]{ ""});
					//email.putExtra(Intent.EXTRA_CC, new String[]{ to});
					//email.putExtra(Intent.EXTRA_BCC, new String[]{to});
					email.putExtra(Intent.EXTRA_SUBJECT, "");
					email.putExtra(Intent.EXTRA_TEXT, "");

					email.setType("message/rfc822");
					startActivity(Intent.createChooser(email, "Choose an Email client :"));
					sharedialod.dismiss();
				}
			});	
		    imgmsg.setOnClickListener(new OnClickListener()
		    {
				@Override
				public void onClick(View arg0) {
				
					startActivity(new Intent(VideoDetailsActivity.this,Message_Activity.class));
					sharedialod.dismiss();
				
				}});
		    
		    imgfb.setOnClickListener(new OnClickListener()
		    {
				@Override
				public void onClick(View arg0) {
					shareViaFacebook();	
					
				}});
		    
		    imgtwitter.setOnClickListener(new OnClickListener()
		    {
				@Override
				public void onClick(View arg0) {
					loginToTwitter();
					
					Log.w("login", "success");
					if (isTwitterLoggedInAlready())
					{
						Intent statusUpdateActivity = new
						Intent(VideoDetailsActivity.this,UpdateTwitterStatusActivity.class);
						
						statusUpdateActivity.putExtra("share_msg",sharemsg);
						
						startActivity(statusUpdateActivity);
						sharedialod.dismiss();
					}
					
				}});
		    
		    imggoogleplus.setOnClickListener(new OnClickListener()
		    {
				@Override
				public void onClick(View arg0)
				{
					
					Intent intent=new Intent(VideoDetailsActivity.this,Share_Googleplus_Activity.class);
					intent.putExtra("share_msg",sharemsg);
					startActivity(intent);	
					
					sharedialod.dismiss();
				}});
		    */
//			break;
		
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);

			//for facebook share dialog responses
			uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
			@Override
			public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
				Log.e(TAG, String.format("Error: %s", error.toString()));
			}

			@Override
			public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
				Log.i(TAG, "Success!");
			}
		});
	}
	 public void twitterpreprocesser() 
	 {
			cd = new ConnectionDetector(getApplicationContext());

			// Check if Internet present
			if (!cd.isConnectingToInternet()) {
				// Internet Connection is not present
				alert.showAlertDialog(VideoDetailsActivity.this, "Internet Connection Error",
						"Please connect to working Internet connection", false);
				// stop executing code by return
				return;
			}

			// Check if twitter keys are set
			if (com.android.brainslam.constants.Constants.TWITTER_CONSUMER_KEY.trim().length() == 0
					|| com.android.brainslam.constants.Constants.TWITTER_CONSUMER_SECRET.trim().length() == 0) {
				// Internet Connection is not present
				alert.showAlertDialog(VideoDetailsActivity.this, "Twitter oAuth tokens",
						"Please set your twitter oauth tokens first!", false);
				// stop executing code by return
				return;
			}

			mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
			/*//**
			 * This if conditions is tested once is redirected from twitter page.
			 * Parse the uri to get oAuth Verifier
			 * */
			if (!isTwitterLoggedInAlready()) {
				Log.e("oncreate", "in If");
				Uri uri = getIntent().getData();
				Log.e("uri", String.valueOf(uri));
				if (uri != null && uri.toString().startsWith(com.android.brainslam.constants.Constants.TWITTER_CALLBACK_URL)) {
					// oAuth verifier
					String verifier = uri.getQueryParameter(com.android.brainslam.constants.Constants.URL_TWITTER_OAUTH_VERIFIER);
					Log.e("uri", verifier);

					try {
						// Get the access token
						AccessToken accessToken = twitter.getOAuthAccessToken(
								requestToken, verifier);
						Log.e("aceess Token", String.valueOf(accessToken));

						
						Utils.storeDatafortwitter(VideoDetailsActivity.this,"Twitpref", 
								com.android.brainslam.constants.Constants.PREF_KEY_OAUTH_TOKEN,	accessToken.getToken(), 
								com.android.brainslam.constants.Constants.PREF_KEY_OAUTH_SECRET,accessToken.getTokenSecret(),
								com.android.brainslam.constants.Constants.PREF_KEY_TWITTER_LOGIN,true);

						
						/*// Shared Preferences
						Editor e = mSharedPreferences.edit();

						// After getting access token, access token secret
						// store them in application preferences
						e.putString(com.android.brainslam.constants.Constants.PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
						e.putString(com.android.brainslam.constants.Constants.PREF_KEY_OAUTH_SECRET,
								accessToken.getTokenSecret());
						// Store login status - true
						e.putBoolean(com.android.brainslam.constants.Constants.PREF_KEY_TWITTER_LOGIN, true);
						e.commit(); // save changes
*/
						Log.e("Twitter OAuth Token", "> " + accessToken.getToken());
						t=0;
						// Getting user details from twitter
						// For now i am getting his name only
						long userID = accessToken.getUserId();
						User user1 = twitter.showUser(userID);
						String username = user1.getName();

						Intent statusUpdateActivity = new Intent(VideoDetailsActivity.this,UpdateTwitterStatusActivity.class);
						startActivity(statusUpdateActivity);

						
					} catch (Exception e) {
						// Check log for login errors
						Log.e("Twitter Login Error", "> " + e.getMessage());
					}
				}
			}
				
	}
		private void loginToTwitter() {
			// Check if already logged in
			if (!isTwitterLoggedInAlready()) {
				Log.w("After", "in If login twitter");
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(com.android.brainslam.constants.Constants.TWITTER_CONSUMER_KEY);
				builder.setOAuthConsumerSecret(com.android.brainslam.constants.Constants.TWITTER_CONSUMER_SECRET);
				Configuration configuration = builder.build();
				Log.w("After", "in If login twitter2");
				final TwitterFactory factory = new TwitterFactory(configuration);

				try {
					Log.w("After", "in try login twitter3");
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							twitter = factory.getInstance();
							System.out.println(twitter);
							Log.e("in run", "above token");

							try {
								requestToken = twitter
										.getOAuthRequestToken(com.android.brainslam.constants.Constants.TWITTER_CALLBACK_URL);
							} catch (TwitterException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Log.e("in run", "below token");
							System.out.println("Authencate uri"
									+ requestToken.getAuthenticationURL());
							boolean installed  =   appInstalledOrNot("com.twitter.android");  
							if(installed) {
								//This intent will help you to launch if the package is already installed
								Intent LaunchIntent = getPackageManager()
										.getLaunchIntentForPackage("com.twitter.android");
								startActivity(LaunchIntent);

								System.out.println("App already installed on your phone");        
							}
							else {
								System.out.println("App is not installed on your phone");
								startActivity(new Intent(Intent.ACTION_VIEW, Uri
										.parse(requestToken.getAuthenticationURL())));
							}
							try {
								getPackageManager().getPackageInfo("com.twitter.android", 0);
								Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=USERID"));
							    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							    startActivity(intent);
							    } catch (NameNotFoundException e) {
								// TODO Auto-generated catch block
								 startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse(requestToken.getAuthenticationURL())));
							}

						}
					}).start();


					//Log.e("After request:", String.valueOf(requestToken));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else
			{
				// user already logged into twitter
				  Toast.makeText(getApplicationContext(),
						"Already Logged into twitter", Toast.LENGTH_LONG).show();
			}
		}

	 private boolean isTwitterLoggedInAlready()
	 {
			Log.i("msharepreferences-->","11111111"+ Utils.getDataBoolean(VideoDetailsActivity.this,"Twitpref",com.android.brainslam.constants.Constants.PREF_KEY_TWITTER_LOGIN));
			
			return Utils.getDataBoolean(VideoDetailsActivity.this,"Twitpref",com.android.brainslam.constants.Constants.PREF_KEY_TWITTER_LOGIN);
			
	 }
	 
	 
		private Session.StatusCallback callback = new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				onSessionStateChange(session, state, exception);
			}
		};

		// facebook share function
		private void shareViaFacebook()
		{

			if (FacebookDialog.canPresentShareDialog(getApplicationContext(), 
					FacebookDialog.ShareDialogFeature.SHARE_DIALOG))
			{
				Uri path = Uri.parse("android.resource://"+VideoDetailsActivity.this.getPackageName()+"/"+R.drawable.ic_launcher);

				FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(VideoDetailsActivity.this)
				.setDescription("to be shared")
				.setPicture(path.getPath())
				.build();
				uiHelper.trackPendingDialogCall(shareDialog.present());
			}
			else
			{
				Log.v(TAG, "No Native App");
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						this);
		 
					// set title
					alertDialogBuilder.setTitle("Your Title");
		 
					// set dialog message
					alertDialogBuilder
						.setMessage("You don't have Facebook App in your phone,Please install it")
						.setCancelable(false)
						.setPositiveButton("Install",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								
								final String appPackageName = "com.facebook.katana"; // getPackageName() from Context or Activity object
								try {
								    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
								} catch (android.content.ActivityNotFoundException anfe) {
								    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
								}
								
								
								dialog.cancel();
								VideoDetailsActivity.this.finish();
							
								
							}
						  })
						.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});
		 
						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
		 
						// show it
						alertDialog.show();
					}
			
				
				/* if device dont have native application then app will use
				 * webdialog for sharing
				 * */
//				Log.i("Session-->",""+Session.getActiveSession());
//				Log.i("Session-1->",""+status);
	//
//							if(status.equals("loggedin"))
//							{
//								publishFeedDialog();
//							}
//							Log.e("in run", "below token");
//							System.out.println("Authencate uri"
//									+ requestToken.getAuthenticationURL());
//							boolean installed  =   appInstalledOrNot("com.twitter.android");  
//							if(installed) {
//								//This intent will help you to launch if the package is already installed
//								Intent LaunchIntent = getPackageManager()
//										.getLaunchIntentForPackage("com.twitter.android");
//								startActivity(LaunchIntent);
//
//								System.out.println("App already installed on your phone");        
//							}
//							else {
//								System.out.println("App is not installed on your phone");
//								startActivity(new Intent(Intent.ACTION_VIEW, Uri
//										.parse(requestToken.getAuthenticationURL())));
//							}
//							try {
//								getPackageManager().getPackageInfo("com.twitter.android", 0);
//								Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=USERID"));
//							    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//							    startActivity(intent);
//							    } catch (NameNotFoundException e) {
//								// TODO Auto-generated catch block
//								 startActivity(new Intent(Intent.ACTION_VIEW, Uri
//								.parse(requestToken.getAuthenticationURL())));
//							}
//
//						}
//					}).start();
//
//
//					//Log.e("After request:", String.valueOf(requestToken));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			} else {
//				// user already logged into twitter
//				Toast.makeText(getApplicationContext(),
//						"Already Logged into twitter", Toast.LENGTH_LONG).show();
//			}
//		}
//
//	 private boolean isTwitterLoggedInAlready() {
//			// return twitter login status from Shared Preferences
//			return mSharedPreferences.getBoolean(com.android.brainslam.constants.Constants.PREF_KEY_TWITTER_LOGIN, false);
//		}
//	 
//		private Session.StatusCallback callback = new Session.StatusCallback() {
//			@Override
//			public void call(Session session, SessionState state, Exception exception) {
//				onSessionStateChange(session, state, exception);
//			}
//		};
//
//		// facebook share function
//		private void shareViaFacebook()
//		{
//
//			if (FacebookDialog.canPresentShareDialog(getApplicationContext(), 
//					FacebookDialog.ShareDialogFeature.SHARE_DIALOG))
//			{
//				Uri path = Uri.parse("android.resource://"+VideoDetailsActivity.this.getPackageName()+"/"+R.drawable.ic_launcher);
//
//				FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
//				.setDescription("to be shared")
//				.setPicture(path.getPath())
//				.build();
//				uiHelper.trackPendingDialogCall(shareDialog.present());
//			}
//			else
//			{
//				Log.v(TAG, "No Native App");
//				
//				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//						this);
//		 
//					// set title
//					alertDialogBuilder.setTitle("Your Title");
//		 
//					// set dialog message
//					alertDialogBuilder
//						.setMessage("You don't have Facebook App in your phone,Please install it")
//						.setCancelable(false)
//						.setPositiveButton("Install",new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,int id) {
//								
//								final String appPackageName = "com.facebook.katana"; // getPackageName() from Context or Activity object
//								try {
//								    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//								} catch (android.content.ActivityNotFoundException anfe) {
//								    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
//								}
//								
//								
//								dialog.cancel();
//								VideoDetailsActivity.this.finish();
//							
//								
//							}
//						  })
//						.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,int id) {
//								// if this button is clicked, just close
//								// the dialog box and do nothing
//								dialog.cancel();
//							}
//						});
//		 
//						// create alert dialog
//						AlertDialog alertDialog = alertDialogBuilder.create();
//		 
//						// show it
//						alertDialog.show();
//					}
//			
//				
//				/* if device dont have native application then app will use
//				 * webdialog for sharing
//				 * */
////				Log.i("Session-->",""+Session.getActiveSession());
////				Log.i("Session-1->",""+status);
//	//
////							if(status.equals("loggedin"))
////							{
////								publishFeedDialog();
////							}
////							else
////							{
////								Dialog d=new Dialog(SharingCircularDialog.this);
////								d.setContentView(R.layout.dialogfb);
////								
////								
////								LoginButton facebookLoginButton=(LoginButton)d.findViewById(R.id.facebookLoginButton);
////								facebookLoginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
////									@Override
////									public void onUserInfoFetched(GraphUser user) {
////										//LoginActivity.this.user = user;
////				
////										Log.v("LoginActivity","user setUserInfoChangedCallback");
////				
////										if(user != null)
////										{
////											publishFeedDialog();
////										}
////										else
////											Log.v("LoginActivity","user null");
////									}
////								});	
//
//			}
//	 private String onSessionStateChange(Session session, SessionState state, Exception exception) {
//
//			if (state.isOpened()) 
//			{
//				Log.i(TAG, "Logged in...");
//				status= "loggedin";
//			} else if (state.isClosed()) 
//			{
//				Log.i(TAG, "Logged out...");
//				status= "loggedout";
//			}
//			return status;
//		}
//	 
			}
	 private String onSessionStateChange(Session session, SessionState state, Exception exception) {

			if (state.isOpened()) 
			{
				Log.i(TAG, "Logged in...");
				status= "loggedin";
			} else if (state.isClosed()) 
			{
				Log.i(TAG, "Logged out...");
				status= "loggedout";
			}
			return status;
		}
	 
	 
	 public void loadmore(View v)
	 {
		    scrollecount++;
		    
		    if(scrollecount<=loopcount)
			{
				Log.i("refreshed -->> ","scrollecount"+scrollecount);
				
				List<BasicNameValuePair> params1 = new ArrayList<BasicNameValuePair>();
				params1.add(new BasicNameValuePair("operation","getMediaComment"));
				params1.add(new BasicNameValuePair("Media_Id","0_2vyeh7e4"));   //0_w1dwjv7h"));
				params1.add(new BasicNameValuePair("User_Id",userID));
				params1.add(new BasicNameValuePair("Secret_Key",secretKey));
				
				params1.add(new BasicNameValuePair("pageSize","4"));
				
				params1.add(new BasicNameValuePair("pageIndex",""+scrollecount));
				
				new AsyncHandler(VideoDetailsActivity.this, Constants.SERVER_URL+
						"media.php?",MEDIA_COMMENT, false, params1).execute();
			}else
			{
				loadmore.setVisibility(View.GONE);
			}
		 
	 }
	 @Override
		protected void onResume() {

			 super.onResume();
			 uiHelper.onResume();
			 int t=0;
			 videoView.setVisibility(View.GONE);
			 video_image.setVisibility(View.VISIBLE);
			 imgloader.DisplayImage(item.Thumbnail_Url, video_image);
			 
		}
		@Override
		public void onPause() {
			super.onPause();
			uiHelper.onPause();
			
			videoView.stopPlayback();
			videoView.clearFocus();
			playerPause.setVisibility(View.VISIBLE);
			videoView.setVisibility(View.GONE);
			imgloader.DisplayImage(item.Thumbnail_Url, video_image);
			t=0;
			
			streamvid.cancel(true);
		}
		@Override
		public void onDestroy() {
			super.onDestroy();
			uiHelper.onDestroy();
			
			videoView.stopPlayback();
			
		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			uiHelper.onSaveInstanceState(outState);
		}

		@Override
		protected void onStop() {
			// TODO Auto-generated method stub
			super.onStop();
			uiHelper.onStop(); 
		}

		private boolean appInstalledOrNot(String uri) {
			PackageManager pm = getPackageManager();
			boolean app_installed = false;
			try {
				pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
				app_installed = true;
			}
			catch (PackageManager.NameNotFoundException e) {
				app_installed = false;
			}
			return app_installed ;
		}
		
//		Dialog bufferDialog;
		int totalDuration, duration;
		int current;

		
	
		private class StreamVideo extends AsyncTask<String, Void, Void> 
		{
			String videoUrl = null;

			@Override
			protected void onPreExecute() 
			{
				super.onPreExecute();

				bufferView.setVisibility(View.VISIBLE);
				
			}

			@Override
			protected Void doInBackground(String... url) 
			{
				videoUrl = url[0];
				return null;
			}

			@Override
			protected void onPostExecute(Void args) {

				try {
					
					Log.i("onpost execute1 ","onpost");
					
					Uri video = Uri.parse(videoUrl);
	//				Uri video = Uri.parse(videoUrl);
					Log.i("onpost execute2 ","onpost");
					//				videoView.setMediaController(mediacontroller);
					
//					videoView.setMediaController(null);
					
					Log.i("onpost execute 3","onpost");
					videoView.setVideoURI(Uri.parse(videoUrl));
					Log.i("onpost execute4 ","onpost");
					videoView.requestFocus();
					Log.i("onpost execute5 ","onpost");

					videoView.setOnPreparedListener(new OnPreparedListener() { 
						// Close the progress bar and play the video
						public void onPrepared(MediaPlayer mp) {
						
							bufferView.setVisibility(View.GONE);
							videoView.start();
							playerPanel.setVisibility(View.INVISIBLE);
							duration = mp.getDuration()/1000;
						    videoDuration.setText(""+getFormattedTime(duration));
							playerSeekBar.setMax(videoView.getDuration());
							playerSeekBar.postDelayed(onEverySecond, 1000);
							totalDuration = videoView.getDuration();
							current = videoView.getCurrentPosition();
						}
					});

				} catch (Exception e) {
					bufferView.setVisibility(View.GONE);
//					Log.e("Error", e.getMessage());
					e.printStackTrace();
				}
			}
		}

		int lastBufferPercentage = 0;
		Runnable onEverySecond=new Runnable() {
			@Override
			public void run() 
			{
				
				if(lastBufferPercentage == videoView.getCurrentPosition() && videoView.getBufferPercentage() < 100)
				{
					if(!videoView.isPlaying())
						bufferView.setVisibility(View.VISIBLE);		
					else
						bufferView.setVisibility(View.GONE);
				}
				else
				{
					bufferView.setVisibility(View.GONE);
				}

// --mayur
//Log.d("", "videoView.getBufferPercentage()::"+videoView.getBufferPercentage());
//				if(!videoView.isPlaying() && playerPause.getVisibility() != View.VISIBLE)
//				{
//					bufferView.setVisibility(View.VISIBLE);		
//				}
//				else
//				{
//					bufferView.setVisibility(View.GONE);
//				}
				
				lastBufferPercentage = videoView.getCurrentPosition();

				

				if(playerSeekBar != null) 
				{
					Log.d("BSVP", "video progress::"+videoView.getCurrentPosition()+"  video getBufferPercentage::"+videoView.getBufferPercentage());

					playerSeekBar.setProgress(videoView.getCurrentPosition());
					playerSeekBar.setSecondaryProgress(videoView.getBufferPercentage() * 1000);
					playerSeekBar.postInvalidate();
				}

				if(videoView.isPlaying()) 
				{
					bufferView.setVisibility(View.GONE);
					playerPause.setVisibility(View.GONE);
					playerSeekBar.postDelayed(onEverySecond, 1000);
				}

				//-------------------------------------
				String countText;

				int countUp = videoView.getCurrentPosition();
				countUp = Math.round(countUp / 1000);

				long a = (long) countUp;
				if (countUp % 60000 <= 9) {
					countText = (a / 60000) + ":0" + (a % 60000);
				} else {
					countText = (a / 60000) + ":" + (a % 60000);           
				} 

				currentDuration.setText(countText);
			}
		};

		private void hidePanel()
		{
			new Handler().postDelayed(new Runnable() 
			{
				public void run() 
				{
					animate(playerPanel, R.anim.fade_out);//panel_player
				}	
			}, 10000);		
		}
		
		private void animate(final View view, final int animId)
		{
			Animation animation = AnimationUtils.loadAnimation(VideoDetailsActivity.this, animId);
			view.startAnimation(animation);

			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) 
				{
					view.setVisibility(View.GONE);
				}
			});
		}
		public String getFormattedTime(int duration)
		{
			int hours = duration / 3600; 
			int minutes = (duration / 60) - (hours * 60);
			int seconds = duration - (hours * 3600) - (minutes * 60) ;
			return String.format("%d:%02d:%02d", hours, minutes, seconds);
		}
		
		public boolean onTouch(View v, MotionEvent event) 
		{
			if(event.getAction() == MotionEvent.ACTION_DOWN)
			{
				Log.d("BSVP", "Here in ontouch listener");
				playerPanel.setVisibility(View.VISIBLE);

				
				//hidePanel();

				if(videoView != null)
				{
					if(videoView.isPlaying())
					{
						Log.d("BSVP", "Video Paused here");
						videoView.pause();
						
						playerPause.setVisibility(View.VISIBLE);
						playerPanel.setVisibility(View.VISIBLE);
					}
					else
					{
						Log.d("BSVP", "Resume Video here");
						videoView.start();
						playerPanel.setVisibility(View.INVISIBLE);
						playerPause.setVisibility(View.GONE);
					}
				}
				return true;	
			}
			else
			{
				return false;
			}
		}
		
		public void shareonclick(View v)
		{
			    sharemsg="Hi Android,What's up...!";
		
			    final android.app.Dialog sharedialod = new android.app.Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
			    sharedialod.setContentView(R.layout.circular_sharingpopup_new);
			    sharedialod.show();
			    	
			    ImageView imgemail=(ImageView)sharedialod.findViewById(R.id.circular_sharingpopup_chats);
			    ImageView imgmsg=(ImageView)sharedialod.findViewById(R.id.circular_sharingpopup_message);
			    ImageView imgfb=(ImageView)sharedialod.findViewById(R.id.circular_sharingpopup_facebook);
			    ImageView imgtwitter=(ImageView)sharedialod.findViewById(R.id.circular_sharingpopup_twitter);
			    ImageView imggoogleplus=(ImageView)sharedialod.findViewById(R.id.circular_sharingpopup_googleplus);
			   
			    Button closebtn=(Button)sharedialod.findViewById(R.id.button1);
				   
			    imgemail.setOnClickListener(new OnClickListener()
			    {
					@Override
					public void onClick(View arg0) {
						
						Intent email = new Intent(Intent.ACTION_SEND);
						email.putExtra(Intent.EXTRA_EMAIL, new String[]{ ""});
						//email.putExtra(Intent.EXTRA_CC, new String[]{ to});
						//email.putExtra(Intent.EXTRA_BCC, new String[]{to});
						email.putExtra(Intent.EXTRA_SUBJECT, "");
						email.putExtra(Intent.EXTRA_TEXT, "");

						email.setType("message/rfc822");
						startActivity(Intent.createChooser(email, "Choose an Email client :"));
						sharedialod.dismiss();
					}
				});	
			    imgmsg.setOnClickListener(new OnClickListener()
			    {
					@Override
					public void onClick(View arg0) {
						
						TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					    int simState = telMgr.getSimState();
					            switch (simState) {
					                case TelephonyManager.SIM_STATE_ABSENT:
					                  Toast.makeText(VideoDetailsActivity.this,"No SIM Card Inserted",Toast.LENGTH_LONG).show();
					                    break;
					                case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
					                    // do something
					                    break;
					                case TelephonyManager.SIM_STATE_PIN_REQUIRED:
					                    // do something
					                    break;
					                case TelephonyManager.SIM_STATE_PUK_REQUIRED:
					                    // do something
					                    break;
					                case TelephonyManager.SIM_STATE_READY:
					                	t=0;
//										startActivity(new Intent(VideoDetailsActivity.this,Message_Activity.class));
										sharedialod.dismiss();
										
					                    break;
					                case TelephonyManager.SIM_STATE_UNKNOWN:
					                    // do something
					                    break;
					            }
						
						
					
					}});
			    
			    imgfb.setOnClickListener(new OnClickListener()
			    {
					@Override
					public void onClick(View arg0) {
						if (!Utils.checkeInternetConnection(VideoDetailsActivity.this))
						{
							Log.e("network error","No Internet Connection");
							new CustomAlertDialog(VideoDetailsActivity.this,"No Internet Connection").showDialog();
						}else
						{
						shareViaFacebook();	
						
						}
					}});
			    
			    imgtwitter.setOnClickListener(new OnClickListener()
			    {
					@Override
					public void onClick(View arg0) {
						
						if (!Utils.checkeInternetConnection(VideoDetailsActivity.this))
						{
							Log.e("network error","No Internet Connection");
							new CustomAlertDialog(VideoDetailsActivity.this,"No Internet Connection").showDialog();
						}else
						{
						
						loginToTwitter();
						
						Log.w("login", "success");
						
						if (isTwitterLoggedInAlready())
						{
							t=0;
							Intent statusUpdateActivity = new
							Intent(VideoDetailsActivity.this,UpdateTwitterStatusActivity.class);
							
							statusUpdateActivity.putExtra("share_msg",sharemsg);
							
							startActivity(statusUpdateActivity);
							
						}
						sharedialod.dismiss();
						}
						
						
					}});
			    imggoogleplus.setOnClickListener(new OnClickListener()
			    {
					@Override
					public void onClick(View arg0)
					{
						t=0;
						Intent intent=new Intent(VideoDetailsActivity.this,Share_Googleplus_Activity.class);
						intent.putExtra("share_msg",sharemsg);
						startActivity(intent);	
						
						sharedialod.dismiss();
					}});
			    
			    closebtn.setOnClickListener(new OnClickListener()
			    {
					@Override
					public void onClick(View v)
					{
						sharedialod.dismiss();
						
					}
				});
		}
	
		public void addfriend(View v)
		{
			List<BasicNameValuePair> params1 = new ArrayList<BasicNameValuePair>();
			params1.add(new BasicNameValuePair("operation","addFriend"));
			params1.add(new BasicNameValuePair("Friend_Id",item.Media_Owner_Id));   //0_w1dwjv7h"));
			params1.add(new BasicNameValuePair("User_Id",userID));
			params1.add(new BasicNameValuePair("Secret_Key",secretKey));
			//params1.add(new BasicNameValuePair("pageSize","4"));
			//params1.add(new BasicNameValuePair("pageIndex",""+scrollecount));
			new AsyncHandler(VideoDetailsActivity.this, Constants.SERVER_URL+
					"user.php?",ADD_FRIEND, false, params1).execute();	
			
			// 	http://10.0.0.201:8888/BrainSlam_API/user.php?operation=addFriend&User_Id=1&Friend_Id=17&
			//	Secret_Key=2ca45896e7ed01b20cbbcb96e37460e4
		}
		public void onClickFeaturedVideo(View v)
		{
			
		}
		public void Addtoplaylist(View v)
		{
			Toast.makeText(this,"work in progress",2000).show();	
		}
		public void userstream(View v)
		{
			Toast.makeText(this,"work in progress",2000).show();	
			
			/*if(videoView.isPlaying())
			{
				//videoView.pause();
				videoView.stopPlayback();
				playerPause.setVisibility(View.VISIBLE);
				videoView.setVisibility(View.GONE);
				imgloader.DisplayImage(item.Thumbnail_Url, video_image);
				
			}
			t=0;
			Intent intent = new Intent(VideoDetailsActivity.this,UserPlaylistActivity.class);
			
			Log.i("iiiddd===>>>",""+item.Media_Owner_Id);
			intent.putExtra("Media_Owner_Id", item.Media_Owner_Id);
			startActivity(intent);*/
		}
		
		
		private void rotateAnimation(View view, float rotateFrom, float rotateTo) 
		{
			Animation rotateAnim = new RotateAnimation(rotateFrom, rotateTo,view.getMeasuredWidth() / 2, view.getMeasuredHeight() / 2);
			System.out.println("Inside Roate Anim");
			
			rotateAnim.setDuration(1000); // duration in ms
			rotateAnim.setRepeatCount(0); // -1 = infinite repeated
			rotateAnim.setFillAfter(true); // keep rotation after animation

			view.startAnimation(rotateAnim);
		}

@Override
public void onBackPressed()
{
	
//	startActivity(new Intent(VideoDetailsActivity.this,BrainSlamMainActivity.class));
	super.onBackPressed();
}
    
		

	}