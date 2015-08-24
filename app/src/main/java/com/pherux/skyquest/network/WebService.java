package com.pherux.skyquest.network;

import com.pherux.skyquest.Constants;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Fernando Valdez on 8/18/15
 */
public interface WebService {
    @GET("/input/" + Constants.MOBILE_SERVICE_PUBLIC_KEY)
    void log(
            @Query("private_key") String key,
            @Query("battery") String battery,
            @Query("time") String time,
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("alt") String alt,
            @Query("notes") String notes,
            Callback<Response> cb);
}
