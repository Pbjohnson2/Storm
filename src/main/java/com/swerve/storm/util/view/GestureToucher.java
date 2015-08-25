package com.swerve.storm.util.view;

import android.view.VelocityTracker;

public interface GestureToucher {
    boolean onTouchUp(final float previousVelocityX, final float previousVelocityY);
}
