package com.android.listdapters;

import java.util.ArrayList;

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

public class MainStreamSearchListAdapter extends BaseAdapter
{
	Context context;
//	ArrayList<SearchMediaData> medias;
//	ArrayList<SearchCategory> categories;
//	ArrayList<SearchCrewData> crews;
//	ArrayList<SearchPlaylistData> playlists;
//	ArrayList<SearchUserData> users;
	private ImageLoader imageLoader; 
	
	ArrayList<ArrayList<String>> searchResultsAl = new ArrayList<ArrayList<String>>();

	public MainStreamSearchListAdapter(Context context, ArrayList<ArrayList<String>> searchResultsAl)
//			ArrayList<SearchMediaData> medias,
//			ArrayList<SearchCategory> categories, ArrayList<SearchCrewData> crews,
//			ArrayList<SearchPlaylistData> playlists, ArrayList<SearchUserData> users)
	{
//		this.users = users;
//		this.categories = categories;
//		this.crews = crews;
//		this.playlists = playlists;
//		this.medias = medias;
		this.context = context;

		this.searchResultsAl = searchResultsAl;
		
		imageLoader = new ImageLoader(context.getApplicationContext());
	}

	@Override
	public int getCount() 
	{
//		int totalItems = (medias.size() + categories.size() + crews.size()
//				+ playlists.size() + users.size());

		return searchResultsAl.size();
	}

	@Override
	public Object getItem(int arg0) 
	{
		return null;
	}

	@Override
	public long getItemId(int arg0) 
	{
		return arg0;
	}

	class ViewHolder
	{
		TextView name, more, headerName, headerSearchName;
		ImageView thumbnail;
	}

	
	public final int HEADER = 0;
	public final int RESULT_ID = 0;
	public final int RESULT_NAME = 1;
	public final int PHOTO_URL = 2;
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) context
		.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		ViewHolder holder;

		Log.d("MSSLA", "getview position::"+position);

//		if(convertView == null)
//		{
			holder = new ViewHolder();

			convertView = inflater.inflate(R.layout.search_list_results_row, null);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.more = (TextView) convertView.findViewById(R.id.tv_more);
			holder.thumbnail = (ImageView) convertView.findViewById(R.id.iv_thumbnail);

			
			ArrayList<String> resultsAl = searchResultsAl.get(position);
			
			
			
				if(resultsAl.size() == 1)
				{
					convertView = getHeaderView(inflater, holder);

					holder.headerName.setText(resultsAl.get(HEADER));

					convertView.setTag(holder);	
					return convertView;
				}
				else
				{
					holder.name.setText(resultsAl.get(RESULT_NAME));
					if(resultsAl.get(PHOTO_URL).length() > 0)
						imageLoader.DisplayImage(resultsAl.get(PHOTO_URL), holder.thumbnail); 					
				}

			
			convertView.setTag(holder);	
//		}
//		else
//		{
//			holder = (ViewHolder) convertView.getTag();
//		}

		return convertView;
	}
	
	
	
