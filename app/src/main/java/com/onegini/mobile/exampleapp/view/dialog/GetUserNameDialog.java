package com.onegini.mobile.exampleapp.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;
import butterknife.Bind;
import com.onegini.mobile.exampleapp.R;

public class GetUserNameDialog extends DialogFragment {

  public static final String TAG = "GetUserNameDialog";

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.edit_text_name)
  EditText nameField;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();

    builder.setView(inflater.inflate(R.layout.dialog_get_user_name, null))
        .setPositiveButton(R.string.dialog_get_user_name_button_ok, (dialog, id) -> {
          // TODO log in user
        });
    return builder.create();
  }
}