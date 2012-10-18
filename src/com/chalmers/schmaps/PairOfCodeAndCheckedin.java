package com.chalmers.schmaps;

public class PairOfCodeAndCheckedin {
	
	private int code;
	private int value;
	
	public PairOfCodeAndCheckedin(int c, int v){
		code=c;
		value=v;
		
	}
	
	public int getCode(){
		return code;
	}
	
	public int getValue(){
		return value;
	}

}
