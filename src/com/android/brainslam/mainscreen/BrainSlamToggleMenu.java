package com.android.brainslam.mainscreen;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.android.brainslam.BrainSlamMainActivity;
import com.android.brainslam.R;
import com.android.brainslam.DnDList.DragNDropCursorAdapter;
import com.android.brainslam.NetworkHandler.AsyncCallBack;
import com.android.brainslam.NetworkHandler.AsyncHandler;
import com.android.brainslam.NetworkHandler.HttpCommunicator;
import com.android.brainslam.constants.Constants;
import com.android.brainslam.db.dao.Category;
import com.android.brainslam.db.dao.Streams;
import com.android.brainslam.db.dao.UserDetails;
import com.android.brainslam.manager.CategoryManager;
import com.android.brainslam.manager.HomeStreamManager;
import com.android.brainslam.vo.FeaturedMediaListData;
import com.android.utils.CustomSpinnerDialog;
import com.android.utils.Utils;
import com.google.gson.Gson;

public class BrainSlamToggleMenu extends FragmentActivity implements
AsyncCallBack {
	private static final int DONE_CLICK = 0;
	private TabHost mTabHost;
	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;

	ContextThemeWrapper con;
	public static int color;
	ImageView backforwardToggleButton;
	String userId;
	String screteKey;
	public static JSONObject UserDtailsJsonObject;
	GetUserDetails userdetail;
	CustomSpinnerDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brain_slam_main_screen);
		color = getResources().getColor(R.color.tabcolor);
		dialog= new CustomSpinnerDialog(BrainSlamToggleMenu.this);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
		for (int i = 0; i < 4; i++) {
			LayoutInflater inflater = (LayoutInflater) this
			.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			View convertView = inflater.inflate(R.layout.tabview, null);
			TextView title = (TextView) convertView.findViewById(R.id.tabName);
			ImageView imageView = (ImageView) convertView
			.findViewById(R.id.tab_icon);
			switch (i) {
			case 0:
				title.setText("Streams");
				imageView.setImageResource(R.drawable.stream_tabselector);
				mTabsAdapter
				.addTab(mTabHost.newTabSpec("Stream").setIndicator(
						convertView), EditStreamingActivity.class, null);
				break;
			case 1:
				title.setText("Category");
				imageView.setImageResource(R.drawable.category_tabselector);
				mTabsAdapter.addTab(mTabHost.newTabSpec("Category")
						.setIndicator(convertView),
						MainScreen_CategoryActivity.class, null);
				break;
			case 2:
				title.setText("Crew");
				imageView.setImageResource(R.drawable.crew_tabselector);
				mTabsAdapter.addTab(
						mTabHost.newTabSpec("Crew").setIndicator(convertView),
						MyFriendsFragment.class, null);
				break;
			case 3:
				title.setText("Account");
				imageView.setImageResource(R.drawable.account_tabselector);
				mTabsAdapter.addTab(mTabHost.newTabSpec("Account")
						.setIndicator(convertView), MyAccountActivity.class,
						null);
				break;

			default:
				break;
			}

		}

		screteKey = Utils.getDataString(BrainSlamToggleMenu.this,
				Constants.PREFS_NAME, Constants.SP_SECRET_KEY);
		userId = Utils.getDataString(BrainSlamToggleMenu.this,
				Constants.PREFS_NAME, Constants.SP_USER_ID);

		userdetail=new GetUserDetails();
		userdetail.execute(); 

		/*
		 * mTabsAdapter.addTab(mTabHost.newTabSpec("Stream").setIndicator(
		 * convertView), EditStreamingActivity.class, null); }
		 */
		/*
		 * mTabsAdapter.addTab(mTabHost.newTabSpec("Category").setIndicator(
		 * "Category", getResources().getDrawable(R.drawable.category)),
		 * MainScreen_CategoryActivity.class, null);
		 * 
		 * mTabsAdapter.addTab(mTabHost.newTabSpec("Crew").setIndicator("Crew",
		 * getResources().getDrawable(R.drawable.crew)), MyFriends.class, null);
		 * 
		 * mTabsAdapter.addTab(mTabHost.newTabSpec("Account").setIndicator("Account"
		 * , getResources().getDrawable(R.drawable.setting)),
		 * MyAccountActivity.class, null);
		 */
		// mTabsAdapter.addTab(mTabHost.newTabSpec("Back").setIndicator("",getResources().getDrawable(R.drawable.forwardarrow)),
		// MyAccountActivity.class, null);
		/*for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {

			TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i)
					.findViewById(android.R.id.title);
			tv.setTextColor(Color.WHITE);

		}*/

		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
		backforwardToggleButton = (ImageView) findViewById(R.id.backforward);
		backforwardToggleButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
