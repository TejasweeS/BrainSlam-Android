package com.android.brainslam.manager;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.android.brainslam.db.DataBaseHelper;
import com.android.brainslam.db.dao.LastSyncTime;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class LastSyncTimeManager 
{
	private DataBaseHelper dataBaseHelper;
	private static LastSyncTimeManager instance ;

	public static LastSyncTimeManager getInstance(Context context)
	{
		if(instance==null)
			instance = new LastSyncTimeManager(context);
			return instance;
	}
	
	private LastSyncTimeManager(Context context) 
	{
		dataBaseHelper = DataBaseHelper.getInstance(context);
	}

	public String getLastSyncTimeFromDb(String keyName)
	{
		String last_sync_time_message = "";
		try {
			Dao dao = dataBaseHelper
					.getGenericDao(LastSyncTime.class);
			QueryBuilder qB = dao.queryBuilder();
			Where wh = qB.where();
			wh.eq("keyName", keyName);
			List<com.android.brainslam.vo.LastSyncTime> lastSyncList = 
				(List<com.android.brainslam.vo.LastSyncTime>) dao
					.query(qB.prepare());

			if (lastSyncList.size() > 0) 
			{
				last_sync_time_message = lastSyncList.get(0).keyValue;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (last_sync_time_message.trim().equalsIgnoreCase(""))
			last_sync_time_message = "0";

		Log.d("LastSyncTime", "getLastSyncTimeFromDb last_sync_time::"+last_sync_time_message);

		return last_sync_time_message;
	}
}