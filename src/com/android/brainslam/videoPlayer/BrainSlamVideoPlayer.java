package com.android.brainslam.videoPlayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.brainslam.R;

public class BrainSlamVideoPlayer extends Activity implements OnTouchListener
{	
	VideoView videoView;
	String str = "http://cdnbakmi.kaltura.com/p/1667341/sp/166734100/flvclipper/internalredirect/true/entry_id/1_79zf9mrb/version/0/a.mp4?novar=0";
	SeekBar playerSeekBar;
	TextView videoDuration, currentDuration;
	ImageView playerPause;
	LinearLayout playerPanel, bufferView;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_player);
//		String str =getIntent().getExtras().getString("videourl");
		videoView = (VideoView)findViewById(R.id.video_view);
		videoView.setOnTouchListener(this);

		playerSeekBar = (SeekBar)findViewById(R.id.sb_player_duration);
		//		playerSeekBar.setIndeterminate(true); 

		videoDuration = (TextView) findViewById(R.id.tv_player_duration);
		currentDuration = (TextView) findViewById(R.id.tv_player_position);

		playerPause = (ImageView) findViewById(R.id.iv_pause_video);
		playerPause.setVisibility(View.GONE);

		playerPanel = (LinearLayout) findViewById(R.id.player_panel);
		bufferView = (LinearLayout) findViewById(R.id.buffer_view);
		bufferView.setVisibility(View.GONE);

		hidePanel();

		playerSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) 
			{
				if(fromUser) 
				{
					// this is when actually seekbar has been seeked to a new position
					videoView.seekTo(progress);
					playerSeekBar.setProgress(videoView.getCurrentPosition());
				}

				videoView.start();
				playerSeekBar.postDelayed(onEverySecond, 1000);
			}
		});

		new StreamVideo().execute(str);   
	}

	private void animate(final View view, final int animId)
	{
		Animation animation = AnimationUtils.loadAnimation(BrainSlamVideoPlayer.this, animId);
		view.startAnimation(animation);

		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) 
			{
				view.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void onPause() 
	{
		super.onPause();

		if(videoView != null)
			videoView.pause();
	}

	@Override
	public void onResume()
	{
		super.onResume();

		if(videoView != null)
			videoView.resume();
	}


	//	Dialog bufferDialog;
	int totalDuration, duration;
	int current;

	// StreamVideo AsyncTask
	private class StreamVideo extends AsyncTask<String, Void, Void> 
	{
		String videoUrl = null;

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();

			bufferView.setVisibility(View.VISIBLE);

			//			bufferDialog = new Dialog(BrainSlamVideoPlayer.this);
			//			bufferDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			//			bufferDialog.setContentView(R.layout.progress_dialog);
			//			bufferDialog.setCancelable(true);
			//			bufferDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
			//
			//			bufferDialog.show();
		}

		@Override
		protected Void doInBackground(String... url) 
		{
			videoUrl = url[0];
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {

			try {
				Log.d("BSVP", "video url::"+videoUrl);

				// Start the MediaController
				//				MediaController mediacontroller = new MediaController(
				//						BrainSlamVideoPlayer.this);
				//				mediacontroller.setAnchorView(videoView);
				// Get the URL from String VideoURL
				Uri video = Uri.parse(videoUrl);
				//				videoView.setMediaController(mediacontroller);
				videoView.setMediaController(null);
				videoView.setVideoURI(video);

				videoView.requestFocus();


				videoView.setOnPreparedListener(new OnPreparedListener() { 
					// Close the progress bar and play the video
					public void onPrepared(MediaPlayer mp) {
						bufferView.setVisibility(View.GONE);
						videoView.start();

//						 mp.setOnBufferingUpdateListener(new OnBufferingUpdateListener() 
//						 {
//							 @Override
//							 public void onBufferingUpdate(MediaPlayer mp, int percent) 
//							 {
//								 Log.d("BSVP", "---buffering percentage::"+percent);
//								 bufferView.setVisibility(View.VISIBLE);		
//							 }
//						 });
						
						duration = mp.getDuration()/1000;

						videoDuration.setText(""+getFormattedTime(duration));

						playerSeekBar.setMax(videoView.getDuration());
						playerSeekBar.postDelayed(onEverySecond, 1000);

						totalDuration = videoView.getDuration();
						current = videoView.getCurrentPosition();
					}
				});

			} catch (Exception e) {
				bufferView.setVisibility(View.GONE);
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
		}
	}


	int lastBufferPercentage = 0;

	
	Runnable onEverySecond=new Runnable() {

		@Override
		public void run() 
		{
//			Log.d("BSVP", "videoView.isPlaying()::"+videoView.isPlaying()+"  video getBufferPercentage::"+videoView.getBufferPercentage()+
//					"  playerPause.getVisibility()::"+playerPause.getVisibility());
//			if(!videoView.isPlaying() && videoView.getBufferPercentage() < 100 && playerPause.getVisibility() == View.GONE)
//			{
//				bufferView.setVisibility(View.VISIBLE);				
//			}
//			else
//			{
//				bufferView.setVisibility(View.GONE);	
//			}
			
			if(lastBufferPercentage == videoView.getCurrentPosition() && videoView.getBufferPercentage() < 100)
			{
				bufferView.setVisibility(View.VISIBLE);		
			}
			else
			{
				bufferView.setVisibility(View.GONE);
			}

			lastBufferPercentage = videoView.getCurrentPosition();

			//			if(videoView.getBufferPercentage() != 0 && duration != 0)
			//			{
			//				currentDuration.setText(getCurrentDuration(duration, videoView.getBufferPercentage()));
			//			}

			if(playerSeekBar != null) 
			{
				Log.d("BSVP", "video progress::"+videoView.getCurrentPosition()+"  video getBufferPercentage::"+videoView.getBufferPercentage());

				playerSeekBar.setProgress(videoView.getCurrentPosition());
				playerSeekBar.setSecondaryProgress(videoView.getBufferPercentage() * 1000);
				playerSeekBar.postInvalidate();
			}

			if(videoView.isPlaying()) 
			{
				playerPause.setVisibility(View.GONE);
				playerSeekBar.postDelayed(onEverySecond, 1000);
			}

			//-------------------------------------
			String countText;

			int countUp = videoView.getCurrentPosition();
			countUp = Math.round(countUp / 1000);

			long a = (long) countUp;
			if (countUp % 60000 <= 9) {
				countText = (a / 60000) + ":0" + (a % 60000);
			} else {
				countText = (a / 60000) + ":" + (a % 60000);           
			} 

			currentDuration.setText(countText);
		}
	};


	public String getFormattedTime(int duration)
	{
		int hours = duration / 3600; 
		int minutes = (duration / 60) - (hours * 60);
		int seconds = duration - (hours * 3600) - (minutes * 60) ;
		return String.format("%d:%02d:%02d", hours, minutes, seconds);
	}

	@Override 
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.login_screen, menu);
		return true;
	}

	private void hidePanel()
	{
		new Handler().postDelayed(new Runnable() 
		{
			public void run() 
			{
				animate(playerPanel, R.anim.fade_out);
			}	
		}, 5000);		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			Log.d("BSVP", "Here in ontouch listener");
			playerPanel.setVisibility(View.VISIBLE);

			hidePanel();

			if(videoView != null)
			{
				if(videoView.isPlaying())
				{
					Log.d("BSVP", "Video Paused here");
					videoView.pause();

					playerPause.setVisibility(View.VISIBLE);

				}
				else
				{
					Log.d("BSVP", "Resume Video here");
					videoView.start();

					playerPause.setVisibility(View.GONE);
				}
			}
			return true;	
		}
		else
		{
			return false;
		}
	}
}