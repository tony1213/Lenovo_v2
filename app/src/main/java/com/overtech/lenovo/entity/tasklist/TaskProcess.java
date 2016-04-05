package com.overtech.lenovo.entity.tasklist;

/**
 * 本单信息 任务进度信息
 * 
 * @author Overtech Will
 * 
 */
public class TaskProcess {
	/**
	 * 工单进度
	 */
	private String state;
	/**
	 * 工单进度对应的时间
	 */
	private String time;
	/**
	 * 工单其他事项
	 */
	private String other;
	/**
	 * 工单动作
	 */
	private String action;

	public TaskProcess(String state, String time, String other, String action) {
		super();
		this.state = state;
		this.time = time;
		this.other = other;
		this.action = action;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
