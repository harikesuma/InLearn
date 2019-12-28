package com.example.inlearn.data.model;

import java.util.List;

public class ResponseComment {

    List<Comment> jawabanList;
    String status;
    String msg;

    public List<Comment> getCommentList() {
        return jawabanList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.jawabanList = commentList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    @Override
    public String toString(){
        return
                "ResponseComment{" +
                        "jawabanList = '" + jawabanList + '\'' +
                        ",msg = '" + msg + '\'' +
                        "}";
    }

}
