package com.android.brainslam;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.brainslam.NetworkHandler.AsyncCallBack;
import com.android.brainslam.NetworkHandler.AsyncHandler;
import com.android.brainslam.constants.Constants;
import com.android.utils.Utils;

public class NavigationTest extends Activity implements AsyncCallBack
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
	NavigationTest.this.finish();
	startActivity(new Intent(NavigationTest.this,ScreencaptureActivity.class));
	
	}
});

Button bdetails=(Button) findViewById(R.id.button2);
bdetails.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String deviceID = tm.getDeviceId(); 
		String secretKey = Utils.md5(Constants.BRAINSLAMSECRETKEY+ deviceID);
		
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("operation","getRelatedMedia"));
		params.add(new BasicNameValuePair("Media_Id","0_w1dwjv7h"));
		params.add(new BasicNameValuePair("User_Id","1"));
		params.add(new BasicNameValuePair("Secret_Key","2ca45896e7ed01b20cbbcb96e37460e4"));

		new AsyncHandler(NavigationTest.this, Constants.SERVER_URL+
				"media.php?", Constants.LOGIN_ID, true,params)
		.execute();
	
		//	http://10.0.0.201:8888/BrainSlam_API/media.php
		//	?operation=getRelatedMedia&Media_Id=0_w1dwjv7h&User_Id=1&Secret_Key=2ca45896e7ed01b20cbbcb96e37460e4

		/*	NavigationTest.this.finish();
			Intent intent=new Intent(NavigationTest.this,VideoDetailsActivity.class);
			startActivity(intent);*/
	
		}
	});

}

@Override
public void onReceive(JSONObject jsonObj, int id) {
	// TODO Auto-generated method stub
	System.out.println("resp :: "+jsonObj);
	System.out.println("id :: "+id);
	

}


}
