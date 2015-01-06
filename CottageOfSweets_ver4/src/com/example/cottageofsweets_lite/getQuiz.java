package com.example.cottageofsweets_lite;

public class getQuiz {
	private int id;
	private String quiz;
	private String answer;
	
	public getQuiz(int id,String quiz,String answer){
		this.id = id;
		this.quiz = quiz;
		this.answer = answer;
	}
	
	public int getID(){
		return id;
	}
	
	public String getQuiz(){
		return quiz;
	}
	
	public String getAnswer(){
		return answer;
	}
}

