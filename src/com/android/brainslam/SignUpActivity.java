package com.android.brainslam;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.android.brainslam.NetworkHandler.AsyncCallBack;
import com.android.brainslam.NetworkHandler.AsyncHandler;
import com.android.brainslam.constants.Constants;
import com.android.brainslam.db.dao.Users;
import com.android.brainslam.manager.UserManager;
import com.android.utils.CustomAlertDialog;
import com.android.utils.CustomSpinnerDialog;
import com.android.utils.Utils;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class SignUpActivity extends Activity implements OnClickListener, AsyncCallBack{

	CustomSpinnerDialog dialog;
	EditText emailIdEditText, pswdEditText, confPswdEditText;
	LinearLayout profilePicLayout;
	ImageView profilePicture;
	int SELECT_IMAGE = 1;
	String type;

	TextView add_photo_textview;
	boolean isFaceBookLogin = false;
	private UiLifecycleHelper uiHelper;

	String profileImageString = "";
	String facebookUserEmailID;
	String facebookUserid; 

	String TAG = "SignUpActivity";

	boolean isFacebookLoggedin = false;
	byte[] profilePicBytes;
	ContextThemeWrapper con;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		con = new ContextThemeWrapper( this, R.style.alert);
		Log.v(TAG, "oncreate");
		emailIdEditText = (EditText) findViewById(R.id.emialid);
		pswdEditText = (EditText) findViewById(R.id.password);
		confPswdEditText = (EditText) findViewById(R.id.confirmpwd);

		profilePicLayout = (LinearLayout) findViewById(R.id.profile_pic_layout);
		profilePicture = (ImageView) findViewById(R.id.profile_pic);
		profilePicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
		profilePicLayout.setOnClickListener(this);
		dialog=new CustomSpinnerDialog(SignUpActivity.this);
		Intent intent = getIntent();
		isFaceBookLogin = intent.getBooleanExtra("IS_FACEBOOK_LOGIN", false);
		add_photo_textview = (TextView) findViewById(R.id.add_photo_textview); 
		if(isFaceBookLogin)
		{
			add_photo_textview.setVisibility(View.GONE);
			pswdEditText.setEnabled(false);
			confPswdEditText.setEnabled(false);
			emailIdEditText.setEnabled(false);
			confPswdEditText.setText("");
			pswdEditText.setText("");
			new GetFacebookProfileInfo().execute();
		}
		else
		{
			add_photo_textview.setVisibility(View.VISIBLE);
			pswdEditText.setEnabled(true);
			confPswdEditText.setEnabled(true);
			emailIdEditText.setEnabled(true);
		}
		emailIdEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		emailIdEditText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub

				if(actionId == EditorInfo.IME_ACTION_NEXT)
				{
					String emailId = emailIdEditText.getText().toString().trim();

					if(emailId.trim().length() == 0)
					{
						emailIdEditText.setError(Constants.BLANK_EMAIL_ID);
						return true;
					}
					else if(!Utils.isValidEmailId(emailId))
					{
						emailIdEditText.setError(Constants.INVALID_EMAIL_ID);
						return true;
					}
				}

				return false;
			}
		});

		pswdEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		pswdEditText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if(actionId == EditorInfo.IME_ACTION_NEXT)
				{
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
				}
				return false;
			}
		});

		confPswdEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
		confPswdEditText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub

				if(actionId == EditorInfo.IME_ACTION_DONE)
				{
					if(confPswdEditText.getText().toString().trim().length() == 0)
					{
						confPswdEditText.setError(Constants.BLANK_PSWD);
						return true;
					}
					else if(!Utils.checkConfirmPswd(pswdEditText.getText().toString(),confPswdEditText.getText().toString()))
					{
						confPswdEditText.setError("Password not matching");
						return true;
					}

				}
				return false;
			}
		});


		LinearLayout linearlayout =  (LinearLayout) findViewById(R.id.linearlayout);
		linearlayout.setOnTouchListener(new OnTouchListener() {

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

		/*For facebook sharing create a helper which will listen to 
		 * the callback
		 */
		uiHelper = new UiLifecycleHelper(SignUpActivity.this, callback);
		uiHelper.onCreate(savedInstanceState);


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
			emailIdEditText.setError(Constants.INVALID_EMAIL_ID);
			emailIdEditText.requestFocus();
			return false;
		}
		else if(pswdEditText.getText().toString().trim().length() == 0 )
		{
			pswdEditText.setError(Constants.BLANK_PSWD);
			pswdEditText.requestFocus();
			return false;
		}
		else if (pswdEditText.getText().toString().trim().length() != 0  && !Utils.isValidPassword(pswdEditText.getText().toString().trim()))
		{
			pswdEditText.setError(Constants.INVALID_PSWD);
			pswdEditText.requestFocus();
			return false;
		}
		else if(confPswdEditText.getText().toString().trim().length() == 0)
		{
			confPswdEditText.setError(Constants.BLANK_PSWD);
			confPswdEditText.requestFocus();
			return false;	
		}
		else if (confPswdEditText.getText().toString().trim().length() != 0  && !Utils.isValidPassword(confPswdEditText.getText().toString().trim()))
		{
			confPswdEditText.setError(Constants.INVALID_PSWD);
			confPswdEditText.requestFocus();
			return false;
		}
		else if(!Utils.checkConfirmPswd(pswdEditText.getText().toString(),confPswdEditText.getText().toString()))
		{
			pswdEditText.setError("Enter valid Password");
			pswdEditText.requestFocus();
			return false;
		}
		else
		{
			return true;
		}
	}
	public void onClick(View view)
	{
		switch (view.getId()) {
		case R.id.sign_btn:

			if(isFaceBookLogin || validate())
			{
				userSignUp(); 
			}
			break;


		case R.id.profile_pic_layout:
		{

			Log.v(TAG, " profile pic layout click");
			if(!isFaceBookLogin)
			{/*
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				Intent intent1 = new Intent();
				intent1.setType("image/*");
				intent1.setAction(Intent.cAP);

				startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
			 */

				Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT,null);
				galleryIntent.setType("image/*");
				galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);

				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 


				Intent chooser = new Intent(Intent.ACTION_CHOOSER);
				chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent);      
				chooser.putExtra(Intent.EXTRA_TITLE, "title");

				Intent[] intentArray =  {cameraIntent}; 
				chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
				startActivityForResult(chooser,SELECT_IMAGE);

			}
		}
		break;
		}
	}





	public void userSignUp()
	{
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String deviceID = tm.getDeviceId(); 
		if(deviceID == null)
			deviceID = android.os.Build.SERIAL;
		String secretKey = Utils.md5(Constants.BRAINSLAMSECRETKEY+ deviceID);
		if(isFaceBookLogin)
		{

			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("operation", "registerUser"));
			params.add(new BasicNameValuePair("Email_Id", facebookUserEmailID));
			params.add(new BasicNameValuePair("Open_Id", deviceID));
			params.add(new BasicNameValuePair("Facebook_UID", facebookUserid));
			params.add(new BasicNameValuePair("Secret_Key", secretKey));
			params.add(new BasicNameValuePair("Photo_Url", "http://graph.facebook.com/"
					+ facebookUserid + "/picture?type=large"));

			new AsyncHandler(SignUpActivity.this, Constants.SERVER_URL+"user.php?",
					Constants.SIGN_UP_ID, false,params).execute();
		}
		else
		{

			List<BasicNameValuePair> postparam = new ArrayList<BasicNameValuePair>();



			postparam.add(new BasicNameValuePair("Email_Id", emailIdEditText.getText().toString().trim()));
			postparam.add(new BasicNameValuePair("Open_Id", deviceID));
			postparam.add(new BasicNameValuePair("Secret_Key",secretKey));
			postparam.add(new BasicNameValuePair("Password", pswdEditText.getText().toString().trim()));
			postparam.add(new BasicNameValuePair("operation", "registerUser"));


			if(null != profilePicBytes)
			{

				System.out.println("BYTESSSS: "+profilePicBytes.length+" :: "+android.util.Base64.encodeToString(profilePicBytes,android.util.Base64.DEFAULT ));
				postparam.add(new BasicNameValuePair("Photo_Url", 
						android.util.Base64.encodeToString(profilePicBytes,android.util.Base64.DEFAULT )));
				postparam.add(new BasicNameValuePair("Photo_Extension", "jpg"));


			}

			new AsyncHandler(SignUpActivity.this, Constants.SERVER_URL+"user.php?",
					Constants.SIGN_UP_ID, false,postparam).execute();
		}
	}

	/*	public int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}*/

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);

		Log.v(TAG, "on activity result "+requestCode +" resultCode::"+resultCode);

		if(requestCode == SELECT_IMAGE)
		{
			//get the image from gallery and set it to the profile pic image view;
			Uri result = null;
			if(null != intent)
			{
				System.out.println("INTNET::"+intent.getAction());

				if(intent.getAction()!=null && intent.getAction().toString().equals("inline-data"))
				{
					Bundle extras = intent.getExtras();
					Bitmap imageBitmap = (Bitmap) extras.get("data");

					
//					profilePicBytes = extras.get("data").toString().getBytes();		
//
//					profilePicture.setImageBitmap(imageBitmap);
//					add_photo_textview.setVisibility(View.GONE);
					

					BitmapFactory.Options options = new BitmapFactory.Options();
					options.outHeight=150;
					options.outWidth=150;
					options.inSampleSize = getScale(50, 50);;

					Bitmap bitmap=null;
					bitmap=Bitmap.createScaledBitmap(imageBitmap, 100, 100, false);

					profilePicture.setImageBitmap(bitmap);
					add_photo_textview.setVisibility(View.GONE);
					ByteArrayOutputStream output = new ByteArrayOutputStream();

					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);

					profilePicBytes = output.toByteArray();
					
					
				}

				else if(intent != null || resultCode == RESULT_OK)
				{



					result = intent.getData();
					ContentResolver cR = SignUpActivity.this.getContentResolver();
					type = cR.getType(result);

					System.out.println("result :::" + result.toString() + " type: "+intent.getType()+":"+type);

					BitmapFactory.Options options = new BitmapFactory.Options();
					options.outHeight=150;
					options.outWidth=150;
					options.inSampleSize = getScale(50, 50);;

					InputStream is;
					Bitmap bitmap=null;
					try {
						is = getContentResolver().openInputStream(result);

						// decode original bitmap
						bitmap = BitmapFactory.decodeStream(is,null,options);
						is.close();

						// resize it to 100x100 size

						bitmap=Bitmap.createScaledBitmap(bitmap, 100, 100, false);

						profilePicture.setImageBitmap(bitmap);
						add_photo_textview.setVisibility(View.GONE);
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

				//for facebook share dialog responses
				uiHelper.onActivityResult(requestCode, resultCode, intent, new FacebookDialog.Callback() {
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
		}


	}

	//	private class LoginAsycTask extends AsyncTask <ArrayList<String>, Void, String> 
	//	{
	//
	//		@Override
	//		protected void onPreExecute() {
	//			// TODO Auto-generated method stub
	//
	//			dialog.show();
	//			super.onPreExecute();
	//
	//		}
	//		@Override
	//		protected String doInBackground(ArrayList<String>... params) {
	//			// TODO Auto-generated method stub
	//			KalturaConfiguration config1 = new KalturaConfiguration();
	//			config1.setEndpoint("http://cdnbakmi.kaltura.com");
	//			final KalturaClient client = new KalturaClient(config1);
	//			try {
	//				//				params[0].toString();
	//				//				params[1].toString();
	//				Object result = client.getUserService().loginByLoginId(params[0].get(0), params[0].get(1));
	//				Log.w("Kiran:",String.valueOf(result));
	//				return String.valueOf(result);
	//			} catch (KalturaApiException e) {
	//				// TODO Auto-generated catch block
	//				e.printStackTrace();
	//			}
	//			return null;
	//		}
	//		@Override
	//		protected void onPostExecute(String result) {
	//			// TODO Auto-generated method stub
	//			dialog.dismiss();
	//			super.onPostExecute(result);
	//			if(result==null)
	//				Toast.makeText(SignUpActivity.this,"Invalid Credentials",Toast.LENGTH_LONG).show();
	//			else
	//			{
	//				Intent intent=new Intent(SignUpActivity.this, GuideActivity.class);
	//				intent.putExtra("ks", result);
	//				SignUpActivity.this.startActivity(intent);
	//				SignUpActivity.this.overridePendingTransition(R.anim.slidein_right, R.anim.slideout_left);
	//			}
	//
	//		}
	//	}

	public int getScale(final int origX, final int origY){

		Log.d("qic upload", "origX::"+origX);
		Log.d("qic upload", "origY::"+origY);

		final int minDimension = (origX+origY)/2;
		final int scale = minDimension / 200;

		Log.d("qic upload", "scale::"+scale);

		return scale;
	}



	/** Async task for fetching user details of logged in user 
	 * */

	private class GetFacebookProfileInfo extends AsyncTask<Void, Void, Void>
	{
		Bitmap mIcon1 = null;
		@Override
		protected void onPreExecute() {
			dialog.show();
			super.onPreExecute();

		}
		@SuppressWarnings("deprecation")
		@Override
		protected Void doInBackground(Void... params) {
			URL img_value = null;
			try {

				facebookUserid = LoginActivity.user.getId();
				String about_me = " ";
				Log.i("id.json ==>",""+facebookUserid);
				img_value = new URL("http://graph.facebook.com/"+facebookUserid+"/picture?type=large");
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
				} 

				Log.v("SignUpActivity", "Facebook "+"   userName:: "+facebookUserEmailID+" about_me:: "+object); 

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			LayoutParams params = profilePicture.getLayoutParams();
			Log.v("SignUpActivity","img width: "+params.width+" :: height "+params.height);
			params.width = LayoutParams.WRAP_CONTENT;
			params.height = LayoutParams.WRAP_CONTENT;
			profilePicture.setLayoutParams(params);
			profilePicture.setImageBitmap(mIcon1);
			emailIdEditText.setText(facebookUserEmailID);

			super.onPostExecute(result);
		}

	}	

	/*  FACEBOOK share code
	 * */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		Log.v(TAG,"onresume");
		super.onResume();

		uiHelper.onResume();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
		startActivity(intent);

		this.finish();
		super.onBackPressed();
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
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
			Log.i(TAG, "Logged in...");
			isFacebookLoggedin = true;
		} else if (state.isClosed()) {
			isFacebookLoggedin = false;

			Log.i(TAG, "Logged out...");
		}
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
			Uri path = Uri.parse("android.resource://"+SignUpActivity.this.getPackageName()+"/"+R.drawable.ic_launcher);

			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
			.setDescription("to be shared")
			.setPicture(path.getPath())
			.build();
			uiHelper.trackPendingDialogCall(shareDialog.present());
		}
		else
		{

			Log.v(TAG, "No Native App");
			/* if device dont have native application then app will use
			 * webdialog for sharing
			 * */
			publishFeedDialog();
		}

	}

	private void publishFeedDialog() {
		Bundle params = new Bundle();
		//	    params.putString("name", "Facebook SDK for Android");
		//	    params.putString("caption", "Build great social apps and get more installs.");
		params.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
		//	    params.putString("link", "https://developers.facebook.com/android");
		params.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

		WebDialog feedDialog = (
				new WebDialog.FeedDialogBuilder(SignUpActivity.this,
						Session.getActiveSession(),
						params))
						.setOnCompleteListener(new OnCompleteListener() {

							@Override
							public void onComplete(Bundle values,
									FacebookException error) {
								if (error == null) {
									// When the story is posted, echo the success
									// and the post Id.
									final String postId = values.getString("post_id");
									if (postId != null) {
										Toast.makeText(SignUpActivity.this,
												"Posted story, id: "+postId,
												Toast.LENGTH_SHORT).show();
									} else {
										// User clicked the Cancel button
										Toast.makeText(SignUpActivity.this, 
												"Publish cancelled", 
												Toast.LENGTH_SHORT).show();
									}
								} else if (error instanceof FacebookOperationCanceledException) {
									// User clicked the "x" button
									Toast.makeText(SignUpActivity.this, 
											"Publish cancelled", 
											Toast.LENGTH_SHORT).show();
								} else {
									// Generic, ex: network error
									Toast.makeText(SignUpActivity.this, 
											"Error posting story", 
											Toast.LENGTH_SHORT).show();
								}
							}

						})
						.build();
		feedDialog.show();
	}




	@Override
	public void onReceive(JSONObject object, int id) {
		// TODO Auto-generated method stub


		System.out.println("Response::: "+object.toString());
		switch (id) {
		case Constants.SIGN_UP_ID:
			try {
				if(object.has("message") && object.getInt("message") == 6 && object.getInt("status")== 0)
				{

					if(isFaceBookLogin)
					{
						TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

						String deviceID = tm.getDeviceId(); 
						if(deviceID == null)
							deviceID = android.os.Build.SERIAL;
						String secretKey = Utils.md5(Constants.BRAINSLAMSECRETKEY+ deviceID);


						List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
						params.add(new BasicNameValuePair("operation", "userLogin"));
						params.add(new BasicNameValuePair("Email_Id", emailIdEditText.getText().toString().trim()));
						params.add(new BasicNameValuePair("Open_Id", deviceID));
						params.add(new BasicNameValuePair("Secret_Key", secretKey));
						new AsyncHandler(
								SignUpActivity.this,
								Constants.SERVER_URL+"user.php?",
								Constants.LOGIN_ID, false, params).execute();

					}
					else
					{

						Log.v("rutuja", "signup by email");

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
						new AsyncHandler(
								SignUpActivity.this,
								Constants.SERVER_URL+"user.php?",
								Constants.LOGIN_ID, false, params).execute();
					}
				}
				else if(object.has("errMessage"))
				{
//					Toast.makeText(this,object.getString("errMessage"), Toast.LENGTH_LONG).show();
					
					if(object.getString("errMessage").equalsIgnoreCase("No Internet Connection"))
                        new CustomAlertDialog(SignUpActivity.this,object.getString("errMessage")).showDialog();					
					else
					Toast.makeText(this,object.getString("errMessage"), Toast.LENGTH_SHORT).show();
				}
				else if((object.has("status") && object.getInt("status") == 1))
				{

					if(object.has("message") && object.getInt("message") == 5)
					{
						AlertDialog.Builder alert = new AlertDialog.Builder(con);
						alert.setTitle("User already exist");
						alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								emailIdEditText.setText("");
								pswdEditText.setText("");
								confPswdEditText.setText("");
							}


						});
						alert.show();
					}
					else
					{
						int code = object.getInt("message");

						if(Utils.getResponseMessage(code).length()!=0)
							Toast.makeText(SignUpActivity.this,Utils.getResponseMessage(code), Toast.LENGTH_LONG).show();

					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		case Constants.LOGIN_ID:
			try {
				if(object.has("message") && object.getString("message").equals("6") && object.getInt("status")== 0)
				{

					System.out.println("Response::: "+object.toString());
					Intent intent=new Intent(SignUpActivity.this, GuideActivity.class);
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

					Utils.storeData(SignUpActivity.this, Constants.PREFS_NAME, Constants.SP_SECRET_KEY, 
							data.getString("App_Login_Session"));

					Utils.storeData(SignUpActivity.this, Constants.PREFS_NAME, Constants.SP_USER_ID, 
							data.getString("User_Id"));

					intent.putExtra("ks", data.getString("App_Login_Session"));
					SignUpActivity.this.startActivity(intent);
					SignUpActivity.this.overridePendingTransition(R.anim.slidein_right, R.anim.slideout_left);
					SignUpActivity.this.finish();
				}
				else if((object.has("status") && object.getInt("status") == 1))
				{

					int code = object.getInt("message");

					if(Utils.getResponseMessage(code).length()!=0)
						Toast.makeText(SignUpActivity.this,Utils.getResponseMessage(code), Toast.LENGTH_LONG).show();


				}
				else if(object.has("errMessage"))
				{
//					Toast.makeText(this,object.getString("errMessage"), Toast.LENGTH_LONG).show();
					if(object.getString("errMessage").equalsIgnoreCase("No Internet Connection"))
                        new CustomAlertDialog(SignUpActivity.this,object.getString("errMessage")).showDialog();					
					else
 					    Toast.makeText(this,object.getString("errMessage"), Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}
}
