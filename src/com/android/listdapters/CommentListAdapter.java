package com.android.listdapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.brainslam.R;
import com.android.utils.ImageLoader;


public class CommentListAdapter extends BaseAdapter{

	

	LayoutInflater inflater;
	View row;
	ArrayList<Map> list;
	Context context;
	public ImageLoader imageLoader;  
	static class ViewHolder
	{
		TextView tvcomment;//, messageTextView, timeTextView;
		ImageView pic1,pic2;
	}
	
	public CommentListAdapter(Context context, List<Map> list2)
	{
		this.context = context;
		this.list = (ArrayList<Map>) list2;
		 imageLoader=new ImageLoader(context);  
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
			 convertView = inflater.inflate(R.layout.commentlist_item ,parent, false);
		     holder = new ViewHolder();
		    
		     holder.tvcomment = (TextView)convertView.findViewById(R.id.tvcomment);
		 	 holder.pic1 = (ImageView) convertView.findViewById(R.id.imageView1);
		 	 holder.pic2 = (ImageView) convertView.findViewById(R.id.imageView2);
			  
				convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		 holder.tvcomment.setText(list.get(position).get("comment").toString());
		 holder.pic1.setBackgroundResource(R.drawable.user);
		 holder.pic2.setBackgroundResource(R.drawable.user);
		 
		 if ( position % 2 == 0 ){
			 holder.pic1.setVisibility(View.VISIBLE);  
			 holder.pic2.setVisibility(View.GONE);
			 
		 }else{
	    	  holder.pic1.setVisibility(View.GONE);
	    	  holder.pic2.setVisibility(View.VISIBLE);
	   }
	
		//imageLoader.DisplayImageAsSource(list.get(position).get("pic_url").toString(), holder.pic);
		
		
		Log.i("-comment--->>>","item--  "+list.get(position).get("comment").toString());
				return convertView;
	}

}
