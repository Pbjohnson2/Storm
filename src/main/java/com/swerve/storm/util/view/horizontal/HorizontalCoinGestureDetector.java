package com.swerve.storm.util.view.horizontal;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import com.swerve.storm.util.view.GestureToucher;
import com.swerve.storm.util.view.MoneyViewManager;

public class HorizontalCoinGestureDetector implements GestureDetector.OnGestureListener, GestureToucher {
    private final MoneyViewManager moneyViewManager;

    public HorizontalCoinGestureDetector(final MoneyViewManager moneyViewManager) {
        this.moneyViewManager = moneyViewManager;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        Log.i("CenterLockHorizontalScroll", "onDown");
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
    public boolean onScroll(MotionEvent motionEvent1, MotionEvent motionEvent2, float distanceX, float distanceY) {
        Log.i("CenterLockHorizontalScroll", "onScroll");

        final boolean scrollingRight = motionEvent1.getX() < motionEvent2.getX();
        return moneyViewManager.onHorizontalScroll(scrollingRight, distanceX);
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent1, MotionEvent motionEvent2, float velocityX, float velocityY) {
        Log.i("CenterLockHorizontalScroll", "onFling");
        return true;
    }

    @Override
    public boolean onTouchUp(final float previousVelocityX, final float previousVelocityY){
        Log.d("HorizontalScrollView", "onTouchUp");
        return moneyViewManager.onHorizontalTouchUp();
    }
}