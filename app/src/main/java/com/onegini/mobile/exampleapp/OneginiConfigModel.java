package com.onegini.mobile.exampleapp;

import android.os.Build;
import com.onegini.mobile.android.sdk.model.OneginiClientConfigModel;

public class OneginiConfigModel implements OneginiClientConfigModel {

  private final String appIdentifier = "ExampleApp";
  private final String appPlatform = "android";
  private final String appScheme = "oneginiexample";
  private final String appVersion = "1.0.0";
  private final String baseURL = "https://onegini-msp-snapshot.test.onegini.io";
  private final String resourceBaseURL = "https://onegini-msp-snapshot.test.onegini.io";
  private final String keystoreHash = "ebdb795f8f39d902b3696d8bcc19a7b17e3831e72f6b883ad2f41e865244c8f8";
  private final int maxPinFailures = 3;

  @Override
  public String getAppIdentifier() {
    return appIdentifier;
  }

  @Override
  public String getAppPlatform() {
    return appPlatform;
  }

  @Override
  public String getAppScheme() {
    return appScheme;
  }

  @Override
  public String getAppVersion() {
    return appVersion;
  }

  @Override
  public String getBaseUrl() {
    return baseURL;
  }

  @Override
  public String getResourceBaseUrl() {
    return resourceBaseURL;
  }

  @Override
  public int getCertificatePinningKeyStore() {
    return R.raw.keystore;
  }

  @Override
  public String getKeyStoreHash() {
    return keystoreHash;
  }

  @Override
  public String getDeviceName() {
    return Build.BRAND + " " + Build.MODEL;
  }

  @Override
  public boolean shouldGetIdToken() {
    return false;
  }

  @Override
  public boolean shouldStoreCookies() {
    return false;
  }

  @Override
  public int getMaxPinFailures() {
    return maxPinFailures;
  }

  @Override
  public int getHttpClientTimeout() {
    return 60000;
  }

  @Override
  public String toString() {
    return "ConfigModel{" +
        "  appIdentifier='" + appIdentifier + "'" +
        ", appPlatform='" + appPlatform + "'" +
        ", appScheme='" + appScheme + "'" +
        ", appVersion='" + appVersion + "'" +
        ", baseURL='" + baseURL + "'" +
        ", maxPinFailures='" + maxPinFailures + "'" +
        ", resourceBaseURL='" + resourceBaseURL + "'" +
        ", keyStoreHash='" + getKeyStoreHash() + "'" +
        ", idTokenRequested='" + shouldGetIdToken() + "'" +
        "}";
  }
}