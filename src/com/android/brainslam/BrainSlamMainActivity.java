package com.android.brainslam;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.brainslam.NetworkHandler.AsyncCallBack;
import com.android.brainslam.NetworkHandler.AsyncHandler;
import com.android.brainslam.NetworkHandler.HttpCommunicator;
import com.android.brainslam.camera.NewcaptureActivty;
import com.android.brainslam.constants.Constants;
import com.android.brainslam.db.dao.Streams;
import com.android.brainslam.mainscreen.BrainSlamToggleMenu;
import com.android.brainslam.manager.HomeStreamManager;
import com.android.brainslam.twitterconnect.AlertDialogManager;
import com.android.brainslam.twitterconnect.ConnectionDetector;
import com.android.brainslam.vo.Category;
import com.android.brainslam.vo.FeaturedMediaList;
import com.android.brainslam.vo.FeaturedMediaListData;
import com.android.brainslam.vo.HomeStream;
import com.android.brainslam.vo.StreamItemDetails;
import com.android.utils.CustomAlertDialog;
import com.android.utils.ImageLoader;
import com.android.utils.Utils;
import com.external_api.android_wheel.adapters.ArrayWheelAdapter;
import com.external_api.android_wheel.widget.OnWheelChangedListener;
import com.external_api.android_wheel.widget.WheelView;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class BrainSlamMainActivity extends Activity implements AsyncCallBack, OnTouchListener
{
	//	KalturaClient client;
	//	BrainSlamMainActivityAsycTask activityAsycTask;
	boolean notfirstcategory=false;
	int category=0;
	LinearLayout layout;
	List<Category> categoryList ;
	private ImageLoader imageLoader; 
	public static final int FEATURED_MEDIA_LIST = 0;
	public static final int USER_HOME_STREAM = 1;
	public static final int MEDIA_LIST_CATEGORIES = 2;
	public static final int LAST_MEDIA_LIST_CATEGORY = 3;
	public static final int UPDATE_SINGLE_HOME_STREAM_ITEM = 4;

	// Twitter
	private static Twitter twitter;
	public static RequestToken requestToken;

	// Shared Preferences
	//public static SharedPreferences mSharedPreferences;

	String status = "";

	// Internet Connection detector
	private ConnectionDetector cd;
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();
	ProgressDialog pDialog;
	private UiLifecycleHelper uiHelper;

	static List<StreamItemDetails> featuredMediaList = new ArrayList<StreamItemDetails>();
	String secretKey, userID;

	ScrollView mainScrollView;
	Button btnSearch, btnFilter, btnPlayList;
	//	WheelView wvSortStream, wvSortByDate;
	WheelView sortByDateWheelView, sortByStreamWheelView;
	RelativeLayout sortLayout;
	Button sortButton;
	int filterStreamSelectedIndex = -1, filterDateSelectedIndex = -1;
	//	FrameLayout rlSearch;
	ProgressBar progress;
	TextView tvPullToRefresh, tvLastUpdatedOn;
	LinearLayout llPull2RefreshView, llFeaturedVideos;
	Button playListButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		Log.d("BSMA on resume", "****in onCreate()");

		setContentView(R.layout.activity_main);
		layout = (LinearLayout) findViewById(R.id.gridview_main_layout);
		mainScrollView = (ScrollView) findViewById(R.id.gridview_main_scroll_layout);
		mainScrollView.setOnTouchListener(this);

		progress = (ProgressBar) findViewById(R.id.pb_pull2refresh);
		btnSearch = (Button) findViewById(R.id.btn_search);
		sortLayout = (RelativeLayout) findViewById(R.id.sort_layout);
		btnPlayList = (Button) findViewById(R.id.buttonplailist);

		sortByDateWheelView = (WheelView)findViewById(R.id.sort_by_date_wheel);
		sortByStreamWheelView = (WheelView)findViewById(R.id.sort_by_stream_wheel);
		sortButton = (Button) findViewById(R.id.button_sort);
		filterStreamSelectedIndex = -1;
		filterDateSelectedIndex = -1;
		sortButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sortLayout.setVisibility(View.GONE);	
				Toast.makeText(BrainSlamMainActivity.this, "work in progress", Toast.LENGTH_LONG).show();
			}
		});
 

		String[] arrayDateWheel = {"Today","This Week","This Month","This Year"};
		String[] arrayStramWheel = {"New","Popular","Most Viewes","Most Rated", "IQ - High To Low","IQ - Low To High"};


		final ArrayWheelAdapter<String> dateAdapter = new ArrayWheelAdapter<String>(BrainSlamMainActivity.this, arrayDateWheel);
		sortByDateWheelView.setViewAdapter(dateAdapter);
		sortByDateWheelView.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				filterDateSelectedIndex = newValue;
				Log.v("WheelView", "wheel..Date selected index ::"+filterDateSelectedIndex);
				dateAdapter.setTextColor(getResources().getColor(R.color.orange_kaltura));
				wheel.setViewAdapter(dateAdapter);
			}
		});

		ArrayWheelAdapter<String> streamAdapter =new ArrayWheelAdapter<String>(BrainSlamMainActivity.this, arrayStramWheel); 

		sortByStreamWheelView.setViewAdapter(streamAdapter);
		sortByStreamWheelView.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub

				filterStreamSelectedIndex = newValue;
				Log.v("WheelView", "wheel.. stream selected index. :: "+filterStreamSelectedIndex);
			}
		});

		//		wvSortStream = (WheelView) findViewById(R.id.btn_search);
		//		wvSortByDate = (WheelView) findViewById(R.id.btn_search);


		//		String [] streamSortArr= {"Popular", "Most Viewed", "Most Rated", "IQ - High to Low", "IQ - Low to High"};
		//		ArrayWheelAdapter wheelAdapterStream =
		//			new ArrayWheelAdapter(BrainSlamMainActivity.this , streamSortArr);
		//		
		//		wvSortStream.setAdapter(wheelAdapterStream);

		tvPullToRefresh = (TextView) findViewById(R.id.tv_pull2refesh);
		tvLastUpdatedOn = (TextView) findViewById(R.id.tv_last_updated_on);
		llPull2RefreshView = (LinearLayout) findViewById(R.id.ll_pull_to_refresh_view);
		llFeaturedVideos = (LinearLayout) findViewById(R.id.ll_featured_videos_list_view);
		
		//		wvSortStream = (WheelView) findViewById(R.id.btn_search);
		//		wvSortByDate = (WheelView) findViewById(R.id.btn_search);


		//		String [] streamSortArr= {"Popular", "Most Viewed", "Most Rated", "IQ - High to Low", "IQ - Low to High"};
		//		ArrayWheelAdapter wheelAdapterStream =
		//			new ArrayWheelAdapter(BrainSlamMainActivity.this , streamSortArr);
		//		
		//		wvSortStream.setAdapter(wheelAdapterStream);

		/*rlSearch = (FrameLayout) findViewById(R.id.rl_search_layout);

		Button btnSearchResult = (Button) findViewById(R.id.btn_done);
		final EditText etSearch = (EditText) findViewById(R.id.et_search);

		btnSearchResult.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(BrainSlamMainActivity.this, MainStreamSearchResults.class);
				intent.putExtra("searchString", etSearch.getText().toString());
				startActivity(intent);
			}
		});*/

		//		mainScrollView.on

		//		featuredMediaHorizontalLV = new HorizontalListView(this, null);

		//		featuredMediaHorizontalLV.setAdapter(fListAdapter);

		//		final String ks=getIntent().getExtras().getString("ks");
		//		
		//		KalturaConfiguration config1 = new KalturaConfiguration();
		//		config1.setEndpoint("http://cdnbakmi.kaltura.com");
		//		
		//		client = new KalturaClient(config1);
		//		client.setSessionId(ks);
		imageLoader = new ImageLoader(this.getApplicationContext());

		//		Gson gson = new  GsonBuilder().create();
		//		categoryList = gson.fromJson(
		//				getIntent().getExtras().getString("categorylist"),
		//				new TypeToken<List<Category>>() {
		//				}.getType());

		//		activityAsycTask=new BrainSlamMainActivityAsycTask();
		//		activityAsycTask.execute("0");


		btnSearch.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				startActivity(new Intent(BrainSlamMainActivity.this, MainStreamSearchResults.class));
			} 
		});

		btnPlayList.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Toast.makeText(BrainSlamMainActivity.this, "work in progress", Toast.LENGTH_LONG).show();
			} 
		});

		
		uiHelper = new UiLifecycleHelper(BrainSlamMainActivity.this, callback);
		uiHelper.onCreate(savedInstanceState);

		btnFilter = (Button) findViewById(R.id.filter_button);
		btnFilter.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Toast.makeText(BrainSlamMainActivity.this, "work in progress", Toast.LENGTH_LONG).show();
				//				sortLayout.setVisibility(View.VISIBLE);
				//				 
				//
				//				new Handler().postDelayed(new Runnable() {
				//
				//					@Override
				//					public void run() {
				//						// TODO Auto-generated method stub
				//						final Animation  bottomUp = AnimationUtils.loadAnimation(BrainSlamMainActivity.this,R.anim.in_from_bottom);
				//						sortLayout.startAnimation(bottomUp);
				//					}
				//				}, 200);

			}
		});


		playListButton = (Button)findViewById(R.id.buttonplailist);
		
		playListButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(BrainSlamMainActivity.this, "work in progress", Toast.LENGTH_LONG).show();
			}
		});
		
		
		secretKey = Utils.getDataString(BrainSlamMainActivity.this,
				Constants.PREFS_NAME, Constants.SP_SECRET_KEY);
		userID = Utils.getDataString(BrainSlamMainActivity.this,
				Constants.PREFS_NAME, Constants.SP_USER_ID);

		// Fetch Featured Media list
		//		fetchUserHomeStream();

		fetchFeaturedMediaList();
	}


	boolean refreshing;
	float startY = 0, lastY = 0;
	boolean isStartY = false;
	@Override
	public boolean onTouch(View v, MotionEvent event) {

		int dragLength = (progress.getMeasuredHeight() * 2);

		//		if (mainScrollView.getScrollY() == 0) 
		//		{
		switch (event.getAction()) 
		{
		//			case MotionEvent.ACTION_DOWN:
		//
		//				Log.i("BSMA pull to refresh", "ACTION_DOWN");
		////				startY = event.getY();
		//				lastY = startY;
		//				break;

		case MotionEvent.ACTION_MOVE:

			if(!isStartY)
			{
				isStartY = true;
				startY = event.getY();
			}

			Log.i("BSMA pull to refresh", "mainScrollView.getScrollY()::" + mainScrollView.getScrollY());
			
			if (!refreshing && event.getY() > lastY && mainScrollView.getScrollY() == 0) 
			{
				lastY = event.getY();

				llPull2RefreshView.setVisibility(View.VISIBLE);

				tvPullToRefresh.setVisibility(View.VISIBLE);
				tvLastUpdatedOn.setVisibility(View.VISIBLE);

				if(lastUpdatedOn.length() > 0)
					tvLastUpdatedOn.setText("Last updated on "+lastUpdatedOn);
				else
					tvLastUpdatedOn.setVisibility(View.GONE);

				Log.i("BSMA pull to refresh", "lastY::" + lastY+" startY::"+startY+" getMeasuredHeight::"+progress.getMeasuredHeight());
				Log.i("BSMA pull to refresh", "action move " + event.getY());

				//					LinearLayout.LayoutParams svParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
				//							LayoutParams.WRAP_CONTENT);
				//					svParams.topMargin = (int) (event.getY() - startY);
				//
				//					mainScrollView.setLayoutParams(svParams);


				if (event.getY() - startY <= dragLength) {
					//						double percent = 1 - (event.getY() - startY) / dragLength;
					//						double weight;
					//						weight = 2 * Math.pow(percent, 0.8);
					//						LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) progress.getLayoutParams();
					//						params.weight = (float) weight;
					//						progress.setLayoutParams(params);
					//						progress.setIndeterminate(false);
					//						progress.setPadding(0, 0, 0, 0);
					return true;
				} else {
					lastUpdatedOn = getCurrenttimeStamp();
					progress.setVisibility(View.VISIBLE);
					refreshing = true;

					pullToRefresh();

					//						startY = 100000f;
					//						LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) progress.getLayoutParams();
					//						params.weight = 0;
					//						progress.setIndeterminate(true);
					//						progress.postInvalidate();
					//						progress.setLayoutParams(params);
				}
			}
			break;

		case MotionEvent.ACTION_UP:
			isStartY = false;
			if (event.getY() - startY <= dragLength) 
			{
				//					startY = 100000f;
				Log.i("BSMA pull to refresh", "action up " + event.getY());

				llPull2RefreshView.setVisibility(View.GONE);
				tvPullToRefresh.setVisibility(View.GONE);
				tvLastUpdatedOn.setVisibility(View.GONE);
				progress.setVisibility(View.GONE);

				refreshing = false;
				//					LinearLayout.LayoutParams svParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
				//							LayoutParams.WRAP_CONTENT);
				//
				//					svParams.topMargin = 0;
				//
				//					mainScrollView.setLayoutParams(svParams);


				//					if (!refreshing) {
				//						LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) progress.getLayoutParams();
				//						params.weight = 2;
				//						progress.setLayoutParams(params);
				//					}
			}
			break;
			//			}
		}
		return false;
	}

	private String getCurrenttimeStamp()
	{
		return java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
	}

	private void pullToRefresh()
	{
		fetchFeaturedMediaList();
		//		fetchUserHomeStream();
		List<Streams> list = HomeStreamManager.getInstance(getApplicationContext()).getHomeStream();
		getHomeStreamMediaList(list);
	}


	private String onSessionStateChange(Session session, SessionState state, Exception exception) {

		if (state.isOpened()) 
		{
			Log.i("", "Logged in...");
			status= "loggedin";
		} else if (state.isClosed()) 
		{
			Log.i("", "Logged out...");
			status= "loggedout";
		}
		return status;
	}

	@Override
	protected void onResume() 
	{
		super.onResume();
		Log.d("BSMA on resume", "****in onResume()");
		//		if(featuredMediaList.size() > 0)
		//			featuredMediaHorizontalLV.setAdapter(new FeaturedListAdapter(
		//					BrainSlamMainActivity.this, featuredMediaList));
		
		fetchUserHomeStream();

		// check streams db
		List<Streams> streamsDb = HomeStreamManager.getInstance(
				this.getApplicationContext()).getHomeStream();

		if(streamsDb == null || streamsDb.size() == 0)
		{
			Log.d("BSMA on resume", "****streams size zero..");

			layout.removeAllViews();

			getAddMoreStream();
			layout.addView(streamView);
		}
		else
		{
			int editStream = isStreamEdited(streamsDb);

			if(editStream == -1)
			{
				Log.d("BSMA on resume", "****new streams added");
				pullToRefresh();
			}
			else if(editStream == 1)
			{
				Log.d("BSMA on resume", "****streams deleted");
				sortList();
				removeDeletedStreams(streamsDb);
				displayCategoryList(false);
			}
			else
			{
				Log.d("BSMA on resume", "****sort list");
				// check if sequence is changed
				sortList();
				displayCategoryList(false);
			}
		}
	}

	private void removeDeletedStreams(List<Streams> streamsDb)
	{
		Log.d("BSMA on resume", "in removeDeletedStreams()");

		ArrayList<Streams> streams = new ArrayList<Streams>(); 

		for (int i = 0; i < streamsDb.size(); i++) 
		{
			if(streamsDb.get(i).Count > 0)
			{
				streams.add(streamsDb.get(i));
			}
		}


		for (int i = 0; i < homeStreamCategoriesList.size(); i++) 
		{
			if (!HomeStreamManager
					.getInstance(BrainSlamMainActivity.this)
					.getStreamName(homeStreamCategoriesList.get(i).get(0).HomeItemId)
					.equalsIgnoreCase(streams.get(i).HomeItemsName))
			{
				homeStreamCategoriesList.remove(i);
				--i;
			}
		}
	}

	private int isStreamEdited(List<Streams> streamsDb)
	{
		int cnt = 0;
		for (int i = 0; i < streamsDb.size(); i++) 
		{
			if(streamsDb.get(i).Count > 0)
			{
				cnt ++;
			}
		}

		Log.d("BSMA on resume", "****streamsDb size::"+streamsDb.size()+" homeStreamCategoriesList size::"+homeStreamCategoriesList.size()+"  cnt::"+cnt);
		

		if(homeStreamCategoriesList.size() < cnt) // update and add to stream 
			return -1;
		else if(homeStreamCategoriesList.size() == cnt)
			return 0;
		else if(homeStreamCategoriesList.size() > cnt) // delete from streams
			return 1;

		return 99;
	}


	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	public void toggleMenu(View V)
	{
		Log.e("In", "Toggle");
		Intent toggleIntent  = new Intent(BrainSlamMainActivity.this,BrainSlamToggleMenu.class);
		this.startActivity(toggleIntent);
		BrainSlamMainActivity.this.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
	}


	public void starRecorder(View view)
	{
		System.out.println("start recording");
		Intent recorderIntent  = new Intent(BrainSlamMainActivity.this,NewcaptureActivty.class);
		
		/*boolean isBackFacing = true;
	    
	      CameraInfo info = new CameraInfo();
	      Camera.getCameraInfo(0, info);
	      if (info.facing == CameraInfo.CAMERA_FACING_BACK) 
	      {
	    		Toast.makeText(BrainSlamMainActivity.this, "DEFAULT BACK FACING "+isBackFacing, Toast.LENGTH_LONG).show();			
	      }	  
	     else
	     {
	  		  isBackFacing = false;
		  Toast.makeText(BrainSlamMainActivity.this, "DEFAULT FRONT FACING "+isBackFacing, Toast.LENGTH_LONG).show();
	     }    */ 
		
		System.out.println("No of cameras::"+Camera.getNumberOfCameras());
		
	    recorderIntent.putExtra("noOfCamera", Camera.getNumberOfCameras());
        
      	
		startActivity(recorderIntent);
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);


	}
	private void fetchUserHomeStream()
	{
		new Handler().postDelayed(new Runnable() 
		{
			@Override
			public void run() 
			{
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

				params.add(new BasicNameValuePair("operation", "userGetHomeStream"));
				params.add(new BasicNameValuePair("User_Id", userID));
				params.add(new BasicNameValuePair("Secret_Key", secretKey));

				Log.d("BSMA", "http://dev-kaltura.brain-slam.com/BrainSlam_API/user.php?"+Utils.printUrl(params));

				new AsyncHandler(BrainSlamMainActivity.this, Constants.SERVER_URL
						+ "user.php?",
						USER_HOME_STREAM, false, params).execute();			
			}
		}, 50);
	}

	private void fetchFeaturedMediaList()
	{
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

		params.add(new BasicNameValuePair("operation", "getFeaturedMediaList"));
		params.add(new BasicNameValuePair("Secret_Key", secretKey));
		params.add(new BasicNameValuePair("User_Id", userID));
		params.add(new BasicNameValuePair("pageSize", 5+""));
		params.add(new BasicNameValuePair("pageIndex", 1+""));

		Log.d("BSMA", "http://dev-kaltura.brain-slam.com/BrainSlam_API/media.php?"+Utils.printUrl(params));

		new MainStreamAsyncHandler(BrainSlamMainActivity.this, Constants.SERVER_URL
				+ "media.php?",
				FEATURED_MEDIA_LIST, "", false, params).execute();
	}


	private void animate(final View view, final int animId, final View viewToRemove, final View viewToShow)
	{
		Animation animation = AnimationUtils.loadAnimation(BrainSlamMainActivity.this, animId);
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


	private void userUpdateHomeStream(String type, String itemId)
	{
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

		params.add(new BasicNameValuePair("operation", "updateSingleHomeStreamItem"));
		params.add(new BasicNameValuePair("Secret_Key", secretKey));
		params.add(new BasicNameValuePair("User_Id", userID));
		params.add(new BasicNameValuePair("Action", "delete"));
		params.add(new BasicNameValuePair("Type", type));
		params.add(new BasicNameValuePair("Home_Item_ID", itemId));

		new AsyncHandler(BrainSlamMainActivity.this, Constants.SERVER_URL
				+ "user.php?", UPDATE_SINGLE_HOME_STREAM_ITEM, false, params).execute();
	}


	private void openShareDialog()
	{
		final String sharemsg = "Hi Android,What's up...!";

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


			/*	startActivity(new Intent(BrainSlamMainActivity.this,ComposeNewMessageActivity.class));
				sharedialod.dismiss();*/

				TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			    int simState = telMgr.getSimState();
			            switch (simState) {
			                case TelephonyManager.SIM_STATE_ABSENT:
			                  Toast.makeText(BrainSlamMainActivity.this,"No SIM Card Inserted",Toast.LENGTH_LONG).show();
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
			                	
//								startActivity(new Intent(BrainSlamMainActivity.this,Message_Activity.class));
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
				if (!Utils.checkeInternetConnection(BrainSlamMainActivity.this))
				{
					Log.e("network error","No Internet Connection");
					new CustomAlertDialog(BrainSlamMainActivity.this,"No Internet Connection").showDialog();
				}else
				{
					shareViaFacebook();	
				}

			}});

		imgtwitter.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) {

				if (!Utils.checkeInternetConnection(BrainSlamMainActivity.this))
				{
					Log.e("network error","No Internet Connection");
					new CustomAlertDialog(BrainSlamMainActivity.this,"No Internet Connection").showDialog();
				}else
				{
				loginToTwitter();
				Log.w("login", "success");
				if (isTwitterLoggedInAlready())
				{
					Intent statusUpdateActivity = new
							Intent(BrainSlamMainActivity.this,UpdateTwitterStatusActivity.class);

					statusUpdateActivity.putExtra("share_msg",sharemsg);

					startActivity(statusUpdateActivity);
					sharedialod.dismiss();
				}

				}
			}});

		imggoogleplus.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{

				Intent intent=new Intent(BrainSlamMainActivity.this,Share_Googleplus_Activity.class);
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
	
	private void displayFeaturedVideos(List<StreamItemDetails> featuredVideos)
	{
		Log.d("BSMA", "displayFeaturedVideos featuredVideos size::"
				+ featuredVideos.size());
		
		llFeaturedVideos.removeAllViews();
		for (int i = 0; i < featuredVideos.size(); i++) 
		{
			llFeaturedVideos.addView(new VideoThumnailView(BrainSlamMainActivity.this,
					featuredVideos.get(i), R.layout.featured_video_grid));
		}
	}
	

	View streamView;
	String catToDelete = "";
	private void displayCategoryList(boolean isUpdate)
	{
		Log.d("BSMA", "displayCategoryList homeStreamCategoriesList size::"
				+ homeStreamCategoriesList.size());
		
		layout.removeAllViews();

		for (int i = 0; i < homeStreamCategoriesList.size(); i++) 
		{
			//						if(homeStreamList.get(i).Count > 0)
			{ 
				LayoutInflater inflater = (LayoutInflater) BrainSlamMainActivity.this
						.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				final View categoryView = inflater.inflate(R.layout.text, null);
				// layout.addView();

				final LinearLayout catMenuExpand = (LinearLayout) categoryView
						.findViewById(R.id.ll_cat_menu_expand);

				final LinearLayout catDefaultRow = (LinearLayout) categoryView
						.findViewById(R.id.category_row);

				final LinearLayout catEditMenu = (LinearLayout) categoryView
						.findViewById(R.id.category_menu);

				final ImageView ivRightArrow = (ImageView) categoryView
						.findViewById(R.id.right_arrow);

				final ImageView ivCatShareBtn = (ImageView) categoryView
						.findViewById(R.id.img_view_share);

				final ImageView ivCatDeleteBtn = (ImageView) categoryView
						.findViewById(R.id.img_view_delete);

				final ToggleButton toggleNotifications = (ToggleButton) categoryView
						.findViewById(R.id.toggle_notifications);

				toggleNotifications.setOnCheckedChangeListener(new OnCheckedChangeListener()
				{
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						Log.d("BSMA", "Notification toggle ::"+isChecked);

					}
				});

				ivCatShareBtn.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{
						Log.d("BSMA", "Clicked category share button");
						openShareDialog();
					}
				});

//						if(homeStreamCategoriesList.get(i).get(0).Searched_Category!= null)
							catToDelete = HomeStreamManager.getInstance
											(BrainSlamMainActivity.this).getStreamName
												(homeStreamCategoriesList.get(i).get(0).HomeItemId);
						
				ivCatDeleteBtn.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{
						Log.d("BSMA", "Clicked category delete button");
						int index = layout.indexOfChild(categoryView);
						layout.removeView(categoryView);
						layout.removeViewAt(index);

						Log.d("BSMA","homeStreamList size::" + homeStreamList.size()
								+ " catToDelete::"+ catToDelete);

						for (int j = 0; j < homeStreamList.size(); j++) 
						{
							if (homeStreamList.get(j).Home_Items_Name != null
									&& homeStreamList.get(j).Home_Items_Name
									.equalsIgnoreCase(catToDelete))
							{
								Log.d("BSMA", "delete item name::"+homeStreamList.get(j).Home_Items_Name);

								HomeStreamManager.getInstance(getApplicationContext()).
								removeStreamByItemId(Integer.parseInt(homeStreamList.get(j).Home_Item_ID));

								userUpdateHomeStream(homeStreamList.get(j).Home_Item_Type, 
										homeStreamList.get(j).Home_Item_ID);
								break;
							}
						}
					}
				});


				// collapse category menu
				ivRightArrow.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{
						Log.d("BSMA", "Clicked category collapse button");
						animate(catEditMenu, R.anim.out_to_right, catEditMenu, catDefaultRow);
					}
				});


				// expand menu to show category edit options
				catMenuExpand.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{
						Log.d("BSMA", "Clicked category expand button");
						catEditMenu.setVisibility(View.VISIBLE);
						animate(catEditMenu, R.anim.in_from_right, catDefaultRow, catEditMenu);
					}
				});


				final TextView categoryText = (TextView) categoryView
						.findViewById(R.id.video_category_name);
				
					categoryText.setText(HomeStreamManager.getInstance(
							BrainSlamMainActivity.this).getStreamName(
							homeStreamCategoriesList.get(i).get(0).HomeItemId));

				layout.addView(categoryView);
				layout.addView(new CategoryScrollView(BrainSlamMainActivity.this,
						homeStreamCategoriesList.get(i)));
			}
		}


		//		LayoutInflater moreStreamInflater = (LayoutInflater) BrainSlamMainActivity.this
		//		.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		//		streamView = moreStreamInflater.inflate(R.layout.add_more_to_stream_row, null);

		getAddMoreStream();

		//		layout.removeView(streamView);
		layout.addView(streamView);

		final Button addToStream = (Button) streamView
				.findViewById(R.id.btn_add_more_to_stream);

		addToStream.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				startActivity(new Intent(BrainSlamMainActivity.this, LandingActivity.class));
			}
		});

		disablePullToRefreshView();
	}

	String lastUpdatedOn = "";
	private void disablePullToRefreshView()
	{
		llPull2RefreshView.setVisibility(View.GONE);
		tvPullToRefresh.setVisibility(View.GONE);
		tvLastUpdatedOn.setVisibility(View.GONE);
		progress.setVisibility(View.GONE);

		refreshing = false;
		//		LinearLayout.LayoutParams svParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
		//				LayoutParams.WRAP_CONTENT);
		//
		//		svParams.topMargin = 0;
		//
		//		mainScrollView.setLayoutParams(svParams);
	}

	private void getAddMoreStream()
	{
		LayoutInflater moreStreamInflater = (LayoutInflater) BrainSlamMainActivity.this
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		streamView = moreStreamInflater.inflate(R.layout.add_more_to_stream_row, null);
	} 

	public void CallVideoActivty(String url)
	{
		/*Intent intent=new Intent(this, BrainSlamVideoPlayer.class);
		intent.putExtra("videourl", url);
		startActivity(intent);*/
	}

	public class CategoryScrollView extends HorizontalScrollView 
	{
		TableRow row1,row2;
		boolean mIsFling;
		ArrayList<StreamItemDetails> mediaList;

		public CategoryScrollView(Context context, ArrayList<StreamItemDetails> result) {
			super(BrainSlamMainActivity.this);
			this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

			mediaList = result;

			LayoutInflater inflater = (LayoutInflater) BrainSlamMainActivity.this
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			this.addView(inflater.inflate(R.layout.video_category_grid, null));
			row1 = (TableRow) this.findViewById(R.id.row1);
			row2 = (TableRow) this.findViewById(R.id.row2);

			for (int i=0;i<result.size();i++) {
				if(i%2==1)
					row1.addView(new VideoThumnailView(context, result.get(i), R.layout.video_grid));
				else
					row2.addView(new VideoThumnailView(context, result.get(i), R.layout.video_grid));
			}
		}


		@SuppressLint("NewApi")
		@Override
		protected void onOverScrolled(int scrollX, int scrollY,
				boolean clampedX, boolean clampedY) 
		{
			super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);

			if(clampedX && scrollX > 10)
			{
				Log.d("onScrollChanged", "************onOverScrolled X");

				
				String catName = HomeStreamManager.getInstance(
						BrainSlamMainActivity.this).getStreamName(
						mediaList.get(0).HomeItemId);
						
						
				Log.d("onScrollChanged", " category to check for pagination::"+catName);


				for (int i = 0; i < homeStreamList.size(); i++) 
				{
					Log.d("onScrollChanged", "homeStreamList Home_Items_Name::"+homeStreamList.get(i).Home_Items_Name);

					if(homeStreamList.get(i).Home_Items_Name != null &&
							homeStreamList.get(i).Home_Items_Name.equalsIgnoreCase(catName))
					{
						int totalCount = homeStreamList.get(i).Count;
						int downloadedCount = mediaList.size();

						Log.d("onScrollChanged", " totalCount::"+totalCount+" downloadedCount::"+downloadedCount);

						if(downloadedCount < totalCount)
						{
							// call pagination
							/**
							pageSize = 5
							pageIndex = (downloadedCount / pageSize) + 1
							 */

							int pageIndex = (downloadedCount / Constants.PAGE_SIZE) + 1;

							getMediaByCategory(homeStreamList.get(i).Home_Item_ID, 10, 10, pageIndex,false);
						}
					}
				}

			}
		}


		@Override
		public void fling(int velocityY) 
		{
			super.fling(velocityY);
			mIsFling = true;
			Log.d("onScrollChanged", "Flinf");
		}



		//		@Override
		//		protected void onScrollChanged(int x, int y, int oldX, int oldY) 
		//		{
		//			super.onScrollChanged(x, y, oldX, oldY);
		//			if (mIsFling) 
		//			{
		//				Log.d("onScrollChanged", "getMeasuredWidth::"+getMeasuredWidth()+
		//						" getMaxScrollAmount::"+getMaxScrollAmount()+" scrollX::"+
		//						getScrollX()+" Width::"+getWidth());
		//				
		//				if (Math.abs(x - oldX) < 2 || x >= getMeasuredWidth() || x == 0) 
		//				{
		//					Log.d("onScrollChanged", "End of scroll");
		//					mIsFling = false;
		//				}
		//				
		//				if(x >= getMeasuredWidth())
		//				{
		//					Log.d("onScrollChanged", "End of scroll start pagination");
		//				}
		//			}
		//		}
	}

	public class VideoThumnailView extends LinearLayout implements OnClickListener{

		StreamItemDetails item;
		public VideoThumnailView(Context context, StreamItemDetails item, int layoutId) 
		{
			super(BrainSlamMainActivity.this);

			if(item != null)
			{
				this.item = item;
				this.setOnClickListener(this);
				LayoutInflater inflater=(LayoutInflater)BrainSlamMainActivity.this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				View view=inflater.inflate(layoutId, null);

				TextView videoTitle=(TextView)view.findViewById(R.id.video_name);
				videoTitle.setText(item.Name);
				TextView username=(TextView)view.findViewById(R.id.video_username);
				username.setText(item.Media_Owner);
				TextView IQScore=(TextView)view.findViewById(R.id.video_IQ_score_value);
				IQScore.setText(""+item.Average_IQ);
				RatingBar videoRating =(RatingBar)view.findViewById(R.id.video_rating_bar);
				videoRating.setRating(Utils.getRating(item.Average_IQ));

				this.addView(view);

				imageLoader.DisplayImage(item.Thumbnail_Url, (ImageView)this.findViewById(R.id.thumbnail));
				//			imageLoader.DisplayImage(item.Download_Url, (ImageView)this.findViewById(R.id.video_username_icon));
			}
		}

		@Override
		public void onClick(View v) 
		{
			//			imageLoader.clearCache();

			if (!Utils.checkeInternetConnection(BrainSlamMainActivity.this))
			{
				Log.e("network error","No Internet Connection");
				new CustomAlertDialog(BrainSlamMainActivity.this,"No Internet Connection").showDialog();
			}else
			{
				
				Intent intent=new Intent(BrainSlamMainActivity.this,VideoDetailsActivity.class);
				intent.putExtra("mediaObj",item);
				startActivity(intent);	
	
				Log.d("rutuja VideoThumnailView on click", "Item clicked::"+item.Media_Id);
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//		activityAsycTask.cancel(true);
		if(null != sortLayout && sortLayout.getVisibility() == View.VISIBLE)
			sortLayout.setVisibility(View.GONE);
		else
		{
			imageLoader.clearCache();
			super.onBackPressed();
		}
	}

	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
	}


	String catName = "";
	private void getHomeStreamMediaList(List<Streams> homeStreamList)
	{
		homeStreamCategoriesList.clear();

		if(homeStreamList != null)
		{
			for (int i = 0; i < homeStreamList.size(); i++) 
			{
				if(homeStreamList.get(i).Count > 0)
				{
					Log.d("BSMA getHomeStreamMediaList", "*****fetching homeStreamList Category::"+homeStreamList.get(i).HomeItemsName);

					catName = homeStreamList.get(i).HomeItemsName;

					// if item type is category call getMediaListByCategory
					if(homeStreamList.get(i).HomeItemType.equalsIgnoreCase("Category"))
					{
						getMediaByCategory(homeStreamList.get(i).HomeItemID+"", i, homeStreamList.size() -1, 1,false);
					}
					else // call getMediaListByType
					{
						// if item type is Crew
						if(homeStreamList.get(i).HomeItemType.equalsIgnoreCase("Crew"))
						{
							getMediaListByType("Crew", homeStreamList.get(i).HomeItemID, i, homeStreamList.size() -1);
						}

						// if item type is User
						if(homeStreamList.get(i).HomeItemType.equalsIgnoreCase("User"))
						{
							getMediaListByType("User", homeStreamList.get(i).HomeItemID, i, homeStreamList.size() -1);
						}

						// if item type is Play list
						if(homeStreamList.get(i).HomeItemType.equalsIgnoreCase("Playlist"))
						{
							getMediaListByType("Playlist", homeStreamList.get(i).HomeItemID, i, homeStreamList.size() -1);
						}
					}
				}
				else
				{
					Log.d("BSMA getHomeStreamMediaList",
							"********getHomeStreamMediaList last category, display list"
									+ homeStreamList.get(i).Count
									+ " home item name::"
									+ homeStreamList.get(i).HomeItemsName);
					Log.d("BSMA getHomeStreamMediaList", "i::"+i+" homeStreamList.size() - 1::"+(homeStreamList.size() - 1));

					if(i == (homeStreamList.size() - 1))
					{
						new Handler().postDelayed(new Runnable() 
						{
							@Override
							public void run() 
							{
								sortList();

								displayCategoryList((isCatUpdateApi != -1) ? true : false);
							}
						}, (7000));
					}
				}
			}
		}
	}




	private void getMediaByCategory(final String categoryId, final int delay,
			final int lastIndex, final int pageIndex,final boolean isToBeFiltered)
	{
		new Handler().postDelayed(new Runnable() 
		{
			@Override
			public void run() 
			{
				// fetch from server
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

				params.add(new BasicNameValuePair("operation", "getMediaListByCategories"));
				params.add(new BasicNameValuePair("category_Ids", "["+categoryId+"]"));
				params.add(new BasicNameValuePair("User_Id", userID));
				params.add(new BasicNameValuePair("Secret_Key", secretKey));
				params.add(new BasicNameValuePair("pageSize", Constants.PAGE_SIZE+""));
				params.add(new BasicNameValuePair("pageIndex", pageIndex+""));

				if(isToBeFiltered)
				{
					if(filterStreamSelectedIndex != -1)
						params.add(new BasicNameValuePair("filter1", ""+filterStreamSelectedIndex));

					if(filterDateSelectedIndex != -1)
						params.add(new BasicNameValuePair("filter2", ""+filterDateSelectedIndex));
				}
				Log.d("BSMA", "http://dev-kaltura.brain-slam.com/BrainSlam_API/media.php?"+Utils.printUrl(params));

				if(delay == lastIndex)
				{
					new MainStreamAsyncHandler(BrainSlamMainActivity.this, Constants.SERVER_URL
							+ "media.php?",
							LAST_MEDIA_LIST_CATEGORY, categoryId, false, params).execute();	
				}
				else
				{
					new MainStreamAsyncHandler(BrainSlamMainActivity.this, Constants.SERVER_URL
							+ "media.php?",
							MEDIA_LIST_CATEGORIES, categoryId, false, params).execute();
				}
			}
		}, (delay * 100));
	}


	private void getMediaListByType(final String listType, final int listTypeId, final int delay, final int lastIndex)
	{
		new Handler().postDelayed(new Runnable() 
		{
			@Override
			public void run() 
			{
				// fetch from server
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

				params.add(new BasicNameValuePair("operation", "getMediaListByType"));
				params.add(new BasicNameValuePair("list_Type", listType));
				params.add(new BasicNameValuePair("list_Type_Id", listTypeId+""));
				params.add(new BasicNameValuePair("User_Id", userID));
				params.add(new BasicNameValuePair("Secret_Key", secretKey));
				//		params.add(new BasicNameValuePair("pageSize", "10"));
				//		params.add(new BasicNameValuePair("pageIndex", "1"));

				Log.d("BSMA", "http://dev-kaltura.brain-slam.com/BrainSlam_API/media.php?"+Utils.printUrl(params));

				if(delay == lastIndex)
				{
					new MainStreamAsyncHandler(BrainSlamMainActivity.this, Constants.SERVER_URL
							+ "media.php?",
							LAST_MEDIA_LIST_CATEGORY, listTypeId+"", false, params).execute();
				}
				else
				{
					new MainStreamAsyncHandler(BrainSlamMainActivity.this, Constants.SERVER_URL
							+ "media.php?",
							MEDIA_LIST_CATEGORIES, listTypeId+"", false, params).execute();
				}

			}
		}, (delay * 50));
	}


	private int isCategoryAvailable(String catName)
	{
		for (int i = 0; i < homeStreamCategoriesList.size(); i++) 
		{
			ArrayList<StreamItemDetails> categoryMedia = homeStreamCategoriesList.get(i);

			for (int j = 0; j < categoryMedia.size(); j++) 
			{
//				if(null != categoryMedia.get(j).Searched_Category)
//				{
					if(HomeStreamManager.getInstance
							(BrainSlamMainActivity.this).getStreamName
							(categoryMedia.get(j).HomeItemId).equalsIgnoreCase(catName))
					{
						return i;
					}
//				}
			}
		}

		return -1;
	}

	int isCatUpdateApi = -1;
	private void parseResponse(JSONObject jsonObj, int id, String streamId)
	{
		Log.d("BSMA", "MEDIA_LIST_CATEGORIES response ::"+jsonObj.toString());
		Gson gson = new Gson();
		try
		{
			JSONObject mediaData = new JSONObject(jsonObj.toString());

			if(mediaData.getString("status").equalsIgnoreCase("0"))
			{
				JSONArray jsonMediaArray = mediaData.getJSONArray("data");

				FeaturedMediaListData mediaDataArr[] = gson.fromJson(jsonMediaArray.toString(), 
						FeaturedMediaListData[].class);

				ArrayList<StreamItemDetails> categoryMediaList = new ArrayList<StreamItemDetails>();

				/** Check if category already exists. If yes pagination api is
				been called, and update the previous element in arraylist.*/

				if(mediaDataArr.length > 0)
					isCatUpdateApi = isCategoryAvailable(HomeStreamManager.getInstance
							(BrainSlamMainActivity.this).getStreamName
							(streamId));
				if(isCatUpdateApi != -1)
				{
					Log.d("BSMA", "Category already available! Update the previous element");

					if(isCatUpdateApi < homeStreamCategoriesList.size())
						categoryMediaList = homeStreamCategoriesList.get(isCatUpdateApi);
					//					homeStreamCategoriesList.remove(isCatUpdateApi);
				}

//				Log.d("LandingAct", "Categories::" + mediaDataArr[0].Categories
//						+ " Desc::" + mediaDataArr[0].Description);

				for (int i = 0; i < mediaDataArr.length; i++) 
				{
					StreamItemDetails stream = new StreamItemDetails();

//					Log.d("LandingAct", "Category ::" + mediaDataArr[i].Searched_Category);

					stream.Name = mediaDataArr[i].Name;
					stream.HomeItemId = streamId;
					stream.Categories = mediaDataArr[i].Categories;
					stream.Average_IQ = mediaDataArr[i].Average_IQ;
					stream.Description = mediaDataArr[i].Description;
					stream.Duration = mediaDataArr[i].Duration;
					stream.Thumbnail_Url = mediaDataArr[i].Thumbnail_Url;
					stream.Download_Url = mediaDataArr[i].Download_Url;
					stream.Media_Id = mediaDataArr[i].Media_Id;
					stream.Media_Owner = mediaDataArr[i].Media_Owner;
					stream.Media_Type = mediaDataArr[i].Media_Type;
					stream.Data_Url = mediaDataArr[i].Data_Url;
					stream.Created_At = mediaDataArr[i].Created_At;
					stream.Updated_At = mediaDataArr[i].Updated_At;
					stream.Views = mediaDataArr[i].Views;
					stream.Width = mediaDataArr[i].Width;
					stream.Height = mediaDataArr[i].Height;
					stream.Tags = mediaDataArr[i].Tags;
					stream.Number_Of_Ratings = mediaDataArr[i].Number_Of_Ratings;
					stream.Tranding_Score = mediaDataArr[i].Tranding_Score;
				//  -->byNaresh
					stream.Media_Owner_Id = mediaDataArr[i].Media_Owner_Id;
					
					categoryMediaList.add(stream);
				}

//				Log.d("BSMA", "Category name ::"+
//						categoryMediaList.get(0).Name);

				if(isCatUpdateApi != -1 && isCatUpdateApi < homeStreamCategoriesList.size())
				{
					Log.d("BSMA", "Updating the previous element");
					homeStreamCategoriesList.set(isCatUpdateApi, categoryMediaList);
				}
				else
				{
					homeStreamCategoriesList.add(categoryMediaList);
				}
			}

			if(id == LAST_MEDIA_LIST_CATEGORY)
			{
				new Handler().postDelayed(new Runnable() 
				{
					@Override
					public void run() 
					{
						sortList();
						lastUpdatedOn = getCurrenttimeStamp();
						
						displayCategoryList((isCatUpdateApi != -1) ? true : false);
					}
				}, (200));
			}

		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(JsonSyntaxException e)
		{
			e.printStackTrace();
		}
	}


	public void sortList()
	{
		// arrange by sequence
		List<Streams> streamsDb = HomeStreamManager.getInstance(
				BrainSlamMainActivity.this.getApplicationContext()).getHomeStream();

		ArrayList<ArrayList<StreamItemDetails>> tempList =
				new ArrayList<ArrayList<StreamItemDetails>>(); //homeStreamCategoriesList;

		for (int j = 0; j < streamsDb.size(); j++) 
		{
			for (int k = 0; k < homeStreamCategoriesList.size(); k++) 
			{
				if(homeStreamCategoriesList.get(k).size() > 0 &&
//						homeStreamCategoriesList.get(k).get(0).Searched_Category != null
						 streamsDb.get(j).HomeItemsName.equalsIgnoreCase
						(HomeStreamManager.getInstance
								(BrainSlamMainActivity.this).getStreamName
								(homeStreamCategoriesList.get(k).get(0).HomeItemId)))
				{
					tempList.add(homeStreamCategoriesList.get(k));
				}
			}
		}

		homeStreamCategoriesList.clear();
		homeStreamCategoriesList = tempList;


//		for (int k = 0; k < homeStreamCategoriesList.size(); k++) 
//		{
//			Log.d("BSMA", "after sorting homeStreamCategoriesList cat name::"+
//					homeStreamCategoriesList.get(k).get(0).Searched_Category );
//		}
	}


	public static ArrayList<ArrayList<StreamItemDetails>> homeStreamCategoriesList = 
			new ArrayList<ArrayList<StreamItemDetails>>();

	//	ArrayList<FeaturedMediaListData> categoryMediaList = new ArrayList<FeaturedMediaListData>();
	int mainStreamHeaders = 0;

	ArrayList<HomeStream> homeStreamList = new ArrayList<HomeStream>();

	@Override
	public void onReceive(JSONObject jsonObj, int id) 
	{
		Gson gson = new Gson();

		try 
		{
			if(jsonObj.has("status") && jsonObj.getInt("status") == 1)
			{
				Toast.makeText(this, Utils.getResponseMessage(jsonObj.getInt("message")), 
						Toast.LENGTH_LONG).show();
			}
		}
		catch (JSONException e1) 
		{
			e1.printStackTrace();
		}

		switch(id)
		{
		case UPDATE_SINGLE_HOME_STREAM_ITEM:

			break;

		case USER_HOME_STREAM:

			Log.d("BSMA", "USER_HOME_STREAM response ::"+jsonObj.toString());
			try
			{
				JSONObject data = new JSONObject(jsonObj.toString());

				if(data.getString("status").equalsIgnoreCase("0"))
				{
					JSONArray jsonArray = data.getJSONArray("data");

					HomeStream homeStream[] = gson.fromJson(jsonArray.toString(), HomeStream[].class);


					mainStreamHeaders = homeStream.length;
					homeStreamList.clear();

					Log.d("LandingAct", "Home_Item_ID::" + homeStream[0].Home_Item_ID
							+ " Home_Item_Type::" + homeStream[0].Home_Item_Type);

					for (int i = 0; i < homeStream.length; i++) 
					{
						HomeStream stream = new HomeStream();

						stream.Home_Item_ID = homeStream[i].Home_Item_ID;
						stream.Count = homeStream[i].Count;
						stream.Home_Items_Name = homeStream[i].Home_Items_Name;
						stream.Home_Item_Sequence = homeStream[i].Home_Item_Sequence;
						stream.Home_Item_Type = homeStream[i].Home_Item_Type;
						stream.Home_Items_Status = homeStream[i].Home_Items_Status;
						stream.Last_Modified = homeStream[i].Last_Modified;
						stream.User_Home_Item_Id = homeStream[i].User_Home_Item_Id;
						stream.User_Id = homeStream[i].User_Id;

						homeStreamList.add(stream);
					}

					List<Streams> streams = new ArrayList<Streams>();

					for (int i = 0; i < homeStreamList.size(); i++) 
					{
						Streams stream = new Streams();
						stream.Count = homeStreamList.get(i).Count;
						stream.HomeItemID = Integer.parseInt(homeStreamList.get(i).Home_Item_ID);
						stream.HomeItemSequence = homeStreamList.get(i).Home_Item_Sequence.length() > 0 ?
								Integer.parseInt(homeStreamList.get(i).Home_Item_Sequence) : 0;
								stream.HomeItemType = homeStreamList.get(i).Home_Item_Type;
								stream.HomeItemsName = homeStreamList.get(i).Home_Items_Name;
								stream.HomeItemsStatus = homeStreamList.get(i).Home_Items_Status;
								stream.LastModified = homeStreamList.get(i).Last_Modified;
								stream.UserHomeItemId = homeStreamList.get(i).User_Home_Item_Id;
								stream.UserId = Integer.parseInt(homeStreamList.get(i).User_Id);

								streams.add(stream);
					}

					// update db here
					HomeStreamManager.getInstance(getApplicationContext()).saveStreams(streams);

					List<Streams> list = HomeStreamManager.getInstance(getApplicationContext()).getHomeStream();


					Log.d("BSMA", "homeStreamCategoriesList.size()::"+homeStreamCategoriesList.size()
							+" list size::"+list.size()+"  isStreamEdited::"+isStreamEdited(list));


					int isEdited = isStreamEdited(list);
					if(isEdited == -1)
					{
						getHomeStreamMediaList(list);
					}
					else if(isEdited == 1)
					{
						Log.d("BSMA on resume", "****streams deleted");
						sortList();
						removeDeletedStreams(list);
						displayCategoryList(false);
					}
					else
					{
						Log.d("BSMA on resume", "****sort list");
						// check if sequence is changed
						sortList();
						displayCategoryList(false);
					}
				}
			}
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			catch(JsonSyntaxException e)
			{
				e.printStackTrace();
			}

			break;
		}
	}
	private boolean isTwitterLoggedInAlready() {

		Log.i("msharepreferences-->","11111111"+ Utils.getDataBoolean(BrainSlamMainActivity.this,"Twitpref",com.android.brainslam.constants.Constants.PREF_KEY_TWITTER_LOGIN));

		return Utils.getDataBoolean(BrainSlamMainActivity.this,"Twitpref",com.android.brainslam.constants.Constants.PREF_KEY_TWITTER_LOGIN);
		// return twitter login status from Shared Preferences
		//Log.i("msharepreferences-->",""+.mSharedPreferences.getBoolean(com.android.brainslam.constants.Constants.PREF_KEY_TWITTER_LOGIN, false));
		//return .mSharedPreferences.getBoolean(com.android.brainslam.constants.Constants.PREF_KEY_TWITTER_LOGIN, false);
	}

	public void twitterpreprocesser() 
	{
		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(BrainSlamMainActivity.this, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}

		// Check if twitter keys are set
		if (com.android.brainslam.constants.Constants.TWITTER_CONSUMER_KEY.trim().length() == 0
				|| com.android.brainslam.constants.Constants.TWITTER_CONSUMER_SECRET.trim().length() == 0) {
			// Internet Connection is not present
			alert.showAlertDialog(BrainSlamMainActivity.this, "Twitter oAuth tokens",
					"Please set your twitter oauth tokens first!", false);
			// stop executing code by return
			return;
		}

		//mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
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


					Utils.storeDatafortwitter(BrainSlamMainActivity.this,"Twitpref", 
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

					// Getting user details from twitter
					// For now i am getting his name only
					long userID = accessToken.getUserId();
					User user1 = twitter.showUser(userID);
					String username = user1.getName();

					Intent statusUpdateActivity = new Intent(BrainSlamMainActivity.this,UpdateTwitterStatusActivity.class);
					startActivity(statusUpdateActivity);


				} catch (Exception e) {
					// Check log for login errors
					Log.e("Twitter Login Error", "> " + e.getMessage());
				}
			}
		}

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
		} else {
			// user already logged into twitter
			Toast.makeText(getApplicationContext(),
					"Already Logged into twitter", Toast.LENGTH_LONG).show();
		}
	}

	private void shareViaFacebook()
	{
		if (FacebookDialog.canPresentShareDialog(getApplicationContext(), 
				FacebookDialog.ShareDialogFeature.SHARE_DIALOG))
		{
			Uri path = Uri.parse("android.resource://"+BrainSlamMainActivity.this.getPackageName()+"/"+R.drawable.ic_launcher);

			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(BrainSlamMainActivity.this)
			.setDescription("to be shared")
			.setPicture(path.getPath())
			.build();
			uiHelper.trackPendingDialogCall(shareDialog.present());

		}
		else
		{
			Log.v("", "No Native App");

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
					BrainSlamMainActivity.this.finish();


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
	}


	public class MainStreamAsyncHandler extends AsyncTask<Void, Integer, JSONObject> 
	{
		ProgressBar prog;
		final Context context;
		final String url;
		int id = 0;
		String streamId;
		boolean isGet;
		List<BasicNameValuePair> postParameters;

		public MainStreamAsyncHandler(final Context context, final String url, int id, String streamId,
				boolean isGet, List<BasicNameValuePair> postParameters) 
		{
			this.context = context;
			this.url = url;
			this.id = id;
			this.isGet = isGet;
			this.postParameters = postParameters; 
			this.streamId = streamId;

			Log.d("mayur", "in AsyncHandler");
		}

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			prog =new ProgressBar(context);
			prog.setVisibility(View.VISIBLE);

			Log.d("mayur", "in pre execute");
		}

		@Override
		protected JSONObject doInBackground(final Void... params) 
		{
			Log.v("mayur","do in background: "+url);
			JSONObject jsonResp = HttpCommunicator.callRsJson(url,isGet,postParameters,context);
			return jsonResp;
		}

		@Override
		protected void onPostExecute(final JSONObject jsonObject) 
		{
			super.onPostExecute(jsonObject);

			Log.d("mayur", "in post execute ");

			//			AsyncCallBack asyn = (AsyncCallBack) context;
			prog.setVisibility(View.GONE);
			//			asyn.onReceive(jsonObject, this.id);
			onStreamResponseReceived(jsonObject, id, streamId);
		}
	}


