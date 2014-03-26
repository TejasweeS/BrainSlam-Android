package com.android.listdapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.brainslam.R;
import com.android.brainslam.data.MessageData;


public class MessageListAdapter extends BaseAdapter{

	

	LayoutInflater inflater;
	View row;
	ArrayList<MessageData> list;
	Context context;
	static class ViewHolder
	{
		TextView titleTextView, messageTextView, timeTextView;
	}
	
	public MessageListAdapter(Context context, ArrayList<MessageData> list)
	{
		this.context = context;
		this.list = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		System.out.println("list count of msg list adapter: "+list.size());
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
     
		final ViewHolder holder;
		inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.messages_list_row ,parent, false);
		     holder = new ViewHolder();
		     holder.messageTextView = (TextView)convertView.findViewById(R.id.message_details_textView);
		     holder.titleTextView = (TextView)convertView.findViewById(R.id.message_title_textview);
		     holder.timeTextView = (TextView)convertView.findViewById(R.id.message_time_textView);
		     
		  
				convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		holder.titleTextView.setText(list.get(position).getMessageTitle());
		holder.timeTextView.setText(list.get(position).getMessageTime());
		holder.messageTextView.setText(list.get(position).getMessage());
		
		
				return convertView;
	}









}
