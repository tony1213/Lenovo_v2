package com.overtech.lenovo.entity.information;

import java.util.List;

/**
 * 信息
 *
 * @author Overtech Will
 */
public class Information {
    public int st;
    public String msg;
    public Body body;

    public class Body {
        public String comment_id;
        public String comment_user;
        public String comment_content;
        public List<InforItem> data;
    }

    public class InforItem {
        public String post_id;
        public String create_user_img;
        public String create_user_name;
        public String create_user_content;
        public String create_datetime;
        public List<UserImg> create_img;
        public List<Comment> comment;
    }

    public class UserImg {
        public String img;
    }

    public class Comment {
        public String comment_id;
        public String comment_datetime;
        public String comment_user;
        public String comment_content;
        public List<CommentResponse> comment_response;
    }

    public class CommentResponse {
        public String comment_datetime;
        public String comment_user;
        public String comment_content;
    }
}
