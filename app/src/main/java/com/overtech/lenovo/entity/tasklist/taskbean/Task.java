package com.overtech.lenovo.entity.tasklist.taskbean;

/**
 * 工单详情
 *
 * @author Overtech Will
 */
public class Task {
    public long appointment_home_datetime;//工程师预约上门时间
    public String repair_person_contact_information;//门店报修人员电话
    public String isUrgent;//是否紧急
    public String issue_resume;//任务单描述
    public String issue_type;//任务单分类
    public String latitude;//任务单对应门店经度
    public String longitude;//任务单对应门店纬度
    public String remarks;//任务单备注
    public String taskLogo;//任务单所属项目图标
    public String taskType;//  工单类型
    public String workorder_code;//工单单号
    public String workorder_create_datetime;//任务单发布时间
}
