package com.android.brainslam;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.android.listdapters.CreateNewCrewAdapter;
import com.android.utils.PullToRefreshListView;
import com.android.utils.PullToRefreshListView.OnRefreshListener;


public class CreateNewCrewActivity extends Activity {
	private PullToRefreshListView listView;
	private CreateNewCrewAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createnewcrew);
		Log.e("listView","object not created yet");
		listView = (PullToRefreshListView) findViewById(R.id.createnewcrew_ListView);
		Log.e("listView","object created");
		listView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// Your code to refresh the list contents goes here

				// for example:
				// If this is a webservice call, it might be asynchronous so
				// you would have to call listView.onRefreshComplete(); when
				// the webservice returns the data
//				adapter.loadData();
				
				// Make sure you call listView.onRefreshComplete()
				// when the loading is done. This can be done from here or any
				// other place, like on a broadcast receive from your loading
				// service or the onPostExecute of your AsyncTask.

				// For the sake of this sample, the code will pause here to
				// force a delay when invoking the refresh
				listView.postDelayed(new Runnable() {

					
					@Override
					public void run() {
						listView.onRefreshComplete();
					}
				}, 2000);
			}
		});
		ArrayList<String> items;
		items = new ArrayList<String>();

		items.add("Ajax Amsterdam");
		items.add("Barcelona");
		items.add("Manchester United");
		items.add("Chelsea");
		items.add("Real Madrid");
		items.add("Bayern Munchen");
		items.add("Internazionale");
 		adapter = new CreateNewCrewAdapter(CreateNewCrewActivity.this,items) {};
		listView.setAdapter(adapter);
		
	}

}
