package com.android.brainslam.mainscreen;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.android.brainslam.AddOrDeleteCrewActivity;
import com.android.brainslam.ListOfRequests;
import com.android.brainslam.R;
import com.android.brainslam.NetworkHandler.HttpCommunicator;
import com.android.brainslam.constants.Constants;
import com.android.brainslam.db.dao.UserDetails;
import com.android.brainslam.vo.CrewListData;
import com.android.listdapters.MainCrewStreamAdapter;
import com.android.utils.CustomSpinnerDialog;
import com.android.utils.Utils;
import com.google.gson.Gson;

public class MyFriendsFragment extends Fragment implements OnClickListener
{
	private static final int YES  = 1;
	ListView myFriendsList;
	GetUserDetails userdetail;
	String userId;
	String screteKey;
	CustomSpinnerDialog dialog;
	TextView pendingFriends, tvPendingFriendRequests, tvPendingCrewReq;

	ArrayList<CrewListData> crewList = new ArrayList<CrewListData>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.mainscreen_crew_myscrew_stream, container, false);
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
		//fetchUserDetails();
		//tab add new crew button
		Button addNewCrew =(Button)getActivity().findViewById(R.id.mainscreen_crew_addnew_screw);
		addNewCrew.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				 
								Intent startAddActivity = new Intent(getActivity(),AddOrDeleteCrewActivity.class);
								startAddActivity.putExtra("Type", true);
							    getActivity().startActivity(startAddActivity);

				//Toast.makeText(getActivity(),"Work in progress",Toast.LENGTH_LONG).show();
			}
		});
		//friendRequestButton tab event
		Button friendRequestButton =(Button)getActivity().findViewById(R.id.mainscreen_crew_friend_request);
		friendRequestButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(),"Work in progress",Toast.LENGTH_LONG).show();
			}
		});
		// keyboard hide
		RelativeLayout relative = (RelativeLayout) getActivity().findViewById(R.id.mycrewrelative);
		relative.setOnTouchListener(new OnTouchListener() {

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

		initUI();
	}
	private void hideKeyboard()
	{
		try
		{
			InputMethodManager inputMethodManager = (InputMethodManager) 
			getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);

			inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void initUI() 
	{
		crewList.clear();
		Gson gson =new Gson();

		if(BrainSlamToggleMenu.UserDtailsJsonObject != null)
		{
			JSONObject userJson =  BrainSlamToggleMenu.UserDtailsJsonObject;
			org.json.JSONObject jsonDataObject;
			UserDetails userDataObject = new UserDetails();
			try {
				jsonDataObject = userJson.getJSONObject("data");

				JSONObject jsonUserObject=jsonDataObject.getJSONObject("User_Details");
				JSONObject jsonCrewObject=jsonDataObject.getJSONObject("Crews");
				JSONObject crewData = new JSONObject(jsonCrewObject.toString());
				@SuppressWarnings("unused")
				org.json.JSONArray jsonCrewArray = crewData.getJSONArray("data");
				CrewListData[] crewDataArr = gson.fromJson(
						jsonCrewArray.toString(),CrewListData[].class);
				for (int i = 0; i < crewDataArr.length; i++) {
					CrewListData crewObject = new CrewListData();
					crewObject.Crew_Id=crewDataArr[i].Crew_Id;
					crewObject.Crew_Name=crewDataArr[i].Crew_Name;
					crewObject.Crew_Members_Count=crewDataArr[i].Crew_Members_Count;
					crewObject.Crew_Logo=crewDataArr[i].Crew_Logo;
					crewObject.Home_Page_Item_Sequence=crewDataArr[i].Home_Page_Item_Sequence;
					crewObject.Added_Date=crewDataArr[i].Added_Date;
					crewList.add(crewObject);
				}
				userDataObject=gson.fromJson(jsonUserObject.toString(),UserDetails.class);
				TextView friends = (TextView) getActivity().findViewById(R.id.mainscreen_crewmystreamfriendscount);
				TextView crews = (TextView) getActivity().findViewById(R.id.mainscreen_crewmystreamcrewscount);
				TextView request = (TextView) getActivity().findViewById(R.id.mainscreen_crewmystreamrequestscount);
				pendingFriends = (TextView) getActivity().findViewById(R.id.pending_friend_requestcount);
				TextView pendingcrews = (TextView) getActivity().findViewById(R.id.pending_crew_requestcount);
				Log.e("MY Usercount in crew", ""+userDataObject.Friends_Count);
				friends.setText(String.valueOf(userDataObject.Friends_Count));
				crews.setText(String.valueOf(userDataObject.Crews_Count));
				request.setText(String.valueOf(userDataObject.Total_Requests));
				pendingFriends.setText(String.valueOf(userDataObject.Pending_Friends_Requests));
				pendingcrews.setText(String.valueOf(userDataObject.Pending_Crews_Requests));
				ListView crewListView = (ListView) getActivity().findViewById(R.id.my_crew_stream_listView);

				tvPendingFriendRequests = (TextView) getActivity().findViewById(R.id.pending_friend_request);
				tvPendingFriendRequests.setOnClickListener(MyFriendsFragment.this);

				tvPendingCrewReq = (TextView) getActivity().findViewById(R.id.pending_crew_requests);
				tvPendingCrewReq.setOnClickListener(MyFriendsFragment.this);

				CrewListData crew = new CrewListData();
				crew.Crew_Id=10;
				crew.Crew_Name="Tnex";
				crewList.add(crew);
				MainCrewStreamAdapter crewAdapter = new MainCrewStreamAdapter(getActivity(),crewList);
				crewListView.setAdapter(crewAdapter);
				Log.e("MY Usercount in crewList1", ""+crewList);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void fetchUserDetails() {
		// TODO Auto-generated method stub

		userdetail=new GetUserDetails();
		userdetail.execute(); 

	}

	private class GetUserDetails extends AsyncTask<Void, Void, UserDetails>
	{



		@Override
		protected UserDetails doInBackground(Void... params) {
			Gson gson =new Gson();
			// TODO Auto-generated method stub
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
				JSONObject jsonCrewObject=jsonDataObject.getJSONObject("Crews");
				JSONObject crewData = new JSONObject(jsonCrewObject.toString());
				@SuppressWarnings("unused")
				org.json.JSONArray jsonCrewArray = crewData.getJSONArray("data");
				CrewListData[] crewDataArr = gson.fromJson(
						jsonCrewArray.toString(),CrewListData[].class);
				for (int i = 0; i < crewDataArr.length; i++) {
					CrewListData crewObject = new CrewListData();
					crewObject.Crew_Id=crewDataArr[i].Crew_Id;
					crewObject.Crew_Name=crewDataArr[i].Crew_Name;
					crewObject.Crew_Members_Count=crewDataArr[i].Crew_Members_Count;
					crewObject.Crew_Logo=crewDataArr[i].Crew_Logo;
					crewObject.Home_Page_Item_Sequence=crewDataArr[i].Home_Page_Item_Sequence;
					crewObject.Added_Date=crewDataArr[i].Added_Date;
					crewList.add(crewObject);
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
			Log.e("MY UserID in crew", ""+result.User_Id);
			TextView friends = (TextView) getActivity().findViewById(R.id.mainscreen_crewmystreamfriendscount);
			TextView crews = (TextView) getActivity().findViewById(R.id.mainscreen_crewmystreamcrewscount);
			TextView request = (TextView) getActivity().findViewById(R.id.mainscreen_crewmystreamrequestscount);
			TextView pendingFriends = (TextView) getActivity().findViewById(R.id.pending_friend_requestcount);
			TextView pendingcrews = (TextView) getActivity().findViewById(R.id.pending_crew_requestcount);
			Log.e("MY Usercount in crew", ""+result.Friends_Count);
			friends.setText(String.valueOf(result.Friends_Count));
			crews.setText(String.valueOf(result.Crews_Count));
			request.setText(String.valueOf(result.Total_Requests));
			pendingFriends.setText(String.valueOf(result.Pending_Friends_Requests));
			pendingcrews.setText(String.valueOf(result.Pending_Crews_Requests));
			ListView crewListView = (ListView) getActivity().findViewById(R.id.my_crew_stream_listView);

			CrewListData crew = new CrewListData();
			crew.Crew_Id=10;
			crew.Crew_Name="Tnex";
			crewList.add(crew);
			MainCrewStreamAdapter crewAdapter = new MainCrewStreamAdapter(getActivity(),crewList);
			crewListView.setAdapter(crewAdapter);
			Log.e("MY Usercount in crewList1", ""+crewList);
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
		super.onPause();
		//userdetail.cancel(true);
	}

	@Override
	public void onClick(View v) 
	{
		Intent intent = new Intent(getActivity(), ListOfRequests.class);

		switch (v.getId()) 
		{
		case R.id.pending_friend_request:

			intent.putExtra(Constants.SCREEN_TYPE, Constants.PENDING_FRIEND_REQUESTS);
			startActivity(intent);

			break;

		case R.id.pending_crew_requests:

			intent.putExtra(Constants.SCREEN_TYPE, Constants.PENDING_CREW_REQUESTS);
			startActivity(intent);

			break;
		}

	}

}