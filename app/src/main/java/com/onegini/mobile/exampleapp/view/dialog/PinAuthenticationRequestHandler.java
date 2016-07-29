package com.onegini.mobile.exampleapp.view.dialog;

import android.content.Context;
import android.content.Intent;
import com.onegini.mobile.android.sdk.handlers.request.OneginiPinAuthenticationRequestHandler;
import com.onegini.mobile.android.sdk.handlers.request.callback.OneginiPinCallback;
import com.onegini.mobile.android.sdk.model.entity.UserProfile;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.model.User;
import com.onegini.mobile.exampleapp.storage.UserStorage;
import com.onegini.mobile.exampleapp.view.activity.PinActivity;

public class PinAuthenticationRequestHandler implements OneginiPinAuthenticationRequestHandler {

  public static OneginiPinCallback oneginiPinCallback;
  private static UserProfile userProfile;
  private final Context context;
  private final UserStorage userStorage;

  public PinAuthenticationRequestHandler(final Context context) {
    this.context = context;
    userStorage = new UserStorage(context);
  }

  @Override
  public void startAuthentication(final UserProfile userProfile, final OneginiPinCallback oneginiPinCallback) {
    PinAuthenticationRequestHandler.userProfile = userProfile;
    PinAuthenticationRequestHandler.oneginiPinCallback = oneginiPinCallback;

    PinActivity.setIsCreatePinFlow(false);
    startPinActivity(userProfile);
  }

  @Override
  public void onNextAuthenticationAttempt(int failedAttemptsCount, int maxAttemptsCount) {
    PinActivity.setRemainingFailedAttempts(maxAttemptsCount - failedAttemptsCount);
    startPinActivity(userProfile);
  }

  @Override
  public void finishAuthentication() {
    PinActivity.setRemainingFailedAttempts(0);
  }

  private void startPinActivity(final UserProfile userProfile) {
    final Intent intent = new Intent(context, PinActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.putExtra(PinActivity.EXTRA_TITLE, context.getString(R.string.pin_title_enter_pin));

    final User user = userStorage.loadUser(userProfile);
    intent.putExtra(PinActivity.EXTRA_USER_NAME, user.getName());

    context.startActivity(intent);
  }
}
