package com.kseniyaa.loftcoin.data.db.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Wallet extends RealmObject {

    @PrimaryKey
    public String walletId;

    public double amount;

    public CoinEntyti coin;

    public Wallet() {
    }

    public Wallet(String walletId, double amount, CoinEntyti coin) {

        this.walletId = walletId;
        this.amount = amount;
        this.coin = coin;
    }
}
