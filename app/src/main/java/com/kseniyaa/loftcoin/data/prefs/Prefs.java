package com.kseniyaa.loftcoin.data.prefs;

import com.kseniyaa.loftcoin.data.model.Fiat;

public interface Prefs {

    boolean isFirstLaunch();

    void setFirstLaunch (Boolean firstLaunch);

    Fiat getFiatCurrency();

    void setFiatCurrency(Fiat currency);

}
