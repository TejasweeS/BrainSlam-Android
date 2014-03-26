package com.android.brainslam;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.android.brainslam.NetworkHandler.AsyncCallBack;
import com.android.brainslam.NetworkHandler.AsyncHandler;
import com.android.brainslam.constants.Constants;
import com.android.brainslam.vo.SearchCategory;
import com.android.brainslam.vo.SearchCrewData;
import com.android.brainslam.vo.SearchData;
import com.android.brainslam.vo.SearchMediaData;
import com.android.brainslam.vo.SearchPlaylistData;
import com.android.brainslam.vo.SearchUserData;
import com.android.listdapters.MainStreamSearchListAdapter;
import com.android.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class MainStreamSearchResults extends Activity implements AsyncCallBack
{
	ListView searchResults;
	TextView tvNoResults, btnDone;
	EditText etSearch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search_results);
		searchResults = (ListView) findViewById(R.id.list);
		tvNoResults = (TextView) findViewById(R.id.tv_search_screen);
		etSearch = (EditText) findViewById(R.id.et_search);
		btnDone = (TextView) findViewById(R.id.btn_done);
		
		etSearch.setOnEditorActionListener(new OnEditorActionListener() {        
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_SEARCH)
				{
					getSearchResults(etSearch.getText().toString());
				}
				return false;
			}
		});
	}
	
	public void searchDataClick(View view)
	{
		getSearchResults(etSearch.getText().toString());
	}

	private void getSearchResults(String searchStr)
	{
		// Fetch Featured Media list
		String secretKey = Utils.getDataString(MainStreamSearchResults.this,
				Constants.PREFS_NAME, Constants.SP_SECRET_KEY);
		String userID = Utils.getDataString(MainStreamSearchResults.this,
				Constants.PREFS_NAME, Constants.SP_USER_ID);

		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

		params.add(new BasicNameValuePair("search_Value", searchStr));
		params.add(new BasicNameValuePair("Secret_Key", secretKey));
		params.add(new BasicNameValuePair("User_Id", userID));

		new AsyncHandler(MainStreamSearchResults.this, Constants.SERVER_URL
				+ "search.php?",
				0, false, params).execute();
	}

	
	@Override
	public void onReceive(JSONObject jsonObj, int id) 
	{
		Log.d("MSSR", "Search api resp::"+jsonObj.toString());
		ArrayList<ArrayList<String>> searchResultsAl = new ArrayList<ArrayList<String>>();

		try
		{
			Gson gson = new  Gson();
			com.android.brainslam.vo.SearchData SearchData = gson
						.fromJson(jsonObj.getJSONObject("data").toString(), SearchData.class);
			
			Log.d("MSSR", "SearchData.Media.data.length::"+SearchData.Media.data.length);

			if(jsonObj.getInt("message") == 2)
			{
				Toast.makeText(MainStreamSearchResults.this,
						"Session has been expired. Log in again.",
						Toast.LENGTH_LONG).show();
			}
			
			// Media
			ArrayList<SearchMediaData> mediaList = new ArrayList<SearchMediaData>();
			for (int i = 0; i < SearchData.Media.data.length; i++) 
			{
				SearchMediaData media = new SearchMediaData();
			
				media.Description = SearchData.Media.data[i].Description;
				media.Media_Id = SearchData.Media.data[i].Average_IQ;
				media.Height = SearchData.Media.data[i].Height;
				media.Categories = SearchData.Media.data[i].Categories;
				media.Tranding_Score = SearchData.Media.data[i].Tranding_Score;
				media.Created_At = SearchData.Media.data[i].Created_At;
				media.Name = SearchData.Media.data[i].Name;
				media.Tags = SearchData.Media.data[i].Tags;
				media.Average_IQ = SearchData.Media.data[i].Average_IQ;
				media.Media_Owner = SearchData.Media.data[i].Media_Owner;
				media.Thumbnail_Url = SearchData.Media.data[i].Thumbnail_Url;
				media.Views = SearchData.Media.data[i].Views;
				media.Duration = SearchData.Media.data[i].Duration;
				media.Download_Url = SearchData.Media.data[i].Download_Url;
				media.Width = SearchData.Media.data[i].Width;
				media.Updated_At = SearchData.Media.data[i].Updated_At;
				media.Number_Of_Ratings = SearchData.Media.data[i].Number_Of_Ratings;
				media.Data_Url = SearchData.Media.data[i].Data_Url;
				media.Media_Type = SearchData.Media.data[i].Media_Type;


				ArrayList<String> searchedData = new ArrayList<String>();

				searchedData.add(SearchData.Media.data[i].Media_Id);
				searchedData.add(SearchData.Media.data[i].Name);
				searchedData.add(SearchData.Media.data[i].Thumbnail_Url);

				if(i == 0)
				{
					ArrayList<String> headerName = new ArrayList<String>();
					headerName.add("Media");
					searchResultsAl.add(headerName);	
				}
				
				searchResultsAl.add(searchedData);


				mediaList.add(media);
			}
			
			// Play list
			ArrayList<SearchPlaylistData> playlistList = new ArrayList<SearchPlaylistData>();
			for (int i = 0; i < SearchData.Playlist.data.length; i++) 
			{
				SearchPlaylistData playlist = new SearchPlaylistData();
			
				playlist.Description = SearchData.Playlist.data[i].Description;
				playlist.Owner_Id = SearchData.Playlist.data[i].Owner_Id;
				playlist.Playlist_Id = SearchData.Playlist.data[i].Playlist_Id;
				playlist.Playlist_Name = SearchData.Playlist.data[i].Playlist_Name;
				playlist.Added_Date = SearchData.Playlist.data[i].Added_Date;
				
				
				ArrayList<String> searchedData = new ArrayList<String>();

				searchedData.add(SearchData.Playlist.data[i].Playlist_Id);
				searchedData.add(SearchData.Playlist.data[i].Playlist_Name);
				searchedData.add("");

				if(i == 0)
				{
					ArrayList<String> headerName = new ArrayList<String>();
					headerName.add("Playlists");
					searchResultsAl.add(headerName);	
				}
				
				searchResultsAl.add(searchedData);
				
				playlistList.add(playlist);
			}
			
			// Crews
			ArrayList<SearchCrewData> crewList = new ArrayList<SearchCrewData>();
			for (int i = 0; i < SearchData.Crew.data.length; i++) 
			{
				SearchCrewData crew = new SearchCrewData();
			
				crew.Crew_Id = SearchData.Crew.data[i].Crew_Id;
				crew.Crew_Logo = SearchData.Crew.data[i].Crew_Logo;
				crew.Added_Date = SearchData.Crew.data[i].Added_Date;
				crew.Mission_Statement = SearchData.Crew.data[i].Mission_Statement;
				crew.Crew_Name = SearchData.Crew.data[i].Crew_Name;
				
				
				ArrayList<String> searchedData = new ArrayList<String>();

				searchedData.add(SearchData.Crew.data[i].Crew_Id);
				searchedData.add(SearchData.Crew.data[i].Crew_Name);
				searchedData.add(SearchData.Crew.data[i].Crew_Logo);

				if(i == 0)
				{
					ArrayList<String> headerName = new ArrayList<String>();
					headerName.add("Crews");
					searchResultsAl.add(headerName);	
				}
				
				searchResultsAl.add(searchedData);
				
				crewList.add(crew);
			}
			
			// Users
			ArrayList<SearchUserData> userList = new ArrayList<SearchUserData>();
			for (int i = 0; i < SearchData.User.data.length; i++) 
			{
				SearchUserData user = new SearchUserData();
				
				user.Email_Id = SearchData.User.data[i].Email_Id;
				user.About_User = SearchData.User.data[i].About_User;
				user.Photo_Url = SearchData.User.data[i].Photo_Url;
				user.User_Id = SearchData.User.data[i].User_Id;
				user.User_Name = SearchData.User.data[i].User_Name;
				user.Is_Friend = SearchData.User.data[i].Is_Friend;
				user.Mobile_Number = SearchData.User.data[i].Mobile_Number;
				user.Added_Date = SearchData.User.data[i].Added_Date;

				
				ArrayList<String> searchedData = new ArrayList<String>();
				
				searchedData.add(SearchData.User.data[i].User_Id);
				searchedData.add(SearchData.User.data[i].User_Name);
				searchedData.add(SearchData.User.data[i].Photo_Url);
				
				if(i == 0)
				{
					ArrayList<String> headerName = new ArrayList<String>();
					headerName.add("Users");
					searchResultsAl.add(headerName);	
				}
				
				searchResultsAl.add(searchedData);	
				
				
				userList.add(user);
			}
			
			// Categories
			ArrayList<SearchCategory> categoryList = new ArrayList<SearchCategory>();
			for (int i = 0; i < SearchData.Category.length; i++) 
			{
				SearchCategory category = new SearchCategory();
				
				category.Category_Id = SearchData.Category[i].Category_Id;
				category.Category_Name = SearchData.Category[i].Category_Name;
				category.Created_At = SearchData.Category[i].Created_At;
				category.Updated_At = SearchData.Category[i].Updated_At;
				category.Description = SearchData.Category[i].Description;
				category.tags = SearchData.Category[i].tags;
				category.Entries_Count = SearchData.Category[i].Entries_Count;
				category.Is_Recommended = SearchData.Category[i].Is_Recommended;
				category.Recommended_Sequence = SearchData.Category[i].Recommended_Sequence;
				category.Is_On_Home_Page = SearchData.Category[i].Is_On_Home_Page;
				category.Thumbnail_Url = SearchData.Category[i].Thumbnail_Url;


				ArrayList<String> searchedData = new ArrayList<String>();

				searchedData.add(SearchData.Category[i].Category_Id+"");
				searchedData.add(SearchData.Category[i].Category_Name);
				searchedData.add(SearchData.Category[i].Thumbnail_Url);

				if(i == 0)
				{
					ArrayList<String> headerName = new ArrayList<String>();
					headerName.add("Categories");
					searchResultsAl.add(headerName);	
				}
				
				searchResultsAl.add(searchedData);	


				categoryList.add(category);
			}
			
			Log.d("MSSR", "set list adapter here");
			
			
			if(mediaList.size() > 0 || categoryList.size() > 0 || crewList.size() > 0 ||
					playlistList.size() > 0 || userList.size() > 0)
			{
				tvNoResults.setVisibility(View.GONE);
				searchResults.setVisibility(View.VISIBLE);
			}
			else
			{
				// No results found
				tvNoResults.setVisibility(View.VISIBLE);
				searchResults.setVisibility(View.GONE);
			}
			
			
			
			MainStreamSearchListAdapter adapter = new MainStreamSearchListAdapter(
					MainStreamSearchResults.this, searchResultsAl);
			
//			MainStreamSearchListAdapter adapter = new MainStreamSearchListAdapter(
//					MainStreamSearchResults.this, mediaList, categoryList, crewList,
//					playlistList, userList);
			
			searchResults.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		catch (JsonSyntaxException e) 
		{
			e.printStackTrace();
		}
		catch (org.json.JSONException e) 
		{
			// No results found
			tvNoResults.setVisibility(View.VISIBLE);
			searchResults.setVisibility(View.GONE);
			e.printStackTrace();
		}
	}
}