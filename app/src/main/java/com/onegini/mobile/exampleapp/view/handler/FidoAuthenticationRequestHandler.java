package com.onegini.mobile.exampleapp.view.handler;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import com.onegini.mobile.exampleapp.view.activity.FidoActivity;
import com.onegini.mobile.exampleapp.view.activity.MobileAuthenticationFidoActivity;
import com.onegini.mobile.sdk.android.handlers.request.OneginiFidoAuthenticationRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiFidoCallback;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;

public class FidoAuthenticationRequestHandler implements OneginiFidoAuthenticationRequestHandler {

  public static OneginiFidoCallback CALLBACK;

  private final Context context;

  public FidoAuthenticationRequestHandler(final Context context) {
    this.context = context;
  }

  @Override
  public void startAuthentication(final UserProfile userProfile, final OneginiFidoCallback oneginiFidoCallback) {
    CALLBACK = oneginiFidoCallback;
    openActivity(userProfile.getProfileId());
  }

  @Override
  public void finishAuthentication() {
    closeActivity();
  }

  private void openActivity(final String profileId) {
    final Intent intent = new Intent(context, FidoActivity.class);
    intent.putExtra(FidoActivity.EXTRA_COMMAND, FidoActivity.COMMAND_START);
    intent.putExtra(FidoActivity.EXTRA_PROFILE_ID, profileId);
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }

  private void closeActivity() {
    final Intent intent = new Intent(context, FidoActivity.class);
    intent.putExtra(FidoActivity.EXTRA_COMMAND, FidoActivity.COMMAND_FINISH);
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }
}
