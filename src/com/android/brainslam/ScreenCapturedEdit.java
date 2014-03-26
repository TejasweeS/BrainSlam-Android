package com.android.brainslam;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.WritableByteChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.utils.CustomSpinnerDialog;
import com.android.utils.Utils;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;

public class ScreenCapturedEdit extends Activity implements OnClickListener{


	VideoView videoView;
	SeekBar videoSeekBar;
	String mediaUrl;
	ImageButton playButton;
	ImageView coverPhotoImageView;
	LinearLayout scrollView;
	int seekedProgress = 0;
	TextView textView ,selectCoverPhoto;
	private int SELECT_IMAGE = 0;
	List<Bitmap> framesbitMapList;
	List<Integer> imageViewId ;
	CustomSpinnerDialog spinner_dialog;

	Bitmap coverPhoto; 
	String TAG = "Rutuja";
	float trimStart = 0 , trimEnd = 0;

	float trimInterval = 30.00f;
	boolean isFromGallary = false;
	String strTrimmed = "";

	TextView startTextView, endTextView, lengthTextView;
	ImageView previewImageView;
	private ContextThemeWrapper con;

	private int RESULT_LOAD_IMAGE = 1;
	boolean isImageOrVideo = false;
	LinearLayout durationLayout ;
	String coverPhotoPathToSend = "";


