package com.android.brainslam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ScreencaptureActivity extends Activity implements Callback,
		android.view.SurfaceHolder.Callback {

	String lastpath;

	int switchmode = 0, paused = 0;
	String filename;
	String path;
	CountDownTimer countDownTimer;
	int myProgress = 0;
	int MAXTIME = 31000;
	int INTERVEL = 1000;
	ProgressBar progressBar;

	int RESULT_LOAD_IMAGE = 1;
	private SurfaceHolder surfaceHolder;
	private SurfaceView surfaceView;
	public MediaRecorder mrec;// = new MediaRecorder();
	private Camera mCamera;
	ImageView flash, flipcam;
	Button bstartover, brec, bnext, flashButton;
	TextView tvflash;
	private boolean isFlashOn;
	private boolean hasFlash;
	Parameters params;
	// MediaPlayer mp;

	boolean mbActive;
	OrientationEventListener orientationListener;
	Boolean isback = true;
	private int progressStatus = 0;

	private int currentOrientation = -1;
	private static final int ORIENTATION_PORTRAIT_NORMAL = 1;
	private static final int ORIENTATION_PORTRAIT_INVERTED = 2;
	private static final int ORIENTATION_LANDSCAPE_NORMAL = 3;
	private static final int ORIENTATION_LANDSCAPE_INVERTED = 4;
	int fromDegree=0;
	int toDegree=0;
	
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screencapture);

		surfaceView = (SurfaceView) findViewById(R.id.surfaceView1);

		// mCamera = Camera.open();


		bstartover = (Button) findViewById(R.id.button1);
		brec = (Button) findViewById(R.id.button2);

		flashButton = (Button) findViewById(R.id.buttonFlash);

		flipcam = (ImageView) findViewById(R.id.imageView3);

		if (Camera.getNumberOfCameras() > 1) {
			flipcam.setVisibility(View.VISIBLE);
		} else {
			flipcam.setVisibility(View.GONE);
		}

		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(ScreencaptureActivity.this);

		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		bnext = (Button) findViewById(R.id.button3);

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
							
							Log.i("Orientation","current Degree"+flashButton.getRotation());
							
							
							break;
						case ORIENTATION_LANDSCAPE_NORMAL:    //3
							fromDegree=0;
							toDegree=90;
							rotateAnimation(flashButton,  fromDegree, toDegree);
							rotateAnimation(flipcam,  fromDegree, toDegree);
//							rotateAnimation(bstartover,  fromDegree, toDegree);
//							rotateAnimation(bnext,  fromDegree, toDegree);
							
							Log.i("Oreintation", "Orientation = 0");
							Log.i("Orientation","current Degree"+flashButton.getRotation());
							
							break;
						case ORIENTATION_PORTRAIT_INVERTED:  //2
							fromDegree=90;
							toDegree=180;
							rotateAnimation(flashButton,  fromDegree, toDegree);
							rotateAnimation(flipcam,  fromDegree, toDegree);
//							rotateAnimation(bstartover,  fromDegree, toDegree);
//							rotateAnimation(bnext,  fromDegree, toDegree);
							Log.i("Oreintation", "Orientation = 270");
							Log.i("Orientation","current Degree"+flashButton.getRotation());
							
							break;
						case ORIENTATION_LANDSCAPE_INVERTED: 		//4
							fromDegree=180;
							toDegree=270;
							rotateAnimation(flashButton,  fromDegree, toDegree);
							rotateAnimation(flipcam,  fromDegree, toDegree);