//	@Override
//	public View getView(int position, View convertView, ViewGroup arg2) 
//	{
//		LayoutInflater inflater = (LayoutInflater) context
//		.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//		ViewHolder holder;
//
//		Log.d("MSSLA", "getview position::"+position);
//
//		if(convertView == null)
//		{
//			holder = new ViewHolder();
//
//			convertView = inflater.inflate(R.layout.search_list_results_row, null);
//			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
//			holder.more = (TextView) convertView.findViewById(R.id.tv_more);
//			holder.thumbnail = (ImageView) convertView.findViewById(R.id.iv_thumbnail);
//
//			if (position < users.size()) 
//			{
//				Log.d("MSSLA", " users.size()::"+ users.size()+"getview position::"+position);
//				
//				if(position == 0)
//				{
//					convertView = getHeaderView(inflater, holder);
//
//					holder.headerName.setText("Users");
//					holder.headerSearchName.setText(users.get(position).User_Name);
//
//					convertView.setTag(holder);	
//					return convertView;
//				}
//
//				holder.name.setText(users.get(position).User_Name);
//				if(users.get(position).Photo_Url.trim().length() > 0)
//					imageLoader.DisplayImage(users.get(position).Photo_Url, holder.thumbnail); 
//
//			}
//			else if (position >= users.size()
//					&& position < (users.size() + categories.size())) 
//			{
//				int pos = position - users.size();
//
//				if(pos == 0)
//				{
//					convertView = getHeaderView(inflater, holder);
//
//					holder.headerName.setText("Categories");
//					holder.headerSearchName.setText(categories.get(pos).Category_Name);
//
//					convertView.setTag(holder);	
//					return convertView;
//				}
//
//				holder.name.setText(categories.get(pos).Category_Name);
//				if(categories.get(pos).Thumbnail_Url.trim().length() > 0)
//					imageLoader.DisplayImage(categories.get(pos).Thumbnail_Url, holder.thumbnail); 
//			}
//			else if (position >= (users.size() + categories.size())
//					&& position < (users.size() + categories.size() + crews.size())) 
//			{
//				int pos = position - (users.size() + categories.size());
//
//				if(pos == 0)
//				{
//					convertView = getHeaderView(inflater, holder);
//
//					holder.headerName.setText("Crews");
//					holder.headerSearchName.setText(crews.get(pos).Crew_Name);
//
//					convertView.setTag(holder);	
//					return convertView;
//				}	
//
//				holder.name.setText(crews.get(pos).Crew_Name);
//				if(crews.get(pos).Crew_Logo.trim().length() > 0)
//					imageLoader.DisplayImage(crews.get(pos).Crew_Logo, holder.thumbnail); 
//			}
//			else if (position >= (users.size() + categories.size() + crews.size())
//					&& position < (users.size() + categories.size() + crews.size() + playlists
//							.size())) 
//			{
//				int pos = position - (users.size() + categories.size() + crews.size());
//				Log.d("MSSLA", "playlist initial position::"+position+" pos::"+pos);
//				if(pos == 0)
//				{
//					convertView = getHeaderView(inflater, holder);
//
//					holder.headerName.setText("Playlists");
//					holder.headerSearchName.setText(playlists.get(pos).Playlist_Name);
//
//					convertView.setTag(holder);	
//					return convertView;
//				}
//
//				holder.name.setText(playlists.get(pos).Playlist_Name);
//				//					imageLoader.DisplayImage(playlists.get(pos).Thumbnail_Url, holder.thumbnail); 
//			}
//			else if (position >= (users.size() + categories.size() + crews.size() + playlists
//					.size())
//					&& position < (users.size() + categories.size() + crews.size()
//							+ playlists.size() + medias.size())) 
//			{
//				int pos = position - (users.size() + categories.size() + crews.size() + playlists.size());
//
//				if(pos == 0)
//				{
//					convertView = getHeaderView(inflater, holder);
//
//					holder.headerName.setText("Medias");
//					holder.headerSearchName.setText(medias.get(pos).Media_Owner);
//
//					convertView.setTag(holder);	
//					return convertView;
//				}	
//
//				holder.name.setText(medias.get(pos).Media_Owner);
//
//				if(medias.get(pos).Thumbnail_Url.trim().length() > 0)
//					imageLoader.DisplayImage(medias.get(pos).Thumbnail_Url, holder.thumbnail); 
//			}
//			convertView.setTag(holder);	
//		}
//		else
//		{
//			holder = (ViewHolder) convertView.getTag();
//		}
//
//		return convertView;
//	}
	
	private View getHeaderView(LayoutInflater inflater, ViewHolder holder)
	{
		View convertView = inflater.inflate(R.layout.search_results_list_header, null);
		holder.headerName = (TextView) convertView.findViewById(R.id.tv_header_name);
//		holder.headerSearchName = (TextView) convertView.findViewById(R.id.tv_name);
		
		return convertView;
	}
}