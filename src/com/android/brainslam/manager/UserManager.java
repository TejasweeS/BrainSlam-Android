package com.android.brainslam.manager;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.android.brainslam.db.DataBaseHelper;
import com.android.brainslam.db.dao.Users;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class UserManager {


	private DataBaseHelper dataBaseHelper;
	private static  UserManager instance ;

	public static UserManager getInstance(Context context)
	{
		if(instance==null)
			instance=new UserManager(context);
		return instance;
	}

	private UserManager(Context context) {
		dataBaseHelper = DataBaseHelper.getInstance(context);
	}

	// adding user to database 
	public void addUser(Users users)
	{
		
		
		try
		{
			if(users != null)
			{
			dataBaseHelper.getGenericDao(com.android.brainslam.db.dao.Users.class).
			createOrUpdate(users);
			}
			else
				Log.v("UsreManager", "User null ");
		}
		catch(SQLException e)
		{
			Log.v("UserManager", "exception in addUser");
			e.printStackTrace();
		}

	}

	public Users getUser(int userId)
	{
		List<Users>  list;

		Dao dao;
		try {
			dao = dataBaseHelper.getGenericDao(Users.class);
			QueryBuilder qb = dao.queryBuilder();			
            Where wh =  qb.where();
			wh.eq("userId", userId);
			list = dao.query(qb.prepare());

			Log.v("rutuja", "rutuja  list size"+list.size());
			if(list.size() >0)
			{				
				return (list.get(0)); 
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.v("UserManager", "exception in getUser");
			e.printStackTrace();
		}	

		return null;

	}

}
