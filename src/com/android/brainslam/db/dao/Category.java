package com.android.brainslam.db.dao;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable 
public class Category implements Serializable 
{ 
	private static final long serialVersionUID = 1L; 
	private boolean select;

	@DatabaseField (generatedId = true ,allowGeneratedIdInsert = true) 
	public int categoryId; 

	@DatabaseField 
	public boolean isRecommended; 

	@DatabaseField 
	public boolean isOnHomePage; 

	@DatabaseField 
	public int itemSequence; 
	
	@DatabaseField 
	public String categoryName;
	
	@DatabaseField 
	public String thumbnailUrl; 
	
	@DatabaseField (foreign = true, foreignAutoRefresh = true) 
	public Users user;

 public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	} 
}