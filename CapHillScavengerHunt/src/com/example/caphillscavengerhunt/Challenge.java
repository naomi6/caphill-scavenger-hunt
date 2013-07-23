package com.example.caphillscavengerhunt;

import com.google.android.gms.maps.model.LatLng;

public class Challenge {
	public String hint;
	public String answer;
	public String trivia;
	public String text;
	public boolean picture;
	public LatLng coords;
	public String name;
	
	public Challenge(String name, String text, String answer, String trivia, String hint, boolean picture, LatLng coords) {
		this.name = name;
		this.hint = hint;
		this.answer = answer;
		this.text = text;
		this.trivia = trivia;
		this.picture = picture;
		this.coords = coords;
	}
}
