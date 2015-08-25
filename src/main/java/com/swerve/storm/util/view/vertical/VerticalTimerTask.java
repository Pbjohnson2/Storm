package com.swerve.storm.util.view.vertical;

import android.app.Activity;
import android.util.Log;
import android.widget.ImageView;

import java.util.TimerTask;

public class VerticalTimerTask extends TimerTask {
    private static final float ACCELERATION_Y = -0.001f;
    private final float currentVelocityX;
    private float currentVelocityY;
    private final float timeInterval;
    private final ImageView coinImage;
    private final Activity activity;

    public VerticalTimerTask (final float currentVelocityX, final float currentVelocityY, final int timeInterval, final ImageView coinImage, final Activity activity) {
        this.currentVelocityX = currentVelocityX;
        this.currentVelocityY = currentVelocityY;
        this.timeInterval = timeInterval;
        this.coinImage = coinImage;
        this.activity = activity;
    }

    @Override
    public void run() {
        final int distanceX = calculateDistance(currentVelocityX, 0);
        final int distanceY = calculateDistance(currentVelocityY, ACCELERATION_Y);
        currentVelocityY = currentVelocityY + (ACCELERATION_Y * timeInterval);
        Log.d("currentVelocityY", String.format("currentVelocityY:%f", currentVelocityY));

        coinImage.setX(coinImage.getX() + distanceX);
        coinImage.setY(coinImage.getY() + distanceY);
        activity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        coinImage.requestLayout();
                    }
                }
        );
    }

    private int calculateDistance (final float velocity, final float acceleration) {
        return (int) ((velocity * timeInterval) + (0.5 * acceleration * Math.pow(timeInterval, 2)));
    }
}
