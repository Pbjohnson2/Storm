package com.swerve.storm.util.view;

import android.view.GestureDetector;
import android.view.VelocityTracker;

public interface GestureToucher extends GestureDetector.OnGestureListener{
    boolean onTouchUp(final float previousVelocityX, final float previousVelocityY);
}
