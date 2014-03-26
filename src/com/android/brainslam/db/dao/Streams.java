package com.android.brainslam.db.dao;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable 
public class Streams implements Serializable 
{ 
	private static final long serialVersionUID = 1L; 

	@DatabaseField 
	public int HomeItemID; 

	@DatabaseField (generatedId = true, allowGeneratedIdInsert = true)
	public int _id; 
	
	@DatabaseField 
	public int Count; 

	@DatabaseField 
	public String HomeItemsName;

	@DatabaseField 
	public int HomeItemSequence;

	@DatabaseField 
	public int UserId; 

	@DatabaseField 
	public String HomeItemType; 

	@DatabaseField 
	public String LastModified; 

	@DatabaseField 
	public String HomeItemsStatus; 

	@DatabaseField 
	public String UserHomeItemId; 
}
