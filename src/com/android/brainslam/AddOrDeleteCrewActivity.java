package com.android.brainslam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.brainslam.NetworkHandler.AsyncCallBack;
import com.android.brainslam.NetworkHandler.AsyncHandler;
import com.android.brainslam.constants.Constants;
import com.android.brainslam.db.dao.Crews;
import com.android.brainslam.mainscreen.BrainSlamToggleMenu;
import com.android.brainslam.mainscreen.MainScreen_CategoryActivity;
import com.android.brainslam.manager.CategoryManager;
import com.android.brainslam.manager.CrewManager;
import com.android.brainslam.vo.CrewListData;
import com.android.listdapters.CreateCrewAdapter;
import com.android.utils.Utils;
import com.google.gson.Gson;

public class AddOrDeleteCrewActivity extends Activity implements AsyncCallBack {

	protected static final int ADDNEWCREW = 1;
	private static final int GETCREWLIST = 0;
		String screteKey, userId;
	Set<CrewListData> crewListForselection=new HashSet<CrewListData>();
	public CreateCrewAdapter adapter;
	ListView crewListview;
	boolean isTypeDelete;

	EditText newCrewText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createnewcrew);
		isTypeDelete=getIntent().getExtras().getBoolean("Type");
		TextView newcrew = (TextView)findViewById(R.id.createnewcrew_New);
		EditText seacrhbar=(EditText)findViewById(R.id.createnewcrew_Search);
     	screteKey = Utils.getDataString(AddOrDeleteCrewActivity.this,
				Constants.PREFS_NAME, Constants.SP_SECRET_KEY);
		userId = Utils.getDataString(AddOrDeleteCrewActivity.this,
				Constants.PREFS_NAME, Constants.SP_USER_ID);

		crewListview = (ListView) findViewById(R.id.createnewcrew_ListView);
		if(isTypeDelete)
		{
			newcrew.setText("New");
		}
		else
		{
			newcrew.setText("Done");

		}
		fetchCrewList();
		seacrhbar.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				for (CrewListData crewListData : crewDataList) {
					if(crewListData.Crew_Name.contains(arg0.toString()))
					{
						crewListData.isVisible=true;
					}
					else
						crewListData.isVisible=false;
				}
				adapter.notifyDataSetChanged();
			}
		});
	}
	public void onClick(View view)
	{
		System.out.println("clciked :: "+crewListForselection.toArray());
		Intent returnIntent =null;
		switch (view.getId()) {
		case R.id.createnewcrew_New:
			    final Dialog addCrewDialog = new Dialog(this);
			    addCrewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	            addCrewDialog.setContentView(R.layout.createnewcrewdialogbox);
	             addCrewDialog.show();
	            final EditText newCrewEditText = (EditText) addCrewDialog.findViewById(R.id.newcrewdialogtextedit);
	            Button okButton = (Button) addCrewDialog.findViewById(R.id.ok);
	            okButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
		             addCrewDialog.dismiss();
		             List<BasicNameValuePair> postparam = new ArrayList<BasicNameValuePair>();
                     postparam.add(new BasicNameValuePair("Secret_Key",screteKey));
						postparam.add(new BasicNameValuePair("User_Id",userId));
						postparam.add(new BasicNameValuePair("Crew_Name",newCrewEditText.getText().toString().trim()));
				        postparam.add(new BasicNameValuePair("Crew_Logo",""));
				        postparam.add(new BasicNameValuePair("Crew_Logo_Extension",""));
						postparam.add(new BasicNameValuePair("operation", "addNewCrew"));
						new AsyncHandler(AddOrDeleteCrewActivity.this, Constants.SERVER_URL+"crew.php?",
								ADDNEWCREW, false,postparam).execute();
						
					   
		             
					}
				});
	            Button cancelButton = (Button) addCrewDialog.findViewById(R.id.cancel);
	            cancelButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
		             addCrewDialog.dismiss();	
					}
				});
	        /*ArrayList<CrewListData> arrayList=new ArrayList<CrewListData>();
			for (Iterator iterator = crewListForselection.iterator(); iterator.hasNext();) {
				arrayList.add((CrewListData) iterator.next());
			}
			
			returnIntent = new Intent();
			returnIntent.putExtra("data",arrayList);
			
			setResult(RESULT_OK, returnIntent);*/
			
		
			break;
		case R.id.createnewcrew_Cancel:
          
			/*returnIntent = new Intent();
			setResult(RESULT_CANCELED, returnIntent);        
			finish();*/
			break;


		default:
			break;
		}
	}

	private void fetchCrewList() {
		// TODO Auto-generated method stub
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("operation", "getAllCrewList"));
		params.add(new BasicNameValuePair("Secret_Key", screteKey));
		params.add(new BasicNameValuePair("User_Id", userId));
		// params.add(new BasicNameValuePair("search_Value", userId));
		// params.add(new BasicNameValuePair("Last_Modified", userId));
		params.add(new BasicNameValuePair("pageSize", 5 + ""));
		params.add(new BasicNameValuePair("pageIndex", 1 + ""));

		new AsyncHandler(AddOrDeleteCrewActivity.this, Constants.SERVER_URL
				+ "crew.php?",GETCREWLIST, false, params).execute();
	}

	List<CrewListData> crewDataList = new ArrayList<CrewListData>();

	@Override
	public void onReceive(JSONObject jsonObj, final int id) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		switch (id) {
		case GETCREWLIST:
			Log.d("Crew", "Json String" + jsonObj.toString());
			  try {
						JSONObject crewData = new JSONObject(jsonObj.toString());
						@SuppressWarnings("unused")
					   
						org.json.JSONArray jsonCrewArray = crewData.getJSONArray("data");
						CrewListData[] crewDataArr = gson.fromJson(
								jsonCrewArray.toString(), CrewListData[].class);
						for (int i = 0; i < crewDataArr.length; i++) {
							CrewListData crewlistdata = new CrewListData();
							crewlistdata.Crew_Id = crewDataArr[i].Crew_Id;
							crewlistdata.Crew_Name = crewDataArr[i].Crew_Name;
							crewlistdata.Crew_Logo = crewDataArr[i].Crew_Logo;
							crewlistdata.Added_Date = crewDataArr[i].Added_Date;
							crewlistdata.Mission_Statement = crewDataArr[i].Mission_Statement;
						    crewlistdata.Is_On_Home_Page = crewDataArr[i].Is_On_Home_Page;
							crewDataList.add(crewlistdata);
							
						}
						List <Crews> crewDaoList = new ArrayList<Crews>();

						for (int i = 0; i < crewDataArr.length; i++) {
							
							Crews crewlisDaoObect = new Crews();
							crewlisDaoObect.Crew_Id = crewDataArr[i].Crew_Id;
							crewlisDaoObect.Crew_Name = crewDataArr[i].Crew_Name;
							crewlisDaoObect.Crew_Logo = crewDataArr[i].Crew_Logo;
							crewlisDaoObect.Added_Date = crewDataArr[i].Added_Date;
							crewlisDaoObect.Mission_Statement = crewDataArr[i].Mission_Statement;
							crewlisDaoObect.Is_On_Home_Page = crewDataArr[i].Is_On_Home_Page == 1 ? true : false;
							crewDaoList.add(crewlisDaoObect);
						}
						
						CrewManager.getInstance(getApplicationContext()).saveCrews(crewDaoList);
						adapter = new CreateCrewAdapter(AddOrDeleteCrewActivity.this,
								crewDataList,isTypeDelete);
						crewListview.setAdapter(adapter);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
			break;
		case ADDNEWCREW:
			try {
				if (jsonObj.has("status") && jsonObj.getInt("status") == 0
						&& jsonObj.getInt("message") == 6) {
					/*Toast.makeText(AddOrDeleteCrewActivity.this, "Successfully update",
							Toast.LENGTH_LONG).show();*/
					JSONObject crewData = new JSONObject(jsonObj.toString());
					@SuppressWarnings("unused")
				   
					org.json.JSONArray jsonCrewArray = crewData.getJSONArray("data");
					CrewListData[] crewDataArr = gson.fromJson(
							jsonCrewArray.toString(), CrewListData[].class);
					for (int i = 0; i < crewDataArr.length; i++) {
						CrewListData crewlistdata = new CrewListData();
						crewlistdata.Crew_Id = crewDataArr[i].Crew_Id;
						crewlistdata.Crew_Name = crewDataArr[i].Crew_Name;
						crewlistdata.Crew_Logo = crewDataArr[i].Crew_Logo;
						crewlistdata.Added_Date = crewDataArr[i].Added_Date;
						crewlistdata.Mission_Statement = crewDataArr[i].Mission_Statement;
					    crewlistdata.Is_On_Home_Page = crewDataArr[i].Is_On_Home_Page;
						crewDataList.add(crewlistdata);
						
					}
				    
					List <Crews> crewDaoList = new ArrayList<Crews>();
					for (int i = 0; i < crewDataArr.length; i++) {
						
						Crews crewlisDaoObect = new Crews();
						crewlisDaoObect.Crew_Id = crewDataArr[i].Crew_Id;
						crewlisDaoObect.Crew_Name = crewDataArr[i].Crew_Name;
						crewlisDaoObect.Crew_Logo = crewDataArr[i].Crew_Logo;
						crewlisDaoObect.Added_Date = crewDataArr[i].Added_Date;
						crewlisDaoObect.Mission_Statement = crewDataArr[i].Mission_Statement;
						crewlisDaoObect.Is_On_Home_Page = crewDataArr[i].Is_On_Home_Page == 1 ? true : false;
						crewDaoList.add(crewlisDaoObect);
						
					}
					
					CrewManager.getInstance(getApplicationContext()).saveCrews(crewDaoList);
					adapter = new CreateCrewAdapter(AddOrDeleteCrewActivity.this,
							crewDataList,isTypeDelete);
					crewListview.setAdapter(adapter);
					//adapter.notifyDataSetChanged();
					
					
				   } else if (jsonObj.has("status") && jsonObj.getInt("status") == 1
						&& jsonObj.getInt("message") == 1) {

					String msg = Utils
					.getResponseMessage(jsonObj.getInt("message"));
					Toast.makeText(AddOrDeleteCrewActivity.this, msg, Toast.LENGTH_LONG)
					.show();

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		default:
			break;
		}
		

	}
	public void CrewSelectedAddition(View view)
	{
		System.out.println("Selected");
		if(((CheckBox)view).isChecked())
			crewListForselection.add((CrewListData)view.getTag());
		else
			crewListForselection.add((CrewListData)view.getTag());
	}
	public void CrewSelectedDeletion(View view)
	{
		crewListForselection.add((CrewListData)view.getTag());
		crewDataList.remove((CrewListData)view.getTag());
		adapter.notifyDataSetChanged();
		
	}

}
