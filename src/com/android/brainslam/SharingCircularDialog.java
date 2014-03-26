package com.android.brainslam;


import twitter4j.Twitter;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.brainslam.twitterconnect.AlertDialogManager;
import com.android.brainslam.twitterconnect.ConnectionDetector;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;


public class SharingCircularDialog extends Activity
{

	/*static String TWITTER_CONSUMER_KEY = "nTcagjv9eoFEy3FWYBshiA"; // place your
	static String TWITTER_CONSUMER_SECRET = "8UPRlbRdeTaTP9KuLmyGOyk0S7xlt53vxE4Q9LBvB0"; // place
	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
	static final String TWITTER_CALLBACK_URL = "BrainSlam://tnex.co.in";
	// Twitter oauth urls
	static final String URL_TWITTER_AUTH = "auth_url";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";*/
	// Progress dialog
	
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
	String status = "";
	String TAG = "SharingCircularDialog";


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.circular_sharingpopup);

	/*	For facebook sharing create a helper which will listen to 
		 * the callback*/
		 
		uiHelper = new UiLifecycleHelper(SharingCircularDialog.this, callback);
		uiHelper.onCreate(savedInstanceState);

		Session.StatusCallback callback1 = new Session.StatusCallback() {
			@Override
			public void call(final Session session, final SessionState state, final Exception exception) {
				onSessionStateChange(session, state, exception);
			}
		};

		twitterpreprocesser();
	
		
		
	}
	
	 public void twitterpreprocesser() 
	 {
//			cd = new ConnectionDetector(getApplicationContext());
//
//			// Check if Internet present
//			if (!cd.isConnectingToInternet()) {
//				// Internet Connection is not present
//				alert.showAlertDialog(SharingCircularDialog.this, "Internet Connection Error",
//						"Please connect to working Internet connection", false);
//				// stop executing code by return
//				return;
//			}
//
//			// Check if twitter keys are set
//			if (com.android.brainslam.constants.Constants.TWITTER_CONSUMER_KEY.trim().length() == 0
//					|| com.android.brainslam.constants.Constants.TWITTER_CONSUMER_SECRET.trim().length() == 0) {
//				// Internet Connection is not present
//				alert.showAlertDialog(SharingCircularDialog.this, "Twitter oAuth tokens",
//						"Please set your twitter oauth tokens first!", false);
//				// stop executing code by return
//				return;
//			}
//
//			mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
//
//
//			/*//**
//			 * This if conditions is tested once is redirected from twitter page.
//			 * Parse the uri to get oAuth Verifier
//			 * */
//			if (!isTwitterLoggedInAlready()) {
//				Log.e("oncreate", "in If");
//				Uri uri = getIntent().getData();
//				Log.e("uri", String.valueOf(uri));
//				if (uri != null && uri.toString().startsWith(com.android.brainslam.constants.Constants.TWITTER_CALLBACK_URL)) {
//					// oAuth verifier
//					String verifier = uri.getQueryParameter(com.android.brainslam.constants.Constants.URL_TWITTER_OAUTH_VERIFIER);
//					Log.e("uri", verifier);
//
//					try {
//						// Get the access token
//						AccessToken accessToken = twitter.getOAuthAccessToken(
//								requestToken, verifier);
//						Log.e("aceess Token", String.valueOf(accessToken));
//
//						// Shared Preferences
//						Editor e = mSharedPreferences.edit();
//
//						// After getting access token, access token secret
//						// store them in application preferences
//						e.putString(com.android.brainslam.constants.Constants.PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
//						e.putString(com.android.brainslam.constants.Constants.PREF_KEY_OAUTH_SECRET,
//								accessToken.getTokenSecret());
//						// Store login status - true
//						e.putBoolean(com.android.brainslam.constants.Constants.PREF_KEY_TWITTER_LOGIN, true);
//						e.commit(); // save changes
//
//						Log.e("Twitter OAuth Token", "> " + accessToken.getToken());
//
//						// Getting user details from twitter
//						// For now i am getting his name only
//						long userID = accessToken.getUserId();
//						User user1 = twitter.showUser(userID);
//						String username = user1.getName();
//
//						Intent statusUpdateActivity = new Intent(SharingCircularDialog.this,UpdateTwitterStatusActivity.class);
//						startActivity(statusUpdateActivity);
//
//
//					} catch (Exception e) {
//						// Check log for login errors
//						Log.e("Twitter Login Error", "> " + e.getMessage());
//					}
//				}
//			}
				
	}

	public void Googleclick(View v)
	{
		startActivity(new Intent(this,Share_Googleplus_Activity.class));

		//Toast.makeText(this,"Google + ",1500).show();
		
	}

	public void fbclick(View v1)
	{
		shareViaFacebook();
	}

	public void twitterclick(View v1)
	{

		//Toast.makeText(getApplicationContext(), "Processing...",Toast.LENGTH_SHORT).show();
		
		loginToTwitter();
		
		Log.w("login", "success");
//		if (isTwitterLoggedInAlready())
//		{
//			Intent statusUpdateActivity = new
//					Intent(SharingCircularDialog.this,UpdateTwitterStatusActivity.class);
//			startActivity(statusUpdateActivity);
//		}

	}

	public void msgclick(View v)
	{
//		startActivity(new Intent(this,Message_Activity.class));
	}

	public void emailclick(View v)
	{
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL, new String[]{ ""});
		//email.putExtra(Intent.EXTRA_CC, new String[]{ to});
		//email.putExtra(Intent.EXTRA_BCC, new String[]{to});
		email.putExtra(Intent.EXTRA_SUBJECT, "");
		email.putExtra(Intent.EXTRA_TEXT, "");

		email.setType("message/rfc822");
		startActivity(Intent.createChooser(email, "Choose an Email client :"));
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
	@Override
	protected void onResume() {

		super.onResume();

		uiHelper.onResume();
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
			Uri path = Uri.parse("android.resource://"+SharingCircularDialog.this.getPackageName()+"/"+R.drawable.ic_launcher);

			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
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
							SharingCircularDialog.this.finish();
						
							
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
//			Log.i("Session-->",""+Session.getActiveSession());
//			Log.i("Session-1->",""+status);
//
//						if(status.equals("loggedin"))
//						{
//							publishFeedDialog();
//						}
//						else
//						{
//							Dialog d=new Dialog(SharingCircularDialog.this);
//							d.setContentView(R.layout.dialogfb);
//							
//							
//							LoginButton facebookLoginButton=(LoginButton)d.findViewById(R.id.facebookLoginButton);
//							facebookLoginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
//								@Override
//								public void onUserInfoFetched(GraphUser user) {
//									//LoginActivity.this.user = user;
//			
//									Log.v("LoginActivity","user setUserInfoChangedCallback");
//			
//									if(user != null)
//									{
//										publishFeedDialog();
//									}
//									else
//										Log.v("LoginActivity","user null");
//								}
//							});	

		}

