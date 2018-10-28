package com.kseniyaa.loftcoin.data.db.room;

import com.kseniyaa.loftcoin.data.db.Database;
import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;
import com.kseniyaa.loftcoin.data.db.model.Transaction;
import com.kseniyaa.loftcoin.data.db.model.TransactionModel;
import com.kseniyaa.loftcoin.data.db.model.Wallet;
import com.kseniyaa.loftcoin.data.db.model.WalletModel;

import java.util.List;

import io.reactivex.Flowable;

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
    public Flowable<List<CoinEntyti>> getCoins() {
        return database.coinDao().getCoins();
    }

    @Override
    public CoinEntyti getCoin(String symbol) {
        return database.coinDao().getCoin(symbol);
    }

    @Override
    public void saveWallet(Wallet wallet) {
        database.walletDao().saveWallet(wallet);
    }

    @Override
    public Flowable<List<WalletModel>> getWallets() {
        return database.walletDao().getWallets();
    }

    @Override
    public void saveTransaction(List<Transaction> transactions) {
        database.walletDao().saveTransactions(transactions);
    }

    @Override
    public Flowable<List<TransactionModel>> getTransactions(String walletId) {
        return database.walletDao().getTransaction(walletId);
    }
}
