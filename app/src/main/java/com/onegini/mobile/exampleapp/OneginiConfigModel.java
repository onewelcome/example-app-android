package com.onegini.mobile.exampleapp;

import android.os.Build;
import com.onegini.mobile.sdk.android.model.OneginiClientConfigModel;

public class OneginiConfigModel implements OneginiClientConfigModel {

  /* Config model generated by SDK Configurator version: v5.3.0 */

  private final String appIdentifier = "STA";
  private final String appPlatform = "android";
  private final String redirectionUri = "oneginiexample://loginsuccess";
  private final String appVersion = "1.0.0";
  private final String baseURL = "https://mobile-security-proxy.test.onegini.com";
  private final String resourceBaseURL = "https://mobile-security-proxy.test.onegini.com/resources/";
  private final String keystoreHash = "0e318af2e41ea3681ccebb72fa94c6e6e2180d1aab6f66e26062b4534291f9fc";
  private final int maxPinFailures = 3;
  private final String serverPublicKey = "C5FC81EDF132BA5305F24A85F459FA85948EC1E8D696CA23FE90A008C7124AA5";
  private final String serverType = "access";
  private final String serverVersion = "96d7391";

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

    public String getServerType() {
    return serverType;
  }

    public String getServerVersion() {
    return serverVersion;
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
            ", serverType='" + serverType + "'" +
            ", serverVersion='" + serverVersion + "'" +
            "}";
  }
}
