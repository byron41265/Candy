package org.lots.candy.entity;

public class TaskType {
	
	private String typeId;
	
	private String fullname;
	
	private String shortname;
	
	private Integer earnedPoint;

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public Integer getEarnedPoint() {
		return earnedPoint;
	}

	public void setEarnedPoint(Integer earnedPoint) {
		this.earnedPoint = earnedPoint;
	}
	

}
