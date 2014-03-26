
package com.android.brainslam.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.android.brainslam.db.DataBaseHelper;
import com.android.brainslam.db.dao.Category;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class CategoryManager
{
	private DataBaseHelper dataBaseHelper;
	private static  CategoryManager instance;
	
	List<Category> categoriesList =  new ArrayList<Category>();  

	public List<Category> getCategoriesList() {
		return categoriesList;
	}
	public void setCategoriesList(List<Category> categoriesList) {
		this.categoriesList = categoriesList;
	}
	public static CategoryManager getInstance(Context context)
	{
		if(instance==null)
			instance = new CategoryManager(context);
		return instance;
	}
	private CategoryManager(Context context) {
		dataBaseHelper = DataBaseHelper.getInstance(context);
	}

	public List<Category> getCategories()
	{
		Dao dao;
		try {
			dao = dataBaseHelper.getGenericDao(com.android.brainslam.db.dao.Category.class);
			QueryBuilder qB = dao.queryBuilder();

			List<Category> catList = dao.query(qB.distinct().prepare());
			setCategoriesList(catList);
			return catList;

		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<Category>();
		}
	}

	public void saveCategories(List<Category> categories)
	{
		Log.v("CategoryManager", "save *********************************************");
		if(categories != null && categories.size() > 0)
		{
			for (int i = 0; i < categories.size(); i++) 
			{
				try
				{
				        dataBaseHelper.getGenericDao(com.android.brainslam.db.dao.Category.class).
					    createOrUpdate(categories.get(i));
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		setCategoriesList(categories);
	}
	

	public void removeAllCategories()
	{
		
		Dao  dao;
		try
		{
			dao = dataBaseHelper.getGenericDao(com.android.brainslam.db.dao.Category.class);
			dao.delete(getCategories());
		}
		catch(SQLException e)
		{
			Log.v("CategoryManager", "ERROR in remove");
			e.printStackTrace();
		}

		Log.v("CategoryManager", "removed all data");
	}
	
	
	public void updateIsOnHomeScreen(int categoryId, boolean isOnHomeScreen)
	{
		Log.v("CategoryManager", "updateIsOnHomeScreen *********************************************");
		
		Dao  dao;
		try {
			
			dao = dataBaseHelper.getGenericDao(com.android.brainslam.db.dao.Category.class);
			QueryBuilder qB = dao.queryBuilder();
			Where whr = qB.where();
			whr.eq("categoryId", categoryId);
			
			List<Category> categoryList = dao.query(whr.prepare());
			
			if(categoryList.size() > 0)
			{
				
				categoryList.get(0).isOnHomePage = isOnHomeScreen;
				dao.update(categoryList.get(0));
				
				
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		Log.v("CategoryManager", "error in updating");
			e.printStackTrace();
		}
	}
	
	
}