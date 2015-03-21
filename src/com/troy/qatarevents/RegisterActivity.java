package com.troy.qatarevents;


import java.io.ByteArrayOutputStream;
import java.io.File;
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

import com.troy.qatarevents.utils.ImageGetter;
import com.troy.qatarevents.utils.Utils;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.provider.MediaStore;
import android.util.Base64;
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
import com.troy.qatarevents.utils.Preferences;

public class RegisterActivity extends Activity implements OnClickListener{
	com.troy.qatarevents.utils.Preferences preferences;
	ImageView backimage,uploadimage;
	TextView actionbartitle;
	EditText userName,regEmail,regPassword,regVPassword;
	Button register;
	private String imagePath = "";
	String fileName;
	ProgressDialog prgDialog;
	String encodedString;
	Bitmap bitmap;
	JSONArray userDetails;
	
	String TAG_USERDETAILS = "userDetails";
	String TAG_USERID = "userid";
	String TAG_USERNAME = "username";
	String TAG_FIRSTNAME = "firstname";
	String TAG_LASTNAME = "lastname";
    String TAG_EMAIL = "email"; 
    String TAG_PICTURE = "picture";
    String TAG_MESSAGE = "message";
    String TAG_LOGINSTATUS = "login_status";
    public Utils utils;
    
