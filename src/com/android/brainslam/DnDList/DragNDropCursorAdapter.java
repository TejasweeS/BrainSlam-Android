/*
 * Copyright 2012 Terlici Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.brainslam.DnDList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.brainslam.R;

public class DragNDropCursorAdapter extends SimpleCursorAdapter implements DragNDropAdapter {
	int mPosition[];
	int mHandler;

	Cursor cursor;
	LayoutInflater inflater;
	Context context;

	public DragNDropCursorAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to, int handler) {
		super(context, layout, cursor, from, to, 0);

		this.context = context;
		this.cursor = cursor;
		mHandler = handler;
		this.inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		setup();
	}

	@Override
	public Cursor swapCursor(Cursor c) {
		Cursor cursor = super.swapCursor(c);

		mPosition = null;
		setup();

		return cursor;
	}

	private void setup() {
		Cursor c = getCursor();

		if (c == null || !c.moveToFirst()) return;

		mPosition = new int[c.getCount()];

		for (int i = 0; i < mPosition.length; ++i) mPosition[i] = i;
	}

	@Override
	public View getDropDownView(int position, View view, ViewGroup group) {
		return super.getDropDownView(mPosition[position], view, group);
	}

	@Override
	public Object getItem(int position) {
		return super.getItem(mPosition[position]);
	}

	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(mPosition[position]);
	}

	@Override
	public long getItemId(int position) {
		return super.getItemId(mPosition[position]);
	}

	public class ViewHolder
	{
		public ImageView ivRemoveStream;
	}


	@Override
	public View getView(int position, View view, ViewGroup group) 
	{
		ViewHolder holder;	
		if(view == null)
		{
			view = inflater.inflate(R.layout.edit_mystream_listitem, group, false);

			holder = new ViewHolder();
			holder.ivRemoveStream = (ImageView) view.findViewById(R.id.stream_close);

			holder.ivRemoveStream.setTag(position);
			view.setTag(holder);
		}
		else
			holder = (ViewHolder)view.getTag();	

		return super.getView(mPosition[position], view, group);
	}

	@Override
	public boolean isEnabled(int position) {
		return super.isEnabled(mPosition[position]);
	}

	@Override
	public void onItemDrag(DragNDropListView parent, View view, int position, long id) {

	}

	@Override
	public void onItemDrop(DragNDropListView parent, View view, int startPosition, int endPosition, long id) {
		int position = mPosition[startPosition];

		if (startPosition < endPosition)
			for(int i = startPosition; i < endPosition; ++i)
				mPosition[i] = mPosition[i + 1];
		else if (endPosition < startPosition)
			for(int i = startPosition; i > endPosition; --i)
				mPosition[i] = mPosition[i - 1];

		mPosition[endPosition] = position;
	}

	@Override
	public int getDragHandler() {
		return mHandler;
	}

}
