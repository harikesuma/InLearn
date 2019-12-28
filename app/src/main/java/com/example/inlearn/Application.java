package com.example.inlearn;

import android.content.Context;

import androidx.room.Room;

import com.example.inlearn.data.room.RoomDatabase;
import com.facebook.stetho.Stetho;

public class Application extends android.app.Application {
    private static Context mContext;
    public static RoomDatabase roomDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Stetho.initializeWithDefaults(this);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());


        roomDatabase = Room.databaseBuilder(getApplicationContext(),
                       RoomDatabase.class,"db_prakrikum_progmob")
                       .allowMainThreadQueries().build();

    }

    public static Context getContext(){
        return mContext;
    }
}
