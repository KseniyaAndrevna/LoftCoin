package com.kseniyaa.loftcoin.data.db.model;


import io.realm.RealmObject;

public class Transaction extends RealmObject {

    public String walletId;

    public double amount;

    public long date;

    public CoinEntyti coin;

    public Transaction() {
    }

    public Transaction(String walletId, double amount, long date, CoinEntyti coin) {
        this.walletId = walletId;
        this.amount = amount;
        this.date = date;
        this.coin = coin;
    }
}
