package com.onegini.mobile.exampleapp.view.helper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class AlertDialogFragment extends DialogFragment {

  private static final String EXTRA_TITLE = "title";
  private static final String EXTRA_MESSAGE = "message";

  public static AlertDialogFragment newInstance(final String errorMessage) {
    final AlertDialogFragment fragment = new AlertDialogFragment();
    final Bundle args = new Bundle();
    args.putString(EXTRA_MESSAGE, errorMessage);
    fragment.setArguments(args);

    return fragment;
  }

  public static AlertDialogFragment newInstance(final String errorMessage, final String title) {
    final AlertDialogFragment fragment = new AlertDialogFragment();

    final Bundle args = new Bundle();
    args.putString(EXTRA_TITLE, title);
    args.putString(EXTRA_MESSAGE, errorMessage);
    fragment.setArguments(args);

    return fragment;
  }

  @Override
  public Dialog onCreateDialog(final Bundle savedInstanceState) {
    final Bundle arguments = getArguments();
    final String title = arguments.getString(EXTRA_TITLE, "Error");
    final String message = arguments.getString(EXTRA_MESSAGE);


    return new AlertDialog.Builder(getActivity())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("OK", (dialog, which) -> { })
        .create();
  }
}
