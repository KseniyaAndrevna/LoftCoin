package com.kseniyaa.loftcoin.data.db;

import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;

import java.util.List;

import io.reactivex.Flowable;

public interface Database {

    void saveCoins (List<CoinEntyti> coins);

    Flowable<List<CoinEntyti>> getCoins();
}
