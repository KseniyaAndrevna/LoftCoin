package com.kseniyaa.loftcoin.data.db.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.kseniyaa.loftcoin.data.model.Fiat;

@Entity(tableName = "Coin")
public class CoinEntyti {

    @PrimaryKey
    public int id;

    public String name;

    public String symbol;

    public String slug;

    public int rank;

    public String updated;

    @Embedded(prefix = "usd_")
    public QuoteEntity usd;

    @Embedded(prefix = "rub_")
    public QuoteEntity rub;

    @Embedded(prefix = "eur_")
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
