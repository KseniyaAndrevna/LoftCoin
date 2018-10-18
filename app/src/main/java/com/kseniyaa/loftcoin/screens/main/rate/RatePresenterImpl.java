package com.kseniyaa.loftcoin.screens.main.rate;

import android.support.annotation.Nullable;

import com.kseniyaa.loftcoin.data.api.Api;
import com.kseniyaa.loftcoin.data.api.model.RateResponse;
import com.kseniyaa.loftcoin.data.prefs.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RatePresenterImpl implements RatePresenter {

    private Api api;
    private Prefs prefs;

    @Nullable
    private RateView view;

    RatePresenterImpl(Api api, Prefs prefs) {
        this.api = api;
        this.prefs = prefs;
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
        api.ticker(prefs.getFiatCurrency().name(), "array").enqueue(new Callback<RateResponse>() {
            @Override
            public void onResponse(Call<RateResponse> call, Response<RateResponse> response) {
                if (view != null && response.body() != null) {
                    view.setCoins(response.body().data);
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
        getRate();

    }
}
