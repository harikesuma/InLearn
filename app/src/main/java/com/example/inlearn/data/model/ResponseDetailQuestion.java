package com.example.inlearn.data.model;

public class ResponseDetailQuestion {

    String msg;
    int id;
    String user_name;
    String kategori;
    int kategori_id;
    String pertanyaan;
    String pict;
    String created_at;
    String edited;
    String user_pict;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public int getKategori_id() {
        return kategori_id;
    }

    public void setKategori_id(int kategori_id) {
        this.kategori_id = kategori_id;
    }

    public String getPertanyaan() {
        return pertanyaan;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
    }

    public String getPict() {
        return pict;
    }

    public void setPict(String pict) {
        this.pict = pict;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getEdited() {
        return edited;
    }

    public void setEdited(String edited) {
        this.edited = edited;
    }

    public String getUser_pict() {
        return user_pict;
    }

    public void setUser_pict(String user_pict) {
        this.user_pict = user_pict;
    }

    @Override
    public String toString(){
        return
                "ResponseDetailQuestion{" +
                        ",msg = '" + msg + '\'' +
                        ",id = '" + id + '\'' +
                        ",user_pict = '" + user_pict+ '\'' +
                        ",user_name = '" + user_name + '\'' +
                        ",kategori = '" + kategori + '\'' +
                        ",kategori_id = '" + kategori_id + '\'' +
                        ",pertanyaan = '" + pertanyaan + '\'' +
                        ",pict = '" + pict + '\'' +
                        ",edited = '" + edited + '\'' +
                        ",created_at = '" + created_at + '\'' +
                        "}";
    }


}
