package com.kseniyaa.loftcoin.data.db.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;

@Database(entities = {CoinEntyti.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {

    abstract CoinDao coinDao();
}
