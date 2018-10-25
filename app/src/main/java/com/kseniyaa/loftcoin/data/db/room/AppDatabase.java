package com.kseniyaa.loftcoin.data.db.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;
import com.kseniyaa.loftcoin.data.db.model.Wallet;

@Database(entities = {CoinEntyti.class, Wallet.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CoinDao coinDao();
    public abstract WalletDao walletDao();
}
