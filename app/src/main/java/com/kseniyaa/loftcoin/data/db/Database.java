package com.kseniyaa.loftcoin.data.db;

import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;

import java.util.List;

public interface Database {

    void saveCoins (List<CoinEntyti> coins);

    List<CoinEntyti> getCoins();
}
