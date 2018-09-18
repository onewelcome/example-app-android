package com.onegini.mobile.exampleapp.view.handler;

import com.onegini.mobile.sdk.android.handlers.OneginiMobileAuthWithPushEnrollmentHandler;


public interface ExtendedOneginiMobileAuthWithPushEnrollmentHandler extends OneginiMobileAuthWithPushEnrollmentHandler {

    void onError(final Throwable throwable);
}
