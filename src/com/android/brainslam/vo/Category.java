package com.android.brainslam.vo;

public class Category  
{ 

	public Category(int category_Id, String category_Name, long created_At,
			long updated_At, String description, String tags, int entriesCount,
			int is_Recommended, int is_On_Home_Page,
			String thumbnail_Url) {
		super();
		Category_Id = category_Id;
		Category_Name = category_Name;
		Created_At = created_At;
		Updated_At = updated_At;
		Description = description;
		this.tags = tags;
		EntriesCount = entriesCount;
		Is_Recommended = is_Recommended;
		Is_On_Home_Page = is_On_Home_Page;
		Thumbnail_Url = thumbnail_Url;
	}
	public int getIs_Recommended() {
		return Is_Recommended;
	}
	public void setIs_Recommended(int is_Recommended) {
		Is_Recommended = is_Recommended;
	}
	public int getIs_On_Home_Page() {
		return Is_On_Home_Page;
	}
	public void setIs_On_Home_Page(int is_On_Home_Page) {
		Is_On_Home_Page = is_On_Home_Page;
	}
	public int getCategory_Id() {
		return Category_Id;
	}
	public void setCategory_Id(int category_Id) {
		Category_Id = category_Id;
	}
	public String getCategory_Name() {
		return Category_Name;
	}
	public void setCategory_Name(String category_Name) {
		Category_Name = category_Name;
	}
	public long getCreated_At() {
		return Created_At;
	}
	public void setCreated_At(long created_At) {
		Created_At = created_At;
	}
	public long getUpdated_At() {
		return Updated_At;
	}
	public void setUpdated_At(long updated_At) {
		Updated_At = updated_At;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public int getEntriesCount() {
		return EntriesCount;
	}
	public void setEntriesCount(int entriesCount) {
		EntriesCount = entriesCount;
	}
	public String getThumbnail_Url() {
		return Thumbnail_Url;
	}
	public void setThumbnail_Url(String thumbnail_Url) {
		Thumbnail_Url = thumbnail_Url;
	}
	public int getItemSequence() {
		return itemSequence;
	}
	public void setItemSequence(int itemSequence) {
		this.itemSequence = itemSequence;
	}


	private int Category_Id; 
	private String Category_Name; 
	private long Created_At;
	private long Updated_At;
	private String Description;
	private String tags;
	private int EntriesCount;
	private int Is_Recommended;
	private int itemSequence;
	private int Is_On_Home_Page;
	private String Thumbnail_Url;


	public int isRecommended; 
}