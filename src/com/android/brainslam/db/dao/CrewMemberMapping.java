package com.android.brainslam.db.dao;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class CrewMemberMapping implements Serializable
{
	private static final long serialVersionUID = 1L; 

	@DatabaseField (generatedId = true,allowGeneratedIdInsert = true) 
	public int CrewMemberMappingId;

	@DatabaseField 
	public String joinedDate;

	@DatabaseField 
	public boolean isAdmin;

	@DatabaseField 
	public boolean isOwner;

	@DatabaseField 
	public int crewMemberMappingStatus;

	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	public Crews crewId;
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	public Users userId;
}