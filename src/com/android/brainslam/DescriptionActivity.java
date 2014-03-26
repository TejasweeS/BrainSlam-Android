package com.android.brainslam;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.android.brainslam.data.MessageData;
import com.android.listdapters.MessageDescriptionAdapter;
import com.android.listdapters.MessageListAdapter;

public class DescriptionActivity extends Activity implements OnClickListener {

	ListView message_main_list;
	

	String[] message = {
			"Brain slam message1 Brain slam message1 Brain slam message1 Brain slam message1",
			"Brain slam message2 Brain slam message2 Brain slam message2 Brain slam message2"
	};

	String[] title = {
			"new Comments 5","5 new ratings"	
	};

	String[] time = {
			"1.07PM","2.55AM"		
	};
	
	ArrayList<MessageData> arrayList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_list_common_layout);
		arrayList= new ArrayList<MessageData>();
		
        
        	for(int j=0;j<message.length;j++)
        		arrayList.add(new MessageData(title[j],time[j],message[j],0,false));
        
		
		message_main_list = (ListView) findViewById(R.id.message_main_list);
		LayoutInflater inflater = getLayoutInflater();
		
//		ViewGroup header = (ViewGroup)inflater.inflate(R.layout.main_message_header, message_main_list, false);
//		message_main_list.addHeaderView(header);
		MessageDescriptionAdapter adapter = new MessageDescriptionAdapter(this);
		adapter.addSection("Brain-Slam Messages ", new MessageListAdapter(this,arrayList)); 
		adapter.addSection("User Messages", new MessageListAdapter(this,arrayList)); 
		adapter.addSection("Group Messages", new MessageListAdapter(this,arrayList)); 
		message_main_list.setAdapter(adapter);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.description, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		
		}
	}

}
