package com.onegini.mobile.exampleapp.view.dialog;

import android.content.Context;
import android.content.Intent;
import com.onegini.mobile.android.sdk.dialogs.OneginiCurrentPinDialog;
import com.onegini.mobile.android.sdk.handlers.OneginiPinProvidedHandler;
import com.onegini.mobile.android.sdk.model.entity.UserProfile;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.model.User;
import com.onegini.mobile.exampleapp.storage.UserStorage;
import com.onegini.mobile.exampleapp.view.activity.PinActivity;

public class CurrentPinDialog implements OneginiCurrentPinDialog {

  public static OneginiPinProvidedHandler oneginiPinProvidedHandler;

  private final Context context;
  private final UserStorage userStorage;

  public CurrentPinDialog(final Context context) {
    this.context = context;
    userStorage = new UserStorage(context);
  }

  @Override
  public void getCurrentPin(final UserProfile userProfile, final OneginiPinProvidedHandler pinProvidedHandler) {
    PinActivity.setIsCreatePinFlow(false);
    oneginiPinProvidedHandler = pinProvidedHandler;
    startPinActivity(userProfile);
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
