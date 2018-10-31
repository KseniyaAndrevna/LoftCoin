package com.kseniyaa.loftcoin.data.db;

import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;
import com.kseniyaa.loftcoin.data.db.model.Transaction;
import com.kseniyaa.loftcoin.data.db.model.Wallet;

import java.util.List;

import io.reactivex.Flowable;

public interface Database {

    void saveCoins(List<CoinEntyti> coins);

    void saveWallet(Wallet wallet);

    void saveTransaction(List<Transaction> transactions);

    Flowable<List<CoinEntyti>> getCoins();

    Flowable<List<Wallet>> getWallets();

    Flowable<List<Transaction>> getTransactions(String walletId);

    CoinEntyti getCoin(String symbol);

    void open();

    void close();

}
