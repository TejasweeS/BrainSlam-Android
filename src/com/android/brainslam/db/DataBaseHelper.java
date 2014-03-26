package com.android.brainslam.db;

import java.sql.SQLException;
import java.util.HashMap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.brainslam.db.dao.Category;
import com.android.brainslam.db.dao.CrewMemberMapping;
import com.android.brainslam.db.dao.CrewRequests;
import com.android.brainslam.db.dao.Crews;
import com.android.brainslam.db.dao.MyFriends;
import com.android.brainslam.db.dao.Streams;
import com.android.brainslam.db.dao.Users;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Database helper which creates and upgrades the database and provides the DAOs
 * for the app.
 * 
 */
public class DataBaseHelper extends OrmLiteSqliteOpenHelper
{
	private final String TAG = "ORM";
	public static final String DATABASE_NAME = "brainslam.db";
	private static final int DATABASE_VERSION = 1;
	private static DataBaseHelper dataBaseHelper;
	private static final Class[] tableArray = 
	{ 
		Streams.class,
		Users.class,
		Crews.class,
		Category.class,
		MyFriends.class,
		CrewMemberMapping.class,
		CrewRequests.class
	};

	@SuppressWarnings("rawtypes")
	private static final HashMap<String, Dao> daoMap = new HashMap<String, Dao>();

	//SINGLETON

	public static DataBaseHelper getInstance(Context context)
	{
		if(dataBaseHelper==null)
			dataBaseHelper=new DataBaseHelper(context);
		//		else
		return dataBaseHelper;
	}
	
	public static DataBaseHelper getInstance()
	{
		return dataBaseHelper;
	}
	
	private DataBaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		System.out.println("DataBaseHelper 1");
	}

	@Override
	public synchronized void close() 
	{

		super.close();
	}


	public boolean deleteField(Class classObject, Object object)
	{
		try
		{
			getGenericDao(classObject).delete(object);
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}



	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase, final ConnectionSource connectionSource)
	{
		Log.v(TAG, "creating database");
		try
		{
			//dropTable(connectionSource);
			for (int i = 0; i < tableArray.length; i++)
			{
				TableUtils.createTable(connectionSource, tableArray[i]);
				Log.v("database: ", tableArray[i].toString());
			}
		}
		catch (SQLException e)
		{
			Log.v(TAG, "Exception in DataBaseHelper :: onCreate()..."+ e.toString());
		}
	}	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Dao getGenericDao(Class object) throws SQLException
	{
		if (daoMap.containsKey(object.getName())) 
		{ 
			return daoMap.get(object.getName()); 
		}
		Dao dao = getDao(object);
		daoMap.put(object.getName(), dao);
		return dao;
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSrc, int oldVersion,
			int newVersion) 
	{
		Log.v(TAG, "databae onUpgrade oldVersion::"+oldVersion+"  newVersion::"+newVersion);
	}
}