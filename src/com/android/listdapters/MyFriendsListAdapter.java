package com.android.listdapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.brainslam.R;
import com.android.brainslam.vo.MyFriendsVo;

public class MyFriendsListAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	Context context;
	ViewHolder holder = null;
	ArrayList<MyFriendsVo> friendsVos;
	
	public MyFriendsListAdapter(Context context, ArrayList<MyFriendsVo> friendsVos)
	{
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.friendsVos = friendsVos;
	}

	@Override
	public int getCount() 
	{
		return friendsVos.size();
	}

	@Override
	public MyFriendsVo getItem(int arg0) 
	{
		return friendsVos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) 
	{
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		if(convertView == null)
		{
			convertView = mInflater.inflate(R.layout.my_friends_list_row, null);

			holder = new ViewHolder();

			holder.userName = (TextView) convertView.findViewById(R.id.tv_user_name);
			holder.status = (TextView) convertView.findViewById(R.id.tv_user_status);
			holder.refresh = (ImageView) convertView.findViewById(R.id.iv_refresh);
			holder.userImg = (ImageView) convertView.findViewById(R.id.iv_user_img);
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		return convertView;
	}

	class ViewHolder
	{
		TextView userName, status;
		ImageView refresh, userImg;
	}
}
