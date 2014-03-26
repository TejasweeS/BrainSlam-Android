package com.android.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import com.android.brainslam.R;


public class CustomSpinnerDialog extends Dialog{

	public CustomSpinnerDialog(Context context) {
		super(context);
		
		
		setContentView(R.layout.spinner);
		setCancelable(true);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		
		
//		setContentView(R.layout.spinner);
//		setCancelable(false);
//		setTitle("Brain-Slam");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setCancelable(boolean flag) {
		// TODO Auto-generated method stub
		super.setCancelable(flag);
	}
	@Override
	public void setTitle(CharSequence title) {
		// TODO Auto-generated method stub
//		super.setTitle("Brain-Slam");
	}
}
