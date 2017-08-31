/*
 * Copyright (c) 2016-2017 Onegini B.V.
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

import com.onegini.mobile.exampleapp.view.action.SimpleCustomAuthAuthenticationAction;
import com.onegini.mobile.exampleapp.view.action.SimpleCustomAuthDeregistrationAction;
import com.onegini.mobile.exampleapp.view.action.SimpleCustomAuthMobileAuthenticationAction;
import com.onegini.mobile.exampleapp.view.action.SimpleCustomAuthRegistrationAction;
import com.onegini.mobile.sdk.android.handlers.customauth.OneginiCustomAuthAuthenticationAction;
import com.onegini.mobile.sdk.android.handlers.customauth.OneginiCustomAuthDeregistrationAction;
import com.onegini.mobile.sdk.android.handlers.customauth.OneginiCustomAuthMobileAuthenticationAction;
import com.onegini.mobile.sdk.android.handlers.customauth.OneginiCustomAuthRegistrationAction;
import com.onegini.mobile.sdk.android.model.OneginiCustomAuthenticator;

public class SimpleCustomAuthenticator implements OneginiCustomAuthenticator {

  private final OneginiCustomAuthRegistrationAction registrationAction = new SimpleCustomAuthRegistrationAction();
  private final OneginiCustomAuthDeregistrationAction deregistrationAction = new SimpleCustomAuthDeregistrationAction();
  private final OneginiCustomAuthAuthenticationAction authAuthenticationAction = new SimpleCustomAuthAuthenticationAction();
  private final OneginiCustomAuthMobileAuthenticationAction mobileAuthenticationAction = new SimpleCustomAuthMobileAuthenticationAction();

  public SimpleCustomAuthenticator() { }

  @Override
  public OneginiCustomAuthRegistrationAction getRegistrationAction() {
    return registrationAction;
  }

  @Override
  public OneginiCustomAuthDeregistrationAction getDeregistrationAction() {
    return deregistrationAction;
  }

  @Override
  public OneginiCustomAuthAuthenticationAction getAuthenticationAction() {
    return authAuthenticationAction;
  }

  @Override
  public OneginiCustomAuthMobileAuthenticationAction getMobileAuthenticationAction() {
    return mobileAuthenticationAction;
  }

  @Override
  public String getId() {
    return "EXPERIMENTAL_CUSTOM_AUTHENTICATOR_ID"; // TODO change ID?
  }
}
