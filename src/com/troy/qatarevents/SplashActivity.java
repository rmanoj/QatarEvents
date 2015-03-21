package com.troy.qatarevents;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.troy.qatarevents.utils.Preferences;
import com.troy.qatarevents.utils.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends FragmentActivity implements OnClickListener {
	private TextView signup_i;
	private TextView signup_q;
	private TextView poweredby;
	private TextView ahalogin;
	private Typeface custom_font_i;
	private Typeface custom_font_q;
	private Typeface custom_font_poweredby;
	private Typeface custom_font_ahalogin;
	private Button registerby_email;
	private LoginButton loginButton;
	private GraphUser user;
	private UiLifecycleHelper uiHelper;
	public com.troy.qatarevents.utils.Preferences preferences;
	Utils utils;
	
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            Log.d("SplashActivity", String.format("Error: %s", error.toString()));
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Log.d("SplashActivity", "Success!");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        preferences = new Preferences(getApplicationContext());
        utils = new Utils();
        
        fontStyle(); 

        loginButton = (LoginButton) findViewById(R.id.facebook_login);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));
        loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                SplashActivity.this.user = user;
                updateUI();
            }
        });
        
        
        if(preferences.checkLogin()){
        	startActivity(new Intent(this,EventsActivity.class));
        	finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();

        updateUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);

        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (
                (exception instanceof FacebookOperationCanceledException ||
                exception instanceof FacebookAuthorizationException)) {
                new AlertDialog.Builder(SplashActivity.this)
                    .setTitle(R.string.cancelled)
                    .setMessage(R.string.permission_not_granted)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    }

    private void updateUI() {
        Session session = Session.getActiveSession();
        boolean enableButtons = (session != null && session.isOpened());
        
        if (enableButtons && user != null) {
        	//finish();
        	String[] myStrings = new String[] {user.asMap().get("email").toString(),user.getFirstName(), user.getLastName(),user.getId()};
        	//startActivity(new Intent(this,EventsActivity.class).putExtra("userDetails", myStrings));
        	preferences.setString(preferences.USERNAME, myStrings[1]+" "+myStrings[2]);
        	preferences.setString(preferences.EMAIL_ADDRESS, myStrings[0]);
        	preferences.setString(preferences.FIRST_NAME, myStrings[1]);
        	preferences.setString(preferences.LAST_NAME, myStrings[2]);        	
        	preferences.setString(preferences.USER_ID, myStrings[3]);
        	
        	/*ArrayList<String> passing = new ArrayList<String>();
	 		passing.add(myStrings[0]);
			new CheckMail().execute(passing);*/
        	startActivity(new Intent(this,EventsActivity.class));
        	finish();
        }
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.aha_login:
			startActivity(new Intent(this, SignInActivity.class));
			//finish();
			break;
		case R.id.registerby_email:
			startActivity(new Intent(this, RegisterActivity.class));
			//finish();
			break;
		default:
			break;
		}
	}

	private class CheckMail extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {
	    

	    @Override
	    protected ArrayList<String> doInBackground(ArrayList<String>... params) {
	    	ArrayList<String> result = new ArrayList<String>();
	        String email = params[0].get(0);
	        Log.e("EMAIL", email);
	    	checkMail(email);
	    	return null;
	    }

	    protected void onPostExecute(ArrayList<String> result){
	  	   //showToast("Comment sent");
	 	}

	}
	
	public void checkMail(String email){
		// Create a new HttpClient and Post Header
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://ilq.isuf.co.uk/iloveqatar/android/index.php");
		  
		 		try {
		 			Log.e("EMAIL", email);
		 				// Add your data
		 		 // create a list to store HTTP variables and their values
				 List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
				 nameValuePair.add(new BasicNameValuePair("email", email));
		         nameValuePair.add(new BasicNameValuePair("method", "checkmail"));
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
			                	   JSONObject jb = (JSONObject)jr.getJSONObject(0);
			                	   String mail = jb.getString("email");
			                	   String status = jb.getString("status");
			                	   if(status.equals(1)){
			                		   //redirect to EventsActivity
			                		   utils.showToast(mail+" is already registered", getApplicationContext());
			                	   }else if (status.equals(0)) {
			                		   //redirect to SetPasswordActivity
			                		   utils.showToast(mail+" is not registered", getApplicationContext()); 
			                	   }else if(status.equals("failed")){
			                		   utils.showToast("Something went wrong.please try again later", getApplicationContext());
			                	   }
			                	   
			                } catch (JSONException e) {
			                    e.printStackTrace();
			                }
			            } else {
			                Log.e("ServiceHandler", "Couldn't get any data from the url");
			            } 
					   			   
					}else{
						utils.showAlert(SplashActivity.this,"Error","Something went wrong try again later.");
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					Log.e("Register Client Protocol Exception",e.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.e("Register IOException",e.toString());
				}
	}
	
	public void fontStyle(){
		ImageView image = (ImageView) findViewById(R.id.qatar_bg);
		Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.signupbg);
        image.setImageBitmap(bMap);
        
		signup_i = (TextView) findViewById(R.id.signup_i);
		signup_i.setText("I");
		custom_font_i = Typeface.createFromAsset(getAssets(), "fonts/century-gothic.ttf");
		signup_i.setTypeface(custom_font_i,Typeface.BOLD);
		
		signup_q = (TextView) findViewById(R.id.signup_q);
		signup_q.setText("Q");
		custom_font_q = Typeface.createFromAsset(getAssets(), "fonts/century-gothic.ttf");
		signup_q.setTypeface(custom_font_q,Typeface.BOLD);
		
		poweredby = (TextView) findViewById(R.id.poweredby);
		poweredby.setText("Powered by");
		custom_font_poweredby = Typeface.createFromAsset(getAssets(), "fonts/AvenirLTStd-Light.otf");
		poweredby.setTypeface(custom_font_poweredby,Typeface.BOLD);
		
		ahalogin = (TextView) findViewById(R.id.aha_login);
		ahalogin.setText("Already have an account? Log In.");
		custom_font_ahalogin = Typeface.createFromAsset(getAssets(), "fonts/AvenirLTStd-Light.otf");
		ahalogin.setTypeface(custom_font_ahalogin,Typeface.BOLD);
		
		registerby_email = (Button)findViewById(R.id.registerby_email);
		ahalogin.setOnClickListener(this);
		registerby_email.setOnClickListener(this);
	}
	
	/*public static String printKeyHash(Activity context) {
		PackageInfo packageInfo;
		String key = null;
		try {
			//getting application package name, as defined in manifest
			String packageName = context.getApplicationContext().getPackageName();

			//Retriving package info
			packageInfo = context.getPackageManager().getPackageInfo(packageName,
					PackageManager.GET_SIGNATURES);
				
			Log.e("Package Name=", context.getApplicationContext().getPackageName());
			
			for (Signature signature : packageInfo.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				key = new String(Base64.encode(md.digest(), 0));
			
				// String key = new String(Base64.encodeBytes(md.digest()));
				Log.e("Key Hash=", key);
			}
		} catch (NameNotFoundException e1) {
			Log.e("Name not found", e1.toString());
		}
		catch (NoSuchAlgorithmException e) {
			Log.e("No such an algorithm", e.toString());
		} catch (Exception e) {
			Log.e("Exception", e.toString());
		}

		return key;
	}*/

}
