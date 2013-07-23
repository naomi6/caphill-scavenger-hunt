package com.example.caphillscavengerhunt;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements
	GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener{
    private GoogleMap mMap;
    private LocationClient mLocationClient;
    private Location mCurrentLocation;
    
    //TODO: use bounding boxes instead of a hardcoded zoom value
    private final static float ZOOM_VALUE = 15;

    
    private final static int 
    	CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	
    //define a dialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
    	//global field to contain the error dialog
    	private Dialog mDialog;
    	//Default constructor. sets the dialog field to null
    	public ErrorDialogFragment() {
    		super();
    		mDialog = null;
    	}
    	
    	//set the dialog to display
    	public void setDialog(Dialog dialog) {
    		mDialog = dialog;
    	}
    	
    	//return a dialog to the DialogFragment.
    	@Override
    	public Dialog onCreateDialog(Bundle savedInstanceState) {
    		return mDialog;
    	}
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
    	case CONNECTION_FAILURE_RESOLUTION_REQUEST :
    		/*
    		 * if the result code is Activty.RESULT_OK, try
    		 * to connect again
    		 */
    		switch (resultCode) {
    			case Activity.RESULT_OK :
    			/*
    			 * Try the request again
    			 */
    			break;
    		}
    	}
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	// app icon in action bar clicked; go home
            Intent intent = new Intent(this, ChallengeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        
        return false;
    }
    
    
    private boolean servicesConnected() {
    	//check that google play services is available
    	int resultCode =
    			GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
    	//if google play services is available
    	if (ConnectionResult.SUCCESS == resultCode) {
    		//in debug mode, log the status
    		Log.d("Location Updates", "Google play services is available!");
    		return true;
    	}
    	else {
    		showErrorDialog(resultCode);
    	}
    	return false;
    }
    
    @Override
    protected void onStart() {
		Log.v("CHRIS", "Map - on start");
    	super.onStart();
    	//connect the client
    	mLocationClient.connect();
    }
    
    @Override
    protected void onStop(){
		Log.v("CHRIS", "Map - on stop");
    	mLocationClient.disconnect();
    	super.onStop();
    }
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		Log.v("CHRIS", "Map - on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //action bar can be used for navigation!
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        
        mLocationClient = new LocationClient(this, this, this);
        //add markers for each completed challenge
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        for (Challenge c : ChallengeActivity.challenges){
		    mMap.addMarker(new MarkerOptions()
		        	.position(c.coords)
		        	.title(c.name));
        }
        mMap.setMyLocationEnabled(true);
      
    }
	

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		/*
		 * google play services can resolve some errors it 
		 * detects. if the error has a resolution, try sending
		 * an intent to start a google play services activity
		 * that can resolve the error
		 */
		if (result.hasResolution()){
			try {
				//start an activity to resolve the error
				result.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * thrown if google play services canceled the original pendingintent
				 */
			}catch (IntentSender.SendIntentException e) {
				//log the error
				e.printStackTrace();
			}
		} else {
			/*
			 * if no resolution available show the dialog
			 */
			showErrorDialog(result.getErrorCode());
		}
	}

	private void showErrorDialog(int errorCode){
		 // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                errorCode,
                this,
                CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {
            // Create a new DialogFragment for the error dialog
            ErrorDialogFragment errorFragment =
                    new ErrorDialogFragment();
            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);
            // Show the error dialog in the DialogFragment
            errorFragment.show(getSupportFragmentManager(),
                    "Location Updates");
        }
	}
	
	@Override
	/*
	 * called by location services when the request to connect
	 * the client finishes successfully. at this point you can
	 * request the current location or start periodic updates
	 */
	public void onConnected(Bundle connectionHint) {
		//display the connection status
		Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
		mCurrentLocation = mLocationClient.getLastLocation();
		LatLng zoomAndCenterTo;
		if (mCurrentLocation !=null){
			zoomAndCenterTo = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
		}
		else {
			Toast.makeText(this, "unable to access location", Toast.LENGTH_SHORT).show();
			//zoom and center to something else
			zoomAndCenterTo = ChallengeActivity.challenges.get(ChallengeActivity.challenges.size()-1).coords;
		}
		
		CameraUpdate center = CameraUpdateFactory.newLatLng(zoomAndCenterTo);
		mMap.moveCamera(center);
		
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(ZOOM_VALUE);
		mMap.moveCamera(zoom);
	}

	@Override
	/*
	 *  called by location services if connection drops b/c of 
	 *  an error
	 */
	public void onDisconnected() {
		//display  the connection status
		Toast.makeText(this, "disconnected. please reconnect", Toast.LENGTH_SHORT).show();
	}

}
