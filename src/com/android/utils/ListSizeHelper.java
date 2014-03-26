package com.android.utils;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListSizeHelper
{
	 public static void getListViewSize(ListView myListView) {
	        ListAdapter myListAdapter = myListView.getAdapter();
	        if (myListAdapter == null) {
	            //do nothing return null
	            return;
	        }
	        //set listAdapter in loop for getting final size
	        int totalHeight = 0;
	        for (int size = 0; size < myListAdapter.getCount(); size++) {
	            View listItem = myListAdapter.getView(size, null, myListView);
	            listItem.measure(0, 0);
	            totalHeight += listItem.getMeasuredHeight();
	        }
	      //setting listview item in adapter
	        ViewGroup.LayoutParams params = myListView.getLayoutParams();
	        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
	        myListView.setLayoutParams(params);
	        // print height of adapter on log
	        Log.i("height of listItem:", String.valueOf(totalHeight));
	    }
	 
	 
	 public static void getGridViewSize(GridView myGridView,int noOfColumns) {
	        BaseAdapter myGridAdapter = (BaseAdapter) myGridView.getAdapter();
	        if (myGridAdapter == null) {
	            //do nothing return null
	            return;
	        }
	        
	        Log.v("rutuja", "myGridAdapter.getCount(): "+myGridAdapter.getCount());
	        //set listAdapter in loop for getting final size
	        int totalHeight = 0;
	        for (int size = 0; size < myGridAdapter.getCount()/noOfColumns; size++) {
	            View listItem = myGridView.getChildAt(0);
//	            listItem.measure(0, 0);
//	            totalHeight += listItem.getLayoutParams().height;
	        }
	        
	        Log.v("rutuja", "height:: "+totalHeight);
	        
	        
//	        ViewGroup.LayoutParams params = myGridView.getLayoutParams();
//	        params.height = totalHeight + ((myListAdapter.getCount() - 1));
//	        myGridView.setLayoutParams(params);
//	        // print height of adapter on log
//	        Log.i("height of listItem:", String.valueOf(totalHeight));
	    }
}
