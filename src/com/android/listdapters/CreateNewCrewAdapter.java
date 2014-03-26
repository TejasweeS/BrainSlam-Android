package com.android.listdapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.brainslam.R;

public class CreateNewCrewAdapter extends BaseAdapter {
	LayoutInflater inflater;
	View row;
	//ArrayList<NewCrew> list;
	private ArrayList<String> items = new ArrayList<String>();
	Context context;

 class ViewHolder {
		ImageView crewImage;
		TextView titleTextView;
		ImageView crewImage1;
	}
 
	public CreateNewCrewAdapter()
	{
		
	}

	public CreateNewCrewAdapter(Context context, ArrayList<String> items) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.items = items;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		String record = (String) getItem(position);
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		//inflater = (LayoutInflater) context
				//.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.createnewcrew_listviewitems, parent, false);
			holder = new ViewHolder();
			holder.crewImage = (ImageView) convertView
					.findViewById(R.id.createnewlistview_crewicon);
			holder.titleTextView = (TextView) convertView
					.findViewById(R.id.createnewlistview_name);
			holder.crewImage1 = (ImageView) convertView
					.findViewById(R.id.selectionBox);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.titleTextView.setText(record);

		return convertView;
	}

}
