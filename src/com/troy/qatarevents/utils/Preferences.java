package com.troy.qatarevents.utils;

import com.troy.qatarevents.EventsActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class Preferences {

	private SharedPreferences sprefs;

	public String USER_ID = "user_id";
	public String FIRST_NAME = "first_name";
	public String LAST_NAME = "last_name";
	public String EMAIL_ADDRESS = "email_address";
	public String PICTURE = "picture";
	public String NOTIFICATION = "notification";
	public String NOTIFICATION_AUDIO = "notification_audio";
	public String AUTO_LOGIN = "auto_login";
	public String USERNAME = "username";
	public String PASSWORD = "password";
	public String SCHEDULE = "schedule";
	public String NOTIFICATION_TIME = "notification_time";
	public static final String MyPREFERENCES = "MyPrefs" ;
	Context _context;
	
//	public static String REMEMBER_ME = "remember_me";
//	public static String PATIENT_ID = "patient_id";
//	public static String PATIENT_NAME = "patient_name";
//	
//	public static String SELECTED_DP = "selected_dose_position";
//	public static String DISCLAIMER = "disclaimer_read";
//	public static String ENTERED_USERNAME = "entered_username";
//	public static String ENTERED_PASSWORD = "entered_password";
	
	public Preferences(Context ctx) {
		sprefs = ctx.getSharedPreferences(MyPREFERENCES, 0);
	}

	public void setInt(String key, int value) {
		Editor editor = sprefs.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public void setBoolean(String key, boolean value) {
		Editor editor = sprefs.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void setString(String key, String value) {
		Editor editor = sprefs.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void setFloat(String key, float value) {
		Editor editor = sprefs.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	public void setLong(String key, long value) {
		Editor editor = sprefs.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public int getInt(String key) {
		return sprefs.getInt(key, -1);
	}

	public boolean getBoolean(String key) {
		return sprefs.getBoolean(key, false);
	}

	public String getString(String key) {
		return sprefs.getString(key, "");
	}

	public float getFloat(String key) {
		return sprefs.getFloat(key, -1.0f);
	}

	public long getLong(String key) {
		return sprefs.getLong(key, -1);
	}
	
	public void removeKey(String key){
		Editor editor = sprefs.edit();
		editor.remove(key);
		editor.commit();
	}
	
	public void removeAll(){
		Editor editor = sprefs.edit();
		editor.clear();
		editor.commit();
	}
	
	 public void createUserLoginSession(String[] loginSession){
		    this.setString(this.USERNAME, loginSession[0]);
		    this.setString(this.EMAIL_ADDRESS, loginSession[1]);
        	this.setString(this.FIRST_NAME, loginSession[2]);
        	this.setString(this.LAST_NAME, loginSession[3]);        	
        	this.setString(this.USER_ID, loginSession[4]);
        	this.setString(this.PICTURE, loginSession[5]);
        	this.setBoolean(this.AUTO_LOGIN,true);
     }
	 
	 public boolean checkLogin(){
         // Check login status
         if(this.isUserLoggedIn()){
             /*// user is  logged in redirect him to Events Activity
             Intent i = new Intent(_context, EventsActivity.class);
              
             // Closing all the Activities from stack
             i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              
             // Add new Flag to start new Activity
             i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              
             // Staring Login Activity
             _context.startActivity(i);*/
              
             return true;
         }
         return false;
     }
	 
     // Check for login
     public boolean isUserLoggedIn(){
         return this.getBoolean(AUTO_LOGIN);
     }
}
