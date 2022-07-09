package com.example.findu;

import com.google.firebase.database.ServerValue;

public class Comment {

    private String content;
    private String comment_user;
    private String uname;
    private Object timestamp;


    public Comment() {
    }

    public Comment(String content, String comment_user, String uname) {
        this.content = content;
        this.comment_user = comment_user;
        this.uname = uname;
        this.timestamp = ServerValue.TIMESTAMP;

    }

    public Comment(String content, String comment_id, String uname, Object timestamp) {
        this.content = content;
        this.comment_user = comment_id;
        this.uname = uname;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComment_user() {
        return comment_user;
    }

    public void setComment_user(String comment_user) {
        this.comment_user = comment_user;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}

