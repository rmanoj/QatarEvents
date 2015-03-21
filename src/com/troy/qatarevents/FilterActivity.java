package com.troy.qatarevents;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class FilterActivity extends Activity implements OnClickListener{
	ImageView backimage;
	TextView actionbartitle;
	TextView misc;
	TextView entertainment;
	TextView sport;
	TextView culture;
	TextView exhibition;
	TextView social;
	TextView night;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		 setContentView(R.layout.activity_filter);
		 
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayShowCustomEnabled(true);
	    LayoutInflater mInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
	    actionbar.setCustomView(mCustomView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	    backimage = (ImageView) mCustomView.findViewById(R.id.back);
	    actionbartitle = (TextView)mCustomView.findViewById(R.id.actionbar_title);
	    actionbartitle.setText("Filter");
	    backimage.setOnClickListener(this);
	    misc = (TextView) findViewById(R.id.misc);
	    misc.setOnClickListener(this);
	    
	    entertainment = (TextView) findViewById(R.id.entertainment);
	    entertainment.setOnClickListener(this);
	    
	    sport = (TextView) findViewById(R.id.sport);
	    sport.setOnClickListener(this);
	    
	    culture = (TextView) findViewById(R.id.culture);
	    culture.setOnClickListener(this);
	    
	    exhibition = (TextView) findViewById(R.id.exhibition);
	    exhibition.setOnClickListener(this);
	    
	    social = (TextView) findViewById(R.id.social);
	    social.setOnClickListener(this);
	    
	    night = (TextView) findViewById(R.id.night);
	    night.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
	        // Respond to the action bar's Up/Home button
	        case R.id.back:
	        	finish();
	        break;
	        case R.id.misc:
	        	startActivity(new Intent(this,FilterEventListActivity.class).putExtra("eventName", "Misc"));
	        	break;
	        case R.id.entertainment:
	        	startActivity(new Intent(this,FilterEventListActivity.class).putExtra("eventName", "Entertainment"));
	        	break;
	        case R.id.sport:
	        	startActivity(new Intent(this,FilterEventListActivity.class).putExtra("eventName", "Sport"));
	        	break;
	        case R.id.culture:
	        	startActivity(new Intent(this,FilterEventListActivity.class).putExtra("eventName", "Culture"));
	        	break;
	        case R.id.exhibition:
	        	startActivity(new Intent(this,FilterEventListActivity.class).putExtra("eventName", "exhibition"));
	        	break;
	        case R.id.social:
	        	startActivity(new Intent(this,FilterEventListActivity.class).putExtra("eventName", "Social"));
	        	break;
	        case R.id.night:
	        	startActivity(new Intent(this,FilterEventListActivity.class).putExtra("eventName", "Night"));
	        	break;	
	        default:
	        break;
        }
	}
}
