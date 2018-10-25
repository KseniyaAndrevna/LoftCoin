package com.kseniyaa.loftcoin.data.db;

import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;
import com.kseniyaa.loftcoin.data.db.model.Wallet;
import com.kseniyaa.loftcoin.data.db.model.WalletModel;

import java.util.List;

import io.reactivex.Flowable;

public interface Database {

    void saveCoins (List<CoinEntyti> coins);

    Flowable<List<CoinEntyti>> getCoins();

    Flowable<List<WalletModel>> getWallets();


    CoinEntyti getCoin(String symbol);

    void saveWallet (Wallet wallet);


}
