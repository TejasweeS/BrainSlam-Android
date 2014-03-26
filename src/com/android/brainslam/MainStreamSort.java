package com.android.brainslam;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.external_api.android_wheel.adapters.ArrayWheelAdapter;
import com.external_api.android_wheel.widget.OnWheelChangedListener;
import com.external_api.android_wheel.widget.WheelView;

public class MainStreamSort extends Activity {

	
	WheelView sortByDateWheelView, sortByStreamWheelView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_stream_sort);
		
		
		sortByDateWheelView = (WheelView)findViewById(R.id.sort_by_date_wheel);
		sortByStreamWheelView = (WheelView)findViewById(R.id.sort_by_stream_wheel);
		
		
		String[] arrayDateWheel = {"Today","This Week","This Month","This Year"};
		String[] arrayStramWheel = {"New","Popular","Most Viewes","Most Rated", "IQ - High To Low","IQ - Low To High"};
		
		
		final ArrayWheelAdapter<String> dateAdapter = new ArrayWheelAdapter<String>(MainStreamSort.this, arrayDateWheel);
		sortByDateWheelView.setViewAdapter(dateAdapter);
		sortByDateWheelView.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				dateAdapter.setTextColor(getResources().getColor(R.color.orange_kaltura));
				wheel.setViewAdapter(dateAdapter);
			}
		});
		
		ArrayWheelAdapter<String> streamAdapter =new ArrayWheelAdapter<String>(MainStreamSort.this, arrayStramWheel); 
		
		sortByStreamWheelView.setViewAdapter(streamAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_stream_sort, menu);
		return true;
	}

}
