package com.example.inlearn.data.model;

import java.util.List;

public class ResponseKategori {

    List<Kategori> kategoriList;
    String status;
    String msg;
    boolean error;

    public List<Kategori> getKategoriList() {
        return kategoriList;
    }

    public void setKategoriList(List<Kategori> kategoriList) {
        this.kategoriList = kategoriList;
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
                "ResponseKategori{" +
                        "kategoriList = '" + kategoriList + '\'' +
                        ",msg = '" + msg + '\'' +
                        "}";
    }
}

