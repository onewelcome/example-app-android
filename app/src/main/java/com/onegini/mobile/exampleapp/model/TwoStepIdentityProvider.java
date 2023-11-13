/*
 * Copyright (c) 2016-2018 Onegini B.V.
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
package com.onegini.mobile.exampleapp.model;

import android.content.Context;
import com.onegini.mobile.exampleapp.view.action.twowayotpidentityprovider.TwoStepRegistrationAction;
import com.onegini.mobile.sdk.android.handlers.action.OneginiCustomRegistrationAction;
import com.onegini.mobile.sdk.android.handlers.action.OneginiCustomTwoStepRegistrationAction;
import com.onegini.mobile.sdk.android.model.OneginiCustomIdentityProvider;

public class TwoStepIdentityProvider implements OneginiCustomIdentityProvider {

  private final OneginiCustomTwoStepRegistrationAction registrationAction;

  public TwoStepIdentityProvider(final Context context) {
    this.registrationAction = new TwoStepRegistrationAction(context);
  }

  @Override
  public OneginiCustomRegistrationAction getRegistrationAction() {
    return registrationAction;
  }

  @Override
  public String getId() {
    return "New2step";
  }
}
