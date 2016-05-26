package com.overtech.lenovo.entity.person;

import java.util.List;

/**
 * Created by Overtech on 16/5/3.
 */
public class PersonalAccount {
    public int st;
    public String msg;
    public Body body;
    public class Body{
        public List<Workorder> data;
    }
    public class Workorder{
        public String datetime;
        public String workorder_code;
        public String standard_fee_amount;
        public String contract ;
        public String isPay;
        public String issue_type;
        public String issue_description;
        public String task_type;
    }
}
