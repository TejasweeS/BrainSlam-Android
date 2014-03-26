package com.android.listdapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.brainslam.R;
import com.android.brainslam.vo.CrewListData;

public class MainCrewStreamAdapter extends BaseAdapter {
	LayoutInflater inflater;
	View row;
	boolean adapterTypeDelete;
	private List<CrewListData> items = new ArrayList<CrewListData>();
	Context context;

	class ViewHolder {

		TextView crewTitleTextView;
		TextView countCrewTextView;
		CheckBox addCrew;

	}

	public MainCrewStreamAdapter(Context context, List<CrewListData> items) {
		// TODO Auto-generated constructor stub
		Log.e("in costructor", "getview");
		this.context = context;
		this.items = items;
		this.inflater = (LayoutInflater) context
		 .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
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
		// TODO Auto-generated method stub
		Log.e("is Empty", "getview");
		ViewHolder holder;
		// CrewListData record = (CrewListData) getItem(position);
		//LayoutInflater inflater = ((Activity) context).getLayoutInflater();

		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.mainscreen_mycrewstream_listview,null);
			holder = new ViewHolder();
			holder.crewTitleTextView = (TextView) convertView
					.findViewById(R.id.crewname);
			holder.countCrewTextView = (TextView) convertView
					.findViewById(R.id.crew_membercount);
			holder.addCrew = (CheckBox) convertView
					.findViewById(R.id.mystreamcrewcheckBox);
			holder.crewTitleTextView.setText("No CrewList available");
			Log.e("is Empty", "Yes");
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Log.e("is Empty", "No");
		holder.crewTitleTextView.setText(items.get(position).Crew_Name);
		holder.countCrewTextView.setText("("
				+ items.get(position).Crew_Members_Count + ")");

		/*holder.crewTitleTextView.setText("kiran");
		holder.countCrewTextView.setText("10");*/
		return convertView;

	}

}