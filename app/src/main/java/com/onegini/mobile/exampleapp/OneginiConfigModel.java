package com.onegini.mobile.exampleapp;

import android.os.Build;
import com.onegini.mobile.sdk.android.library.model.OneginiClientConfigModel;

@SuppressWarnings({ "unused", "WeakerAccess" })
public class OneginiConfigModel implements OneginiClientConfigModel {

  private final String appIdentifier = "ExampleApp";
  private final String appPlatform = "android";
  private final String appScheme = "oneginiexample";
  private final String appVersion = "1.0.0";
  private final String baseURL = "https://tokenserver-demo-test.onegini.com";
  private final String resourceBaseURL = "https://tokenserver-demo-test.onegini.com";
  private final String keystoreHash = "fb3b032b76817fa9056806e397624eca98b0004ee9b396458f272741cd61d946";
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
