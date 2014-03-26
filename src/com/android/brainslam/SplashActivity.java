package com.android.brainslam;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		final ImageView splash = (ImageView) this.findViewById(R.id.splash);

		splash.postDelayed(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				splash.setVisibility(View.GONE);
				
			}
		  
		}, 3000);
	}
}
