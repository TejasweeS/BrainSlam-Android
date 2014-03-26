package com.android.listdapters;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
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

import com.android.brainslam.LandingActivity;
import com.android.brainslam.R;
import com.android.brainslam.db.dao.Category;
import com.android.brainslam.manager.CategoryManager;
import com.android.utils.ImageLoader;
//import com.android.brainslam.vo.Category;

//Adapter class extends with BaseAdapter and implements with OnClickListener 
public class LazyImageAdapterLandingtiles extends BaseAdapter{
    public  int setimagecount=0;
    private Activity activity;
    private ArrayList<ArrayList<Category>> list;
    
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyImageAdapterLandingtiles(Activity a, ArrayList<ArrayList<Category>> list) {
        activity = a;
        this.list = list;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        // Create ImageLoader object to download and show image in list
        // Call ImageLoader constructor to initialize FileCache
        imageLoader = new ImageLoader(activity.getApplicationContext());
    }

 
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
    
    /********* Create a holder Class to contain inflated xml file elements *********/
    public  class ViewHolder{
         
        public TextView text;
        public TextView text1;
        public TextView textWide;
        public ImageView image1,image2,image3,image4;
        public CheckBox imgchk1,imgchk2,imgchk3,imgchk4;
 
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
    	
        View vi=convertView;
        ViewHolder holder=null;
         
        if(convertView==null){
             
            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.landing_activity_tilesitem, null);
//             
//            /****** View Holder Object to contain tabitem.xml file elements ******/
            holder = new ViewHolder();
            
            
//          //  holder.text = (TextView) vi.findViewById(R.id.textView1);
//            holder.image1=(ImageView)vi.findViewById(R.id.imageView1);
            holder.image2=(ImageView)vi.findViewById(R.id.imageView2);
            holder.image3=(ImageView)vi.findViewById(R.id.imageView3);
            holder.image4=(ImageView)vi.findViewById(R.id.imageView4);
            
//            holder.imgchk1=(CheckBox)vi.findViewById(R.id.checkBox1);
            holder.imgchk2=(CheckBox)vi.findViewById(R.id.checkBox2);
            holder.imgchk3=(CheckBox)vi.findViewById(R.id.checkBox3);
            holder.imgchk4=(CheckBox)vi.findViewById(R.id.checkBox4);
            
            
//           /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else 
         holder=(ViewHolder)vi.getTag();
        
//        holder.image1.setVisibility(View.GONE);
        holder.image2.setVisibility(View.GONE);
        holder.image3.setVisibility(View.GONE);
        holder.image4.setVisibility(View.GONE);
        
//        holder.imgchk1.setVisibility(View.GONE);
        holder.imgchk2.setVisibility(View.GONE);
        holder.imgchk3.setVisibility(View.GONE);
        holder.imgchk4.setVisibility(View.GONE);
        
        
        //holder.text.setText("Category  "+position);
      /*  ImageView image1 = holder.image1;
        ImageView image2 = holder.image2;
        ImageView image3 = holder.image3;
        ImageView image4 = holder.image4;*/
       

        ArrayList<Category> arrayList = new  ArrayList<Category>();
        
        arrayList = list.get(position);
		ImageView[] ImageViewArray = {  holder.image2,
				holder.image3, holder.image4 };
		
		CheckBox[] CheckBoxArray = {holder.imgchk2,holder.imgchk3,holder.imgchk4};
        for(int i=0;i<arrayList.size() ;i++)
        {
        	
        
        	Log.v("rutuja", "row:: "+position+" cloumn::"+i);
        	String url = arrayList.get(i).thumbnailUrl;
//          if( i == 0)
//          {
//        	  
//        	  int width = holder.image1.getWidth();
//        	  int height = holder.image1.getHeight();
//        	  
//        	  url = arrayList.get(i).thumbnailUrl+"/width/600/height/200";
//        	  
//        	  System.out.println("rutuja width :: "+width+"::: height ::"+height);
//        	  
//        	  
//          }
        
          ImageViewArray[i].setVisibility(View.VISIBLE);
      	CheckBoxArray[i].setVisibility(View.VISIBLE);
      	CheckBoxArray[i].setTag(arrayList.get(i));
      	
      	
      	if(arrayList.get(i).isOnHomePage)
      	{
      		CheckBoxArray[i].setChecked(true);
//      		LandingActivity.hashSetRecommended.add(""+arrayList.get(i).categoryId);	
//				CategoryManager.getInstance(activity.getApplicationContext())
//						.updateIsOnHomeScreen(arrayList.get(i).categoryId,
//								arrayList.get(i).isOnHomePage);
      	}
      	
      	CheckBoxArray[i].setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
//				arrayList.get(i).isOnHomePage = isChecked;
			}
		});
      	
    	    imageLoader.DisplayImage(url, ImageViewArray[i]);
        	

        }
        
//        String url = list.get(position).thumbnailUrl;
//    	System.out.println("rutuja adapter ::: "+list.get(position).categoryName);
//      holder.image2.setVisibility(View.VISIBLE);
//      holder.imgchk2.setVisibility(View.VISIBLE);
//      holder.imgchk2.setTag(list.get(position).categoryId);
//  	if(list.get(position).isOnHomePage)
//  	{
//  		holder.imgchk2.setChecked(true);
//  		LandingActivity.hashSetRecommended.add(""+list.get(position).categoryId);	
//  	}	
//	    imageLoader.DisplayImage(url, holder.image2);        
       
        /******** Set Item Click Listner for LayoutInflater for each row ***********/
//       vi.setOnClickListener(new OnItemClickListener(position));
        return vi;
        
    }
    private int rowPosition;    

	
	
    
    /********* Called when Item click in ListView ************/
//    private class OnItemClickListener  implements OnClickListener{           
//      
//        
//       OnItemClickListener(int position){
//        	 rowPosition = position;
//        	 
//        	 System.out.println("rutuja adapter selected row:: "+rowPosition);
//        }
//        
//        @Override
//        public void onClick(View arg0){
////        	LandingActivity sct = (LandingActivity)activity;
////        	sct.onItemClick(mPosition);
//        }               
//    }

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}   
}