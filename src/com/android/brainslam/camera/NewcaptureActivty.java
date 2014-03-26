package com.android.brainslam.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.R.anim;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.SensorManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.brainslam.R;
import com.android.brainslam.ScreenCapturedEdit;

public class NewcaptureActivty extends Activity{
	private Camera mCamera;
	private boolean isRecording = false;
	private CameraPreview mPreview;
	private boolean isFlashOn=false;
	private MediaRecorder mMediaRecorder;
	private String capturedcontentPath;
	private boolean isbackCamera=true;
	private boolean isRecordingMode=false;
	private String TAG="Camera";
	private int myProgress = 0;
	private Button flashButton;
	private ImageView  flipcam;
	private final int  MAXTIME = 31000;
	private final int INTERVEL = 1000;
	private  final int MEDIA_TYPE_IMAGE = 1;
	private   final int MEDIA_TYPE_VIDEO = 2;
	ProgressBar progressBar;
	private int currentOrientation = -1;
	final int RESULT_LOAD_IMAGE = 1;
	private  final int ORIENTATION_PORTRAIT_NORMAL = 1;
	private  final int ORIENTATION_PORTRAIT_INVERTED = 2;
	private  final int ORIENTATION_LANDSCAPE_NORMAL = 3;
	private  final int ORIENTATION_LANDSCAPE_INVERTED = 4;
	int fromDegree=0;
	private int noOfCamera=Camera.getNumberOfCameras();
	int toDegree=0;
	private OrientationEventListener orientationListener;
	private CountDownTimer countDownTimer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screencapture);

		// Create an instance of Camera

	}

	private void orientationListener()
	{
		if (orientationListener == null) {
			orientationListener = new OrientationEventListener(this,
					SensorManager.SENSOR_DELAY_NORMAL) {

				@Override
				public void onOrientationChanged(int orientation) {

					int lastOrientation = currentOrientation;

					if (orientation >= 315 || orientation < 45) {
						if (currentOrientation != ORIENTATION_PORTRAIT_NORMAL) {
							currentOrientation = ORIENTATION_PORTRAIT_NORMAL;
						}
					} else if (orientation < 315 && orientation >= 225) {
						if (currentOrientation != ORIENTATION_LANDSCAPE_NORMAL) {
							currentOrientation = ORIENTATION_LANDSCAPE_NORMAL;
						}
					} else if (orientation < 225 && orientation >= 135) {
						if (currentOrientation != ORIENTATION_PORTRAIT_INVERTED) {
							currentOrientation = ORIENTATION_PORTRAIT_INVERTED;
						}
					} else { // orientation <135 && orientation > 45
						if (currentOrientation != ORIENTATION_LANDSCAPE_INVERTED) {
							currentOrientation = ORIENTATION_LANDSCAPE_INVERTED;
						}
					}

					if (lastOrientation != currentOrientation) {

						switch (currentOrientation) {
						case ORIENTATION_PORTRAIT_NORMAL:			//1	

							Log.i("Oreintation", "Orientation = 90");
							if(toDegree==90)
							{
								fromDegree=90;
								toDegree=0;
								rotateAnimation(flashButton,  fromDegree, toDegree);
								rotateAnimation(flipcam,  fromDegree, toDegree);
								//								rotateAnimation(bstartover,  fromDegree, toDegree);
								//								rotateAnimation(bnext,  fromDegree, toDegree);
							}
							else if(toDegree==270)
							{

								fromDegree=270;
								toDegree=360;
								rotateAnimation(flashButton,  fromDegree, toDegree);
								rotateAnimation(flipcam,  fromDegree, toDegree);
								//								rotateAnimation(bstartover,  fromDegree, toDegree);
								//								rotateAnimation(bnext,  fromDegree, toDegree);
							}

							//							Log.i("Orientation","current Degree"+flashButton.getRotation());


							break;
						case ORIENTATION_LANDSCAPE_NORMAL:    //3
							fromDegree=0;
							toDegree=90;
							rotateAnimation(flashButton,  fromDegree, toDegree);
							rotateAnimation(flipcam,  fromDegree, toDegree);
							//							rotateAnimation(bstartover,  fromDegree, toDegree);
							//							rotateAnimation(bnext,  fromDegree, toDegree);

							Log.i("Oreintation", "Orientation = 0");
							//							Log.i("Orientation","current Degree"+flashButton.getRotation());

							break;
						case ORIENTATION_PORTRAIT_INVERTED:  //2
							fromDegree=90;
							toDegree=180;
							rotateAnimation(flashButton,  fromDegree, toDegree);
							rotateAnimation(flipcam,  fromDegree, toDegree);
							//							rotateAnimation(bstartover,  fromDegree, toDegree);
							//							rotateAnimation(bnext,  fromDegree, toDegree);
							Log.i("Oreintation", "Orientation = 270");
							//							Log.i("Orientation","current Degree"+flashButton.getRotation());

							break;
						case ORIENTATION_LANDSCAPE_INVERTED: 		//4
							fromDegree=180;
							toDegree=270;
							rotateAnimation(flashButton,  fromDegree, toDegree);
							rotateAnimation(flipcam,  fromDegree, toDegree);
							//							rotateAnimation(bstartover,  fromDegree, toDegree);
							//							rotateAnimation(bnext,  fromDegree, toDegree);
							Log.i("Oreintation", "Orientation = 180");
							//							Log.i("Orientation","current Degree"+flashButton.getRotation());


							break;
						}

					}

				}
			};
		}
		if (orientationListener.canDetectOrientation()) {
			orientationListener.enable();
		}

	}

	private void rotateAnimation(View view, float rotateFrom, float rotateTo) {
		Animation rotateAnim = new RotateAnimation(rotateFrom, rotateTo,view.getMeasuredWidth() / 2, view.getMeasuredHeight() / 2);
		System.out.println("Inside Roate Anim");

		rotateAnim.setDuration(1000); // duration in ms
		rotateAnim.setRepeatCount(0); // -1 = infinite repeated
		rotateAnim.setFillAfter(true); // keep rotation after animation

		view.startAnimation(rotateAnim);
	}


	/** A safe way to get an instance of the Camera object. */
	public Camera getCameraInstance(boolean isbackCamera){
		Camera c = null;
		try {
			System.out.println("IS Back CAMERA::"+isbackCamera);

			if(isbackCamera==false)
			{
				c = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT); // Camera.CameraInfo.CAMERA_FACING_FRONT
			}
			else
			{
				if(noOfCamera==2)
					c = Camera.open();
				else if(noOfCamera==1)
				{
					System.out.println("Front Camera");
					c = Camera.open(0);
				}
				// attempt to get a Camera instance FACING_BACK
				/*if(c==null)
				{
					c = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);//
				}*/
			}
			c.setDisplayOrientation(90);
		}
		catch (Exception e){
			Toast.makeText(NewcaptureActivty.this, "Unable to Open Camera" ,2).show();
			this.finish();
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}


	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if(getIntent().getExtras()!=null && getIntent().getExtras().containsKey("isBackCamera") )
		{
			isbackCamera=getIntent().getExtras().getBoolean("isBackCamera");
			System.out.println("BACK CAMERA::"+isbackCamera);
			mCamera = getCameraInstance(isbackCamera);
			Toast.makeText(NewcaptureActivty.this, "IF "+isbackCamera, Toast.LENGTH_LONG).show();

		}
		else{
			mCamera = getCameraInstance(true);
			Toast.makeText(NewcaptureActivty.this, "ELSE ", Toast.LENGTH_LONG).show();

		}


		// Create our Preview view and set it as the content of our activity.
		if(mCamera!=null)
		{
			mPreview = new CameraPreview(this, mCamera);
			FrameLayout preview = (FrameLayout) findViewById(R.id.surfaceView1);
			preview.addView(mPreview);



			if(getIntent().getExtras()!=null && getIntent().getExtras().containsKey("videocapture") && getIntent().getExtras().getBoolean("videocapture"))
			{
				Button button=(Button)findViewById(R.id.button3);

				Drawable img1 = getApplicationContext().getResources()
						.getDrawable(R.drawable.camcorder);
				img1.setBounds(0, 0, 30, 30);
				button.setCompoundDrawables(null, img1, null, null);
				//				prepareVideoRecorder();
				System.out.println("Start for video recording");
				isRecordingMode=true;
			}
		}
		flashButton = (Button) findViewById(R.id.buttonFlash);

		flipcam = (ImageView) findViewById(R.id.imageView_switch_camera);
		orientationListener();
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar.setProgress(0);
	}

	/*
	 * Turning On flash
	 */
	private void turnOnFlash(Parameters params) {
		if (!isFlashOn) {
			if (mCamera == null) {
				return;
			}
			// play sound
			// playSound();

			params = mCamera.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_TORCH);
			mCamera.setParameters(params);
			// mCamera.startPreview();
			isFlashOn = true;
			flashButton.setText("ON");
			// changing button/switch image
			/*toggleButtonImage();*/
		}

	}

	/*
	 * Turning Off flash
	 */
	private void turnOffFlash(Parameters params) {
		if (isFlashOn) {
			if (mCamera == null ) {
				return;
			}
			// play sound
			// playSound();

			params = mCamera.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_OFF);
			mCamera.setParameters(params);
			// mCamera.stopPreview();
			isFlashOn = false;
			flashButton.setText("OFF");
			// changing button/switch image
			//			toggleButtonImage();
		}
	}

	private boolean checkFlash()
	{
		return getApplicationContext().getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
	}


	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
			if (pictureFile == null){
				Log.d(TAG, "Error creating media file, check storage permissions: ");
				return;
			}

			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
				Intent intent = new Intent(NewcaptureActivty.this,
						ScreenCapturedEdit.class);
				intent.putExtra("videoPath", pictureFile.getAbsolutePath());
				intent.putExtra("fromGallary", false);
				intent.putExtra("imageOrVideo", true);
				startActivity(intent);

			} catch (FileNotFoundException e) {
				Log.d(TAG, "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d(TAG, "Error accessing file: " + e.getMessage());
			}
		}
	};

	private boolean prepareImageRecorder()
	{

		//		mCamera = getCameraInstance();

		// Create our Preview view and set it as the content of our activity.
		mPreview = new CameraPreview(this, mCamera);
		mCamera.setDisplayOrientation(90);
		return true;
	}
	private boolean prepareVideoRecorder(){
		//		if(mCamera!=null)
		//		mCamera = getCameraInstance();
		mMediaRecorder = new MediaRecorder();

		// Step 1: Unlock and set camera to MediaRecorder
		mCamera.unlock();
		mMediaRecorder.setCamera(mCamera);


		// Step 2: Set sources
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		CamcorderProfile profile=null;
		if(isbackCamera)
			profile = CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_BACK, CamcorderProfile.QUALITY_HIGH);

		else
			profile = CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_FRONT, CamcorderProfile.QUALITY_HIGH);	

		
		
		
		/***NARESH*
		 * 
		 * 
		 * 
		 * mCamera.lock();
		mCamera.unlock();

		// Please maintain sequence of following code.
		// If you change sequence it will not work.
		mrec.setCamera(mCamera);

		// CamcorderProfile profile =
		// CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);

		// mrec.setProfile(profile);
		mrec.setVideoSource(MediaRecorder.VideoSource.CAMERA); // 1
		mrec.setAudioSource(MediaRecorder.AudioSource.MIC); // 2
		mrec.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // 3
		mrec.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP); // 4
		mrec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // 5
		mrec.setMaxDuration(30000); // 30 sec //6
		mrec.setPreviewDisplay(surfaceHolder.getSurface()); // 7
		mrec.setOutputFile(path + filename); // 8
		mrec.prepare(); // 9
		mrec.start(); // 10

		 * 
		 * ////
		
		
		
		/*	mMediaRecorder.setProfile(CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_BACK,CamcorderProfile.QUALITY_HIGH));
			mMediaRecorder.setProfile(CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_FRONT,CamcorderProfile.QUALITY_HIGH));*/

		if(profile == null){Log.d("Profile ", "the camcorder profile instance is null");

		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
		}else{
			mMediaRecorder.setProfile(profile);
		}


		// Step 3: Set a CamcorderProfile (requires API Level 8 or higher)

		// Step 4: Set output file
		capturedcontentPath=getOutputMediaFile(MEDIA_TYPE_VIDEO).getPath();
		//		capturedcontentPath=file.getAbsolutePath();

		mMediaRecorder.setOutputFile(capturedcontentPath);

		// Step 5: Set the preview output
		mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

		// Step 6: Prepare configured MediaRecorder
		mMediaRecorder.setMaxDuration(30000);
		try {
			mMediaRecorder.prepare();
		} catch (IllegalStateException e) {
			Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
			releaseMediaRecorder();
			return false;
		} catch (IOException e) {
			Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
			releaseMediaRecorder();
			return false;
		}
		return true;
	}

	/** Create a file Uri for saving an image or video */
	private  Uri getOutputMediaFileUri(int type){
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private  File getOutputMediaFile(int type){
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), "BrainSlam");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (! mediaStorageDir.exists()){
			if (! mediaStorageDir.mkdirs()){
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE){
			mediaFile = new File(mediaStorageDir.getPath() + File.separator +
					"IMG_"+ timeStamp + ".jpg");
		} else if(type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator +
					"VID_"+ timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}

	private void releaseMediaRecorder(){
		if (mMediaRecorder != null) {
			System.out.println("media recorder release");
			mMediaRecorder.reset();   // clear recorder configuration
			mMediaRecorder.release(); // release the recorder object
			mMediaRecorder = null;
			mCamera.lock();           // lock camera for later use
		}
	}

	private void releaseCamera(){
		if (mCamera != null){
			System.out.println("camera release");
			mCamera.release();        // release the camera for other applications
			mCamera = null;
		}
	}


	public void  onClick(View view) {
		// TODO Auto-generated method stub
		Button button;
		switch (view.getId()) {
		case R.id.buttonFlash:
			if(isFlashOn)
				turnOffFlash(mCamera.getParameters());
			else if(checkFlash())
				turnOnFlash(mCamera.getParameters());
			else
				Toast.makeText(this,"No Flash Found", 2).show();
			break;
		case R.id.imageView_switch_camera: // flip the camera
			if(isRecording) // recording is going please stop it
			{
				Toast.makeText(this,"Stop Recodring first", 2).show();
			}
			else if(noOfCamera==2)
				flipit(!isbackCamera);
			break;
		case R.id.button1: // photo Roll
			button=(Button)view;

			if(button.getText().equals("Start Over"))
			{
				flipit(isbackCamera);	
			}
			else
			{

				final Intent galleryIntent = new Intent(
						Intent.ACTION_GET_CONTENT);
				galleryIntent.setType("*/*");
				startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
				//Photo roll
			}

			break;

		case R.id.button2:// record
			button=(Button)view;

			Button startOver=(Button)findViewById(R.id.button1);
			startOver.setText("Start Over");
			Button continueBtn=(Button)findViewById(R.id.button3);
			continueBtn.setText("Continue");

			Drawable img = getApplicationContext().getResources()
					.getDrawable(R.drawable.startover);
			img.setBounds(0, 0, 30, 30);
			startOver.setCompoundDrawables(null, img, null, null);


			Drawable img1 = getApplicationContext().getResources()
					.getDrawable(R.drawable.nextbutton);
			img1.setBounds(0, 0, 30, 30);
			continueBtn.setCompoundDrawables(null, img1, null, null);

			if(isRecordingMode)
			{
				button.setText("Stop");
				camptureVideoContents();
				System.out.println("capture viceo contents");

			}
			else
			{
				mCamera.takePicture(null, null, mPicture);
			}
			break;

		case R.id.button3:// Swicth camera mode to video or Image capture

			button=(Button)view;
			if(button.getText().equals("Continue"))
			{
				if(isRecording)
					Toast.makeText(this,"Stop Recodring first", 2).show();
				else
				{
					// send media to different activity
					Intent intent = new Intent(NewcaptureActivty.this,
							ScreenCapturedEdit.class);
					intent.putExtra("videoPath", capturedcontentPath);
					intent.putExtra("fromGallary", false);
					intent.putExtra("imageOrVideo", false);
					startActivity(intent);
				}

			}
			else
			{
				//				button.se
				if(isRecording) // recording is going please stop it
				{
					Toast.makeText(this,"Stop Recodring first", 2).show();
				}
				else
				{

					// switch the camera
					if(isRecordingMode) // switch to video recording
					{
						Drawable img2 = getApplicationContext().getResources()
								.getDrawable(R.drawable.camcorder);
						img2.setBounds(0, 0, 30, 30);
						button.setCompoundDrawables(null, img2, null, null);
						System.out.println("Image Recording Mode");
						releaseCamera();

						changecameraMode(false);
						//					prepareImageRecorder();
						isRecordingMode=false;
					}
					else // switch to photot view
					{
						System.out.println("Video  Recording Mode");
						Drawable img2 = getApplicationContext().getResources()
								.getDrawable(R.drawable.switchmode);
						img2.setBounds(0, 0, 30, 30);
						button.setCompoundDrawables(null, img2, null, null);
						releaseCamera();
						changecameraMode(true);
						//					prepareVideoRecorder();
						//					setPreview();
						isRecordingMode=true;
					}
				}
			}
			break;


		case R.id.imageView1:
			releaseCamera();
			if(isRecording)
				releaseMediaRecorder();
			this.finish();			
		default:
			break;
		}
	}



	private void camptureVideoContents()
	{
		System.out.println("IS rsording :: "+isRecording);
		if (isRecording) {
			System.out.println("IS rsording :: "+isRecording);
			// stop recording and release camera
			mMediaRecorder.stop();  // stop the recording
			releaseMediaRecorder(); // release the MediaRecorder object
			mCamera.lock();         // take camera access back from MediaRecorder
			countDownTimer.cancel();
			// inform the user that recording has stopped
			//             setCaptureButtonText("Capture");
			isRecording = false;
		} else {
			System.out.println(" start video recording");
			// initialize video camera
			if (prepareVideoRecorder()) {
				// Camera is available and unlocked, MediaRecorder is prepared,
				// now you can start recording
				try{
					mMediaRecorder.start();
					progressBar.setProgress(1000);
					progressBar.setMax(30000);
					startCountDown();
					isRecording = true;
				}
				catch(Exception exception){
					exception.printStackTrace();
					releaseMediaRecorder();
					Toast.makeText(this, "Unable To Record Video", Toast.LENGTH_LONG).show();
				}
				System.out.println("prepareVideoRecorder");
				// inform the user that recording has started
				// setCaptureButtonText("Stop");
				System.out.println("prepareVideoRecorder");


			} else {
				System.out.println("release");
				// prepare didn't work, release the camera
				releaseMediaRecorder();
				// inform user
			}
		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		System.out.println("On pause");
		releaseMediaRecorder();       // if you are using MediaRecorder, release it first
		releaseCamera();              // release the camera immediately on pause event
	}
	public void flipit(boolean b) {
		//		releaseMediaRecorder();
		//		releaseCamera();
		Intent intent=new Intent(this, NewcaptureActivty.class);
		System.out.println("Flip it::"+b);
		intent.putExtra("isBackCamera", b);
		intent.putExtra("videocapture", isRecordingMode);
		intent.putExtra("noOfcamera", noOfCamera);

		startActivity(intent);
		overridePendingTransition(anim.fade_in, anim.fade_out);
		this.finish();

	}

	private void changecameraMode(boolean isVideoRecording)
	{
		//		releaseMediaRecorder();
		//		releaseCamera();
		Intent intent=new Intent(this, NewcaptureActivty.class);
		intent.putExtra("videocapture", isVideoRecording);
		intent.putExtra("isBackCamera", isbackCamera);
		intent.putExtra("noOfcamera", noOfCamera);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		overridePendingTransition(anim.fade_in, anim.fade_out);
		this.finish();
	}

	private void startCountDown()
	{
		System.out.println("start Count Down");
		countDownTimer = new CountDownTimer(MAXTIME, INTERVEL) {


			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				if (millisUntilFinished == 0) {
					progressBar.setProgress(1000);
				}
				TextView tvtime = (TextView) findViewById(R.id.textView2);
				int ticktime = progressBar.getProgress() + 1000;
				ticktime = ticktime / 1000;
				tvtime.setText("00:" + ticktime);

				System.out.println("on tick" + progressBar.getProgress());
				progressBar.setProgress(progressBar.getProgress() + 1000);

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				camptureVideoContents();
				//				brec.setText("Record");
				// Toast.makeText(ScreencaptureActivity.this,"Video Recorded",
				// 2000).show();

				Log.v("ScreenCaptureAcivity", "Video Recorded");
				myProgress = 0;
				Log.v("", "str Sending file recorded video 30 sec: " + capturedcontentPath);
				Intent intent = new Intent(NewcaptureActivty.this,
						ScreenCapturedEdit.class);
				intent.putExtra("videoPath", capturedcontentPath);
				intent.putExtra("fromGallary", false);
				intent.putExtra("imageOrVideo", false);
				startActivity(intent);
			}
		};
		countDownTimer.start();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& data != null) {
			// Let's read picked image data - its URI
			Uri pickedImage = data.getData();

			Log.v("", "RUTUJA GET TYPE:: " + data.getType()
					+ "   Content resolver type :: "
					+ getContentResolver().getType(data.getData()));
			// Let's read picked image path using content resolver
			String[] filePath = { MediaStore.Images.Media.DATA };
			Log.i("pickedImage-->", "" + pickedImage);

			Cursor cursor = getContentResolver().query(pickedImage, filePath,
					null, null, null);
			cursor.moveToFirst();
			capturedcontentPath = cursor.getString(cursor.getColumnIndex(filePath[0]));
			// lastpath = pickedImage.toString();
			Log.i("image/video/audio Path-lastpath->",
					"" + pickedImage.toString());

			// Now we need to set the GUI ImageView data with data read from the
			// picked file.
			// image.setImageBitmap(BitmapFactory.decodeFile(imagePath));

			// At the end remember to close the cursor or you will end with the
			// RuntimeException!
			cursor.close();

			releaseCamera();
			Log.i("after release cam--111111-->", "released");

			String type = getContentResolver().getType(data.getData());
			String[] strType = type.split("/");
			Log.v("", "rutuja type length:: " + strType.length);
			Log.v("", "str Sending file selected from gallary: " + capturedcontentPath);

			Intent intent = new Intent(NewcaptureActivty.this,
					ScreenCapturedEdit.class);
			if (null != strType && strType[0].equalsIgnoreCase("image"))
				intent.putExtra("imageOrVideo", true);
			else if (null != strType && strType[0].equalsIgnoreCase("video"))
				intent.putExtra("imageOrVideo", false);
			// ******************Get path from Continue
			// screen8*******************

			intent.putExtra("videoPath", capturedcontentPath);
			intent.putExtra("fromGallary", true);

			startActivity(intent);

		}
	}
}
