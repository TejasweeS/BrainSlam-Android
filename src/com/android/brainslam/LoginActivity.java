package com.android.brainslam;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.brainslam.NetworkHandler.AsyncCallBack;
import com.android.brainslam.NetworkHandler.AsyncHandler;
import com.android.brainslam.constants.Constants;
import com.android.brainslam.db.DataBaseHelper;
import com.android.brainslam.db.dao.Users;
import com.android.brainslam.manager.FriendManager;
import com.android.brainslam.manager.UserManager;
import com.android.utils.CustomAlertDialog;
import com.android.utils.CustomSpinnerDialog;
import com.android.utils.Utils;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;


public class LoginActivity extends Activity implements AsyncCallBack
{

	String TAG = "LoginActivity";
	CustomSpinnerDialog dialog;
	String userName, emailId;
	private UiLifecycleHelper uiHelper;
	public static GraphUser user;
	List<GraphUser> friendsList;
	LoginButton facebookLoginButton;
	TextView forgotPswdTextView;
	LinearLayout brainSlamLinearLayout;
	//	Dialog forgotPswdDialog;
	EditText emailIdEditText,pswdEditText;
	VideoView videoView;
	List<String> facebookFriendsId;
	//	boolean isFacebookLoggedin = false;
	ContextThemeWrapper con;
	String facebookUserEmailID;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);

		if(Utils.checkExternalStorageInstalled())
		{
			System.out.println("SWapnil::"+Environment.getExternalStorageDirectory());
		}
		DataBaseHelper.getInstance(getApplicationContext());

		con = new ContextThemeWrapper( this, R.style.alert);
		uiHelper = new UiLifecycleHelper(LoginActivity.this, callback);
		uiHelper.onCreate(savedInstanceState);
		dialog=new CustomSpinnerDialog(LoginActivity.this);
		facebookLoginButton = (LoginButton) findViewById(R.id.facebook_signin);
		videoView = (VideoView) findViewById(R.id.login_screen_video_view);

		friendsList = new ArrayList<GraphUser>();
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		videoView.setMediaController(null);
		Uri video = Uri.parse("android.resource://" + getPackageName() +"/"+ R.raw.movie);

		videoView.setVideoURI(video);
		videoView.setClickable(false);
		videoView.setFocusable(false);
		videoView.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.setLooping(true);
			}
		});

		boolean isLoggedin = Utils.getDataBoolean(LoginActivity.this, Constants.PREFS_NAME, Constants.IS_FACEBOOK_LOGIN);

		if(isLoggedin)
		{
			dialog.show();

		}

		videoView.start();


		videoView.setOnTouchListener(new OnTouchListener() {

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
		forgotPswdTextView = (TextView) findViewById(R.id.forgot_password);
		emailIdEditText = (EditText) findViewById(R.id.emialid);
		pswdEditText = (EditText) findViewById(R.id.password);
		//		brainSlamLinearLayout = (ImageView) findViewById(R.id.linearLayout1);
		forgotPswdTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("rutuja onclick");

				showForgotPswdDialog();
			}
		});

		forgotPswdTextView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					forgotPswdTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
				}
				else if(event.getAction() == MotionEvent.ACTION_UP)
				{
					forgotPswdTextView.setTextColor(getResources().getColor(android.R.color.white));
				}
				return false;
			}
		});

		emailIdEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		emailIdEditText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				String emailId = emailIdEditText.getText().toString().trim();
				if(actionId == EditorInfo.IME_ACTION_NEXT)
				{
					if(emailId.trim().length() == 0)
					{
						//					Toast.makeText(this, "Email ID cannot be blank", Toast.LENGTH_SHORT).show();
						emailIdEditText.setError(Constants.BLANK_EMAIL_ID);
						emailIdEditText.setNextFocusUpId(0);

						return true;
					}
					else if(!Utils.isValidEmailId(emailId))
					{
						//					Toast.makeText(this, "Enter valid Email ID", Toast.LENGTH_SHORT).show();
						emailIdEditText.setNextFocusUpId(0);
						emailIdEditText.setError(Constants.INVALID_EMAIL_ID );
						return true;
					}	
				}

				return false;
			}
		});


		pswdEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

		pswdEditText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if(pswdEditText.getText().toString().trim().length() != 0 && !Utils.isValidPassword(pswdEditText.getText().toString().trim()))
				{
					pswdEditText.setError(Constants.INVALID_PSWD);
					return true;
				}
				else if(pswdEditText.getText().toString().trim().length() == 0)
				{
					pswdEditText.setError(Constants.BLANK_PSWD);
					return true;
				}



				return false;
			}
		});


		facebookLoginButton.setReadPermissions("friends_about_me","email");
		facebookLoginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
			@Override
			public void onUserInfoFetched(GraphUser user) {


				LoginActivity.this.user = user;
				Log.v("LoginActivity","user setUserInfoChangedCallback");

				if(user != null)
				{
					Utils.storeData(LoginActivity.this, Constants.PREFS_NAME, Constants.IS_FACEBOOK_LOGIN, 
							true);

					System.out.println("FACEBOOK USERID: "+LoginActivity.user.getId());
					System.out.println("ACCESS TOKEn:: "+Utils.getDataString(LoginActivity.this, Constants.PREFS_NAME,Constants.FACEBOOK_ACCESSTOKEN));
					//post request for Graph API
					List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
					new AsyncHandler(LoginActivity.this,
							"https://graph.facebook.com/"+LoginActivity.user.getId()+"/friends?access_token="
									+Utils.getDataString(LoginActivity.this, Constants.PREFS_NAME,Constants.FACEBOOK_ACCESSTOKEN),
									Constants.FACEBOOK_FRIENDS,true,params).execute();    

				}
				else
					Log.v("LoginActivity","user null");
			}
		});

  //should remove the friends after logging out
     FriendManager.getInstance(getApplicationContext()).removeAllfrinends();



	}

	private void hideKeyboard()
	{
		try
		{
			InputMethodManager inputMethodManager = (InputMethodManager) 
					getSystemService(INPUT_METHOD_SERVICE);

			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public boolean validate()
	{
		String emailId = emailIdEditText.getText().toString().trim();

		if(emailId.trim().length() == 0)
		{
			//			Toast.makeText(this, "Email ID cannot be blank", Toast.LENGTH_SHORT).show();
			emailIdEditText.setError(Constants.BLANK_EMAIL_ID);
			emailIdEditText.requestFocus();
			return false;
		}
		else if(!Utils.isValidEmailId(emailId))
		{
			//			Toast.makeText(this, "Enter valid Email ID", Toast.LENGTH_SHORT).show();

			emailIdEditText.setError(Constants.INVALID_EMAIL_ID);
			emailIdEditText.requestFocus();
			return false;
		}
		else if(pswdEditText.getText().toString().trim().length() == 0)
		{
			pswdEditText.setError(Constants.BLANK_PSWD);
			pswdEditText.requestFocus();
			return false;
		}
		else if(pswdEditText.getText().toString().trim().length() != 0 && !Utils.isValidPassword(pswdEditText.getText().toString().trim()))
		{
			pswdEditText.setError(Constants.INVALID_PSWD);
			pswdEditText.requestFocus();
			return false;
		}
		else
		{
			return true;
		}
	}

	private void showForgotPswdDialog()
	{

		final AlertDialog.Builder alert = new AlertDialog.Builder(con);
		alert.setTitle("Forgot Password");
		TextView subTitleTextView = new TextView(this);
		subTitleTextView.setLayoutParams(new  LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		subTitleTextView.setText("  Enter The Email ID :");
		subTitleTextView.setGravity(Gravity.CENTER);
		final EditText editText = new EditText(this);

		editText.setHint("Email Id");
		editText.setPadding(15, 15, 15, 15); 
		editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		editText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(1);
		layout.addView(subTitleTextView);
		layout.addView(editText);

		alert.setView(layout);
		alert.setPositiveButton("Send",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();


				if(editText.getText().toString().trim().length() !=0 &&  Utils.isValidEmailId(editText.getText().toString().trim()))
				{
					params.add(new BasicNameValuePair("operation", "forgotPassword"));
					params.add(new BasicNameValuePair("Email_Id", editText.getText().toString().trim()));

					new AsyncHandler(LoginActivity.this,
							Constants.SERVER_URL + "user.php?"
							,
							Constants.FORGOT_PSWD_ID, false,params).execute();
				}
				else
				{
					Toast.makeText(LoginActivity.this, Constants.INVALID_EMAIL_ID, Toast.LENGTH_LONG).show();
					hideKeyboard();
				}
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});


		alert.show();
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		Log.v(TAG,"onresume");
		super.onResume();
		videoView.start();
		uiHelper.onResume();

		if(null != emailIdEditText &&
				null != pswdEditText)
		{
			emailIdEditText.setText("");
			pswdEditText.setText("");
		}


		//		//------------temporary
				emailIdEditText.setText("suhasg@tnex.co.in");
				pswdEditText.setText("Tnex@123");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
		//		videoView.stopPlayback();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
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

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (state.isOpened()) {

			Log.i(TAG, "Logged in..."+session.getAccessToken());

			Utils.storeData(LoginActivity.this, Constants.PREFS_NAME,Constants.FACEBOOK_ACCESSTOKEN, session.getAccessToken());


		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
			Utils.storeData(LoginActivity.this, Constants.PREFS_NAME, Constants.IS_FACEBOOK_LOGIN, 
					false);
		}
	}
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	public void onClick(View view)
	{

		switch (view.getId()) {
		case R.id.email_signup:
			Intent intent=new Intent(this, SignUpActivity.class);
			intent.putExtra("IS_FACEBOOK_LOGIN", false);
			startActivity(intent);
			overridePendingTransition(R.anim.slidein_right, R.anim.slideout_left);
			LoginActivity.this.finish();
			break;
		case R.id.login_btn:

			if(validate())
			{
				userLogin();
			}
			break;
		default:
			break;
		}
	}



	public void userLogin()
	{
		String passwordDigest = Utils.md5(pswdEditText.getText().toString().trim());
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		String deviceID = tm.getDeviceId(); 


		if(deviceID == null)
			deviceID = android.os.Build.SERIAL;
		String secretKey = Utils.md5(Constants.BRAINSLAMSECRETKEY+ deviceID);

		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("operation", "userLogin"));
		params.add(new BasicNameValuePair("Email_Id", emailIdEditText.getText().toString().trim()));
		params.add(new BasicNameValuePair("Open_Id", deviceID));
		params.add(new BasicNameValuePair("Password", passwordDigest));
		params.add(new BasicNameValuePair("Secret_Key", secretKey));

		new AsyncHandler(LoginActivity.this, Constants.SERVER_URL+
				"user.php?", Constants.LOGIN_ID, false,params)
		.execute();
	}


	@Override
	public void onReceive(JSONObject object, int id) {
		// TODO Auto-generated method stub

		switch(id)
		{
		case Constants.FORGOT_PSWD_ID:
		{
			try
			{
				if(object.has("status") && object.getInt("status")== 0 && object.getInt("message") == 6)
				{
					Toast.makeText(this, "Email has been sent", Toast.LENGTH_SHORT).show();
				}
				else if(object.has("errMessage"))
				{
					if(object.getString("errMessage").equalsIgnoreCase("No Internet Connection"))
						new CustomAlertDialog(LoginActivity.this,object.getString("errMessage")).showDialog();					
					else
						Toast.makeText(this,object.getString("errMessage"), Toast.LENGTH_SHORT).show();
				}
				else if(object.has("status") && object.getInt("status")== 1)
				{
					int code = object.getInt("message");


					if(Utils.getResponseMessage(code).length()!=0)
						Toast.makeText(LoginActivity.this,Utils.getResponseMessage(code), Toast.LENGTH_LONG).show();

					//					Toast.makeText(this, "userExist", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		break;

		case Constants.LOGIN_ID:
			try
			{
				if(object.has("status") && object.getInt("status") == 0 && object.getInt("message")== 6)
				{
					//					Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();

					System.out.println("Response::: "+object.toString());

					JSONObject data = object.getJSONObject("data");

					Users users = new Users();
					users.userId = data.getInt("User_Id");
					users.aboutUser = data.getString("About_User");
					users.appLoginSession = data.getString("App_Login_Session");
					users.facebookUID = data.getString("Facebook_UID");
					users.emailId = data.getString("Email_Id");
					users.isLoggedIn= true;
					users.name = data.getString("User_Name");
					users.mobileNumber = data.getString("Mobile_Number");
					users.photoUrl = data.getString("Photo_Url");


					// storing usre in data base
					UserManager.getInstance(getApplicationContext()).addUser(users);

					Utils.storeData(LoginActivity.this, Constants.PREFS_NAME, Constants.SP_SECRET_KEY, 
							data.getString("App_Login_Session"));

					Utils.storeData(LoginActivity.this, Constants.PREFS_NAME, Constants.SP_USER_ID, 
							data.getString("User_Id"));

					Intent intent=new Intent(LoginActivity.this, GuideActivity.class);

					intent.putExtra("ks", data.getString("App_Login_Session"));
					LoginActivity.this.startActivity(intent);
					LoginActivity.this.overridePendingTransition(R.anim.slidein_right, R.anim.slideout_left);

					LoginActivity.this.finish();
				}
				else if(object.has("errMessage"))
				{
					if(object.getString("errMessage").equalsIgnoreCase("No Internet Connection"))
						new CustomAlertDialog(LoginActivity.this,object.getString("errMessage")).showDialog();					
					else
						Toast.makeText(this,object.getString("errMessage"), Toast.LENGTH_SHORT).show();
				}
				else if(object.has("status") && object.getInt("status") == 1)
				{

					int code = object.getInt("message");

					if(code == 3 )
					{
						AlertDialog.Builder alert = new AlertDialog.Builder(con);
						alert.setTitle("Please register to login");
						alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								emailIdEditText.setText("");
								pswdEditText.setText("");
							}


						});
						alert.show();
					}
					else if( code == 8)
					{
						AlertDialog.Builder alert = new AlertDialog.Builder(con);
						alert.setTitle(Constants.ERROR_CODE_3);

						alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								//								emailIdEditText.setText("");
								//								pswdEditText.setText("");
							}


						});
						alert.show();
					}
					else if(Utils.getResponseMessage(code).length()!=0)
						Toast.makeText(LoginActivity.this,Utils.getResponseMessage(code), Toast.LENGTH_LONG).show();

				}
				else
					Toast.makeText(this, object.getString("message"), Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		case Constants.SIGN_UP_ID:
			try
			{
				//facebook login
				if((object.has("status") && object.has("status") && object.getInt("status") == 0))
				{

					System.out.println("Response::: "+object.toString());
					Intent intent=new Intent(LoginActivity.this, GuideActivity.class);
					intent.putExtra("IS_FACEBOOK_LOGIN", true);
					JSONObject data = object.getJSONObject("data");

					if(data != null)
					{


						Users users = new Users();
						users.userId = data.getInt("User_Id");
						users.aboutUser = data.getString("About_User");
						users.appLoginSession = data.getString("App_Login_Session");
						users.facebookUID = data.getString("Facebook_UID");
						users.emailId = data.getString("Email_Id");
						users.isLoggedIn= true;
						users.name = data.getString("User_Name");
						users.mobileNumber = data.getString("Mobile_Number");
						users.photoUrl = data.getString("Photo_Url");


						// storing usre in data base
						UserManager.getInstance(getApplicationContext()).addUser(users);

						Utils.storeData(LoginActivity.this, Constants.PREFS_NAME, Constants.SP_USER_ID, 
								data.getString("User_Id"));

						Utils.storeData(LoginActivity.this, Constants.PREFS_NAME, Constants.SP_SECRET_KEY, 
								data.getString("App_Login_Session"));

						intent.putExtra("ks", data.getString("App_Login_Session"));
						LoginActivity.this.startActivity(intent);
						LoginActivity.this.overridePendingTransition(R.anim.slidein_right, R.anim.slideout_left);
						LoginActivity.this.finish();

					}
					else
					{

						AlertDialog.Builder alert = new AlertDialog.Builder(con);
						alert.setTitle(Constants.USER_ALREADY_EXISTS);
						alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub

							}


						});
						alert.show();
					}
				}
				else if((object.has("status") && object.getInt("status") == 1))
				{

					int code = object.getInt("message");

					if(Utils.getResponseMessage(code).length()!=0)
						Toast.makeText(LoginActivity.this,Utils.getResponseMessage(code), Toast.LENGTH_LONG).show();


				}
				else if(object.has("errMessage"))
				{
					if(object.getString("errMessage").equalsIgnoreCase("No Internet Connection"))
						new CustomAlertDialog(LoginActivity.this,object.getString("errMessage")).showDialog();					
					else
						Toast.makeText(this,object.getString("errMessage"), Toast.LENGTH_SHORT).show();					
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case Constants.FACEBOOK_FRIENDS:

			Log.v(TAG, "FCAEBOOK FRIENDS RESPONSE:::: "+object.toString());
			if(object.has("errMessage"))
			{
				try {
					Toast.makeText(this,object.getString("errMessage"), Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{

				if(object.has("data"))
				{
					try {
						JSONArray array = object.getJSONArray("data");
						facebookFriendsId = new ArrayList<String>(); 

						for(int i=0;i<array.length();i++)
						{
							facebookFriendsId.add(array.getJSONObject(i).getString("id"));
							//				  	System.out.println(""+facebookFriendsId.get(i));
						}

						new GetFacebookProfileInfo().execute();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}

			}

			break;
		}
	}

	/** Async task for fetching user details of logged in user 
	 * */

	private class GetFacebookProfileInfo extends AsyncTask<Void, Void, Boolean> 
	{
		Bitmap mIcon1 = null;

		private String facebookUserid;
		@Override
		protected void onPreExecute() {
			
			if(!Utils.checkeInternetConnection(LoginActivity.this))
			{
				new CustomAlertDialog(LoginActivity.this,"No Internet Connection").showDialog();
				cancel(true);
			}
			else
			{
			if(!dialog.isShowing())
				dialog.show();
			}
			super.onPreExecute();

		}
		@SuppressWarnings("deprecation")
		@Override
		protected Boolean doInBackground(Void... params) {
			URL img_value = null;

			final ConnectivityManager connMgr = (ConnectivityManager) LoginActivity.this
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			final android.net.NetworkInfo wifi = connMgr
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			final android.net.NetworkInfo mobile = connMgr
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo activeConnection =   connMgr
					.getActiveNetworkInfo();

			if ((mobile==null && ! wifi.isAvailable())||(null == activeConnection  || !activeConnection.isConnected())) {
				// Do something
				//				Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();

				//			    this.cancel(true);
				Log.e("network error","No Internet Connection");
				return false; 


			}
			else
			{
				try {

					facebookUserid = LoginActivity.user.getId();
					String about_me = " ";
					Log.i("id.json ==>",""+facebookUserid);
					img_value = new URL("https://graph.facebook.com/"+facebookUserid+"/picture?type=large");
					JSONObject object = LoginActivity.user
							.getInnerJSONObject();
					try {
						mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					try {
						facebookUserEmailID = object.getString("email");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						facebookUserEmailID = "";

					} 

					if(facebookUserEmailID.length() == 0)
					{
						try {
							facebookUserEmailID = object.getString("username")+"@facebook.com";
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Log.v("SignUpActivity", "Facebook ---------"+"   userName:: "+facebookUserEmailID+" about_me:: "+object); 

				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return true;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if(result)
			{
				StringBuilder builder = new StringBuilder();

				for(int i=0 ; i < facebookFriendsId.size() ; i++)
				{
					builder.append(facebookFriendsId.get(i));

					if(i != facebookFriendsId.size() - 1)
						builder.append(",");
				}

				Log.v(TAG,"FACEBOOK FRIENDLIST:: "+builder.toString());

				TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
				String deviceID = tm.getDeviceId(); 
				if(deviceID == null)
					deviceID = android.os.Build.SERIAL;
				String secretKey = Utils.md5(Constants.BRAINSLAMSECRETKEY+ deviceID);

				//			if(facebookUserEmailID.equals(""))
				//				facebookUserEmailID = null;

				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("operation", "registerUser"));
				params.add(new BasicNameValuePair("Email_Id", facebookUserEmailID));
				params.add(new BasicNameValuePair("Open_Id", deviceID));
				params.add(new BasicNameValuePair("Facebook_UID", facebookUserid));
				params.add(new BasicNameValuePair("Secret_Key", secretKey));
				params.add(new BasicNameValuePair("Photo_Url", "http://graph.facebook.com/"
						+ facebookUserid + "/picture?type=large"));

				//			new AsyncHandler(LoginActivity.this, Constants.REGISTERUSER
				//					+ "&Email_Id=" + facebookUserEmailID + "&Facebook_UID="
				//					+ facebookUserid + "&Open_Id=" + deviceID + "&Secret_Key="
				//					+ secretKey + "&Photo_Url=http://graph.facebook.com/"
				//					+ facebookUserid
				//					+ "/picture?type=large&Facebook_Friend_UID="+builder.toString(),
				//					Constants.SIGN_UP_ID, false,params).execute();



				new AsyncHandler(LoginActivity.this, Constants.SERVER_URL+"user.php?",
						Constants.SIGN_UP_ID, false,params).execute();
				//			intent.putExtra("ks", object.getString("App_Login_Session"));

			}
			else
				new CustomAlertDialog(LoginActivity.this,"No Internet Connection").showDialog();

			super.onPostExecute(result);
		}

	}

}
