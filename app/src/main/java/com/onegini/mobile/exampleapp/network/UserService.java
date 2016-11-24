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
import com.onegini.mobile.exampleapp.network.client.UserClient;
import com.onegini.mobile.exampleapp.network.client.SecureResourceClient;
import com.onegini.mobile.exampleapp.network.response.DevicesResponse;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class UserService {

  private static UserService instance;

  public static UserService getInstance(final Context context) {
    if (instance == null) {
      instance = new UserService(context);
    }
    return instance;
  }

  private final UserClient userClient;

  private UserService(final Context context) {
    userClient = SecureResourceClient.prepareSecuredUserClient(UserClient.class, context);
  }

  public Observable<DevicesResponse> getDevices() {
    return userClient.getDevices()
        .observeOn(AndroidSchedulers.mainThread());
  }
}
