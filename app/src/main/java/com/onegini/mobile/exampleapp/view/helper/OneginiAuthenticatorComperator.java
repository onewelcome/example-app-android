package com.onegini.mobile.exampleapp.view.helper;

import java.util.Comparator;

import com.onegini.mobile.sdk.android.model.OneginiAuthenticator;

/**
 * Order provided authenticators in following order:
 * 1. PIN authenticator
 * 2. Android fingerprint authenticator
 * 3. FIDO authenticators ordered by name
 */
public class OneginiAuthenticatorComperator implements Comparator<OneginiAuthenticator> {

  @Override
  public int compare(final OneginiAuthenticator o1, final OneginiAuthenticator o2) {
    final int resultByType = compareByType(o1.getType(), o2.getType());
    if (resultByType == 0) {
      return compareByName(o1, o2);
    } else {
      return resultByType;
    }
  }

  private int compareByType(final int o1, final int o2) {
    if (o1 == o2) {
      return 0;
    }

    // o1 == PIN && o2 == FINGERPRINT || FIDO
    if (o1 == OneginiAuthenticator.PIN) {
      return -1;
    }

    // o2 == PIN && o1 == FINGERPRINT || FIDO
    if (o2 == OneginiAuthenticator.PIN) {
      return 1;
    }

    // o1 == FINGERPRINT && o2 == FIDO
    if (o1 == OneginiAuthenticator.FINGERPRINT) {
      return -1;
    }

    // o2 == FINGERPRINT && o1 == FIDO
    return 1;
  }

  private int compareByName(final OneginiAuthenticator o1, final OneginiAuthenticator o2) {
    return o1.getName().compareTo(o2.getName());
  }
}
