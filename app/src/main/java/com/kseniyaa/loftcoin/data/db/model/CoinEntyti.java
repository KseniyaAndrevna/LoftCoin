package com.kseniyaa.loftcoin.data.db.model;


import com.kseniyaa.loftcoin.data.model.Fiat;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class CoinEntyti extends RealmObject {

    @PrimaryKey
    public int id;

    public String name;

    public String symbol;

    public String slug;

    public int rank;

    public String updated;

    public QuoteEntity usd;

    public QuoteEntity rub;

    public QuoteEntity eur;


    public QuoteEntity getQuote(Fiat fiat) {
        QuoteEntity quote = null;

        switch (fiat) {
            case USD:
                quote = usd;
                break;

            case EUR:
                quote = eur;
                break;
            case RUB:
                quote = rub;
                break;
        }

        if (quote == null) {
            return usd;
        } else {
            return quote;
        }
    }
}
