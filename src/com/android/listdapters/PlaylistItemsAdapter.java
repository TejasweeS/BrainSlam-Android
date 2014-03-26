package com.android.listdapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.brainslam.R;
import com.android.utils.ImageLoader;

public class PlaylistItemsAdapter  extends BaseAdapter{

	LayoutInflater inflater;
	View row;
	ArrayList<HashMap<String, String>> list;
	Context context;
	public ImageLoader imageLoader;  
	static class ViewHolder
	{
		TextView tvtitle,usernm;//, messageTextView, timeTextView;
		//ImageView pic;
	}
	
	public PlaylistItemsAdapter(Context context, ArrayList<HashMap<String, String>> mylist)
	{
		this.context = context;
		this.list = (ArrayList<HashMap<String, String>>) mylist;
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
			 convertView = inflater.inflate(R.layout.playlist_item_listview ,parent, false);
		     holder = new ViewHolder();
		     holder.tvtitle = (TextView)convertView.findViewById(R.id.playlist_itemlistview_videotitle);
		     holder.usernm = (TextView)convertView.findViewById(R.id.playlist_items_usename);
		     
		 		convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tvtitle.setText(list.get(position).get("Name"));
		holder.usernm.setText(list.get(position).get("Media_Owner"));
				return convertView;
	}

}

