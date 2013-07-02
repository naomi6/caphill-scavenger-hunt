package com.example.caphillscavengerhunt;


import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


public class ChallengeActivity extends FragmentActivity{
	private Challenge challenges [];
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
			//if user is currently on first item just click the back button
			super.onBackPressed();
		}
		else {
			//else go to the previous challenge
			mPager.setCurrentItem(mPager.getCurrentItem()-1);
		}
	}
	
	private class ChallengePagerAdapter extends FragmentStatePagerAdapter {
		public ChallengePagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
			
			Challenge c1 = new Challenge("Walk down the street, the tavern will be on your left.", 
		        		"Kurt Cobain", 
		        		"What famous musician was last seen here before his death?", 
		        		"Some cool piece of trivia knowledge", 
		        		"Some clue to help you out");
		    Challenge c2 = new Challenge("Follow the smell of pie", 
		        		"oven",
		        		"Ask an employee: The copper tube connects to the ______.",
		        		"Some sweet piece of trivia about pie",
		        		"Where doe pies get baked?");
		    Challenge c3 = new Challenge("There is an ice cream shop around the corner that is out of this world!",
		        		"mint leaves",
		        		"What ingrediant comes from the furthest away?",
		        		"Some cool piece of Molly Moons trivia/History",
		        		"Check out the map in the corner for a clue!");
		    Challenge c4 = new Challenge("Go down the street and turn right on 11th, you should see a mural",
		        		"Baso Fibonacci",
		        		"Who painted this?/ or maybe a picture challenge!",
		        		"Something super interesting about the mural!",
		        		"Go read the little sign!");
		    challenges = new Challenge[]{c1, c2, c3, c4};
		        
			//initialize the progress bar    
			ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
			pb.setMax(challenges.length);
			pb.setProgress(0);
           ((TextView)findViewById(R.id.progressText)).setText("0 of " + challenges.length);
		}
		
		@Override 
		public ChallengeFragment getItem(int position) {
			return ChallengeFragment.create(challenges[position]);
		}
		
		@Override
		public int getCount() {
			return unlocked+1;
		}
		
	
		
	}
	
	  public void submit(View view) {
		    Challenge currentChallenge = challenges[mPager.getCurrentItem()];
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
	    		
	    		//unlock the next challenge
	    		if (mPager.getCurrentItem() != challenges.length-1) {
	    			unlocked++;
	    			//make sure the pageradapter knows there is a new element now
	    			mPagerAdapter.notifyDataSetChanged();
	    		}
	    		//update progress text
	            ((TextView)findViewById(R.id.progressText)).setText(unlocked + " of " + challenges.length);
	            //update progress bar
	            ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
	            pb.setProgress(mPager.getCurrentItem()+1);
	            
	            //move to next challenge
	            mPager.setCurrentItem(unlocked);
	            
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
