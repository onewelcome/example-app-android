package com.onegini.mobile.exampleapp.view.activity;

import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;

import java.nio.charset.StandardCharsets;

public class IdTokenActivity extends AppCompatActivity {

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @BindView(R.id.text_id_token)
  TextView idTokenTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_id_token);
    ButterKnife.bind(this);
    setupActionBar();
    showIdToken();
  }

  private void setupActionBar() {
    setSupportActionBar(toolbar);

    final ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setLogo(R.mipmap.ic_launcher);
      actionBar.setDisplayUseLogoEnabled(true);
      actionBar.setDisplayShowTitleEnabled(false);
    }
  }

  private void showIdToken() {
    final String idToken = OneginiSDK.getOneginiClient(this).getUserClient().getIdToken();
    final String content;
    if (idToken != null) {
      content = getFormattedUserInfo(idToken);
    } else {
      content = getString(R.string.id_token_null_description);
    }
    idTokenTextView.setText(content);
  }

  private String getFormattedUserInfo(final String idToken) {
    final String jwtPayload = idToken.split("\\.")[1];
    final String decodedJson = new String(Base64.decode(jwtPayload, Base64.DEFAULT), StandardCharsets.UTF_8);
    return new GsonBuilder()
        .setPrettyPrinting()
        .create()
        .toJson(new JsonParser().parse(decodedJson));
  }
}
