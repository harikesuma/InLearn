package com.example.inlearn.data.model;

import java.util.List;

public class ResponseUser {
    String msg;
    List<User> userList;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public String toString(){
        return
                "ResponseUser{" +
                        "userList = '" + userList + '\'' +
                        ",msg = '" + msg + '\'' +
                        "}";
    }
}
