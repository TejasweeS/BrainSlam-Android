package com.android.listdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.brainslam.R;

public class MessageHeaderAdapter extends  BaseAdapter {

	Context context;
//	 String title;
	 String[] title = {
			 "Brain- Slam Messages",
			 "User Messages",
			 "Group Message"
			 
	 };
	 
	 int position;
	public MessageHeaderAdapter(Context context
			) {
		super();
		
		this.context = context;
		
//		this.title = title;
		// TODO Auto-generated constructor stub
	}

	
	 class ViewHolder
	{
	  	
		 TextView headerTitle;
		 ImageView closeImage;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		System.out.println("get vew header adapter"+position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		final ViewHolder holder;
		this.position = position;
		if(convertView == null)
		{
			
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.sub_msg_list_header, parent);
			
			holder.headerTitle = (TextView) convertView.findViewById(R.id.header_title);
			holder.closeImage = (ImageView) convertView.findViewById(R.id.message_close);
			
			convertView.setTag(holder);	
		}
		else
			 holder = (ViewHolder) convertView.getTag();
			 
		holder.headerTitle.setText(title[position]);
		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return title.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return this.position;
	}
	

}
