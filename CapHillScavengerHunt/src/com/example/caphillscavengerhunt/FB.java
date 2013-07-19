package com.example.caphillscavengerhunt;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;

public class FB {
	private static FB instance = null;
	private Facebook mFacebook;
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    private static final int REAUTH_ACTIVITY_CODE = 100;
	private static Activity activity;
	
	public FB(Activity a){
		activity = a;
		//Facebook Login
        Session.openActiveSession(activity, true, new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (session.isOpened()){
					Log.v("FACEBOOK", "OPEN");
					//make request to the /me API
					Settings.addLoggingBehavior(LoggingBehavior.REQUESTS);

					Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
						@Override
						public void onCompleted(GraphUser user, Response response) {
							Log.v("FACEBOOK", "WELCOME " + user.getName());
							requestPublishPermissions(Session.getActiveSession());

						}
					});
				}
			}
		});
        instance = this;
	}
	
	//use this to make it a singleton class...
	public static FB getInstance(){
		if (instance ==null) {
			//throw new Exception("FB class must be instantiated with an Activity before you can use it!");
		}
		return instance;
	}
	
   private void requestPublishPermissions(Session session) {
        if (session != null) {
            Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(this.activity, PERMISSIONS)
                    // demonstrate how to set an audience for the publish permissions,
                    // if none are set, this defaults to FRIENDS
            		.setDefaultAudience(SessionDefaultAudience.ONLY_ME)
                    //.setDefaultAudience(SessionDefaultAudience.FRIENDS)
                    .setRequestCode(REAUTH_ACTIVITY_CODE);
            session.requestNewPublishPermissions(newPermissionsRequest);
        }
    }
	   
	public void postAlbum(File file) {
		String message = "I just finished the Cap Hill Scavenger Hunt! Try it yourself <a href='http://bubernak.com'>here</a>";
		Request request;
		try {
			request = Request.newUploadPhotoRequest(Session.getActiveSession(), file, new Request.Callback() {
				@Override
				public void onCompleted(Response response) {
					Log.v("FACEBOOK", "UPLOADED PHOTO!");
					Log.v("FACEBOOK", response.toString());
		    		Toast.makeText(activity, "Image finished uploaded!", Toast.LENGTH_LONG).show();
				}
			});
			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		}
		
		/*Request request = Request.newStatusUpdateRequest(Session.getActiveSession(), message, new Request.Callback() {
			@Override
			public void onCompleted(Response response) {
				Log.v("FACEBOOK", Session.getActiveSession().getPermissions().toString());
			}
		});*/
	}

}
