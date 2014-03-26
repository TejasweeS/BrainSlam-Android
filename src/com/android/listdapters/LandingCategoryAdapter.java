package com.android.listdapters;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.brainslam.LandingActivity;
import com.android.brainslam.R;
import com.android.brainslam.db.dao.Category;
import com.android.brainslam.db.dao.MyFriends;
import com.android.brainslam.manager.CategoryManager;
import com.android.brainslam.manager.FriendManager;
import com.android.utils.ImageLoader;


public class LandingCategoryAdapter extends BaseAdapter{
	Context context;
	ArrayList<Category> categoryList;
	ArrayList<MyFriends> friendList;
	//commit
	public ImageLoader imageLoader;   // private static LayoutInflater inflater=null;
	public LandingCategoryAdapter(Context context,ArrayList<Category> list,ArrayList<MyFriends> friendList)
	{
		this.context=context;
		this.categoryList=list;
		this.friendList=friendList;

		// Create ImageLoader object to download and show image in list
		// Call ImageLoader constructor to initialize FileCache
		imageLoader = new ImageLoader(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(friendList==null)
			return categoryList.size();
		else
			return categoryList.size()+friendList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(position<categoryList.size())
			return categoryList.get(position);
		else 
			return friendList.get(position-categoryList.size());
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		ViewHolder holder;

		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.landing_activity_category_listitem, null);
			holder.text = (TextView) convertView.findViewById(R.id.textView1);
			holder.categoryImage = (ImageView) convertView.findViewById(R.id.imageView1);
			holder.checkBox = (CheckBox)convertView.findViewById(R.id.checkBox1);
			holder.checkBox.setChecked(false);
			convertView.setTag(holder);	
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();

		}

		if(position<categoryList.size())
		{
			holder.text.setText(categoryList.get(position).categoryName );
			imageLoader.DisplayImage(categoryList.get(position).thumbnailUrl, holder.categoryImage);
			holder.checkBox.setTag(categoryList.get(position));
			if(categoryList.get(position).isOnHomePage)
			{
				//				holder.checkBox.setChecked(true);
				holder.checkBox.setChecked(categoryList.get(position).isOnHomePage);


				//				if(null != LandingActivity.hashSetRecommended)
				//				     LandingActivity.hashSetRecommended.add(""+(categoryList.get(position)).categoryId);
			}
		}
		else
		{
			holder.text.setText(friendList.get(position-categoryList.size()).name);
			imageLoader.DisplayImage(friendList.get(position-categoryList.size()).photoUrl, holder.categoryImage);
			holder.checkBox.setTag(friendList.get(position-categoryList.size()));
			//			if(null != LandingActivity.hashSetCategory)
			//			   LandingActivity.hashSetCategory.add(""+(friendList.get(position-categoryList.size()).getUser_Id()));

		}

		holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) 
			{
				Log.d("LCA", "here in onCheckedChanged::"+isChecked);

				if(position<categoryList.size())
					categoryList.get(position).isOnHomePage = isChecked;
				else
					friendList.get(position-categoryList.size()).isOnHomeScreen = isChecked;


				if(position <categoryList.size())
				{
					CategoryManager
					.getInstance(
							context.getApplicationContext())
							.updateIsOnHomeScreen(
									categoryList.get(position).categoryId,
									isChecked);
				}
				else 
				{
               Log.v("friends", "updating friends");
					FriendManager
					.getInstance(
							context.getApplicationContext())
							.updateIsOnHomeScreen(
									friendList.get(position
											- categoryList.size()).userId,
											isChecked);
				}
			}
		});

		return convertView;
	}


	class ViewHolder
	{
		TextView text;
		ImageView categoryImage;
		CheckBox checkBox;
	}

}