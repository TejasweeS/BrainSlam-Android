package com.android.listdapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.brainslam.R;
import com.android.brainslam.vo.FeaturedMediaListData;
import com.android.utils.ImageLoader;

public class AccountSettingVideosAdapter extends BaseAdapter{
	private int visibleFlag = 0;
	ArrayList<FeaturedMediaListData> thumbnailArray;
	Context context;
	AccountSettingVideosAdapter adapter;
	  public ImageLoader imageLoader; 
	public AccountSettingVideosAdapter(FragmentActivity activity,
			ArrayList<FeaturedMediaListData> thumbnail) {
		// TODO Auto-generated constructor stub
		context=activity;
		this.thumbnailArray=thumbnail;
		
		 imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return thumbnailArray.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return thumbnailArray.get(position);
	}

	@Override
	public long getItemId(int Id) {
		// TODO Auto-generated method stub
		return Id;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		/*ImageView imageView = new ImageView(context);
        imageView.setImageResource(thumbnail.get(position));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(70, 70));  */
		ViewHolder holder;
	    if(convertView==null)
	    {
	    	LayoutInflater inflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	        convertView = inflater.inflate(R.layout.image_ingridview, null);
            holder = new ViewHolder();
	        holder.thumbnail = (ImageView) convertView.findViewById(R.id.gridimage);
	        holder.closeButton=(ImageView) convertView.findViewById(R.id.closeButton);
	        convertView.setTag(holder);
	    }

	    else{
	        holder = (ViewHolder) convertView.getTag();
	    }
	    imageLoader.DisplayImage(thumbnailArray.get(position).Thumbnail_Url+"&width=500&height=500", holder.thumbnail);
	    if(thumbnailArray.get(position).Thumbnail_Url==null)
	    	Toast.makeText(context, "No Media", Toast.LENGTH_LONG).show();
	     if (visibleFlag==1) {
	    	holder.closeButton.setVisibility(View.VISIBLE);
		}else
		{
			holder.closeButton.setVisibility(View.INVISIBLE);
		}
	    
	    /*holder.closeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				thumbnailArray.remove(position);
				adapter.notifyDataSetChanged();
				
			}
		});*/

        return convertView;
		
	}
private class ViewHolder
{
	ImageView thumbnail;
	ImageView closeButton;
}
public void updateView(int i) {
	// TODO Auto-generated method stub
	 visibleFlag = i;
     notifyDataSetChanged();
}
}
