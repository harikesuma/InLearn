package com.example.inlearn.data.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.inlearn.data.model.Question;
import com.example.inlearn.data.model.TopOfUser;

import java.util.List;

@Dao
public interface TopOfUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTopOfUser(List<TopOfUser> topOfUserList);

    @Query("SELECT * FROM tb_top_of_user LIMIT 6")
    List<TopOfUser> getAllTopOfUser();

    @Query("DELETE FROM tb_top_of_user")
    void deleteAll();
}
