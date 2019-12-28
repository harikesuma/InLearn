package com.example.inlearn.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tb_top_of_user")
public class TopOfUser {

    @PrimaryKey(autoGenerate = true)
    public int id;

    String user_name;
    public String like_as;





    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getLike() {
        return like_as;
    }

    public void setLike(String like) {
        this.like_as = like;
    }

    @Override
    public String toString(){
        return
                "topOfUserItem{" +
                        ",like = '" + like_as + '\'' +
                        ",user_id = '" + user_name + '\'' +
                        "}";
    }

}
