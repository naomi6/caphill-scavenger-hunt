package com.example.caphillscavengerhunt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {	

	private static final int MAP_REQUEST_CODE = 50;

	@Override
    protected void onCreate(Bundle savedInstanceState) {   
		Log.v("CHRIS", "Main - on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_map:
            	startActivityForResult(new Intent(this, MapActivity.class), MAP_REQUEST_CODE);
             return true;
        }
        
        return false;
    }
    
    public void start(View view) {
    	//this is just testing some FB stuff...don't need it right now
    	/*new FB(this);
    	File file = new File(Environment.getExternalStorageDirectory()+"/CAP_HILL_SC_HUNT/");
        //gets a list of the files
        File[] d = file.listFiles();
    	FB.getInstance().postAlbum(d[1]);*/
    	
    	
        startActivity(new Intent(this, ChallengeActivity.class));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }  
}


    
