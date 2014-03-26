package com.android.brainslam;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.brainslam.NetworkHandler.AsyncCallBack;
import com.android.brainslam.NetworkHandler.AsyncHandler;
import com.android.brainslam.constants.Constants;
import com.android.brainslam.vo.PendingFriendReqVo;
import com.android.listdapters.RequestsListAdapter;
import com.android.utils.Utils;
import com.google.gson.Gson;

public class ListOfRequests extends Activity implements AsyncCallBack
{
	ListView requsetsList;
	String secretKey, userID;
	public final int PENDING_FRIEND_REQUESTS = 0;
	public final int PENDING_CREW_REQUESTS = 1;
	public final int ATTEND_USER_FRIEND_REQUESTS = 2;
	TextView tvNoReqFound;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.requests_list);
		requsetsList = (ListView) findViewById(R.id.requests_list); 
		tvNoReqFound = (TextView) findViewById(R.id.tv_no_pending_req_found);

		secretKey = Utils.getDataString(ListOfRequests.this,
				Constants.PREFS_NAME, Constants.SP_SECRET_KEY);
		userID = Utils.getDataString(ListOfRequests.this,
				Constants.PREFS_NAME, Constants.SP_USER_ID);

		TextView title = (TextView) findViewById(R.id.tv_request_title);

		int screenType = getIntent().getIntExtra(Constants.SCREEN_TYPE, -1);

		if(screenType == Constants.PENDING_FRIEND_REQUESTS)
		{
			title.setText("Pending friend requests");
			fetchPendingFriendRequests();
		}
		else if(screenType == Constants.PENDING_CREW_REQUESTS)
		{
			title.setText("Pending crew requests");
			fetchPendingCrewRequests();
		}

	}

	private void fetchPendingFriendRequests()
	{
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

		params.add(new BasicNameValuePair("operation", "getPendingFriendRequests"));
		params.add(new BasicNameValuePair("Secret_Key", secretKey));
		params.add(new BasicNameValuePair("User_Id", userID));

		Log.d("BSMA", "http://dev-kaltura.brain-slam.com/BrainSlam_API/user.php?"+Utils.printUrl(params));

		new AsyncHandler(ListOfRequests.this, Constants.SERVER_URL
				+ "user.php?",
				PENDING_FRIEND_REQUESTS, false, params).execute();
	}

	private void fetchPendingCrewRequests()
	{

		//		"http://10.0.0.201:8888/BrainSlam_API/crew.php?operation
		//		=getPendingCrewRequests&Secret_Key=2ca45896e7ed01b20cbbcb96e37460e4&User_Id=1
		//
		//		1. For specific crew requests
		//		&Crew_Id=2"


		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

		params.add(new BasicNameValuePair("operation", "getPendingCrewRequests"));
		params.add(new BasicNameValuePair("Secret_Key", secretKey));
		params.add(new BasicNameValuePair("User_Id", userID));

		Log.d("BSMA", "http://dev-kaltura.brain-slam.com/BrainSlam_API/crew.php?"+Utils.printUrl(params));

		new AsyncHandler(ListOfRequests.this, Constants.SERVER_URL
				+ "crew.php?",
				PENDING_FRIEND_REQUESTS, false, params).execute();
	}

	public void acceptRequestOnClick(View view)
	{
		PendingFriendReqVo friendReqVo = (PendingFriendReqVo) view.getTag();
		attendRequest(friendReqVo.User_Friend_Mappings_Id, true);
	}
	
	public void declineRequestOnClick(View view)
	{
		PendingFriendReqVo friendReqVo = (PendingFriendReqVo) view.getTag();
		attendRequest(friendReqVo.User_Friend_Mappings_Id, false);
	}
	
	private void attendRequest(int userFriendMappingsId, boolean isAccepted)
	{
		//	http://10.0.0.201:8888/BrainSlam_API/user.php?operation
//		=attendUserFriendRequests&Secret_Key=
//		2ca45896e7ed01b20cbbcb96e37460e4&User_Id=1&User_Friend_Mappings_Ids=[5,4]&User_Friend_Status=Active

		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

		params.add(new BasicNameValuePair("operation", "attendUserFriendRequests"));
		params.add(new BasicNameValuePair("Secret_Key", secretKey));
		params.add(new BasicNameValuePair("User_Id", userID));
		params.add(new BasicNameValuePair("User_Friend_Mappings_Ids", "["+userFriendMappingsId+"]"));
		params.add(new BasicNameValuePair("User_Friend_Status", isAccepted ? "Active" : "Denied"));
		 
		Log.d("BSMA", "http://dev-kaltura.brain-slam.com/BrainSlam_API/user.php?"+Utils.printUrl(params));

		new AsyncHandler(ListOfRequests.this, Constants.SERVER_URL
				+ "user.php?",
				ATTEND_USER_FRIEND_REQUESTS, false, params).execute();
	}
	
	@Override
	public void onReceive(JSONObject jsonObj, int id) 
	{
		Gson gson = new Gson();
		Log.d("LOR", "Pending requests response ::"+jsonObj.toString());

		try 
		{
			JSONObject data = new JSONObject(jsonObj.toString());

			switch (id)
			{
			case PENDING_FRIEND_REQUESTS:

				if(data.getString("status").equalsIgnoreCase("0"))
				{
					JSONArray jsonArray = data.getJSONArray("data");

					PendingFriendReqVo pendingFrndReq[] = gson.fromJson(jsonArray.toString(), PendingFriendReqVo[].class);

					ArrayList<PendingFriendReqVo> pendingReq = new ArrayList<PendingFriendReqVo>();

					for (int i = 0; i < pendingFrndReq.length; i++) 
					{
						PendingFriendReqVo requestsVo = new PendingFriendReqVo();

						requestsVo.Email_Id = pendingFrndReq[i].Email_Id;
						requestsVo.Photo_Url = pendingFrndReq[i].Photo_Url;
						requestsVo.User_Friend_Mappings_Id = pendingFrndReq[i].User_Friend_Mappings_Id;
						requestsVo.User_Id = pendingFrndReq[i].User_Id;
						requestsVo.User_Name = pendingFrndReq[i].User_Name;

						pendingReq.add(requestsVo);
					}

					RequestsListAdapter requestsListAdapter = new RequestsListAdapter
					(ListOfRequests.this, pendingReq);
					requsetsList.setAdapter(requestsListAdapter);
				}

				break;

			case PENDING_CREW_REQUESTS:
				break;
				
			case ATTEND_USER_FRIEND_REQUESTS:
				break;
			}
		}
		catch (JSONException e) 
		{
			requsetsList.setVisibility(View.GONE);
			tvNoReqFound.setVisibility(View.VISIBLE);

			e.printStackTrace();
		}

	}
}
