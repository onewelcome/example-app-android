package com.onegini.mobile.exampleapp.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.onegini.mobile.android.sdk.client.OneginiClient;
import com.onegini.mobile.android.sdk.handlers.OneginiAuthenticationHandler;
import com.onegini.mobile.android.sdk.model.entity.UserProfile;
import com.onegini.mobile.exampleapp.Constants;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.model.User;
import com.onegini.mobile.exampleapp.storage.UserStorage;

public class RegistrationActivity extends Activity {

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.name_edit_text)
  EditText nameEditText;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.create_profile_button)
  Button createProfileButton;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.progress_bar_register)
  ProgressBar progressBar;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.layout_register_content)
  LinearLayout layoutRegisterContent;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.user_profile_debug)
  TextView userProfileDebugText;

  private UserProfile registeredProfile;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_registration);
    ButterKnife.bind(this);

    setupUserInterface();
    registerUser();
  }

  private void setupUserInterface() {
    createProfileButton.setEnabled(false);
    layoutRegisterContent.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);
    userProfileDebugText.setText("");
    nameEditText.addTextChangedListener(new ProfileNameChangeListener());
  }

  @Override
  public void onNewIntent(final Intent intent) {
    super.onNewIntent(intent);
    handleRedirection(intent.getData());
  }

  private void handleRedirection(final Uri uri) {
    final OneginiClient client = OneginiSDK.getOneginiClient(getApplicationContext());
    if (uri != null && uri.getScheme().equals(client.getConfigModel().getAppScheme())) {
      client.getUserClient().handleAuthorizationCallback(uri);
    }
  }

  private void registerUser() {
    final OneginiClient oneginiClient = OneginiSDK.getOneginiClient(this);
    oneginiClient.getUserClient().registerUser(Constants.DEFAULT_SCOPES, new OneginiAuthenticationHandler() {

      @Override
      public void authenticationSuccess(final UserProfile userProfile) {
        PinActivity.setRemainingFailedAttempts(0);
        registeredProfile = userProfile;
        userProfileDebugText.setText(userProfile.getProfileId());
        askForProfileName();
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

  private void askForProfileName() {
    progressBar.setVisibility(View.GONE);
    layoutRegisterContent.setVisibility(View.VISIBLE);
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.create_profile_button)
  public void onCreateProfileClick() {
    storeUserProfile();
    startDashboardActivity();
  }

  private void showToast(final String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  private void storeUserProfile() {
    final String name = nameEditText.getText().toString().trim();
    final User user = new User(registeredProfile, name);
    final UserStorage userStorage = new UserStorage(this);
    userStorage.saveUser(user);
  }

  private void startDashboardActivity() {
    final Intent intent = new Intent(this, DashboardActivity.class);
    startActivity(intent);
    finish();
  }

  private class ProfileNameChangeListener implements TextWatcher {

    @Override
    public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

    }

    @Override
    public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {

    }

    @Override
    public void afterTextChanged(final Editable s) {
      final String name = s.toString().trim();
      if (name.isEmpty()) {
        createProfileButton.setEnabled(false);
      } else {
        createProfileButton.setEnabled(true);
      }
    }
  }
}
