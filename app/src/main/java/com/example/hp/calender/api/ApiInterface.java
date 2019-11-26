package com.example.hp.calender.api;

import com.example.hp.calender.model.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("events/")
    Call<Example> getCalendarEvents(@Query("key")String apiKey);
}
