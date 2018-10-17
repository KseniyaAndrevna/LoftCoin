package com.kseniyaa.loftcoin.data.api;

import com.kseniyaa.loftcoin.data.api.model.RateResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api  {
    @GET("ticker")
    Call<RateResponse> ticker(@Query("convert") String convert, @Query("structure") String structure);
}
