package com.overtech.lenovo.entity.knowledge;

import java.util.List;

/**
 * Created by Overtech on 16/4/27.
 */
public class Knowledges {
    public int st;
    public String msg;
    public Body body;
    public class Body{
        public String content;
        public List<KnowledgeAndContract> data;
    }
    public class KnowledgeAndContract{
        public String knowledge_type;
        public List<Knowledge> knowledges;
        public String contract_code;
        public String contract_name;

        @Override
        public String toString() {
            return contract_name;
        }
    }
    public class Knowledge{
        public String knowledge_id;
        public String logo;
        public String subject;
    }

}
