package com.overtech.lenovo.entity.person;

import java.util.List;

/**
 * Created by Overtech on 16/4/22.
 */
public class Person {
    public int st;
    public String msg;
    public Body body;

    public class Body {
        public String avator;
        public String finance;
        public String month_workorder_amount;
        public String year_workorder_amount;
        public String satisfaction;

        public String name;
        public String sex;
        public String birthday;
        public String isCertificated;
        public String certificate_datetime;
        public String mobile;
        public String qq;
        public String wechat;
        public String email;
        public List<Type> territory_node_path;
        public String address;
        public List<Type> degree;
        public List<Type> english_ability;
        public String working_life;
        public List<Type> self_orientation;
        public List<Type> type_of_id;
        public String idcard;
        public String positive_identity_card;
        public String opposite_identity_card;
    }

    public class Type {
        public String isDefault;
        public String _id;
        public String name;

        @Override
        public String toString() {
            return name;
        }
    }
}
