package org.lots.candy.entity;

public enum  TaskEnum {
	BINDTWITTER("1"), FOLLOWTWITTER("2"), RETWEET("3"), LIKETWITTER("4"), WRITEARTICLE("5"), VIDEO("6"), BINDFACEBOOK("7"),
	BINDTELEGRAM("8"), INVITETELEGRAM("9");
	
	private String value;
	
	private TaskEnum(String value){
		this.value = value;
	}
	
	public String toString(){
		return this.value;
	}

}
