package com.onegini.mobile.exampleapp.view.helper;

import android.content.Context;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.storage.UserStorage;
import com.onegini.mobile.exampleapp.util.DeregistrationUtil;
import com.onegini.mobile.exampleapp.view.handler.InitializationHandler;
import com.onegini.mobile.sdk.android.client.OneginiClient;
import com.onegini.mobile.sdk.android.handlers.OneginiInitializationHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiInitializationError;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;

import java.util.Set;

public class OneginiClientInitializer {

  private static boolean isInitialized;

  private final DeregistrationUtil deregistrationUtil;
  private final UserStorage userStorage;
  private final Context context;

  public OneginiClientInitializer(final Context context) {
    this.context = context;
    deregistrationUtil = new DeregistrationUtil(context);
    userStorage = new UserStorage(context);
  }

  public void startOneginiClient(final InitializationHandler initializationHandler) {
    if (!isInitialized) {
      start(initializationHandler);
    } else {
      initializationHandler.onSuccess();
    }
  }

  private void start(final InitializationHandler initializationHandler) {
    final OneginiClient oneginiClient = OneginiSDK.getOneginiClient(context);
    oneginiClient.start(new OneginiInitializationHandler() {
      @Override
      public void onSuccess(final Set<UserProfile> removedUserProfiles) {
        setInitialized();
        if (!removedUserProfiles.isEmpty()) {
          removeUserProfiles(removedUserProfiles);
        }
        initializationHandler.onSuccess();
      }

      @Override
      public void onError(final OneginiInitializationError error) {
        @OneginiInitializationError.InitializationErrorType final int errorType = error.getErrorType();
        if (errorType == OneginiInitializationError.DEVICE_DEREGISTERED) {
          deregistrationUtil.onDeviceDeregistered();
        }
        initializationHandler.onError(error.getMessage());
      }

      private void removeUserProfiles(final Set<UserProfile> removedUserProfiles) {
        for (final UserProfile userProfile : removedUserProfiles) {
          userStorage.removeUser(userProfile);
        }
      }

      private synchronized void setInitialized() {
        isInitialized = true;
      }
    });
  }
}
