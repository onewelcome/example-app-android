/*
 * Copyright (c) 2016 Onegini B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.onegini.mobile.exampleapp.network;

import android.content.Context;
import com.onegini.mobile.exampleapp.model.ApplicationDetails;
import com.onegini.mobile.exampleapp.network.client.AnonymousClient;
import com.onegini.mobile.exampleapp.network.client.SecureResourceClient;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class AnonymousService {

  private static AnonymousService instance;

  public static AnonymousService getInstance(final Context context) {
    if (instance == null) {
      instance = new AnonymousService(context);
    }
    return instance;
  }

  private final AnonymousClient applicationDetailsClient;

  private AnonymousService(final Context context) {
    applicationDetailsClient = SecureResourceClient.prepareSecuredAnonymousClient(AnonymousClient.class, context);
  }

  public Observable<ApplicationDetails> getApplicationDetails() {
    return applicationDetailsClient.getApplicationDetails()
        .observeOn(AndroidSchedulers.mainThread());
  }
}