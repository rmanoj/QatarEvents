package com.troy.qatarevents;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.troy.qatarevents.utils.JSONParser;
import com.troy.qatarevents.utils.Utils;
import com.troy.qatarevents.utils.imagefromurl.ImageLoader;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EventDetailsActivity extends Activity implements OnClickListener{
	ImageView backimage;
	TextView actionbartitle;
	RelativeLayout filterEventsTopBar;
	String entity_id;
	ImageView event_icon,acRefresh;
	TextView event_title,event_date_time_place,event_summary,event_summary_title,event_description_title,event_description;
	ProgressDialog prgDialog;
	Utils utils;
	ImageLoader imgLoader;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		 setContentView(R.layout.activity_event_details);
	     
	     imgLoader = new ImageLoader(getApplicationContext());
	     
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayShowCustomEnabled(true);
	    LayoutInflater mInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
	    actionbar.setCustomView(mCustomView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	    backimage = (ImageView) mCustomView.findViewById(R.id.back);
	    actionbartitle = (TextView)mCustomView.findViewById(R.id.actionbar_title);
	    actionbartitle.setText("Details");
	    backimage.setOnClickListener(this);
	    filterEventsTopBar = (RelativeLayout)mCustomView.findViewById(R.id.top_bar);
	    filterEventsTopBar.setBackgroundColor(Color.parseColor("#990380D7"));
	    
	    event_icon = (ImageView) findViewById(R.id.event_details_image);
	    event_title = (TextView) findViewById(R.id.event_details_title);
	    event_date_time_place = (TextView) findViewById(R.id.event_details_date_time_place);
	    event_summary = (TextView) findViewById(R.id.event_details_summary);
	    event_summary_title = (TextView) findViewById(R.id.event_details_summary_title);
	    event_description_title = (TextView) findViewById(R.id.event_details_description_title);
	    event_description = (TextView) findViewById(R.id.event_details_description);
	    
	    acRefresh = (ImageView) mCustomView.findViewById(R.id.ac_refresh);
	    acRefresh.setVisibility(View.VISIBLE);
	    acRefresh.setOnClickListener(this);
	    
	    Bundle extras = getIntent().getExtras();
		if (extras != null){
			entity_id = extras.getString("entity_id");
		  	
		  	ArrayList<String> passing = new ArrayList<String>();
	 		passing.add(entity_id);
	 		new EventDetailsAsyncTask().execute(passing);
		}    
	}
	
	private class EventDetailsAsyncTask extends AsyncTask<ArrayList<String>, Void, ArrayList<String>>{
		String image,status,title,date_time_place,event_summary,event_description;
		
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        prgDialog = new ProgressDialog(EventDetailsActivity.this);
	        prgDialog.setCancelable(true);
		    prgDialog.setMessage("Please wait...");
		    prgDialog.show();
	    }
		
		@Override
		protected ArrayList<String> doInBackground(ArrayList<String>... params) {
			ArrayList<String> result = new ArrayList<String>();
	        String entityId = params[0].get(0);
	        listEvent(entityId);
	 	 return null;
	 	}
		
	 	protected void onPostExecute(ArrayList<String> result){
	 		String[] eventDetails = {image,status,title,date_time_place,event_summary,event_description};
	 		loadEvent(eventDetails);
			prgDialog.dismiss();
		}
	 	
	 	public void listEvent(String entityId){
	 		String url = "http://ilq.isuf.co.uk/iloveqatar/android/_index.php";
	 		
	 		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
	         nameValuePair.add(new BasicNameValuePair("node",entityId));
	         nameValuePair.add(new BasicNameValuePair("method", "filterEventsByNode"));
	         
	         String json = new JSONParser().getJSONFromUrl(url, nameValuePair);
	         if (json != null) {
	                try {
	                	JSONArray jr = new JSONArray(json);
                         try {
                             JSONObject obj = jr.getJSONObject(0);
                             status = obj.getString("status").toString();
                             title = obj.getString("title").toString();
                             date_time_place = obj.getString("date_time_place").toString();
                             image = obj.getString("uri").toString();
                             event_summary = obj.getString("body_summary").toString();
                             event_description = obj.getString("body_value").toString();
                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
		               }
	                catch (JSONException e) {
	                    e.printStackTrace();
	                }
	            } else {
	                Log.e("ServiceHandler", "Couldn't get any data from the url");
	            } 
	 	}
	} 	
	
		public void loadEvent(String[] eventDetails){
		//image,status,title,date_time_place,event_summary,event_description
			if(eventDetails[0].equals("0")){
				utils.showAlert(EventDetailsActivity.this,"Error","Cant fetch the event details.try again");
				finish();
			}else{
				int loader = R.drawable.rounded_image_border;
		        imgLoader.DisplayImage(eventDetails[0], loader, event_icon);
		        event_title.setText(eventDetails[2]);
		        event_date_time_place.setText(eventDetails[3]);
		        if(!eventDetails[4].equals(" ")){
			        event_summary.setText(eventDetails[4]);
			        event_summary_title.setText("Summary");
		        }if(!eventDetails[5].equals("")){
		        	event_description_title.setText("Description");
		        	event_description.setText(eventDetails[5]);
		        }
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
				Intent intent = getIntent();
				finish();
				startActivity(intent);
			break;
	        default:
	        break;
        }
	}
}
