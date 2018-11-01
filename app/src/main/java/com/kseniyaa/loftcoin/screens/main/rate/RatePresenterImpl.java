package com.kseniyaa.loftcoin.screens.main.rate;

import android.support.annotation.Nullable;

import com.kseniyaa.loftcoin.data.api.Api;
import com.kseniyaa.loftcoin.data.api.model.Coin;
import com.kseniyaa.loftcoin.data.db.Database;
import com.kseniyaa.loftcoin.data.db.model.CoinEntityMapper;
import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;
import com.kseniyaa.loftcoin.data.model.Fiat;
import com.kseniyaa.loftcoin.data.prefs.Prefs;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class RatePresenterImpl implements RatePresenter {

    private Api api;
    private Prefs prefs;
    private Database mainDatabase;
    private Database workerDatabase;
    private CoinEntityMapper mapper;
    private CompositeDisposable disposables = new CompositeDisposable();


    @Nullable
    private RateView view;

    RatePresenterImpl(Api api, Prefs prefs, Database mainDatabase, Database workerDatabase, CoinEntityMapper mapper) {
        this.api = api;
        this.prefs = prefs;
        this.mainDatabase = mainDatabase;
        this.workerDatabase = workerDatabase;
        this.mapper = mapper;
    }

    @Override
    public void attachView(RateView view) {
        this.view = view;
        mainDatabase.open();
    }

    @Override
    public void detachView() {
        mainDatabase.close();
        disposables.dispose();
        this.view = null;
    }

    @Override
    public void getRate() {
        Disposable disposable = mainDatabase.getCoins()
                .subscribe(
                        coinEntyti -> {
                            if (view != null) {
                                view.setCoins(coinEntyti);
                            }
                        },
                        throwable -> {
                        }
                );
        disposables.add(disposable);
    }

    private void loadRate(Boolean fromRefresh) {

        if (!fromRefresh) {
            if (view != null) {
                view.showProgress();
            }
        }

        Disposable disposable = api.ticker(prefs.getFiatCurrency().name(), "array")
                .subscribeOn(Schedulers.io())
                .map(rateResponse -> {
                    List<Coin> coins = rateResponse.data;
                    List<CoinEntyti> coinEntities = mapper.mapCoins(coins);
                    workerDatabase.open();
                    workerDatabase.saveCoins(coinEntities);
                    workerDatabase.close();

                    return new Object();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        object -> {
                            if (view != null) {
                                if (fromRefresh) {
                                    view.setRefreshing(false);
                                } else {
                                    view.hideProgress();
                                }
                            }

                        }, throwable -> {
                            if (fromRefresh) {
                                view.setRefreshing(false);
                            } else {
                                view.hideProgress();
                            }
                        }
                );
        disposables.add(disposable);
    }

    @Override
    public void onRefresh() {
        loadRate(true);
    }

    @Override
    public void onMenuItemCurrencyClick() {
        view.showCurrencyDialog();
    }

    @Override
    public void onFiatCurrencySelected(Fiat currency) {
        prefs.setFiatCurrency(currency);
        loadRate(false);
    }
}