    String uname;
	String email;
	String pass1;
	String pass2;
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_register);
		
		utils = new Utils();
		prgDialog = new ProgressDialog(this);
        // Set Cancelable as False
        prgDialog.setCancelable(true);
		
        preferences = new com.troy.qatarevents.utils.Preferences(getApplicationContext());
        
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayShowCustomEnabled(true);
	    
		LayoutInflater mInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
	    actionbar.setCustomView(mCustomView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	    
	    backimage = (ImageView) mCustomView.findViewById(R.id.back);
	    actionbartitle = (TextView)mCustomView.findViewById(R.id.actionbar_title);
	    actionbartitle.setText("Register");
	    backimage.setOnClickListener(this);
	    register = (Button) findViewById(R.id.register_yalla);
	    register.setOnClickListener(this);
	    
	    uploadimage = (ImageView) findViewById(R.id.click_image);
	    uploadimage.setOnClickListener(this);
	    
        userName = (EditText) findViewById(R.id.reg_username);
        regEmail = (EditText) findViewById(R.id.reg_email);
        regPassword = (EditText) findViewById(R.id.reg_password);
        regVPassword = (EditText) findViewById(R.id.reg_vpassword);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
        // Respond to the action bar's Up/Home button
        case R.id.back:
        finish();
        break;
        case R.id.register_yalla:
        	uname = userName.getText().toString();
        	email = regEmail.getText().toString();
    		pass1 = regPassword.getText().toString();
    		pass2 = regVPassword.getText().toString();
    		
    	if(isNetworkAvailable()){
    		if (uname.equals("") || pass1.equals("") || pass2.equals("")
					|| email.equals("") || imagePath.equals("")) {
				if (uname.equals("")) {
					utils.showToast("Username should not be empty",getApplicationContext());
				}else if (pass1.equals("")) {
					utils.showToast("Password should not be empty",getApplicationContext());
				}else if (pass2.equals("")){
					utils.showToast("Verify Password should not be empty",getApplicationContext());
				}else if (email.equals("")) {
					utils.showToast("Email should not be empty",getApplicationContext());
				}else if (imagePath.equals("")){
					utils.showToast("You must select image before try to register",getApplicationContext());
				}
			}else{
				File Img = new File(imagePath);
				long length = Img.length()/1024;
				 
				if(!utils.isValidEmail(email)){
					regEmail.setError("Invalid email");
				}else if(!utils.isValidUsername(uname)){
					utils.showToast("Invalid username",getApplicationContext());
				}else if(!utils.isValidPassword(pass1)){
					utils.showToast("Password should contain atleast 4 characters,1 letter,1 number and 1 special character",getApplicationContext());
				}else if(!pass1.equals(pass2)){
					utils.showToast("Password is not matching",getApplicationContext());
				}else if(length > 2048){
					utils.showToast("Image size should not be greater than 2MB",getApplicationContext());
				}else{

					prgDialog.setMessage("Please wait...");
			 		prgDialog.show();
			 		
	    	 		ArrayList<String> passing = new ArrayList<String>();
	    	 		passing.add(uname);
	    	 		passing.add(pass1);
	    	 		passing.add(email);
	    			new MyAsyncTask().execute(passing);
				}
			}
    	}else{
    		utils.showToast("Please check your network connection",getApplicationContext());
    	}
        break;
        case R.id.click_image:
        	getImage();
        break;
        default:
        	break;
     }
	}
	
	public boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}	
	
	private void getImage() {
		final String items[] = { "Camera", "Gallery", "Remove Image" };
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("Choose");
		ab.setCancelable(true);
		ab.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface d, int choice) {
				// 0 : Camera, 1 : Gallery
				switch (choice) {
				case Utils.CAMERA_PIC_REQUEST:
					Utils.imgCaptureUri = Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), "tmp_avatar_"
							+ String.valueOf(System.currentTimeMillis())
							+ ".jpg"));
					startActivityForResult(
							new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
									.putExtra(
											android.provider.MediaStore.EXTRA_OUTPUT,
											Utils.imgCaptureUri).putExtra(
											"return-data", true),
							Utils.CAMERA_PIC_REQUEST);
					break;
				case Utils.GALLERY_PIC_REQUEST:
					startActivityForResult(
							new Intent(
									Intent.ACTION_PICK,
									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
							Utils.GALLERY_PIC_REQUEST);
					break;
				case Utils.REMOVE_IMAGE:
					imagePath = "";
					uploadimage.setImageResource(R.drawable.camera);
					break;
				}
			}
		});
		ab.show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			try {
				String temp = "";
				switch (requestCode) {
				case Utils.CAMERA_PIC_REQUEST:
					if (Utils.imgCaptureUri != null) {
						temp = Utils.imgCaptureUri.getPath();
					}
					break;
				case Utils.GALLERY_PIC_REQUEST:
					if (data.getData() != null) {
						temp = Utils.getRealPathFromURI(data.getData(), this);
					}
					break;
				}
				Log.i("Path", temp);
				imagePath = temp;
				//uploadimage.setImageBitmap(Utils.getbitmap(imagePath, this));
				if(uploadimage.getTag() != null) {
				    ((ImageGetter) uploadimage.getTag()).cancel(true);
				}
				ImageGetter task = new ImageGetter(uploadimage) ;
				task.execute(new File(imagePath));
				uploadimage.setTag(task);
			} catch (Exception e) {
				Log.e("Error in getting image", e.getMessage().toString());
				e.printStackTrace();
			}
		}
	}
	
  private class MyAsyncTask extends AsyncTask<ArrayList<String>, Void, ArrayList<String>>{
            	 
	@Override
	protected ArrayList<String> doInBackground(ArrayList<String>... params) {
		
		ArrayList<String> result = new ArrayList<String>();
        String Puname = params[0].get(0);
        String Ppass  = params[0].get(1);
        String Pmail  = params[0].get(2);
        
		String fileNameSegments[] = imagePath.split("/");
        fileName = fileNameSegments[fileNameSegments.length - 1];
		
		// TODO Auto-generated method stub
		BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        bitmap = BitmapFactory.decodeFile(imagePath,
                options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream); 
        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        encodedString = Base64.encodeToString(byte_arr, 0);
        
		postData(Puname, Ppass, Pmail, encodedString, fileName);
		
 	 return null;
 	}
	
 	protected void onPostExecute(ArrayList<String> result){
 	   //showToast("Comment sent");
	}
  
	public void postData(String Puname,String Ppass,String Pmail,String Pimage,String Pfilename) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://ilq.isuf.co.uk/iloveqatar/android/index.php");
  
 		try {
 				// Add your data
 		 // create a list to store HTTP variables and their values
		 List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
         nameValuePair.add(new BasicNameValuePair("username",Puname));
         nameValuePair.add(new BasicNameValuePair("password",Ppass));
         nameValuePair.add(new BasicNameValuePair("email",Pmail));
         nameValuePair.add(new BasicNameValuePair("image",Pimage));
         nameValuePair.add(new BasicNameValuePair("filename",Pfilename));
         nameValuePair.add(new BasicNameValuePair("method", "register"));
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
		                	   
		                	   String Remail = ud.getString(TAG_EMAIL);
		                	   String Rcode = ud.getString("code");
		                	   
		                	   String RuserId = ud.getString(TAG_USERID);
		                	   String RuserName = ud.getString(TAG_USERNAME);
		                	   String RfirstName = ud.getString(TAG_FIRSTNAME);
		                	   String RlastName = ud.getString(TAG_LASTNAME);
		                	   String Rpicture = ud.getString(TAG_PICTURE);
		                	   
		                	   String Rmessage = ud.getString(TAG_MESSAGE);
				               String RuserRegisterSuccess = "200";
				               String Runknown = "500";
				               
				               String emailFound = ud.getString("email");
		                	   String usernameFound = ud.getString("username");
		                		
		                		if(emailFound.equals("0") && usernameFound.equals("0")){
		                			//both are found
		                			showAlert("Error", Rmessage);
		                		}else if(emailFound.equals(0) && !usernameFound.equals(0)){
		                			//email found
		                			showAlert("Error", Rmessage);
		                		}else if(!emailFound.equals(0) && usernameFound.equals(0)){
		                			//username found
		                			showAlert("Error", Rmessage);
		                		}else if(!emailFound.equals(0) && !usernameFound.equals(0)){
		                			//both are not found
		                			String[] myStrings = new String[] {RuserName,Remail,
			                	    		RfirstName,RlastName,RuserId,Rpicture};
                                    preferences.createUserLoginSession(myStrings);
			                    	startActivity(new Intent(RegisterActivity.this, EventsActivity.class));
		                			//showAlert("Error4", Rmessage);
		                		}else if(Rcode.equals(Runknown)){
			                    	showAlert("Error",Rmessage);
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
			Log.e("Register Client Protocol Exception",e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("Register IOException",e.toString());
		}
	}
	
	 public void showAlert(final String title,final String message){
	        RegisterActivity.this.runOnUiThread(new Runnable() {
	            public void run() {
	                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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
}
}
