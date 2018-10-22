package com.kseniyaa.loftcoin.data.db;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.kseniyaa.loftcoin.data.db.room.AppDatabase;
import com.kseniyaa.loftcoin.data.db.room.DatabaseImplRoom;

public class DatabaseInitialaizer {

    public Database inint(Context context) {
        AppDatabase appDatabase = Room
                .databaseBuilder(context, AppDatabase.class, "loftcoin.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        return new DatabaseImplRoom(appDatabase);
    }
}
