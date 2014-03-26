package com.android.brainslam;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.android.brainslam.NetworkHandler.AsyncCallBack;
import com.android.brainslam.NetworkHandler.AsyncHandler;
import com.android.brainslam.constants.Constants;
import com.android.brainslam.db.dao.Category;
import com.android.brainslam.db.dao.MyFriends;
import com.android.brainslam.manager.CategoryManager;
import com.android.brainslam.manager.FriendManager;
import com.android.brainslam.manager.UserManager;
import com.android.brainslam.vo.MyFriendsVo;
import com.android.listdapters.LandingCategoryAdapter;
import com.android.listdapters.LazyImageAdapterLandingtiles;
import com.android.utils.ListSizeHelper;
import com.android.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class LandingActivity extends Activity implements AsyncCallBack {

	ArrayList<Category> categoryList1;
	String[] subcategories = {"Brain-Slam", "Intense", "Clunker", "Sick & Wrong", "Disturbed", "Featured"};
	LinearLayout llrecomended;
	TextView tvcategory,tvall,tvfriends;

	Set<Category> categorySet;
	ArrayList<Category> recommendedCategoryList ;
	ArrayList<Category> categoryList ;
	ArrayList<MyFriends> friendsCategoryList ;
	ArrayList<ArrayList<Category>> recommendedArrayList;

	//	public static HashSet<String> hashSetRecommended ;
	//	public static HashSet<String> hashSetCategory ;

	CheckBox chkcategory;
	ListView categoryListView;
	LandingCategoryAdapter categoryAdapter;
	LazyImageAdapterLandingtiles recommendedAdapter;
	ListView recommendedListview;
	EditText searchbar;

	//	GridView reccomendedgridView;

	String TAG = "LandingActivity";
	public static final int CATEGORY_LIST = 0;
	public static final int FEATURED_MEDIA_LIST = 1;
	public static final int FRIENDS_LIST = 2;
	public static final int DONE_CLICK = 3;
	int bottomButton = 2;
	ContextThemeWrapper con;
	TextView headerTitleTextView;
	List<Category> categoryDbList ;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_landing_main);
			Log.v(TAG, "oncretae");
		chkcategory=(CheckBox) findViewById(R.id.checkBox1);
		tvcategory=(TextView) findViewById(R.id.activity_landing_bottombar_category);
		tvall=(TextView) findViewById(R.id.activity_landing_bottombar_all);
		tvfriends=(TextView) findViewById(R.id.activity_landing_bottombar_friends);
		llrecomended = (LinearLayout) findViewById(R.id.llrecomended);

		categoryListView=(ListView)findViewById(R.id.activity_landing_category_grid);

		categoryDbList = new ArrayList<Category>();
		categoryList = new ArrayList<Category>();
		friendsCategoryList = new ArrayList<MyFriends>();
		recommendedCategoryList = new ArrayList<Category>();

		con = new ContextThemeWrapper( this, R.style.alert);
		recommendedListview= (ListView)findViewById(R.id.activity_landing_tiles);

		headerTitleTextView = (TextView) findViewById(R.id.title);

		llrecomended.setVisibility(View.VISIBLE);	


		searchbar=(EditText)findViewById(R.id.landing_searchbar);

		searchbar.setFocusable(false);
		searchbar.setFocusableInTouchMode(true);		
		
		
		
		searchbar.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence searchTerm, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

				ArrayList<Category> categoriesSearch = new ArrayList<Category>();
				ArrayList<MyFriends> friendsSearch = new ArrayList<MyFriends>();


				for(int i=0;i<categoryList.size()+friendsCategoryList.size();i++)
				{

					if(i < categoryList.size())
					{
						String playerName = categoryList.get(i).categoryName;
						//compare the String in EditText with Names in the ArrayList
						if(Utils.searchData(searchbar.getText().toString().trim(), playerName)){

							System.out.println("rutuja match found category");
							categoriesSearch.add(categoryList.get(i));
						}
					}
					else
					{
						String playerName1 = friendsCategoryList.get(i - categoryList.size()).name;
						//compare the String in EditText with Names in the ArrayList
						if(Utils.searchData(searchbar.getText().toString().trim(), playerName1)){
							System.out.println("rutuja match found friends");
							friendsSearch.add(friendsCategoryList.get(i - categoryList.size()));
						}

					}


				}

				if(searchbar.getText().toString().trim().length() > 0)
				{
					if(bottomButton != 2)
						llrecomended.setVisibility(View.GONE);


					switch(bottomButton)
					{
					case 1:

						categoryAdapter=new LandingCategoryAdapter(LandingActivity.this, categoriesSearch , new ArrayList<MyFriends>());
						categoryListView.setAdapter(categoryAdapter);
						ListSizeHelper.getListViewSize(categoryListView);
						categoryAdapter.notifyDataSetChanged();
						llrecomended.setVisibility(View.GONE);
						break;
					case 2:

						categoryAdapter =  new LandingCategoryAdapter(LandingActivity.this, categoriesSearch, friendsSearch);
						categoryListView.setAdapter(categoryAdapter);
						ListSizeHelper.getListViewSize(categoryListView);
						categoryAdapter.notifyDataSetChanged();
						llrecomended.setVisibility(View.GONE);
						break;
					case 3:

						categoryAdapter=new LandingCategoryAdapter(LandingActivity.this,new ArrayList<Category>(),friendsSearch);
						categoryListView.setAdapter(categoryAdapter);
						ListSizeHelper.getListViewSize(categoryListView);
						categoryAdapter.notifyDataSetChanged();
						llrecomended.setVisibility(View.GONE);
						break;
					}

				}
				else if(searchbar.getText().toString().trim().length() == 0)
				{

					switch(bottomButton)
					{
					case 1:

						categoryAdapter=new LandingCategoryAdapter(LandingActivity.this, categoryList , new ArrayList<MyFriends>());
						categoryListView.setAdapter(categoryAdapter);
						ListSizeHelper.getListViewSize(categoryListView);
						categoryAdapter.notifyDataSetChanged();
						llrecomended.setVisibility(View.GONE);
						break;
					case 2:

						categoryAdapter=new LandingCategoryAdapter(LandingActivity.this, categoryList , friendsCategoryList);
						categoryListView.setAdapter(categoryAdapter);
						ListSizeHelper.getListViewSize(categoryListView);
						categoryAdapter.notifyDataSetChanged();
						llrecomended.setVisibility(View.VISIBLE);
						break;
					case 3:

						categoryAdapter=new LandingCategoryAdapter(LandingActivity.this,new ArrayList<Category>(),friendsCategoryList);
						categoryListView.setAdapter(categoryAdapter);
						ListSizeHelper.getListViewSize(categoryListView);
						categoryAdapter.notifyDataSetChanged();
						llrecomended.setVisibility(View.GONE);
						break;
					}


				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}
			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});

		searchbar.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub


				if(actionId == EditorInfo.IME_ACTION_SEARCH)
				{


					return true;
				}
				return false;
			}
		}) ;


		ScrollView scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
		scrollView1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					System.out.println("ONNNN CLICK");
					hideKeyboard();
				}
				return false;
			}
		});


	}

	private void hideKeyboard()
	{
		try
		{
			InputMethodManager inputMethodManager = (InputMethodManager) 
					getSystemService(INPUT_METHOD_SERVICE);

			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void onClick(View v)
	{

		if(v.getId() == R.id.landing_searchbar_done)
		{

			final String secretKey = Utils.getDataString(LandingActivity.this,
					Constants.PREFS_NAME, Constants.SP_SECRET_KEY);
			final String userID = Utils.getDataString(LandingActivity.this,
					Constants.PREFS_NAME, Constants.SP_USER_ID);


			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

			params.add(new BasicNameValuePair("operation", "userUpdateHomeStream"));
			params.add(new BasicNameValuePair("Secret_Key", secretKey));
			params.add(new BasicNameValuePair("User_Id", userID));


			//			if(hashSetRecommended.size() >0 || hashSetRecommended.size() > 0)
			//			{
			//
			//				JSONObject jsonObject = new JSONObject();
			//				JSONArray array = new JSONArray();
			//				try { 
			//
			//
			//					for(String str: hashSetRecommended)
			//					{
			//						array.put(Integer.parseInt(str));
			//					}
			//
			//					jsonObject.put("Category", (Object)array);
			//
			//					array = new JSONArray();
			//					for(String str: hashSetCategory)
			//					{
			//						array.put(Integer.parseInt(str));
			//					}
			//
			//					jsonObject.put("User", array);
			//				} catch (JSONException e) {
			//					// TODO Auto-generated catch block
			//					e.printStackTrace();
			//				}
			//
			//				System.out.println(" DONE click:: "+jsonObject.toString());
			//
			//				params.add(new BasicNameValuePair("updateStreams",jsonObject.toString()));
			//			}
			//			else
			//			params.add(new BasicNameValuePair("updateStreams","{\"User\":\"[]\",\"Category\":\"[]\"}"));


			List<MyFriends> friendList = new ArrayList<MyFriends>();
			friendList = FriendManager.getInstance(getApplicationContext()).getMyFriends();
			List<Category> categoryList = new  ArrayList<Category>();
			categoryList = CategoryManager.getInstance(getApplicationContext()).getCategories();
			StringBuilder builder = new StringBuilder();
			builder.append("{\"Category\":\"[");
			boolean gotCat = false, gotFrnd = false;
			for(int i =0; i<categoryList.size();i++)
			{

				if(categoryList.get(i).isOnHomePage)
				{
					builder.append(""+categoryList.get(i).categoryId);
					gotCat = true;
					if(i != categoryList.size())
						builder.append(",");
				}

			}
			if(gotCat)
				builder.deleteCharAt(builder.length()-1) ;
			builder.append("]\",\"User\":\"[");
			for(int i=0 ; i<friendList.size() ;i ++)
			{	

				if(friendList.get(i).isOnHomeScreen)
				{
					builder.append(""+friendList.get(i).userId);
					if(i != friendList.size())
						builder.append(",");

					gotFrnd = true;
				}
			}
			if(gotFrnd)
				builder.deleteCharAt(builder.length()-1);
			builder.append("]\"}");

			Log.v(TAG,	"DONE CLICK:"+builder.toString());

			params.add(new BasicNameValuePair("updateStreams",builder.toString()));

			new AsyncHandler(LandingActivity.this, Constants.SERVER_URL
					+ "user.php?", DONE_CLICK, false, params).execute();


		}


	}




	boolean isAllButtonClicked = false;

	public void bottomtabclk(View v)
	{	

		switch (v.getId())
		{
		case R.id.activity_landing_bottombar_category:
			tvall.setBackgroundColor(getResources().getColor(R.color.black));
			tvfriends.setBackgroundColor(getResources().getColor(R.color.black));
			tvcategory.setBackgroundColor(getResources().getColor(R.color.blue_kaltura));
			tvcategory.setTextColor(getResources().getColor(R.color.white));
			tvall.setTextColor(getResources().getColor(R.color.blue_kaltura));
			tvfriends.setTextColor(getResources().getColor(R.color.blue_kaltura));
			llrecomended.setVisibility(View.GONE);

			categoryAdapter=new LandingCategoryAdapter(LandingActivity.this,categoryList,new ArrayList<MyFriends>());
			categoryListView.setAdapter(categoryAdapter);
			categoryAdapter.notifyDataSetChanged();
			ListSizeHelper.getListViewSize(categoryListView);
			headerTitleTextView.setText("Categories");
			isAllButtonClicked = false; 
			bottomButton = 1;
			//					ListSizeHelper.getListViewSize(lcategory);
			//			recommendedListview.setAdapter(recommendedAdapter);

			//			lcategory.setVisibility(View.VISIBLE);
			//			friendscategoryListView.setVisibility(View.GONE);
			searchbar.setText("");

			break;
		case R.id.activity_landing_bottombar_all:
			tvall.setBackgroundColor(getResources().getColor(R.color.blue_kaltura));
			tvfriends.setBackgroundColor(getResources().getColor(R.color.black));
			tvcategory.setBackgroundColor(getResources().getColor(R.color.black));
			tvall.setTextColor(getResources().getColor(R.color.white));
			tvcategory.setTextColor(getResources().getColor(R.color.blue_kaltura));
			tvfriends.setTextColor(getResources().getColor(R.color.blue_kaltura));
			llrecomended.setVisibility(View.VISIBLE);
			categoryAdapter=new LandingCategoryAdapter(LandingActivity.this,categoryList,friendsCategoryList);
			categoryListView.setAdapter(categoryAdapter);
			ListSizeHelper.getListViewSize(categoryListView);

			isAllButtonClicked = true; 
			categoryAdapter.notifyDataSetChanged();
			bottomButton = 2;
			headerTitleTextView.setText("Categories & Friends");
			//			lcategory.setVisibility(View.VISIBLE);
			//			friendscategoryListView.setVisibility(View.VISIBLE);
			searchbar.setText("");
			break;
		case R.id.activity_landing_bottombar_friends:
			tvall.setBackgroundColor(getResources().getColor(R.color.black));
			tvfriends.setBackgroundColor(getResources().getColor(R.color.blue_kaltura));
			tvcategory.setBackgroundColor(getResources().getColor(R.color.black));
			tvfriends.setTextColor(getResources().getColor(R.color.white));
			tvcategory.setTextColor(getResources().getColor(R.color.blue_kaltura));
			tvall.setTextColor(getResources().getColor(R.color.blue_kaltura));
			llrecomended.setVisibility(View.GONE);

			categoryAdapter=new LandingCategoryAdapter(LandingActivity.this,new ArrayList<Category>(),friendsCategoryList);
			categoryListView.setAdapter(categoryAdapter);
			ListSizeHelper.getListViewSize(categoryListView);
			categoryAdapter.notifyDataSetChanged();
			isAllButtonClicked = false; 
			bottomButton = 3;
			headerTitleTextView.setText("Friends");
			//			lcategory.setVisibility(View.GONE);
			//			friendscategoryListView.setVisibility(View.VISIBLE);
			searchbar.setText("");
			break;
		}



	}



	/**
	 * Hello Russel,
	 * 
	 * Please find the attached excel sheet regarding time and cost estimate for the game.
	 * I am sorry for the delay, as the graphics guy was not available.
	 * Total Cost including development for Android and Iphone and Graphics designing would be 1470 USD,
	 * I have provided a detailed breakdown for the same in the excel sheet.

	 * 
	 * 
	 * */

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		if(recommendedAdapter != null)
			recommendedAdapter.imageLoader.clearCache();

		if(categoryAdapter != null)
			categoryAdapter.imageLoader.clearCache();

		super.onBackPressed();
	}


	public void setCategoryAndRecommendedList()
	{
		recommendedCategoryList = new ArrayList<Category>();
		recommendedArrayList = new ArrayList<ArrayList<Category>>();
		categoryList = new ArrayList<Category>();
		for(Category category: categoryDbList)
		{

			if(category.isRecommended)
			{
				if(recommendedCategoryList.size() < 3)
				{
					System.out.println("rutuja rarylst: "+category.categoryName);
					recommendedCategoryList.add(category);
				}

				if(recommendedCategoryList.size()== 3)
				{

					System.out.println("rutuja row added size 3");
					recommendedArrayList.add(recommendedCategoryList);
					recommendedCategoryList = new ArrayList<Category>();
				}
			}	
			else
				//				categoryList.add(category);
			{

				int i;
				for( i=0;i < subcategories.length ; i++)
				{
					if(category.categoryName.equalsIgnoreCase(subcategories[i]))
						break;
				}
				if(i == subcategories.length)
					categoryList.add(category);
			}

		}

		if(recommendedCategoryList.size() < 3 & recommendedCategoryList.size() > 0)
		{

			System.out.println("rutuja row added size < 3");
			recommendedArrayList.add(recommendedCategoryList);
			recommendedCategoryList = new ArrayList<Category>();
		}
		Log.v(TAG, "rutuja recommendedArrayList :: "+recommendedArrayList.size());

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v(TAG, "on resume");
		categoryDbList = CategoryManager.getInstance(getApplicationContext()).getCategories();
        friendsCategoryList = (ArrayList<MyFriends>) FriendManager.getInstance(getApplicationContext()).getMyFriends(); 
		if(null != categoryDbList && null != recommendedAdapter && null != categoryAdapter)
		{

			Log.v(TAG, "On resume of landing activity change list");
			setCategoryAndRecommendedList();
			recommendedAdapter = new LazyImageAdapterLandingtiles(
					LandingActivity.this, recommendedArrayList);
			recommendedListview.setAdapter(recommendedAdapter);
			recommendedAdapter.notifyDataSetChanged();
			ListSizeHelper.getListViewSize(recommendedListview);

			if(null != friendsCategoryList)
			{
				categoryAdapter=new LandingCategoryAdapter(LandingActivity.this,categoryList,friendsCategoryList);
				categoryListView.setAdapter(categoryAdapter);
				categoryAdapter.notifyDataSetChanged();
				ListSizeHelper.getListViewSize(categoryListView);

			}
			else
			{
				categoryAdapter=new LandingCategoryAdapter(LandingActivity.this,categoryList,new ArrayList<MyFriends>());
				categoryListView.setAdapter(categoryAdapter);
				categoryAdapter.notifyDataSetChanged();
				ListSizeHelper.getListViewSize(categoryListView);

			}

		}
		else
		{

			final String secretKey = Utils.getDataString(LandingActivity.this,
					Constants.PREFS_NAME, Constants.SP_SECRET_KEY);
			final String userID = Utils.getDataString(LandingActivity.this,
					Constants.PREFS_NAME, Constants.SP_USER_ID);


			final List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

			params.add(new BasicNameValuePair("operation", "categoryList"));
			params.add(new BasicNameValuePair("Secret_Key", secretKey));
			params.add(new BasicNameValuePair("User_Id", userID));
			long unixTime = System.currentTimeMillis() / 1000L;

			if(CategoryManager.getInstance(getApplicationContext()).getCategories().size() > 0)
				params.add(new BasicNameValuePair("Last_Modified",""+unixTime));
			
//			new Handler().postDelayed(new Runnable() {
//
//				@Override
//				public void run() 
//				{
					new AsyncHandler(LandingActivity.this, Constants.SERVER_URL
							+ "category.php?", CATEGORY_LIST, false, params).execute();
//				}
//			},500);
		}



	}
	@Override
	public void onReceive(JSONObject jsonObj, int id) 
	{
		Log.d(TAG, "onReceive json :"+id+" ::"+jsonObj.toString());
		Gson categoryGson = new  Gson();
		Type listType;
		switch (id) 
		{
		case CATEGORY_LIST:


			categoryDbList = CategoryManager.getInstance(getApplicationContext()).getCategoriesList();


			recommendedArrayList = new ArrayList<ArrayList<Category>>();

			try {
				if(jsonObj.has("status") && jsonObj.getInt("status") == 0 && jsonObj.getInt("message") == 6)
				{

					if(jsonObj.has("data") && jsonObj.get("data").toString().length() >0)
					{
						listType = (Type) new TypeToken<List<com.android.brainslam.vo.Category>>(){}.getType();
						List<com.android.brainslam.vo.Category> CategoryList = categoryGson.fromJson(jsonObj.getJSONArray("data").toString(), listType);


						for (com.android.brainslam.vo.Category category : CategoryList) {
							Category daoCategory = new Category();

							daoCategory.categoryId = category.getCategory_Id();
							daoCategory.categoryName = category.getCategory_Name();
							daoCategory.isRecommended =  category.getIs_Recommended() == 1 ? true : false;
							daoCategory.thumbnailUrl = category.getThumbnail_Url();
							daoCategory.isOnHomePage =  category.getIs_On_Home_Page() == 1 ? true : false;
							//								daoCategory.itemSequence =  category.getItemSequence();
							daoCategory.user = UserManager.getInstance(
									getApplicationContext()).getUser(
											Integer.parseInt(Utils.getDataString(
													LandingActivity.this,
													Constants.PREFS_NAME,
													Constants.SP_USER_ID)));


							if (category.getIs_Recommended() == 1)
							{
								if(recommendedCategoryList.size() < 3)
								{
									Log.v(TAG,"rutuja rarylst: "+category.getCategory_Name());
									recommendedCategoryList.add(daoCategory);
								}

								if (recommendedCategoryList.size() == 3)
								{

									Log.v(TAG,"rutuja row added size 3");
									recommendedArrayList.add(recommendedCategoryList);
									recommendedCategoryList = new ArrayList<Category>();
								}
								//								recommendedCategoryList.add(daoCategory);
							}
							else
							{
								//																	boolean gotIt = false;
								int i;
								for( i=0;i < subcategories.length ; i++)
								{
									if(daoCategory.categoryName.equalsIgnoreCase(subcategories[i]))
										break;
								}
								if(i == subcategories.length)
									categoryList.add(daoCategory);

							}	


							categoryDbList.add(daoCategory); 

						}


						//							setCategoryAndRecommendedList();
						if(recommendedCategoryList.size() < 3 & recommendedCategoryList.size() > 0)
						{

							System.out.println("rutuja row added size < 3");
							recommendedArrayList.add(recommendedCategoryList);
							recommendedCategoryList = new ArrayList<Category>();
						}


						//store list to db
						CategoryManager.getInstance(getApplicationContext()).removeAllCategories();
						CategoryManager.getInstance(getApplicationContext()).saveCategories(categoryDbList);
						Log.v(TAG, "rutuja recommendedArrayList :: "+recommendedArrayList.size());
						Log.v("rutuja", " reccomendedgridView.size::: "+recommendedCategoryList.size());
						recommendedAdapter = new LazyImageAdapterLandingtiles(
								LandingActivity.this, recommendedArrayList);
						recommendedListview.setAdapter(recommendedAdapter);
						ListSizeHelper.getListViewSize(recommendedListview);

					}
					else
					{
						setCategoryAndRecommendedList();
						recommendedAdapter = new LazyImageAdapterLandingtiles(
								LandingActivity.this, recommendedArrayList);
						Log.v("rutuja", "No new updates reccomendedgridView.size::: "+recommendedCategoryList.size());
						recommendedListview.setAdapter(recommendedAdapter);
						ListSizeHelper.getListViewSize(recommendedListview);
						//						Toast.makeText(LandingActivity.this, "No new updates", Toast.LENGTH_LONG).show();
					}

					List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

					final String secretKey = Utils.getDataString(LandingActivity.this,
							Constants.PREFS_NAME, Constants.SP_SECRET_KEY);
					final String userID = Utils.getDataString(LandingActivity.this,
							Constants.PREFS_NAME, Constants.SP_USER_ID);

					params.add(new BasicNameValuePair("operation", "friendsList"));
					params.add(new BasicNameValuePair("Secret_Key", secretKey));
					params.add(new BasicNameValuePair("User_Id", userID));
					params.add(new BasicNameValuePair("pageSize", 5+""));
					params.add(new BasicNameValuePair("pageIndex", 1+""));
					long unixTime = System.currentTimeMillis() / 1000L;
					if(FriendManager.getInstance(getApplicationContext()).getMyFriends().size() > 0)
						params.add(new BasicNameValuePair("Last_Modified",""+unixTime));

					new AsyncHandler(LandingActivity.this, Constants.SERVER_URL
							+ "user.php?",
							FRIENDS_LIST, false, params).execute();
				}
				else if(jsonObj.has("errMessage"))
				{
					Toast.makeText(this,jsonObj.getString("errMessage"), Toast.LENGTH_LONG).show();
				}
				else if(jsonObj.has("status") && jsonObj.getInt("status") == 1)
				{

					String msg = Utils.getResponseMessage(jsonObj.getInt("message"));
					Toast.makeText(LandingActivity.this, msg, Toast.LENGTH_LONG).show();

				}


			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case FRIENDS_LIST:

			categoryGson = new  Gson();

			friendsCategoryList = (ArrayList<MyFriends>) FriendManager.getInstance(getApplicationContext()).getMyFriends();
			List<MyFriendsVo> friendsCategoryVoList = new ArrayList<MyFriendsVo>();


			try {
				if(jsonObj.has("status") && jsonObj.getInt("status") == 0 && jsonObj.getInt("message") == 6)
				{
					if(jsonObj.has("data") && jsonObj.get("data").toString().length() > 0)
					{
						listType = (Type) new TypeToken<List<MyFriendsVo>>(){}.getType();

						friendsCategoryVoList = categoryGson.fromJson(jsonObj.getJSONArray("data").toString(), listType);

						for(MyFriendsVo friendsVo: friendsCategoryVoList)
						{

							MyFriends friend = new MyFriends();

							friend.aboutUser = friendsVo.getAbout_User();
							friend.addedDate = friendsVo.getAdded_Date();
							friend.emailId = friendsVo.getEmail_Id();
							friend.isOnHomeScreen = false;
							friend.lastModified = System.currentTimeMillis() / 1000L;
							friend.mobileNumber = friendsVo.getMobile_Number();
							friend.name = friendsVo.getUser_Name();
							friend.photoUrl = friendsVo.getPhoto_Url();
							friend.userId = friendsVo.getUser_Id();


							friendsCategoryList.add(friend);
						}
						
						FriendManager.getInstance(getApplicationContext()).removeAllfrinends();
						FriendManager.getInstance(getApplicationContext()).saveMyFriends(friendsCategoryList);
					}
					else
					{


						Log.v(TAG,"Friends list  No NewUPdates:"+ Constants.NO_RECORDS_TO_DISPLAY);
					}

					categoryAdapter=new LandingCategoryAdapter(LandingActivity.this,categoryList,friendsCategoryList);
					categoryListView.setAdapter(categoryAdapter);
					ListSizeHelper.getListViewSize(categoryListView);


				}
				else if(jsonObj.has("errMessage"))
				{
					Toast.makeText(this,jsonObj.getString("errMessage"), Toast.LENGTH_LONG).show();
				}
				else if(jsonObj.has("status") && jsonObj.getInt("status") == 1)
				{
					String msg = Utils.getResponseMessage(jsonObj.getInt("message"));
					Toast.makeText(LandingActivity.this, msg, Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			break;

		case DONE_CLICK:

			try {
				if(jsonObj.has("status") && jsonObj.getInt("status") == 0 && jsonObj.getInt("message") == 6)
				{

					//saving final list to database
//					CategoryManager.getInstance(getApplicationContext()).removeAllCategories();
//					CategoryManager.getInstance(getApplicationContext()).saveCategories(categoryDbList);
					Intent intent = new Intent(LandingActivity.this,BrainSlamMainActivity.class);
					startActivity(intent);
				}
				else if(jsonObj.has("status") && jsonObj.getInt("status") == 1)
				{

					String msg = Utils.getResponseMessage(jsonObj.getInt("message"));
					Toast.makeText(LandingActivity.this, msg, Toast.LENGTH_LONG).show();

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		}
	}




	public void catchecked(View v)
	{

		//		//recommended grid view
		LinearLayout rl=(LinearLayout) v.getParent();
		//			ImageView Iv=(TextView) rl.getChildAt(0);
		CheckBox chkcategory=(CheckBox) rl.getChildAt(1);

		if(chkcategory.isChecked()){
			System.out.println("rutuja category id:: "+((Category)chkcategory.getTag()).categoryId);
			//			hashSetRecommended.add(""+((Category)chkcategory.getTag()).categoryId);
			CategoryManager.getInstance(getApplicationContext())
			.updateIsOnHomeScreen(
					((Category) chkcategory.getTag()).categoryId, true);


		}else if(!chkcategory.isChecked())
		{
			//			if(hashSetRecommended.contains(""+((Category)chkcategory.getTag()).categoryId))
			//				hashSetRecommended.remove(""+((Category)chkcategory.getTag()).categoryId);
			//			updateCategoryList(((Category)chkcategory.getTag()).categoryId,false);

			CategoryManager
			.getInstance(getApplicationContext())
			.updateIsOnHomeScreen(
					((Category) chkcategory.getTag()).categoryId, false);
		}
		//


	}

	public void updateCategoryList(int categoryId, boolean isOnHomeScreen)
	{
		for(Category category: categoryDbList)
		{
			if(category.categoryId == categoryId)
			{
				category.isOnHomePage = isOnHomeScreen;
				break;
			}
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	public void listcheck(View v)
	{

		//category list
		RelativeLayout rl=(RelativeLayout) v.getParent();
		//			ImageView Iv=(TextView) rl.getChildAt(0);
		CheckBox chkcategory=(CheckBox) rl.getChildAt(1);

		if(chkcategory.isChecked()){
			System.out.println("rutuja category id:: "+chkcategory.getTag());


			if(chkcategory.getTag().getClass().equals(Category.class))
			{
				//				hashSetRecommended.add(""+((Category)chkcategory.getTag()).categoryId);
				CategoryManager.getInstance(getApplicationContext()).updateIsOnHomeScreen(((Category)chkcategory.getTag()).categoryId,true); 
				//				updateCategoryList(((Category)chkcategory.getTag()).categoryId,true);
			}
			else if(chkcategory.getTag().getClass().equals(MyFriendsVo.class))
			{
				//				hashSetCategory.add(""+((MyFriendsVo)chkcategory.getTag()).getUser_Id());
				FriendManager
				.getInstance(getApplicationContext())
				.updateIsOnHomeScreen(
						((MyFriendsVo) chkcategory.getTag())
						.getUser_Id(),
						true);

			}

		}else if(!chkcategory.isChecked())
		{

			if(chkcategory.getTag().getClass().equals(Category.class))
			{
				//				if(hashSetRecommended.contains(""+((Category)chkcategory.getTag()).categoryId))
				//					hashSetRecommended.remove(""+((Category)chkcategory.getTag()).categoryId);
				//
				//				updateCategoryList(((Category)chkcategory.getTag()).categoryId,false);

				CategoryManager.getInstance(getApplicationContext()).updateIsOnHomeScreen(((Category)chkcategory.getTag()).categoryId,true);
			}
			else if(chkcategory.getTag().getClass().equals(MyFriendsVo.class))
			{
				//				if(hashSetCategory.contains(""+((MyFriendsVo)chkcategory.getTag()).getUser_Id()))
				//					hashSetCategory.remove(""+((MyFriendsVo)chkcategory.getTag()).getUser_Id());

				FriendManager
				.getInstance(getApplicationContext())
				.updateIsOnHomeScreen(
						((MyFriendsVo) chkcategory.getTag())
						.getUser_Id(),
						false);
			}

		}


	}

}