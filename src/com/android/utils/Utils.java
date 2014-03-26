package com.android.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import com.android.brainslam.constants.Constants;

public class Utils {

	public static SharedPreferences settings1;
	public static SharedPreferences.Editor editor1;

	public final static Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("^[^.][a-zA-Z0-9 .]*@[a-z0-9]+\\.[^.]+[a-z.]+");

	public final static Pattern PASSWORD_PATTERN = Pattern
			.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).*");
	//			("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]))");

	//			.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");

	public static void CopyStream(InputStream is, OutputStream os)
	{
		final int buffer_size=1024;
		try
		{

			byte[] bytes=new byte[buffer_size];
			for(;;)
			{
				//Read byte from input stream

				int count=is.read(bytes, 0, buffer_size);
				if(count==-1)
					break;

				//Write byte from output stream
				os.write(bytes, 0, count);
			}
		}
		catch(Exception ex){}
	}

	public static String printUrl(List<BasicNameValuePair> params)
	{
		StringBuilder url = new StringBuilder();

		for (int i = 0; i < params.size(); i++) 
		{
			BasicNameValuePair nameValuePair = params.get(i);

			if(i != 0)
				url.append("&");
			url.append(nameValuePair.getName());
			url.append("=");
			url.append(nameValuePair.getValue());
		}

		return url.toString();
	}

	public static int getRating(int avgIQScore)
	{
		for (int i = 0; i < Constants.RATINGS_ARR.length; i++) 
		{
			if(avgIQScore > Constants.RATINGS_ARR[i][0] &&
					avgIQScore < Constants.RATINGS_ARR[i][1])
			{
				return (i+1);
			}
		}

		return -1;
	}

	public static boolean searchData(String searchString, String value)
	{
		if (searchString.length() <= value.length())
		{
			return value.trim().toLowerCase().contains(searchString.toLowerCase());
		}
		return false;
	}

	public static void storeData(Context context, String prefsName, String keyName, String value)
	{
		SharedPreferences settings = context.getSharedPreferences(prefsName, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(keyName, value);
		editor.commit();
	}

	public static void storeData(Context context, String prefsName, String keyName, int value)
	{
		SharedPreferences settings = context.getSharedPreferences(prefsName, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(keyName, value);
		editor.commit();
	}

	public static void storeData(Context context, String prefsName, String keyName, boolean value)
	{
		SharedPreferences settings = context.getSharedPreferences(prefsName, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(keyName, value);
		editor.commit();
	}

	public static void storeDatafortwitter(Context context, String prefsName,String keyName1,String value1 ,String keyName2,String value2,String keyName3, boolean value3)
	{
		settings1 = context.getSharedPreferences(prefsName, 0);
		editor1 = settings1.edit();
		editor1.putString(keyName1, value1);
		editor1.putString(keyName2, value2);
		editor1.putBoolean(keyName3, value3);
		editor1.commit();
	}


	public static boolean getDataBoolean(Context context, String prefsName, String keyName)
	{
		SharedPreferences settings = context.getSharedPreferences(prefsName, 0);
		return settings.getBoolean(keyName, false);
	}
	//	public static String[] getDatafortweet(Context context, String prefsName, String keyName1,String keyName2)
	//	{
	//		SharedPreferences settings = context.getSharedPreferences(prefsName, 0);
	//		String arr[]=new String[2];
	//		arr[0]=settings.getString((keyName1, null);
	//		arr[1]=settings.getString((keyName2, null);
	//		
	//		return arr;
	//	}



	public static void Logout()
	{

		editor1 = settings1.edit();
		editor1.remove(com.android.brainslam.constants.Constants.PREF_KEY_OAUTH_TOKEN );
		editor1.remove(com.android.brainslam.constants.Constants.PREF_KEY_OAUTH_SECRET);
		editor1.remove(com.android.brainslam.constants.Constants.PREF_KEY_TWITTER_LOGIN);
		editor1.commit();	
	}

	public static int getDataInt(Context context, String prefsName, String keyName)
	{
		SharedPreferences settings = context.getSharedPreferences(prefsName, 0);
		return settings.getInt(keyName, -1);
	}

	public static String getDataString(Context context, String prefsName, String keyName)
	{
		SharedPreferences settings = context.getSharedPreferences(prefsName, 0);
		return settings.getString(keyName, null);
	}

	public static boolean isValidEmailId(String emailId)
	{
		return(EMAIL_ADDRESS_PATTERN.matcher(emailId).matches());
	}
	public static boolean isValidPassword(String paswd)
	{
		return(PASSWORD_PATTERN.matcher(paswd).matches());
	}


	public static boolean checkConfirmPswd(String pswd1, String pswd2)
	{
		return pswd1.trim().equals(pswd2.trim());
	}

	public static boolean intToBoolean(int response)
	{
		return (response == 0) ? true : false; 
	}
	/*  Function for creating 
	 * */
	public static final String md5(final String password) {
		try {

			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(password.getBytes());
			byte messageDigest[] = digest.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}


	public static String getResponseMessage(int responseCode)
	{
		String msg = "";
		switch(responseCode)
		{

		case 1: 
			msg = Constants.ERROR_CODE_1;
			break;

		case 2:
			msg = Constants.ERROR_CODE_2;
			break;

		case 3:
		case 8:
			msg = Constants.ERROR_CODE_3;
			break;

		case 4:
			msg = Constants.ERROR_CODE_4;
			break;

		case 5:
			msg = Constants.ERROR_CODE_5;
			break;
		case 7:
			msg = Constants.ERROR_CODE_7;
			break;

		}
		return msg;
	}

	public static boolean checkExternalStorageInstalled()
	{
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
			return true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
			return false;
		} else {
			// Something else is wrong. It may be one of many other states, but all we need
			//  to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
			return false;
		}
	}

	public static boolean checkeInternetConnection(Context context)
	{
		final ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		final android.net.NetworkInfo wifi = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		final android.net.NetworkInfo mobile = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo activeConnection =   connMgr
				.getActiveNetworkInfo();

		if ((mobile==null && ! wifi.isAvailable())||(null == activeConnection  || !activeConnection.isConnected())) 
			return false;
		else
			return true;
	}
	public static String getPaddedTime(long value)
	{
		if((value+"").length()==1)
		{
			return "0"+value;
		}
		else
		{
			return ""+value;
		}
	}
}