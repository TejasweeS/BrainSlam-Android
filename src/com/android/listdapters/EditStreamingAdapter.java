package com.android.listdapters;


import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.brainslam.R;
import com.android.brainslam.db.dao.Streams;

public class EditStreamingAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	List<Streams> streams;
	
	String [] titles= {
			"Popular","Lowest IQ","HieghestIQ","Username 1","sports",
			"Popular","Lowest IQ","HieghestIQ","Username 1","sports"

	};
	public EditStreamingAdapter(Context context, List<Streams> streams)
	{
		this.context = context;
		this.streams = streams;
		this.inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return streams.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	public class ViewHolder
	{
		TextView tvTitle, tvSubTitle;
		ImageView ivStreamRemove;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) 
	{
		ViewHolder holder;	
		if(view == null)
		{
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.edit_mystream_listitem, parent,false);
			holder.tvTitle = (TextView) view.findViewById(R.id.stream_name);
//			holder.tvSubTitle = (TextView) view.findViewById(R.id.stream_subtitle);
			holder.ivStreamRemove = (ImageView) view.findViewById(R.id.stream_close);
			
			view.setTag(holder);
		}
		else
			holder = (ViewHolder)view.getTag();	

		holder.ivStreamRemove.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
//				HomeStreamManager.getInstance(context).removeStream(streams.get(position).HomeItemID);
//				streams = HomeStreamManager.getInstance(context).getHomeStream();
//				
//				notifyDataSetChanged();
//				
//				Log.d("ESA", "streams size::"+streams.size());

			}
		});
		
//		holder.tvTitle.setText(streams.get(position).HomeItemsName != null ?
//				streams.get(position).HomeItemsName : "Null");
//		holder.titleTextView.setText(titles[position]);
		return view;
	}

}