	final long seconds_in_millies = 1000;
	final long minutes_in_millies = seconds_in_millies * 60;
	final long hours_in_millies = minutes_in_millies * 60;
	//	TextView st
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_captured_edit);


		// image or video path
		mediaUrl = getIntent().getExtras().getString("videoPath");

		con = new ContextThemeWrapper( this, R.style.alert);
		//true if  then video/image is selected from gallary otherwise captured using camera
		isFromGallary = getIntent().getExtras().getBoolean("fromGallary", false);

		//true if image is acptured
		isImageOrVideo = getIntent().getExtras().getBoolean("imageOrVideo", false);

		startTextView = (TextView) findViewById(R.id.start_text);
		endTextView = (TextView) findViewById(R.id.end_text);
		lengthTextView = (TextView) findViewById(R.id.length_text);
		Log.v(TAG, "STR path:: "+mediaUrl +"isFromGallary:: "+isFromGallary+" ::  isImageOrVideo ::"+isImageOrVideo);

		spinner_dialog = new CustomSpinnerDialog(ScreenCapturedEdit.this);
		previewImageView = (ImageView) findViewById(R.id.image_preview);
		videoView = (VideoView) findViewById(R.id.videoView1);
		videoSeekBar = (SeekBar) findViewById(R.id.video_seek_bar);
		playButton = (ImageButton) findViewById(R.id.play_button);
		durationLayout = (LinearLayout) findViewById(R.id.duration_layout);
		scrollView = (LinearLayout) findViewById(R.id.frames_layout);

		selectCoverPhoto = (TextView) findViewById(R.id.select_cover_photo_button);
		selectCoverPhoto.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.duration_text);
		coverPhotoImageView = (ImageView)findViewById(R.id.selected_cover_photo);
		TextView nextButton = (TextView)findViewById(R.id.next_button);
		nextButton.setOnClickListener(this);
		//video is captured from camera then no need to seek bar directly display the frames



		if(!isImageOrVideo)
		{
			previewImageView.setVisibility(View.GONE);
			videoView.setVisibility(View.VISIBLE);

			//			videoSeekBar.setVisibility(View.VISIBLE);
			if(!isFromGallary ) // captured 
			{
				Log.v(TAG, "Video is captured from camera");
				videoSeekBar.setVisibility(View.GONE);
				//				durationLayout.setVisibility(View.GONE);
			}
			else
			{
				videoSeekBar.setVisibility(View.VISIBLE);
				//				durationLayout.setVisibility(View.VISIBLE);
			}
		}
		else
		{
			// display captured/selected image
			videoSeekBar.setVisibility(View.GONE);
			videoView.setVisibility(View.GONE);
			//			durationLayout.setVisibility(View.GONE);
			previewImageView.setVisibility(View.VISIBLE);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			Matrix matrix = new Matrix();
			matrix.postRotate(90);
			Bitmap bmp = BitmapFactory.decodeFile(mediaUrl,options);

			if(null != bmp)
				previewImageView.setImageBitmap(bmp);
			else
				Toast.makeText(ScreenCapturedEdit.this, "image not found", Toast.LENGTH_LONG).show();


		}

		//listner for seek bar
		videoSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub

				if(fromUser)
				{
					Log.v(TAG, "mannualy seeked the video");
					videoView.seekTo(progress);
					//					videoView.pause();

					if(android.os.Build.VERSION.SDK_INT >9)
					{
						addFrames(videoView.getCurrentPosition());
						trimStart = (videoView.getCurrentPosition())/1000;   

					}

				}

				videoView.start();
				playButton.setImageResource(R.drawable.pause_button);
				videoSeekBar.postDelayed(onEverySecond, 1000);

			}
		});


		videoView.setVideoURI(Uri.parse(mediaUrl));
		videoView.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub

				videoView.start();	
				playButton.setImageResource(R.drawable.pause_button);
				videoSeekBar.setMax(videoView.getDuration());
				if(!isFromGallary)
					addFrames(0);

			}
		});
		
		videoView.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				playButton.setImageResource(R.drawable.playbutton);
			}
		});

	}


	// saving captured photo to sdcard
	public void saveCoverPhoto(Bitmap bmp)
	{
		FileOutputStream outStream =null;

		//    	
		//    	int bytes = bmp.;
		//    	//or we can calculate bytes this way. Use a different value than 4 if you don't use 32bit images.
		//    	//int bytes = b.getWidth()*b.getHeight()*4; 
		//
		//    	ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
		//    	bmp.copyPixelsToBuffer(buffer); //Move the byte data to the buffer
		//
		//    	byte[] array = buffer.array();
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);



		coverPhotoPathToSend = Environment.getExternalStorageDirectory()+"/temppic.jpg";
		try {
			outStream = new FileOutputStream(coverPhotoPathToSend);   
			outStream.write(output.toByteArray());
			outStream.close();

			Log.v(TAG,"str save to"+coverPhotoPathToSend);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.v(TAG, "str notsaved"+coverPhotoPathToSend);
			e.printStackTrace();
		}

	}
	//-------------------------------methods used for trimming the video-----------------------

	public void  trimVideo()
	{
		try {
			Movie movie = new MovieCreator().build(new RandomAccessFile(mediaUrl, "r").getChannel());


			List<Track> tracks = movie.getTracks();
			movie.setTracks(new LinkedList<Track>());

			double startTime = trimStart;
			double endTime = trimEnd ;
			Log.v(TAG, "Start Tym:: "+startTime+" :: endtym:: "+endTime + " total_tym:: "+videoView.getDuration());
			boolean timeCorrected = false;

			// Here we try to find a track that has sync samples. Since we can only start decoding
			// at such a sample we SHOULD make sure that the start of the new fragment is exactly
			// such a frame
			for (Track track : tracks) {
				if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
					if (timeCorrected) {
						// This exception here could be a false positive in case we have multiple tracks
						// with sync samples at exactly the same positions. E.g. a single movie containing
						// multiple qualities of the same video (Microsoft Smooth Streaming file)

						throw new RuntimeException("The startTime has already been corrected by another track with SyncSample. Not Supported.");
					}
					startTime = correctTimeToSyncSample(track, startTime, false);
					endTime = correctTimeToSyncSample(track, endTime, true);



					timeCorrected = true;
				}
			}

			for (Track track : tracks) {
				long currentSample = 0;
				double currentTime = 0;
				long startSample = -1;
				long endSample = -1;

				for (int i = 0; i < track.getDecodingTimeEntries().size(); i++) {
					TimeToSampleBox.Entry entry = track.getDecodingTimeEntries().get(i);
					for (int j = 0; j < entry.getCount(); j++) {
						// entry.getDelta() is the amount of time the current sample covers.

						if (currentTime <= startTime) {
							// current sample is still before the new starttime
							startSample = currentSample;
						}
						if (currentTime <= endTime) {
							// current sample is after the new start time and still before the new endtime
							endSample = currentSample;
						} else {
							// current sample is after the end of the cropped video
							break;
						}
						currentTime += (double) entry.getDelta() / (double) track.getTrackMetaData().getTimescale();
						currentSample++;
					}
				}
				movie.addTrack(new CroppedTrack(track, startSample, endSample));
			}
			long start1 = System.currentTimeMillis();
			Container out =  new DefaultMp4Builder().build(movie);
			long start2 = System.currentTimeMillis();

			//	        FileOutputStream fos = new FileOutputStream(String.format("output-%f-%f.mp4", startTime, endTime));
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String filename =  Environment.getExternalStorageDirectory()+"/CroppedVID"+timeStamp+".mp4";
			FileOutputStream fos = new FileOutputStream(filename);

			WritableByteChannel fc = fos.getChannel();
			out.writeContainer(fc);
			fc.close();
			fos.close();

			// Play trimmed video....
			//disbale the seek bar here


			Log.v(TAG, "video trimmed...."+filename);
			mediaUrl = filename;

//			videoView.setVideoURI(Uri.parse(mediaUrl));
//			videoView.setOnPreparedListener(new OnPreparedListener() {
//
//				@Override
//				public void onPrepared(MediaPlayer mp) {
//					// TODO Auto-generated method stub
//
//					videoView.start();	
//					playButton.setImageResource(R.drawable.pause_button);
//					videoSeekBar.setMax(videoView.getDuration());
//
//				}
//			});
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.v(TAG, "video not trimmed....");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.v(TAG, "video not strimmed....");
			e.printStackTrace();
		}
	}


	protected static long getDuration(Track track) {
		long duration = 0;
		for (TimeToSampleBox.Entry entry : track.getDecodingTimeEntries()) {
			duration += entry.getCount() * entry.getDelta();
		}
		return duration;
	}

	private static double correctTimeToSyncSample(Track track, double cutHere, boolean next) {
		double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
		long currentSample = 0;
		double currentTime = 0;
		for (int i = 0; i < track.getDecodingTimeEntries().size(); i++) {
			TimeToSampleBox.Entry entry = track.getDecodingTimeEntries().get(i);
			for (int j = 0; j < entry.getCount(); j++) {
				if (Arrays.binarySearch(track.getSyncSamples(), currentSample + 1) >= 0) {
					// samples always start with 1 but we start with zero therefore +1
					timeOfSyncSamples[Arrays.binarySearch(track.getSyncSamples(), currentSample + 1)] = currentTime;
				}
				currentTime += (double) entry.getDelta() / (double) track.getTrackMetaData().getTimescale();
				currentSample++;
			}
		}
		double previous = 0;
		for (double timeOfSyncSample : timeOfSyncSamples) {
			if (timeOfSyncSample > cutHere) {
				if (next) {
					return timeOfSyncSample;
				} else {
					return previous;
				}
			}
			previous = timeOfSyncSample;
		}
		return timeOfSyncSamples[timeOfSyncSamples.length - 1];
	}
	//--------------------------------------------------------------------------------
	//on click of play recorded video
	public void onClickPlayRecordedVideo(View view)
	{
		scrollView.setBackgroundResource(android.R.color.transparent);
		System.out.println("rutuja on click");
		coverPhotoImageView.setVisibility(View.GONE);

		if(!isImageOrVideo)
		{


			if(videoView.isPlaying())
			{
				videoView.pause();
				playButton.setImageResource(R.drawable.playbutton);  

			}
			else
			{
				videoView.start();
				playButton.setImageResource(R.drawable.pause_button);  

			}

		}
		else
		{
			Toast.makeText(ScreenCapturedEdit.this, "Image selected", Toast.LENGTH_LONG).show();
		}



	}






	// method to display the frames after every 2 sec
	@SuppressLint("NewApi")
	public void addFrames(int startTym)
	{
		MediaMetadataRetriever retriever = new MediaMetadataRetriever(); 

		float trimlength = trimInterval;
		retriever.setDataSource(mediaUrl);

		//		int noOfFrms = ((startTym + 30000) > videoView.getDuration() ? videoView.getDuration() - (startTym ) : (startTym + 30000))/2000;

		System.out.println("rutu startTym:: "+startTym + "Total duration:: "+videoView.getDuration());

		float startTime;
		if(startTym != 0)
			startTime = (startTym/1000);
		else
			startTime = 0;

		float time = (startTime + trimInterval); 


		trimEnd = time;

		if(time >(float) (videoView.getDuration()/1000))
		{
			time = (float) (videoView.getDuration()/1000) - startTime;
			trimEnd = trimStart + time;

			trimlength = trimEnd - trimStart;
		}



		startTextView.setText(timeMilliesToString((long)trimStart*1000)); 
		endTextView.setText(timeMilliesToString((long)trimEnd*1000)); 
		lengthTextView.setText(timeMilliesToString((long)trimlength*1000));


		//		videoSeekBar.setMax((int)trimEnd*1000);
		System.out.println("rutu time:: "+time+" total duration:: "+(videoView.getDuration()/1000));
		int noOfFrms = Math.round(time/2);
		System.out.println("no of frames:: "+noOfFrms);

		scrollView.removeAllViews();
		framesbitMapList = new ArrayList<Bitmap>();
		imageViewId =  new ArrayList<Integer>();


		for(int i=0 ; i< noOfFrms ; i++)
		{
			final Bitmap btm = retriever.getFrameAtTime((startTym*1000)+(2000000*i));


			framesbitMapList.add(btm);
			ImageView imageView = new ImageView(this);
			imageView.setImageBitmap(btm);
			imageView.setScaleType(ScaleType.FIT_CENTER);
			imageView.setLayoutParams(new LayoutParams(400,200));
			imageViewId.add(imageView.getId());

			//			Matrix matrix=new Matrix();
			//			matrix.postRotate(45);
			//			imageView.setImageMatrix(matrix);

			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					coverPhoto = btm;
					coverPhotoImageView.setVisibility(View.VISIBLE);
					coverPhotoImageView.setImageBitmap(coverPhoto);
					saveCoverPhoto(coverPhoto);
				}
			});

			scrollView.setEnabled(false);
			scrollView.addView(imageView);

			System.out.println("no frms:: "+noOfFrms);

		}
	}

	//converter of start tym and end tym

	public String convertTym(float countUp)
	{

		String countText;

		//		countUp = Math.round(countUp / 1000);


		long a = (long) countUp;

		Log.v(TAG, "TIME (a % 60000) "+(a % 60000 )+"  (a / 60000): "+(a / 60000));
		if (a % 60000 <= 9.0f) {
			countText = (a / 60000) + ":0" + (a % 60000);
		} else {
			countText = (a / 60000) + ":" + (a % 60000);           
		} 

		Log.v("rutuja", "TIME countText :: "+countText);
		return countText;
	}
	Runnable onEverySecond = new Runnable() {

		@Override
		public void run() 
		{

			if(videoSeekBar != null) 
			{
				Log.d(TAG, "video progress::"+videoView.getCurrentPosition()+"  video getBufferPercentage::"+videoView.getBufferPercentage());

				videoSeekBar.setSecondaryProgress(videoView.getCurrentPosition());
				videoSeekBar.postInvalidate();
			}

			if(videoView.isPlaying()) 
			{
				videoSeekBar.postDelayed(onEverySecond, 1000);
			}

			if(videoView.isPlaying() && trimEnd != 0 && Math.round(videoView.getCurrentPosition() / 1000) == trimEnd)
			{

				Log.v(TAG, "video paused......... current::"+Math.round(videoView.getCurrentPosition() / 1000)+" trimend:: "+trimEnd);
				videoView.pause();
				playButton.setImageResource(R.drawable.playbutton);
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
			textView.setText(countText);


		}
	};


	public String timeMilliesToString(long elapsed)
	{
		//		 long elapsed = 36610000;
		long hours = elapsed / hours_in_millies;
		elapsed %= hours_in_millies;
		long minutes = elapsed / minutes_in_millies;
		elapsed %= minutes_in_millies;
		long seconds = elapsed / seconds_in_millies;
		System.out.println("rutuja TIME :: "+Utils.getPaddedTime(hours)+":"+Utils.getPaddedTime(minutes)+":"+Utils.getPaddedTime(seconds)+" :::     "+hours+":"+minutes+":"+seconds);

		//	        Utils.getPaddedTime(hours)+":"+
		return Utils.getPaddedTime(minutes)+":"+Utils.getPaddedTime(seconds);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.screen_captured_edit, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
			Uri pickedImage = data.getData();

			String[] filePath = { MediaStore.Images.Media.DATA };
			Log.i("pickedImage-->",""+pickedImage);

			Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
			cursor.moveToFirst();
			String lastpath = cursor.getString(cursor.getColumnIndex(filePath[0]));

			Log.i("rutuja",""+lastpath);

			Uri result = data.getData();

			// to be sent on next Click
			coverPhotoPathToSend = result.toString();
			InputStream is = null;
			try {
				is = getContentResolver().openInputStream(result);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(is != null)
			{
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				coverPhotoImageView.setVisibility(View.VISIBLE);
				coverPhoto = BitmapFactory.decodeStream(is,null,options); 
				coverPhotoImageView.setImageBitmap(coverPhoto);
				saveCoverPhoto(coverPhoto);
			}
			cursor.close();

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())

		{

		case R.id.select_cover_photo_button:
			if(!isImageOrVideo)
			{
				if(videoView.isPlaying())
				{
					videoView.pause();
					playButton.setImageResource(R.drawable.playbutton);  

				}
				coverPhotoImageView.setVisibility(View.GONE);
				AlertDialog.Builder alert = new AlertDialog.Builder(con);
				final AlertDialog dialog = alert.create();
				dialog.setTitle("Select Option");

				Button selectFirstFrameButton = new Button(ScreenCapturedEdit.this);
				selectFirstFrameButton.setText("Select First Frame");
				// select first frame as cover photo
				selectFirstFrameButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						if(null != framesbitMapList && framesbitMapList.size() != 0)
						{
							coverPhoto = framesbitMapList.get(0);
							coverPhotoImageView.setVisibility(View.VISIBLE);
							coverPhotoImageView.setImageBitmap(coverPhoto);	
							scrollView.setBackgroundResource(android.R.color.transparent);
							saveCoverPhoto(coverPhoto);
						}
						dialog.dismiss();
					}
				});
				Button selectFrameButton = new Button(ScreenCapturedEdit.this);
				selectFrameButton.setText("Select Frame");


				selectFrameButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						scrollView.setEnabled(true);
						//					scrollView.setBackgroundResource(R.drawable.border);
						dialog.dismiss();
					}
				});

				Button selectFromGallaryButton = new Button(ScreenCapturedEdit.this);
				selectFromGallaryButton.setText("Select From Gallary");


				selectFromGallaryButton.setOnClickListener(new OnClickListener() {



					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
						galleryIntent.setType("image/*");
						scrollView.setBackgroundResource(android.R.color.transparent);
						startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
						dialog.dismiss();

					}
				});


				LinearLayout layout = new LinearLayout(ScreenCapturedEdit.this);
				layout.setOrientation(1);
				layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				layout.addView(selectFirstFrameButton);
				layout.addView(selectFrameButton);
				layout.addView(selectFromGallaryButton);
				dialog.setView(layout);


				dialog.show();
			}
			else
			{
				final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
				galleryIntent.setType("image/*");
				scrollView.setBackgroundResource(android.R.color.transparent);
				startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);	
			}
			break;

		case R.id.next_button:
			// start the triming process
			scrollView.setBackgroundResource(android.R.color.transparent);
						new TrimVideo().execute();
