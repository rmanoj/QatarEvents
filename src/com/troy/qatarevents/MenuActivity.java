package com.troy.qatarevents;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class MenuActivity extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_menu);
	}
}
