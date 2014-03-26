package com.android.brainslam.manager;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.android.brainslam.db.DataBaseHelper;
import com.android.brainslam.db.dao.Crews;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

public class CrewManager 
{
	private DataBaseHelper dataBaseHelper;
	private static  CrewManager instance ;

	public static CrewManager getInstance(Context context)
	{
		if(instance==null)
			instance = new CrewManager(context);
		return instance;
	}
	
	private CrewManager(Context context) 
	{
		dataBaseHelper = DataBaseHelper.getInstance(context);
	}

	public List<Crews> getCrews()
	{
		Dao dao;
		try {
			dao = dataBaseHelper.getGenericDao(com.android.brainslam.db.dao.Crews.class);
			QueryBuilder qB = dao.queryBuilder();

			List<Crews> crewList = dao.query(qB.prepare());
			return crewList;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void removeAllCrews()
	{

		Dao  dao;
		try
		{
			dao = dataBaseHelper.getGenericDao(com.android.brainslam.db.dao.Crews.class);
			dao.delete(getCrews());
		}
		catch(SQLException e)
		{
			Log.v("CategoryManager", "ERROR in remove");
			e.printStackTrace();
		}

		Log.v("CategoryManager", "removed all data");
	
		
	}
	public void saveCrews(List<Crews> crews)
	{
		if(crews != null && crews.size() > 0)
		{
			for (int i = 0; i < crews.size(); i++) 
			{
				try
				{
					dataBaseHelper.getGenericDao(com.android.brainslam.db.dao.Crews.class).
					createOrUpdate(crews.get(i));
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
