package com.onegini.mobile.exampleapp.view.handler;

import static com.onegini.mobile.exampleapp.Constants.COMMAND_ASK_TO_ACCEPT_OR_DENY;
import static com.onegini.mobile.exampleapp.Constants.COMMAND_FINISH;
import static com.onegini.mobile.exampleapp.Constants.EXTRA_COMMAND;
import static com.onegini.mobile.exampleapp.view.activity.AuthenticationActivity.EXTRA_MESSAGE;
import static com.onegini.mobile.exampleapp.view.activity.AuthenticationActivity.EXTRA_USER_PROFILE_ID;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import com.onegini.mobile.exampleapp.view.activity.MobileAuthenticationBiometricActivity;
import com.onegini.mobile.sdk.android.handlers.request.OneginiMobileAuthWithPushBiometricRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiBiometricCallback;
import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthenticationRequest;

public class MobileAuthenticationBiometricRequestHandler implements OneginiMobileAuthWithPushBiometricRequestHandler {

  public static OneginiBiometricCallback CALLBACK;
  public static BiometricPrompt.CryptoObject CRYPTO_OBJECT;
  private final Context context;
  private String userProfileId;
  private String message;

  public MobileAuthenticationBiometricRequestHandler(final Context context) {
    this.context = context;
  }

  @Override
  public void startAuthentication(@NonNull OneginiMobileAuthenticationRequest oneginiMobileAuthenticationRequest,
                                  @NonNull BiometricPrompt.CryptoObject cryptoObject,
                                  @NonNull OneginiBiometricCallback oneginiBiometricCallback) {
    CALLBACK = oneginiBiometricCallback;
    CRYPTO_OBJECT = cryptoObject;
    userProfileId = oneginiMobileAuthenticationRequest.getUserProfile().getProfileId();
    message = oneginiMobileAuthenticationRequest.getMessage();
    notifyActivity(COMMAND_ASK_TO_ACCEPT_OR_DENY);
  }

  @Override
  public void finishAuthentication() {
    notifyActivity(COMMAND_FINISH);
  }

  private void notifyActivity(final String command) {
    final Intent intent = new Intent(context, MobileAuthenticationBiometricActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.putExtra(EXTRA_COMMAND, command);
    intent.putExtra(EXTRA_MESSAGE, message);
    intent.putExtra(EXTRA_USER_PROFILE_ID, userProfileId);
    context.startActivity(intent);
  }
}
