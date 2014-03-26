package com.android.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;

import com.android.brainslam.R;


public class CustomAlertDialog{

	AlertDialog.Builder alert;
	public ContextThemeWrapper con;
	
	public CustomAlertDialog(Context context,String text) {
		
		// TODO Auto-generated constructor stub
		con = new ContextThemeWrapper(context, R.style.alert);	
		 alert = new AlertDialog.Builder(con);
		alert.setTitle(text);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}


		});
	
		
		
	}

	
public void showDialog()
{
	alert.show();
}
	
}
