package com.swerve.storm.util.view;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

public class CoinTouchListener implements View.OnTouchListener{
    private final GestureDetectorCompat detector;
    private final GestureToucher gestureToucher;
    private VelocityTracker velocityTracker;
    private float previousVelocityX;
    private float previousVelocityY;

    public CoinTouchListener(final GestureDetectorCompat detector, final GestureToucher gestureToucher) {
        this.detector = detector;
        this.gestureToucher = gestureToucher;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.i("CenterLockTouchListener", "onTouch:" + motionEvent.toString());
        int index = motionEvent.getActionIndex();
        int action = motionEvent.getActionMasked();
        int pointerId = motionEvent.getPointerId(index);
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                if(velocityTracker == null) {
                    // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                    velocityTracker = VelocityTracker.obtain();
                }
                else {
                    // Reset the velocity tracker back to its initial state.
                    velocityTracker.clear();
                }
                // Add a user's movement to the tracker.
                velocityTracker.addMovement(motionEvent);
                break;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.addMovement(motionEvent);
                velocityTracker.computeCurrentVelocity(1);
                previousVelocityX = VelocityTrackerCompat.getXVelocity(velocityTracker, pointerId);
                previousVelocityY = VelocityTrackerCompat.getYVelocity(velocityTracker, pointerId);
                Log.d("", "X velocity: " +
                        VelocityTrackerCompat.getXVelocity(velocityTracker,
                                pointerId));
                Log.d("", "Y velocity: " +
                        VelocityTrackerCompat.getYVelocity(velocityTracker,
                                pointerId));
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.addMovement(motionEvent);
                // When you want to determine the velocity, call
                // computeCurrentVelocity(). Then call getXVelocity()
                // and getYVelocity() to retrieve the velocity for each pointer ID.
                velocityTracker.computeCurrentVelocity(1);
                // Log velocity of pixels per second
                // Best practice to use VelocityTrackerCompat where possible.
                Log.d("", "X velocity: " +
                        VelocityTrackerCompat.getXVelocity(velocityTracker,
                                pointerId));
                Log.d("", "Y velocity: " +
                        VelocityTrackerCompat.getYVelocity(velocityTracker,
                                pointerId));
                Log.d("CoinTouchListener", String.format("onTouchUp:previousVelocityX:%f:previousVelocityY:%f", previousVelocityX, previousVelocityY));
                final float currentVelocityX = VelocityTrackerCompat.getXVelocity(velocityTracker, pointerId);
                final float currentVelocityY = VelocityTrackerCompat.getYVelocity(velocityTracker, pointerId);
                final boolean onTouch = gestureToucher.onTouchUp(
                        currentVelocityX,
                        currentVelocityY);
                previousVelocityX = 0;
                previousVelocityY = 0;
                return onTouch;
            case MotionEvent.ACTION_CANCEL:
                // Return a VelocityTracker object back to be re-used by others.
                velocityTracker.recycle();
                break;
        }
        return detector.onTouchEvent(motionEvent);
    }
}
