package com.onegini.mobile.exampleapp.network.client;

import com.onegini.mobile.exampleapp.model.Person;
import retrofit.http.GET;
import rx.Observable;

public interface PersonClient {

  @GET("/api/persons")
  Observable<Person> getPerson();
}
