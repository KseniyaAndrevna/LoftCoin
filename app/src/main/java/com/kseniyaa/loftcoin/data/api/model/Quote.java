package com.kseniyaa.loftcoin.data.api.model;

import com.google.gson.annotations.SerializedName;

public class Quote {

    public double price;

    @SerializedName("percent_change_1h")
    public float percentChange1h;

    @SerializedName("percent_change_24h")
    public float percentChange24h;

    @SerializedName("percent_change_7d")
    public float percentChange7dd;
}
