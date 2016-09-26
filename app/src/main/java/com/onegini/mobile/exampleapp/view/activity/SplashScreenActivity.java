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

import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.storage.UserStorage;
import com.onegini.mobile.sdk.android.client.OneginiClient;
import com.onegini.mobile.sdk.android.handlers.OneginiInitializationHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiError;
import com.onegini.mobile.sdk.android.handlers.error.OneginiInitializationError;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;

public class SplashScreenActivity extends Activity {

  private UserStorage userStorage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.userStorage = new UserStorage(this);

    setContentView(R.layout.activity_login);
    setupOneginiSDK();
  }

  private void setupOneginiSDK() {
    final OneginiClient oneginiClient = OneginiSDK.getOneginiClient(this);
    oneginiClient.start(new OneginiInitializationHandler() {
      @Override
      public void onSuccess(final Set<UserProfile> removedUserProfiles) {
        boolean removedProfiles = !removedUserProfiles.isEmpty();
        if (removedProfiles) {
          removeUserProfiles(removedUserProfiles);
        }
        startLoginActivity();
      }

      @Override
      public void onError(final OneginiInitializationError error) {
        handleInitializationErrors(error);
      }
    });
  }

  private void handleInitializationErrors(final OneginiInitializationError error) {
    int errorType = error.getErrorType();
    switch (errorType) {
      case OneginiInitializationError.NETWORK_CONNECTIVITY_PROBLEM:
      case OneginiInitializationError.SERVER_NOT_REACHABLE:
        showToast("No internet connection.");
        break;
      case OneginiInitializationError.OUTDATED_APP:
        showToast("Please update this application in order to use it.");
        break;
      case OneginiInitializationError.OUTDATED_OS:
        showToast("Please update your Android version to use this application.");
        break;
      case OneginiInitializationError.DEVICE_DEREGISTERED:
        onDeviceDeregistered();
        break;
      case OneginiInitializationError.GENERAL_ERROR:
      default:
        // Just display the error for other, less relevant errors
        displayError(error);
        break;
    }
  }

  private void onDeviceDeregistered() {
    userStorage.clearStorage();
    showToast("Device deregistered");
  }

  private void displayError(final OneginiError error) {
    final StringBuilder stringBuilder = new StringBuilder("Error: ");
    stringBuilder.append(error.getErrorDescription());

    final Exception exception = error.getException();
    if (exception != null) {
      stringBuilder.append(" Check logcat for more details.");
      exception.printStackTrace();
    }

    showToast(stringBuilder.toString());
  }

  private void removeUserProfiles(final Set<UserProfile> removedUserProfiles) {
    for (final UserProfile userProfile : removedUserProfiles) {
      userStorage.removeUser(userProfile);
    }
  }

  private void startLoginActivity() {
    startActivity(new Intent(this, LoginActivity.class));
    finish();
  }

  private void showToast(final String message) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
  }
}
