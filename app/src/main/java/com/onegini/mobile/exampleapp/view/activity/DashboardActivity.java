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
package com.onegini.mobile.exampleapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.model.User;
import com.onegini.mobile.exampleapp.storage.UserStorage;
import com.onegini.mobile.exampleapp.util.DeregistrationUtil;
import com.onegini.mobile.sdk.android.client.OneginiClient;
import com.onegini.mobile.sdk.android.handlers.OneginiDeregisterUserProfileHandler;
import com.onegini.mobile.sdk.android.handlers.OneginiLogoutHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiDeregistrationError;
import com.onegini.mobile.sdk.android.handlers.error.OneginiLogoutError;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;

public class DashboardActivity extends AppCompatActivity {

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.toolbar)
  Toolbar toolbar;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.dashboard_welcome_text)
  TextView dashboardWelcomeText;

  private UserStorage userStorage;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dashboard);
    ButterKnife.bind(this);

    userStorage = new UserStorage(this);
    setupUserInterface();
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.button_logout)
  public void logout() {
    OneginiSDK.getOneginiClient(this).getUserClient().logout(
        new OneginiLogoutHandler() {
          @Override
          public void onSuccess() {
            // Go to login screen
            showToast("logoutSuccess");
            startLoginActivity();
          }

          @Override
          public void onError(final OneginiLogoutError oneginiLogoutError) {
            handleLogoutError(oneginiLogoutError);
          }
        }
    );
  }

  private void handleLogoutError(final OneginiLogoutError oneginiLogoutError) {
    if (oneginiLogoutError.getErrorType() == OneginiLogoutError.LOCAL_LOGOUT) {
      showToast("The user was only logged out on the device. The access token has not been invalidated on the server-side.");
    } else if (oneginiLogoutError.getErrorType() == OneginiLogoutError.GENERAL_ERROR) {
      // General error handling for other, less relevant errors
      showToast("Logout error: " + oneginiLogoutError.getErrorDescription());
    }
    startLoginActivity();
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.button_deregister_user)
  public void deregisterUser() {
    final OneginiClient oneginiClient = OneginiSDK.getOneginiClient(this);
    final UserProfile userProfile = oneginiClient.getUserClient().getAuthenticatedUserProfile();
    if (userProfile == null) {
      showToast("userProfile == null");
      return;
    }

    oneginiClient.getUserClient().deregisterUser(userProfile, new OneginiDeregisterUserProfileHandler() {
          @Override
          public void onSuccess() {
            onUserDeregistered(userProfile);
          }

          @Override
          public void onError(final OneginiDeregistrationError oneginiDeregistrationError) {
            handleDeregistrationError(oneginiDeregistrationError, userProfile);
          }
        }
    );
  }

  private void handleDeregistrationError(final OneginiDeregistrationError oneginiDeregistrationError, final UserProfile userProfile) {
    new DeregistrationUtil(this).onUserDeregistered(userProfile);

    if (oneginiDeregistrationError.getErrorType() == OneginiDeregistrationError.LOCAL_DEREGISTRATION) {
      showToast("The user was only logged out on the device. The access token has not been invalidated on the server-side.");
    } else if (oneginiDeregistrationError.getErrorType() == OneginiDeregistrationError.GENERAL_ERROR) {
      // General error handling for other, less relevant errors
      showToast("Logout error: " + oneginiDeregistrationError.getErrorDescription());
    }
    startLoginActivity();
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.button_your_devices)
  public void startDevicesActivity() {
    startActivity(new Intent(this, DevicesListActivity.class));
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.button_settings)
  public void startSettingsActivity() {
    startActivity(new Intent(this, SettingsActivity.class));
  }


  private void showToast(final String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  private void setupUserInterface() {
    setupActionBar();
    setupWelcomeText();
  }

  private void setupWelcomeText() {
    final UserProfile userProfile = OneginiSDK.getOneginiClient(this).getUserClient().getAuthenticatedUserProfile();
    final User user = userStorage.loadUser(userProfile);
    dashboardWelcomeText.setText(getString(R.string.welcome_user_text, user.getName()));
  }

  private void setupActionBar() {
    setSupportActionBar(toolbar);

    final ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayShowHomeEnabled(true);
      actionBar.setLogo(R.mipmap.ic_launcher);
      actionBar.setDisplayUseLogoEnabled(true);
      actionBar.setDisplayShowTitleEnabled(false);
    }
  }

  private void onUserDeregistered(final UserProfile userProfile) {
    new DeregistrationUtil(this).onUserDeregistered(userProfile);
    showToast("deregisterUserSuccess");

    startLoginActivity();
  }

  private void startLoginActivity() {
    final Intent intent = new Intent(this, LoginActivity.class);
    startActivity(intent);
    finish();
  }
}
