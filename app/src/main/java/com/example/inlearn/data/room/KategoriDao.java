package com.example.inlearn.data.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.example.inlearn.data.model.Kategori;
import com.example.inlearn.data.model.User;

import java.util.List;

@Dao
public interface KategoriDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllKategori(List<Kategori> kategoriList);
}
