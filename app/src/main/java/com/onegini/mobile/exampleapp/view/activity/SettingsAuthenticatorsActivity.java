package com.onegini.mobile.exampleapp.view.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.model.AuthenticatorListItem;
import com.onegini.mobile.exampleapp.view.adapter.AuthenticatorsAdapter;
import com.onegini.mobile.exampleapp.view.helper.OneginiAuthenticatorComperator;
import com.onegini.mobile.sdk.android.client.UserClient;
import com.onegini.mobile.sdk.android.handlers.OneginiAuthenticatorDeregistrationHandler;
import com.onegini.mobile.sdk.android.handlers.OneginiAuthenticatorRegistrationHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiAuthenticatorDeregistrationError;
import com.onegini.mobile.sdk.android.handlers.error.OneginiAuthenticatorRegistrationError;
import com.onegini.mobile.sdk.android.model.OneginiAuthenticator;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;

public class SettingsAuthenticatorsActivity extends AppCompatActivity {

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.toolbar)
  Toolbar toolbar;
  @SuppressWarnings({"unused", "WeakerAccess"})
  @Bind(R.id.settings_authenticator_selector_text)
  TextView loginMethodTextView;
  @SuppressWarnings({"unused", "WeakerAccess"})
  @Bind(R.id.authenticators_list)
  ListView authenticatorsListView;

  private AuthenticatorListItem[] authenticators;
  private AuthenticatorsAdapter listViewAdapter;
  private UserClient userClient;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings_authenticators);
    ButterKnife.bind(this);
    userClient = OneginiSDK.getOneginiClient(this).getUserClient();
  }

  @Override
  protected void onResume() {
    super.onResume();
    setupView();
  }

  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {
    switch (item.getItemId()) {
      // Respond to the action bar's Up/Home button
      case android.R.id.home:
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.settings_authenticator_selector)
  public void onChangePreferredAuthenticatorClick() {
    final UserProfile userProfile = userClient.getAuthenticatedUserProfile();
    final Set<OneginiAuthenticator> registeredAuthenticators = userClient.getRegisteredAuthenticators(userProfile);

    final List<String> authenticatorNames = new ArrayList<>(registeredAuthenticators.size());
    for (final OneginiAuthenticator authenticator : registeredAuthenticators) {
      authenticatorNames.add(authenticator.getName());
    }
    final String[] authenticatorsToSelect = new String[authenticatorNames.size()];

    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(R.string.pick_authenticator);
    builder.setItems(authenticatorNames.toArray(authenticatorsToSelect), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(final DialogInterface dialog, final int which) {
        updatePreferredAuthenticator(which, registeredAuthenticators);
      }
    });
    builder.show();
  }

  private void updatePreferredAuthenticator(final int which, final Set<OneginiAuthenticator> registeredAuthenticators) {
    int index = 0;
    for (final OneginiAuthenticator authenticator : registeredAuthenticators) {
      if (which == index) {
        userClient.setPreferredAuthenticator(authenticator);
        setPreferredAuthenticatorText(authenticator);
        break;
      }
      index++;
    }
  }

  private void setupView() {
    setupActionBar();
    prepareAuthenticatorsList();
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

  private void prepareAuthenticatorsList() {
    final UserProfile userProfile = userClient.getAuthenticatedUserProfile();
    final Set<OneginiAuthenticator> allAuthenticators = userClient.getAllAuthenticators(userProfile);
    final OneginiAuthenticator[] oneginiAuthenticators = sortLists(allAuthenticators);
    authenticators = wrapAuthenticatorsToListItems(oneginiAuthenticators);
    listViewAdapter = new AuthenticatorsAdapter(this, authenticators);
    authenticatorsListView.setAdapter(listViewAdapter);
    authenticatorsListView.setOnItemClickListener(new AuthenticatorClickListener());
  }

  private OneginiAuthenticator[] sortLists(final Set<OneginiAuthenticator> allAuthenticators) {
    final List<OneginiAuthenticator> authenticatorList = new ArrayList<>(allAuthenticators);
    Collections.sort(authenticatorList, new OneginiAuthenticatorComperator());
    return authenticatorList.toArray(new OneginiAuthenticator[authenticatorList.size()]);
  }

  private AuthenticatorListItem[] wrapAuthenticatorsToListItems(final OneginiAuthenticator[] oneginiAuthenticators) {
    final AuthenticatorListItem[] authenticators = new AuthenticatorListItem[oneginiAuthenticators.length];
    for (int i = 0; i < oneginiAuthenticators.length; i++) {
      final OneginiAuthenticator currentAuthenticator = oneginiAuthenticators[i];
      if (currentAuthenticator.isPreferred()) {
        setPreferredAuthenticatorText(currentAuthenticator);
      }
      authenticators[i] = new AuthenticatorListItem(currentAuthenticator);
    }
    return authenticators;
  }

  private void setPreferredAuthenticatorText(final OneginiAuthenticator authenticator) {
    loginMethodTextView.setText(authenticator.getName());
  }

  private void registerAuthenticator(final OneginiAuthenticator authenticator, final int position) {
    userClient.registerAuthenticator(authenticator, new OneginiAuthenticatorRegistrationHandler() {
      @Override
      public void onSuccess() {
        authenticators[position].setIsProcessed(false);
        prepareAuthenticatorsList();
        Toast.makeText(SettingsAuthenticatorsActivity.this, "Authenticator registered", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onError(final OneginiAuthenticatorRegistrationError error) {
        authenticators[position].setIsProcessed(false);
        Toast.makeText(SettingsAuthenticatorsActivity.this, error.getErrorDescription(), Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void deregisterAuthenticator(final OneginiAuthenticator authenticator, final int position) {
    userClient.deregisterAuthenticator(authenticator, new OneginiAuthenticatorDeregistrationHandler() {
      @Override
      public void onSuccess() {
        authenticators[position].setIsProcessed(false);
        prepareAuthenticatorsList();
        Toast.makeText(SettingsAuthenticatorsActivity.this, "Authenticator unregistered", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onError(final OneginiAuthenticatorDeregistrationError error) {
        authenticators[position].setIsProcessed(false);
        Toast.makeText(SettingsAuthenticatorsActivity.this, error.getErrorDescription(), Toast.LENGTH_SHORT).show();
      }
    });
  }

  private class AuthenticatorClickListener implements AdapterView.OnItemClickListener {

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
      final AuthenticatorListItem clickedAuthenticatorItem = authenticators[position];
      final OneginiAuthenticator clickedAuthenticator = clickedAuthenticatorItem.getAuthenticator();

      if (clickedAuthenticatorItem.isProcessed() || clickedAuthenticator.getType() == OneginiAuthenticator.PIN) {
        return;
      }
      clickedAuthenticatorItem.setIsProcessed(true);
      listViewAdapter.notifyDataSetChanged();

      if (clickedAuthenticator.isRegistered()) {
        deregisterAuthenticator(clickedAuthenticatorItem.getAuthenticator(), position);
      } else {
        registerAuthenticator(clickedAuthenticatorItem.getAuthenticator(), position);
      }
    }
  }
}
