package com.android.brainslam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Backfromcamera extends Activity
{
@Override
protected void onCreate(Bundle savedInstanceState)
{
	super.onCreate(savedInstanceState);
setContentView(R.layout.backfromcam);

Button bk=(Button) findViewById(R.id.button1);
bk.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	Backfromcamera.this.finish();
	startActivity(new Intent(Backfromcamera.this,ScreencaptureActivity.class));
	
	}
});
	}
}
