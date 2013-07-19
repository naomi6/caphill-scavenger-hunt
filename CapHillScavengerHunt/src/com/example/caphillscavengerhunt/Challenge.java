package com.example.caphillscavengerhunt;

import com.google.android.gms.maps.model.LatLng;

public class Challenge {
	public String hint;
	public String answer;
	public String question;
	public String trivia;
	public String directions;
	public boolean picture;
	public LatLng coords;
	public String name;
	
	public Challenge(String name, String directions, String answer, String question, String trivia, String hint, boolean picture, LatLng coords) {
		this.name = name;
		this.hint = hint;
		this.answer = answer;
		this.question = question;
		this.trivia = trivia;
		this.directions = directions;
		this.picture = picture;
		this.coords = coords;
	}
}
