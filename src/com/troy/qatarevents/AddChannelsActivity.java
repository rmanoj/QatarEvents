package com.troy.qatarevents;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class AddChannelsActivity extends Activity implements OnClickListener{
	ImageView backimage;
	TextView actionbartitle;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		 setContentView(R.layout.activity_add_channels);
		
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayShowCustomEnabled(true);
	    LayoutInflater mInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
	    actionbar.setCustomView(mCustomView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	    backimage = (ImageView) mCustomView.findViewById(R.id.back);
	    actionbartitle = (TextView)mCustomView.findViewById(R.id.actionbar_title);
	    actionbartitle.setText("Add Channels");
	    backimage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
	        // Respond to the action bar's Up/Home button
	        case R.id.back:
	        	finish();
	        break;
	        default:
	        break;
        }
	}
}
