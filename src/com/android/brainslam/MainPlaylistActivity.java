package com.android.brainslam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainPlaylistActivity extends Activity
{
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_mainplaylist);
	
		
}
public void editclk(View v)
{

	startActivity(new Intent(MainPlaylistActivity.this,PlaylistActivity.class));
	
}
public void backclick(View v)
{
	MainPlaylistActivity.this.finish();
	//startActivity(new Intent(MainPlaylistActivity.this,BrainSlamMainActivity.class));
	
}
}
