package com.onegini.mobile.exampleapp;

import android.os.Build;
import com.onegini.mobile.sdk.android.model.OneginiClientConfigModel;

public class OneginiConfigModel implements OneginiClientConfigModel {

  /* Config model generated by SDK Configurator version: v5.2.0 */

  private final String appIdentifier = "ExampleApp";
  private final String appPlatform = "android";
  private final String redirectionUri = "oneginiexample://loginsuccess";
  private final String appVersion = "7.7.0";
  private final String baseURL = "https://mobile-security-proxy.onegini.com";
  private final String resourceBaseURL = "https://mobile-security-proxy.onegini.com";
  private final String keystoreHash = "ce47fa0890678dcc444bb06772d525e466a8eef91b4b4f53a874e4a637135238";
  private final int maxPinFailures = 3;
  private final String serverPublicKey = "60746FCBFDCAD5B89581405148B1FB1F25EDF2F4880C6EF7B55AFA2CD7CB0F5F";

  public String getAppIdentifier() {
    return appIdentifier;
  }

  public String getAppPlatform() {
    return appPlatform;
  }

  public String getRedirectUri() {
    return redirectionUri;
  }

  public String getAppVersion() {
    return appVersion;
  }

  public String getBaseUrl() {
    return baseURL;
  }

  public String getResourceBaseUrl() {
    return resourceBaseURL;
  }

  public int getCertificatePinningKeyStore() {
    return R.raw.keystore;
  }

  public String getKeyStoreHash() {
    return keystoreHash;
  }

  public String getDeviceName() {
    return Build.BRAND + " " + Build.MODEL;
  }

  public String getServerPublicKey() {
    return serverPublicKey;
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
            ", serverPublicKey='" + serverPublicKey + "'" +
            "}";
  }
}
