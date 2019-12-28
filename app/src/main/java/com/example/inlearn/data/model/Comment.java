package com.example.inlearn.data.model;

public class Comment {

    int id;

    String user_id;

    String user_name;

    String user_pict;

    String comment;

    String created_at;

    String like;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return user_name;
    }

    public void setName(String name) {
        this.user_name = name;
    }

    public String getPict() {
        return user_pict;
    }

    public void setPict(String pict) {
        this.user_pict = pict;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString(){
        return
                "commentItem{" +
                        ",id = '" + id + '\'' +
                        ",user_name = '" + user_name + '\'' +
                        ",user_pict = '" + user_pict+ '\'' +
                        ",comment = '" + comment + '\'' +
                        ",like = '" + like + '\'' +
                        ",created_at = '" + created_at + '\'' +
                        ",user_id = '" + user_id + '\'' +
                        "}";
    }
}
