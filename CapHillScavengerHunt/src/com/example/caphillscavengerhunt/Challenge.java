package com.example.caphillscavengerhunt;

public class Challenge {
	public String hint;
	public String answer;
	public String question;
	public String trivia;
	public String directions;
	
	
	public Challenge(String directions, String answer, String question, String trivia, String hint) {
		this.hint = hint;
		this.answer = answer;
		this.question = question;
		this.trivia = trivia;
		this.directions = directions;
	}
}