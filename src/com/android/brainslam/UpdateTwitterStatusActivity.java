package com.android.brainslam;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.utils.Utils;

public class UpdateTwitterStatusActivity extends Activity {
	EditText txtUpdate;
	Button bntUpdate;
	Button bntLogout;
	Button bntBack;
//	public SharedPreferences mSharedPreferences;
	
/*	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";*/
	
	
	ProgressDialog pDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_twitter_status);
		txtUpdate = (EditText) findViewById(R.id.txtupdatetwitterstatus);
		bntUpdate = (Button) findViewById(R.id.btnupdatetwitterstatus);
		bntLogout = (Button) findViewById(R.id.btnlogouttwitterstatus);
		bntBack = (Button) findViewById(R.id.btnBacktwitterstatus);
//		mSharedPreferences = getApplicationContext().getSharedPreferences(
//			"MyPref", 0);
		

		bntUpdate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String status = txtUpdate.getText().toString();

				// Check for blank text
				if (status.trim().length() > 0) {
					// update status
					new updateTwitterStatus().execute(status);
				} else {
					// EditText is empty
					Toast.makeText(getApplicationContext(),
							"Please enter status message", Toast.LENGTH_SHORT)
							.show();
				}

				

			}
		});
       bntLogout.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			logoutFromTwitter();
			Toast.makeText(getApplicationContext(), "Logout Successfully...", Toast.LENGTH_SHORT).show();
			
		}
	});
       
       bntBack.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
	});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update_twitter_status, menu);
		return true;
	}

	/**
	 * Function to update status
	 * */
	class updateTwitterStatus extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(UpdateTwitterStatusActivity.this);
			pDialog.setMessage("Updating to twitter...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {
			Log.d("Tweet Text", "> " + args[0]);
			String status = args[0];
			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				
				builder.setOAuthConsumerKey(com.android.brainslam.constants.Constants.TWITTER_CONSUMER_KEY);
				
				builder.setOAuthConsumerSecret(com.android.brainslam.constants.Constants.TWITTER_CONSUMER_SECRET);
				

				// Access Token
				String access_token = Utils.settings1.getString(
						com.android.brainslam.constants.Constants.PREF_KEY_OAUTH_TOKEN, "");
				// Access Token Secret
				String access_token_secret =Utils.settings1.getString(
						com.android.brainslam.constants.Constants.PREF_KEY_OAUTH_SECRET, "");

				AccessToken accessToken = new AccessToken(access_token,
						access_token_secret);
				Twitter twitter = new TwitterFactory(builder.build())
						.getInstance(accessToken);

				// Update status
				twitter4j.Status response = twitter.updateStatus(status);

				Log.d("Status", "> " + response.getText());
			} catch (TwitterException e) {
				// Error in updating status
				Log.d("Twitter Update Error", e.getMessage());
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog and show
		 * the data in UI Always use runOnUiThread(new Runnable()) to update UI
		 * from background thread, otherwise you will get error
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(),
							"Status tweeted successfully", Toast.LENGTH_SHORT)
							.show();
					// Clearing EditText field
					txtUpdate.setText("");
				}
			});
		}

	}


	/**
	 * Function to logout from twitter
	 * It will just clear the application shared preferences
	 * */
	private void logoutFromTwitter() {
		
		Utils.Logout();
		// Clear the shared preferences
//		Editor e = mSharedPreferences.edit();
//		e.remove(PREF_KEY_OAUTH_TOKEN);
//		e.remove(PREF_KEY_OAUTH_SECRET);
//		e.remove(PREF_KEY_TWITTER_LOGIN);
//		e.commit();
		finish();
		
       }

	
}
