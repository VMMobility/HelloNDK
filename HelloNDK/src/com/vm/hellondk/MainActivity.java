package com.vm.hellondk;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
		private TextView txtHello;
		
		// LogCat tag
		private static final String TAG = MainActivity.class.getSimpleName();
		
	 
	    // Camera activity request codes
	    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
	    
	    public static final int MEDIA_TYPE_IMAGE = 1;
	    public static final int MEDIA_TYPE_VIDEO = 2;
	 
	    private Uri fileUri; // file url to store image/video
	    
	    private Button btnCapturePicture, btnNativeImage;
	    
	    private byte[] imageByteArray;
	    
	    byte[] byteArray = null;

	// load the library - name matches jni/Android.mk
	static {
		System.loadLibrary("ndkfoo");
	}
	// declare the native code function - must match ndkfoo.c
	  private native String invokeNativeFunction();
	  
	  private native byte[] returnArray();
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
//		txtHello = (TextView)findViewById(R.id.txtHello);
		// this is where we call the native code
//		imageByteArray = invokeNativeFunction();//returnArray
		imageByteArray = returnArray();
		String status = "";
		try {
			status = new String(imageByteArray, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Toast.makeText(MainActivity.this, ""+ status,Toast.LENGTH_LONG).show(); 
//		Toast.makeText(getApplicationContext(), imageByteArray, Toast.LENGTH_LONG).show();
		//new AlertDialog.Builder(this).setMessage(hello).show();
		
//		btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);
		btnNativeImage = (Button) findViewById(R.id.btnNativePicture);
        
        /**
         * Capture image button click event
         */
        /*btnCapturePicture.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {
                // capture picture
                captureImage();
            }
        });*/
 
        /**
         * Record video button click event
         */
        btnNativeImage.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {
                // record video
                showPicture();
            }
        });
 
	}
	
	/**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
 
        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
 
        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }
    
    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                
            	// successfully captured the image
                // launching upload activity
            	launchUploadActivity(true);
            	
            	
            } else if (resultCode == RESULT_CANCELED) {
                
            	// user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        
        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                
            	// video successfully recorded
                // launching upload activity
            	launchUploadActivity(false);
            
            } else if (resultCode == RESULT_CANCELED) {
                
            	// user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();
            
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    
    
    private void launchUploadActivity(boolean isImage){
    	Intent i = new Intent(MainActivity.this, ImageActivity.class);
        i.putExtra("filePath", fileUri.getPath());
        i.putExtra("isImage", isImage);
        startActivity(i);
    }
    
    private void launchUploadActivity(byte[] imgByteArray){
    	
    	Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.img);
    	ByteArrayOutputStream stream = new ByteArrayOutputStream();
    	bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
    	byte[] mByteArray = stream.toByteArray();
    	System.out.println(Arrays.toString(mByteArray));
    	
    	Intent i = new Intent(MainActivity.this, ImageActivity.class);
        i.putExtra("img", imgByteArray);//byteArray
        startActivity(i);
    }
     
    /**
     * ------------ Helper Methods ---------------------- 
     * */
 
    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    
    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {
 
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);
 
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
 
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }
 
        return mediaFile;
    }

	/**
     * Launching camera app to capture image
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
 
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
 
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
 
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
    
    /**
     * Launching camera app to record video
     */
    private void showPicture() {
        /*Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
 
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
 
        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
 
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
                                                            // name
 
        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);*/
    	
    	launchUploadActivity(imageByteArray);
    }
	public native String messageFromNativeCode();

}