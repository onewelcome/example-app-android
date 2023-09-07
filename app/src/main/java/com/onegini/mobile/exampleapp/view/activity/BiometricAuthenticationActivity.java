package com.onegini.mobile.exampleapp.view.activity;

import static com.onegini.mobile.exampleapp.Constants.COMMAND_START;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import butterknife.ButterKnife;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.view.handler.BiometricAuthenticationRequestHandler;

public class BiometricAuthenticationActivity extends AuthenticationActivity {

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_biometric);
    ButterKnife.bind(this);

    initialize();
  }

  @Override
  protected void initialize() {
    parseIntent();
    updateTexts();
  }

  @Override
  protected void parseIntent() {
    super.parseIntent();
    if (COMMAND_START.equals(command)) {
      startBiometricAuthentication();
    }
  }

  private void startBiometricAuthentication() {
    final BiometricPrompt biometricPrompt =
        new BiometricPrompt(this, ContextCompat.getMainExecutor(this), new BiometricPromptAuthenticationCallback());

    final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
        .setTitle(getString(R.string.biometric_authentication_title))
        .setSubtitle(getString(R.string.biometric_authentication_subtitle))
        .setNegativeButtonText(getString(R.string.btn_cancel_label))
        .build();

    biometricPrompt.authenticate(promptInfo, BiometricAuthenticationRequestHandler.CRYPTO_OBJECT);
  }

  private static class BiometricPromptAuthenticationCallback extends BiometricPrompt.AuthenticationCallback {

    @Override
    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
      super.onAuthenticationError(errorCode, errString);
      if (errorCode == BiometricPrompt.ERROR_CANCELED || errorCode == BiometricPrompt.ERROR_USER_CANCELED || errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
        BiometricAuthenticationRequestHandler.CALLBACK.fallbackToPin();
      }
      //TODO handle error in scope of SDKAND-1543
    }

    @Override
    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
      super.onAuthenticationSucceeded(result);
      BiometricAuthenticationRequestHandler.CALLBACK.userAuthenticatedSuccessfully();
    }

    @Override
    public void onAuthenticationFailed() {
      super.onAuthenticationFailed();
      //TODO handle error in scope of SDKAND-1543
    }
  }
}
