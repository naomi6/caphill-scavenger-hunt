package com.example.caphillscavengerhunt;

import java.util.LinkedList;
import java.util.Locale;

import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.EditText;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;

public class MainActivity extends Activity {
		
	private static Challenge currentChallenge;
	private static int index =0;
	private static Challenge [] challenges;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
                
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

        challenges = new Challenge[]{c1, c2, c3};
        currentChallenge = challenges[index];
        challenges[0].unlocked = true;
        
        
        ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
        pb.setMax(challenges.length);
        pb.setProgress(index);
        
        ((TextView)findViewById(R.id.clue)).setText(currentChallenge.clue);
        ((TextView)findViewById(R.id.question)).setText(currentChallenge.question);
        ((TextView)findViewById(R.id.progressText)).setText(index + " of " + challenges.length);

    }


    public void back(View view) {
    	if (index != 0) {
    		index--;
    		currentChallenge = challenges[index];
            ((TextView)findViewById(R.id.clue)).setText(currentChallenge.clue);
            ((TextView)findViewById(R.id.question)).setText(currentChallenge.question);
    	}
    }
    
    public void next(View view){
    	if (index != challenges.length-1 && challenges[index+1].unlocked) {
    		index++;
    		currentChallenge = challenges[index];
            ((TextView)findViewById(R.id.clue)).setText(currentChallenge.clue);
            ((TextView)findViewById(R.id.question)).setText(currentChallenge.question);
    	}
    }
    
    public void submit(View view) {
    	String correct = currentChallenge.answer.toLowerCase(Locale.ENGLISH);

    	String user = ((EditText)findViewById(R.id.answerField)).getEditableText().toString().toLowerCase(Locale.ENGLISH);

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
    		if (index != challenges.length-1) {
    			challenges[index+1].unlocked = true;
    		}
    		//update progress text
            ((TextView)findViewById(R.id.progressText)).setText((index+1) + " of " + challenges.length);
            //update progress bar
            ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
            pb.setProgress(index+1);
            
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
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    public class Challenge {
    	public String hint;
    	public String answer;
    	public String question;
    	public String trivia;
    	public String clue;
    	public boolean unlocked;
    	
    	
    	public Challenge(String hint, String answer, String question, String trivia, String clue) {
    		this.hint = hint;
    		this.answer = answer;
    		this.question = question;
    		this.trivia = trivia;
    		this.clue = clue;
    		this.unlocked = false;
    	}
    }
}
