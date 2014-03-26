package com.android.brainslam;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.android.brainslam.NetworkHandler.HttpCommunicator;
import com.android.brainslam.constants.Constants;
import com.android.brainslam.db.dao.Crews;
import com.android.brainslam.db.dao.MyFriends;
import com.android.utils.Utils;
import com.google.gson.JsonObject;


public class HandPickActivity extends Activity {
	/*	private PullToRefreshListView listView;
	 * 
	 * 
	private CreateNewCrewAdapter adapter;*/
	ArrayList<Crews> crewlist=new ArrayList<Crews>(); 
	ArrayList<MyFriends> friendlist=new ArrayList<MyFriends>(); 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createnewcrew);
		//		listView = (PullToRefreshListView) findViewById(R.id.createnewcrew_ListView);
		Log.e("listView","object created");
	}




	class HandPickAsycTask extends AsyncTask<Void, Integer, JsonObject>
	{

		@Override
		protected JsonObject doInBackground(Void... params) {
			// TODO Auto-generated method stub

			String secretKey = Utils.getDataString(HandPickActivity.this,
					Constants.PREFS_NAME, Constants.SP_SECRET_KEY);
			String userID = Utils.getDataString(HandPickActivity.this,
					Constants.PREFS_NAME, Constants.SP_USER_ID);

			List<BasicNameValuePair> requestParams = new ArrayList<BasicNameValuePair>();

			requestParams.add(new BasicNameValuePair("operation", "getAllCrew.List"));
			requestParams.add(new BasicNameValuePair("Secret_Key", secretKey));
			requestParams.add(new BasicNameValuePair("User_Id", userID));
			/*long unixTime = System.currentTimeMillis() / 1000L;
				requestParams.add(new BasicNameValuePair("Last_Modified",""+unixTime));*/
			requestParams.add(new BasicNameValuePair("By_User_Id", userID));
			requestParams.add(new BasicNameValuePair("pageSize", "30"));
			requestParams.add(new BasicNameValuePair("pageIndex", ""+1));
			JSONObject jsonResp = HttpCommunicator.callRsJson(Constants.SERVER_URL+"media.php?",false,requestParams,HandPickActivity.this);
//			System.out.println("R");
			

			return null;
		}

	}

}
