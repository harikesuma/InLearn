package com.example.inlearn.data.model;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "tb_user")
public class User {

    @PrimaryKey
    int id;

    @ColumnInfo(name = "nama")
    String name;

    @ColumnInfo(name = "user_nama")
    String user_name;

    @ColumnInfo(name = "email")
    String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString(){
        return
                "userItem{" +
                        ",id = '" + id + '\'' +
                        ",name = '" + name + '\'' +
                        ",user_name = '" + user_name + '\'' +
                        ",email = '" + email + '\'' +
                        "}";
    }
}
