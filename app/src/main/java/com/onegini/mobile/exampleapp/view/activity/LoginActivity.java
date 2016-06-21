package com.onegini.mobile.exampleapp.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.onegini.mobile.exampleapp.Constants;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.sdk.android.library.OneginiClient;
import com.onegini.mobile.sdk.android.library.handlers.OneginiAuthenticationHandler;
import com.onegini.mobile.sdk.android.library.model.entity.UserProfile;

public class LoginActivity extends Activity {

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.login_button)
  Button loginButton;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.progress_bar_login)
  ProgressBar progressBar;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.layout_login_content)
  RelativeLayout layoutLoginContent;

  public static void startActivity(@NonNull final Activity context) {
    final Intent intent = new Intent(context, LoginActivity.class);
    context.startActivity(intent);
    context.finish();
  }

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);

    setProgressbarVisibility(false);
  }

  @Override
  protected void onResume() {
    super.onResume();
    setupLoginButtonText();
  }

  @Override
  public void onNewIntent(final Intent intent) {
    super.onNewIntent(intent);
    handleRedirection(intent.getData());
  }

  private void handleRedirection(final Uri uri) {
    final OneginiClient client = OneginiSDK.getOneginiClient(this);
    if (uri != null && uri.getScheme().equals(client.getConfigModel().getAppScheme())) {
      client.handleAuthorizationCallback(uri);
    }
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.login_button)
  public void onButtonClicked() {
    setProgressbarVisibility(true);

    loginOrRegisterUser();
  }

  private void loginOrRegisterUser() {
    OneginiClient oneginiClient = OneginiSDK.getOneginiClient(this);
    UserProfile userProfile = oneginiClient.getAuthenticatedUserProfile();
    if (oneginiClient.isRegistered() && userProfile != null) {
      loginUser(userProfile);
    } else {
      registerUser();
    }
  }

  private void registerUser() {
    OneginiSDK.getOneginiClient(this).registerUser(Constants.DEFAULT_SCOPES, new OneginiAuthenticationHandler() {

      @Override
      public void authenticationSuccess(final UserProfile userProfile) {
        PinActivity.setRemainingFailedAttempts(0);
        loginUser(userProfile);
      }

      @Override
      public void authenticationError() {
        showToast("authenticationError");
      }

      @Override
      public void authenticationException(final Exception e) {
        showToast("authenticationException");
      }

      @Override
      public void authenticationErrorInvalidRequest() {
        showToast("authenticationErrorInvalidRequest");
      }

      @Override
      public void authenticationErrorClientRegistrationFailed() {
        showToast("authenticationErrorClientRegistrationFailed");
      }

      @Override
      public void authenticationErrorInvalidState() {
        showToast("authenticationErrorInvalidState");
      }

      @Override
      public void authenticationErrorInvalidGrant(final int remaining) {
        // Show error the token was invalid, user should authorize again.
        setProgressbarVisibility(true);
        PinActivity.setRemainingFailedAttempts(remaining);
        registerUser();
      }

      @Override
      public void authenticationErrorNotAuthenticated() {
        showToast("authenticationErrorNotAuthenticated");
      }

      @Override
      public void authenticationErrorInvalidScope() {
        showToast("authenticationErrorInvalidScope");
      }

      @Override
      public void authenticationErrorNotAuthorized() {
        showToast("authenticationErrorNotAuthorized");
      }

      @Override
      public void authenticationErrorInvalidGrantType() {
        showToast("authenticationErrorInvalidGrantType");
      }

      @Override
      public void authenticationErrorTooManyPinFailures() {
        showToast("authenticationErrorTooManyPinFailures");
      }

      @Override
      public void authenticationErrorInvalidApplication() {
        showToast("authenticationErrorInvalidApplication");
      }

      @Override
      public void authenticationErrorUnsupportedOS() {
        showToast("authenticationErrorUnsupportedOS");
      }

      @Override
      public void authenticationErrorInvalidProfile() {
        showToast("authenticationErrorInvalidProfile");
      }
    });
  }

  private void loginUser(UserProfile userProfile) {
    OneginiSDK.getOneginiClient(this).authenticateUser(userProfile, Constants.DEFAULT_SCOPES, new OneginiAuthenticationHandler() {
      @Override
      public void authenticationSuccess(final UserProfile userProfileSuccessfullyAuthenticated) {
        DashboardActivity.startActivity(LoginActivity.this);
      }

      @Override
      public void authenticationError() {
        showToast("authenticationError");
      }

      @Override
      public void authenticationException(final Exception e) {
        showToast("authenticationException");
      }

      @Override
      public void authenticationErrorInvalidRequest() {
        showToast("authenticationErrorInvalidRequest");
      }

      @Override
      public void authenticationErrorClientRegistrationFailed() {
        showToast("authenticationErrorClientRegistrationFailed");
      }

      @Override
      public void authenticationErrorInvalidState() {
        showToast("authenticationErrorInvalidState");
      }

      @Override
      public void authenticationErrorInvalidGrant(final int remaining) {
        setProgressbarVisibility(true);
        PinActivity.setRemainingFailedAttempts(remaining);
        loginUser(userProfile);
      }

      @Override
      public void authenticationErrorNotAuthenticated() {
        showToast("authenticationErrorNotAuthenticated");
      }

      @Override
      public void authenticationErrorInvalidScope() {
        showToast("authenticationErrorInvalidScope");
      }

      @Override
      public void authenticationErrorNotAuthorized() {
        showToast("authenticationErrorNotAuthorized");
      }

      @Override
      public void authenticationErrorInvalidGrantType() {
        showToast("authenticationErrorInvalidGrantType");
      }

      @Override
      public void authenticationErrorTooManyPinFailures() {
        showToast("authenticationErrorTooManyPinFailures");
      }

      @Override
      public void authenticationErrorInvalidApplication() {
        showToast("authenticationErrorInvalidApplication");
      }

      @Override
      public void authenticationErrorUnsupportedOS() {
        showToast("authenticationErrorUnsupportedOS");
      }

      @Override
      public void authenticationErrorInvalidProfile() {
        showToast("authenticationErrorInvalidProfile");
      }
    });
  }

  private void setProgressbarVisibility(final boolean isVisible) {
    progressBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);

    layoutLoginContent.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    loginButton.setVisibility(isVisible ? View.GONE : View.VISIBLE);
  }

  private void setupLoginButtonText() {
    final String buttonLabel = OneginiSDK.getOneginiClient(this).isRegistered() ? getString(R.string.btn_login_label) : getString(R.string.btn_register_label);
    loginButton.setText(buttonLabel);
  }

  private void showToast(final String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
}
