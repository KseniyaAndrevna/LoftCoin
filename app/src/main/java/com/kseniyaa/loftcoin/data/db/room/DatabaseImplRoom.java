package com.kseniyaa.loftcoin.data.db.room;

import com.kseniyaa.loftcoin.data.db.Database;
import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;

import java.util.List;

public class DatabaseImplRoom implements Database {

    private AppDatabase database;

    public DatabaseImplRoom(AppDatabase database) {
        this.database = database;
    }

    @Override
    public void saveCoins(List<CoinEntyti> coins) {
        database.coinDao().saveCoins(coins);
    }

    @Override
    public List<CoinEntyti> getCoins() {
        return database.coinDao().getCoins();
    }
}
