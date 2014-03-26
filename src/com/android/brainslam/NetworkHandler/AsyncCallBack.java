package com.android.brainslam.NetworkHandler;

import org.json.JSONObject;

public interface AsyncCallBack 
{
	void onReceive(JSONObject jsonObj, int id);
}