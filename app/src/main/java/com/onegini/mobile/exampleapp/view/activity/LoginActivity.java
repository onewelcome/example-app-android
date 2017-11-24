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

import static com.onegini.mobile.exampleapp.view.helper.ErrorMessageParser.parseErrorMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SwitchCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.model.ApplicationDetails;
import com.onegini.mobile.exampleapp.model.ImplicitUserDetails;
import com.onegini.mobile.exampleapp.model.User;
import com.onegini.mobile.exampleapp.network.AnonymousService;
import com.onegini.mobile.exampleapp.network.ImplicitUserService;
import com.onegini.mobile.exampleapp.storage.DeviceSettingsStorage;
import com.onegini.mobile.exampleapp.storage.UserStorage;
import com.onegini.mobile.exampleapp.util.DeregistrationUtil;
import com.onegini.mobile.exampleapp.view.helper.AlertDialogFragment;
import com.onegini.mobile.exampleapp.view.helper.NotificationMenuHelper;
import com.onegini.mobile.exampleapp.view.helper.RegisteredAuthenticatorsMenu;
import com.onegini.mobile.sdk.android.client.UserClient;
import com.onegini.mobile.sdk.android.handlers.OneginiAuthenticationHandler;
import com.onegini.mobile.sdk.android.handlers.OneginiDeviceAuthenticationHandler;
import com.onegini.mobile.sdk.android.handlers.OneginiImplicitAuthenticationHandler;
import com.onegini.mobile.sdk.android.handlers.OneginiPendingMobileAuthWithPushRequestsHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiAuthenticationError;
import com.onegini.mobile.sdk.android.handlers.error.OneginiDeviceAuthenticationError;
import com.onegini.mobile.sdk.android.handlers.error.OneginiError;
import com.onegini.mobile.sdk.android.handlers.error.OneginiImplicitTokenRequestError;
import com.onegini.mobile.sdk.android.handlers.error.OneginiPendingMobileAuthWithPushRequestError;
import com.onegini.mobile.sdk.android.model.OneginiAuthenticator;
import com.onegini.mobile.sdk.android.model.entity.CustomAuthenticatorInfo;
import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthWithPushRequest;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import rx.Subscription;

