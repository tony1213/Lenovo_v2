package com.overtech.lenovo.entity.tasklist;

/**
 * Created by Overtech on 16/4/22.
 */
public class DetailInfo {
    public int st;
    public String msg;
    public Body body;

    public class Body{
        public Contract contract;
        public WorkorderMessage workorder_message;
        public SLA sla;
    }
    public class Contract{
        public String contract_description;
        public String contract_manager;
        public String contract_manager_phone;
        public String contract_technical_support;
        public String contract_technical_support_phone;
        public String contract_technical_support_qq_group;
        public String contract_technical_support_wechat_group;
    }
    public class WorkorderMessage{
        public String repair_person;
        public String repair_date;
        public String repair_person_contact_information;
        public String create_datetime;
        public String repair_address;
        public String repair_store;
        public String store_contact_phone;
        public String remark;
    }
    public class SLA{
        public String sla_response_datetime;
        public String sla_home_datetime;
        public String sla_solved_datetime;
    }
}
