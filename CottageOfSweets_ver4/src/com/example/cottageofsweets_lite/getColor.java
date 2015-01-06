package com.example.cottageofsweets_lite;

public class getColor {
	private int id;
	private String pic;
	private int red,green,blue;
	
	public getColor(int id,String pic,int red,int green,int blue){
		this.id = id;
		this.pic = pic;
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public int getId(){
		return id;
	}
	
	public String getPic(){
		return pic;
	}
	
	public int getRed(){
		return red;
	}
	
	public int getBlue(){
		return blue;
	}
	
	public int getGreen(){
		return green;
	}
}

