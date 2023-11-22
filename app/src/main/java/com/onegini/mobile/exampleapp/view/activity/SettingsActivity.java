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

package com.onegini.mobile.exampleapp.view.activity;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.onegini.mobile.exampleapp.view.helper.ErrorMessageParser.parseErrorMessage;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.network.fcm.FCMRegistrationService;
import com.onegini.mobile.exampleapp.util.DeregistrationUtil;
import com.onegini.mobile.sdk.android.client.UserClient;
import com.onegini.mobile.sdk.android.handlers.OneginiChangePinHandler;
import com.onegini.mobile.sdk.android.handlers.OneginiMobileAuthEnrollmentHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiChangePinError;
import com.onegini.mobile.sdk.android.handlers.error.OneginiMobileAuthEnrollmentError;
import com.onegini.mobile.sdk.android.handlers.error.OneginiMobileAuthWithPushEnrollmentError;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;

public class SettingsActivity extends AppCompatActivity {

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.button_mobile_authentication)
  Button mobileAuthButton;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.button_mobile_authentication_push)
  Button mobileAuthPushButton;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.button_change_pin)
  Button changePinButton;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.button_change_authentication)
  Button changeAuthentication;
  @BindView(R.id.result)
  TextView resultTextView;
  @SuppressWarnings({ "unused", "WeakerAccess" })

  private UserProfile authenticatedUserProfile;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    ButterKnife.bind(this);

    authenticatedUserProfile = OneginiSDK.getOneginiClient(this).getUserClient().getAuthenticatedUserProfile();
  }

  @Override
  protected void onResume() {
    super.onResume();
    setupView();
  }

  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {
    switch (item.getItemId()) {
      // Respond to the action bar's Up/Home button
      case android.R.id.home:
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void setupView() {
    setupActionBar();
    setupMobileAuthButtons();
  }

  private void setupMobileAuthButtons() {
    mobileAuthButton.setText(R.string.settings_mobile_enrollment_off);
    mobileAuthPushButton.setEnabled(false);
    mobileAuthPushButton.setText(R.string.settings_mobile_push_enrollment_not_available);

    final UserClient userClient = OneginiSDK.getOneginiClient(this).getUserClient();
    if (userClient.isUserEnrolledForMobileAuth(authenticatedUserProfile)) {
      onMobileAuthEnabled();
    }
  }

  private void setupActionBar() {
    setSupportActionBar(toolbar);

    final ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setLogo(R.drawable.thales_icon_white);
      actionBar.setDisplayUseLogoEnabled(true);
      actionBar.setDisplayShowTitleEnabled(false);
    }
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.button_mobile_authentication)
  public void enrollMobileAuthentication() {
    final OneginiMobileAuthEnrollmentHandler mobileAuthEnrollmentHandler = new OneginiMobileAuthEnrollmentHandler() {
      @Override
      public void onSuccess() {
        onMobileAuthEnabled();
        resultTextView.setText(R.string.enable_mobile_authentication_finished_successfully);
      }

      @Override
      public void onError(final OneginiMobileAuthEnrollmentError error) {
        @OneginiMobileAuthEnrollmentError.MobileAuthEnrollmentErrorType final int errorType = error.getErrorType();
        final String errorMessage = parseErrorMessage(error);
        if (errorType == OneginiMobileAuthEnrollmentError.DEVICE_DEREGISTERED) {
          new DeregistrationUtil(SettingsActivity.this).onDeviceDeregistered();
          startLoginActivity(errorMessage);
        } else if (errorType == OneginiMobileAuthEnrollmentError.USER_NOT_AUTHENTICATED) {
          startLoginActivity(errorMessage);
        } else {
          resultTextView.setText(errorMessage);
        }
      }
    };
    OneginiSDK.getOneginiClient(this).getUserClient().enrollUserForMobileAuth(mobileAuthEnrollmentHandler);
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.button_mobile_authentication_push)
  public void enrollMobileAuthenticationWithPush() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      requestPermissions(new String[]{ Manifest.permission.POST_NOTIFICATIONS }, 1);
    }

    final FCMRegistrationService.PushEnrollmentHandler mobileAuthWithPushEnrollmentHandler =
        new FCMRegistrationService.PushEnrollmentHandler() {

          @Override
          public void onSuccess() {
            mobileAuthPushButton.setText(R.string.settings_mobile_push_enrollment_on);
            resultTextView.setText(R.string.enable_mobile_authentication_with_push_finished_successfully);
          }

          @Override
          public void onError(final Throwable throwable) {
            if (throwable instanceof OneginiMobileAuthWithPushEnrollmentError) {
              final OneginiMobileAuthWithPushEnrollmentError error = (OneginiMobileAuthWithPushEnrollmentError) throwable;
              @OneginiMobileAuthWithPushEnrollmentError.MobileAuthWithPushEnrollmentErrorType final int errorType = error.getErrorType();
              if (errorType == OneginiMobileAuthWithPushEnrollmentError.DEVICE_DEREGISTERED) {
                new DeregistrationUtil(SettingsActivity.this).onDeviceDeregistered();
                startLoginActivity(parseErrorMessage(error));
              }
              resultTextView.setText(parseErrorMessage(error));
            } else {
              resultTextView.setText(throwable.getMessage());
            }
          }
        };

    final FCMRegistrationService fcmRegistrationService = new FCMRegistrationService(this);
    fcmRegistrationService.enrollForPush(mobileAuthWithPushEnrollmentHandler);
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.button_change_pin)
  public void startChangePinFlow() {
    OneginiSDK.getOneginiClient(this).getUserClient().changePin(new OneginiChangePinHandler() {
      @Override
      public void onSuccess() {
        resultTextView.setText(R.string.change_pin_finished_successfully);
      }

      @Override
      public void onError(final OneginiChangePinError oneginiChangePinError) {
        @OneginiChangePinError.ChangePinErrorType int errorType = oneginiChangePinError.getErrorType();
        final String errorMessage = parseErrorMessage(oneginiChangePinError);
        if (errorType == OneginiChangePinError.USER_DEREGISTERED) {
          new DeregistrationUtil(SettingsActivity.this).onUserDeregistered(authenticatedUserProfile);
          startLoginActivity(errorMessage);
        } else if (errorType == OneginiChangePinError.DEVICE_DEREGISTERED) {
          new DeregistrationUtil(SettingsActivity.this).onDeviceDeregistered();
          startLoginActivity(errorMessage);
        }
        resultTextView.setText(errorMessage);
      }
    });
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.button_change_authentication)
  public void changeAuthentication() {
    startActivity(new Intent(this, SettingsAuthenticatorsActivity.class));
  }

  private void onMobileAuthEnabled() {
    mobileAuthButton.setText(R.string.settings_mobile_enrollment_on);
    final int googlePlayServicesAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
    if (googlePlayServicesAvailable == ConnectionResult.SUCCESS) {
      mobileAuthPushButton.setEnabled(true);
      mobileAuthPushButton.setText(R.string.settings_mobile_push_enrollment_off);
      if (OneginiSDK.getOneginiClient(this).getUserClient().isUserEnrolledForMobileAuthWithPush(authenticatedUserProfile)) {
        mobileAuthPushButton.setText(R.string.settings_mobile_push_enrollment_on);
      }
    }
  }

  private void startLoginActivity(final String errorMessage) {
    final Intent intent = new Intent(this, LoginActivity.class);
    intent.putExtra(LoginActivity.ERROR_MESSAGE_EXTRA, errorMessage);
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
  }
}
