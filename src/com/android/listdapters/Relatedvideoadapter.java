package com.android.listdapters;


import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.brainslam.R;
import com.android.brainslam.vo.BeanRelated;
import com.android.utils.ImageLoader;
import com.android.utils.Utils;

public class Relatedvideoadapter extends BaseAdapter
{
	List<BeanRelated> beanrelated;
	Context context;
	public ImageLoader imageLoader;
	
	public Relatedvideoadapter(Context context, List<BeanRelated> beanrelated)
	{
		Log.d("FeaturedListAdapter", "FeaturedListAdapter constructor");
		this.beanrelated = beanrelated;
		this.context = context;
		imageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		return beanrelated.size();
	}

	@Override
	public Object getItem(int position) {
		return beanrelated.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class ViewHolder
	{
		ImageView thumbnail, icon;
		TextView videoName, iqScore, userName;
		RatingBar vidRating;
	}

	ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		Log.d("FeaturedListAdapter", "getView()");
		
		if(convertView == null)
		{
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_video_grid, null);
			holder = new ViewHolder();

			holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
			holder.icon = (ImageView) convertView.findViewById(R.id.video_username_icon);
			holder.videoName = (TextView) convertView.findViewById(R.id.video_name);
			holder.iqScore = (TextView) convertView.findViewById(R.id.video_IQ_score_value);
			holder.userName = (TextView) convertView.findViewById(R.id.video_username);
			holder.vidRating = (RatingBar) convertView.findViewById(R.id.video_rating_bar);
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		//String ss=beanrelated.get(position).Thumbnail_Url;
		
		System.out.println("url==>"+beanrelated.get(position).Thumbnail_Url);
		
		imageLoader.DisplayImage(beanrelated.get(position).Thumbnail_Url, holder.thumbnail);
//		imageLoader.DisplayImage(beanrelated.get(position).Data_Url, holder.icon);
		
		//imageLoader.DisplayImage("http://6269-9001.zippykid.netdna-cdn.com/wp-content/uploads/2013/08/Amazing-Nature-Wallpaper.jpg",holder.thumbnail);
		//imageLoader.DisplayImage("http://s.ytimg.com/yts/img/favicon-vfldLzJxy.ico", holder.icon);
		
		holder.vidRating.setRating(Utils.getRating(Integer.parseInt(beanrelated.get(position).Average_IQ)));
		
		holder.videoName.setText(beanrelated.get(position).Name);
		holder.iqScore.setText(beanrelated.get(position).Average_IQ+"");
		holder.userName.setText(beanrelated.get(position).Media_Owner);
		
		return convertView;
	}
}