//			Toast.makeText(ScreenCapturedEdit.this, "Work In Progress", Toast.LENGTH_LONG).show();
			break;

		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		ScreenCapturedEdit.this.finish();
//		startActivity(new Intent(ScreenCapturedEdit.this, ScreencaptureActivity.class));

	} 

	//asynctask for triming the video
	private class TrimVideo extends AsyncTask<Void, Void, Void> 
	{

		private String facebookUserid;
		@Override
		protected void onPreExecute() {
			spinner_dialog.show();
			super.onPreExecute();

		}
		@Override
		protected Void doInBackground(Void... arg0) {

			Log.v(TAG, "do in background of triming video");
			if(!isImageOrVideo && isFromGallary)
				trimVideo();
			return null;
		}


		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			Log.v(TAG, " on post done triming");
			spinner_dialog.dismiss();

			coverPhotoImageView.setVisibility(View.GONE);
			/************** please Send me the following parameters in intent *******************
			 * 
			 * 
			 * "CoverPhotourl" // SD card Path of cover photo that has been selected for video (if video is getting uploaded)
			 * 
			 * "Mediaurl"  // SD card Path of the media which is going to be uploaded
			 * 
			 * "VideoMediaType" boolean flag to indicate that video or image is getting uploaded*/
//			Toast.makeText(ScreenCapturedEdit.this, "Next Clicked", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(ScreenCapturedEdit.this, UploadDetailsActivity.class);

			Log.v(TAG, "Str sending data:: mediaUrl:"+mediaUrl+" : coverPhotoPathToSend::"+coverPhotoPathToSend);
			intent.putExtra("Mediaurl", mediaUrl);

			
			if(!isImageOrVideo && coverPhotoPathToSend.trim().length() == 0)
			{
				if(null != framesbitMapList && framesbitMapList.size() != 0)
				{
					Log.v(TAG, "Str sending coverPhotoNot selected");
					coverPhoto = framesbitMapList.get(0);
					saveCoverPhoto(coverPhoto);
				}
			}
			

			if(isImageOrVideo) //image
				intent.putExtra("VideoMediaType", false);
			else //video
				intent.putExtra("VideoMediaType", true);
			intent.putExtra("CoverPhotourl",coverPhotoPathToSend);
			startActivity(intent);

		}

	}
}
