package com.android.brainslam.camera;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.android.brainslam.R;

public class CameraViewActivity extends Activity {
	private static final int MEDIA_TYPE_VIDEO = 2;
	
	private static final String TAG = "VideoRecorderActivity";
	private static final String FILE_PREFIX = "cs5248";

	private Camera myCamera;
	private SurfaceView surfaceView;
	private MediaRecorder mediaRecorder;
	
	boolean recording;
	private Button startButton;
	private Handler	recordingTimerHandler;
	private long	recordingLength;

	protected static final String EXTRA_IMAGE_PATH = "com.android.brainslam.CameraViewActivity.EXTRA_IMAGE_PATH";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		recording = false;
		setContentView(R.layout.activity_video_rec);

		// Get Camera for preview
		myCamera = getCameraInstance();
		

		surfaceView = new CameraSurfaceView(this, myCamera);
		surfaceView.setKeepScreenOn(true);
		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.videoView);
		frameLayout.addView(surfaceView);

		startButton = (Button) findViewById(R.id.buttonStrat);
		this.recordingTimerHandler = new Handler();
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		if(recording){
			stopRecording();
		}else{
//			statusView.setText("OnPause");
			releaseMediaRecorder(); // if you are using MediaRecorder, release it
			 // release the camera immediately on pause event
			startButton.setText("Record");
		}
		releaseCamera();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
	}

	public void startButtonClicked(View view) {
		if (!recording) {
			releaseCamera();

			if (!prepareMediaRecorder()) {
				Log.d(TAG, "fail in prepareMediaRecorder()");
				finish();
			}

			mediaRecorder.start();
			startRecordingTimer();
			recording = true;
//			statusView.setText("Starting recording...");
			startButton.setText("Stop");
		} else {
			stopRecording();
		}
	}
	
	private void stopRecording(){
		if (recording) {
			mediaRecorder.stop(); // stop the recording
			releaseMediaRecorder(); // release the MediaRecorder object
			
			this.recordingTimerHandler.removeCallbacks(this.recordingTimerUpdater);
			
			recording = false;

//			statusView.setText("Recording Stopped");
			startButton.setText("Record");
		}
	}

	private Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
		} catch (Exception e) {
			System.out.println(e);
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}

	private boolean prepareMediaRecorder() {
		myCamera = getCameraInstance();
		mediaRecorder = new MediaRecorder();

		myCamera.unlock();
		mediaRecorder.setCamera(myCamera);

		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
		mediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
		mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());

		try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			releaseMediaRecorder();
			Log.e(TAG, e.getMessage());
			return false;
		} catch (IOException e) {
			releaseMediaRecorder();
			Log.e(TAG, e.getMessage());
			return false;
		}
		return true;
	}

	private void releaseMediaRecorder() {
		if (mediaRecorder != null) {
			mediaRecorder.reset(); // clear recorder configuration
			mediaRecorder.release(); // release the recorder object
			mediaRecorder = null;
			myCamera.lock(); // lock camera for later use
		}
	}

	private void releaseCamera() {

		if (myCamera != null) {
			myCamera.release(); // release the camera for other applications
			myCamera = null;
		}
	}

	private static File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = Storage.getMediaFolder(true);

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		
		if (type == MEDIA_TYPE_VIDEO) {
			return new File(mediaStorageDir.getPath() + File.separator
					+ FILE_PREFIX + "_" + timeStamp + ".mp4");
		}
		
		return null;
	}
	
	private void startRecordingTimer() {
		this.recordingLength = 0;
		this.recordingTimerUpdater.run();
	}
	
	private void onRecordingTimerTick() {
		final long h = TimeUnit.SECONDS.toHours(this.recordingLength);
		final long m = TimeUnit.SECONDS.toMinutes(this.recordingLength) - (TimeUnit.SECONDS.toHours(this.recordingLength)* 60);
		final long s = TimeUnit.SECONDS.toSeconds(this.recordingLength) - (TimeUnit.SECONDS.toMinutes(this.recordingLength) *60);
		++this.recordingLength;
		updateRecordingLength(h, m, s);
	}
	
	//UI THREAD ONLY
	private void updateRecordingLength(final long h, final long m, final long s) {
		if (h > 0) {
//			this.statusView.setText(String.format("Recording %02d:%02d:%02d", h, m, s));
		} else {
//			this.statusView.setText(String.format("Recording %02d:%02d", m, s));
		}
	}
	
	Runnable recordingTimerUpdater = new Runnable() {
		public void run() {
			onRecordingTimerTick();
			recordingTimerHandler.postDelayed(recordingTimerUpdater, 1000);
		}
	};
}
