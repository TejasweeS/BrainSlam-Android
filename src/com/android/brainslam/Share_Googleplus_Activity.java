
package com.android.brainslam;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.plus.PlusShare;

public class Share_Googleplus_Activity extends Activity implements  View.OnClickListener,DialogInterface.OnCancelListener
{
	private EditText mEditSendText;
	protected static final String TAG = "ShareActivity";
	private static final int REQ_SELECT_PHOTO = 10;
	private static final int REQ_START_SHARE = 20;
	private static final int DIALOG_GET_GOOGLE_PLAY_SERVICES = 1;
	private static final int REQUEST_CODE_INTERACTIVE_POST = 1;
	private static final int REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES = 2;
	
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.share_googleplus);
Button sendButton = (Button) findViewById(R.id.send_interactive_button);
sendButton.setOnClickListener(this);
Button sendmedia = (Button) findViewById(R.id.buttonmedia);
sendmedia.setOnClickListener(this);

mEditSendText = (EditText) findViewById(R.id.share_prefill_edit);

String msg = getIntent().getExtras().getString("share_msg");
mEditSendText.setText(msg);

int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	if (available != ConnectionResult.SUCCESS)
	{
		showDialog(DIALOG_GET_GOOGLE_PLAY_SERVICES);
	}
}


@Override
protected Dialog onCreateDialog(int id)
{
if (id != DIALOG_GET_GOOGLE_PLAY_SERVICES)
{
    return super.onCreateDialog(id);
}

int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
if (available == ConnectionResult.SUCCESS) {
    return null;
}
if (GooglePlayServicesUtil.isUserRecoverableError(available)) {
    return GooglePlayServicesUtil.getErrorDialog(
            available, this, REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES, this);
}
return new AlertDialog.Builder(this)
        .setMessage("Sign in with Google is not available")  // R.string.plus_generic_error)
        .setCancelable(true)
        .setOnCancelListener(this)
        .create();
}


public void onClick(View view) {
switch (view.getId()) {
    case R.id.send_interactive_button:
    	
    //	getInteractivePostIntent();  //  without img
        
    	 PlusShare.Builder share = new PlusShare.Builder(this);
	      share.setText(mEditSendText.getText());
	      share.setType("image/jpeg");
	                share.setContentDeepLinkId("/cheesecake/lemon", //** Deep-link identifier *//*
	                "Lemon Cheesecake recipe", //** Snippet title *//*
	                "A tasty recipe for making lemon cheesecake.",//Uri.parse(picturePath)/** Snippet description 
	                Uri.parse("http://androidexample.com/media/webservice/LazyListView_images/image2.png")
	                )
	        .getIntent();
	      startActivityForResult(share.getIntent(), REQ_START_SHARE);
         
        break;
    case R.id.buttonmedia:
    	 boolean installed  =   appInstalledOrNot("com.google.android.apps.plus");  
      if(installed)
{
//    	 AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
//     	 Account[] list = manager.getAccounts();
//     	 String gmail = null;
//
//     	 for(Account account: list)
//     	 {
//     	     if(account.type.equalsIgnoreCase("com.google"))
//     	     {
//     	         gmail = account.name;
//     	         Log.i("XXXXXX==>>",""+gmail);
//     	         break;
//     	     }
//     	 }
        	 
    	 Intent photoPicker = new Intent(Intent.ACTION_PICK);
		 photoPicker.setType("video/*, image/*");
         startActivityForResult(photoPicker, REQ_SELECT_PHOTO);
}else
{
Toast.makeText(this,"Google+ is not installed in your phone,Please install it" ,Toast.LENGTH_SHORT).show();	
}
	
        return;
}


}
protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
switch (requestCode) {
    case REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES:
        if (resultCode != RESULT_OK) {
            Log.e(TAG, "Unable to sign the user in.");
            finish();
        }
        break;

    case REQUEST_CODE_INTERACTIVE_POST :
        if (resultCode != RESULT_OK) {
            Log.e(TAG, "Failed to create interactive post");
        }else{ 
        	
        
        }
        break;
    
    case REQ_SELECT_PHOTO:
           if(resultCode == RESULT_OK)
           {
        		
//				String[] filePathColumn = { MediaStore.Images.Media.DATA };
//				Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
//				cursor.moveToFirst();
//				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//				String picturePath = cursor.getString(columnIndex);
//				Log.i("path------>", "" + picturePath);
//				cursor.close();
//				File ff = new File(picturePath);
//		    	
//		   // 	File tmpFile = new File("/path/to/image");
//		    	 String photoUri = null;
//				try {
//					photoUri = MediaStore.Images.Media.insertImage(
//					         getContentResolver(), ff.getAbsolutePath(), null, null);
//				//	Log.i("photoUri==>",""+photoUri);
//				} catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
        	  
        	   Log.i("picked--", "img");
        	   Uri selectedImage = intent.getData();
   	    	   ContentResolver cr = this.getContentResolver();
   		       String mime = cr.getType(selectedImage);
   		       PlusShare.Builder share = new PlusShare.Builder(this);
   		       share.setText(mEditSendText.getText());
   		       share.addStream(selectedImage);
   		       share.setType(mime);
   		       startActivityForResult(share.getIntent(), REQ_START_SHARE);

           }
       
    break;
	case REQ_START_SHARE:
    if(resultCode == RESULT_OK)
    {
    	mEditSendText.setText("");	
    	
    }else
    {
    	Log.i("XXXXXXXXXXX","else--XXXXXXXXXXXXXXXXXXX");	
    }
}
}

private boolean appInstalledOrNot(String uri) {
    PackageManager pm = getPackageManager();
    boolean app_installed = false;
    try {
        pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
        app_installed = true;
    }
    catch (PackageManager.NameNotFoundException e) {
        app_installed = false;
    }
    return app_installed ;
}



@Override
public void onCancel(DialogInterface arg0) {
	// ---------------------------------------------------------------------------

	
}
	
}