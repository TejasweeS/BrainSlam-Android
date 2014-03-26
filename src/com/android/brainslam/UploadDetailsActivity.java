package com.android.brainslam;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import com.android.brainslam.NetworkHandler.HttpCommunicator;
import com.android.brainslam.constants.Constants;
import com.android.brainslam.db.dao.Category;
import com.android.brainslam.db.dao.Crews;
import com.android.brainslam.db.dao.MyFriends;
import com.android.brainslam.manager.CrewManager;
import com.android.brainslam.manager.FriendManager;
import com.android.brainslam.vo.CrewListData;
import com.android.brainslam.vo.MyFriendsVo;
import com.android.listdapters.HandPicListAdapter;
import com.android.utils.CustomSpinnerDialog;
import com.android.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/************** please Send me the following parameters in intent *******************
 * 
 * 
 * "CoverPhotourl" // SD card Path of cover photo that has been selected for video (if video is getting uploaded)
 * 
 * "Mediaurl"  // SD card Path of the media which is going to be uploaded
 * 
 * "VideoMediaType" boolean flag to indicate that video or audio is getting uploaded
 * */
public class UploadDetailsActivity extends Activity implements OnEditorActionListener,OnTouchListener,AnimationListener{
	private ViewFlipper viewFlipper;
	private float lastX;
	boolean isWorld, isHandpic;
	String Mediaurl;
	boolean sharingArray[]=new boolean[3];
	ArrayList<CrewListData> crewData;

	StringBuffer videoName=new StringBuffer();
	GridView tagGridview;
	TagGridAdapter adapter;
	VideoView videoView;
	CustomSpinnerDialog dialog;
	ImageView videoPreviewPlayButton;
	ImageView videoThumbnailImageView;
	RelativeLayout VideoPreviewlayout;
	boolean isTypeVideo = false;
	ImageView ImagePreviewImageView;
	ArrayList<String> tags=new ArrayList<String>();
	int currentChild = -1;

	//---- hand pic---
	ListView handPicListView;

