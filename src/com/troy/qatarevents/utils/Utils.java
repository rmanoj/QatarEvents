package com.troy.qatarevents.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.troy.qatarevents.utils.imagefromurl.ImageLoader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class Utils extends Activity {
	
	public static final int CAMERA_PIC_REQUEST = 0;
	public static final int GALLERY_PIC_REQUEST = 1;
	public static final int REMOVE_IMAGE = 2;
	public static Uri imgCaptureUri = null;
	String USERNAME_PATTERN = "[a-zA-Z]+";
    String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-zA-Z])(?=.*[~'!@#$%?\\\\/&*\\]|\\[=()}\"{+_:;,.><'-])).{4,15}";
    Pattern pattern;
    Matcher matcher;
    
    ImageLoader imgLoader;
	@SuppressWarnings("deprecation")
	public static String getRealPathFromURI(Uri contentUri, Context ctx) {
		try {
			final Activity activity = (Activity) ctx;
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = activity.managedQuery(contentUri, proj, null, null,
					null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			Log.e("Error in getting Real Path of Image", e.getMessage()
					.toString());
			return contentUri.getPath();
		}
	}
	
	public static Bitmap getNormalizedBitmap(Bitmap bitmap, String path) {
		try {
			ExifInterface ei = new ExifInterface(path);
			int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				bitmap = Utils.RotateBitmap(bitmap, 90);
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				bitmap = Utils.RotateBitmap(bitmap, 180);
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				bitmap = Utils.RotateBitmap(bitmap, 270);
				break;
			}
		} catch (Exception e) {

		}
		return bitmap;
	}
	
	public static Bitmap RotateBitmap(Bitmap source, float angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
				source.getHeight(), matrix, true);
	}
	

	public static Bitmap getbitmap(String path, Context context) {
		Bitmap imgthumBitmap = null;
		try {
			final int THUMBNAIL_WIDTH = 150;
			final int THUMBNAIL_HEIGHT = 150;

			FileInputStream fis = new FileInputStream(path);
			imgthumBitmap = BitmapFactory.decodeStream(fis);

			imgthumBitmap = Bitmap.createScaledBitmap(imgthumBitmap,
					THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, false);
			imgthumBitmap = Utils.getNormalizedBitmap(imgthumBitmap, path);
			ByteArrayOutputStream bytearroutstream = new ByteArrayOutputStream();
			imgthumBitmap.compress(Bitmap.CompressFormat.PNG, 100,
					bytearroutstream);

		} catch (Exception ex) {

		}
		return imgthumBitmap;
	}
	
	
	 public static Bitmap decodeFile(File f,int WIDTH,int HIGHT){
		 try {
		     //Decode image size
		     BitmapFactory.Options o = new BitmapFactory.Options();
		     o.inJustDecodeBounds = true;
		     BitmapFactory.decodeStream(new FileInputStream(f),null,o);

		     //The new size we want to scale to
		     final int REQUIRED_WIDTH=WIDTH;
		     final int REQUIRED_HIGHT=HIGHT;
		     //Find the correct scale value. It should be the power of 2.
		     int scale=1;
		     while(o.outWidth/scale/2>=REQUIRED_WIDTH && o.outHeight/scale/2>=REQUIRED_HIGHT)
		         scale*=2;

		     //Decode with inSampleSize
		     BitmapFactory.Options o2 = new BitmapFactory.Options();
		     o2.inSampleSize=scale;
		     return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		 } catch (FileNotFoundException e) {}
		 return null;
		}
	 
	 public void showAlert(final Activity context,final String title,final String message){
	        ((Activity) context).runOnUiThread(new Runnable() {
	            public void run() {
	                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
	 
	public void showToast(String message,Context context){
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	} 
	
	public Boolean isNetworkAvailable(Activity mContext) {
		  boolean haveConnectedWifi = false;
		  boolean haveConnectedMobile = false;
		  ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		  for (NetworkInfo ni : netInfo) {
		   if (ni.getTypeName().equalsIgnoreCase("WIFI"))
		    if (ni.isConnected())
		     haveConnectedWifi = true;
		   if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
		    if (ni.isConnected())
		     haveConnectedMobile = true;
		  }
		  return haveConnectedWifi || haveConnectedMobile;
		 }
	
	// validating email id
	public boolean isValidEmail(String email) {
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	public boolean isValidUsername(String username) {
		pattern = Pattern.compile(USERNAME_PATTERN);
		matcher = pattern.matcher(username);
		return matcher.matches();
		//PASSWORD_PATTERN
	}
	
	public boolean isValidPassword(String password) {
		pattern = Pattern.compile(PASSWORD_PATTERN);
		matcher = pattern.matcher(password);
		return matcher.matches();
	}
	
	public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
	
	public ImageLoader getImageLoader(){
		if (imgLoader == null) {
			imgLoader = new ImageLoader(getApplicationContext());
        }
        return this.imgLoader;
	}
	
	public Bitmap getBitmapFromURL(String src) {
	    try {
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
}
