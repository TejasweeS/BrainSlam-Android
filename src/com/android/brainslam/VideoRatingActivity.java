package com.android.brainslam;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.android.brainslam.NetworkHandler.HttpCommunicator;
import com.android.brainslam.constants.Constants;
import com.android.utils.CustomAlertDialog;
import com.android.utils.CustomSpinnerDialog;
import com.android.utils.ImageLoader;
import com.android.utils.Utils;

public class VideoRatingActivity extends Activity implements OnSeekBarChangeListener {

	Button ratingValue;
	int IQScore=0;
	boolean isFromUploadScreen;
	ImageView videoThumbnailImageView;
	CustomSpinnerDialog prog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_rating);
		prog = new CustomSpinnerDialog(VideoRatingActivity.this);
		isFromUploadScreen = getIntent().getExtras().getBoolean("FromUploadScreen");
		Log.v("", "rutuja isFromUploadScreen"+isFromUploadScreen);
		String mediaID = getIntent().getExtras().getString("MediaID");
		
		String thumbnailUrl = getIntent().getExtras().getString("ThumbnailUrl");
		videoThumbnailImageView = (ImageView) findViewById(R.id.videothumb);
		
		if(thumbnailUrl.trim().length() > 0)
			new ImageLoader(VideoRatingActivity.this).DisplayImage(thumbnailUrl, videoThumbnailImageView);
			
		System.out.println("rutuja Got MEDIA ID"+mediaID);

		ratingValue=(Button)findViewById(R.id.ratingValue);
		ratingValue.setVisibility(View.INVISIBLE);
		// Disturbed Listener
		SeekBar  rating=(SeekBar) findViewById(R.id.disturbed_seekBar);
		rating.setTag("0");

		rating.setOnSeekBarChangeListener(this);
		// Seek &  Wrong Listener
		rating=(SeekBar) findViewById(R.id.sick_seekBar);
		rating.setTag("1");
		rating.setOnSeekBarChangeListener(this);

		// Clunker
		rating=(SeekBar) findViewById(R.id.clunker_seekBar);
		rating.setTag("2");
		rating.setOnSeekBarChangeListener(this);

		// Inetnse
		rating=(SeekBar) findViewById(R.id.intense_seekBar);
		rating.setTag("3");
		rating.setOnSeekBarChangeListener(this);

		// Brain Slam
		rating=(SeekBar) findViewById(R.id.brainslam_seekBar);
		rating.setTag("4");
		rating.setOnSeekBarChangeListener(this);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		if(fromUser)
		{
			int rateingValue=Integer.parseInt(seekBar.getTag().toString());
			rateingValue=(rateingValue*30)+progress;
			System.out.println("Rating  Value");
			ratingValue.setVisibility(View.VISIBLE);
			ratingValue.setText("Rating "+rateingValue);
			Animation animation=AnimationUtils.loadAnimation(this, R.anim.fade_out);
			ratingValue.setAnimation(animation);
			IQScore=rateingValue;

		}

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
//		this.finish();
		new UploadVideoAsycTask().execute(1);


	}



	class UploadVideoAsycTask extends AsyncTask<Integer, Integer , JSONObject>
	{
		

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			
			prog.show();
			
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			JSONObject jsonResp = null;
			// 	Get Details of the Video From Shared Preference while Uploading Video Details
			if(isFromUploadScreen)
			{
				SharedPreferences preferences=getSharedPreferences("UploadDetails", Context.MODE_PRIVATE);
				String nameOfvideo=preferences.getString("videoName", "Test");
				String videoTags=preferences.getString("tags", "");
				String videoDescription=preferences.getString("description", "");
				boolean shareWithWorld=preferences.getBoolean("world", false);

				String categories=preferences.getString("selectedCategories", null);
				String friends=preferences.getString("selectedFriends", null);


				// Use Following Code For Video Upload

				int bytesRead, bytesAvailable, bufferSize;
				byte[] buffer;
				int maxBufferSize = 1 * 1024 * 1024;

				List<BasicNameValuePair> postParameters =new ArrayList<BasicNameValuePair>();
				BasicNameValuePair basicNameValuePair=new BasicNameValuePair("operation", "addMedia");
				postParameters.add(basicNameValuePair);

				basicNameValuePair=new BasicNameValuePair("Media_Extension", "3gp");
				postParameters.add(basicNameValuePair);

				basicNameValuePair=new BasicNameValuePair("Name", "SwapnilTest");
				postParameters.add(basicNameValuePair);

				basicNameValuePair=new BasicNameValuePair("User_Id", "190");
				postParameters.add(basicNameValuePair);

				basicNameValuePair=new BasicNameValuePair("Secret_Key", "a80ab72b03f5aee336e2726d8eb35002");
				postParameters.add(basicNameValuePair);

				FileInputStream fileInputStream=null;

				try {
					fileInputStream = new FileInputStream("/storage/sdcard0/WhatsApp/Media/WhatsApp Video/VID-20140226-WA0002.3gp");
					bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size

					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];

					// read file and write it into form...
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
					StringBuffer buffer2=new StringBuffer("&Media=");
					while (bytesRead > 0) {

						buffer2.append(Base64.encodeToString(buffer,Base64.DEFAULT));
						bytesAvailable = fileInputStream.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream.read(buffer, 0, bufferSize);
					}

					basicNameValuePair=new BasicNameValuePair("Media", buffer2.toString());

					postParameters.add(basicNameValuePair);
					fileInputStream.close();
					System.out.println("writing::"+buffer2.length());
					jsonResp = HttpCommunicator.callRsJson(Constants.SERVER_URL+"media.php?",false,postParameters,VideoRatingActivity.this);
					System.out.println("Rating Response:;"+jsonResp);

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
			else
			{
				if(null != VideoRatingActivity.this.getIntent().getExtras().getString("MediaID"))
				{
					String mediaID = VideoRatingActivity.this.getIntent().getExtras().getString("MediaID");
					System.out.println("MEDIA ID"+mediaID);
					List<BasicNameValuePair> postParameters =new ArrayList<BasicNameValuePair>();
					BasicNameValuePair basicNameValuePair=new BasicNameValuePair("operation", "updateUserMediaRating");
					postParameters.add(basicNameValuePair);

					basicNameValuePair=new BasicNameValuePair("Media_Id", mediaID);
					postParameters.add(basicNameValuePair);

					basicNameValuePair=new BasicNameValuePair("User_Id", Utils.getDataString(VideoRatingActivity.this, Constants.PREFS_NAME, Constants.SP_USER_ID));
					postParameters.add(basicNameValuePair);

					basicNameValuePair=new BasicNameValuePair("Rating", IQScore+"");
					postParameters.add(basicNameValuePair);

					basicNameValuePair=new BasicNameValuePair("Secret_Key", Utils.getDataString(VideoRatingActivity.this, Constants.PREFS_NAME, Constants.SP_SECRET_KEY));
					postParameters.add(basicNameValuePair);

					jsonResp = HttpCommunicator.callRsJson(Constants.SERVER_URL+"media.php?",false,postParameters,VideoRatingActivity.this);
					System.out.println("Rating Response:;"+jsonResp);
				}
				else
					Log.v("rutuja", "media id NULL");
			}
			return jsonResp;
		}

		@Override
		protected void onPostExecute(JSONObject object) {
			// TODO Auto-generated method stub
		
			prog.dismiss();
			try
			{
				if(object.has("status") && object.getInt("status")== 0 && object.getInt("message") == 6)
				{
					
					Intent intent=new Intent(VideoRatingActivity.this, BrainSlamMainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
					
				}
				else if(object.has("errMessage"))
				{
					if(object.getString("errMessage").equalsIgnoreCase("No Internet Connection"))
						new CustomAlertDialog(VideoRatingActivity.this,object.getString("errMessage")).showDialog();					
					else
						Toast.makeText(VideoRatingActivity.this,object.getString("errMessage"), Toast.LENGTH_SHORT).show();
				}
				else if(object.has("status") && object.getInt("status")== 1)
				{
					int code = object.getInt("message");


					if(Utils.getResponseMessage(code).length()!=0)
						Toast.makeText(VideoRatingActivity.this,Utils.getResponseMessage(code), Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			super.onPostExecute(object);
			//			VideoRatingActivity.this.finish();
		}
	}



}
