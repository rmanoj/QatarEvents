package com.troy.qatarevents;


import java.io.IOException;
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

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.troy.qatarevents.utils.Utils;
import com.troy.qatarevents.utils.Preferences;

public class SignInActivity extends Activity implements OnClickListener{
	ImageView backimage;
	Button bt_login;
	TextView actionbartitle;
	Utils utils;
	Preferences preferences;
	
	String TAG_USERDETAILS = "userDetails";
	String TAG_USERID = "userid";
	String TAG_USERNAME = "username";
	String TAG_FIRSTNAME = "firstname";
	String TAG_LASTNAME = "lastname";
    String TAG_EMAIL = "email"; 
    String TAG_PICTURE = "picture";
    String TAG_MESSAGE = "message";
    String TAG_LOGINSTATUS = "login_status";
    
	ProgressDialog prgDialog;
	EditText username;
	EditText password;
	String uname;
	String email;
	String pass;
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_signin);
		
		utils = new Utils();
		prgDialog = new ProgressDialog(this);
        // Set Cancelable as False
        prgDialog.setCancelable(true);
        
        preferences = new Preferences(getApplicationContext());
		
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayShowCustomEnabled(true);
	    LayoutInflater mInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
	    actionbar.setCustomView(mCustomView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	    backimage = (ImageView) mCustomView.findViewById(R.id.back);
	    actionbartitle = (TextView)mCustomView.findViewById(R.id.actionbar_title);
	    actionbartitle.setText("Sign In");
	    backimage.setOnClickListener(this);
	    bt_login = (Button) findViewById(R.id.bt_login);
	    bt_login.setOnClickListener(this);
	    
	    username = (EditText) findViewById(R.id.emailText);
	    password = (EditText) findViewById(R.id.passwordText);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
        // Respond to the action bar's Up/Home button
        case R.id.back:
        finish();
        break;
        case R.id.bt_login:
        	uname = username.getText().toString();
        	pass = password.getText().toString();
        	if(utils.isNetworkAvailable(SignInActivity.this)){
	        	if (uname.equals("") || pass.equals("")) {
					if (uname.equals("")) {
						utils.showToast("Please enter username",getApplicationContext());
					}else if (pass.equals("")) {
						utils.showToast("Please enter password",getApplicationContext());
					}
	        	}else{
	        		prgDialog.setMessage("Please wait...");
			 		prgDialog.show();
			 		
	    	 		ArrayList<String> passing = new ArrayList<String>();
	    	 		passing.add(uname);
	    	 		passing.add(pass);
	    			new SignAsyncTask().execute(passing);
	        		//loginDoha();
	        	}
        	}else{
        		utils.showAlert(SignInActivity.this, "Info", "Please check your network connection");
        	}
			//startActivity(new Intent(this,MenuActivity.class));
			break;
        default:
        	break;
     }
	}
	
	private class SignAsyncTask extends AsyncTask<ArrayList<String>, Void, ArrayList<String>>{
   	 
		@Override
		protected ArrayList<String> doInBackground(ArrayList<String>... params) {
			
			ArrayList<String> result = new ArrayList<String>();
	        String Puname = params[0].get(0);
	        String Ppass  = params[0].get(1);
	        loginDoha(Puname, Ppass);
	 	 return null;
	 	}
		
	 	protected void onPostExecute(ArrayList<String> result){
	 	   //showToast("Comment sent");
		}
	  
		public void loginDoha(String Puname,String Ppass) {
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://ilq.isuf.co.uk/iloveqatar/android/index.php");
	  
	 		try {
	 				// Add your data
	 		 // create a list to store HTTP variables and their values
			 List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
	         nameValuePair.add(new BasicNameValuePair("username",Puname));
	         nameValuePair.add(new BasicNameValuePair("password",Ppass));
	         nameValuePair.add(new BasicNameValuePair("method", "login"));
	 		 httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
	  
	 				// Execute HTTP Post Request
	 		 	HttpResponse response = httpclient.execute(httppost);
				if (response.getStatusLine().getStatusCode() == 200)
				{
			 		prgDialog.dismiss();
				    HttpEntity entity = response.getEntity();
				    String json = EntityUtils.toString(entity);
				    System.out.println("Web service : "+json);
				   
				    if (json != null) {
		                try {
			                	   JSONArray jr = new JSONArray(json);
			                	   JSONObject jb = (JSONObject)jr.getJSONObject(0);
			                	   JSONObject ud = (JSONObject)jb.getJSONObject(TAG_USERDETAILS);
			                	   
			                	   String RuserId = ud.getString(TAG_USERID);
			                	   String RuserName = ud.getString(TAG_USERNAME);
			                	   String RfirstName = ud.getString(TAG_FIRSTNAME);
			                	   String RlastName = ud.getString(TAG_LASTNAME);
			                	   String Remail = ud.getString(TAG_EMAIL);
			                	   String Rpicture = ud.getString(TAG_PICTURE);
			                	   String Rmessage = ud.getString(TAG_MESSAGE);
			                	   String RloginStatus = ud.getString(TAG_LOGINSTATUS);
			                	   
			                	   if(RloginStatus.equals("0")){
			                		   showAlert("Error", Rmessage);
			                	   }else if(RloginStatus.equals("1")){
			                	    String[] myStrings = new String[] {RuserName,Remail,
			                	    		RfirstName,RlastName,RuserId,Rpicture};
                                    preferences.createUserLoginSession(myStrings);
			                       	finish();
			                       	startActivity(new Intent(SignInActivity.this,EventsActivity.class));
			                		//   showAlert("Success", Rmessage);
			                	   }
			                	   
		                } catch (JSONException e) {
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
        SignInActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
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
	
	/*public boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}*/
}
