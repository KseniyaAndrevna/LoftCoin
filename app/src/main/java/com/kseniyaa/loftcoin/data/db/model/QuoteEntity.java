package com.kseniyaa.loftcoin.data.db.model;

import io.realm.RealmObject;

public class QuoteEntity extends RealmObject {

    public double price;

    public float percentChange1h;

    public float percentChange24h;

    public float percentChange7dd;

}