//			if(status.equals("loggedin"))
//			{
//				publishFeedDialog();
//				Toast.makeText(this,"LOGGED IN",1500).show();
//
//			}
//			else
//			{
//				Toast.makeText(this,"LOGGED OUT",1500).show();
//				Dialog d=new Dialog(SharingCircularDialog.this);
//				d.setContentView(R.layout.dialogfb);
//				LoginButton facebookLoginButton=(LoginButton)d.findViewById(R.id.facebookLoginButton);
//				facebookLoginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
//					@Override
//					public void onUserInfoFetched(GraphUser user) {
//						//LoginActivity.this.user = user;
//
//						Log.v("LoginActivity","user setUserInfoChangedCallback");
//
//						if(user != null)
//						{
//							Log.v("LoginActivity","user not null");
//							publishFeedDialog();
//
//
//
//						}
//						else
//						{	
//							Log.v("LoginActivity","user null");
//							publishFeedDialog();
//						}
//					}
//				});	
//				d.show();
//			}
//
//		}
		//				
	
	//	}

	/*private void publishFeedDialog() {
		Bundle params = new Bundle();
		//	    params.putString("name", "Facebook SDK for Android");
		//	    params.putString("caption", "Build great social apps and get more installs.");
		params.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
		//	    params.putString("link", "https://developers.facebook.com/android");
		params.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");
		WebDialog feedDialog = (
				new WebDialog.FeedDialogBuilder(SharingCircularDialog.this,
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
										Toast.makeText(SharingCircularDialog.this,
												"Posted story, id: "+postId,
												Toast.LENGTH_SHORT).show();
									} else {
										// User clicked the Cancel button
										Toast.makeText(SharingCircularDialog.this, 
												"Publish cancelled", 
												Toast.LENGTH_SHORT).show();
									}
								} else if (error instanceof FacebookOperationCanceledException) {
									// User clicked the "x" button
									Toast.makeText(SharingCircularDialog.this, 
											"Publish cancelled", 
											Toast.LENGTH_SHORT).show();
								} else {
									// Generic, ex: network error
									Toast.makeText(SharingCircularDialog.this, 
											"Error posting story", 
											Toast.LENGTH_SHORT).show();
								}
							}

						})
						.build();
		feedDialog.show();
	}
*/
	/**//**
	 * Function to login twitter
	 * *//*
*/	private void loginToTwitter() {
		// Check if already logged in
//		if (!isTwitterLoggedInAlready()) {
//			Log.w("After", "in If login twitter");
//			ConfigurationBuilder builder = new ConfigurationBuilder();
//			builder.setOAuthConsumerKey(com.android.brainslam.constants.Constants.TWITTER_CONSUMER_KEY);
//			builder.setOAuthConsumerSecret(com.android.brainslam.constants.Constants.TWITTER_CONSUMER_SECRET);
//			Configuration configuration = builder.build();
//			Log.w("After", "in If login twitter2");
//			final TwitterFactory factory = new TwitterFactory(configuration);
//
//			try {
//				Log.w("After", "in try login twitter3");
//				new Thread(new Runnable() {
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						twitter = factory.getInstance();
//						System.out.println(twitter);
//						Log.e("in run", "above token");
//
//						try {
//							requestToken = twitter
//									.getOAuthRequestToken(com.android.brainslam.constants.Constants.TWITTER_CALLBACK_URL);
//						} catch (TwitterException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						Log.e("in run", "below token");
//						System.out.println("Authencate uri"
//								+ requestToken.getAuthenticationURL());
//						boolean installed  =   appInstalledOrNot("com.twitter.android");  
//						if(installed) {
//							//This intent will help you to launch if the package is already installed
//							Intent LaunchIntent = getPackageManager()
//									.getLaunchIntentForPackage("com.twitter.android");
//							startActivity(LaunchIntent);
//
//							System.out.println("App already installed on your phone");        
//						}
//						else {
//							System.out.println("App is not installed on your phone");
//							startActivity(new Intent(Intent.ACTION_VIEW, Uri
//									.parse(requestToken.getAuthenticationURL())));
//						}
//						try {
//							getPackageManager().getPackageInfo("com.twitter.android", 0);
//							Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=USERID"));
//						    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						    startActivity(intent);
//						    } catch (NameNotFoundException e) {
//							// TODO Auto-generated catch block
//							startActivity(new Intent(Intent.ACTION_VIEW, Uri
//							.parse(requestToken.getAuthenticationURL())));
//						}
//
//					}
//				}).start();
//
//
//				//Log.e("After request:", String.valueOf(requestToken));
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		} else {
//			// user already logged into twitter
//			Toast.makeText(getApplicationContext(),
//					"Already Logged into twitter", Toast.LENGTH_LONG).show();
//		}
	}

	/**//**
	 * Check user already logged in your application using twitter Login flag is
	 * fetched from Shared Preferences
	 * *//*
*/
//private boolean isTwitterLoggedInAlready() {
//		// return twitter login status from Shared Preferences
////		return mSharedPreferences.getBoolean(com.android.brainslam.constants.Constants.PREF_KEY_TWITTER_LOGIN, false);
//	}



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


}
