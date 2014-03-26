package com.android.listdapters;

import java.util.ArrayList;
import java.util.List;

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
import com.android.brainslam.vo.CrewListData;

public class CreateCrewAdapter extends BaseAdapter {
	LayoutInflater inflater;
	View row;
	boolean adapterTypeDelete;
	private List<CrewListData> items = new ArrayList<CrewListData>();
	Context context;

	class ViewHolder {
		ImageView crewImage;
		TextView titleTextView;
		CheckBox addCrew;
		ImageView deletCrew;
	}

	public CreateCrewAdapter(Context context,List<CrewListData> items,boolean adapterType) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.adapterTypeDelete=adapterType;
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
	public void clear(int position) {

		items.remove(position);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		if(items.get(position).isVisible)
		{
			CrewListData record = (CrewListData) getItem(position);
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
				holder.addCrew = (CheckBox) convertView
				.findViewById(R.id.selectionBox);
				holder.deletCrew = (ImageView) convertView
				.findViewById(R.id.deleteCrew_btn);	
				if(adapterTypeDelete)
				{
					holder.addCrew.setVisibility(View.GONE);
					holder.deletCrew.setVisibility(View.VISIBLE);
				}

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.titleTextView.setText(record.Crew_Name);
			
			if(adapterTypeDelete)
			{
				holder.titleTextView.setText(record.Crew_Name);
				holder.addCrew.setTag(new Integer(position));
				holder.addCrew.setTag(items.get(position));
				holder.deletCrew.setTag(new Integer(position));
				holder.deletCrew.setTag(items.get(position));
				/*holder.crewImage1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				int position = (Integer) v.getTag();
	             deleteTask(position);
	             CreateCrewActivity.adapter.notifyDataSetChanged();

			}

			protected void deleteTask(int position) {
			    CreateCrewActivity.adapter.clear(position);
			    Toast.makeText(context, "Task Deleted", Toast.LENGTH_SHORT).show();
			    CreateCrewActivity.adapter.notifyDataSetChanged();

			    //testing  - problem cannot delete item. 
			}
		});*/

			}
		}
		return convertView;
	}
}