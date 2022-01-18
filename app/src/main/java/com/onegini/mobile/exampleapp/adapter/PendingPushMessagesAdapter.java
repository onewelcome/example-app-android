/*
 * Copyright (c) 2016-2018 Onegini B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onegini.mobile.exampleapp.adapter;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.model.User;
import com.onegini.mobile.exampleapp.network.fcm.MobileAuthenticationService;
import com.onegini.mobile.exampleapp.storage.UserStorage;
import com.onegini.mobile.exampleapp.util.DeregistrationUtil;
import com.onegini.mobile.exampleapp.view.activity.LoginActivity;
import com.onegini.mobile.sdk.android.handlers.OneginiDenyMobileAuthWithPushRequestHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiAuthenticatorDeregistrationError;
import com.onegini.mobile.sdk.android.handlers.error.OneginiDenyMobileAuthWithPushRequestError;
import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthWithPushRequest;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class PendingPushMessagesAdapter extends RecyclerView.Adapter<PendingPushMessagesAdapter.ViewHolder> {

  private final Context context;
  private final List<OneginiMobileAuthWithPushRequest> list;
  private final UserStorage userStorage;
  private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);

  public PendingPushMessagesAdapter(final Context context) {
    this.context = context;
    list = new ArrayList<>();
    userStorage = new UserStorage(context);
  }

  public void update(final Set<OneginiMobileAuthWithPushRequest> set) {
    list.clear();
    list.addAll(set);
    notifyDataSetChanged();
  }

  @Override
  public PendingPushMessagesAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
    final Context context = parent.getContext();
    final LayoutInflater inflater = LayoutInflater.from(context);

    final View deviceItemView = inflater.inflate(R.layout.row_item_pending_message, parent, false);

    return new PendingPushMessagesAdapter.ViewHolder(deviceItemView);
  }

  @Override
  public void onBindViewHolder(final PendingPushMessagesAdapter.ViewHolder viewHolder, final int position) {
    final OneginiMobileAuthWithPushRequest oneginiMobileAuthWithPushRequest = list.get(position);
    final User user = userStorage.loadUser(new UserProfile(oneginiMobileAuthWithPushRequest.getUserProfileId()));
    viewHolder.profileTextView.setText(user.getName());
    viewHolder.messageTextView.setText(oneginiMobileAuthWithPushRequest.getMessage());

    final Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(oneginiMobileAuthWithPushRequest.getTimestamp());
    viewHolder.dateTextView.setText(sdf.format(calendar.getTime()));

    calendar.add(Calendar.SECOND, oneginiMobileAuthWithPushRequest.getTimeToLiveSeconds());
    viewHolder.expiresTextView.setText(context.getString(R.string.notification_expires_at, sdf.format(calendar.getTime())));

    viewHolder.onClickListener = v -> context.startService(getServiceIntent(oneginiMobileAuthWithPushRequest));
    setDenyButtonListener(viewHolder, oneginiMobileAuthWithPushRequest);
  }

  private void setDenyButtonListener(final ViewHolder viewHolder, final OneginiMobileAuthWithPushRequest oneginiMobileAuthWithPushRequest) {
    viewHolder.denyButton
        .setOnClickListener(v -> OneginiSDK.getOneginiClient(context).getUserClient().denyMobileAuthWithPushRequest(oneginiMobileAuthWithPushRequest,
            new OneginiDenyMobileAuthWithPushRequestHandler() {
              @Override
              public void onSuccess() {
                removeRequestFromList(oneginiMobileAuthWithPushRequest.getTransactionId());
                notifyDataSetChanged();
              }

              @Override
              public void onError(final OneginiDenyMobileAuthWithPushRequestError error) {
                @OneginiDenyMobileAuthWithPushRequestError.DenyMobileAuthWithPushRequestErrorType int errorType = error.getErrorType();
                if (errorType == OneginiAuthenticatorDeregistrationError.USER_DEREGISTERED) {
                  final UserProfile authenticatedUserProfile = OneginiSDK.getOneginiClient(context).getUserClient().getAuthenticatedUserProfile();
                  new DeregistrationUtil(context).onUserDeregistered(authenticatedUserProfile);
                  startLoginActivity(error.getMessage());
                } else if (errorType == OneginiAuthenticatorDeregistrationError.DEVICE_DEREGISTERED) {
                  new DeregistrationUtil(context).onDeviceDeregistered();
                  startLoginActivity(error.getMessage());
                } else {
                  Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
              }
            }));
  }

  private void removeRequestFromList(final String transactionId) {
    final Iterator<OneginiMobileAuthWithPushRequest> iterator = list.iterator();
    while (iterator.hasNext()) {
      final OneginiMobileAuthWithPushRequest request = iterator.next();
      if (request.getTransactionId().equals(transactionId)) {
        iterator.remove();
      }
    }
  }

  private void startLoginActivity(final String errorMessage) {
    final Intent intent = new Intent(context, LoginActivity.class);
    intent.putExtra(LoginActivity.ERROR_MESSAGE_EXTRA, errorMessage);
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
    context.startActivity(intent);
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  private Intent getServiceIntent(final OneginiMobileAuthWithPushRequest mobileAuthWithPushRequest) {
    final Intent intent = new Intent(context, MobileAuthenticationService.class);
    intent.putExtra(MobileAuthenticationService.EXTRA_TRANSACTION_ID, mobileAuthWithPushRequest.getTransactionId());
    intent.putExtra(MobileAuthenticationService.EXTRA_MESSAGE, mobileAuthWithPushRequest.getMessage());
    intent.putExtra(MobileAuthenticationService.EXTRA_PROFILE_ID, mobileAuthWithPushRequest.getUserProfileId());
    return intent;
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    final TextView messageTextView;
    final TextView dateTextView;
    final TextView profileTextView;
    final TextView expiresTextView;
    final ImageView denyButton;

    OnClickListener onClickListener;

    ViewHolder(final View itemView) {
      super(itemView);

      messageTextView = itemView.findViewById(R.id.pending_message);
      dateTextView = itemView.findViewById(R.id.pending_message_timestamp);
      profileTextView = itemView.findViewById(R.id.pending_message_profile);
      expiresTextView = itemView.findViewById(R.id.pending_message_expires);
      denyButton = itemView.findViewById(R.id.pending_message_deny);
      itemView.setOnClickListener(v -> {
        if (onClickListener != null) {
          onClickListener.onClick(v);
        }
      });
    }
  }
}
