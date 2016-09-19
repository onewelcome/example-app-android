package com.onegini.mobile.exampleapp.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.util.AnimationUtils;
import com.onegini.mobile.exampleapp.view.handler.FingerprintAuthenticationRequestHandler;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FingerprintActivity extends Activity {

  public static final String MSG_EXTRA_ACTION = "fingerprint-action";
  public static final String MSG_EXTRA_START = "start";
  public static final String MSG_EXTRA_SHOW_SCANNING = "show";
  public static final String MSG_EXTRA_RECEIVED_FINGERPRINT = "received";
  public static final String MSG_EXTRA_CLOSE = "close";

  @Bind(R.id.action_text)
  TextView actionText;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fingerprint);
    ButterKnife.bind(this);

    setupUi(getIntent().getStringExtra(MSG_EXTRA_ACTION));
  }

  @Override
  protected void onPostCreate(final Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    if (FingerprintAuthenticationRequestHandler.fingerprintCallback != null) {
      FingerprintAuthenticationRequestHandler.fingerprintCallback.acceptAuthenticationRequest();
    }
  }

  @Override
  protected void onNewIntent(final Intent intent) {
    setupUi(intent.getStringExtra(MSG_EXTRA_ACTION));
  }

  private void setupUi(final String action) {
    if (MSG_EXTRA_SHOW_SCANNING.equals(action)) {
      actionText.setText(R.string.verifying);
    } else if (MSG_EXTRA_START.equals(action)) {
      actionText.setText(R.string.scan_fingerprint);
    } else if (MSG_EXTRA_RECEIVED_FINGERPRINT.equals(action)) {
      actionText.setText(R.string.try_again);
      actionText.setAnimation(AnimationUtils.getBlinkAnimation());
    } else if (MSG_EXTRA_CLOSE.equals(action)) {
      finish();
    }
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.fallback_to_pin_button)
  public void onFallbackToPinButtonClick() {
    if (FingerprintAuthenticationRequestHandler.fingerprintCallback != null) {
      FingerprintAuthenticationRequestHandler.fingerprintCallback.fallbackToPin();
      finish();
    }
  }
}
