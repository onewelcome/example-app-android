package com.onegini.mobile.exampleapp.view.handler;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import com.onegini.mobile.exampleapp.view.activity.MobileAuthenticationFidoActivity;
import com.onegini.mobile.sdk.android.handlers.request.OneginiMobileAuthenticationFidoRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiFidoCallback;
import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthenticationRequest;

public class MobileAuthenticationFidoRequestHandler implements OneginiMobileAuthenticationFidoRequestHandler {

  public static OneginiFidoCallback CALLBACK;

  private final Context context;

  public MobileAuthenticationFidoRequestHandler(final Context context) {
    this.context = context;
  }

  @Override
  public void startAuthentication(final OneginiMobileAuthenticationRequest oneginiMobileAuthenticationRequest, final OneginiFidoCallback oneginiFidoCallback) {
    CALLBACK = oneginiFidoCallback;
    openActivity(oneginiMobileAuthenticationRequest.getUserProfile().getProfileId(), oneginiMobileAuthenticationRequest.getMessage());
  }

  @Override
  public void finishAuthentication() {
    closeActivity();
  }

  private void openActivity(final String profileId, final String message) {
    final Intent intent = new Intent(context, MobileAuthenticationFidoActivity.class);
    intent.putExtra(MobileAuthenticationFidoActivity.EXTRA_COMMAND, MobileAuthenticationFidoActivity.COMMAND_START);
    intent.putExtra(MobileAuthenticationFidoActivity.EXTRA_PROFILE_ID, profileId);
    intent.putExtra(MobileAuthenticationFidoActivity.EXTRA_MESSAGE, message);
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }

  private void closeActivity() {
    final Intent intent = new Intent(context, MobileAuthenticationFidoActivity.class);
    intent.putExtra(MobileAuthenticationFidoActivity.EXTRA_COMMAND, MobileAuthenticationFidoActivity.COMMAND_FINISH);
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }
}
