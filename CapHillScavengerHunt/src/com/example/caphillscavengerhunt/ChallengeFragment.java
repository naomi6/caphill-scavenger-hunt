package com.example.caphillscavengerhunt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    public static final String ARG_QUESTION = "question";
    public static final String ARG_DIRECTIONS = "directions";

    /**
     * The fragment's attributes, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private String mQuestion;
    private String mDirections;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ChallengeFragment create(Challenge c) {
    	Log.v("LOL", c.toString());
        ChallengeFragment fragment = new ChallengeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION, c.question);
        args.putString(ARG_DIRECTIONS, c.directions);

        fragment.setArguments(args);
        return fragment;
    }

    public ChallengeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDirections = getArguments().getString(ARG_DIRECTIONS);
        mQuestion = getArguments().getString(ARG_QUESTION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_challenge, container, false);

        // Set the view to show the directions to the site and the question
        ((TextView) rootView.findViewById(R.id.question)).setText(mQuestion);
        ((TextView) rootView.findViewById(R.id.directions)).setText(mDirections);

        return rootView;
    }  
}