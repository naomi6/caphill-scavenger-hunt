package com.example.caphillscavengerhunt;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class ChallengeActivity extends FragmentActivity{
	public static ArrayList<Challenge>challenges;
	private int unlocked = 0;
	private static final int CAMERA_REQUEST = 100;
	private static final int MAP_REQUEST_CODE = 50;
	private static final String ROOT_FOLDER = "CAP_HILL_SC_HUNT";
	private static String IMAGE_PATH;
	
	//the pager widget which handles the animation and swiping
	private ViewPager mPager;
	
	//the pager adapter which provides the pages to the pager widget
	private PagerAdapter mPagerAdapter;
	
   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.challenge, menu);
        
        return true;
    }  
   
   public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()) {
       case R.id.action_map:
           	startActivityForResult(new Intent(this, MapActivity.class), MAP_REQUEST_CODE);
            return true;
       case android.R.id.home:
    	   // app icon in action bar clicked; go home
           endHuntDialog();
           return true;
       }
       
       return false;
   }
   
   @Override
   protected void onDestroy() {
	   challenges = null;
	   super.onDestroy();
   }
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v("CHRIS", "Challenge - on create");
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_screen_slide);
		
        //action bar can be used for navigation!
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
		//instantiate a ViewPager and a PagerAdapter
		mPager = (ViewPager)findViewById(R.id.pager);
		mPagerAdapter = new ChallengePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
	}
	
	private void endHuntDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("End Hunt?")
		.setMessage("This will end your scavenger hunt.")
		.setCancelable(false)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				endHunt();
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	@Override
	public void onBackPressed(){
		if (mPager.getCurrentItem()== 0){
			//if user is currently on first item ask them if they want to end the hunt
			endHuntDialog();
		}
		else {
			//else go to the previous challenge
			mPager.setCurrentItem(mPager.getCurrentItem()-1);
		}
	}
	
	//ends the hunt
	public void endHunt(){
		this.finishActivity(MAP_REQUEST_CODE);
		super.onBackPressed();
	}
	
	private class ChallengePagerAdapter extends FragmentStatePagerAdapter {
		public ChallengePagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
			
			try {
				loadChallenges();
			} catch (JSONException e) {
				e.printStackTrace();

			} catch (IOException e) {
				e.printStackTrace();
			}
					        
			//initialize the progress bar    
			ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
			pb.setMax(challenges.size());
			pb.setProgress(1);
           ((TextView)findViewById(R.id.progressText)).setText("1 of " + challenges.size());
		}
		
		private void loadChallenges() throws JSONException, IOException {
			InputStream inputStream = getResources().openRawResource(R.raw.challenges);
			BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder total = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
			    total.append(line);
			}
			JSONArray arr = new JSONArray(total.toString());
			JSONObject tempObj;
			Challenge c;
			challenges = new ArrayList<Challenge>();
			for(int i = 0; i < arr.length(); i++){
			    tempObj = arr.getJSONObject(i);
			    if (tempObj.getBoolean("picture")){
			    	c = new Challenge(tempObj.getString("name"),
			    			tempObj.getString("text"),
			    			"n/a",
			    			"n/a",
			    			"n/a",
			    			tempObj.getBoolean("picture"), 
			    			new LatLng(Float.parseFloat(tempObj.getString("lat")), Float.parseFloat(tempObj.getString("long"))));
			    }
			    else {
			    	c = new Challenge(tempObj.getString("name"),
			    		tempObj.getString("text"), 
			    		tempObj.getString("answer"),
			    		tempObj.getString("trivia"),
			    		tempObj.getString("hint"),
			    		tempObj.getBoolean("picture"),
		    			new LatLng(Float.parseFloat(tempObj.getString("lat")), Float.parseFloat(tempObj.getString("long"))));
			    }
			    challenges.add(c);
			}			
		}

		@Override 
		public ChallengeFragment getItem(int position) {
			return ChallengeFragment.create(challenges.get(position));
		}
		
		@Override
		public int getCount() {
			if (unlocked == challenges.size()){
				return unlocked;
			}
			return unlocked+1;
		}
	}
	
	
    public void startCamera(View view){
    	File imagesFolder = new File(Environment.getExternalStorageDirectory(), ROOT_FOLDER);
    	imagesFolder.mkdirs();
    	SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy-hh:mm:ss", Locale.US);
    	
    	Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    	cameraIntent.putExtra("return-data", true);
    	Uri uri= Uri.fromFile(new File(imagesFolder, s.format(new Date())+".jpg"));
    	IMAGE_PATH = uri.toString();
    	cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
    	startActivityForResult(cameraIntent, CAMERA_REQUEST);
    	
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
    		Toast.makeText(this, "Image captured!", Toast.LENGTH_LONG).show();
    		//((ImageButton)mPager.getFocusedChild().findViewById(R.id.camera)).setEnabled(false);
        	//FB.getInstance().postAlbum(new File(IMAGE_PATH));
    		completedChallenge();
    	}
    }
    
    
    //takes care of all cleanup (marking challenge as completed, update progress, 
    //move to next challenge, etc that results from completing a challenge
    private void completedChallenge(){
		if (unlocked == mPager.getCurrentItem()) {
			unlocked = mPager.getCurrentItem()+1;
			//make sure the pageradapter knows there is a new element now
			mPagerAdapter.notifyDataSetChanged();
		}
		
		//update progress text
        ((TextView)findViewById(R.id.progressText)).setText((unlocked+1) + " of " + challenges.size());
        //update progress bar
        ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
        pb.setProgress((unlocked+1));

        //move to next challenge
        if (unlocked < challenges.size()) {
        	mPager.setCurrentItem(unlocked);
        }        
        else {
        	//go to finished activity
           	startActivityForResult(new Intent(this, MapActivity.class), MAP_REQUEST_CODE);
        }
    }
    
  public void submit(View view) {
	    
	    Challenge currentChallenge = challenges.get(mPager.getCurrentItem());
    	String user;
	    try {
	    	user = ((EditText)mPager.getFocusedChild().findViewById(R.id.answerField))
	    			.getEditableText().toString().toLowerCase(Locale.ENGLISH);
	    }
	    catch (NullPointerException e){
	    	return;
	    }

	    String correct = currentChallenge.answer.toLowerCase(Locale.ENGLISH);
    	
    	
    	if(user.equals(correct)) {
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Correct!")
    		.setMessage(currentChallenge.trivia)
    		.setCancelable(false)
    		.setNegativeButton("Close", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int id) {
    				dialog.cancel();
    			}
    		});
    		AlertDialog alert = builder.create();
    		alert.show();
    		((Button)mPager.getFocusedChild().findViewById(R.id.submit)).setEnabled(false);
    		((EditText)mPager.getFocusedChild().findViewById(R.id.answerField)).setEnabled(false);
    		completedChallenge();
    	}
    	else {
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Incorrect")
    		.setMessage(currentChallenge.hint)
    		.setCancelable(false)
    		.setNegativeButton("Close", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int id) {
    				dialog.cancel();
    			}
    		});
    		AlertDialog alert = builder.create();
    		alert.show();
    		
    	}
    	//close the keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(
        	      Context.INPUT_METHOD_SERVICE);
        	imm.hideSoftInputFromWindow(findViewById(R.id.answerField).getWindowToken(), 0);
    }
}
