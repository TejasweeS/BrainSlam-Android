package com.android.brainslam.mainscreen;

import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.brainslam.LandingActivity;
import com.android.brainslam.R;
import com.android.brainslam.DnDList.DragNDropCursorAdapter;
import com.android.brainslam.DnDList.DragNDropListView;
import com.android.brainslam.DnDList.DragNDropListView.OnItemDragNDropListener;
import com.android.brainslam.db.dao.Streams;
import com.android.brainslam.manager.HomeStreamManager;
import com.android.listdapters.EditStreamingAdapter;

public class EditStreamingActivity extends Fragment implements OnClickListener {

	ListView streamList;
	EditStreamingAdapter adapter;

	TextView addNewStream;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.edit_mystream, container, false);
		init(view);

		return view;
	}

	public void onResume() 
	{
		super.onResume();
		Log.d("ESA", "in Onresume");
		
		cursor = HomeStreamManager.getInstance(
				getActivity().getApplicationContext()).getHomeStreamCursor();

		cursor.moveToFirst();
		while(cursor.moveToNext())
		{
			Log.d("ESA", "column HomeItemsName::"+cursor.getString(cursor.getColumnIndex("HomeItemsName")));			
		}
		cursor.moveToFirst();	
			
		// get cursor here from db
		final DragNDropCursorAdapter adapter = new DragNDropCursorAdapter(getActivity(),
				R.layout.edit_mystream_listitem,
				cursor,
				new String[]{"HomeItemsName"},
				new int[]{R.id.stream_name},
				R.id.iv_stream_handler);

		dndStreamList.setDragNDropAdapter(adapter);
		adapter.notifyDataSetChanged();
		
//		final List<Streams> streams = new ArrayList<Streams>();
//		
//		Cursor streamsCursor = cursor;
//		
//		streamsCursor.moveToFirst();
//		while(streamsCursor.moveToNext())
//		{
//			Log.d("ESA", "**column HomeItemsName::"+streamsCursor.getString(streamsCursor.getColumnIndex("HomeItemsName")));	
//			
//			Streams stream = new Streams();
//			
//			stream._id = streamsCursor.getInt(streamsCursor.getColumnIndex("_id"));
//			stream.HomeItemID = streamsCursor.getInt(streamsCursor.getColumnIndex("HomeItemID"));
//			stream.Count = streamsCursor.getInt(streamsCursor.getColumnIndex("Count"));
//			stream.HomeItemsName = streamsCursor.getString(streamsCursor.getColumnIndex("HomeItemsName"));
//			stream.HomeItemSequence = streamsCursor.getInt(streamsCursor.getColumnIndex("HomeItemSequence"));
//			stream.UserId = streamsCursor.getInt(streamsCursor.getColumnIndex("UserId"));
//			stream.HomeItemType = streamsCursor.getString(streamsCursor.getColumnIndex("HomeItemType"));
//			stream.LastModified = streamsCursor.getString(streamsCursor.getColumnIndex("LastModified"));
//			stream.HomeItemsStatus = streamsCursor.getString(streamsCursor.getColumnIndex("HomeItemsStatus"));
//			stream.UserHomeItemId = streamsCursor.getString(streamsCursor.getColumnIndex("UserHomeItemId"));
//			
//			streams.add(stream);
//		}
//		streamsCursor.moveToFirst();
		
		dndStreamList.setOnItemDragNDropListener(new OnItemDragNDropListener()
		{
			@Override
			public void onItemDrop(DragNDropListView parent, View view,
					int startPosition, int endPosition, long id) 
			{
				Log.d("ESA", "startPosition::"+startPosition+" endPosition::"+endPosition);
				
				List<Streams> str = HomeStreamManager.getInstance(
						getActivity().getApplicationContext()).getHomeStream();
				
				Log.d("ESA", "streams size::"+str.size());
				
				Streams stream = str.get(startPosition);
				str.remove(startPosition);
				
				
				Log.d("ESA", "X item removed::"+stream.HomeItemsName);
				
				if(endPosition > str.size())
					str.add(stream);
				else
					str.add(endPosition, stream);
				
				for (int i = 0; i < str.size(); i++) 
				{
					str.get(i).HomeItemSequence = i;
					Log.d("ESA", "after add HomeItemsName::"+str.get(i).HomeItemsName);
				}
				
				
				Log.d("ESA", "streams size::"+str.size());
				
//				str = HomeStreamManager.getInstance(
//						getActivity().getApplicationContext()).getHomeStream();
//				
//				for (int i = 0; i < str.size(); i++) 
//				{
//					Log.d("ESA", "prev HomeItemsName::"+str.get(i).HomeItemsName);
//				}
				//-------------
				HomeStreamManager.getInstance(
						getActivity().getApplicationContext()).saveStreams(str);
				//-------------
				List<Streams> str1 = HomeStreamManager.getInstance(
						getActivity().getApplicationContext()).getHomeStream();
				
				for (int i = 0; i < str1.size(); i++) 
				{
					Log.d("ESA", "--after HomeItemsName::"+str1.get(i).HomeItemsName+"  seq id::"+str1.get(i).HomeItemSequence);
				}
				 
			}
			
			@Override
			public void onItemDrag(DragNDropListView parent, View view, int position,
					long id) 
			{
				
			}
		});
	}
	
	List<Streams> list;
	public static DragNDropListView dndStreamList;
	Cursor cursor;
	public void init(View view) 
	{
		//		list = HomeStreamManager.getInstance(
		//				getActivity().getApplicationContext()).getHomeStream();

//		cursor = HomeStreamManager.getInstance(
//				getActivity().getApplicationContext()).getHomeStreamCursor();
//
//		Log.d("ESA", "column count::"+cursor.getCount());
//
////		for (int i = 0; i < cursor.getCount(); i++) 
////		{
////			Log.d("ESA", "column HomeItemsName::"+cursor.getString(cursor.getColumnIndex("HomeItemsName")));			
////		}
//
		dndStreamList = (DragNDropListView)view.findViewById(android.R.id.list);
//
//
//		// get cursor here from db
//		final DragNDropCursorAdapter adapter = new DragNDropCursorAdapter(getActivity(),
//				R.layout.edit_mystream_listitem,
//				cursor,
//				new String[]{"HomeItemsName"},
//				new int[]{R.id.stream_name},
//				R.id.iv_stream_handler);
//
//		dndStreamList.setDragNDropAdapter(adapter);


		//		streamList = (ListView) view.findViewById(R.id.streamList);
		//		adapter = new EditStreamingAdapter(this.getActivity(), list);
		//		streamList.setAdapter(adapter);

		addNewStream = (TextView) view.findViewById(R.id.addnew_stream);
		addNewStream.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) 
	{
		Intent intent;
		switch (v.getId()) 
		{
		case R.id.stream:

			break;
		case R.id.category:
			break;
		case R.id.account:
			// intent = new Intent(EditStreamingActivity.this,
			// MyAccountActivity.class);
			// startActivity(intent);
			break;
		case R.id.crew:
			// intent = new Intent(EditStreamingActivity.this, MyFriends.class);
			// startActivity(intent);
			break;
		case R.id.addnew_stream:
			intent = new Intent(getActivity(), LandingActivity.class);
			startActivity(intent);

			break;
		}
	}
}