package com.android.brainslam.mainscreen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.view.inputmethod.EditorInfo;

import android.widget.ListView;

import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.android.brainslam.R;
import com.android.brainslam.NetworkHandler.AsyncCallBack;
import com.android.brainslam.NetworkHandler.AsyncHandlerSansSpinner;
import com.android.brainslam.constants.Constants;
import com.android.brainslam.db.dao.Category;
import com.android.brainslam.manager.CategoryManager;
import com.android.brainslam.vo.MyFriendsVo;
import com.android.listdapters.LandingCategoryAdapter;
import com.android.listdapters.MainScreenCategoryAdapter;
import com.android.utils.Utils;

public class MainScreen_CategoryActivity extends Fragment implements AsyncCallBack
{
	private static final int DONE_CLICK =1;
	ListView categorylistview;
	public static ArrayList<Category> categoryList ;
	String userId;
	String screteKey;
//	Button done;
    TextView searchtext;
    MainScreenCategoryAdapter category;
    public static HashSet<String> hashSetRecommended = new HashSet<String>();
    LandingCategoryAdapter categoryAdapter;
    ArrayList<MyFriendsVo> friendsCategoryList = new ArrayList<MyFriendsVo>();
    String[] subcategories = {"Brain-Slam", "Intense", "Clunker", "Sick & Wrong", "Disturbed", "Featured"};
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
		View view = inflater.inflate(R.layout.activity_main_screen__category, container, false);
        return view;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onActivityCreated(savedInstanceState);
		categorylistview = (ListView) getView().findViewById(R.id.mainscreen_category_listView);
		searchtext = (TextView) getView().findViewById(R.id.mainscreen_category_searchbar);
        categoryList = new ArrayList<Category>();
		hashSetRecommended = new HashSet<String>();
		categoryList = (ArrayList<Category>) CategoryManager.getInstance(getActivity()).getCategories();

		int i,j;
		for( i=0;i < subcategories.length ; i++)
			for( j=0;j < categoryList.size() ; j++)
		        if(categoryList.get(j).categoryName.equalsIgnoreCase(subcategories[i]))
				categoryList.remove(j);
		
			Log.d("List",""+categoryList);
		//categoryAdapter=new LandingCategoryAdapter(getActivity(), categoryList,friendsCategoryList);
    	category = new MainScreenCategoryAdapter(getActivity(), categoryList);
		categorylistview.setAdapter(category);
		searchtext.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				ArrayList<Category> categoriesSearch = new ArrayList<Category>();

				for(int i=0;i<categoryList.size();i++)
				{

				if(i < categoryList.size())
				{
					String playerName = categoryList.get(i).categoryName;
					Log.e("ENter",""+searchtext.getText().toString().trim());
					//compare the String in EditText with Names in the ArrayList
					if(Utils.searchData(searchtext.getText().toString().trim(), playerName)){
					  categoriesSearch.add(categoryList.get(i));
					}
				}
				}
				category = new MainScreenCategoryAdapter(getActivity(), categoriesSearch);
				categorylistview.setAdapter(category);
				if(categoriesSearch!=null)
				{
                hideKeyboard();
				}
				/*categoryAdapter=new LandingCategoryAdapter(getActivity(),categoriesSearch,null);
				categorylistview.setAdapter(categoryAdapter);
				categoryAdapter.notifyDataSetChanged();*/

				category.notifyDataSetChanged();
				if(searchtext.getText().toString().trim().length() == 0)
				{
					hideKeyboard();
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	    //hide keypad touch on screen
		LinearLayout relative = (LinearLayout) getActivity().findViewById(R.id.categoryrelative);
		relative.setOnTouchListener(new OnTouchListener() {

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
		
		categorylistview.setOnTouchListener(new OnTouchListener() {

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
		searchtext.setOnEditorActionListener(new OnEditorActionListener() {

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
			}
	
	private void hideKeyboard()
	{
		try
		{
			InputMethodManager inputMethodManager = (InputMethodManager) 
					getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);

			inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	 
	   @Override
	public void onPause() {
		// TODO Auto-generated method stub
		   
		   super.onPause();
//		   HashSet<String>hashSetRecommended=hashSetRecommended;
//		   if (hashSetRecommended.size() > 0) {

			   screteKey = Utils.getDataString(getActivity(),
						Constants.PREFS_NAME, Constants.SP_SECRET_KEY);
		 		userId = Utils.getDataString(getActivity(),
						Constants.PREFS_NAME, Constants.SP_USER_ID);
	          
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

				params.add(new BasicNameValuePair("operation",
						"userUpdateHomeStream"));
				params.add(new BasicNameValuePair("Secret_Key", screteKey));
				params.add(new BasicNameValuePair("User_Id", userId));

//				JSONObject jsonObject = new JSONObject();
//				JSONArray array = new JSONArray();
//				try {
//
//					for (String str : hashSetRecommended)
//
//					{
//						array.put(Integer.parseInt(str));
//					}
//
//					jsonObject.put("Category", (Object) array);
//
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
				
				categoryList = (ArrayList<Category>) CategoryManager.getInstance(getActivity().getApplicationContext()).getCategories();
				StringBuilder builder = new StringBuilder();
				builder.append("{\"Category\":\"[");
				boolean gotCat = false;
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
				builder.append("]\"}");
				System.out.println(" DONE click:: " + builder.toString());

				params.add(new BasicNameValuePair("updateStreams", builder
						.toString()));

				new AsyncHandlerSansSpinner(getActivity(), Constants.SERVER_URL
						+ "user.php?", Constants.DONE_CLICK, false, params).execute();
		
//	}


 }

	@Override
	public void onReceive(JSONObject jsonObj, int id) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		 HashSet<String>hashSetRecommended = hashSetRecommended;
//		   if (hashSetRecommended.size() > 0) {

			   screteKey = Utils.getDataString(getActivity(),
						Constants.PREFS_NAME, Constants.SP_SECRET_KEY);
		 		userId = Utils.getDataString(getActivity(),
						Constants.PREFS_NAME, Constants.SP_USER_ID);
	          
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

				params.add(new BasicNameValuePair("operation",
						"userUpdateHomeStream"));
				params.add(new BasicNameValuePair("Secret_Key", screteKey));
				params.add(new BasicNameValuePair("User_Id", userId));

				categoryList = (ArrayList<Category>) CategoryManager.getInstance(getActivity().getApplicationContext()).getCategories();
				StringBuilder builder = new StringBuilder();
				builder.append("{\"Category\":\"[");
				boolean gotCat = false;
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
				builder.append("]\"}");

				System.out.println(" DONE click:: " + builder.toString());

				params.add(new BasicNameValuePair("updateStreams", builder
						.toString()));

				new AsyncHandlerSansSpinner(getActivity(), Constants.SERVER_URL
						+ "user.php?", Constants.DONE_CLICK, false, params).execute();
		
//	}

	}
}
