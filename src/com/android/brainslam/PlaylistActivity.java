package com.android.brainslam;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class PlaylistActivity extends Activity {

	EditText searchEt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playlist__option);

		searchEt = (EditText) findViewById(R.id.searchbar);
		searchEt.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//get the text in the EditText
				String searchString=searchEt.getText().toString();
				int textLength=searchString.length();
//				searchResults.clear();
//				
//				for(int i=0;i<originalValues.size();i++)
//				{
//					String playerName=originalValues.get(i).get("name").toString();
//						//compare the String in EditText with Names in the ArrayList
//					if(Utils.searchData(searchString, playerName)){
//							searchResults.add(originalValues.get(i));
//					}
//				}
//				adapter.notifyDataSetChanged();
			
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			public void afterTextChanged(Editable s) {
				
				
			}
		});
	}
	
	
	public void backclick(View v)
	{
		PlaylistActivity.this.finish();
	   //startActivity(new Intent(this,MainPlaylistActivity.class));	
	
	}
}
