package com.troy.qatarevents.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.troy.qatarevents.EventDetailsActivity;
import com.troy.qatarevents.Events;
import com.troy.qatarevents.R;
import com.troy.qatarevents.utils.JSONParser;
import com.troy.qatarevents.utils.SortEventsListAdapter;
import com.troy.qatarevents.utils.Utils;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class ListEventsAsyncTask extends AsyncTask<ArrayList<String>, Void, ArrayList<String>>{
	String status;
	String[] titles;
	String[] datetimeplace;
	String[] images;
	String[] entity_id;
	String[] category_name;
	Utils utils;
	ListView listView;
    List<Events> rowItems;
    ProgressDialog prgDialog;
    Activity mContext;
    
    TextView list_empty;
    String sortBy;
	//Bitmap createdBitmap[];
    
    public ListEventsAsyncTask(Activity mcontext){
    	this.mContext = mcontext;
        list_empty = (TextView)this.mContext.findViewById(R.id.today_list_empty);
  		list_empty.setVisibility(View.GONE);
    }
	
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        prgDialog = new ProgressDialog(this.mContext);
        prgDialog.setCancelable(true);
	    prgDialog.setMessage("Please wait...");
	    prgDialog.show();
    }
	
	@Override
	protected ArrayList<String> doInBackground(ArrayList<String>... params) {
		ArrayList<String> result = new ArrayList<String>();
        sortBy = params[0].get(0);
        listEvent(sortBy);
 	 return null;
 	}
	
	@Override
 	protected void onPostExecute(ArrayList<String> result){
 	   //showToast("Comment sent");
 		loadEvents(entity_id,titles,datetimeplace,images,status,category_name);
		prgDialog.dismiss();
	}
  
 	public String getEntityId(int position){
 		return entity_id[position];
 	}
 	
	public void listEvent(String sortBy) {
		String url = "http://ilq.isuf.co.uk/iloveqatar/android/_index.php";
 		
 		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
 		 nameValuePair.add(new BasicNameValuePair("sortby",sortBy));
         nameValuePair.add(new BasicNameValuePair("method", "sortEvents"));
         
         String json = new JSONParser().getJSONFromUrl(url, nameValuePair);
			    if (json != null) {
	                try {
	                	JSONArray jr = new JSONArray(json);  
	                	titles = new String[jr.length()]; 
	                	datetimeplace = new String[jr.length()];
	                	images = new String[jr.length()];
	                	//createdBitmap = new Bitmap[jr.length()];
	                	entity_id = new String[jr.length()];
	                	category_name = new String[jr.length()];
	                	
	                	 for (int i = 0; i < jr.length(); i++) {
                            try {
                                JSONObject obj = jr.getJSONObject(i);
                                status = obj.getString("status").toString();
                                titles[i] = obj.getString("title").toString();
                                datetimeplace[i] = obj.getString("date_time_place").toString();
                                images[i] = obj.getString("uri").toString();
                                entity_id[i] = obj.getString("entity_id").toString();
                                category_name[i] = obj.getString("category_name").toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
	                	Log.e("TITLES", String.valueOf(titles.length));
	                	Log.e("CREATED", String.valueOf(datetimeplace.length));
                        
		               }
	                catch (JSONException e) {
	                    e.printStackTrace();
	                }
	            } else {
	                Log.e("ServiceHandler", "Couldn't get any data from the url");
	            } 
		} 
	

	//Bitmap mIconbitmap[]
	 public void loadEvents(final String[] entityid,String[] titles,String[] datetimeplace,String[] images,String status,String[] categoryname){
		 listView = (ListView) mContext.findViewById(R.id.sort_event_list);
		 if(status.equals("0")){
			 list_empty.setVisibility(View.VISIBLE);
			 listView.setVisibility(View.GONE);
			 
		 }else{
			 listView.setVisibility(View.VISIBLE);
			 list_empty.setVisibility(View.GONE);
		     rowItems = new ArrayList<Events>();
	        for (int i = 0; i < titles.length; i++) {
	        	//,mIconbitmap[i]
	        	Events item = new Events(titles[i].toString(), images[i].toString(), datetimeplace[i].toString(),categoryname[i].toString());
	            rowItems.add(item);
	        	//Log.e("Title",titles[i]);
	        }
	        SortEventsListAdapter adapter = new SortEventsListAdapter(mContext, rowItems);
	        listView.setAdapter(adapter);
	       //adapter.notifyDataSetChanged();
	        
	        listView.setOnItemClickListener(new OnItemClickListener() {

	            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
	                Log.d("Click on the item",sortBy+"!!!!!!!!!!"+entityid[position].toString());
	                mContext.startActivity(new Intent(mContext,EventDetailsActivity.class).putExtra("entity_id",entityid[position].toString()));
	            } 
	         });
		 }
	 }
}