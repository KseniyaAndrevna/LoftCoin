package com.kseniyaa.loftcoin.data.db.model;

import com.kseniyaa.loftcoin.data.api.model.Coin;
import com.kseniyaa.loftcoin.data.api.model.Quote;
import com.kseniyaa.loftcoin.data.model.Fiat;

import java.util.ArrayList;
import java.util.List;

public class CoinEntityMapper {

    public List<CoinEntyti> mapCoins(List<Coin> coins) {
        List<CoinEntyti> entitys = new ArrayList<>();

        for (Coin coin : coins) {
            entitys.add(mapCoin(coin));
        }

        return entitys;
    }

    private CoinEntyti mapCoin(Coin coin) {
        CoinEntyti entity = new CoinEntyti();

        entity.id = coin.id;
        entity.name = coin.name;
        entity.symbol = coin.symbol;
        entity.slug = coin.slug;
        entity.rank = coin.rank;
        entity.updated = coin.updated;

        entity.usd = mapQuote(coin.quotes.get(Fiat.USD.name()));
        entity.rub = mapQuote(coin.quotes.get(Fiat.RUB.name()));
        entity.eur = mapQuote(coin.quotes.get(Fiat.EUR.name()));

        return entity;
    }

    private QuoteEntity mapQuote(Quote quote) {
        if (quote == null) {
            return null;
        }

        QuoteEntity entity = new QuoteEntity();

        entity.price = quote.price;
        entity.percentChange1h = quote.percentChange1h;
        entity.percentChange24h = quote.percentChange24h;
        entity.percentChange7dd = quote.percentChange7dd;

        return entity;
    }
}
