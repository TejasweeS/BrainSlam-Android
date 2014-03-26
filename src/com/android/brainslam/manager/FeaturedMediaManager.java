package com.android.brainslam.manager;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.android.brainslam.db.DataBaseHelper;
import com.android.brainslam.db.dao.FeaturedMedia;
import com.android.brainslam.vo.FeaturedMediaList;
import com.android.brainslam.vo.FeaturedMediaListData;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

public class FeaturedMediaManager 
{
	private DataBaseHelper dataBaseHelper;
	private static FeaturedMediaManager instance ;

	public static FeaturedMediaManager getInstance(Context context)
	{
		if(instance==null)
		instance = new FeaturedMediaManager(context);
		return instance;
	}

	private FeaturedMediaManager(Context context) 
	{
		dataBaseHelper = DataBaseHelper.getInstance(context);
	}

	public List<FeaturedMediaListData> getFeaturedMedia()
	{
		Dao dao;
		try {
			dao = dataBaseHelper.getGenericDao(FeaturedMedia.class);
			QueryBuilder qB = dao.queryBuilder();

			List<FeaturedMediaListData> mediaList = dao.query(qB.prepare());
			return mediaList;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void saveFeaturedMedia(FeaturedMediaList mediaList)
	{
		if(mediaList != null)
		{
			for (int i = 0; i < mediaList.data.length; i++) 
			{
				FeaturedMedia fMediaDao = new FeaturedMedia();
				
				fMediaDao.Description = mediaList.data[i].Description;
				fMediaDao.Media_Id = mediaList.data[i].Media_Id;
				fMediaDao.Height = mediaList.data[i].Height;
				fMediaDao.Width = mediaList.data[i].Width;
				fMediaDao.Categories = mediaList.data[i].Categories;
				fMediaDao.Tranding_Score = mediaList.data[i].Tranding_Score;
				fMediaDao.Created_At = mediaList.data[i].Created_At;
				fMediaDao.Updated_At = mediaList.data[i].Updated_At;
				fMediaDao.Name = mediaList.data[i].Name;
				fMediaDao.Tags = mediaList.data[i].Tags;
				fMediaDao.Average_IQ = mediaList.data[i].Average_IQ;
				fMediaDao.Thumbnail_Url = mediaList.data[i].Thumbnail_Url;
				fMediaDao.Views = mediaList.data[i].Views;
				fMediaDao.Duration = mediaList.data[i].Duration;
				fMediaDao.Number_Of_Ratings = mediaList.data[i].Number_Of_Ratings;
				fMediaDao.Download_Url = mediaList.data[i].Download_Url;
				fMediaDao.Data_Url = mediaList.data[i].Data_Url;
				fMediaDao.Media_Type = mediaList.data[i].Media_Type;
				
				try {
					dataBaseHelper.getGenericDao(
							FeaturedMedia.class)
							.createOrUpdate(fMediaDao);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
