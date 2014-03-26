
package com.android.brainslam.manager;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.android.brainslam.db.DataBaseHelper;
import com.android.brainslam.db.dao.Category;
import com.android.brainslam.db.dao.Streams;
import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class HomeStreamManager
{
	private DataBaseHelper dataBaseHelper;
	private static  HomeStreamManager instance;

	public static HomeStreamManager getInstance(Context context)
	{
		if (instance == null)
			instance = new HomeStreamManager(context);

		return instance;
	}
	private HomeStreamManager(Context context) 
	{
		dataBaseHelper = DataBaseHelper.getInstance(context);
	}

	public List<Streams> getHomeStream()
	{
		Dao dao;
		try {
			dao = dataBaseHelper.getGenericDao(com.android.brainslam.db.dao.Streams.class);
			QueryBuilder qB = dao.queryBuilder();

			Where wh = qB.where();
			wh.isNotNull("HomeItemsName");

			qB.orderBy("HomeItemSequence", true);

			List<Streams> results = dao.query(qB.prepare());			

			return results;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getStreamName(String homeItemId)
	{
		Dao dao;
		List<Streams> results = null;
		try 
		{
			dao = dataBaseHelper.getGenericDao(com.android.brainslam.db.dao.Streams.class);
			QueryBuilder qB = dao.queryBuilder();

			Where wh = qB.where();
			wh.eq("HomeItemID", homeItemId);

			results = dao.query(qB.prepare());			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}

		if(results != null && results.size() > 0)
			return results.get(0).HomeItemsName;
		else
			return "";
	}

	public Cursor getHomeStreamCursor()
	{
		Dao dao;
		try {
			dao = dataBaseHelper.getGenericDao(com.android.brainslam.db.dao.Streams.class);
			QueryBuilder qB = dao.queryBuilder();

			Where wh = qB.where();
			wh.isNotNull("HomeItemsName");

			qB.orderBy("HomeItemSequence", true);

			//			List<Streams> stream = dao.query(qB.prepare());

			CloseableIterator<Streams> iterator = dao.iterator(qB.prepare());
			try 
			{
				// get the raw results which can be cast under Android
				AndroidDatabaseResults results =
					(AndroidDatabaseResults)iterator.getRawResults();
				Cursor cursor = results.getRawCursor();
				return cursor;
			}
			finally 
			{
				//			   iterator.closeQuietly();
			}

		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	public void saveStreams(List<Streams> stream)
	{
		Log.d("ESA", "saving home stream size::"+stream.size());

		List<Streams> list = getHomeStream();

		try
		{
			dataBaseHelper.getGenericDao(com.android.brainslam.db.dao.Streams.class).
			delete(list);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}

		if(stream != null && stream.size() > 0)
		{
			for (int i = 0; i < stream.size(); i++) 
			{
				try
				{
					dataBaseHelper.getGenericDao(com.android.brainslam.db.dao.Streams.class).
					createOrUpdate(stream.get(i));
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public void deleteLastItem()
	{
		Log.v("HSM", "****************deleteLastItem*********************");

		Dao dao;
		try 
		{
			dao = dataBaseHelper.getGenericDao(com.android.brainslam.db.dao.Streams.class);
			QueryBuilder qB = dao.queryBuilder();

			Where wh = qB.where();
			wh.isNotNull("HomeItemsName");

			qB.orderBy("HomeItemSequence", true);

			List<Streams> results = dao.query(qB.prepare());			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

	public void removeStream(int id)
	{
		Log.v("HSM", "****************removeStream*********************");

		//		Log.d("HSM", "get home stream b4::"+getHomeStream().size());

		Dao dao;
		try 
		{
			dataBaseHelper.getGenericDao(Streams.class)
			.deleteById(id);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		//		Log.d("HSM", "get home stream ftr::"+getHomeStream().size());
	}


	public void addStream(Streams streams)
	{
		try
		{
			dataBaseHelper.getGenericDao(com.android.brainslam.db.dao.Streams.class).
			createOrUpdate(streams);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void updateHomeStream(Category category, boolean isOnHomeScreen)
	{
		Log.v("HSM", "****************updateHomeStream*********************");

		Dao  dao;
		try {

			if(isOnHomeScreen)
			{
				Streams stream = new Streams();

				stream.HomeItemID = category.categoryId;
				stream.HomeItemsName = category.categoryName;
				stream.UserId = category.user.userId;
				stream.HomeItemType = "Category";
				stream.LastModified = "";
				stream.HomeItemsStatus = "Active";
				stream.UserHomeItemId = "";

				addStream(stream);
			}
			else 
			{
				dao = dataBaseHelper.getGenericDao(com.android.brainslam.db.dao.Streams.class);
				QueryBuilder qB = dao.queryBuilder();
				Where whr = qB.where();
				whr.eq("HomeItemID", category.categoryId);

				List<Streams> streams = dao.query(whr.prepare());

				if(streams.size() > 0)
					removeStreamByItemId(category.categoryId);
			}


			//			List<Streams> st = getHomeStream();
			//
			//			for (int i = 0; i < st.size(); i++) 
			//			{
			//				Log.d("HSM", "streams updated HomeItemsName::"+st.get(i).HomeItemsName);
			//			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.v("HSM", "error in updating");
			e.printStackTrace();
		}
	}

	public void removeStreamByItemId(int id)
	{
		Log.v("HSM", "****************removeStreamByItemId*********************");

		//		Log.d("HSM", "get home stream b4::"+getHomeStream().size());
		Log.d("HSM", "remove stream id::"+id);

		try 
		{
			Dao dao = dataBaseHelper.getGenericDao(com.android.brainslam.db.dao.Streams.class);
			DeleteBuilder delB = dao.deleteBuilder();

			Where wh = delB.where();
			wh.eq("HomeItemID", id);
			dao.delete(delB.prepare());

		} catch (SQLException e) {
			e.printStackTrace();
		}

		//		Log.d("HSM", "get home stream ftr::"+getHomeStream().size());
	}
}