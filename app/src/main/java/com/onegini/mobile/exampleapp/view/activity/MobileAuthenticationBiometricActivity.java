package com.onegini.mobile.exampleapp.view.activity;

import static com.onegini.mobile.exampleapp.Constants.COMMAND_ASK_TO_ACCEPT_OR_DENY;
import static com.onegini.mobile.exampleapp.Constants.COMMAND_START;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import butterknife.OnClick;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.view.handler.MobileAuthenticationBiometricRequestHandler;

public class MobileAuthenticationBiometricActivity extends BiometricAuthenticationActivity {


  protected void setupUi() {
    if (COMMAND_ASK_TO_ACCEPT_OR_DENY.equals(command)) {
      setFingerprintAuthenticationPermissionVisibility(true);
    } else if (COMMAND_START.equals(command)) {
      startBiometricAuthentication();
    } else {
      super.setupUi();
    }
  }

  @Override
  protected void setCancelButtonVisibility() {
    cancelButton.setVisibility(View.GONE);
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.auth_accept_button)
  public void onAuthenticationAcceptButtonClick() {
    startBiometricAuthentication();
    setFingerprintAuthenticationPermissionVisibility(false);
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.auth_deny_button)
  public void onDenyAuthenticationButtonClick() {
    if (MobileAuthenticationBiometricRequestHandler.CALLBACK != null) {
      MobileAuthenticationBiometricRequestHandler.CALLBACK.denyAuthenticationRequest();
      finish();
    }
  }

  private void startBiometricAuthentication() {
    final BiometricPrompt biometricPrompt =
        new BiometricPrompt(this, ContextCompat.getMainExecutor(this), new BiometricPromptAuthenticationCallback());

    final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
        .setTitle(getString(R.string.biometric_authentication_title))
        .setSubtitle(getString(R.string.biometric_authentication_subtitle))
        .setNegativeButtonText(getString(R.string.use_pin_authentication))
        .build();

    biometricPrompt.authenticate(promptInfo, MobileAuthenticationBiometricRequestHandler.CRYPTO_OBJECT);
  }

  private static class BiometricPromptAuthenticationCallback extends BiometricPrompt.AuthenticationCallback {

    @Override
    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
      if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
        MobileAuthenticationBiometricRequestHandler.CALLBACK.fallbackToPin();
      } else {
        MobileAuthenticationBiometricRequestHandler.CALLBACK.onBiometricAuthenticationError(errorCode);
      }
    }

    @Override
    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
      MobileAuthenticationBiometricRequestHandler.CALLBACK.userAuthenticatedSuccessfully();
    }

    @Override
    public void onAuthenticationFailed() {
    }
  }
}
