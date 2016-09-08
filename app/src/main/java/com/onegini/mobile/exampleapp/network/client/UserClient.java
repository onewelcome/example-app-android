package com.onegini.mobile.exampleapp.network.client;

import com.onegini.mobile.exampleapp.network.response.DevicesResponse;
import retrofit.http.GET;
import rx.Observable;

public interface UserClient {

  @GET("/devices")
  Observable<DevicesResponse> getDevices();
}
