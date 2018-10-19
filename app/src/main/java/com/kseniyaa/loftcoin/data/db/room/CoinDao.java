package com.kseniyaa.loftcoin.data.db.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;

import java.util.List;

@Dao
public interface CoinDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void saveCoins (List<CoinEntyti> coins);

    @Query("SELECT * FROM Coin" )
    List<CoinEntyti> getCoins();
}