//			    Intent startBranSlamActivity = new Intent(BrainSlamToggleMenu.this,BrainSlamMainActivity.class);
//				startActivity(startBranSlamActivity);
			    BrainSlamToggleMenu.this.finish();
			    
				BrainSlamToggleMenu.this.overridePendingTransition(
						R.anim.in_from_right, R.anim.out_to_left);

			}
		});

	}


	public void removeStreamClick(View view)
	{
		int pos = (Integer) view.getTag();
		Log.d("ESA", "removeStreamClick clicked pos::" + pos);

		Cursor cursor = HomeStreamManager.getInstance(
				this.getApplicationContext()).getHomeStreamCursor();

		cursor.moveToFirst();

		int index = 0;

		while (cursor.moveToNext()) {
			Log.d("ESA",
					"cursor data::"
					+ cursor.getString(cursor
							.getColumnIndex("HomeItemsName")));
			// Log.d("ESA",
			// "**removeStreamClick clicked pos::"+cursor.getInt(cursor.getColumnIndex("_id")));
			if (pos == index) {
				// Log.d("ESA",
				// "**removeStreamClick name clicked::"+cursor.getString(cursor.getColumnIndex("HomeItemsName")));

				cursor.moveToPrevious();
				break;
			}
			index++;
		}
		// cursor.moveToFirst();


		//		HomeStreamManager.getInstance(this).removeStream(
		//				cursor.getInt(cursor.getColumnIndex("_id")));
		//
		//
		//		Log.d("ESA", "remove stream cursor count::"+cursor.getCount()+ "  index::"+cursor.getColumnIndex("_id"));

		String itemType = "", itemId = "";
		if(cursor.moveToNext())
		{
			cursor.moveToPrevious();

			itemType = cursor.getString(cursor.getColumnIndex("HomeItemType"));
			itemId = cursor.getInt(cursor.getColumnIndex("HomeItemID"))+"";

			//			HomeStreamManager.getInstance(this).removeStreamByItemId(cursor.getInt(cursor.getColumnIndex("HomeItemID")));

			Log.d("ESA", "remove stream cursor index::"+cursor.getInt(cursor.getColumnIndex("_id")));
			HomeStreamManager.getInstance(this).removeStream(cursor.getInt(cursor.getColumnIndex("_id")));
			updateCategoryDb(cursor);
		}
		else
		{
			cursor.moveToPrevious();
			Log.d("ESA", "remove stream cursor index::"+cursor.getInt(cursor.getColumnIndex("_id")));

			itemType = cursor.getString(cursor.getColumnIndex("HomeItemType"));
			itemId = cursor.getInt(cursor.getColumnIndex("HomeItemID"))+"";

			HomeStreamManager.getInstance(this).removeStreamByItemId(
					cursor.getInt(cursor.getColumnIndex("HomeItemID")));
			//			HomeStreamManager.getInstance(this).deleteLastItem();

			updateCategoryDb(cursor);
		}


		cursor = HomeStreamManager.getInstance(this.getApplicationContext())
		.getHomeStreamCursor();


		final DragNDropCursorAdapter adapter = new DragNDropCursorAdapter(this,
				R.layout.edit_mystream_listitem, cursor,
				new String[] { "HomeItemsName" },
				new int[] { R.id.stream_name }, R.id.iv_stream_handler);

		EditStreamingActivity.dndStreamList.setDragNDropAdapter(adapter);
		adapter.notifyDataSetChanged();


		//		userUpdateHomeStream(
		//				cursor.getString(cursor.getColumnIndex("HomeItemType")),
		//				cursor.getString(cursor.getColumnIndex("HomeItemID")));


		//		Log.d("ESA", "db itemType::"+(cursor.getString(cursor.getColumnIndex("HomeItemType")))+
		//				"  db itemId::"+(cursor.getString(cursor.getColumnIndex("HomeItemID"))));

		Log.d("ESA", "itemType::"+itemType+"  itemId::"+itemId);
		userUpdateHomeStream(itemType, itemId);

	}

	private void updateCategoryDb(Cursor cursor)
	{
		List<Category> categories = CategoryManager.getInstance(BrainSlamToggleMenu.this).getCategories();

		for (int i = 0; i < categories.size(); i++) 
		{
			if(categories.get(i).categoryName.equalsIgnoreCase(
					cursor.getString(cursor.getColumnIndex("HomeItemsName"))))
			{
				categories.get(i).isOnHomePage = false;
				break;
			}
		}

		CategoryManager.getInstance(BrainSlamToggleMenu.this).removeAllCategories();
		CategoryManager.getInstance(BrainSlamToggleMenu.this).saveCategories(categories);
	}

	private void userUpdateHomeStream(String type, String itemId) {
		String secretKey = Utils.getDataString(BrainSlamToggleMenu.this,
				Constants.PREFS_NAME, Constants.SP_SECRET_KEY);
		String userID = Utils.getDataString(BrainSlamToggleMenu.this,
				Constants.PREFS_NAME, Constants.SP_USER_ID);

		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

		params.add(new BasicNameValuePair("operation",
		"updateSingleHomeStreamItem"));
		params.add(new BasicNameValuePair("Secret_Key", secretKey));
		params.add(new BasicNameValuePair("User_Id", userID));
		params.add(new BasicNameValuePair("Action", "delete"));
		params.add(new BasicNameValuePair("Type", type));
		params.add(new BasicNameValuePair("Home_Item_ID", itemId));

		new AsyncHandler(BrainSlamToggleMenu.this, Constants.SERVER_URL
				+ "user.php?", 0, false, params).execute();
	}

	/*public void updateCategory(View V) {
		if (hashSetRecommended.size() > 0) {

			final String secretKey = Utils.getDataString(
					BrainSlamToggleMenu.this, Constants.PREFS_NAME,
					Constants.SP_SECRET_KEY);
			final String userID = Utils.getDataString(BrainSlamToggleMenu.this,
					Constants.PREFS_NAME, Constants.SP_USER_ID);

			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

			params.add(new BasicNameValuePair("operation",
					"userUpdateHomeStream"));
			params.add(new BasicNameValuePair("Secret_Key", secretKey));
			params.add(new BasicNameValuePair("User_Id", userID));

			StringBuilder builder = new StringBuilder();

			HashMap<String, HashSet<String>> hashMap = new HashMap<String, HashSet<String>>();

			JSONObject jsonObject = new JSONObject();
			JSONArray array = new JSONArray();
			try {

				for (String str : hashSetRecommended)

				{
					array.put(Integer.parseInt(str));
				}

				jsonObject.put("Category", (Object) array);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println(" DONE click:: " + jsonObject.toString());

			params.add(new BasicNameValuePair("updateStreams", jsonObject
					.toString()));

			new AsyncHandler(BrainSlamToggleMenu.this, Constants.SERVER_URL
					+ "user.php?", DONE_CLICK, false, params).execute();

		} else {

			AlertDialog.Builder alert = new AlertDialog.Builder(con);
			alert.setTitle("Select category to proceed");
			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}

					});
			alert.show();

		}
	}*/

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		super.onBackPressed();
	}
	/*public void catchecked(View v)
	{
		//recommended grid view
		RelativeLayout rl=(RelativeLayout) v.getParent();
		//			ImageView Iv=(TextView) rl.getChildAt(0);
		CheckBox chkcategory=(CheckBox) rl.getChildAt(1);

		if(chkcategory.isChecked()){
			System.out.println("rutuja category id:: "+((Category)chkcategory.getTag()).categoryId);
			hashSetRecommended.add(""+((Category)chkcategory.getTag()).categoryId);


		}else if(!chkcategory.isChecked())
		{
			if(hashSetRecommended.contains(""+((Category)chkcategory.getTag()).categoryId))
				hashSetRecommended.remove(""+((Category)chkcategory.getTag()).categoryId);
		}
	}
	 */


	public void updateCategoryList(int categoryId, boolean isOnHomeScreen)
	{

		List<Category> categoryDbList = new ArrayList<Category>();
		categoryDbList = CategoryManager.getInstance(getApplicationContext()).getCategories();

		for(Category category: categoryDbList)
		{
			if(category.categoryId == categoryId)
			{
				category.isOnHomePage = isOnHomeScreen;
				break;
			}
		}

		CategoryManager.getInstance(getApplicationContext()).removeAllCategories();
		CategoryManager.getInstance(getApplicationContext()).saveCategories(categoryDbList);

	}
	public void listcheck(final View v) {
		Log.e("In", "checklist");

		//
		//		new Handler().postDelayed(new Runnable() {
		//
		//			@Override
		//			public void run() 
		//			{
		//				RelativeLayout rl = (RelativeLayout) v.getParent();
		//				CheckBox chkcategory = (CheckBox) rl.getChildAt(1);
		//				if (chkcategory.isChecked()) {
		//					if (chkcategory.getTag().getClass().equals(Category.class)) {
		//						hashSetRecommended.add(""
		//								+ ((Category) chkcategory.getTag()).categoryId);
		//						updateCategoryList(((Category) chkcategory.getTag()).categoryId,true);
		//
		//					}
		//					Log.e("addCheck", "" + hashSetRecommended);
		//				} else {
		//
		//					//										if (chkcategory.getTag().getClass().equals(Category.class)) {
		//					if (hashSetRecommended.contains(""
		//							+ ((Category) chkcategory.getTag()).categoryId))
		//						hashSetRecommended.remove(""
		//								+ ((Category) chkcategory.getTag()).categoryId);
		//					updateCategoryList(((Category) chkcategory.getTag()).categoryId,false);
		//					//										}
		//				}
		//				Log.e("CheckList",""+hashSetRecommended);	
		//			}
		//		}, 100);


	}

	public static class TabsAdapter extends FragmentPagerAdapter implements
	TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
		private final Context mContext;
		private final TabHost mTabHost;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		static final class TabInfo {
			private final String tag;
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(String _tag, Class<?> _class, Bundle _args) {
				tag = _tag;
				clss = _class;
				args = _args;
			}
		}

		static class DummyTabFactory implements TabHost.TabContentFactory {
			private final Context mContext;

			public DummyTabFactory(Context context) {
				mContext = context;
			}

			public View createTabContent(String tag) {
				View v = new View(mContext);
				v.setMinimumWidth(0);
				v.setMinimumHeight(0);
				return v;
			}
		}

		public TabsAdapter(FragmentActivity activity, TabHost tabHost,
				ViewPager pager) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mTabHost = tabHost;
			mViewPager = pager;
			mTabHost.setOnTabChangedListener(this);
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
			tabSpec.setContent(new DummyTabFactory(mContext));
			String tag = tabSpec.getTag();

			TabInfo info = new TabInfo(tag, clss, args);
			mTabs.add(info);
			mTabHost.addTab(tabSpec);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override

		public Fragment getItem(int position)
		{
			//        	if(position == 4)
			//        		return null;


			TabInfo info = mTabs.get(position);

			return Fragment.instantiate(mContext, info.clss.getName(),
					info.args);
		}

		public void setTabColor(TabHost mTabHost) {
			// TODO Auto-generated method stub
			for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++)
				mTabHost.getTabWidget().getChildAt(i)
				.setBackgroundColor(Color.BLACK); // unselected

			if( mTabHost.getCurrentTab()>=0)
			{

				mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab())
				.setBackgroundColor(BrainSlamToggleMenu.color); // 1st
				// tab
				// selected
			}
		}


		public void onTabChanged(String tabId)
		{
			Log.d("BSMS", "tab id::"+tabId);
			if(!tabId.equalsIgnoreCase("back"))
			{


				int position = mTabHost.getCurrentTab();
				mViewPager.setCurrentItem(position);
				setTabColor(mTabHost);
			} else {
				Intent startMainScreen = new Intent(mContext,
						BrainSlamMainActivity.class);
				mContext.startActivity(startMainScreen);
				((Activity) mContext).overridePendingTransition(
						R.anim.slidein_right, R.anim.slideout_left);
			}
		}

		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			Log.d("BSMS", "onPageScrolled position::" + position);

		}

		public void onPageSelected(int position) {
			Log.d("BSMS", "onPageSelected position::" + position);

			if (position != 4) {
				TabWidget widget = mTabHost.getTabWidget();
				int oldFocusability = widget.getDescendantFocusability();
				widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
				mTabHost.setCurrentTab(position);
				widget.setDescendantFocusability(oldFocusability);
			}
		}

		public void onPageScrollStateChanged(int state) {

		}
	}

	@Override
	public void onReceive(JSONObject jsonObj, int id) {

		Log.e("kiran", "" + jsonObj.toString());
		switch (id) {
		case Constants.DONE_CLICK:
			try {
				if (jsonObj.has("status") && jsonObj.getInt("status") == 0
						&& jsonObj.getInt("message") == 6) {
					/*Toast.makeText(BrainSlamToggleMenu.this, "Successfully update",
							Toast.LENGTH_LONG).show();*/

//					CategoryManager.getInstance(getApplicationContext()).removeAllCategories();
//					CategoryManager.getInstance(getApplicationContext()).saveCategories(MainScreen_CategoryActivity.categoryList);
				} else if (jsonObj.has("status") && jsonObj.getInt("status") == 1) {

					String msg = Utils
					.getResponseMessage(jsonObj.getInt("message"));
					Toast.makeText(BrainSlamToggleMenu.this, msg, Toast.LENGTH_LONG)
					.show();

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		default:
			break;
		}


	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
Log.v("", "onpause CategoryManager size::"+CategoryManager.getInstance(this).getCategories().size());
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	//Changes
	private class GetUserDetails extends AsyncTask<Void, Void, JSONObject>
	{


		private static final int YES = 1;
		ArrayList< FeaturedMediaListData> mediaList = new ArrayList<FeaturedMediaListData>();
		@Override
		protected JSONObject doInBackground(Void... params) {
			Gson gson =new Gson();
			// TODO Auto-generated method stub

			List<BasicNameValuePair> params1= new ArrayList<BasicNameValuePair>();
			params1.add(new BasicNameValuePair("operation", "getUserDetails"));
			params1.add(new BasicNameValuePair("Secret_Key", screteKey));
			params1.add(new BasicNameValuePair("User_Id", userId));
			params1.add(new BasicNameValuePair("Details_Of", userId));
			params1.add(new BasicNameValuePair("Complete_Details",String.valueOf(YES)));
			JSONObject userJson = HttpCommunicator.callRsJson(Constants.SERVER_URL
					+ "user.php?",false, params1,BrainSlamToggleMenu.this);
			org.json.JSONObject jsonDataObject;
			UserDetails userDataObject = new UserDetails();
			try {
				jsonDataObject = userJson.getJSONObject("data");

				JSONObject jsonUserObject=jsonDataObject.getJSONObject("User_Details");
				JSONObject jsonMediaObject=jsonDataObject.getJSONObject("Media");
				JSONObject MediaData = new JSONObject(jsonMediaObject.toString());
				@SuppressWarnings("unused")
				org.json.JSONArray jsonCrewArray = MediaData.getJSONArray("data");
				FeaturedMediaListData[] mediaDataArr = gson.fromJson(
						jsonCrewArray.toString(),FeaturedMediaListData[].class);
				for (int i = 0; i < mediaDataArr.length; i++) {
					FeaturedMediaListData mediaObject = new FeaturedMediaListData();
					mediaObject.Average_IQ=mediaDataArr[i].Average_IQ;
					mediaObject.Categories=mediaDataArr[i].Categories;
					mediaObject.Created_At=mediaDataArr[i].Created_At;
					mediaObject.Data_Url=mediaDataArr[i].Data_Url;
					mediaObject.Description=mediaDataArr[i].Description;
					mediaObject.Download_Url=mediaDataArr[i].Download_Url;
					mediaObject.Duration=mediaDataArr[i].Duration;
					mediaObject.Height=mediaDataArr[i].Height;
					mediaObject.Media_Id=mediaDataArr[i].Media_Id;
					mediaObject.Media_Owner=mediaDataArr[i].Media_Owner;
					mediaObject.Media_Type=mediaDataArr[i].Media_Type;
					mediaObject.Name=mediaDataArr[i].Name;
					mediaObject.Number_Of_Ratings=mediaDataArr[i].Number_Of_Ratings;
					mediaObject.Searched_Category=mediaDataArr[i].Searched_Category;
					mediaObject.Tags=mediaDataArr[i].Tags;
					mediaObject.Thumbnail_Url=mediaDataArr[i].Thumbnail_Url;
					mediaObject.Tranding_Score=mediaDataArr[i].Tranding_Score;
					mediaObject.Updated_At=mediaDataArr[i].Updated_At;
					mediaObject.Views=mediaDataArr[i].Views;
					mediaObject.Width=mediaDataArr[i].Width;

					mediaList.add(mediaObject);
				}
				userDataObject=gson.fromJson(jsonUserObject.toString(),UserDetails.class);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return userJson;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			UserDtailsJsonObject=result;
			dialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog.show();
			super.onPreExecute();

		}

	}


@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	userdetail=new GetUserDetails();
	userdetail.execute();
	
	Log.v("", "onpause CategoryManager size::"+CategoryManager.getInstance(this).getCategories().size());
}






}
