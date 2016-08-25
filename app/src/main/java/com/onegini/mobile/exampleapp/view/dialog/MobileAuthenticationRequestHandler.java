package com.onegini.mobile.exampleapp.view.dialog;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import com.onegini.mobile.android.sdk.handlers.request.OneginiMobileAuthenticationRequestHandler;
import com.onegini.mobile.android.sdk.handlers.request.callback.OneginiAcceptDenyCallback;
import com.onegini.mobile.android.sdk.model.entity.OneginiMobileAuthenticationRequest;
import com.onegini.mobile.exampleapp.view.activity.MobileAuthenticationActivity;

public class MobileAuthenticationRequestHandler implements OneginiMobileAuthenticationRequestHandler {

  public static OneginiAcceptDenyCallback CALLBACK;

  private final Context context;

  public MobileAuthenticationRequestHandler(final Context context) {
    this.context = context;
  }

  @Override
  public void startAuthentication(final OneginiMobileAuthenticationRequest oneginiMobileAuthenticationRequest,
                                  final OneginiAcceptDenyCallback oneginiAcceptDenyCallback) {
    CALLBACK = oneginiAcceptDenyCallback;
    openActivity(oneginiMobileAuthenticationRequest.getUserProfile().getProfileId(), oneginiMobileAuthenticationRequest.getMessage());
  }

  @Override
  public void finishAuthentication() {
    closeActivity();
  }

  private void openActivity(final String profileId, final String message) {
    final Intent intent = new Intent(context, MobileAuthenticationActivity.class);
    intent.putExtra(MobileAuthenticationActivity.EXTRA_COMMAND, MobileAuthenticationActivity.COMMAND_START);
    intent.putExtra(MobileAuthenticationActivity.EXTRA_PROFILE_ID, profileId);
    intent.putExtra(MobileAuthenticationActivity.EXTRA_MESSAGE, message);
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }

  private void closeActivity() {
    final Intent intent = new Intent(context, MobileAuthenticationActivity.class);
    intent.putExtra(MobileAuthenticationActivity.EXTRA_COMMAND, MobileAuthenticationActivity.COMMAND_FINISH);
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }
}
