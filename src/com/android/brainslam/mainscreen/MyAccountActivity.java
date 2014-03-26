package com.android.brainslam.mainscreen;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.android.brainslam.ExpandableHeightGridView;
import com.android.brainslam.FriendNotificationActivity;
import com.android.brainslam.R;
import com.android.brainslam.NetworkHandler.AsyncCallBack;
import com.android.brainslam.NetworkHandler.AsyncHandler;
import com.android.brainslam.NetworkHandler.HttpCommunicator;
import com.android.brainslam.constants.Constants;
import com.android.brainslam.db.dao.UserDetails;
import com.android.brainslam.db.dao.Users;
import com.android.brainslam.manager.UserManager;
import com.android.brainslam.vo.FeaturedMediaListData;
import com.android.listdapters.AccountSettingVideosAdapter;
import com.android.utils.CustomSpinnerDialog;
import com.android.utils.ImageLoader;
import com.android.utils.MemoryCache;
import com.android.utils.Utils;
import com.google.gson.Gson;

public class MyAccountActivity extends Fragment implements AsyncCallBack
{
	protected static final int SELECT_IMAGE = 1;
	private static final int YES  = 1;
	private static final int USERDETAILS = 0;
	protected static final int UPDATEDETAILS = 1;
	Users userlist;
	String userId;
	String screteKey;
	byte[] profilePicBytes;
	String type;
	CustomSpinnerDialog dialog;
	RelativeLayout pushnotify;
	EditText emailId;
	EditText password;
	EditText confirm;
	EditText aboutMe;
	GetUserDetails userdetail;
	ExpandableHeightGridView GridGallery;
	ArrayList< FeaturedMediaListData> mediaList = new ArrayList<FeaturedMediaListData>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.account_settings, container, false);
		
		return view;
	}
	 @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 
		super.onActivityCreated(savedInstanceState);
		screteKey = Utils.getDataString(getActivity(),
				Constants.PREFS_NAME, Constants.SP_SECRET_KEY);
 		userId = Utils.getDataString(getActivity(),
				Constants.PREFS_NAME, Constants.SP_USER_ID);
		Log.e("UserID1", ""+userId);
		dialog= new CustomSpinnerDialog(getActivity());
	    GridGallery= (ExpandableHeightGridView)getActivity().findViewById(R.id.accountMediagridview);
		initilizeUserDetails();
		
		//fetchUserDetails();
		
		ScrollView scrollView1 = (ScrollView) getActivity().findViewById(R.id.scrollView1);
		scrollView1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					System.out.println("ONNNN CLICK");
					hideKeyboard();
				}
				return false;
			}
		});

		onpostExecution();
		
	  }
	 
	 private void hideKeyboard()
		{
			try
			{
				InputMethodManager inputMethodManager =(InputMethodManager) 
						 getActivity().getSystemService( getActivity().INPUT_METHOD_SERVICE);

				inputMethodManager.hideSoftInputFromWindow( getActivity().getCurrentFocus().getWindowToken(), 0);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	 private void onpostExecution() {
		// TODO Auto-generated method stub
    mediaList.clear();
    Gson gson =new Gson();
    JSONObject userJson =  BrainSlamToggleMenu.UserDtailsJsonObject;
    org.json.JSONObject jsonDataObject;
	UserDetails userDataObject = new UserDetails();
	try {
		jsonDataObject = userJson.getJSONObject("data");
	
	JSONObject jsonUserObject=jsonDataObject.getJSONObject("User_Details");
	JSONObject jsonMediaObject=jsonDataObject.getJSONObject("Media");
	JSONObject MediaData = new JSONObject(jsonMediaObject.toString());
	@SuppressWarnings("unused")
	org.json.JSONArray jsonCrewArray = MediaData.getJSONArray("data");
	FeaturedMediaListData[] mediaDataArr = gson.fromJson(
			jsonCrewArray.toString(),FeaturedMediaListData[].class);
	for (int i = 0; i < mediaDataArr.length; i++) {
		FeaturedMediaListData mediaObject = new FeaturedMediaListData();
		mediaObject.Average_IQ=mediaDataArr[i].Average_IQ;
		mediaObject.Categories=mediaDataArr[i].Categories;
		mediaObject.Created_At=mediaDataArr[i].Created_At;
		mediaObject.Data_Url=mediaDataArr[i].Data_Url;
		mediaObject.Description=mediaDataArr[i].Description;
		mediaObject.Download_Url=mediaDataArr[i].Download_Url;
		mediaObject.Duration=mediaDataArr[i].Duration;
		mediaObject.Height=mediaDataArr[i].Height;
		mediaObject.Media_Id=mediaDataArr[i].Media_Id;
		mediaObject.Media_Owner=mediaDataArr[i].Media_Owner;
		mediaObject.Media_Type=mediaDataArr[i].Media_Type;
		mediaObject.Name=mediaDataArr[i].Name;
		mediaObject.Number_Of_Ratings=mediaDataArr[i].Number_Of_Ratings;
		mediaObject.Searched_Category=mediaDataArr[i].Searched_Category;
		mediaObject.Tags=mediaDataArr[i].Tags;
		mediaObject.Thumbnail_Url=mediaDataArr[i].Thumbnail_Url;
		mediaObject.Tranding_Score=mediaDataArr[i].Tranding_Score;
		mediaObject.Updated_At=mediaDataArr[i].Updated_At;
		mediaObject.Views=mediaDataArr[i].Views;
		mediaObject.Width=mediaDataArr[i].Width;
		mediaList.add(mediaObject);
	}
	userDataObject=gson.fromJson(jsonUserObject.toString(),UserDetails.class);
	TextView friends = (TextView) getActivity().findViewById(R.id.mainscreen_friendscount);
	TextView crews = (TextView) getActivity().findViewById(R.id.mainscreen_crewscount);
	TextView request = (TextView) getActivity().findViewById(R.id.mainscreen_requestscount);
	ImageView profileImageId = (ImageView) getView().findViewById(R.id.accountsettingUserimage);
	ImageLoader profileimage = new ImageLoader(getActivity());
	Log.e("MY Usercount", ""+userDataObject.Friends_Count);
	friends.setText(String.valueOf(userDataObject.Friends_Count));
	crews.setText(String.valueOf(userDataObject.Crews_Count));
	request.setText(String.valueOf(userDataObject.Followers_Count));
	profileimage.DisplayImage("http://dev-kaltura.brain-slam.com/BrainSlam_API/"+userDataObject.Photo_Url,profileImageId);
	fetchUserVideos();
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}
	private void fetchUserVideos() {
		// TODO Auto-generated method stub
		/*final ArrayList< Integer> thumbnail = new ArrayList<Integer>();
		for(int i=0;i<6;i++)
		{
		thumbnail.add(R.drawable.bg_logo);
		}*/
		  
		 final AccountSettingVideosAdapter videoAdapt = new AccountSettingVideosAdapter(getActivity(),mediaList);
		 GridGallery.setAdapter(videoAdapt);
		 GridGallery.setExpanded(true);
		 TextView liveVideo=(TextView)getActivity().findViewById(R.id.livevideo);
		 liveVideo.setText("Live Videos("+mediaList.size()+")");
		
		 

		
	   //Live Videos Edit
			final TextView videoedit = (TextView)getActivity().findViewById(R.id.videoedit);
			videoedit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				//Toast.makeText(getActivity(), "click on edit",Toast.LENGTH_LONG).show();
				String getText=videoedit.getText().toString();
				if(getText.equals("Edit"))
				{
				videoedit.setText("Done");
				videoAdapt.updateView(1);
				}
				else
				{
			     videoedit.setText("Edit");
			     videoAdapt.updateView(0);
				}
				//videoAdapt.updateView(1);
				GridGallery.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View view, int position,
							long arg3) {
						// TODO Auto-generated method stub
						//delete media sever call
					    List<BasicNameValuePair> postparam = new ArrayList<BasicNameValuePair>();
                        postparam.add(new BasicNameValuePair("Secret_Key",screteKey));
						postparam.add(new BasicNameValuePair("User_Id",userId));
						postparam.add(new BasicNameValuePair("Media_Id",mediaList.get(position).Media_Id));
						postparam.add(new BasicNameValuePair("operation", "deleteMedia"));
						new AsyncHandler(getActivity(), Constants.SERVER_URL+"media.php?",
								UPDATEDETAILS, false,postparam).execute();
						mediaList.remove(position);
					    videoAdapt.notifyDataSetChanged();
					    
					}
				});
				}
			});
		
	}
	 
	private void fetchUserDetails() {
	  userdetail=new GetUserDetails();
	  userdetail.execute(); 
	}
	private void initilizeUserDetails()
	 {
		    TextView username =(TextView) getActivity().findViewById(R.id.accountsettingUsername);
			ImageView profileImageId = (ImageView) getView().findViewById(R.id.accountsettingUserimage);
			aboutMe =(EditText) getView().findViewById(R.id.aboutdescription);
		    //ImageLoader profileimage = new ImageLoader(getActivity());
			userlist = UserManager.getInstance(getActivity().getApplicationContext()).getUser(Integer.parseInt(userId));
			Log.e("Username1", userlist.name+"::"+userlist.photoUrl);
			username.setText(userlist.name);
			//Log.e("About User1", ""+userlist.aboutUser);
			if(userlist.aboutUser.equalsIgnoreCase(""))
			aboutMe.setText("No Status Available");
			else
			aboutMe.setText(userlist.aboutUser);
			//Log.e("Url",userlist.photoUrl);
			//if(userlist.photoUrl==null)
			//profileImageId.setImageResource(R.drawable.profile);
			
			emailId = (EditText)getActivity().findViewById(R.id.email);
			password = (EditText)getActivity().findViewById(R.id.pass);
			confirm = (EditText)getActivity().findViewById(R.id.confirm);
			emailId.setText(userlist.emailId);
			emailId.setEnabled(false);
			password.setEnabled(false);
			confirm.setEnabled(false);
			aboutMe.setEnabled(false);
			
			//Validation Text Field
			emailId.setImeOptions(EditorInfo.IME_ACTION_NEXT);
			emailId.setOnEditorActionListener(new OnEditorActionListener() {

				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					// TODO Auto-generated method stub

					if(actionId == EditorInfo.IME_ACTION_NEXT)
					{
						String emailId1 = emailId.getText().toString().trim();

						if(emailId1.trim().length() == 0)
						{
							emailId.setError(Constants.BLANK_EMAIL_ID);
							return true;
						}
						else if(!Utils.isValidEmailId(emailId1))
						{
							emailId.setError(Constants.INVALID_EMAIL_ID);
							return true;
						}
					}

					return false;
				}
			});

			password.setImeOptions(EditorInfo.IME_ACTION_NEXT);
			password.setOnEditorActionListener(new OnEditorActionListener() {

				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					// TODO Auto-generated method stub
					if(actionId == EditorInfo.IME_ACTION_NEXT)
					{
						if(password.getText().toString().trim().length() != 0 && !Utils.isValidPassword(password.getText().toString().trim()))
						{
							password.setError(Constants.INVALID_PSWD);
							return true;
						}
						else if(password.getText().toString().trim().length() == 0)
						{
							password.setError(Constants.BLANK_PSWD);
							return true;
						}
					}
					return false;
				}
			});

			
			//Error Message when focus leave
			password.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					// TODO Auto-generated method stub
					if(password.getText().toString().trim().length() != 0 && !Utils.isValidPassword(password.getText().toString().trim()))
					{
						password.setError(Constants.INVALID_PSWD);
						
					}
					else if(password.getText().toString().trim().length() == 0)
					{
						password.setError(Constants.BLANK_PSWD);
						
					}
				}
			});
			
			
			confirm.setImeOptions(EditorInfo.IME_ACTION_DONE);
			confirm.setOnEditorActionListener(new OnEditorActionListener() {

				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					// TODO Auto-generated method stub

					if(actionId == EditorInfo.IME_ACTION_DONE)
					{
						if(confirm.getText().toString().trim().length() == 0)
						{
							confirm.setError(Constants.BLANK_PSWD);
							return true;
						}
						else if(!Utils.checkConfirmPswd(password.getText().toString(),confirm.getText().toString()))
						{
							confirm.setError("Password not matching");
							return true;
						}

					}
					return false;
				}
			});
			confirm.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					// TODO Auto-generated method stub
					if(confirm.getText().toString().trim().length() == 0)
					{
						confirm.setError(Constants.BLANK_PSWD);
						
					}
					else if(!Utils.checkConfirmPswd(password.getText().toString(),confirm.getText().toString()))
					{
						confirm.setError("Password not matching");
				    }
				}
			});
				
			//image Tab Event
		    profileImageId.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(userlist.facebookUID!=null)
					{
					//Toast.makeText(getActivity(), "on click",Toast.LENGTH_LONG).show();
					Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT,null);
					galleryIntent.setType("image/*");
					galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);

					Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 


					Intent chooser = new Intent(Intent.ACTION_CHOOSER);
					chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent);      
					chooser.putExtra(Intent.EXTRA_TITLE, "Select Image");

					Intent[] intentArray =  {cameraIntent}; 
					chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
					startActivityForResult(chooser,SELECT_IMAGE);
					}
				}
			});
		    
		    //Push Notification Events
		    pushnotify = (RelativeLayout)getActivity().findViewById(R.id.editnotification);
		    pushnotify.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent PushIntent =new Intent(getActivity(), FriendNotificationActivity.class);
					getActivity().startActivity(PushIntent);
				}
			});
		    
		    //Email password Edit tab event
		     final TextView emailEdit = (TextView)getActivity().findViewById(R.id.accountsettingedit);
			emailEdit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String getText=emailEdit.getText().toString();
					if(getText.equals("Edit"))
					{
					emailEdit.setText("Done");
					emailId.setEnabled(true);
					password.setEnabled(true);
					confirm.setEnabled(true);
					aboutMe.setBackgroundColor(Color.WHITE);
					aboutMe.setTextColor(Color.BLACK);
					aboutMe.setEnabled(true);
					}
					else
					{
						emailEdit.setText("Edit");
						password.setEnabled(false);
						confirm.setEnabled(false);
						aboutMe.setEnabled(false);
						emailId.setEnabled(false);
						
						//update user Details
						/*if(password.getText().toString().length()==0 && confirm.getText().toString().length()==0)
						{
							Toast.makeText(getActivity(), "Please Enter Valid Information",Toast.LENGTH_LONG).show();
						}
						else
						{*/
						List<BasicNameValuePair> postparam = new ArrayList<BasicNameValuePair>();

                        postparam.add(new BasicNameValuePair("Secret_Key",screteKey));
						postparam.add(new BasicNameValuePair("User_Id",userId));
						if(emailId.getText().toString().trim().length()==0)
						{
							Log.e("In If","EmailId");
							postparam.add(new BasicNameValuePair("Email_Id",userlist.emailId));
						}else
						{
							Log.e("In else","EmailId");
							postparam.add(new BasicNameValuePair("Email_Id",emailId.getText().toString().trim()));
						}
						postparam.add(new BasicNameValuePair("User_Name", userlist.name));
						
						/*if(password.getText().toString().trim().length()==0)
						{
							Log.e("In If","password");
							postparam.add(new BasicNameValuePair("Password",userlist.password));
						}
						else
						{*/
						postparam.add(new BasicNameValuePair("Password",password.getText().toString().trim()));
						postparam.add(new BasicNameValuePair("About_User", aboutMe.getText().toString().trim()));
						postparam.add(new BasicNameValuePair("Facebook_UID", userlist.facebookUID));
						postparam.add(new BasicNameValuePair("operation", "updateUserDetails"));
                        if(null != profilePicBytes)
						{

							System.out.println("BYTESSSS: "+profilePicBytes.length+" :: "+android.util.Base64.encodeToString(profilePicBytes,android.util.Base64.DEFAULT ));
							postparam.add(new BasicNameValuePair("Photo_Url", 
									android.util.Base64.encodeToString(profilePicBytes,android.util.Base64.DEFAULT )));
							postparam.add(new BasicNameValuePair("Photo_Extension", "jpg"));


						}

						new AsyncHandler(getActivity(), Constants.SERVER_URL+"user.php?",
								UPDATEDETAILS, false,postparam).execute();
						//}
					}
						
						
				}
			});
			//Delete account Tab Events
			Button deleteaccount = (Button)getActivity().findViewById(R.id.deleteaccount);
			deleteaccount.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "Work in progress",Toast.LENGTH_SHORT).show();
				}
			});
	 }
	 
	 @Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
			// TODO Auto-generated method stub
		 Log.e("onactivity", "Yes");
			super.onActivityResult(requestCode, resultCode, intent);
			
			Log.v("My accounts", "On activity result:: "+resultCode);
			ImageView profilePicture=(ImageView)getActivity().findViewById(R.id.accountsettingUserimage);
			if(requestCode == SELECT_IMAGE)
			{
				//get the image from gallery and set it to the profile pic image view;
				 Log.e("onactivity", "in if"+intent+" ::"+resultCode);
				Uri result = null;
				if(null != intent)
				{
					 Log.e("onactivity", "in intent not null");
					 System.out.println("INTNET::"+intent.getAction());

					if(intent.getAction()!=null && intent.getAction().toString().equals("inline-data"))
					{
						Bundle extras = intent.getExtras();
						Bitmap imageBitmap = (Bitmap) extras.get("data");

				
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.outHeight=150;
						options.outWidth=150;
						options.inSampleSize = getScale(50, 50);;

						Bitmap bitmap=null;
						bitmap=Bitmap.createScaledBitmap(imageBitmap, 100, 100, false);
						
                       Log.e("IN Intent", "Not Null");
						profilePicture.setImageBitmap(bitmap);
						//add_photo_textview.setVisibility(View.GONE);
						ByteArrayOutputStream output = new ByteArrayOutputStream();

						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);

						profilePicBytes = output.toByteArray();
						
						
					}

					else if(intent != null || requestCode== getActivity().RESULT_OK)
					{


                        Log.e("onactivity Result","else part");
						result = intent.getData();
						ContentResolver cR =getActivity().getContentResolver();
						type = cR.getType(result);

						System.out.println("result :::" + result.toString() + " type: "+intent.getType()+":"+type);

						BitmapFactory.Options options = new BitmapFactory.Options();
						options.outHeight=150;
						options.outWidth=150;
						options.inSampleSize = getScale(50, 50);;

						InputStream is;
						Bitmap bitmap=null;
						try {
							is = getActivity().getContentResolver().openInputStream(result);

							// decode original bitmap
							bitmap = BitmapFactory.decodeStream(is,null,options);
							is.close();

							// resize it to 100x100 size

							bitmap=Bitmap.createScaledBitmap(bitmap, 100, 100, false);
							 Log.e("IN Intent", "Null");

							profilePicture.setImageBitmap(bitmap);
							//add_photo_textview.setVisibility(View.GONE);
							ByteArrayOutputStream output = new ByteArrayOutputStream();

							bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);

							profilePicBytes = output.toByteArray();		

						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					System.out.println("in on activity result");				

				}
			}
			
		}
	 public int getScale(final int origX, final int origY){

			Log.d("qic upload", "origX::"+origX);
			Log.d("qic upload", "origY::"+origY);

			final int minDimension = (origX+origY)/2;
			final int scale = minDimension / 200;

			Log.d("qic upload", "scale::"+scale);

			return scale;
		}
	
