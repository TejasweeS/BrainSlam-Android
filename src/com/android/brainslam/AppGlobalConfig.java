package com.android.brainslam;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import com.android.brainslam.db.DataBaseHelper;

public class AppGlobalConfig extends Application 
{
	
	DataBaseHelper dbHelper;
	private static AppGlobalConfig instance;
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
		Log.v("AppGlobalConfig: ", "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}

	public AppGlobalConfig()
	{
		instance = this;
	}

	public static AppGlobalConfig getInstance()
	{
		return instance;
	}
	
	@Override
	public void onCreate() 
	{
		System.out.println("App created");
		super.onCreate();
	
//        dbHelper = DataBaseHelper.getInstance();
		
		Log.v("onCreate"," AppGlobalConfig onCreate");
	}

	@Override
	public void onLowMemory() 
	{
		super.onLowMemory();
		Log.v("AppGlobalConfig: ", "onLowMemory");
	}

	@Override
	public void onTerminate() 
	{
		super.onTerminate();
	}
}