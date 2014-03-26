package com.android.brainslam.db.dao;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable 
public class FeaturedMedia implements Serializable
{
	private static final long serialVersionUID = 1L; 

	@DatabaseField (generatedId = true,allowGeneratedIdInsert = true) 
	public String Media_Id;

	@DatabaseField 
	public int Media_Type;

	@DatabaseField 
	public String Data_Url;

	@DatabaseField 
	public long Created_At;
	
	@DatabaseField 
	public long Updated_At;
	
	@DatabaseField 
	public int Views;
	
	@DatabaseField 
	public int Width;
	
	@DatabaseField 
	public int Height;
	
	@DatabaseField 
	public int Duration;
	
	@DatabaseField 
	public String Name;
	
	@DatabaseField 
	public String Description;
	
	@DatabaseField 
	public String Tags;
	
	@DatabaseField 
	public String Categories;
	
	@DatabaseField 
	public int Rank;
	
	@DatabaseField 
	public String Download_Url;
	
	@DatabaseField 
	public String Thumbnail_Url;
	
	@DatabaseField 
	public int Tranding_Score;
	
	@DatabaseField 
	public int Average_IQ;
	
	@DatabaseField 
	public int Number_Of_Ratings;
}
