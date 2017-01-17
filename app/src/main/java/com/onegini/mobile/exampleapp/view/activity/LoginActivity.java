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

package com.onegini.mobile.exampleapp.view.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.model.ApplicationDetails;
import com.onegini.mobile.exampleapp.model.User;
import com.onegini.mobile.exampleapp.network.AnonymousService;
import com.onegini.mobile.exampleapp.storage.RetrofitClientSettingsStorage;
import com.onegini.mobile.exampleapp.storage.UserStorage;
import com.onegini.mobile.exampleapp.util.DeregistrationUtil;
import com.onegini.mobile.sdk.android.handlers.OneginiAuthenticationHandler;
import com.onegini.mobile.sdk.android.handlers.OneginiDeviceAuthenticationHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiAuthenticationError;
import com.onegini.mobile.sdk.android.handlers.error.OneginiDeviceAuthenticationError;
import com.onegini.mobile.sdk.android.handlers.error.OneginiError;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import rx.Subscription;

public class LoginActivity extends Activity {

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.label)
  TextView label;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.users_spinner)
  Spinner usersSpinner;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.login_button)
  Button loginButton;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.register_button)
  Button registerButton;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.progress_bar_login)
  ProgressBar progressBar;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.layout_login_content)
  RelativeLayout layoutLoginContent;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.application_details)
  TextView applicationDetailsTextView;

  private List<User> listOfUsers = new ArrayList<>();
  private boolean userIsLoggingIn = false;
  private Subscription subscription;
  private UserProfile authenticatedUserProfile;
  private RetrofitClientSettingsStorage retrofitClientSettingsStorage;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);
    retrofitClientSettingsStorage = new RetrofitClientSettingsStorage(this);
    authenticateDevice();
  }

  private void authenticateDevice() {
    authenticatedUserProfile = OneginiSDK.getOneginiClient(this).getUserClient().getAuthenticatedUserProfile();
    OneginiSDK.getOneginiClient(this).getDeviceClient()
        .authenticateDevice(new String[]{ "application-details" }, new OneginiDeviceAuthenticationHandler() {
              @Override
              public void onSuccess() {
                callAnonymousResourceCallToFetchApplicationDetails();
              }

              @Override
              public void onError(final OneginiDeviceAuthenticationError error) {
                final @OneginiDeviceAuthenticationError.DeviceAuthenticationErrorType int errorType = error.getErrorType();
                if (errorType == OneginiDeviceAuthenticationError.DEVICE_DEREGISTERED) {
                  onDeviceDeregistered();
                } else if (errorType == OneginiDeviceAuthenticationError.USER_DEREGISTERED) {
                  onUserDeregistered(authenticatedUserProfile);
                } else {
                  displayError(error);
                }
              }
            }
        );
  }

  private void callAnonymousResourceCallToFetchApplicationDetails() {
    final boolean useRetrofit2 = retrofitClientSettingsStorage.shouldUseRetrofit2();
    subscription = AnonymousService.getInstance(this)
        .getApplicationDetails(useRetrofit2)
        .subscribe(this::onApplicationDetailsFetched, throwable -> onApplicationDetailsFetchFailed());
  }

  private void onApplicationDetailsFetched(final ApplicationDetails applicationDetails) {
    applicationDetailsTextView.setText(applicationDetails.getApplicationDetailsCombined());
  }

  private void onApplicationDetailsFetchFailed() {
    applicationDetailsTextView.setText("Application details fetch failed");
  }

  @Override
  protected void onResume() {
    super.onResume();
    setupUserInterface();
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.login_button)
  public void loginButtonClicked() {
    setProgressbarVisibility(true);

    final User user = listOfUsers.get(usersSpinner.getSelectedItemPosition());
    loginUser(user.getUserProfile());
    userIsLoggingIn = true;
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.register_button)
  public void registerButtonClicked() {
    final Intent intent = new Intent(this, RegistrationActivity.class);
    startActivity(intent);
    finish();
  }

  private void setupUserInterface() {
    if (userIsLoggingIn) {
      registerButton.setVisibility(View.INVISIBLE);
    } else {
      setProgressbarVisibility(false);
      registerButton.setVisibility(View.VISIBLE);
    }

    if (isRegisteredAtLeastOneUser()) {
      prepareListOfProfiles();
      setupUsersSpinner();
      loginButton.setVisibility(View.VISIBLE);
    } else {
      usersSpinner.setVisibility(View.INVISIBLE);
      loginButton.setVisibility(View.INVISIBLE);
    }
  }

  private void setupUsersSpinner() {
    usersSpinner.setVisibility(View.VISIBLE);

    final ArrayAdapter<User> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listOfUsers);
    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    usersSpinner.setAdapter(spinnerArrayAdapter);
  }

  private void loginUser(final UserProfile userProfile) {
    OneginiSDK.getOneginiClient(this).getUserClient().authenticateUser(userProfile, new OneginiAuthenticationHandler() {

      @Override
      public void onSuccess(final UserProfile userProfile) {
        startDashboardActivity();
      }

      @Override
      public void onError(final OneginiAuthenticationError oneginiAuthenticationError) {
        userIsLoggingIn = false;
        setProgressbarVisibility(true);
        handleAuthenticationErrors(oneginiAuthenticationError, userProfile);
      }
    });
  }

  private void handleAuthenticationErrors(final OneginiAuthenticationError error, final UserProfile userProfile) {
    @OneginiAuthenticationError.AuthenticationErrorType int errorType = error.getErrorType();
    switch (errorType) {
      case OneginiAuthenticationError.ACTION_CANCELED:
        showToast("Authentication was cancelled");
        break;
      case OneginiAuthenticationError.NETWORK_CONNECTIVITY_PROBLEM:
      case OneginiAuthenticationError.SERVER_NOT_REACHABLE:
        showToast("No internet connection.");
        break;
      case OneginiAuthenticationError.OUTDATED_APP:
        showToast("Please update this application in order to use it.");
        break;
      case OneginiAuthenticationError.OUTDATED_OS:
        showToast("Please update your Android version to use this application.");
        break;
      case OneginiAuthenticationError.USER_DEREGISTERED:
        onUserDeregistered(userProfile);
        break;
      case OneginiAuthenticationError.DEVICE_DEREGISTERED:
        onDeviceDeregistered();
        break;
      case OneginiAuthenticationError.GENERAL_ERROR:
      default:
        // Just display the error for other, less relevant errors
        displayError(error);
        break;
    }
  }

  private void onUserDeregistered(final UserProfile userProfile) {
    new DeregistrationUtil(this).onUserDeregistered(userProfile);
    showToast("User deregistered");
    setupUserInterface();
  }

  private void onDeviceDeregistered() {
    new DeregistrationUtil(this).onDeviceDeregistered();
    showToast("Device deregistered");
    setupUserInterface();
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

  private boolean isRegisteredAtLeastOneUser() {
    final Set<UserProfile> userProfiles = OneginiSDK.getOneginiClient(this).getUserClient().getUserProfiles();
    return userProfiles.size() > 0;
  }

  private void setProgressbarVisibility(final boolean isVisible) {
    progressBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);

    registerButton.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    layoutLoginContent.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    if (isRegisteredAtLeastOneUser()) {
      loginButton.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }
  }

  private void prepareListOfProfiles() {
    final Set<UserProfile> userProfiles = OneginiSDK.getOneginiClient(this).getUserClient().getUserProfiles();
    final UserStorage userStorage = new UserStorage(this);
    listOfUsers = userStorage.loadUsers(userProfiles);
  }

  private void showToast(final String message) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
  }

  private void startDashboardActivity() {
    startActivity(new Intent(this, DashboardActivity.class));
    finish();
  }

  @Override
  public void onDestroy() {
    if (subscription != null) {
      subscription.unsubscribe();
    }
    super.onDestroy();
  }
}
