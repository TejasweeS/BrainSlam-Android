package com.android.listdapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.brainslam.R;
import com.android.brainslam.vo.MyFriendsVo;
import com.android.utils.ImageLoader;


public class LandingFriendsAdapter extends BaseAdapter{
	Context context;
	ArrayList<MyFriendsVo> list; 
	//commit
	public ImageLoader imageLoader;   // private static LayoutInflater inflater=null;
	public LandingFriendsAdapter(Context context,ArrayList<MyFriendsVo> list)
	{
		this.context=context;
		this.list=list;
	
	        
	        // Create ImageLoader object to download and show image in list
	        // Call ImageLoader constructor to initialize FileCache
	        imageLoader = new ImageLoader(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
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
			//holder.checkBox =(CheckBox)convertView.findViewById(R.id.category_isselected);
			convertView.setTag(holder);	
		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		holder.text.setText(list.get(position).getUser_Name());
		
		
		
		imageLoader.DisplayImage(list.get(position).getPhoto_Url(), holder.categoryImage);
		//	imageLoader.DisplayImage(data[position][3], holder.image4);
		
		
//		holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			public void onCheckedChanged(CompoundButton button, boolean value) {
//				list.get(position).isSelected=value;
//			}
//		});
//			holder.checkBox.setChecked(list.get(position).isSelected);
			

		return convertView;
	}


	class ViewHolder
	{
		TextView text;
		ImageView categoryImage;
		CheckBox checkBox;
	}

}