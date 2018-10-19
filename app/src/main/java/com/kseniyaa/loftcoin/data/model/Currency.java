package com.kseniyaa.loftcoin.data.model;

import android.support.annotation.DrawableRes;

import com.kseniyaa.loftcoin.R;

public enum Currency {

    BTC(R.drawable.ic_currency_bt),
    DOGE(R.drawable.ic_currency_doge),
    ETH(R.drawable.ic_currency_et),
    XMR(R.drawable.ic_currency_xm),
    XRP(R.drawable.ic_currency_xr),
    DASH(R.drawable.ic_currency_dash);

    public int iconRes;


    Currency(@DrawableRes int iconRes) {
        this.iconRes = iconRes;
    }

    public static Currency getCurrency(String currency) {
        try {
            return Currency.valueOf(currency);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
