package com.kseniyaa.loftcoin.screens.main.rate;

import com.kseniyaa.loftcoin.data.api.model.Coin;

import java.util.List;

public interface RateView {

    void setCoins(List<Coin> coins);

    void setRefreshing (Boolean refreshing);

    void showCurrencyDialog();
}