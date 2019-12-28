package com.example.inlearn.data.model;

public class Notification {
    String notification;
    String created_at;

    public Notification(String notification, String createdAt) {
        this.notification = notification;
        this.created_at = createdAt;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String createdAt) {
        this.created_at = createdAt;
    }

    @Override
    public String toString(){
        return
                "kategoriItem{" +
                        ",notification = '" + notification + '\'' +
                        ",created_at = '" + created_at + '\'' +
                        "}";
    }
}
