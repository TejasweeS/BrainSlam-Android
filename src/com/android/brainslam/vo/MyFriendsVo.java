package com.android.brainslam.vo;



public class MyFriendsVo
{
	public MyFriendsVo(String photo_Url, int user_Id, String user_Name,
			String email_Id, String about_User, String mobile_Number,
			String added_Date) {
		super();
		Photo_Url = photo_Url;
		User_Id = user_Id;
		User_Name = user_Name;
		Email_Id = email_Id;
		About_User = about_User;
		Mobile_Number = mobile_Number;
		Added_Date = added_Date;
	}

	private String User_Name, Email_Id, About_User, Mobile_Number, Added_Date;
	private String Photo_Url;
	private int User_Id; 
	
	public String getUser_Name() {
		return User_Name;
	}
	public void setUser_Name(String user_Name) {
		User_Name = user_Name;
	}
	public String getEmail_Id() {
		return Email_Id;
	}
	public void setEmail_Id(String email_Id) {
		Email_Id = email_Id;
	}
	public String getAbout_User() {
		return About_User;
	}
	public void setAbout_User(String about_User) {
		About_User = about_User;
	}
	public String getMobile_Number() {
		return Mobile_Number;
	}
	public void setMobile_Number(String mobile_Number) {
		Mobile_Number = mobile_Number;
	}
	public String getAdded_Date() {
		return Added_Date;
	}
	public void setAdded_Date(String added_Date) {
		Added_Date = added_Date;
	}
	public String getPhoto_Url() {
		return Photo_Url;
	}
	public void setPhoto_Url(String photo_Url) {
		Photo_Url = photo_Url;
	}
	public int getUser_Id() {
		return User_Id;
	}
	public void setUser_Id(int user_Id) {
		User_Id = user_Id;
	}
}