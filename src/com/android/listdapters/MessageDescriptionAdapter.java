package com.android.listdapters;

import java.util.LinkedHashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;

public class MessageDescriptionAdapter extends BaseAdapter {


	
	LayoutInflater inflater;
	
	

	public final Map<String,Adapter> sections = new LinkedHashMap<String,Adapter>();
    public final BaseAdapter headers;
    public final static int TYPE_SECTION_HEADER = 0;
    
    public MessageDescriptionAdapter(Context context) {
        headers = new MessageHeaderAdapter((Activity)context) ;
//        		ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
    }
    
    public void addSection(String section, Adapter adapter) {
    	System.out.println("add section"+section);
    	
//        this.headers.add(section);
        this.sections.put(section, adapter);
    }
    
    public Object getItem(int position) {
        for(Object section : this.sections.keySet()) {
            Adapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;
            
            // check if position inside this section 
            if(position == 0) return section;
            if(position < size) return adapter.getItem(position - 1);

            // otherwise jump into next section
            position -= size;
        }
        return null;
    }

    public int getCount() {
        // total together all sections, plus one for each section header
        int total = 0;
        for(Adapter adapter : this.sections.values())
            total += adapter.getCount() + 1;
        return total;
    }

    public int getViewTypeCount() {
        // assume that headers count as one, then total all sections
        int total = 1;
        for(Adapter adapter : this.sections.values())
            total += adapter.getViewTypeCount();
        return total;
    }
    
    public int getItemViewType(int position) {
        int type = 1;
        for(Object section : this.sections.keySet()) {
            Adapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;
            
            // check if position inside this section 
            if(position == 0) return TYPE_SECTION_HEADER;  
            if(position < size) return type + adapter.getItemViewType(position - 1);

            // otherwise jump into next section
            position -= size;
            type += adapter.getViewTypeCount();
        }
        return -1;
    }
    
    public boolean areAllItemsSelectable() {
        return false;
    }

    public boolean isEnabled(int position) {
        return (getItemViewType(position) != TYPE_SECTION_HEADER);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int sectionnum = 0;
        for(Object section : this.sections.keySet()) {
            Adapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;
            
            // check if position inside this section 
            if(position == 0) 
            	return headers.getView(sectionnum, convertView, null); // this is where your header names will get bind. correctly. 
            if(position < size)
            	return adapter.getView(position - 1, convertView, parent);
            

            // otherwise jump into next section
            position -= size;
            sectionnum++;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
