package com.android.brainslam.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.android.brainslam.db.DataBaseHelper;
import com.android.brainslam.db.dao.MyFriends;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class FriendManager {

	private DataBaseHelper dataBaseHelper;
	private static FriendManager instance;
	List<MyFriends> friendsList = new ArrayList<MyFriends>();

	public FriendManager(Context context) {
		dataBaseHelper = DataBaseHelper.getInstance(context);
	}

	public static FriendManager getInstance(Context context) {
		// if(instance==null)
		instance = new FriendManager(context);
		return instance;
	}

	public List<MyFriends> getMyFriendsList() {
		return friendsList;
	}

	public void setMyFriendsList(List<MyFriends> myFriendsList) {
		this.friendsList = myFriendsList;
	}

	// Adding a friend to database
	public void addFriend(MyFriends friend) {
		try {
			if (friend != null) {
				dataBaseHelper.getGenericDao(com.android.brainslam.db.dao.MyFriends.class).createOrUpdate(friend);
			} else
				Log.v("FRIEND MANAGER", "Friend is Null ");
		} catch (SQLException e) {
			Log.v("FRIEND MANAGER", "Exception in addFriend()");
			e.printStackTrace();
		}

	}

	// Get Friend List from Database
	public List<MyFriends> getMyFriends() {
		Dao dao;
		try {
			dao = dataBaseHelper.getGenericDao(com.android.brainslam.db.dao.MyFriends.class);
			QueryBuilder qB = dao.queryBuilder();

			List<MyFriends> friendsList = dao.query(qB.prepare());
			setMyFriendsList(friendsList);
			return friendsList;

		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<MyFriends>();
		}
	}

	// Save Friend List to Database.
	public void saveMyFriends(List<MyFriends> myFriendsList) {
		if (myFriendsList != null && myFriendsList.size() > 0) {
			for (int i = 0; i < myFriendsList.size(); i++) {
				try {
					dataBaseHelper.getGenericDao(com.android.brainslam.db.dao.MyFriends.class).createOrUpdate(
							myFriendsList.get(i));
					System.out.println("Friend Saved");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void removeAllfrinends()
	{
		
		Dao  dao;
		try
		{
			dao = dataBaseHelper.getGenericDao(com.android.brainslam.db.dao.MyFriends.class);
			dao.delete(getMyFriends());
		}
		catch(SQLException e)
		{
			Log.v("CategoryManager", "ERROR in remove");
			e.printStackTrace();
		}

		Log.v("CategoryManager", "removed all data");
	}
	
	public void updateIsOnHomeScreen(int userId, boolean isOnHomeScreen)
	{
	
		Dao  dao;
		try {
			
			dao = dataBaseHelper.getGenericDao(com.android.brainslam.db.dao.MyFriends.class);
			QueryBuilder qB = dao.queryBuilder();
			Where whr = qB.where();
			whr.eq("userId", userId);
			
			List<MyFriends> friendsList = dao.query(whr.prepare());
			
			if(friendsList.size() > 0)
			{
				
				friendsList.get(0).isOnHomeScreen = isOnHomeScreen;
				dao.update(friendsList.get(0));
				Log.v("FriendsManager", "friend updating.....");
			}
			
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		Log.v("FriendsManager", "error in updating");
			e.printStackTrace();
		}
	}
}