//	public void onClickFeaturedVideo(View view)
//	{
//		int pos = (Integer) view.getTag();
//
//		Log.d("onClickFeaturedVideo", "position clicked::"+pos+"  Video thumbnail name::"+featuredMediaList.get(pos).Name);
//
//		//		Intent intent=new Intent(BrainSlamMainActivity.this,VideoDetailsActivity.class);
//		//		intent.putExtra("mediaObj",featuredMediaList.get(pos));
//		//		startActivity(intent);	
//	}

	private void onStreamResponseReceived(JSONObject jsonObj, int id, String streamId)
	{
		Gson gson = new Gson();

		try 
		{
			if(jsonObj.has("status") && jsonObj.getInt("status") == 1)
			{
				Toast.makeText(this, Utils.getResponseMessage(jsonObj.getInt("message")), 
						Toast.LENGTH_LONG).show();
			}
		}
		catch (JSONException e1) 
		{
			e1.printStackTrace();
		}

		switch(id)
		{
		case LAST_MEDIA_LIST_CATEGORY:
			parseResponse(jsonObj, LAST_MEDIA_LIST_CATEGORY, streamId);
			break;

		case MEDIA_LIST_CATEGORIES:
			parseResponse(jsonObj, MEDIA_LIST_CATEGORIES, streamId);
			break;

		case FEATURED_MEDIA_LIST:

			Log.d("BSMA", "FEATURED_MEDIA_LIST response ::"+jsonObj.toString());
			featuredMediaList.clear();

			try
			{
				JSONObject data = new JSONObject(jsonObj.toString());
				if(data.getString("status").equalsIgnoreCase("0"))
				{
					FeaturedMediaList mediaList = gson.fromJson(jsonObj.toString(), FeaturedMediaList.class);
					Log.d("LandingAct", "Thumbnail_Url::" + mediaList.data[0].Thumbnail_Url
							+ " msg::" + mediaList.message + "  status::"
							+ mediaList.status);

					for (int i = 0; i < mediaList.data.length; i++) 
					{
						StreamItemDetails fMediaDao = new StreamItemDetails();

						fMediaDao.Description = mediaList.data[i].Description;
						fMediaDao.Media_Id = mediaList.data[i].Media_Id;
						fMediaDao.Height = mediaList.data[i].Height;
						fMediaDao.Width = mediaList.data[i].Width;
						fMediaDao.Categories = mediaList.data[i].Categories;
						fMediaDao.Tranding_Score = mediaList.data[i].Tranding_Score;
						fMediaDao.Created_At = mediaList.data[i].Created_At;
						fMediaDao.Updated_At = mediaList.data[i].Updated_At;
						fMediaDao.Name = mediaList.data[i].Name;
						fMediaDao.Tags = mediaList.data[i].Tags;
						fMediaDao.Average_IQ = mediaList.data[i].Average_IQ;
						fMediaDao.Thumbnail_Url = mediaList.data[i].Thumbnail_Url;
						fMediaDao.Views = mediaList.data[i].Views;
						fMediaDao.Duration = mediaList.data[i].Duration;
						fMediaDao.Number_Of_Ratings = mediaList.data[i].Number_Of_Ratings;
						fMediaDao.Download_Url = mediaList.data[i].Download_Url;
						fMediaDao.Data_Url = mediaList.data[i].Data_Url;
						fMediaDao.Media_Type = mediaList.data[i].Media_Type;
						
				//		--byNaresh
						Log.d("BSMA-->", mediaList.data[i].Media_Owner_Id);
						fMediaDao.Media_Owner_Id = mediaList.data[i].Media_Owner_Id;
						
						featuredMediaList.add(fMediaDao);
					}

					Log.d("BSMA", "featuredMediaList size::"+featuredMediaList.size());

					displayFeaturedVideos(featuredMediaList);
					
					// insert in db
					//				FeaturedMediaManager.getInstance().saveFeaturedMedia(mediaList);
					//				featuredMediaList = FeaturedMediaManager.getInstance().getFeaturedMedia();

					//				featuredMediaHorizontalLV.setAdapter(mAdapter);
//					featuredMediaHorizontalLV.setAdapter(new FeaturedListAdapter(
//							BrainSlamMainActivity.this, featuredMediaList));
				}
			}
			catch(JsonSyntaxException e)
			{
				e.printStackTrace();
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}


			break;
		}
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		//for facebook share dialog responses
		uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
			@Override
			public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
				Log.e("", String.format("Error: %s", error.toString()));
			}

			@Override
			public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
				Log.i("", "Success!");
			}
		});
	}

	public void MessageIconClick(View view)
	{
//		Intent intent=new Intent(BrainSlamMainActivity.this, MessagesSectionActivity.class);
//		startActivity(intent);
//		overridePendingTransition(R.anim.in_from_bottom,R.anim.fade_out);
		
//		Intent newMessageIntent =new Intent(BrainSlamMainActivity.this, ComposeNewMessageActivity.class);
//		startActivity(newMessageIntent);
	}
}