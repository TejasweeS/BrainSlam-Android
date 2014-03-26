package com.android.listdapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.brainslam.R;
import com.android.brainslam.constants.Constants;
import com.android.brainslam.vo.PendingFriendReqVo;
import com.android.utils.ImageLoader;
import com.android.utils.Utils;

public class RequestsListAdapter  extends BaseAdapter
{
	Context context;
	ArrayList<PendingFriendReqVo> requestsList;
	public ImageLoader imageLoader;
	String secretKey, userID;
	public RequestsListAdapter(Context context, ArrayList<PendingFriendReqVo> requestsList)
	{
		this.context = context;
		this.requestsList = requestsList;
		imageLoader = new ImageLoader(context);


		secretKey = Utils.getDataString(context,
				Constants.PREFS_NAME, Constants.SP_SECRET_KEY);
		userID = Utils.getDataString(context,
				Constants.PREFS_NAME, Constants.SP_USER_ID);
	}

	@Override
	public int getCount() 
	{
		return requestsList.size();
	}

	@Override
	public Object getItem(int arg0) 
	{
		return requestsList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) 
	{
		return arg0;
	}

	class ViewHolder 
	{
		TextView tvRequestName;
		ImageView ivRequestImg;
		Button btnAccept, btnDecline;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup viewGrp) 
	{
		ViewHolder holder;

		Log.d("MSSLA", "getview position::"+position);

		if (convertView == null) 
		{
			LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			holder = new ViewHolder();

			convertView = inflater.inflate(R.layout.requests_list_row, null);

			holder.tvRequestName = (TextView) convertView.findViewById(R.id.tv_pending_req_name);
			holder.ivRequestImg = (ImageView) convertView.findViewById(R.id.iv_pending_req);
			holder.btnAccept = (Button) convertView.findViewById(R.id.btn_accept);
			holder.btnDecline = (Button) convertView.findViewById(R.id.btn_decline);
			
			holder.btnAccept.setTag(requestsList.get(position));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvRequestName.setText(requestsList.get(position).User_Name);

		if(requestsList.get(position).Photo_Url.length() > 0)
			imageLoader.DisplayImage(requestsList.get(position).Photo_Url, holder.ivRequestImg); 					

		return convertView;
	}

}