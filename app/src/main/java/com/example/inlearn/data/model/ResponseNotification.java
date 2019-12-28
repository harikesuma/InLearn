package com.example.inlearn.data.model;

import java.util.List;

public class ResponseNotification {
    List<Notification> notificationList;
    String msg;
    boolean error;

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
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
                "ResponseNotification{" +
                        "notificationList = '" + notificationList + '\'' +
                        ",msg = '" + msg + '\'' +
                        "}";
    }
}
