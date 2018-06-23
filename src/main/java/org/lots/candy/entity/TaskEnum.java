package org.lots.candy.entity;

public enum  TaskEnum {
	BINDTWITTER("1");
	
	private String value;
	
	private TaskEnum(String value){
		this.value = value;
	}
	
	public String toString(){
		return this.value;
	}

}
