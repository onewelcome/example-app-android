package com.onegini.mobile.exampleapp.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.sdk.android.library.OneginiClient;
import com.onegini.mobile.sdk.android.library.handlers.OneginiAuthorizationHandler;

public class LoginActivity extends Activity {

  @Bind(R.id.login_button)
  Button loginButton;
  @Bind(R.id.progress_bar_login)
  ProgressBar progressBar;
  @Bind(R.id.layout_login_content)
  RelativeLayout layoutLoginContent;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);

    initLoginButtonListener();
  }

  @Override
  protected void onResume() {
    super.onResume();
    setupLoginButtonText();
    setProgressbarVisibility(false);
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

  private void initLoginButtonListener() {
    loginButton.setOnClickListener(v -> onButtonClicked());
  }

  private void onButtonClicked() {
    setProgressbarVisibility(true);
    authenticateUser();
  }

  @Override
  public void onNewIntent(final Intent intent) {
    super.onNewIntent(intent);

    final Uri uri = intent.getData();
    handleRedirection(uri);
  }

  private void handleRedirection(final Uri uri) {
    final OneginiClient client = OneginiSDK.getOneginiClient(this);
    if (uri != null && uri.getScheme().equals(client.getConfigModel().getAppScheme())) {
      client.handleAuthorizationCallback(uri);
    }
  }

  private void authenticateUser() {
    OneginiSDK.getOneginiClient(this).authorize(new String[]{ "read" }, new OneginiAuthorizationHandler() {
      @Override
      public void authorizationSuccess() {
        // Show user is logged in.
      }

      @Override
      public void authorizationError() {
        // Show error a general error occurred.
      }

      @Override
      public void authorizationException(final Exception exception) {
        // Show error an exception occurred, for example the storage was corrupted.
      }

      @Override
      public void authorizationErrorInvalidRequest() {
        // Show error the requests send by the SDK were not accepted by the Token Server.
      }

      @Override
      public void authorizationErrorClientRegistrationFailed() {
        // Show error the device was not able to perform DCR, potential timing issue or the current app version is not supported anymore.
      }

      @Override
      public void authorizationErrorInvalidState() {
        // Show error the callback failed due to an invalid state param, retry the operation.
      }

      @Override
      public void authorizationErrorInvalidGrant(final int remaining) {
        // Show error the token was invalid, user should authorize again.
      }

      @Override
      public void authorizationErrorNotAuthenticated() {
        // Show error the client crentials used are invalid, user should authorize again.
      }

      @Override
      public void authorizationErrorInvalidScope() {
        // Show error the requested scope is invalid and not available for this client.
      }

      @Override
      public void authorizationErrorNotAuthorized() {
        // Show error the application is not authorized to perform this operation.
      }

      @Override
      public void authorizationErrorInvalidGrantType() {
        // Show error the operation requested by the application is not supported by the token server.
      }

      @Override
      public void authorizationErrorTooManyPinFailures() {
        // Show error the wrong pin was used for too many times, the user should authorize again.
      }

      @Override
      public void authorizationErrorInvalidApplication() {
        // Show error the application uses wrong version and the update is needed.
      }

      @Override
      public void authorizationErrorUnsupportedOS() {
        // Show error the device is using usupported OS version, the user should upgrade his OS.
      }
    });
  }
}