	List<Crews> crewList;
	List<MyFriends> friendList;
	LinearLayout step2Layout,handPicLayout;
	 EditText handpicSearchEditText;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_details);

		/******************** Enable this code when all attributes will come from inetent****************/

		Mediaurl=getIntent().getExtras().getString("Mediaurl");
		isTypeVideo=getIntent().getExtras().getBoolean("VideoMediaType");

		videoThumbnailImageView=(ImageView)findViewById(R.id.video_thumbnail);
		ImagePreviewImageView=(ImageView)findViewById(R.id.image_thumbnail);
		VideoPreviewlayout=(RelativeLayout)findViewById(R.id.VideoPreview);
		handPicListView = (ListView) findViewById(R.id.hand_pic_list_view); 
		dialog = new CustomSpinnerDialog(UploadDetailsActivity.this);
		
		crewList = CrewManager.getInstance(getApplicationContext()).getCrews(); 
		friendList = FriendManager.getInstance(getApplicationContext()).getMyFriends();
		step2Layout=(LinearLayout)findViewById(R.id.step2_layout );
		handPicLayout=(LinearLayout)findViewById(R.id.HandPick );
		handpicSearchEditText = (EditText) findViewById(R.id.handpic_search_edittext);
		new HandPickAsycTask().execute();
		if(isTypeVideo) //video selected
		{
			String thumnailUrl = getIntent().getExtras().getString("CoverPhotourl");
			Drawable d = (Drawable) Drawable.createFromPath(thumnailUrl);
//			if(Build.VERSION.SDK_INT >= 4.0){
//				videoThumbnailImageView.setBackgroundDrawable(d);
//				//this code will be executed on devices running on DONUT (NOT ICS) or later
//			}
//			else
//				videoThumbnailImageView.setBackground(d);
			
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			Bitmap bmp = BitmapFactory.decodeFile(thumnailUrl,options);
			if(null != bmp)
				videoThumbnailImageView.setImageBitmap(bmp);
			else
				Toast.makeText(UploadDetailsActivity.this, "image not found", Toast.LENGTH_LONG).show();

			videoThumbnailImageView.setVisibility(View.VISIBLE);
			ImagePreviewImageView.setVisibility(View.GONE);
		}
		else// image selected
		{
			VideoPreviewlayout.setVisibility(View.GONE);
			
			Drawable d = (Drawable) Drawable.createFromPath(Mediaurl);

			
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			Bitmap bmp = BitmapFactory.decodeFile(Mediaurl,options);
			if(null != bmp)
				ImagePreviewImageView.setImageBitmap(bmp);
			else
				Toast.makeText(UploadDetailsActivity.this, "image not found", Toast.LENGTH_LONG).show();
			ImagePreviewImageView.setVisibility(View.VISIBLE);
		}

		viewFlipper=(ViewFlipper)findViewById(R.id.viewFlipper1);
		tagGridview=(GridView)findViewById(R.id.VideoTags);
		videoView = (VideoView)findViewById(R.id.videoView);
		videoPreviewPlayButton = (ImageView)findViewById(R.id.Preview_Play_button);
		tagGridview.setOnTouchListener(this);
		EditText videoName=(EditText)findViewById(R.id.video_upload_title);
		EditText tag=(EditText)findViewById(R.id.searchbar);
		tag.setImeOptions(EditorInfo.IME_ACTION_DONE);
		tag.setOnEditorActionListener(this);
		videoName.setOnEditorActionListener(this);
		adapter=new TagGridAdapter(tags);
		videoView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					System.out.println("ONNNN CLICK");
					if(videoView.isPlaying())
					{
						videoView.pause();
						videoPreviewPlayButton.setVisibility(View.VISIBLE);  
					}
					else
					{
						videoView.start();
						videoPreviewPlayButton.setVisibility(View.GONE);
					}
				}
				return false;
			}
		});

		tagGridview.setAdapter(adapter);
	}

	@Override
	public boolean onTouchEvent(MotionEvent touchevent) {

		//ImageView fourcircle=(ImageView)findViewById(R.id.guides_circle4);
		// TODO Auto-generated method stub
		switch (touchevent.getAction())
		{
		// when user first touches the screen to swap
		case MotionEvent.ACTION_DOWN: 
		{
			lastX = touchevent.getX();
			break;
		}
		case MotionEvent.ACTION_UP: 
		{
			float currentX = touchevent.getX();

			// if left to right swipe on screen
			if (lastX < currentX) 
			{
				// If no more View/Child to flip
				moveToNext();
			}
			// if right to left swipe on screen
			if (lastX > currentX)
			{
				moveToPrevious();
			}

			hideKeyboard();

			break;
		}
		}
		return false;
	}

	private void hideKeyboard()
	{
		try
		{
			InputMethodManager inputMethodManager = (InputMethodManager) 
					UploadDetailsActivity.this.getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	private void moveToPrevious()
	{

		System.out.println("rutuja prev Displayed child::"+viewFlipper.getDisplayedChild());
		currentChild = viewFlipper.getDisplayedChild();
		if (viewFlipper.getDisplayedChild() == 1)
			//			break;
		{
			Toast.makeText(UploadDetailsActivity.this, "work in progress", Toast.LENGTH_LONG).show();
			return;
		}
		// set the required Animation type to ViewFlipper
		// The Next screen will come in form Right and current Screen will go OUT from Left 
		viewFlipper.setInAnimation(this, R.anim.in_from_right);
		viewFlipper.setOutAnimation(this, R.anim.out_to_left);
        
		switch(viewFlipper.getDisplayedChild())
		{

		case 0:
			Log.v("rutuja", "VIDEO PLAY");
			break;
		case 1:
			if(isHandpic)
			{
				step2Layout.setVisibility(View.GONE);
				handPicLayout.setVisibility(View.VISIBLE);
				Log.v("rutuja", "display child 2 hand pic ");
			}
			
			break;
		case 2:

			if(isTypeVideo)//video
			{
				videoPreviewPlayButton.setVisibility(View.VISIBLE);
				videoThumbnailImageView.setVisibility(View.VISIBLE);
				videoView.setVisibility(View.GONE);
			}
			else
			{
				VideoPreviewlayout.setVisibility(View.GONE);
				ImagePreviewImageView.setVisibility(View.VISIBLE);
				videoView.setVisibility(View.GONE);
			}
			break;


		}
		// Show The Previous Screen
		EditText videoName=(EditText)findViewById(R.id.video_upload_title);
		if (viewFlipper.getDisplayedChild() == 0 && videoName.getText().length()==0)
		{

			videoName.setError("Video Title should not be Blank");
		}
//		else if(viewFlipper.getDisplayedChild() == 1)
//		{
//			Toast.makeText(UploadDetailsActivity.this, "work in progress", Toast.LENGTH_LONG).show();
//		}
		else
		{
			videoName.setError(null);
			viewFlipper.showPrevious();
		}
		System.out.println("PREVIOUS"+viewFlipper.getDisplayedChild());
		int childId=viewFlipper.getDisplayedChild();
		upadteDots(childId);
	}


	private void upadteDots(int childId)
	{
		ImageView firstcircle=(ImageView)findViewById(R.id.uploaddetails_circle1);
		ImageView secondcircle=(ImageView)findViewById(R.id.uploaddetails_circle3);
		ImageView thirdcircle=(ImageView)findViewById(R.id.uploaddetails_circle2);
		switch(childId)
		{
		case 0:
			System.out.println("0 th");
			firstcircle.setBackgroundResource(R.drawable.slam_circle);
			secondcircle.setBackgroundResource(R.drawable.white_circle);
			thirdcircle.setBackgroundResource(R.drawable.white_circle);

			break;
		case 1:
			System.out.println("1 th");
			firstcircle.setBackgroundResource(R.drawable.white_circle);
			secondcircle.setBackgroundResource(R.drawable.slam_circle);
			thirdcircle.setBackgroundResource(R.drawable.white_circle);
			break;
		case 2:
			System.out.println("2 th");

			firstcircle.setBackgroundResource(R.drawable.white_circle);
			secondcircle.setBackgroundResource(R.drawable.white_circle);
			thirdcircle.setBackgroundResource(R.drawable.slam_circle);
			/*}
			else
			{
//				viewFlipper.showPrevious()
				EditText videoName=(EditText)findViewById(R.id.video_upload_title);
				videoName.setError("Video Title should not be Blank");
			}
			 */	
			break;
		}
	}
	private void moveToNext()
	{
		System.out.println("rutuja next Displayed child::"+viewFlipper.getDisplayedChild());
		currentChild = viewFlipper.getDisplayedChild();
		if (viewFlipper.getDisplayedChild() == 0)
		{
			//			break;
			return;
		}
		else if(viewFlipper.getDisplayedChild() == 2)
		{
			if(isHandpic)
			{
				step2Layout.setVisibility(View.VISIBLE);
				handPicLayout.setVisibility(View.GONE);
				Log.v("rutuja", "display child 2 hand pic ");
				isHandpic = false;
			}
			
			return;
		}
		
		
		// set the required Animation type to ViewFlipper
		// The Next screen will come in form Left and current Screen will go OUT from Right 
		viewFlipper.setInAnimation(this, R.anim.in_from_left);
		viewFlipper.setOutAnimation(this, R.anim.out_to_right);
		// Show the next Screen
		System.out.println("Displayed child::"+viewFlipper.getDisplayedChild());

		
		System.out.println("Displayed child after shownext::"+viewFlipper.getDisplayedChild());
			
			viewFlipper.showNext();

		switch(viewFlipper.getDisplayedChild())
		{

		case 0:
			Log.v("rutuja", "VIDEO PLAY");
			break;
		case 1:
			
			
			break;
		case 2:


			if(isTypeVideo)//video
			{
				videoPreviewPlayButton.setVisibility(View.VISIBLE);
				videoThumbnailImageView.setVisibility(View.VISIBLE);
				videoView.setVisibility(View.GONE);
			}
			else
			{

				VideoPreviewlayout.setVisibility(View.GONE);
				ImagePreviewImageView.setVisibility(View.VISIBLE);
				videoView.setVisibility(View.GONE);

			}

			Log.v("rutuja", "step 2");
			break;


		}
		int childId=viewFlipper.getDisplayedChild();
		upadteDots(childId);
	}

	public void onClick(View view)
	{
		switch (view.getId()) {
		case R.id.world:
			moveToPrevious();
			isWorld=true;
			isHandpic = false;
			break;
		case R.id.crew_pick:
			isWorld=false;
			isHandpic = false;
			moveToPrevious();
			break;

		case R.id.handpick:
			isHandpic = true;
			//			  SWPNIL
			
			//			layout2.setVisibility(View.INVISIBLE);
			Animation inFromBotoom=AnimationUtils.loadAnimation(this, R.anim.in_from_bottom);
			Animation fadeOut=AnimationUtils.loadAnimation(this, R.anim.fade_out);

			//			fadeOut.setAnimationListener(this);
			inFromBotoom.setAnimationListener(this);

			step2Layout.startAnimation(fadeOut);
			//			inFromBotoom.setAnimationListener(listener)
			handPicLayout.startAnimation(inFromBotoom);
			//			inFromBotoom.start();
			//			fadeOut.start();

		  
		   handpicSearchEditText.addTextChangedListener(new TextWatcher() {
			
//			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				ArrayList<Crews> crewSearch = new ArrayList<Crews>();
				ArrayList<MyFriends> friendsSearch = new ArrayList<MyFriends>();


				for(int i=0;i<crewList.size()+friendList.size();i++)
				{

					if(i < crewList.size())
					{
						String playerName = crewList.get(i).Crew_Name;
						//compare the String in EditText with Names in the ArrayList
						if(Utils.searchData(handpicSearchEditText.getText().toString().trim(), playerName)){

							System.out.println("rutuja match found category");
							crewSearch.add(crewList.get(i));
						}
					}
					else
					{
						String playerName1 = friendList.get(i - crewList.size()).name;
						//compare the String in EditText with Names in the ArrayList
						if(Utils.searchData(handpicSearchEditText.getText().toString().trim(), playerName1)){
							System.out.println("rutuja match found friends");
							friendsSearch.add(friendList.get(i - crewList.size()));
						}

					}


				}

				if(handpicSearchEditText.getText().toString().trim().length() > 0)
				{
					HandPicListAdapter adapter ;
					//set adapter for the listset adapter for both the list				
					adapter = new HandPicListAdapter(UploadDetailsActivity.this, crewSearch, friendsSearch);
					handPicListView.setAdapter(adapter);
				}
				else if(handpicSearchEditText.getText().toString().trim().length() == 0)
				{
					hideKeyboard();
					HandPicListAdapter adapter ;
					//set adapter for the listset adapter for both the list				
					adapter = new HandPicListAdapter(UploadDetailsActivity.this, crewList, friendList);
					handPicListView.setAdapter(adapter);			
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
		});

			

//			Toast.makeText(UploadDetailsActivity.this, "work in progress", Toast.LENGTH_LONG).show();




			/*moveToPrevious();
			Intent friendIntent=new Intent(this, HandPickActivity.class);
			startActivityForResult(friendIntent,3);*/
			break;
		case R.id.uploaddetails_next:
			Log.v("rutuja", "next click current child:: "+currentChild);

			//			if(currentChild == 2)
			//			{
			//				
			//				Log.v("rutuja", "done withe view flipper go to Rating");
			//				
			//			}
			//			else if(currentChild != -1)
			moveToPrevious();

			/*Intent friendIntent=new Intent(this, AddOrDeleteCrewActivity.class);
			startActivityForResult(friendIntent,3);
			break;*/
		case R.id.Preview_Play_button:
			Log.v("rutuja", "rutuja  play button clicked");

			videoThumbnailImageView.setVisibility(View.GONE);
			videoView.setVisibility(View.VISIBLE);
			videoView.setVideoURI(Uri.parse(Mediaurl));
			videoView.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub

					videoView.start();	
					videoPreviewPlayButton.setVisibility(View.GONE);

				}
			});
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onEditorAction(TextView editext, int keycode, KeyEvent keyEvent) {
		// TODO Auto-generated method stub
		System.out.println("KEYCODE::"+keycode);
		switch (editext.getId()) {
		case R.id.video_upload_title:
			videoName=new StringBuffer(editext.getText());
			break;
		case R.id.searchbar:
			if(tags.contains(editext.getText().toString())==false)
			{
				tags.add(editext.getText().toString());
				editext.setText("");
				adapter.notifyDataSetChanged();
			}
			break;
		}
		return false;
	}

	/**** this class is adapter of GridView of Tags******/ 

	class TagGridAdapter extends BaseAdapter
	{
		//		ArrayList< String> tagList;

		public TagGridAdapter(ArrayList<String> tagList) {
			// TODO Auto-generated constructor stub
			//			this.tagList = tagList;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return tags.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return tags.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			final ViewHolder holder;
			LayoutInflater inflater = UploadDetailsActivity.this.getLayoutInflater();
			//inflater = (LayoutInflater) context
			//.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.taglist_items, parent, false);
				holder = new ViewHolder();
				holder.closeTag = (ImageView) convertView
						.findViewById(R.id.tagclose);
				holder.tagText = (TextView) convertView
						.findViewById(R.id.tagname);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.closeTag.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					tags.remove(position);
					notifyDataSetChanged();
				}
			});
			holder.tagText.setText(tags.get(position));
			return convertView;
		}
		class ViewHolder {
			ImageView closeTag;
			TextView tagText;
		}
	}



	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		onTouchEvent(event);
		return false;
	}

	public void listcheck(View v)
	{

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 2: // for Crew
			if(resultCode == RESULT_OK && data.getExtras().containsKey("data")){  
				crewData=(ArrayList<CrewListData>) data.getSerializableExtra("data");
				viewFlipper.showNext();
				upadteDots(viewFlipper.getDisplayedChild());
				isWorld=false;
			}
			if (resultCode == RESULT_CANCELED) {    
				//Write your code if there's no result
			}
			break;
		case 3: // for HandPic

			break;

		default:
			break;
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
	}

	public void onChecked(View view)
	{
		CheckBox box=(CheckBox)view;
		switch (Integer.parseInt(view.getTag().toString())) {
		case 0:
			sharingArray[0]=box.isChecked();
			break;
		case 1:
			sharingArray[1]=box.isChecked();
			break;
		case 2:
			sharingArray[2]=box.isChecked();
			break;

		default:
			break;
		}
	}

	class HandPickAsycTask extends AsyncTask<Void, Integer, JSONObject>
	{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.show();

		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			// TODO Auto-generated method stub

			String secretKey = Utils.getDataString(UploadDetailsActivity.this,
					Constants.PREFS_NAME, Constants.SP_SECRET_KEY);
			String userID = Utils.getDataString(UploadDetailsActivity.this,
					Constants.PREFS_NAME, Constants.SP_USER_ID);

			List<BasicNameValuePair> requestParams = new ArrayList<BasicNameValuePair>();

			requestParams.add(new BasicNameValuePair("operation", "getAllCrewList"));
			requestParams.add(new BasicNameValuePair("Secret_Key", secretKey));
			requestParams.add(new BasicNameValuePair("User_Id", userID));
			long unixTime = System.currentTimeMillis() / 1000L;
			if(crewList.size() > 0)
				requestParams.add(new BasicNameValuePair("Last_Modified",""+unixTime));
			requestParams.add(new BasicNameValuePair("By_User_Id", userID));
			requestParams.add(new BasicNameValuePair("pageSize", "30"));
			requestParams.add(new BasicNameValuePair("pageIndex", ""+1));
			//subscribed crew only

			requestParams.add(new BasicNameValuePair("Unsubscribed_Only",""+1)); 
			JSONObject jsonResp = HttpCommunicator.callRsJson(Constants.SERVER_URL+"crew.php?",false,requestParams,UploadDetailsActivity.this);
			//			System.out.println("R");


			return jsonResp;
		}

		@Override
		protected void onPostExecute(JSONObject jsonObj) {
			// TODO Auto-generated method stub
			super.onPostExecute(jsonObj);
			Gson categoryGson = new  Gson();
			List<CrewListData> crewVoList = new ArrayList<CrewListData>();
			try {
				if(jsonObj.has("status") && jsonObj.getInt("status") == 0 && jsonObj.getInt("message") == 6)
				{

					if(jsonObj.has("data") && jsonObj.get("data").toString().length() >0)
					{
                         
						Type listType = (Type) new TypeToken<List<CrewListData>>(){}.getType();

						crewVoList = categoryGson.fromJson(jsonObj.getJSONArray("data").toString(), listType);
						for(CrewListData crewListData: crewVoList)
						{
							Crews crews = new Crews();
							crews.CrewId = crewListData.Crew_Id;
							crews.Added_Date = crewListData.Added_Date;
							crews.Crew_Logo = crewListData.Crew_Logo;
							crews.Crew_Name = crewListData.Crew_Name;
							if(crewListData.Home_Page_Item_Sequence.trim().length() > 0)
							    crews.Home_Page_Item_Sequence = Integer.parseInt(crewListData.Home_Page_Item_Sequence);
							crews.Is_On_Home_Page = crewListData.Is_On_Home_Page == 1 ? true : false;
							crews.Mission_Statement = crewListData.Mission_Statement;
							
							crewList.add(crews);
						}
						
						CrewManager.getInstance(getApplicationContext()).removeAllCrews();
						CrewManager.getInstance(getApplicationContext()).saveCrews(crewList);
						
						
					}

				}
				else if(jsonObj.has("errMessage"))
				{
					Toast.makeText(UploadDetailsActivity.this,jsonObj.getString("errMessage"), Toast.LENGTH_LONG).show();
				}
				else if(jsonObj.has("status") && jsonObj.getInt("status") == 1)
				{

					String msg = Utils.getResponseMessage(jsonObj.getInt("message"));
					Toast.makeText(UploadDetailsActivity.this, msg, Toast.LENGTH_LONG).show();

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}



			new GetFrindsListAsync().execute();









		}
	}


	class GetFrindsListAsync extends AsyncTask<Void, Integer, JSONObject>
	{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			if(!dialog.isShowing())
				dialog.show();

		}

		@Override
		protected JSONObject doInBackground(Void... param) {
			// TODO Auto-generated method stub

			String secretKey = Utils.getDataString(UploadDetailsActivity.this,
					Constants.PREFS_NAME, Constants.SP_SECRET_KEY);
			String userID = Utils.getDataString(UploadDetailsActivity.this,
					Constants.PREFS_NAME, Constants.SP_USER_ID);

			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

			params.add(new BasicNameValuePair("operation", "friendsList"));
			params.add(new BasicNameValuePair("Secret_Key", secretKey));
			params.add(new BasicNameValuePair("User_Id", userID));
			params.add(new BasicNameValuePair("pageSize", 5+""));
			params.add(new BasicNameValuePair("pageIndex", 1+""));
			long unixTime = System.currentTimeMillis() / 1000L;
			if(FriendManager.getInstance(getApplicationContext()).getMyFriends().size() > 0)
				params.add(new BasicNameValuePair("Last_Modified",""+unixTime));

			JSONObject jsonResp = HttpCommunicator.callRsJson(Constants.SERVER_URL
					+ "user.php?",false,params,UploadDetailsActivity.this);

			return jsonResp;
		}

		@Override
		protected void onPostExecute(JSONObject jsonObj) {
			// TODO Auto-generated method stub
			super.onPostExecute(jsonObj);
			Gson categoryGson = new  Gson();
			List<MyFriendsVo> friendsCategoryVoList = new ArrayList<MyFriendsVo>();
			try {
				if(jsonObj.has("status") && jsonObj.getInt("status") == 0 && jsonObj.getInt("message") == 6)
				{

					if(jsonObj.has("data") && jsonObj.get("data").toString().length() >0)
					{
						Type listType = (Type) new TypeToken<List<MyFriendsVo>>(){}.getType();

						friendsCategoryVoList = categoryGson.fromJson(jsonObj.getJSONArray("data").toString(), listType);

						for(MyFriendsVo friendsVo: friendsCategoryVoList)
						{

							MyFriends friend = new MyFriends();

							friend.aboutUser = friendsVo.getAbout_User();
							friend.addedDate = friendsVo.getAdded_Date();
							friend.emailId = friendsVo.getEmail_Id();
							friend.isOnHomeScreen = false;
							friend.lastModified = System.currentTimeMillis() / 1000L;
							friend.mobileNumber = friendsVo.getMobile_Number();
							friend.name = friendsVo.getUser_Name();
							friend.photoUrl = friendsVo.getPhoto_Url();
							friend.userId = friendsVo.getUser_Id();


							friendList.add(friend);
						}

						FriendManager.getInstance(getApplicationContext()).removeAllfrinends();
						FriendManager.getInstance(getApplicationContext()).saveMyFriends(friendList);
					}
					else
						Log.v("UploadDetailsActivity", "No new updates for Friends list");

				}
				else if(jsonObj.has("errMessage"))
				{
					Toast.makeText(UploadDetailsActivity.this,jsonObj.getString("errMessage"), Toast.LENGTH_LONG).show();
				}
				else if(jsonObj.has("status") && jsonObj.getInt("status") == 1)
				{

					String msg = Utils.getResponseMessage(jsonObj.getInt("message"));
					Toast.makeText(UploadDetailsActivity.this, msg, Toast.LENGTH_LONG).show();

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			HandPicListAdapter adapter ;
			//set adapter for the listset adapter for both the list				
			adapter = new HandPicListAdapter(UploadDetailsActivity.this, crewList, friendList);
			handPicListView.setAdapter(adapter);
			dialog.dismiss();
		}
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		LinearLayout layout2=(LinearLayout)findViewById(R.id.HandPick );
		layout2.setVisibility(View.INVISIBLE);
	}



}
