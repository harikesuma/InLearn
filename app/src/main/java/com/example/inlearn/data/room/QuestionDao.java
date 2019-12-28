package com.example.inlearn.data.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.inlearn.data.model.Question;
import com.example.inlearn.data.model.User;

import java.util.List;

@Dao
public interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllQuestion(List<Question> questionList);

    @Query("SELECT * FROM tb_pertanyaan ORDER BY id DESC")
    List<Question> getAllQuestion();


}
