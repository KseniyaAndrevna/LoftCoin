package com.kseniyaa.loftcoin.data.db;

import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;
import com.kseniyaa.loftcoin.data.db.model.Transaction;
import com.kseniyaa.loftcoin.data.db.model.TransactionModel;
import com.kseniyaa.loftcoin.data.db.model.Wallet;
import com.kseniyaa.loftcoin.data.db.model.WalletModel;

import java.util.List;

import io.reactivex.Flowable;

public interface Database {

    void saveCoins(List<CoinEntyti> coins);

    void saveWallet(Wallet wallet);

    void saveTransaction(List<Transaction> transactions);


    Flowable<List<CoinEntyti>> getCoins();

    Flowable<List<WalletModel>> getWallets();

    Flowable<List<TransactionModel>> getTransactions(String walletId);

    CoinEntyti getCoin(String symbol);

}
