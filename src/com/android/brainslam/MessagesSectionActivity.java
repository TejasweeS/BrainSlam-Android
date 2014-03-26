package com.android.brainslam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessagesSectionActivity extends Activity
{
//	 	ExpandableListAdapter listAdapter;
	    ExpandableListView expListView;
	    List<String> listDataHeader;
	    HashMap<String, List<String>> listDataChild;
	    TextView messagesView,notificationsView;
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_messages_section);
//	 
	        messagesView=(TextView) findViewById(R.id.textView_messages);
	         notificationsView=(TextView) findViewById(R.id.textView_notifications);
	        // tvfriends=(TextView) findViewById(R.id.activity_landing_bottombar_friends);
			
	        // get the listview
//	        expListView = (ExpandableListView) findViewById(R.id.lvExp);
//	 
//	        // preparing list data
//	        prepareListData();
//	 
////	        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
//	 
//	        // setting list adapter
////	        expListView.setAdapter(listAdapter);
////	        expListView.setGroupIndicator(null);
//	        
//	      
//
//	        expListView.expandGroup(0);
//	        expListView.expandGroup(1);
//	        
//	        expListView.setOnChildClickListener(new OnChildClickListener() {
//				
//				@Override
//				public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
//						int arg3, long arg4) {
//					
//			
//					Toast.makeText(MessagesSectionActivity.this,"->"+arg2+"-->"+arg3,1000).show();
//					
//					
////					Dialog d=new Dialog(Message_Sectoin_Activity.this,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
////					d.setContentView(R.layout.circular_sharingpopup);
////					d.show();
//					
////					startActivity(new Intent(Message_Sectoin_Activity.this,SharingCircularDialog.class));
//					
//		
//	        
//					return false;
//				}
//			});
	       
	    }
	 
	    public void tabClick(View v)
		{	

			switch (v.getId())
			{
			case R.id.textView_messages:
				notificationsView.setBackgroundColor(getResources().getColor(R.color.black));
				
				messagesView.setBackgroundColor(getResources().getColor(R.color.blue_kaltura));
				messagesView.setTextColor(getResources().getColor(R.color.white));
				notificationsView.setTextColor(getResources().getColor(R.color.blue_kaltura));
				
				Toast.makeText(getApplicationContext(), "MESSAGES", Toast.LENGTH_SHORT).show();
				
				//llrecomended.setVisibility(View.GONE);
				break;
			case R.id.textView_notifications:
				notificationsView.setBackgroundColor(getResources().getColor(R.color.blue_kaltura));
				
				messagesView.setBackgroundColor(getResources().getColor(R.color.black));
				notificationsView.setTextColor(getResources().getColor(R.color.white));
				messagesView.setTextColor(getResources().getColor(R.color.blue_kaltura));
				
				Toast.makeText(getApplicationContext(), "NOTIFICATIONS", Toast.LENGTH_SHORT).show();
				
				//llrecomended.setVisibility(View.VISIBLE);break;
			
			}



		}
	   
	     
	    private void prepareListData() {
	        listDataHeader = new ArrayList<String>();
	        listDataChild = new HashMap<String, List<String>>();
	 
	        // Adding child data
	        listDataHeader.add("Brain-Slam Messages");
	        listDataHeader.add("User Messages");
	      //  listDataHeader.add("Coming Soon..");
	 
	        // Adding child data
	        List<String> top250 = new ArrayList<String>();
	        top250.add("The Shawshank Redemption");
	        top250.add("The Godfather");
	        top250.add("The Godfather: Part II");
	        top250.add("Pulp Fiction");
	        top250.add("The Good, the Bad and the Ugly");
	        top250.add("The Dark Knight");
	        top250.add("12 Angry Men");
	 
	        List<String> nowShowing = new ArrayList<String>();
	        nowShowing.add("The Conjuring");
	        nowShowing.add("Despicable Me 2");
	        nowShowing.add("Turbo");
	        nowShowing.add("Grown Ups 2");
	        nowShowing.add("Red 2");
	        nowShowing.add("The Wolverine");
	 
//	        List<String> comingSoon = new ArrayList<String>();
//	        comingSoon.add("2 Guns");
//	        comingSoon.add("The Smurfs 2");
//	        comingSoon.add("The Spectacular Now");
//	        comingSoon.add("The Canyons");
//	        comingSoon.add("Europa Report");
	 
	        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
	        listDataChild.put(listDataHeader.get(1), nowShowing);
	       // listDataChild.put(listDataHeader.get(2), comingSoon);
	    }
	    
	    public void BackIconClick(View view)
		{
			Intent intent=new Intent(MessagesSectionActivity.this, BrainSlamMainActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.in_from_top,R.anim.fade_out);
		}
	}
