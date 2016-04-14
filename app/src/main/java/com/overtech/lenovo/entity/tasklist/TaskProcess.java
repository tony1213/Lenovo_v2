package com.overtech.lenovo.entity.tasklist;

/**
 * 本单信息 任务进度信息
 * 
 * @author Overtech Will
 * 
 */
public class TaskProcess {
	/**
	 * 已开单
	 */
	public static final int CREATE=-1;
	/**
	 * 未接单
	 */
	public static final int RECEIVE=0;
	/**
	 * 未预约
	 */
	public static final int ORDER=1;
	/**
	 * 未上门
	 */
	public static final int VISIT=2;
	/**
	 * 未解决
	 */
	public static final int RESOLVE=3;
	/**
	 * 评价状态
	 */
	public static final int EVALUATE=4;
	public int taskType;//工单类型
	public String workorder_code;//工单号
	public String workorder_create_datetime;//工单创建时间
	public String assigned_datetime;//工单确认时间
	public String appointment_datetime;//工单预约时间
	public String appointment_home_datetime;//工单预约上门时间
	public String evaluate_feedback;//工单评价反馈
	public String solution;//解决方案
}
