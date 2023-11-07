package com.onegini.mobile.exampleapp.view.activity;

import static com.onegini.mobile.exampleapp.Constants.COMMAND_START;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.view.handler.BiometricAuthenticationRequestHandler;

public class BiometricAuthenticationActivity extends AuthenticationActivity {

  @BindView(R.id.content_accept_deny)
  FrameLayout layoutAcceptDeny;

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
    setFingerprintAuthenticationPermissionVisibility(false);
    setCancelButtonVisibility();
    setupUi();
  }

  protected void setupUi() {
    if (COMMAND_START.equals(command)) {
      startBiometricAuthentication();
    }
  }

  protected void setFingerprintAuthenticationPermissionVisibility(final boolean isVisible) {
    layoutAcceptDeny.setVisibility(isVisible ? View.VISIBLE : View.GONE);
  }

  protected void setCancelButtonVisibility() {
    cancelButton.setVisibility(View.VISIBLE);
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
      BiometricAuthenticationRequestHandler.CALLBACK.onBiometricAuthenticationError(errorCode);
    }

    @Override
    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
      BiometricAuthenticationRequestHandler.CALLBACK.userAuthenticatedSuccessfully();
    }

    @Override
    public void onAuthenticationFailed() {
    }
  }
}
