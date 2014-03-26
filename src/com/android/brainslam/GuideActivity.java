package com.android.brainslam;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.brainslam.NetworkHandler.AsyncCallBack;

public class GuideActivity extends Activity implements OnTouchListener, AsyncCallBack{
	private ViewFlipper viewFlipper;
	private float lastX;
	int childId = 0;
	
	RelativeLayout guideMain;
	String TAG = "GuideActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		viewFlipper=(ViewFlipper)findViewById(R.id.viewFlipper1);
		TextView bstart=(TextView) findViewById(R.id.start_brainslam);
		
		//	flipper.setInAnimation(an/)
        guideMain=(RelativeLayout)findViewById(R.id.guide_main);
		guideMain.setOnTouchListener(this);
		
		
		
		bstart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
////				System.out.println("on click");
				Intent intent=new Intent(GuideActivity.this, LandingActivity.class);
			    intent.putExtra("ks", getIntent().getExtras().getString("ks"));
				GuideActivity.this.startActivity(intent);
				GuideActivity.this.overridePendingTransition(R.anim.slidein_right, R.anim.slideout_left);

				
//				Intent intent1=new Intent(GuideActivity.this,UploadDetailsActivity.class);
//				GuideActivity.this.startActivity(intent1);
//				GuideActivity.this.overridePendingTransition(R.anim.slidein_right, R.anim.slideout_left);


//				AlertDialog.Builder alert = new AlertDialog.Builder(GuideActivity.this);
//				alert.setTitle("Work in progress");
//				alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//					}
//					
//					
//				});
//				alert.show();
				
//				startActivity(new Intent(GuideActivity.this, LandingActivity.class));
//				GuideActivity.this.overridePendingTransition(R.anim.slidein_right, R.anim.slideout_left);
				
			}
		});
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.guide, menu);
		return true;
	}


//	public void onClick(View view)
//	{
//		
//	}

	@Override
	public boolean onTouch(View v, MotionEvent touchevent) {
		
		ImageView secondcircle=(ImageView)findViewById(R.id.guides_circle2);
		ImageView firstcircle=(ImageView)findViewById(R.id.guides_circle1);
		ImageView thirdcircle=(ImageView)findViewById(R.id.guides_circle3);
		ImageView fourcircle=(ImageView)findViewById(R.id.guides_circle4);
		// TODO Auto-generated method stub
		switch (touchevent.getAction())
		{
		// when user first touches the screen to swap
		case MotionEvent.ACTION_DOWN: 
		{
			lastX = touchevent.getX();
			break;
		}
		case MotionEvent.ACTION_UP: 
		{
			float currentX = touchevent.getX();

			// if left to right swipe on screen
			if (lastX < currentX) 
			{

				// If no more View/Child to flip
				if (viewFlipper.getDisplayedChild() == 0)
				{

					//firstcircle.setBackgroundColor(Color.BLACK);
					//secondcircle.setBackgroundColor(Color.WHITE);
					break;
				}

				// set the required Animation type to ViewFlipper
				// The Next screen will come in form Left and current Screen will go OUT from Right 
				viewFlipper.setInAnimation(this, R.anim.in_from_left);
				viewFlipper.setOutAnimation(this, R.anim.out_to_right);
				// Show the next Screen
				viewFlipper.showNext();
				
				childId=viewFlipper.getDisplayedChild();

			}

			// if right to left swipe on screen
			if (lastX > currentX)
			{
				System.out.println("child"+viewFlipper.getDisplayedChild());


				if (viewFlipper.getDisplayedChild() == 1)
					break;
				// set the required Animation type to ViewFlipper
				// The Next screen will come in form Right and current Screen will go OUT from Left 
				viewFlipper.setInAnimation(this, R.anim.in_from_right);
				viewFlipper.setOutAnimation(this, R.anim.out_to_left);
				// Show The Previous Screen
				viewFlipper.showPrevious();
				childId=viewFlipper.getDisplayedChild();

			}
			switch(childId)
			{
			case 0:
				System.out.println("0 th");
				guideMain.setBackgroundColor(getResources().getColor(R.color.blue_kaltura));
				firstcircle.setBackgroundResource(R.drawable.gray_circle);
				secondcircle.setBackgroundResource(R.drawable.white_circle);
				thirdcircle.setBackgroundResource(R.drawable.white_circle);
				fourcircle.setBackgroundResource(R.drawable.white_circle);

				break;
			case 1:
				guideMain.setBackgroundColor(getResources().getColor(R.color.orange_kaltura));
				System.out.println("1 th");
				firstcircle.setBackgroundResource(R.drawable.white_circle);
				secondcircle.setBackgroundResource(R.drawable.white_circle);
				thirdcircle.setBackgroundResource(R.drawable.white_circle);
				fourcircle.setBackgroundResource(R.drawable.gray_circle);
				break;
			case 2:
				System.out.println("2 th");
				guideMain.setBackgroundColor(getResources().getColor(R.color.blue_kaltura));
				firstcircle.setBackgroundResource(R.drawable.white_circle);
				secondcircle.setBackgroundResource(R.drawable.white_circle);
				thirdcircle.setBackgroundResource(R.drawable.gray_circle);
				fourcircle.setBackgroundResource(R.drawable.white_circle);
				break;
			case 3:
				System.out.println("3 th");
				guideMain.setBackgroundColor(getResources().getColor(R.color.orange_kaltura));
				firstcircle.setBackgroundResource(R.drawable.white_circle);
				secondcircle.setBackgroundResource(R.drawable.gray_circle);
				thirdcircle.setBackgroundResource(R.drawable.white_circle);
				fourcircle.setBackgroundResource(R.drawable.white_circle);
				break;
			}
			break;
		}
		}
		return false;
	}

	@Override
	public void onReceive(JSONObject object, int id) {
		// TODO Auto-generated method stub
		Log.v(TAG, "FCAEBOOK FRIENDS RESPONSE:::: "+object.toString());
	}
}
