package com.kseniyaa.loftcoin.screens.main.rate;

import com.kseniyaa.loftcoin.data.model.Fiat;

public interface RatePresenter {

    void attachView (RateView view);

    void detachView ();

    void getRate();

    void onRefresh();

    void onMenuItemCurrencyClick();

    void onFiatCurrencySelected(Fiat currency);

    void onRateLongClick(String symbol);
}
