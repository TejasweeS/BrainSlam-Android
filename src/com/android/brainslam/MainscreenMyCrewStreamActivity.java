package com.android.brainslam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainscreenMyCrewStreamActivity extends Activity{
	Button addNewCrew;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainscreen_crew_myscrew_stream);
		addNewCrew=(Button)findViewById(R.id.mainscreen_crew_addnew_screw); 
		addNewCrew.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent createNewCrewActivity = new Intent(MainscreenMyCrewStreamActivity.this,AddOrDeleteCrewActivity.class );
				startActivity(createNewCrewActivity);
				
				
			}
		});
		
	}

}