//							rotateAnimation(bstartover,  fromDegree, toDegree);
//							rotateAnimation(bnext,  fromDegree, toDegree);
							Log.i("Oreintation", "Orientation = 180");
							Log.i("Orientation","current Degree"+flashButton.getRotation());
							
							
							break;
						}
						
					}

				}
			};
		}
		if (orientationListener.canDetectOrientation()) {
			orientationListener.enable();
		}

		// First check if device is supporting flashlight or not
		hasFlash = getApplicationContext().getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

		// get the camera
		// getCamera();

		final PictureCallback capturedIt = new PictureCallback() {
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {

				FileOutputStream outStream = null;
				try {

					lastpath = String.format(
							Environment.getExternalStorageDirectory()
									+ "/%d.jpg", System.currentTimeMillis());
					outStream = new FileOutputStream(lastpath);
					outStream.write(data);
					outStream.close();

					Log.d(".lastpath..111111--",
							"lastpath---> "
									+ String.format("/sdcard/%d.jpg",
											System.currentTimeMillis()));

					releaseCamera();
					Log.i("after release cam-->", "released");
					ScreencaptureActivity.this.finish();
					Log.v("", "str Sending file captured photo: " + lastpath);
					Intent intent = new Intent(ScreencaptureActivity.this,
							ScreenCapturedEdit.class);
					intent.putExtra("videoPath", lastpath);
					intent.putExtra("fromGallary", false);
					intent.putExtra("imageOrVideo", true);
					startActivity(intent);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
				}

				/*
				 * Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data
				 * .length); if(bitmap==null){
				 * Toast.makeText(getApplicationContext(), "not taken",
				 * Toast.LENGTH_SHORT).show(); } else {
				 * Toast.makeText(getApplicationContext(), "taken",
				 * Toast.LENGTH_SHORT).show(); }
				 */
				// mCamera.release();
			}
		};

		flipcam.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {

				// myViewer is the SurfaceView object which uses
				// the camera

				if (switchmode == 0) {
					if (brec.getText().equals("Record")) {
						flipit();
					} else {
						Toast.makeText(ScreencaptureActivity.this,
								"Stop Recording first", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					flipit();

					// mCamera.stopPreview();
					// mCamera.release();
					// mCamera =
					// Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
					try {
						mCamera.setPreviewDisplay(surfaceHolder);
					} catch (IOException e) {
						e.printStackTrace();
					}
					mCamera.startPreview();
				}

			}

		});

		flashButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (hasFlash) {
					if (isFlashOn) {
						// turn off flash
						turnOffFlash();
						// tvflash.setText("OFF");
						flashButton.setText("OFF");
						
						
					} else {
						// turn on flash
						turnOnFlash();
						// tvflash.setText("ON");
						flashButton.setText("ON");
						flashButton.setTextColor(Color.WHITE);

					}
				} else {
					final AlertDialog alert = new AlertDialog.Builder(
							ScreencaptureActivity.this).create();
					alert.setTitle("Error");
					alert.setMessage("Sorry, your device doesn't support flash light!");
					alert.setButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// closing the application
									// finish();
									alert.dismiss();
								}
							});
					alert.show();
				}

			}

		});
		bstartover.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {

				if (bstartover.getText().equals("Photo Roll")) {
					final Intent galleryIntent = new Intent(
							Intent.ACTION_GET_CONTENT);
					galleryIntent.setType("*/*");
					startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);

				} else {
					if (brec.getText().equals("Record")) // at rec off
					{

						try {
							startRecording();
						} catch (Exception e) {

							String message = e.getMessage();
							Log.i(null, "Problem " + message);
							mrec.release();
						}

					} else if (brec.getText().equals("Stop")) // at rec on
					{

						stopRecording();

						brec.setText("Record");
						mbActive = true;

						try {
							startRecording();
						} catch (Exception e) {

							String message = e.getMessage();
							Log.i(null, "Problem " + message);
							mrec.release();
						}

					}

				}
			}
		});

		bnext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				if (bnext.getText().equals("Continue")) {
					// move to next activity to upload or share

					releaseMediaRecorder();
					releaseCamera();

					// ******************Get path from Continue
					// screen8*******************

					// only for recording video

					progressBar.setProgress(0);
					myProgress = 0;

					Log.v("", "str Sending file captured video but half: "
							+ lastpath);
					Intent intent = new Intent(ScreencaptureActivity.this,
							ScreenCapturedEdit.class);
					intent.putExtra("videoPath", lastpath);
					intent.putExtra("fromGallary", false);
					intent.putExtra("imageOrVideo", false);
					// intent.putExtra("lastpath",lastpath);
					startActivity(intent);

				} else {
					switchmode = 1;
//					   bnext.setCompoundDrawablesWithIntrinsicBounds(
//	        					0,R.drawable.switchmode, 0, 0);
					mCamera.stopPreview();

					Display display = ScreencaptureActivity.this
							.getWindowManager().getDefaultDisplay();

					if (display.getRotation() == Surface.ROTATION_0) {

						if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
							// parameters.setPreviewSize(h, w);
							mCamera.setDisplayOrientation(0);
						} else {
							// parameters.setPreviewSize(h, w);
							mCamera.setDisplayOrientation(90);
						}
					}

					else if (display.getRotation() == Surface.ROTATION_90) {
						if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
							// parameters.setPreviewSize(w, h);
							mCamera.setDisplayOrientation(270);
						} else {
							// parameters.setPreviewSize(w, h);
							mCamera.setDisplayOrientation(0);
						}
					}

					else if (display.getRotation() == Surface.ROTATION_180) {
						if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
							// parameters.setPreviewSize(h, w);
							mCamera.setDisplayOrientation(180);
						} else {
							// parameters.setPreviewSize(h, w);
							mCamera.setDisplayOrientation(270);
						}
					}

					else if (display.getRotation() == Surface.ROTATION_270) {
						if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
							// parameters.setPreviewSize(w, h);
							mCamera.setDisplayOrientation(90);
						} else {
							// parameters.setPreviewSize(w, h);
							mCamera.setDisplayOrientation(180);
						}
					}

					try {
						mCamera.setPreviewDisplay(surfaceHolder);
					} catch (IOException e) {
						e.printStackTrace();
					}
					// "this" is a SurfaceView which implements
					// SurfaceHolder.Callback,
					// as found in the code examples
					// mCamera.setPreviewCallback((PreviewCallback)
					// ScreencaptureActivity.this);
					mCamera.startPreview();
					// mCamera.takePicture(null, null, capturedIt) ;
				}

			}
		});

		brec.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				// *******************************************************************************************

				if (switchmode == 1) {
					mCamera.takePicture(null, null, capturedIt);

					// ******************Get path from Continue
					// screen8*******************

					// intent.putExtra("Media_path",lastpath);

					// progressBar.setProgress(0);
					myProgress = 0;
					// Photo capture

				} else {
					// if(brec.getText().equals("Continue")){
					if (brec.getText().equals("Record")) {
						try {

							startRecording();

						} catch (Exception e) {
							String message = e.getMessage();
							Log.i(null, "Problem " + message);
							mrec.release();
						}
					} else if (brec.getText().equals("Stop")) {

						stopRecording();
						brec.setText("Record");
						mbActive = true;

					}

					bstartover.setText("Start Over");
					Drawable img = getApplicationContext().getResources()
							.getDrawable(R.drawable.startover);
					img.setBounds(0, 0, 30, 30);
					bstartover.setCompoundDrawables(null, img, null, null);

					bnext.setText("Continue");
					Drawable img1 = getApplicationContext().getResources()
							.getDrawable(R.drawable.nextbutton);
					img1.setBounds(0, 0, 30, 30);
					bnext.setCompoundDrawables(null, img1, null, null);

				}

			}

		});

	}

	public void cancel(View v) {
		if (mrec != null)
			stopRecording();

		releaseCamera();
		ScreencaptureActivity.this.finish();
//		startActivity(new Intent(ScreencaptureActivity.this,
//				BrainSlamMainActivity.class));

	}

	public void flipit() {

		if (Camera.getNumberOfCameras() >= 2) {

			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;

			releaseMediaRecorder();
		}
		if (isback) {
			mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
			mCamera.setDisplayOrientation(90);
			// mCamera.startPreview();
			Toast.makeText(ScreencaptureActivity.this, "Front Camera ON",
					Toast.LENGTH_LONG).show();
			isback = false;
		} else {
			mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);

			mCamera.setDisplayOrientation(90);

			Toast.makeText(ScreencaptureActivity.this, "Back Camera ON",
					Toast.LENGTH_LONG).show();
			isback = true;
		}
		// -----------------------------------------------------------------------------------

	}

	@Override
	protected void onPause() {
		super.onPause();
		orientationListener.enable();
		// on pause turn off the flash
		turnOffFlash();

		// orientationListener.disable();
	}

	private void getCamera() {
		if (mCamera == null) {
			try {

				mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
				Toast.makeText(ScreencaptureActivity.this, "Back Camera",
						Toast.LENGTH_LONG).show();

				if (mCamera == null) {
					mCamera = Camera
							.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
					Toast.makeText(ScreencaptureActivity.this, "Front Camera",
							Toast.LENGTH_LONG).show();
					isback = false;
				}

				params = mCamera.getParameters();

			} catch (RuntimeException e) {
				e.printStackTrace();
				Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
			}
		}
	}

	/*
	 * Turning On flash
	 */
	private void turnOnFlash() {
		if (!isFlashOn) {
			if (mCamera == null || params == null) {
				return;
			}
			// play sound
			// playSound();

			params = mCamera.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_TORCH);
			mCamera.setParameters(params);
			// mCamera.startPreview();
			isFlashOn = true;

			// changing button/switch image
			toggleButtonImage();
		}

	}

	/*
	 * Turning Off flash
	 */
	private void turnOffFlash() {
		if (isFlashOn) {
			if (mCamera == null || params == null) {
				return;
			}
			// play sound
			// playSound();

			params = mCamera.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_OFF);
			mCamera.setParameters(params);
			// mCamera.stopPreview();
			isFlashOn = false;

			// changing button/switch image
			toggleButtonImage();
		}
	}

	protected void startRecording() throws IOException {
		// if(mCamera==null)
		// mCamera = Camera.open();

		paused = 0;
		brec.setText("Stop");
		path = Environment.getExternalStorageDirectory().getAbsolutePath()
				.toString();
		Date date = new Date();
		filename = "/rec" + date.toString().replace(" ", "_").replace(":", "_")
				+ ".mp4";

		// create empty file it must use
		// File file=new File(path,filename);

		lastpath = path + filename;

		Log.i("lastpath..11111..>>>", "" + lastpath);

		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar.setProgress(1000);
		progressBar.setMax(30000);

		mrec = new MediaRecorder();

		mCamera.lock();
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

		myProgress = 0;
		MAXTIME = 31000;
		INTERVEL = 1000;
		progressBar.setProgress(0);

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
				stopRecording();
				brec.setText("Record");
				// Toast.makeText(ScreencaptureActivity.this,"Video Recorded",
				// 2000).show();

				Log.v("ScreenCaptureAcivity", "Video Recorded");
				myProgress = 0;
				Log.v("", "str Sending file recorded video 30 sec: " + lastpath);
				Intent intent = new Intent(ScreencaptureActivity.this,
						ScreenCapturedEdit.class);
				intent.putExtra("videoPath", lastpath);
				intent.putExtra("fromGallary", false);
				intent.putExtra("imageOrVideo", false);
				startActivity(intent);
			}
		};
		countDownTimer.start();

	}

	protected void stopRecording() {

		// progressBar.setProgress(0);
		paused = 1;

		progressBar.setProgress(progressBar.getProgress());

		scanMedia(path + filename);
		if (mrec != null) {
			try {

				System.out.println("Stop Recording");
				mrec.stop();
				mrec.reset();
				mrec.release();
				mrec = null;
				mCamera.stopPreview();

				// recorder.stop();

				countDownTimer.cancel();

			} catch (RuntimeException stopException) {
				// handle cleanup here
				stopException.printStackTrace();
			}
			// mCamera.lock();

			// progressBar=null;
			// mCamera.release();
			// mCamera.lock();
		}
	}

	@SuppressWarnings("unused")
	private void releaseMediaRecorder() {

		if (mrec != null) {
			mrec.reset(); // clear recorder configuration
			mrec.release(); // release the recorder object
		}
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}

	}

	private void toggleButtonImage() {
		if (isFlashOn) {
			flashButton.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.flash, 0, 0, 0);
		} else {
			flashButton.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.flash, 0, 0, 0);
		}
	}

	private void scanMedia(String path) {
		File file = new File(path);
		Uri uri = Uri.fromFile(file);
		Intent scanFileIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
		sendBroadcast(scanFileIntent);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub

		super.onConfigurationChanged(newConfig);
		setContentView(R.layout.activity_screencapture);

		Log.i("paused--->>", "" + paused);
		if (paused == 0) {
			releaseMediaRecorder();
		}

		releaseCamera();

		// countDownTimer.cancel();
		if (countDownTimer != null) {
			countDownTimer.cancel();
			countDownTimer = null;
		}
		ScreencaptureActivity.this.finish();
		startActivity(new Intent(ScreencaptureActivity.this,
				ScreencaptureActivity.class));

		Toast.makeText(ScreencaptureActivity.this, "Orientation Changed",
				Toast.LENGTH_SHORT).show();
		Log.i("Orientation", "ORIENTATION CHANGED");

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
			lastpath = cursor.getString(cursor.getColumnIndex(filePath[0]));
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
			Log.v("", "str Sending file selected from gallary: " + lastpath);

			Intent intent = new Intent(ScreencaptureActivity.this,
					ScreenCapturedEdit.class);
			if (null != strType && strType[0].equalsIgnoreCase("image"))
				intent.putExtra("imageOrVideo", true);
			else if (null != strType && strType[0].equalsIgnoreCase("video"))
				intent.putExtra("imageOrVideo", false);
			// ******************Get path from Continue
			// screen8*******************

			intent.putExtra("videoPath", lastpath);
			intent.putExtra("fromGallary", true);

			startActivity(intent);

		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		Log.i("Orientation", "SURFACE CHANGED");

		// if(mCamera!=null)
		// if ( getResources().getConfiguration().orientation ==
		// Configuration.ORIENTATION_LANDSCAPE) {
		// mCamera.setDisplayOrientation(180);
		// }else
		// {

		if (mCamera != null)
			mCamera.setDisplayOrientation(90);
		// }

	}

	public void surfaceCreated(SurfaceHolder holder) {

		// Display display = this.getWindowManager()
		// .getDefaultDisplay();
		// Checks the orientation of the screen

		Log.i("Orientation", "SURFACE CREATED");

		if (mCamera != null) {

			Parameters params = mCamera.getParameters();
			mCamera.setParameters(params);
			// mCamera.setDisplayOrientation(90);
			Log.i("Surface", "Created");
		} else {

			Toast.makeText(getApplicationContext(), "Camera not available!",
					Toast.LENGTH_LONG).show();
			finish();
		}

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// mCamera.stopPreview();
		// mCamera.release();

		Log.i("Orientation", "SURFACE DESTROYED");

		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;

		}
		// stopRecording();

	}

	@Override
	protected void onResume() {
		super.onResume();
		orientationListener.enable();
		// // on resume turn on the flash
		// if(hasFlash)
		// turnOnFlash();
	}

	@Override
	protected void onStart() {
		super.onStart();

		// on starting the app get the camera params
		getCamera();
	}

	@Override
	protected void onDestroy() {

		releaseMediaRecorder();
		// stopRecording();

		// progressBar.setProgress(0);
		myProgress = 0;

		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
		}

		super.onDestroy();
	}

	private void onstop() {
		// TODO Auto-generated method stub
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, 0, 0, "Start");
		return super.onCreateOptionsMenu(menu);
	}

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item)
	// {
	// if(item.getTitle().equals("Start"))
	// {
	// try {
	//
	// startRecording();
	// item.setTitle("Stop");
	//
	// } catch (Exception e) {
	//
	// String message = e.getMessage();
	// Log.i(null, "Problem " + message);
	// mrec.release();
	// }
	//
	// }
	// else if(item.getTitle().equals("Stop"))
	// {
	// mrec.stop();
	// mrec.release();
	// mrec = null;
	// item.setTitle("Start");
	// }
	//
	// return super.onOptionsItemSelected(item);
	// }

	@Override
	public void invalidateDrawable(Drawable arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void scheduleDrawable(Drawable arg0, Runnable arg1, long arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void unscheduleDrawable(Drawable arg0, Runnable arg1) {
	}

	private void rotateAnimation(View view, float rotateFrom, float rotateTo) {
		Animation rotateAnim = new RotateAnimation(rotateFrom, rotateTo,view.getMeasuredWidth() / 2, view.getMeasuredHeight() / 2);
		System.out.println("Inside Roate Anim");
		
		rotateAnim.setDuration(1000); // duration in ms
		rotateAnim.setRepeatCount(0); // -1 = infinite repeated
		rotateAnim.setFillAfter(true); // keep rotation after animation

		view.startAnimation(rotateAnim);
	}


}
