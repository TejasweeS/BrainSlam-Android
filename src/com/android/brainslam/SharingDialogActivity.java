package com.android.brainslam;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class SharingDialogActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.sharing_popup);
		TextView textView=(TextView)findViewById(R.id.sharing_facebook);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		
		textView.setWidth(displaymetrics.widthPixels-20);
		
	}
	
public void popup(View v)
{
	final Dialog dia =new Dialog(this);
	dia.setContentView(R.layout.sharing_popup);
	dia.setTitle("Title...");
	WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    lp.copyFrom(dia.getWindow().getAttributes());
    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
    dia.show();
    dia.getWindow().setAttributes(lp);
    }
}
