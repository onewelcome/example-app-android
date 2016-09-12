package com.onegini.mobile.exampleapp.view.adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.model.AuthenticatorListItem;
import com.onegini.mobile.sdk.android.model.OneginiAuthenticator;

public class AuthenticatorsAdapter extends ArrayAdapter<AuthenticatorListItem> {

  private final Context context;
  private final AuthenticatorListItem[] authenticators;

  public AuthenticatorsAdapter(final Context context, final AuthenticatorListItem[] authenticators) {
    super(context, -1, authenticators);
    this.context = context;
    this.authenticators = authenticators;
  }

  @Override
  public View getView(final int position, final View convertView, final ViewGroup parent) {
    final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    final View rowView = inflater.inflate(R.layout.authenticator_switcher, parent, false);
    final TextView nameTextView = (TextView) rowView.findViewById(R.id.authenticator_name);
    final Switch authenticatorSwitch = (Switch) rowView.findViewById(R.id.authenticator_switch);
    final ProgressBar progressIndicator = (ProgressBar) rowView.findViewById(R.id.progress_bar_authenticators);

    final AuthenticatorListItem authenticatorListItem = authenticators[position];
    final OneginiAuthenticator authenticator = authenticatorListItem.getAuthenticator();

    if (position % 2 == 0) {
      rowView.setBackgroundResource(R.color.switcher_dark);
    }

    nameTextView.setText(authenticator.getName());

    if (authenticator.getType() == OneginiAuthenticator.PIN) {
      authenticatorSwitch.setChecked(true);
      authenticatorSwitch.setEnabled(false);
      return rowView;
    }

    authenticatorSwitch.setChecked(authenticatorListItem.isRegistered());

    if (authenticatorListItem.isProcessed()) {
      progressIndicator.setVisibility(VISIBLE);
      authenticatorSwitch.setEnabled(false);
    } else {
      progressIndicator.setVisibility(GONE);
      authenticatorSwitch.setEnabled(true);
    }

    return rowView;
  }
}


