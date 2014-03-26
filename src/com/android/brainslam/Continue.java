package com.android.brainslam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Continue extends Activity
{
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.continuescreen);

Button bk=(Button) findViewById(R.id.button1);
bk.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	Continue.this.finish();
	startActivity(new Intent(Continue.this,ScreencaptureActivity.class));
	
	}
});
}
}
