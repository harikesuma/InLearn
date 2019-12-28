package com.example.inlearn.data.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tb_kategori")
public class Kategori {

    @PrimaryKey
    int id;

    @ColumnInfo(name = "kategori")
    String kategori;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    @Override
    public String toString(){
        return
                "kategoriItem{" +
                        ",id = '" + id + '\'' +
                        ",kategori = '" + kategori + '\'' +
                        "}";
    }
}
