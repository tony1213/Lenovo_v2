package com.overtech.lenovo.entity.tasklist;

/**
 * 本单信息 任务进度信息
 *
 * @author Overtech Will
 */
public class TaskProcess {
    /**
     * 已开单
     */
    public static final String CREATE = "-1";
    /**
     * 未接单
     */
    public static final String RECEIVE = "0";
    /**
     * 未预约
     */
    public static final String APPOINT = "1";
    /**
     * 未上门
     */
    public static final String HOME = "2";
    /**
     * 未解决
     */
    public static final String SOLUTION = "3";
    /**
     * 待关单
     */
    public static final String SHUTDOWN = "4";
    /**
     * 待结单
     */
    public static final String ACCOUNT = "5";

    public static final String COMPLITE = "6";
    public static final String EVALUATE = "10";//如果有内容，则显示该节点

    public String taskType;//工单类型
    public String workorder_code;//工单号
    public String workorder_create_datetime;//工单创建时间
    public String confirm_datetime;//工单确认时间
    public String appointment_datetime;//工单预约时间
    public String appointment_home_datetime;//工单预约上门时间
    public String home_datetime;//工程师上门时间
    public String issue_description;//问题描述
    public String solution;//解决方案
    public String shutdown_datetime;//工单关单时间
    public String feedback_solved_datetime;//工程师反馈的解决时间
    public String feedback;//工单评价反馈

    public TaskProcess(String taskType, String workorder_code, String workorder_create_datetime, String confirm_datetime, String appointment_datetime, String appointment_home_datetime, String home_datetime, String issue_description, String solution, String shutdown_datetime, String feedback_solved_datetime, String feedback) {
        this.taskType = taskType;
        this.workorder_code = workorder_code;
        this.workorder_create_datetime = workorder_create_datetime;
        this.confirm_datetime = confirm_datetime;
        this.appointment_datetime = appointment_datetime;
        this.appointment_home_datetime = appointment_home_datetime;
        this.home_datetime = home_datetime;
        this.issue_description = issue_description;
        this.solution = solution;
        this.shutdown_datetime = shutdown_datetime;
        this.feedback_solved_datetime = feedback_solved_datetime;
        this.feedback = feedback;
    }
}
