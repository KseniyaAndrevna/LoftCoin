package com.kseniyaa.loftcoin.screens.main.wallets;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;
import com.kseniyaa.loftcoin.data.db.model.Transaction;
import com.kseniyaa.loftcoin.data.db.model.Wallet;

import java.util.List;

public abstract class WalletsViewModel extends AndroidViewModel {

    public WalletsViewModel(@NonNull Application application) {
        super(application);
    }

    public abstract void onNewWalletClick();

    public abstract LiveData<Object> selectCurrency();

    public abstract LiveData<Object> scrollToNewWallet();

    public abstract LiveData<List<Wallet>> wallets();

    public abstract LiveData<List<Transaction>> transactions();

    public abstract LiveData<Boolean> walletsVisible();

    public abstract LiveData<Boolean> newWalletVisible();

    public abstract void onCurrencySelected(CoinEntyti coin);

    public abstract void getWallets();

    public abstract void onWalletChange(int position);
}
