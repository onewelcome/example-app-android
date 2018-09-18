package com.onegini.mobile.exampleapp.view.helper;

import java.util.Collections;
import java.util.Set;

import android.content.Context;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.storage.UserStorage;
import com.onegini.mobile.exampleapp.util.DeregistrationUtil;
import com.onegini.mobile.sdk.android.client.OneginiClient;
import com.onegini.mobile.sdk.android.handlers.OneginiInitializationHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiInitializationError;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;

public class OneginiClientInitializer {

  private static boolean isInitialized;

  private Context context;

  public OneginiClientInitializer(final Context context) {
    this.context = context;
  }

  public void startOneginiClient(final OneginiInitializationHandler oneginiInitializationHandler) {
    if (!isInitialized) {
      start(oneginiInitializationHandler);
    } else {
      oneginiInitializationHandler.onSuccess(Collections.emptySet());
    }
  }

  private void start(final OneginiInitializationHandler oneginiInitializationHandler) {
    final OneginiClient oneginiClient = OneginiSDK.getOneginiClient(context);
    oneginiClient.start(new OneginiInitializationHandler() {
      @Override
      public void onSuccess(final Set<UserProfile> removedUserProfiles) {
        isInitialized = true;
        if (!removedUserProfiles.isEmpty()) {
          removeUserProfiles(removedUserProfiles);
        }
        oneginiInitializationHandler.onSuccess(removedUserProfiles);
      }

      @Override
      public void onError(final OneginiInitializationError error) {
        @OneginiInitializationError.InitializationErrorType final int errorType = error.getErrorType();
        if (errorType == OneginiInitializationError.DEVICE_DEREGISTERED) {
          new DeregistrationUtil(context).onDeviceDeregistered();
        }
        oneginiInitializationHandler.onError(error);
      }

      private void removeUserProfiles(final Set<UserProfile> removedUserProfiles) {
        final UserStorage userStorage = new UserStorage(context);
        for (final UserProfile userProfile : removedUserProfiles) {
          userStorage.removeUser(userProfile);
        }
      }
    });
  }
}
