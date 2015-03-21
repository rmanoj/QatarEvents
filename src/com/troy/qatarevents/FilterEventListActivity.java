package com.troy.qatarevents;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.ls.LSInput;

import com.troy.qatarevents.utils.CustomListAdapter;
import com.troy.qatarevents.utils.Utils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FilterEventListActivity extends Activity implements OnClickListener{
	ImageView backimage;
	TextView actionbartitle;
	String eventName;
	String colorCode;
	RelativeLayout filterEventsTopBar;
	Utils utils;
	ListView listView;
    List<Events> rowItems;
    ProgressDialog prgDialog;
    TextView list_empty,cat_line;
    ImageView acRefresh;
    ArrayList<String> passing;
    //Bundle b;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		 setContentView(R.layout.activity_filter_event_list);
		 
	      list_empty = (TextView)findViewById(R.id.list_empty);
		  list_empty.setVisibility(View.GONE);
		  
		  
		 Bundle extras = getIntent().getExtras();
		    if (extras != null)
		    {
		    	  eventName = extras.getString("eventName");
		    	  if(eventName.equals("Misc")){
		    		  colorCode = "#A31221";
		    	  }else if(eventName.equals("Entertainment")){
		    		  colorCode = "#0042BC";
		    	  }else if(eventName.equals("Sport")){
		    		  colorCode = "#E07E00";
		    	  }else if(eventName.equals("Culture")){
		    		  colorCode = "#00A33D";
		    	  }else if(eventName.equals("exhibition")){
		    		  colorCode = "#E6B300";
		    	  }else if(eventName.equals("Social")){
		    		  colorCode = "#0380D7";
		    	  }else if(eventName.equals("Night")){
		    		  colorCode = "#6F43AD";
		    	  }
		    }
		 
		 
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayShowCustomEnabled(true);
	    LayoutInflater mInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
	    actionbar.setCustomView(mCustomView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	    backimage = (ImageView) mCustomView.findViewById(R.id.back);
	    actionbartitle = (TextView)mCustomView.findViewById(R.id.actionbar_title);
	    actionbartitle.setText(eventName);
	    backimage.setOnClickListener(this);
	    filterEventsTopBar = (RelativeLayout)mCustomView.findViewById(R.id.top_bar);
	    filterEventsTopBar.setBackgroundColor(Color.parseColor(colorCode));
	    
	    acRefresh = (ImageView) mCustomView.findViewById(R.id.ac_refresh);
	    acRefresh.setVisibility(View.VISIBLE);
	    acRefresh.setOnClickListener(this);
	    
	    passing = new ArrayList<String>();
 		passing.add(eventName);
	    new ListEventsAsyncTask().execute(passing);
	}

	private class ListEventsAsyncTask extends AsyncTask<ArrayList<String>, Void, ArrayList<String>>{
		String status;
		String[] titles;
		String[] datetimeplace;
		String[] images;
		String[] entity_id;
		//Bitmap createdBitmap[];
		
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(FilterEventListActivity.this);
	        prgDialog.setCancelable(true);
		    prgDialog.setMessage("Please wait...");
		    prgDialog.show();
        }
		
		@Override
		protected ArrayList<String> doInBackground(ArrayList<String>... params) {
			
			ArrayList<String> result = new ArrayList<String>();
	        String eventName = params[0].get(0);
	       /* String Ppass  = params[0].get(1);*/
	        listEvent(eventName);
	 	 return null;
	 	}
		
	 	protected void onPostExecute(ArrayList<String> result){
	 	   //showToast("Comment sent");
	 		loadEvents(entity_id,titles,datetimeplace,images,status);
			prgDialog.dismiss();
		}
	  
	 	public String getEntityId(int position){
	 		return entity_id[position];
	 	}
	 	
		public void listEvent(String eventname) {
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://ilq.isuf.co.uk/iloveqatar/android/_index.php");
	  
	 		try {
	 				// Add your data
	 		 // create a list to store HTTP variables and their values
			 List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
	         nameValuePair.add(new BasicNameValuePair("name",eventname));
	         nameValuePair.add(new BasicNameValuePair("method", "filterEventsByName"));
	 		 httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
	  
	 				// Execute HTTP Post Request
	 		 	HttpResponse response = httpclient.execute(httppost);
				if (response.getStatusLine().getStatusCode() == 200)
				{
				    HttpEntity entity = response.getEntity();
				    String json = EntityUtils.toString(entity);
				    System.out.println("Web service : "+json);
				   
				    if (json != null) {
		                try {
		                	JSONArray jr = new JSONArray(json);  
		                	titles = new String[jr.length()]; 
		                	datetimeplace = new String[jr.length()];
		                	images = new String[jr.length()];
		                	//createdBitmap = new Bitmap[jr.length()];
		                	entity_id = new String[jr.length()];
		                	 for (int i = 0; i < jr.length(); i++) {
	                            try {
	                                JSONObject obj = jr.getJSONObject(i);
	                                status = obj.getString("status").toString();
	                                titles[i] = obj.getString("title").toString();
	                                datetimeplace[i] = obj.getString("date_time_place").toString();
	                                images[i] = obj.getString("uri").toString();
	                                entity_id[i] = obj.getString("entity_id").toString();
	                               /* String src = images[i];
	                                 try {
	                                        java.net.URL url = new java.net.URL(src);
	                                        HttpURLConnection connection = (HttpURLConnection) url
	                                                .openConnection();
	                                        connection.setDoInput(true);
	                                        connection.connect();
	                                        //file list
	                                        InputStream input = connection.getInputStream();
	                                        createdBitmap[i] = BitmapFactory.decodeStream(input);
	                                        
	                                    } catch (IOException e) {
	                                        e.printStackTrace();
	                                    }	*/
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
				   			   
				}else{
					showAlert("Error","Something went wrong try again later.");
					prgDialog.dismiss();
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e("Sign in Client Protocol Exception",e.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Sign in IOException",e.toString());
			}
		}
	}
	public void showAlert(final String title,final String message){
        FilterEventListActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(FilterEventListActivity.this);
                builder.setTitle(title);
                builder.setMessage(message)  
                       .setCancelable(false)
                       .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                           }
                       });                     
                AlertDialog alert = builder.create();
                alert.show();               
            }
        });
	}
	//Bitmap mIconbitmap[]
	 public void loadEvents(final String[] entityid,String[] titles,String[] datetimeplace,String[] images,String status){
		 listView = (ListView) findViewById(R.id.list_filter_events);
		 if(status.equals("0")){
			 list_empty.setVisibility(View.VISIBLE);
			 listView.setVisibility(View.GONE);
			 
		 }else{
			 listView.setVisibility(View.VISIBLE);
			 list_empty.setVisibility(View.GONE);
		    rowItems = new ArrayList<Events>();
	        for (int i = 0; i < titles.length; i++) {
	        	//,mIconbitmap[i]
	        	Events item = new Events(titles[i].toString(), images[i].toString(), datetimeplace[i].toString(),eventName);
	            rowItems.add(item);
	        	//Log.e("Title",titles[i]);
	        }
	        CustomListAdapter adapter = new CustomListAdapter(this, rowItems);
	        listView.setAdapter(adapter);
	       // adapter.notifyDataSetChanged();
	        
	        listView.setOnItemClickListener(new OnItemClickListener() {

	            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
	            	//String entityId = new ListEventsAsyncTask().getEntityId(position);
	                Log.d("Click on the item",eventName+"!!!!!!!!!!"+entityid[position].toString());
	                startActivity(new Intent(FilterEventListActivity.this,EventDetailsActivity.class).putExtra("entity_id",entityid[position].toString()));
	                /*b.putStringArray("event_details", new String[]{entityid[position].toString(),colorCode });
	                startActivity(new Intent(FilterEventListActivity.this,EventDetailsActivity.class).putExtras(b));*/
	               //utils.showToast("Clicked", getApplicationContext()); 
	            } 
	         });
		 }
	 }
	 

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
	        // Respond to the action bar's Up/Home button
	        case R.id.back:
	        	finish();
	        break;
	        case R.id.ac_refresh:
				/*Intent intent = getIntent();
				finish();
				startActivity(intent)*/
	        	new ListEventsAsyncTask().execute(passing);
			break;
	        default:
	        break;
	    }
	}
}
