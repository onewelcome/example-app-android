package com.onegini.mobile.exampleapp.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.model.Device;

public class DevicesAdapter extends
    RecyclerView.Adapter<DevicesAdapter.ViewHolder> {

  private List<Device> devices;

  public DevicesAdapter(List<Device> contacts) {
    devices = contacts;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    public TextView nameTextView;

    public ViewHolder(View itemView) {
      super(itemView);

      nameTextView = (TextView) itemView.findViewById(R.id.textView);
    }
  }

  @Override
  public DevicesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);

    View deviceItemView = inflater.inflate(R.layout.row_item_device, parent, false);

    return new ViewHolder(deviceItemView);
  }

  @Override
  public void onBindViewHolder(DevicesAdapter.ViewHolder viewHolder, int position) {
    Device device = devices.get(position);

    TextView textView = viewHolder.nameTextView;
    textView.setText(device.getDeviceFullInfo());
  }

  @Override
  public int getItemCount() {
    return devices.size();
  }
}