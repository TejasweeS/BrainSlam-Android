package com.android.brainslam;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class ComposeNewMessageActivity extends Activity {
	Button sendBtn;
	EditText txtphoneNo;
	EditText txtMessage;
	int requestCode=37;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_compose_new_message);

		sendBtn = (Button) findViewById(R.id.btnSendSMS);
		txtphoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
		txtMessage = (EditText) findViewById(R.id.editTextSMS);

		/*
		 * sendBtn.setOnClickListener(new View.OnClickListener() { public void
		 * onClick(View view) { sendSMSMessage(); } });
		 */

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnSendSMS:
			sendSMSMessage();
			break;

		case R.id.editTextPhoneNo:
			System.out.println("Clicked");
			addFriends();
			break;
		default:
			break;
		}
	}

	protected void sendSMSMessage() {
		Log.i("Send SMS", "");

		String phoneNo = txtphoneNo.getText().toString();
		String message = txtMessage.getText().toString();

		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNo, null, message, null, null);
			Toast.makeText(getApplicationContext(), "SMS sent.",
					Toast.LENGTH_LONG).show();
			txtphoneNo.setText("");
			txtMessage.setText("");

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"SMS faild, please try again.", Toast.LENGTH_LONG).show();
			e.printStackTrace();

		}
	}

	protected void addFriends() {

		Toast.makeText(ComposeNewMessageActivity.this,
				"Add Friends to send message", Toast.LENGTH_SHORT).show();

//		Intent addFriendsIntent = new Intent(ComposeNewMessageActivity.this,
//				AddOrDeleteFriendsActivity.class);
//
//		startActivityForResult(addFriendsIntent, requestCode);
	}

}