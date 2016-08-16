package com.onegini.mobile.exampleapp.network.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Base class to handle Instance ID service notifications on token refresh
 */
public class GcmInstanceIdListenerService extends InstanceIDListenerService {

  @Override
  public void onTokenRefresh() {
    super.onTokenRefresh();
    startService(new Intent(this, GcmIntentService.class));
  }

}
