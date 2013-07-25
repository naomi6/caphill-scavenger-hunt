package com.example.caphillscavengerhunt;

import java.io.File;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ImageDialog extends Activity {
	private ImageView mDialog;
    private final String ROOT_FOLDER = "CAP_HILL_SC_HUNT";

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_layout);
		
		Bundle extras = getIntent().getExtras(); 
		String path = extras.getString("uri");
		File imagesFolder = new File(Environment.getExternalStorageDirectory(), ROOT_FOLDER);

		mDialog = (ImageView)findViewById(R.id.your_image);
		mDialog.setImageBitmap(BitmapFactory.decodeFile(imagesFolder + "/" + path +".jpg"));

		mDialog.setClickable(true);
		
		//finish the activity if the user clicks anywhere on the image
		mDialog.setOnClickListener(new OnClickListener () {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}
}
