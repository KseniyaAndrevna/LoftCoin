package com.kseniyaa.loftcoin.screens.main.wallets;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.kseniyaa.loftcoin.App;
import com.kseniyaa.loftcoin.data.db.Database;
import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;
import com.kseniyaa.loftcoin.data.db.model.Wallet;
import com.kseniyaa.loftcoin.data.db.model.WalletModel;
import com.kseniyaa.loftcoin.utils.SingleLiveEvent;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WalletsViewModelImpl extends WalletsViewModel {

    private SingleLiveEvent<Object> selectCurrency = new SingleLiveEvent<>();
    private MutableLiveData<Boolean> walletsVisible = new MutableLiveData<>();
    private MutableLiveData<Boolean> newWalletVisible = new MutableLiveData<>();
    private MutableLiveData<List<WalletModel>> walletsItems = new MutableLiveData<>();

    private CompositeDisposable disposables = new CompositeDisposable();

    private Database database;

    public WalletsViewModelImpl(@NonNull Application application) {
        super(application);

        database = ((App) getApplication()).getDatabase();
    }

    @Override
    public void onNewWalletClick() {
        selectCurrency.postValue(new Object());
    }

    @Override
    public LiveData<Object> selectCurrency() {
        return selectCurrency;
    }

    @Override
    public LiveData<List<WalletModel>> wallets() {
        return walletsItems;
    }

    @Override
    public LiveData<Boolean> walletsVisible() {
        return walletsVisible;
    }

    @Override
    public LiveData<Boolean> newWalletVisible() {
        return newWalletVisible;
    }

    @Override
    public void onCurrencySelected(CoinEntyti coin) {
        Wallet wallet = randomWallet(coin);

        Disposable disposable = Observable.fromCallable(() -> {
                    database.saveWallet(wallet);
                    return new Object();
                }
        )
                .subscribeOn(Schedulers.io())
                .subscribe();

        disposables.add(disposable);
    }

    @Override
    public void getWallets() {
        getWalletsInner();
    }

    private void getWalletsInner() {
        Disposable disposable = database.getWallets()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        wallets -> {
                            if (wallets.isEmpty()) {
                                walletsVisible.setValue(false);
                                newWalletVisible.setValue(true);
                            } else {
                                walletsVisible.setValue(true);
                                newWalletVisible.setValue(false);

                                walletsItems.setValue(wallets);
                            }
                        });
        disposables.add(disposable);
    }

    private Wallet randomWallet(CoinEntyti coin) {
        Random random = new Random();
        return new Wallet(UUID.randomUUID().toString(), coin.id, 10 + random.nextDouble());
    }
}
