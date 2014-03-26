package com.android.brainslam.db.dao;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable 
public class CrewRequests implements Serializable
{
	private static final long serialVersionUID = 1L; 

	@DatabaseField (generatedId = true,allowGeneratedIdInsert = true) 
	public int crewRequestId;

	@DatabaseField 
	public String requestedDate;

	@DatabaseField 
	public int crewRequestStatus;

	@DatabaseField 
	public int requestType;

	@DatabaseField 
	public Crews crewId;
	
	@DatabaseField 
	public Users userId;
}
