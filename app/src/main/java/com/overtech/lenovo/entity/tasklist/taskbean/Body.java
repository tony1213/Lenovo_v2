package com.overtech.lenovo.entity.tasklist.taskbean;

import java.util.List;

/**
 * Created by Overtech on 16/4/12.
 */
public class Body {
    public List<Task> data;
    public List<AD> ad;
    public String repair_person;
    public String repair_person_contact_information;
    public String repair_address;
    public String standard_fee_amount;
    public String taskType;
    public String workorder_create_datetime;
    public String confirm_datetime;
    public String appointment_datetime;
    public String appointment_home_datetime;
    public String home_datetime;
    public String solution;
    public String shutdown_datetime;
    public String feedback_solved_datetime;
    public String feedback;

    public String workorder_notification_datetime;

    public String contract_code;
    public String contract_name;
    public String contract_start_date;
    public String contract_end_date;
    public String contract_date;
    public String contract_party_a;
    public String contract_party_b;
}
