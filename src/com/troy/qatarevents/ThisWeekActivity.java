package com.troy.qatarevents;

import java.util.ArrayList;
import com.troy.qatarevents.asynctask.ListEventsAsyncTask;
import com.troy.qatarevents.utils.Utils;
import android.app.Activity;
import android.os.Bundle;

public class ThisWeekActivity extends Activity {
	String sortBy;
	String colorCode;
	Utils utils;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.sort_event_list);
		 
		    sortBy = "thisweek";
		    ArrayList<String> passing = new ArrayList<String>();
	 		passing.add(sortBy);
		    new ListEventsAsyncTask(ThisWeekActivity.this).execute(passing);
	}
}
