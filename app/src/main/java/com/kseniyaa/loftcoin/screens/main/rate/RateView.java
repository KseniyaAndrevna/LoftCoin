package com.kseniyaa.loftcoin.screens.main.rate;

import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;

import java.util.List;

public interface RateView {

    void setCoins(List<CoinEntyti> coins);

    void setRefreshing (Boolean refreshing);

    void showCurrencyDialog();

    void showProgress();

    void hideProgress();
}
