package com.android.listdapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.brainslam.R;
import com.android.brainslam.DnDList.DragNDropCursorAdapter;
import com.android.brainslam.db.dao.Category;
import com.android.brainslam.mainscreen.EditStreamingActivity;
import com.android.brainslam.mainscreen.MainScreen_CategoryActivity;
import com.android.brainslam.manager.CategoryManager;
import com.android.brainslam.manager.HomeStreamManager;
import com.android.utils.ImageLoader;

public class MainScreenCategoryAdapter extends BaseAdapter {
	Context context;
	ArrayList<Category> list;
	public ImageLoader imageLoader;
	//	HashSet<String> hashSetRecommended = new HashSet<String>();
	public MainScreenCategoryAdapter(Context context,
			ArrayList<Category> categoryList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = categoryList;
		imageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);

	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return list.get(position).categoryId;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context
		.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.landing_activity_category_listitem, null);
			holder.text = (TextView) convertView.findViewById(R.id.textView1);
			holder.categoryImage = (ImageView) convertView.findViewById(R.id.imageView1);
			holder.checkBox = (CheckBox)convertView.findViewById(R.id.checkBox1);

			convertView.setTag(holder);

		} else
			holder = (ViewHolder) convertView.getTag();

		holder.checkBox
		.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton button,
					boolean value) {
				System.out.println("CHECK CHANGED"+position+value);
				//list.get(position).isOnHomePage = value;
				//holder.checkBox.setChecked(list.get(position).isOnHomePage);
				//Integer getPosition = (Integer) button.getTag();  // Here we get the position that we have set for the checkbox using setTag.
				//                list.get(getPosition).setSelect(button.isChecked()); // Set the value of checkbox to maintain its state.
				list.get(position).isOnHomePage=value;
				//			Log.e("ID", ""+list.get(position).categoryId);
				//			hashSetRecommended.add(""+list.get(position).categoryId);
				//			Log.e("HASHSET", ""+hashSetRecommended);
			}

		});
		//         holder.checkBox.setTag(position); 
		System.out.println("CHECK CHANGED"+position+"::"+list.get(position).isOnHomePage);
		holder.checkBox.setChecked(list.get(position).isOnHomePage);
		holder.text.setText(list.get(position).categoryName);
		if(list.get(position).isOnHomePage)
			MainScreen_CategoryActivity.hashSetRecommended.add(""+list.get(position).categoryId);
		imageLoader.DisplayImage(list.get(position).thumbnailUrl, holder.categoryImage);
		//		 holder.checkBox.setChecked(list.get(position).isSelect());
		holder.checkBox.setTag(list.get(position));
		convertView.setTag(holder);


		holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub

				MainScreen_CategoryActivity.categoryList.get(position).isOnHomePage = isChecked;
				if (isChecked) {
					MainScreen_CategoryActivity.hashSetRecommended.add(""
							+list.get(position).categoryId);

					Log.e("addCheck", "" + MainScreen_CategoryActivity.hashSetRecommended);
				} else {

					//										if (chkcategory.getTag().getClass().equals(Category.class)) {
					if (MainScreen_CategoryActivity.hashSetRecommended.contains(""
							+ (list.get(position).categoryId)))
						MainScreen_CategoryActivity.hashSetRecommended.remove(""
								+ (list.get(position).categoryId));
					//										}
				}

				CategoryManager.getInstance(context.getApplicationContext()).
				updateIsOnHomeScreen(list.get(position).categoryId,isChecked); 

				HomeStreamManager.getInstance(context.getApplicationContext()).
				updateHomeStream(list.get(position), isChecked);


				updateStreamsList();
			}
		});

		return convertView;
	}

	private void updateStreamsList()
	{
		Cursor cursor = HomeStreamManager.getInstance(
				context.getApplicationContext()).getHomeStreamCursor();

		cursor.moveToFirst();
		while(cursor.moveToNext())
		{
			Log.d("ESA", "column HomeItemsName::"+cursor.getString(cursor.getColumnIndex("HomeItemsName")));			
		}
		cursor.moveToFirst();	

		// get cursor here from db
		final DragNDropCursorAdapter adapter = new DragNDropCursorAdapter(context,
				R.layout.edit_mystream_listitem,
				cursor,
				new String[]{"HomeItemsName"},
				new int[]{R.id.stream_name},
				R.id.iv_stream_handler);

		EditStreamingActivity.dndStreamList.setDragNDropAdapter(adapter);
	}

	class ViewHolder {
		TextView text;
		ImageView categoryImage;
		CheckBox checkBox;
	}

}
