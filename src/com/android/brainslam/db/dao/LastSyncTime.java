package com.android.brainslam.db.dao;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable 
public class LastSyncTime implements Serializable
{
	private static final long serialVersionUID = 1L; 

	@DatabaseField (generatedId = true,allowGeneratedIdInsert = true) 
	public int lastSyncTimeId;

	@DatabaseField 
	public String keyName;

	@DatabaseField 
	public String keyValue;
}