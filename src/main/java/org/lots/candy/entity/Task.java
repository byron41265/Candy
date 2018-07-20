package org.lots.candy.entity;

public class Task {
	
	private String taskId;
	
	private String name;
	
	private int eachPoint;
	
	private int pointLimit;
	
	private int dailyPointLimit;
	
	private int earnedPoint;
	
	private String instruction;
	
	private String ifClosed;
	
	private String checkMethod;
	
	private String typeId;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getEachPoint() {
		return eachPoint;
	}

	public void setEachPoint(int eachPoint) {
		this.eachPoint = eachPoint;
	}

	public int getPointLimit() {
		return pointLimit;
	}

	public void setPointLimit(int pointLimit) {
		this.pointLimit = pointLimit;
	}

	public int getDailyPointLimit() {
		return dailyPointLimit;
	}

	public void setDailyPointLimit(int dailyPointLimit) {
		this.dailyPointLimit = dailyPointLimit;
	}


	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public int getEarnedPoint() {
		return earnedPoint;
	}

	public void setEarnedPoint(int earnedPoint) {
		this.earnedPoint = earnedPoint;
	}

	public String getIfClosed() {
		return ifClosed;
	}

	public void setIfClosed(String ifClosed) {
		this.ifClosed = ifClosed;
	}

	public String getCheckMethod() {
		return checkMethod;
	}

	public void setCheckMethod(String checkMethod) {
		this.checkMethod = checkMethod;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

}
