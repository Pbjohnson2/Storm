package com.swerve.storm.util.view.vertical;

import android.util.Log;
import android.view.*;
import com.swerve.storm.util.view.GestureToucher;
import com.swerve.storm.util.view.MoneyViewManager;

public class VerticalCoinGestureDetector implements GestureToucher{
    private final MoneyViewManager moneyViewManager;

    public VerticalCoinGestureDetector (final MoneyViewManager moneyViewManager) {
        this.moneyViewManager = moneyViewManager;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float distanceX, float distanceY) {
        Log.i("CenterLockHorizontalScroll", "onScroll");
        moneyViewManager.onScrollActiveCoin(distanceX, distanceY);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public boolean onTouchUp(final float previousVelocityX, final float previousVelocityY) {
        moneyViewManager.onFling(previousVelocityX, previousVelocityY);
        return true;
    }
}
