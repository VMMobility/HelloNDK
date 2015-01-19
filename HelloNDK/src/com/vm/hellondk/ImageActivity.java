package com.vm.hellondk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class ImageActivity extends Activity {
	// LogCat tag
	private static final String TAG = MainActivity.class.getSimpleName();

	private ProgressBar progressBar;
	private String filePath = null;
	private String imgbteArray = null;
	private TextView txtPercentage;
	private ImageView imgPreview;
	private Button btnUpload;
	long totalSize = 0;
	byte[] byteArray ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);
		txtPercentage = (TextView) findViewById(R.id.txtPercentage);
//		btnUpload = (Button) findViewById(R.id.btnUpload);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		imgPreview = (ImageView) findViewById(R.id.imgPreview);



		// Receiving the data from previous activity
		Intent i = getIntent();

		// image or video path that is captured in previous activity
		filePath = i.getStringExtra("filePath");
		byteArray = i.getByteArrayExtra("img");
		String stringByteArray = byteArray.toString();
		Toast.makeText(getApplicationContext(), stringByteArray, Toast.LENGTH_LONG).show();

		// boolean flag to identify the media type, image or video
		boolean isImage = i.getBooleanExtra("isImage", false);

		if (filePath != null) {
			// Displaying the image or video on the screen
			previewMedia(isImage,byteArray);
		}
		else if(byteArray != null){
			
			previewMedia(isImage,byteArray);
		}else {
			Toast.makeText(getApplicationContext(),
					"Sorry, file path is missing!", Toast.LENGTH_LONG).show();
		}

//		btnUpload.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// uploading the file to server
////				new UploadFileToServer().execute();
//			}
//		});

	}

	/**
	 * Displaying captured image/video on the screen
	 * */
	private void previewMedia(boolean isImage,byte[] imgbyteArray) {
		// Checking whether captured media is image or video
		if (isImage) {
			imgPreview.setVisibility(View.VISIBLE);

			// bimatp factory
			BitmapFactory.Options options = new BitmapFactory.Options();

			// down sizing image as it throws OutOfMemory Exception for larger
			// images
			options.inSampleSize = 8;

			final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

			imgPreview.setImageBitmap(bitmap);
		} else {
			imgPreview.setVisibility(View.VISIBLE);
			Bitmap bmp = BitmapFactory.decodeByteArray(imgbyteArray, 0, imgbyteArray.length);
			
			imgPreview.setImageBitmap(bmp);
			

		}
	}

	
	}

