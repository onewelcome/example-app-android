package com.onegini.mobile.exampleapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.model.User;
import com.onegini.mobile.sdk.android.library.model.entity.UserProfile;

public class GetUserNameView extends RelativeLayout {

  private final EditText editText;
  private User user;

  public GetUserNameView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    mInflater.inflate(R.layout.get_user_name_view, this, true);

    editText = (EditText) findViewById(R.id.edit_text_name);
  }

  public void setup(final UserProfile userProfile) {
    this.setVisibility(VISIBLE);
    this.user = new User(userProfile);
  }

  public User getUser() {
    user.setName(editText.getText().toString());
    return user;
  }
}
