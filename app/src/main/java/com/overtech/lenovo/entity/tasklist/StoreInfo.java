package com.overtech.lenovo.entity.tasklist;

import java.util.List;

/**
 * Created by Overtech on 16/4/22.
 */
public class StoreInfo {
    public int st;
    public String msg;
    public Body body;
    public class Body{
        public String name;
        public List<ImageUrl> imageList;
        public List<Remark> remarks;
    }
    public class ImageUrl{
        public String imageUrl;
    }
    public class Remark{
        public String create_datetime;
        public String create_user_name;
        public String create_content;
    }
}
