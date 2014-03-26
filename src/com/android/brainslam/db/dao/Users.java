package com.android.brainslam.db.dao;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable 
public class Users implements Serializable
{
	private static final long serialVersionUID = 1L; 

	@DatabaseField (generatedId = true,allowGeneratedIdInsert = true) 
	public int userId;

	@DatabaseField 
	public String name;

	@DatabaseField 
	public String emailId;

	@DatabaseField 
	public String facebookUID;

	@DatabaseField 
	public String password;

	@DatabaseField 
	public String mobileNumber;

	@DatabaseField 
	public String photoUrl;

	@DatabaseField 
	public String aboutUser;

	@DatabaseField 
	public String addedDate;

	@DatabaseField 
	public String appLoginSession;

	@DatabaseField 
	public String lastLogin;

	@DatabaseField 
	public boolean isLoggedIn;

	@DatabaseField 
	public int userStatus;
}
