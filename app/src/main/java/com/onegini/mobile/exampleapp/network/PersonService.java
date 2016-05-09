package com.onegini.mobile.exampleapp.network;

import android.content.Context;
import com.onegini.mobile.exampleapp.model.Person;
import com.onegini.mobile.exampleapp.network.client.PersonClient;
import com.onegini.mobile.exampleapp.network.client.SecuredClient;
import rx.android.schedulers.AndroidSchedulers;

public class PersonService {

  private static PersonService instance;

  public static PersonService getInstance(Context context) {
    if (instance == null) {
      instance = new PersonService(context);
    }
    return instance;
  }

  private final PersonClient personClient;

  private PersonService(final Context context) {
    personClient = SecuredClient.prepareSecuredClient(PersonClient.class, context);
  }

  public rx.Observable<Person> getPerson() {
    return personClient.getPerson()
        .observeOn(AndroidSchedulers.mainThread());
  }
}
