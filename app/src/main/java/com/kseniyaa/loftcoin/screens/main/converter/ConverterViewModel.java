package com.kseniyaa.loftcoin.screens.main.converter;

import android.os.Bundle;

import com.kseniyaa.loftcoin.data.db.model.CoinEntyti;

import io.reactivex.Observable;

interface ConverterViewModel {

    Observable<String> sourceCurrency();

    Observable<String> destinationCurrency();

    Observable<String> destinationAmount();

    Observable<Object> selectSourceCurrency();

    Observable<Object> selectDestinationCurrency();

    void onSourceAmountChange(String amount);

    void onSourceCurrencyClick();

    void onDestinationCurrencyClick();

    void onSourceCurrencySelected(CoinEntyti coin);

    void onDestinationCurrencySelected(CoinEntyti coin);

    void saveState(Bundle outState);

    void onDetach();
}
