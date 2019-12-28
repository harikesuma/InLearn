package com.example.inlearn.data.room;

import androidx.room.Database;

import com.example.inlearn.data.model.Kategori;
import com.example.inlearn.data.model.Question;
import com.example.inlearn.data.model.TopOfUser;
import com.example.inlearn.data.model.User;

@Database(entities = {User.class, Kategori.class, Question.class, TopOfUser.class}, version = 2)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    public abstract UserDao userDao();
    public abstract KategoriDao kategoriDao();
    public abstract QuestionDao questionDao();
    public abstract TopOfUserDao topOfUserDao();



}
