package com.overtech.lenovo.entity.tasklist;
/**
 * 工单详情
 * @author Overtech Will
 *
 */
public class Task {
	private String taskLogoPath;
	private String taskNo;
	private String taskDate;
	private String taskClass;
	private String taskDescription;
	private String taskRemark;
	private String latitude;
	private String longitude;
	private String taskVisitTime;
	private String isUrgent;
	private String taskType;
	
	public Task(String taskLogoPath, String taskNo, String taskDate,
			String taskClass, String taskDescription, String taskRemark,
			String latitude, String longitude, String taskVisitTime,
			String isUrgent, String taskType) {
		super();
		this.taskLogoPath = taskLogoPath;
		this.taskNo = taskNo;
		this.taskDate = taskDate;
		this.taskClass = taskClass;
		this.taskDescription = taskDescription;
		this.taskRemark = taskRemark;
		this.latitude = latitude;
		this.longitude = longitude;
		this.taskVisitTime = taskVisitTime;
		this.isUrgent = isUrgent;
		this.taskType = taskType;
	}
	public String getTaskLogoPath() {
		return taskLogoPath;
	}
	public void setTaskLogoPath(String taskLogoPath) {
		this.taskLogoPath = taskLogoPath;
	}
	public String getTaskNo() {
		return taskNo;
	}
	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}
	public String getTaskDate() {
		return taskDate;
	}
	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}
	public String getTaskClass() {
		return taskClass;
	}
	public void setTaskClass(String taskClass) {
		this.taskClass = taskClass;
	}
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	public String getTaskRemark() {
		return taskRemark;
	}
	public void setTaskRemark(String taskRemark) {
		this.taskRemark = taskRemark;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getTaskVisitTime() {
		return taskVisitTime;
	}
	public void setTaskVisitTime(String taskVisitTime) {
		this.taskVisitTime = taskVisitTime;
	}
	public String getIsUrgent() {
		return isUrgent;
	}
	public void setIsUrgent(String isUrgent) {
		this.isUrgent = isUrgent;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	
}
