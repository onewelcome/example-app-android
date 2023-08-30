package com.onegini.mobile.exampleapp.view.action.statelessidentityprovider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.onegini.mobile.sdk.android.handlers.action.OneginiCustomTwoStepRegistrationAction;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiCustomRegistrationCallback;
import com.onegini.mobile.sdk.android.model.entity.CustomInfo;

public class StatelessRegistrationAction implements OneginiCustomTwoStepRegistrationAction {
  @Override
  public void initRegistration(@NonNull OneginiCustomRegistrationCallback oneginiCustomRegistrationCallback,
                               @Nullable CustomInfo customInfo) {
    oneginiCustomRegistrationCallback.returnSuccess(null);
  }

  @Override
  public void finishRegistration(@NonNull OneginiCustomRegistrationCallback oneginiCustomRegistrationCallback,
                                 @Nullable CustomInfo customInfo) {
    oneginiCustomRegistrationCallback.returnSuccess(null);
  }

  @Override
  public boolean isStatelessRegistration() {
    return true;
  }
}
