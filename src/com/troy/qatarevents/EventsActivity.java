package com.troy.qatarevents;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.facebook.Session;
import com.facebook.widget.ProfilePictureView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.troy.qatarevents.asynctask.ListEventsAsyncTask;
import com.troy.qatarevents.utils.Preferences;
import com.troy.qatarevents.utils.Utils;
import com.troy.qatarevents.utils.imagefromurl.ImageLoader;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

@SuppressLint("NewApi")
public class EventsActivity extends FragmentActivity implements OnClickListener{
	com.troy.qatarevents.utils.Preferences preferences;
	private SlidingMenu slidingMenu ;
	ImageView backimage;
	TextView actionbartitle;
	TextView username;
	ProfilePictureView profilePictureView;
	Button settings;
	Button search;
	ImageView acImg,acSearch,acRefresh;
	TextView acTxt;
	TabHost tabHost;
	Utils utils;
	//ArrayList<String> passing;
	//String sortBy;
	@SuppressWarnings("deprecation")
	LocalActivityManager mLocalActivityManager;
	
	private static final String TODAY_SPEC = "Today";
    private static final String THIS_WEEK_SPEC = "This Week";
    private static final String LATER_SPEC = "Later";
    static int selectTab;
    
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_events);
		
		 utils = new Utils();
		 initTabView(savedInstanceState);
	        
		
		slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.activity_menu);
		
        preferences = new Preferences(getApplicationContext());
        
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayShowCustomEnabled(true);
	    LayoutInflater mInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
	    backimage = (ImageView) mCustomView.findViewById(R.id.back);
	    actionbar.setCustomView(mCustomView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	    actionbartitle = (TextView)mCustomView.findViewById(R.id.actionbar_title);
	    backimage.setImageResource(R.drawable.toggler);
	    backimage.setOnClickListener(this);
	    actionbartitle.setText("Events");
	
	    profilePictureView = (ProfilePictureView) findViewById(R.id.profile_image);
	    settings = (Button) findViewById(R.id.settings);
	    search = (Button) findViewById(R.id.search);
	    acRefresh = (ImageView) findViewById(R.id.ac_refresh);
	    acSearch = (ImageView) findViewById(R.id.ac_search); 
	    acImg = (ImageView) findViewById(R.id.ac_icon);
	    acTxt = (TextView) findViewById(R.id.ac_text);
	    username = (TextView) findViewById(R.id.user_name);
		
	    /*Bundle extras = getIntent().getExtras();
	    if (extras != null)
	    {
	    	  String[] myStrings = extras.getStringArray("userDetails");
			profilePictureView.setProfileId(myStrings[2]);
			username.setText(myStrings[0]+" "+myStrings[1]);
	    }*/
	    if(preferences.getString(preferences.USERNAME)!=null){
	    	username.setText(preferences.getString(preferences.USERNAME));
	    }
	    if(preferences.getString(preferences.USER_ID)!=null){
	    	profilePictureView.setProfileId(preferences.getString(preferences.USER_ID));
	    }
	    if(preferences.getString(preferences.PICTURE)!=null){
	    	int loader = R.drawable.rounded_image_border;
	    	
	    	ImageView image= (ImageView) findViewById(R.id.doha_profile_image);
	    	image.setVisibility(View.VISIBLE);
	    	
	        // Image url
	        String image_url = "http://ilq.isuf.co.uk/iloveqatar/android/profile_pictures/"+preferences.getString(preferences.PICTURE);
	         
	        // ImageLoader class instance
	        ImageLoader imgLoader = new ImageLoader(getApplicationContext());
	         
	        // whenever you want to load an image from url
	        // call DisplayImage function
	        // url - image url to load
	        // loader - loader image, will be displayed before getting image
	        // image - ImageView 
	        imgLoader.DisplayImage(image_url, loader, image);
	    }
		settings.setOnClickListener(this);
		search.setOnClickListener(this);
		acTxt.setOnClickListener(this);
		acImg.setOnClickListener(this);
		acSearch.setVisibility(View.VISIBLE);
		acRefresh.setVisibility(View.VISIBLE);
		acSearch.setOnClickListener(this);
		acRefresh.setOnClickListener(this);
	}
	
	@SuppressWarnings("deprecation")
	public void initTabView(Bundle savedInstanceState){
		tabHost = (TabHost) findViewById(android.R.id.tabhost); //here tabHost will be your Tabhost
        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState); // state will be bundle your activity state which you get in onCreate
        tabHost.setup(mLocalActivityManager);
       
        
        tabHost.addTab(createTab(TodayActivity.class,"today","Today"));
        tabHost.addTab(createTab(ThisWeekActivity.class,"thisweek","This Week"));
        tabHost.addTab(createTab(LaterActivity.class,"later","Later"));
        tabHost.setCurrentTab(0);
        
        tabHost.getTabWidget().getChildAt(selectTab).setBackgroundResource(R.color.gray);
        //final int tabCount = tabHost.getChildCount();
		 tabHost.setOnTabChangedListener(new OnTabChangeListener() {
					
					@Override
					public void onTabChanged(String tabId) {
						// TODO Auto-generated method stub
						utils.showToast(tabId, getApplicationContext());
						//tabHost.setCurrentTab(selectTab);
						//tabHost.getTabWidget().getChildAt(selectTab).setBackgroundResource(R.color.gray);
					}
				});
    	
	}
	
	private TabSpec createTab(final Class<?> intentClass, final String tag,final String title){
        final Intent intent = new Intent().setClass(this, intentClass);
        final View tab = LayoutInflater.from(tabHost.getContext()).inflate(R.layout.tab_layout, null);
        ((TextView)tab.findViewById(R.id.bt_tag)).setText(title);

        return tabHost.newTabSpec(tag).setIndicator(tab).setContent(intent);
    }	
	
	/*@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mLocalActivityManager.dispatchResume();
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		selectTab = 0;
	}
	@Override
	protected void onRestart() {
		selectTab = tabHost.getCurrentTab();
		Intent intent = getIntent();
		finish();
		startActivity(intent);
		super.onRestart();
	}*/
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
        // Respond to the action bar's Up/Home button
		case R.id.back:
			/*showToast("Click settings icon to Logout");*/
			listChannels();
			this.slidingMenu.toggle();
	        break;
		case R.id.bt_login:
			startActivity(new Intent(this,MenuActivity.class));
			break;
		case R.id.settings:
			if (Session.getActiveSession() != null) {
			    Session.getActiveSession().closeAndClearTokenInformation();
			}
			Session.setActiveSession(null);
			startActivity(new Intent(this,SplashActivity.class));
			finish();
			preferences.removeAll();
			break;
		case R.id.search:
			startActivity(new Intent(this,FilterActivity.class));
			break;
		case R.id.ac_search:
			startActivity(new Intent(this,FilterActivity.class));
			break;	
		case R.id.ac_icon:
			startActivity(new Intent(this,AddChannelsActivity.class));
			break;
		case R.id.ac_refresh:
			selectTab = tabHost.getCurrentTab();
			Intent intent = getIntent();
			finish();
			startActivity(intent);
			break;
        default:
        	break;
     }
	}
	
	 
     public void onBackPressed() {
		 if ( slidingMenu.isMenuShowing()) {
	            slidingMenu.toggle();
	        }else{
	        	new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
		         .setMessage("Are you sure you want to exit?")
		         .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		             @Override
		             public void onClick(DialogInterface dialog, int which) {
		                 //finish();
		            	 Intent intent = new Intent(Intent.ACTION_MAIN);
		                 intent.addCategory(Intent.CATEGORY_HOME);
		                 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		                 startActivity(intent);
		                 finishAffinity();
		                 System.exit(0);
		             }
		         }).setNegativeButton("No", null).show();
	        }
	 }
	 
	 public void listChannels(){
		 String[] channel_name = new String[] {
			        "Katara",
			        "Qatar Foundation",
			        "Red Bull"
			    };
			 
			    // Array of integers points to images stored in /res/drawable-ldpi/
			    int[] channel_icon = new int[]{
			        R.drawable.katara,
			        R.drawable.qatar_f,
			        R.drawable.redbull
			    };
			    
			    List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
			    
		        for(int i=0;i<3;i++){
		            HashMap<String, String> hm = new HashMap<String,String>();
		            hm.put("c_name", channel_name[i]);
		            hm.put("c_icon", Integer.toString(channel_icon[i]) );
		            aList.add(hm);
		        }
		 
		        // Keys used in Hashmap
		        String[] from = { "c_name","c_icon" };
		 
		        // Ids of views in listview_layout
		        int[] to = { R.id.c_name,R.id.c_icon};
		 
		        // Instantiating an adapter to store each items
		        // R.layout.listview_layout defines the layout of each item
		        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listchannel_layout, from, to);
		 
		        // Getting a reference to listview of main.xml layout file
		        ListView listView = ( ListView ) findViewById(R.id.list_channels);
		 
		        // Setting the adapter to the listView
		        listView.setAdapter(adapter);
	 }
}
