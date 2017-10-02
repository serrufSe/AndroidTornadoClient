package com.example.adm.myapplication;

import retrofit2.http.*;
import rx.Observable;


public interface TornadoAPI {

    @GET("/items")
    Observable<DTO> getItems();

    @POST("/bind")
    Observable<Void> bindApplication(@Query("token") String token, @Query("item") String item);

}
