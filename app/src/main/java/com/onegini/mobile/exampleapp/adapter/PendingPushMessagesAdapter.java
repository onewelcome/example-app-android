/*
 * Copyright (c) 2016-2017 Onegini B.V.
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthWithPushRequest;

public class PendingPushMessagesAdapter extends RecyclerView.Adapter<PendingPushMessagesAdapter.ViewHolder> {

  private final List<OneginiMobileAuthWithPushRequest> list;
  private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd.MM.yy", Locale.US);

  public PendingPushMessagesAdapter(final Set<OneginiMobileAuthWithPushRequest> set) {
    list = new ArrayList<>(set);
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    final TextView messageTextView;
    final TextView dateTextView;

    ViewHolder(View itemView) {
      super(itemView);

      messageTextView = itemView.findViewById(R.id.pending_message);
      dateTextView = itemView.findViewById(R.id.pending_message_timestamp);
    }
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
    final String message = new StringBuilder(oneginiMobileAuthWithPushRequest.getUserProfileId())
        .append(": ")
        .append(oneginiMobileAuthWithPushRequest.getMessage())
        .toString();
    final String date = sdf.format(new Date(oneginiMobileAuthWithPushRequest.getTimestamp()));

    viewHolder.messageTextView.setText(message);
    viewHolder.dateTextView.setText(date);
  }

  @Override
  public int getItemCount() {
    return list.size();
  }
}
