package com.onegini.mobile.exampleapp;

import android.os.Build;
import com.onegini.mobile.android.sdk.model.OneginiClientConfigModel;

public class OneginiConfigModel implements OneginiClientConfigModel {

  private final String appIdentifier = "ExampleApp-SDKAND36";
  private final String appPlatform = "android";
  private final String redirectionUri = "appscheme://loginsuccess";
  private final String appVersion = "1.0.0";
  private final String baseURL = "https://onegini-msp-snapshot.test.onegini.io";
  private final String resourceBaseURL = "https://onegini-msp-snapshot.test.onegini.io";
  private final String keystoreHash = "8a70d8c81261da12ecebbb7f227dfc937a374a8a0cfb30c662d333837ef667e0";
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
  public String getRedirectUri() {
    return redirectionUri;
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
        ", redirectionUri='" + redirectionUri + "'" +
        ", appVersion='" + appVersion + "'" +
        ", baseURL='" + baseURL + "'" +
        ", maxPinFailures='" + maxPinFailures + "'" +
        ", resourceBaseURL='" + resourceBaseURL + "'" +
        ", keyStoreHash='" + getKeyStoreHash() + "'" +
        ", idTokenRequested='" + shouldGetIdToken() + "'" +
        "}";
  }
}
