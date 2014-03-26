package com.android.brainslam.NetworkHandler;

import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.utils.CustomAlertDialog;
import com.android.utils.Utils;

public class AsyncHandlerSansSpinner extends AsyncTask<Void, Integer, JSONObject> 
{

//	CustomSpinnerDialog prog;
	//	ProgressDialog prog;
//	public Dialog loadingDialog;
	
	final Context context;
	final String url;
	int id = 0;
	boolean isGet;
	List<BasicNameValuePair> postParameters;

	
	
	public AsyncHandlerSansSpinner(final Context context, final String url, int id,
			boolean isGet, List<BasicNameValuePair> postParameters) 
	{
		this.context = context;
		this.url = url;
		this.id = id;
		this.isGet = isGet;
		this.postParameters = postParameters; 
		Log.d("mayur", "in AsyncHandler");
	}

	@Override
	protected void onPreExecute() 
	{
		super.onPreExecute();
		
		if (!Utils.checkeInternetConnection(context)) {
			Log.e("network error","No Internet Connection");
			new CustomAlertDialog(context,"No Internet Connection").showDialog();
			cancel(true);
		}
		else
		{
//			loadingDialog.show();
		}
		Log.d("mayur", "in pre execute");
	}

	@Override
	protected JSONObject doInBackground(final Void... params) 
	{
		Log.v("mayur","do in background: "+url);
		JSONObject jsonResp = HttpCommunicator.callRsJson(url,isGet,postParameters,context);
		return jsonResp;
	}

	@Override
	protected void onPostExecute(final JSONObject jsonObject) 
	{
		super.onPostExecute(jsonObject);

		Log.d("mayur", "in post execute ");

		AsyncCallBack asyn = (AsyncCallBack) context;
//		loadingDialog.dismiss();
		asyn.onReceive(jsonObject, id);
	}
}