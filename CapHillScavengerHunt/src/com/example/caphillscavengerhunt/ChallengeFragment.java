package com.example.caphillscavengerhunt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A fragment representing a single challenge.
 */
public class ChallengeFragment extends Fragment {
    /**
     * The argument keys for the challenge this fragment represents
     */
    public static final String ARG_TEXT = "text";
    public static final String ARG_PICCHALLENGE ="picture";

    /**
     * The fragment's attributes, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private String mText;
    private Boolean mPicChallenge;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ChallengeFragment create(Challenge c) {
        ChallengeFragment fragment = new ChallengeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, c.text);
        args.putBoolean(ARG_PICCHALLENGE, c.picture);
        fragment.setArguments(args);
        return fragment;
    }

    public ChallengeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mText = getArguments().getString(ARG_TEXT);
        mPicChallenge = getArguments().getBoolean(ARG_PICCHALLENGE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_challenge, container, false);

        // Set the view to show the directions to the site and the question
        ((TextView) rootView.findViewById(R.id.text)).setText(mText);
        
        //if it is a picture challenge show the camera button
        if (mPicChallenge) {
        	rootView.findViewById(R.id.answerField).setVisibility(View.GONE);
        	rootView.findViewById(R.id.submit).setVisibility(View.GONE);
        }
        //else show the submit button and textbox
        else {
        	rootView.findViewById(R.id.pictureButton).setVisibility(View.GONE);
        }
        
        return rootView;
    }  
}