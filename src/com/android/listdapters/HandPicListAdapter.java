package com.android.listdapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.brainslam.R;
import com.android.brainslam.db.dao.Crews;
import com.android.brainslam.db.dao.MyFriends;

public class HandPicListAdapter extends BaseAdapter{

	
	List<Crews> crewList;
	List<MyFriends> friendList;
	Context context;
	public HandPicListAdapter(Context context,List<Crews> crewList2, List<MyFriends> friendList2)
	{
		this.crewList = crewList2;
		this.friendList = friendList2;
		this.context = context;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(friendList == null && crewList != null)
			return crewList.size();
		else if(crewList == null && friendList != null)
			return friendList.size();
		else if(crewList != null && friendList != null)
			return  crewList.size() + friendList.size();
		return 0;
		
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(position<crewList.size())
			return crewList.get(position);
		else 
			return friendList.get(position-crewList.size());
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	class ViewHolder
	{
		TextView text;
		ImageView friendImageView;
		CheckBox checkBox;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		ViewHolder holder;

		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.hand_pic_list_row, null);
			holder.text = (TextView) convertView.findViewById(R.id.textView1);
			holder.friendImageView = (ImageView) convertView.findViewById(R.id.friend_image_view);
			holder.checkBox = (CheckBox)convertView.findViewById(R.id.checkBox1);
			holder.checkBox.setChecked(false);
			convertView.setTag(holder);	
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();

		}
		
		
		
		if(position < crewList.size())
		{
			holder.text.setText(crewList.get(position).Crew_Name);
			holder.friendImageView.setVisibility(View.GONE);
		}
		else
		{
			holder.text.setText(friendList.get(position-crewList.size()).name);
			holder.friendImageView.setVisibility(View.VISIBLE);
		}
		
		
		
		
		
		
		holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return convertView;
	}

}
