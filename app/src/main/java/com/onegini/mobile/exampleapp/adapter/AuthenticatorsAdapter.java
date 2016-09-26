package com.onegini.mobile.exampleapp.adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.model.AuthenticatorListItem;
import com.onegini.mobile.exampleapp.view.activity.SettingsAuthenticatorsActivity;
import com.onegini.mobile.sdk.android.model.OneginiAuthenticator;

public class AuthenticatorsAdapter extends
    RecyclerView.Adapter<AuthenticatorsAdapter.ViewHolder> {

  private final AuthenticatorListItem[] authenticators;
  private final SettingsAuthenticatorsActivity.AuthenticatorClickListener authenticatorClickListener;

  public AuthenticatorsAdapter(final AuthenticatorListItem[] authenticators,
                               final SettingsAuthenticatorsActivity.AuthenticatorClickListener authenticatorClickListener) {
    this.authenticators = authenticators;
    this.authenticatorClickListener = authenticatorClickListener;
  }

  @Override
  public AuthenticatorsAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View authenticatorItemView = inflater.inflate(R.layout.row_item_authenticator, parent, false);

    return new ViewHolder(authenticatorItemView);
  }

  @Override
  public void onBindViewHolder(final AuthenticatorsAdapter.ViewHolder holder, final int position) {
    final AuthenticatorListItem authenticatorListItem = authenticators[position];
    final OneginiAuthenticator authenticator = authenticatorListItem.getAuthenticator();

    holder.itemView.setClickable(true);
    holder.itemView.setOnClickListener(v -> authenticatorClickListener.onAuthenticatorItemClick(position));

    if (position % 2 == 0) {
      holder.itemView.setBackgroundResource(R.color.switcher_dark);
    }

    holder.nameTextView.setText(authenticator.getName());

    if (authenticator.getType() == OneginiAuthenticator.PIN) {
      holder.authenticatorSwitch.setChecked(true);
      holder.authenticatorSwitch.setEnabled(false);
      return;
    }

    holder.authenticatorSwitch.setChecked(authenticator.isRegistered());

    if (authenticatorListItem.isProcessed()) {
      holder.progressIndicator.setVisibility(VISIBLE);
      holder.authenticatorSwitch.setEnabled(false);
    } else {
      holder.progressIndicator.setVisibility(GONE);
      holder.authenticatorSwitch.setEnabled(true);
    }
  }

  @Override
  public int getItemCount() {
    return authenticators.length;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    public TextView nameTextView;
    public Switch authenticatorSwitch;
    public ProgressBar progressIndicator;

    public ViewHolder(final View contactView) {
      super(contactView);
      nameTextView = (TextView) itemView.findViewById(R.id.authenticator_name);
      authenticatorSwitch = (Switch) itemView.findViewById(R.id.authenticator_switch);
      progressIndicator = (ProgressBar) itemView.findViewById(R.id.progress_bar_authenticators);
    }
  }
}


