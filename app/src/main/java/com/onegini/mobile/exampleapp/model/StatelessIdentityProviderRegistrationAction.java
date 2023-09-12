package com.onegini.mobile.exampleapp.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.onegini.mobile.sdk.android.handlers.action.OneginiCustomTwoStepRegistrationAction;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiCustomRegistrationCallback;
import com.onegini.mobile.sdk.android.model.entity.CustomInfo;

public class StatelessIdentityProviderRegistrationAction implements OneginiCustomTwoStepRegistrationAction {

  @Override
  public void initRegistration(@NonNull OneginiCustomRegistrationCallback oneginiCustomRegistrationCallback,
                               @Nullable CustomInfo customInfo) {
    oneginiCustomRegistrationCallback.returnSuccess("success");
  }

  @Override
  public void finishRegistration(@NonNull OneginiCustomRegistrationCallback oneginiCustomRegistrationCallback,
                                 @Nullable CustomInfo customInfo) {
    oneginiCustomRegistrationCallback.returnSuccess("success");
  }
}
