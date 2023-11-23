package com.onegini.mobile.exampleapp.view.handler;

import static com.onegini.mobile.exampleapp.Constants.COMMAND_FINISH;
import static com.onegini.mobile.exampleapp.Constants.COMMAND_START;
import static com.onegini.mobile.exampleapp.Constants.EXTRA_COMMAND;
import static com.onegini.mobile.exampleapp.view.activity.AuthenticationActivity.EXTRA_USER_PROFILE_ID;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import com.onegini.mobile.exampleapp.view.activity.BiometricAuthenticationActivity;
import com.onegini.mobile.sdk.android.handlers.request.OneginiBiometricAuthenticationRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiBiometricCallback;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;

public class BiometricAuthenticationRequestHandler implements OneginiBiometricAuthenticationRequestHandler {

  public static OneginiBiometricCallback CALLBACK;
  public static BiometricPrompt.CryptoObject CRYPTO_OBJECT;
  private final Context context;
  private String userProfileId;

  public BiometricAuthenticationRequestHandler(final Context context) {
    this.context = context;
  }

  @Override
  public void startAuthentication(@NonNull UserProfile userProfile, @NonNull BiometricPrompt.CryptoObject cryptoObject,
                                  @NonNull OneginiBiometricCallback oneginiBiometricCallback) {
    CALLBACK = oneginiBiometricCallback;
    CRYPTO_OBJECT = cryptoObject;
    userProfileId = userProfile.getProfileId();
    notifyActivity(COMMAND_START);
  }

  @Override
  public void finishAuthentication() {
    CRYPTO_OBJECT = null;
    CALLBACK = null;
    notifyActivity(COMMAND_FINISH);
  }

  private void notifyActivity(final String command) {
    final Intent intent = new Intent(context, BiometricAuthenticationActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    intent.putExtra(EXTRA_COMMAND, command);
    intent.putExtra(EXTRA_USER_PROFILE_ID, userProfileId);
    context.startActivity(intent);
  }
}

