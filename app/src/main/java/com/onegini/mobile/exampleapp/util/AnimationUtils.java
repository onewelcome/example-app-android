package com.onegini.mobile.exampleapp.util;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;

public class AnimationUtils {

  private AnimationUtils() {
  }

  public static Animation getBlinkAnimation() {
    final Animation animation = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f);
    animation.setDuration(350);
    animation.setInterpolator(new LinearInterpolator());
    animation.setRepeatCount(1);
    animation.setRepeatMode(Animation.REVERSE);

    return animation;
  }
}
