package com.android.brainslam.constants;


public class Constants 
{
	final public static String URL="http://cdnbakmi.kaltura.com";
	//   http://10.0.0.201:8888/BrainSlam_API/media.php
	//   ?operation=getRelatedMedia&Media_Id=0_w1dwjv7h&User_Id=1&Secret_Key=2ca45896e7ed01b20cbbcb96e37460e4
	
	//final public static String LOCAL_SERVER_URL = "http://dev-kaltura.brain-slam.com/BrainSlam_API/";
	final public static String SERVER_URL = "http://dev-kaltura.brain-slam.com/BrainSlam_API/";
	final public static String FORGOT_PASSWORD_URL = "http://10.0.0.201:8888/BrainSlam_API/user.php?operation=forgotPassword";

	//http://10.0.0.201:8888/BrainSlam_API/user.php?operation=userLogin
	final public static String USER_LOGIN = "http://10.0.0.201:8888/BrainSlam_API/user.php?";
	final public static String REGISTERUSER = "http://10.0.0.201:8888/BrainSlam_API/user.php?operation=registerUser";
	final public static String BRAINSLAMSECRETKEY = "brainslam@application#key$123";
	
	final public static int LOGIN_ID = 1;
	final public static int SIGN_UP_ID = 1 + LOGIN_ID;
	final public static int FORGOT_PSWD_ID = 1 + SIGN_UP_ID;
	final public static int FACEBOOK_FRIENDS = 1 + FORGOT_PSWD_ID;
	final public static int DONE_CLICK = 1 + FACEBOOK_FRIENDS;
	public static String FACEBOOK_ACCESSTOKEN = "FACEBOOK_ACCESSTOKEN";
	public static final String PREFS_NAME = "BS_CREDENTIALS";
	public static final String SP_SECRET_KEY = "SECRET_KEY";
	public static final String IS_FACEBOOK_LOGIN = "IS_FACEBOOK_LOGIN";
	public static final String SP_USER_ID = "USER_ID";
	
	// validation messages
	public static String NO_RECORDS_TO_DISPLAY = "No records to display."; 
	public static String USER_ALREADY_EXISTS = "User already exists.";
	public static String BLANK_EMAIL_ID = "Email ID cannot be blank";
	public static String INVALID_EMAIL_ID = "Enter valid email ID";
	public static String INVALID_PSWD ="Enter valid password with atleast one uppercase, lower case and a number";
	public static String BLANK_PSWD = "Enter password";
	
	//Error messages
	public static String ERROR_CODE_1 = "Missing parameter";
	public static String ERROR_CODE_2 = "Your credentials are already in use";
	public static String ERROR_CODE_3 = "Please enter valid credentials";
	public static String ERROR_CODE_4 = "Request Failed";
	public static String ERROR_CODE_5 = "User already exist";
	public static String ERROR_CODE_7 = "Access Denied";
	// These Constant are used for CreateCrewActivity
	
	public static String SCREEN_TYPE = "SCREEN_TYPE";
	public static int PENDING_FRIEND_REQUESTS = 0;
	public static int PENDING_CREW_REQUESTS = 1;
	public static int FRIEND_REQUESTS = 2;
	
	public static int [][] RATINGS_ARR = {{1, 29}, {30, 59}, {60, 89}, {90, 119}, {120, 150}};
	
/*	public static String TWITTER_CONSUMER_KEY = "nTcagjv9eoFEy3FWYBshiA"; // place your
	public static String TWITTER_CONSUMER_SECRET = "8UPRlbRdeTaTP9KuLmyGOyk0S7xlt53vxE4Q9LBvB0"; // place
	public static String PREFERENCE_NAME = "twitter_oauth";
	public static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	public static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	public static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
	public static final String TWITTER_CALLBACK_URL = "BrainSlam://tnex.co.in";
	// Twitter oauth urls
	public static final String URL_TWITTER_AUTH = "auth_url";
	public static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	public static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";*/
	
	public static final int PAGE_SIZE = 5 ;

	
	public static String TWITTER_CONSUMER_KEY = "nTcagjv9eoFEy3FWYBshiA"; // place your
	public static String TWITTER_CONSUMER_SECRET = "8UPRlbRdeTaTP9KuLmyGOyk0S7xlt53vxE4Q9LBvB0"; // place
	public static String PREFERENCE_NAME = "twitter_oauth";
	public static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	public static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	public static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
	public static final String TWITTER_CALLBACK_URL = "BrainSlam://tnex.co.in";
	// Twitter oauth urls
	public static final String URL_TWITTER_AUTH = "auth_url";
	public static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	public static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

	public static final String PREFERENCES_NAME = "PUSH_NOTICATION";

	public static final String NEWUPLOADS ="NEWUPLOADS";

	public static final String NEWFOLLOWERS = "NEWFOLLOWERS";

	public static final String HOMESTREAMUPDATE = "HOMESTREAMUPDATE";

	public static final String NEWFEATUREVIDEOS ="NEWFEATUREVIDEOS";

	public static final String NEWMESSAGE = "NEWMESSAGE";

	public static final String CREWACTIVITYUPDATES = "CREWACTIVITYUPDATES";

	public static final String NEWCREWFOLLOWERS = "NEWCREWFOLLOWERS";

	public static final String FRIENDREQUEST ="FRIENDREQUEST";

	public static final String NEWPLAYLISTSUBSCRIBERS =" NEWPLAYLISTSUBSCRIBERS";

	public static final String NEWPLAYLIST ="NEWPLAYLIST";

	public static final String PLAYLISTUPDATES = "PLAYLISTUPDATES";

	
}
