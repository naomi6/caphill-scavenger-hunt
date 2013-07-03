package com.example.caphillscavengerhunt;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


public class ChallengeActivity extends FragmentActivity{
	private ArrayList<Challenge>challenges = new ArrayList<Challenge>();
	private int unlocked = 0;
	
	//the pager widget which handles the animation and swiping
	private ViewPager mPager;
	
	//the pager adapter which provides the pages to the pager widget
	private PagerAdapter mPagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_slide);
		
		//instantiate a ViewPager and a PagerAdapter
		mPager = (ViewPager)findViewById(R.id.pager);
		mPagerAdapter = new ChallengePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
	}
	
	@Override
	public void onBackPressed(){
		if (mPager.getCurrentItem()== 0){
			//if user is currently on first item ask them if they want to end the hunt
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
		else {
			//else go to the previous challenge
			mPager.setCurrentItem(mPager.getCurrentItem()-1);
		}
	}
	
	//ends the hunt
	public void endHunt(){
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
			pb.setProgress(0);
           ((TextView)findViewById(R.id.progressText)).setText("0 of " + challenges.size());
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
			for(int i = 0; i < arr.length(); i++){
			    tempObj = arr.getJSONObject(i);
			    c = new Challenge(tempObj.getString("directions"), 
			    		tempObj.getString("answer"),
			    		tempObj.getString("question"),
			    		tempObj.getString("trivia"),
			    		tempObj.getString("hint"));
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
	
	  public void submit(View view) {
		    Challenge currentChallenge = challenges.get(mPager.getCurrentItem());
	    	String correct = currentChallenge.answer.toLowerCase(Locale.ENGLISH);
		  		   
	    	String user = ((EditText)mPager.getFocusedChild().findViewById(R.id.answerField)).getEditableText().toString().toLowerCase(Locale.ENGLISH);
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
	    		
    			if (unlocked == mPager.getCurrentItem()) {
    				unlocked = mPager.getCurrentItem()+1;
    				//make sure the pageradapter knows there is a new element now
    				mPagerAdapter.notifyDataSetChanged();
    			}
	    		
	    		//update progress text
	            ((TextView)findViewById(R.id.progressText)).setText(unlocked + " of " + challenges.size());
	            //update progress bar
	            ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
	            pb.setProgress(unlocked);

	            //move to next challenge
	            if (unlocked < challenges.size()) {
	            	mPager.setCurrentItem(unlocked);
	            }
	            else {
	            	//go to finished activity!
	            }
	            //close the keyboard
	            InputMethodManager imm = (InputMethodManager)getSystemService(
	            	      Context.INPUT_METHOD_SERVICE);
	            	imm.hideSoftInputFromWindow(findViewById(R.id.answerField).getWindowToken(), 0);
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
	    		
	    		//close the keyboard
	            InputMethodManager imm = (InputMethodManager)getSystemService(
	            	      Context.INPUT_METHOD_SERVICE);
	            	imm.hideSoftInputFromWindow(findViewById(R.id.answerField).getWindowToken(), 0);
	    	}
	    }
}
