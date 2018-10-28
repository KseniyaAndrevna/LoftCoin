package com.kseniyaa.loftcoin.data.db.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.kseniyaa.loftcoin.data.db.model.Transaction;
import com.kseniyaa.loftcoin.data.db.model.TransactionModel;
import com.kseniyaa.loftcoin.data.db.model.Wallet;
import com.kseniyaa.loftcoin.data.db.model.WalletModel;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface WalletDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveWallet(Wallet wallet);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveTransactions(List<Transaction> transactions);

    @Query("SELECT w.*, c.* FROM Wallet w, Coin c WHERE w.currencyId = c.id")
    Flowable<List<WalletModel>> getWallets();

    @Query("SELECT t.*, c.* FROM `Transaction` t, Coin c WHERE t.walletId = :walletId AND t.currencyId = c.id ORDER BY date DESC")
    Flowable<List<TransactionModel>> getTransaction(String walletId);
}
