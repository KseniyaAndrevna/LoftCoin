package com.kseniyaa.loftcoin.screens.start;

import android.support.annotation.Nullable;

import com.kseniyaa.loftcoin.data.api.Api;
import com.kseniyaa.loftcoin.data.api.model.Coin;
import com.kseniyaa.loftcoin.data.db.Database;
import com.kseniyaa.loftcoin.data.db.model.CoinEntityMapper;
import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;
import com.kseniyaa.loftcoin.data.prefs.Prefs;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StartPresenterImpl implements StartPresenter {

    private static final String TAG = "StartPresenterImpl";

    private Api api;
    private Prefs prefs;
    private Database database;
    private CoinEntityMapper mapper;

    private CompositeDisposable disposables = new CompositeDisposable();

    @Nullable
    private StartView view;

    StartPresenterImpl(Api api, Prefs prefs, Database database, CoinEntityMapper mapper) {
        this.api = api;
        this.prefs = prefs;
        this.database = database;
        this.mapper = mapper;
    }

    @Override
    public void attachView(StartView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        disposables.dispose();
        this.view = null;
    }

    @Override
    public void loadRate() {

        Disposable disposable = api.ticker(prefs.getFiatCurrency().name(), "array")
                .subscribeOn(Schedulers.io())
                .map(rateResponse -> {
                    List<Coin> coins = rateResponse.data;
                    List<CoinEntyti> coinEntities = mapper.mapCoins(coins);
                    database.open();
                    database.saveCoins(coinEntities);
                    database.close();

                    return coinEntities;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        coinEntities -> {
                            if (view != null) {
                                view.navigateToMainScreen();
                            }

                        }, throwable -> {

                        }
                );
        disposables.add(disposable);
    }
}













