package com.android.brainslam.NetworkHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.android.utils.Utils;

public class HttpCommunicator 
{
	public final static int TIMEOUT_DURATION = 60000;

	public static JSONObject callRsJson(final String url,boolean isGet ,List<BasicNameValuePair> postParameters,Context context) 
	{
		JSONObject retVal = null;

		Log.d("mayur", " url::" + url+Utils.printUrl(postParameters));
		JSONObject erroJson = null;
		try {
			
//			final ConnectivityManager connMgr = (ConnectivityManager) context
//					.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//			final android.net.NetworkInfo wifi = connMgr
//					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//
//			final android.net.NetworkInfo mobile = connMgr
//					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//				 NetworkInfo activeConnection =   connMgr
//							.getActiveNetworkInfo();
//			
//			if ((mobile==null && ! wifi.isAvailable())||(null == activeConnection  || !activeConnection.isConnected())) {
//				// Do something
////				Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
//				
//				Log.e("network error","No Internet Connection");
//				
//				return new JSONObject(
//						"{\"errMessage\": \"No Internet Connection\"}");
//				
//			}
//			else
			{
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, TIMEOUT_DURATION);
			HttpConnectionParams.setSoTimeout(params, TIMEOUT_DURATION);
			erroJson = new JSONObject(
					"{\"errMessage\": \"Server Error, Please try again error\"}");

			HttpResponse response;
			
			if(isGet)
				response = httpClient.execute(new HttpGet(url));
			else
			{
				HttpPost postRequest = new HttpPost(url);
				
//				StringEntity input = new StringEntity(postRequest.toString());
				
//			input.setContentType("application/text");
				postRequest.setEntity(new UrlEncodedFormEntity(postParameters));
//				postRequest.setEntity(input);
				
				response = httpClient.execute(postRequest);
			}
				

			Log.d("Http Respone Code::", ""
					+ response.getStatusLine().getStatusCode());

			// TODO Auto-generated catch block
			if (response.getStatusLine().getStatusCode() != 200) {
				// throw new Exception("Failed : HTTP error code : "
				// + response.getStatusLine().getStatusCode());
				return new JSONObject(
						"{\"errMessage\": \"Server Error, Please try again !\"}");
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = br.readLine()) != null) {
				sb.append(line + NL);
			}

			System.out.println("OUTPUT::"+sb.toString());
			Log.d("OUTPUT::", sb.toString());

			retVal = new JSONObject(sb.toString());

			httpClient.getConnectionManager().shutdown();
			}
		} catch (ConnectTimeoutException e) {

			Log.e("mayur", "ConnectTimeoutException::" + e.toString());
			e.printStackTrace();
			return erroJson;

		} catch (MalformedURLException e) {

			e.printStackTrace();
			Log.v("mayur", "MalformedURLException");
			return erroJson;

		} catch (IOException e) {
			Log.v("mayur", "IOException");
			e.printStackTrace();
			return erroJson;

		} catch (JSONException e) {
			Log.v("mayur", "JSONException");

			e.printStackTrace();
			return erroJson;

		} catch (Exception e) {
			Log.v("mayur", "Exception");

			e.printStackTrace();
			return erroJson;
		}

//		Log.d("mayur", "JSON resp ::" + retVal);

		return retVal;
	}

	public static String converJsonToStringFromAssetFolder(
			final String fileName, final Context context) throws IOException {
		AssetManager manager = context.getAssets();
		InputStream file = manager.open(fileName);

		byte[] data = new byte[file.available()];
		file.read(data);
		file.close();
		return new String(data);
	}
}