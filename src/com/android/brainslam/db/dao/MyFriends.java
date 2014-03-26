package com.android.brainslam.db.dao;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable 
public class MyFriends implements Serializable
{
	private static final long serialVersionUID = 1L; 

	@DatabaseField (generatedId = true,allowGeneratedIdInsert = true) 
	public int userId;

	@DatabaseField 
	public String name;

	@DatabaseField 
	public String emailId;

	/*@DatabaseField 
	public String facebookUID;

	@DatabaseField 
	public boolean password;
	
	@DatabaseField 
	public int userStatus;
*/
	@DatabaseField 
	public String mobileNumber;

	@DatabaseField 
	public String photoUrl;

	@DatabaseField 
	public String aboutUser;

	@DatabaseField 
	public String addedDate;

	@DatabaseField
	public long lastModified;
	
	@DatabaseField
	public boolean isOnHomeScreen;
	
	/*@DatabaseField 
	public String appLoginSession;

	@DatabaseField 
	public String lastLogin;

	@DatabaseField 
	public boolean isLoggedIn;*/

	
}