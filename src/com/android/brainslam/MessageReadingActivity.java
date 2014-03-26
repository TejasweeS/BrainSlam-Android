package com.android.brainslam;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MessageReadingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message__reading);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message__reading, menu);
		return true;
	}

}
