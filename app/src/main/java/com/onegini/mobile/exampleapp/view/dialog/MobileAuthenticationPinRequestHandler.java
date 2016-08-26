package com.onegini.mobile.exampleapp.view.dialog;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import com.onegini.mobile.android.sdk.handlers.request.OneginiMobileAuthenticationPinRequestHandler;
import com.onegini.mobile.android.sdk.handlers.request.callback.OneginiPinCallback;
import com.onegini.mobile.android.sdk.model.entity.OneginiMobileAuthenticationRequest;
import com.onegini.mobile.exampleapp.view.activity.MobileAuthenticationPinActivity;

public class MobileAuthenticationPinRequestHandler implements OneginiMobileAuthenticationPinRequestHandler {

  public static OneginiPinCallback CALLBACK;

  private final Context context;

  private String message;
  private String userProfileId;
  private int failedAttemptsCount;
  private int maxAttemptsCount;

  public MobileAuthenticationPinRequestHandler(final Context context) {
    this.context = context;
  }

  @Override
  public void startAuthentication(final OneginiMobileAuthenticationRequest oneginiMobileAuthenticationRequest, final OneginiPinCallback oneginiPinCallback) {
    CALLBACK = oneginiPinCallback;
    message = oneginiMobileAuthenticationRequest.getMessage();
    userProfileId = oneginiMobileAuthenticationRequest.getUserProfile().getProfileId();
    failedAttemptsCount = maxAttemptsCount = 0;
    startActivity();
  }

  @Override
  public void onNextAuthenticationAttempt(final int failedAttemptsCount, final int maxAttemptsCount) {
    this.failedAttemptsCount = failedAttemptsCount;
    this.maxAttemptsCount = maxAttemptsCount;
    startActivity();
  }

  @Override
  public void finishAuthentication() {
    closeActivity();
  }

  private void startActivity() {
    final Intent intent = new Intent(context, MobileAuthenticationPinActivity.class);
    intent.putExtra(MobileAuthenticationPinActivity.EXTRA_COMMAND, MobileAuthenticationPinActivity.COMMAND_START);
    intent.putExtra(MobileAuthenticationPinActivity.EXTRA_MESSAGE, message);
    intent.putExtra(MobileAuthenticationPinActivity.EXTRA_PROFILE_ID, userProfileId);
    intent.putExtra(MobileAuthenticationPinActivity.EXTRA_FAILED_ATTEMPTS_COUNT, failedAttemptsCount);
    intent.putExtra(MobileAuthenticationPinActivity.EXTRA_MAX_FAILED_ATTEMPTS, maxAttemptsCount);
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);

    context.startActivity(intent);
  }

  private void closeActivity() {
    final Intent intent = new Intent(context, MobileAuthenticationPinActivity.class);
    intent.putExtra(MobileAuthenticationPinActivity.EXTRA_COMMAND, MobileAuthenticationPinActivity.COMMAND_FINISH);
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }
}
