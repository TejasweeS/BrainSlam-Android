package com.android.brainslam;

import com.android.brainslam.constants.Constants;
import com.android.utils.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ToggleButton;

public class FriendNotificationActivity extends Activity {

	ToggleButton newuplaods, newfollowers, homestreamupdate;
	ToggleButton newFeaturedVideos;
	ToggleButton newMessage;
	ToggleButton crewactivityUpdate;
	ToggleButton newcrewfollowers;
	ToggleButton friendRequest;
	ToggleButton newplaylistSubscribers;
	ToggleButton newplaylist;
	ToggleButton playlistUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friend__notification);
		newuplaods = (ToggleButton) findViewById(R.id.upload_toggleButton);
		newfollowers = (ToggleButton) findViewById(R.id.follower_toggleButton);
		homestreamupdate = (ToggleButton) findViewById(R.id.home_stream_toggleButton);
		newFeaturedVideos = (ToggleButton) findViewById(R.id.new_featured_toggleButton);
		newMessage = (ToggleButton) findViewById(R.id.new_messages_toggleButton);
		crewactivityUpdate = (ToggleButton) findViewById(R.id.crew_activity_update_toggleButton);
		newcrewfollowers = (ToggleButton) findViewById(R.id.new_follower_toggleButton);
		friendRequest = (ToggleButton) findViewById(R.id.friend_request_toggleButton);
		newplaylistSubscribers = (ToggleButton) findViewById(R.id.new_playlist_subscriber_toggleButton);
		newplaylist = (ToggleButton) findViewById(R.id.new_playlist_subscriber1_toggleButton);
		playlistUpdate = (ToggleButton) findViewById(R.id.playlist_update_toggleButton);

		Boolean value = Utils.getDataBoolean(FriendNotificationActivity.this,
				Constants.PREFERENCES_NAME, Constants.NEWUPLOADS);
		newuplaods.setChecked(value);

		value = Utils.getDataBoolean(FriendNotificationActivity.this,
				Constants.PREFERENCES_NAME, Constants.NEWFOLLOWERS);
		newfollowers.setChecked(value);
		value = Utils.getDataBoolean(FriendNotificationActivity.this,
				Constants.PREFERENCES_NAME, Constants.HOMESTREAMUPDATE);
		homestreamupdate.setChecked(value);
		value = Utils.getDataBoolean(FriendNotificationActivity.this,
				Constants.PREFERENCES_NAME, Constants.NEWFEATUREVIDEOS);
		newFeaturedVideos.setChecked(value);
		value = Utils.getDataBoolean(FriendNotificationActivity.this,
				Constants.PREFERENCES_NAME, Constants.NEWMESSAGE);
		newMessage.setChecked(value);
		value = Utils.getDataBoolean(FriendNotificationActivity.this,
				Constants.PREFERENCES_NAME, Constants.CREWACTIVITYUPDATES);
		crewactivityUpdate.setChecked(value);
		value = Utils.getDataBoolean(FriendNotificationActivity.this,
				Constants.PREFERENCES_NAME, Constants.NEWCREWFOLLOWERS);
		newcrewfollowers.setChecked(value);
		value = Utils.getDataBoolean(FriendNotificationActivity.this,
				Constants.PREFERENCES_NAME, Constants.FRIENDREQUEST);
		friendRequest.setChecked(value);
		value = Utils.getDataBoolean(FriendNotificationActivity.this,
				Constants.PREFERENCES_NAME, Constants.NEWPLAYLISTSUBSCRIBERS);
		newplaylistSubscribers.setChecked(value);
		value = Utils.getDataBoolean(FriendNotificationActivity.this,
				Constants.PREFERENCES_NAME, Constants.NEWPLAYLIST);
		newplaylist.setChecked(value);
		value = Utils.getDataBoolean(FriendNotificationActivity.this,
				Constants.PREFERENCES_NAME, Constants.PLAYLISTUPDATES);
		playlistUpdate.setChecked(value);

		ImageView backtomyaccount = (ImageView) findViewById(R.id.frieand_notification_back_icon);
		backtomyaccount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				FriendNotificationActivity.this.finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend__notification, menu);
		return true;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (newuplaods.isChecked()) {
			Utils.storeData(FriendNotificationActivity.this,
					Constants.PREFERENCES_NAME, Constants.NEWUPLOADS, true);

		} else {
			Utils.storeData(FriendNotificationActivity.this,
					Constants.PREFERENCES_NAME, Constants.NEWUPLOADS, false);

		}
		if (newfollowers.isChecked()) {
			Utils.storeData(FriendNotificationActivity.this,
					Constants.PREFERENCES_NAME, Constants.NEWFOLLOWERS, true);

		} else {
			Utils.storeData(FriendNotificationActivity.this,
					Constants.PREFERENCES_NAME, Constants.NEWFOLLOWERS, false);

		}
		if (homestreamupdate.isChecked()) {
			Utils.storeData(FriendNotificationActivity.this,
					Constants.PREFERENCES_NAME, Constants.HOMESTREAMUPDATE,
					true);

		} else {
			Utils.storeData(FriendNotificationActivity.this,
					Constants.PREFERENCES_NAME, Constants.HOMESTREAMUPDATE,
					false);

		}
		if (newFeaturedVideos.isChecked()) {
			Utils.storeData(FriendNotificationActivity.this,
					Constants.PREFERENCES_NAME, Constants.NEWFEATUREVIDEOS,
					true);

		} else {
			Utils.storeData(FriendNotificationActivity.this,
					Constants.PREFERENCES_NAME, Constants.NEWFEATUREVIDEOS,
					false);

		}
		if (newMessage.isChecked()) {
			Utils.storeData(FriendNotificationActivity.this,
					Constants.PREFERENCES_NAME, Constants.NEWMESSAGE, true);

		} else {
			Utils.storeData(FriendNotificationActivity.this,
					Constants.PREFERENCES_NAME, Constants.NEWMESSAGE, false);

		}

		if (crewactivityUpdate.isChecked()) {
			Utils.storeData(FriendNotificationActivity.this,
					Constants.PREFERENCES_NAME, Constants.CREWACTIVITYUPDATES,
					true);

		} else {

			Utils.storeData(FriendNotificationActivity.this,
					Constants.PREFERENCES_NAME, Constants.CREWACTIVITYUPDATES,
					false);

		}
		if (newcrewfollowers.isChecked()) {
			Utils.storeData(FriendNotificationActivity.this,
					Constants.PREFERENCES_NAME, Constants.NEWCREWFOLLOWERS,
					true);

		} else {
			Utils.storeData(FriendNotificationActivity.this,
					Constants.PREFERENCES_NAME, Constants.NEWCREWFOLLOWERS,
					false);

		}
		if (friendRequest.isChecked()) {
			Utils.storeData(FriendNotificationActivity.this,
					Constants.PREFERENCES_NAME, Constants.FRIENDREQUEST, true);

		} else {
			Utils.storeData(FriendNotificationActivity.this,
					Constants.PREFERENCES_NAME, Constants.FRIENDREQUEST, false);

		}
		if (newplaylistSubscribers.isChecked()) {
			Utils.storeData(FriendNotificationActivity.this,
					Constants.PREFERENCES_NAME,
					Constants.NEWPLAYLISTSUBSCRIBERS, true);

		} else {
			Utils.storeData(FriendNotificationActivity.this,
					Constants.PREFERENCES_NAME,
					Constants.NEWPLAYLISTSUBSCRIBERS, false);

		}
		if (newplaylist.isChecked()) {
			Utils.storeData(FriendNotificationActivity.this,
					Constants.PREFERENCES_NAME, Constants.NEWPLAYLIST, true);

		} else {
			Utils.storeData(FriendNotificationActivity.this,
					Constants.PREFERENCES_NAME, Constants.NEWPLAYLIST, false);

		}
		if (playlistUpdate.isChecked()) {
			Utils.storeData(FriendNotificationActivity.this,
					Constants.PREFERENCES_NAME, Constants.PLAYLISTUPDATES, true);

		} else {
			Utils.storeData(FriendNotificationActivity.this,
					Constants.PREFERENCES_NAME, Constants.PLAYLISTUPDATES,
					false);

		}

	}

}
