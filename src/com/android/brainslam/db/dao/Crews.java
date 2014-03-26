package com.android.brainslam.db.dao;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable 
public class Crews implements Serializable
{
	private static final long serialVersionUID = 1L; 

	@DatabaseField (generatedId = true, allowGeneratedIdInsert = true) 
	public int CrewId;

	@DatabaseField 
	public int Crew_Id;

	@DatabaseField 
	public String Crew_Name;

	@DatabaseField 
	public String Mission_Statement;

	/*@DatabaseField 
	public int crewType;*/

	@DatabaseField 
	public String Crew_Logo;

	/*	@DatabaseField 
	public String crewStatus;
	 */
	@DatabaseField
	public String Added_Date;
	@DatabaseField 
	
	public boolean Is_On_Home_Page;
	@DatabaseField
	
	public int Home_Page_Item_Sequence;
}