private class GetUserDetails extends AsyncTask<Void, Void, UserDetails>
{

	@Override
	protected UserDetails doInBackground(Void... params) {
		Gson gson =new Gson();
		// TODO Auto-generated method stub
		mediaList.clear();
		List<BasicNameValuePair> params1= new ArrayList<BasicNameValuePair>();
		params1.add(new BasicNameValuePair("operation", "getUserDetails"));
		params1.add(new BasicNameValuePair("Secret_Key", screteKey));
		params1.add(new BasicNameValuePair("User_Id", userId));
		params1.add(new BasicNameValuePair("Details_Of", userId));
		params1.add(new BasicNameValuePair("Complete_Details",String.valueOf(YES)));
		JSONObject userJson = HttpCommunicator.callRsJson(Constants.SERVER_URL
				+ "user.php?",false, params1,getActivity());
		org.json.JSONObject jsonDataObject;
		UserDetails userDataObject = new UserDetails();
		try {
			jsonDataObject = userJson.getJSONObject("data");
		
		JSONObject jsonUserObject=jsonDataObject.getJSONObject("User_Details");
		JSONObject jsonMediaObject=jsonDataObject.getJSONObject("Media");
		JSONObject MediaData = new JSONObject(jsonMediaObject.toString());
		@SuppressWarnings("unused")
		org.json.JSONArray jsonCrewArray = MediaData.getJSONArray("data");
		FeaturedMediaListData[] mediaDataArr = gson.fromJson(
				jsonCrewArray.toString(),FeaturedMediaListData[].class);
		for (int i = 0; i < mediaDataArr.length; i++) {
			FeaturedMediaListData mediaObject = new FeaturedMediaListData();
			mediaObject.Average_IQ=mediaDataArr[i].Average_IQ;
			mediaObject.Categories=mediaDataArr[i].Categories;
			mediaObject.Created_At=mediaDataArr[i].Created_At;
			mediaObject.Data_Url=mediaDataArr[i].Data_Url;
			mediaObject.Description=mediaDataArr[i].Description;
			mediaObject.Download_Url=mediaDataArr[i].Download_Url;
			mediaObject.Duration=mediaDataArr[i].Duration;
			mediaObject.Height=mediaDataArr[i].Height;
			mediaObject.Media_Id=mediaDataArr[i].Media_Id;
			mediaObject.Media_Owner=mediaDataArr[i].Media_Owner;
			mediaObject.Media_Type=mediaDataArr[i].Media_Type;
			mediaObject.Name=mediaDataArr[i].Name;
			mediaObject.Number_Of_Ratings=mediaDataArr[i].Number_Of_Ratings;
			mediaObject.Searched_Category=mediaDataArr[i].Searched_Category;
			mediaObject.Tags=mediaDataArr[i].Tags;
			mediaObject.Thumbnail_Url=mediaDataArr[i].Thumbnail_Url;
			mediaObject.Tranding_Score=mediaDataArr[i].Tranding_Score;
			mediaObject.Updated_At=mediaDataArr[i].Updated_At;
			mediaObject.Views=mediaDataArr[i].Views;
			mediaObject.Width=mediaDataArr[i].Width;
			mediaList.add(mediaObject);
		}
		userDataObject=gson.fromJson(jsonUserObject.toString(),UserDetails.class);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    return userDataObject;
	}

	@Override
	protected void onPostExecute(UserDetails result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		dialog.dismiss();
		Log.e("MY UserID", ""+result.User_Id);
		TextView friends = (TextView) getActivity().findViewById(R.id.mainscreen_friendscount);
		TextView crews = (TextView) getActivity().findViewById(R.id.mainscreen_crewscount);
		TextView request = (TextView) getActivity().findViewById(R.id.mainscreen_requestscount);
		ImageView profileImageId = (ImageView) getView().findViewById(R.id.accountsettingUserimage);
		ImageLoader profileimage = new ImageLoader(getActivity());
		Log.e("MY Usercount", ""+result.Friends_Count);
		friends.setText(String.valueOf(result.Friends_Count));
		crews.setText(String.valueOf(result.Crews_Count));
		request.setText(String.valueOf(result.Followers_Count));
		//aboutMe.setText(String.valueOf(result.About_User));
		profileimage.DisplayImage("http://dev-kaltura.brain-slam.com/BrainSlam_API/"+result.Photo_Url,profileImageId);
		fetchUserVideos();
		//initilizeUserDetails();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
	  super.onPreExecute();
	 dialog.show();
	}
	
	}

@Override
public void onPause() {
	// TODO Auto-generated method stub
	Log.e("Myaccount", "onpause");
	super.onPause();
	//userdetail.cancel(true);
	//mediaList.clear();
}

@Override
public void onStop() {
	// TODO Auto-generated method stub
	super.onStop();
	Log.e("Myaccount", "onStop");
	MemoryCache memorycache = new MemoryCache();
	memorycache.clear();
	
}
@Override
public void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	Log.e("Myaccount", "onDestroy");

	
}
@Override
public void onReceive(JSONObject jsonObj, int id) {
	// TODO Auto-generated method stub
switch (id) {
case UPDATEDETAILS:
	Log.e("kiran", "" + jsonObj.toString());

	try {
		if (jsonObj.has("status") && jsonObj.getInt("status") == 0
				&& jsonObj.getInt("message") == 6) {
			Toast.makeText(getActivity(), "Successfully update",
					Toast.LENGTH_LONG).show();

		} else if (jsonObj.has("status") && jsonObj.getInt("status") == 1) {

			String msg = Utils
					.getResponseMessage(jsonObj.getInt("message"));
			Toast.makeText(getActivity(),msg, Toast.LENGTH_LONG)
					.show();

		}
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	
	break;

default:
	break;
}
	
}

}