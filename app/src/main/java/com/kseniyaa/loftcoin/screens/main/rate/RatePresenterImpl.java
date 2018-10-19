package com.kseniyaa.loftcoin.screens.main.rate;

import android.support.annotation.Nullable;

import com.kseniyaa.loftcoin.data.api.Api;
import com.kseniyaa.loftcoin.data.api.model.Coin;
import com.kseniyaa.loftcoin.data.api.model.RateResponse;
import com.kseniyaa.loftcoin.data.db.Database;
import com.kseniyaa.loftcoin.data.db.model.CoinEntityMapper;
import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;
import com.kseniyaa.loftcoin.data.prefs.Prefs;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RatePresenterImpl implements RatePresenter {

    private Api api;
    private Prefs prefs;
    private Database database;
    private CoinEntityMapper mapper;

    @Nullable
    private RateView view;

    RatePresenterImpl(Api api, Prefs prefs, Database database, CoinEntityMapper mapper) {
        this.api = api;
        this.prefs = prefs;
        this.database = database;
        this.mapper = mapper;
    }

    @Override
    public void attachView(RateView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void getRate() {
        List<CoinEntyti> coins = database.getCoins();
        if (view != null) {
            view.setCoins(coins);
        }
    }

    private void loadRate() {
        api.ticker(prefs.getFiatCurrency().name(), "array").enqueue(new Callback<RateResponse>() {
            @Override
            public void onResponse(Call<RateResponse> call, Response<RateResponse> response) {
                if (response.body() != null) {
                    List<Coin> coins = response.body().data;
                    List<CoinEntyti> entytis = mapper.mapCoins(coins);

                    database.saveCoins(entytis);

                    if (view != null) {
                        view.setCoins(entytis);
                    }
                }

                if (view != null) {
                    view.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<RateResponse> call, Throwable t) {
                if (view != null) {
                    view.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        loadRate();
    }
}