public class LoginActivity extends Activity {

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.label)
  TextView label;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.users_spinner)
  Spinner usersSpinner;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.login_button)
  Button loginButton;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.register_button)
  Button registerButton;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.progress_bar_login)
  ProgressBar progressBar;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.layout_login_content)
  RelativeLayout layoutLoginContent;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.application_details)
  TextView applicationDetailsTextView;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.login_with_preferred_authenticator)
  SwitchCompat usePreferredAuthenticatorSwitchCompat;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.implicit_user_details)
  TextView implicitUserDetailsTextView;
  @BindView(R.id.bottom_navigation)
  BottomNavigationView bottomNavigationView;

  public static final String ERROR_MESSAGE_EXTRA = "error_message";

  private List<User> listOfUsers = new ArrayList<>();
  private boolean userIsLoggingIn = false;
  private Subscription subscription;
  private UserProfile authenticatedUserProfile;
  private DeviceSettingsStorage deviceSettingsStorage;
  private String errorMessage;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);
    deviceSettingsStorage = new DeviceSettingsStorage(this);
    authenticateDevice();
  }

  @Override
  public void onDestroy() {
    if (subscription != null) {
      subscription.unsubscribe();
    }
    super.onDestroy();
  }

  @Override
  protected void onResume() {
    super.onResume();
    getErrorMessage();
    setupUserInterface();
  }

  private void getErrorMessage() {
    if (getIntent().hasExtra(ERROR_MESSAGE_EXTRA)) {
      errorMessage = getIntent().getStringExtra(ERROR_MESSAGE_EXTRA);
    }
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
                errorMessage = parseErrorMessage(error);
                if (errorType == OneginiDeviceAuthenticationError.DEVICE_DEREGISTERED) {
                  onDeviceDeregistered();
                } else if (errorType == OneginiDeviceAuthenticationError.USER_DEREGISTERED) {
                  onUserDeregistered();
                } else {
                  LoginActivity.this.onError(error);
                }
              }
            }
        );
  }

  private void callAnonymousResourceCallToFetchApplicationDetails() {
    final boolean useRetrofit2 = deviceSettingsStorage.shouldUseRetrofit2();
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

  @SuppressWarnings("unused")
  @OnCheckedChanged(R.id.login_with_preferred_authenticator)
  public void usePreferredAuthenticatorSwitchStateChanged() {
    if (usePreferredAuthenticatorSwitchCompat.isChecked()) {
      loginButton.setText(getString(R.string.btn_login_label));
    } else {
      loginButton.setText(getString(R.string.btn_login_with_label));
    }
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.login_button)
  public void loginButtonClicked() {
    final UserProfile userProfile = listOfUsers.get(usersSpinner.getSelectedItemPosition()).getUserProfile();
    if (usePreferredAuthenticatorSwitchCompat.isChecked()) {
      authenticate(userProfile, null);
    } else {
      showRegisteredAuthenticatorsPopup(userProfile);
    }
  }

  private void showRegisteredAuthenticatorsPopup(@NonNull final UserProfile userProfile) {
    final Set<OneginiAuthenticator> registeredAuthenticators = OneginiSDK.getOneginiClient(this).getUserClient().getRegisteredAuthenticators(userProfile);
    final RegisteredAuthenticatorsMenu menu = new RegisteredAuthenticatorsMenu(new PopupMenu(this, loginButton), registeredAuthenticators);
    menu.setOnClickListener(authenticator -> authenticate(userProfile, authenticator)).show();
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.register_button)
  public void registerButtonClicked() {
    final Intent intent = new Intent(this, RegistrationActivity.class);
    startActivity(intent);
    finish();
  }

  @SuppressWarnings("unused")
  @OnItemSelected(R.id.users_spinner)
  void onItemSelected(int position) {
    final User user = listOfUsers.get(position);
    getImplicitUserDetails(user.getUserProfile());
  }

  private void getImplicitUserDetails(final UserProfile userProfile) {
    OneginiSDK.getOneginiClient(this).getUserClient()
        .authenticateUserImplicitly(userProfile, new String[]{ "read" }, new OneginiImplicitAuthenticationHandler() {
          @Override
          public void onSuccess(final UserProfile profile) {
            callImplicitResourceCallToFetchImplicitUserDetails();
          }

          @Override
          public void onError(final OneginiImplicitTokenRequestError oneginiImplicitTokenRequestError) {
            handleImplicitAuthenticationErrors(oneginiImplicitTokenRequestError, userProfile);
            implicitUserDetailsTextView.setText(oneginiImplicitTokenRequestError.getErrorType() + ": " +
                getString(R.string.implicit_user_details_fetch_failed_label));
          }
        });
  }

  private void callImplicitResourceCallToFetchImplicitUserDetails() {
    final boolean useRetrofit2 = deviceSettingsStorage.shouldUseRetrofit2();
    subscription = ImplicitUserService.getInstance(this)
        .getImplicitUserDetails(useRetrofit2)
        .subscribe(this::onImplicitUserDetailsFetched, this::onImplicitDetailsFetchFailed);
  }

  private void onImplicitUserDetailsFetched(final ImplicitUserDetails implicitUserDetails) {
    implicitUserDetailsTextView.setText(implicitUserDetails.toString());
  }

  private void onImplicitDetailsFetchFailed(final Throwable throwable) {
    implicitUserDetailsTextView.setText(R.string.implicit_user_details_fetch_failed_label);
    throwable.printStackTrace();
  }

  private void handleImplicitAuthenticationErrors(final OneginiImplicitTokenRequestError error, final UserProfile userProfile) {
    @OneginiImplicitTokenRequestError.ImplicitTokenRequestErrorType int errorType = error.getErrorType();
    switch (errorType) {
      case OneginiImplicitTokenRequestError.USER_DEREGISTERED:
        new DeregistrationUtil(this).onUserDeregistered(userProfile);
        break;
      case OneginiImplicitTokenRequestError.DEVICE_DEREGISTERED:
        new DeregistrationUtil(this).onDeviceDeregistered();
        break;
      case OneginiImplicitTokenRequestError.NETWORK_CONNECTIVITY_PROBLEM:
      case OneginiImplicitTokenRequestError.SERVER_NOT_REACHABLE:
      case OneginiImplicitTokenRequestError.CONFIGURATION_ERROR:
      case OneginiImplicitTokenRequestError.GENERAL_ERROR:
      default:
        // Just display the error for other, less relevant errors
        error.printStackTrace();
        break;
    }
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
      usePreferredAuthenticatorSwitchCompat.setVisibility(View.VISIBLE);
    } else {
      usersSpinner.setVisibility(View.INVISIBLE);
      loginButton.setVisibility(View.INVISIBLE);
      usePreferredAuthenticatorSwitchCompat.setVisibility(View.INVISIBLE);
    }

    if (errorMessage != null && !errorMessage.isEmpty()) {
      showError();
    }
    setupNavigationDrawer();
  }

  private void setupUsersSpinner() {
    usersSpinner.setVisibility(View.VISIBLE);

    final ArrayAdapter<User> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listOfUsers);
    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    usersSpinner.setAdapter(spinnerArrayAdapter);
  }

  private void authenticate(final UserProfile userProfile, @Nullable final OneginiAuthenticator authenticator) {
    setProgressbarVisibility(true);
    userIsLoggingIn = true;

    final UserClient userClient = OneginiSDK.getOneginiClient(this).getUserClient();
    final OneginiAuthenticationHandler handler = getAuthenticationHandler(userProfile);

    if (authenticator == null) {
      userClient.authenticateUser(userProfile, handler);
    } else {
      userClient.authenticateUser(userProfile, authenticator, handler);
    }
  }

  private OneginiAuthenticationHandler getAuthenticationHandler(final UserProfile userProfile) {
    return new OneginiAuthenticationHandler() {

      @Override
      public void onSuccess(final UserProfile userProfile, final CustomAuthenticatorInfo customAuthenticatorInfo) {
        startDashboardActivity();
      }

      @Override
      public void onError(final OneginiAuthenticationError oneginiAuthenticationError) {
        LoginActivity.this.onError(oneginiAuthenticationError, userProfile);
      }
    };
  }

  private void onError(final OneginiAuthenticationError oneginiAuthenticationError, final UserProfile userProfile) {
    userIsLoggingIn = false;
    setProgressbarVisibility(true);
    handleAuthenticationErrors(oneginiAuthenticationError, userProfile);
  }

  private void handleAuthenticationErrors(final OneginiAuthenticationError error, final UserProfile userProfile) {
    @OneginiAuthenticationError.AuthenticationErrorType int errorType = error.getErrorType();
    errorMessage = error.getErrorType() + ": ";
    switch (errorType) {
      case OneginiAuthenticationError.ACTION_CANCELED:
        errorMessage += "Authentication was cancelled";
        break;
      case OneginiAuthenticationError.NETWORK_CONNECTIVITY_PROBLEM:
        errorMessage += "No internet connection.";
        break;
      case OneginiAuthenticationError.SERVER_NOT_REACHABLE:
        errorMessage += "The server is not reachable.";
        break;
      case OneginiAuthenticationError.OUTDATED_APP:
        errorMessage += "Please update this application in order to use it.";
        break;
      case OneginiAuthenticationError.OUTDATED_OS:
        errorMessage += "Please update your Android version to use this application.";
        break;
      case OneginiAuthenticationError.USER_DEREGISTERED:
        new DeregistrationUtil(this).onUserDeregistered(userProfile);
        errorMessage += "User deregistered";
        break;
      case OneginiAuthenticationError.DEVICE_DEREGISTERED:
        new DeregistrationUtil(this).onDeviceDeregistered();
        errorMessage += "Device deregistered";
        break;
      case OneginiAuthenticationError.GENERAL_ERROR:
      default:
        // Just display the error for other, less relevant errors
        onError(error);
        break;
    }
  }

  private void onDeviceDeregistered() {
    new DeregistrationUtil(this).onDeviceDeregistered();
    showError();
  }

  private void onUserDeregistered() {
    new DeregistrationUtil(this).onUserDeregistered(authenticatedUserProfile);
    showError();
  }

  private void onError(final OneginiError error) {
    error.printStackTrace();
    errorMessage = error.getMessage() + " Check logcat for more details.";
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
      usePreferredAuthenticatorSwitchCompat.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }
  }

  private void prepareListOfProfiles() {
    final Set<UserProfile> userProfiles = OneginiSDK.getOneginiClient(this).getUserClient().getUserProfiles();
    final UserStorage userStorage = new UserStorage(this);
    listOfUsers = userStorage.loadUsers(userProfiles);
  }

  private void showError() {
    final DialogFragment dialog = AlertDialogFragment.newInstance(errorMessage);
    errorMessage = null;
    dialog.show(getFragmentManager(), "alert_dialog");
  }

  private void startDashboardActivity() {
    startActivity(new Intent(this, DashboardActivity.class));
    finish();
  }

  private void setupNavigationDrawer() {
    bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
      if (item.getItemId() == R.id.action_notifications) {
        startActivity(new Intent(LoginActivity.this, PendingPushMessagesActivity.class));
        return true;
      }
      return false;
    });

    OneginiSDK.getOneginiClient(this).getUserClient().getPendingMobileAuthWithPushRequests(new OneginiPendingMobileAuthWithPushRequestsHandler() {
      @Override
      public void onSuccess(final Set<OneginiMobileAuthWithPushRequest> set) {
        final MenuItem menuItem = bottomNavigationView.getMenu().findItem(R.id.action_notifications);
        menuItem.setIcon(NotificationMenuHelper.getNotificationIcon(set.size()));
        menuItem.setTitle(NotificationMenuHelper.getNotificationTitle(LoginActivity.this, set.size()));
      }

      @Override
      public void onError(final OneginiPendingMobileAuthWithPushRequestError oneginiPendingMobileAuthWithPushRequestError) {
        LoginActivity.this.onError(oneginiPendingMobileAuthWithPushRequestError);
      }
    });
  }
}
