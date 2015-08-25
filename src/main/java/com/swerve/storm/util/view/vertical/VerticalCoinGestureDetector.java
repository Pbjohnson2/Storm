package com.swerve.storm.util.view.vertical;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ScrollView;
import com.swerve.storm.R;
import com.swerve.storm.util.view.GestureToucher;

public class VerticalCoinGestureDetector implements GestureDetector.OnGestureListener, GestureToucher{
    private final ImageView [] coins;
    private final Rect screenBounds;
    private final int screenWidth;
    private final int halfCoinWidth;
    private final float heightPosition;
    private final Activity activity;

    public VerticalCoinGestureDetector (final ImageView [] coins, final int screenWidth, final int halfCoinWidth, final float heightPosition, final ScrollView verticalScrollView, final Activity activity) {
        this.coins = coins;
        this.screenWidth = screenWidth;
        this.halfCoinWidth = halfCoinWidth;
        this.heightPosition = heightPosition;
        this.activity = activity;
        screenBounds = new Rect();
        verticalScrollView.getHitRect(screenBounds);
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
        final ImageView coin = coins[1];
        coin.setX(coin.getX() - distanceX);
        coin.setY(coin.getY() - distanceY);
        coin.requestLayout();
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
        final ImageView coin = coins[1];
        final ViewGroup parent = (ViewGroup) coin.getParent();
        final ImageView clone = copyCoin(coin, parent);

        parent.addView(clone);
        coins[1] = clone;

        coinToPosition(clone, ((screenWidth / 2) - halfCoinWidth), parent.getHeight() + coins[2].getHeight());
        animateCoinToPosition(clone, (int) coins[2].getY());
        animateFling(coin, previousVelocityX, previousVelocityY);
        return true;
    }

    private ImageView copyCoin(final ImageView coin, final ViewGroup parent) {
        final ImageView clone = new ImageView(activity);
        clone.setImageDrawable(activity.getResources().getDrawable(R.drawable.coin_ten));
        clone.setScaleType(coin.getScaleType());
        clone.setLayoutParams(coin.getLayoutParams());
        return clone;
    }

    private void animateFling(final ImageView coin, final float previousVelocityX, final float previousVelocityY) {
        final ValueAnimator pathAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        pathAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private final float accelerationGravity = -0.013f;

            final float startX = coin.getX();
            final float startY = coin.getY();
            float currentVelocityX = previousVelocityX;
            float currentVelocityY = -1 * previousVelocityY;
            long startTimeMillis = 0;

            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                final long currentTimeMillis = System.currentTimeMillis();
                if (startTimeMillis == 0) {
                    startTimeMillis = currentTimeMillis;
                    return;
                }
                if (!isCoinOnScreen(coin)) {
                    ((ViewManager) coin.getParent()).removeView(coin);
                    animation.cancel();
                }
                final long changeInTime = currentTimeMillis - startTimeMillis;
                final float distanceX = (float) calculateDistance(currentVelocityX, 0, changeInTime);
                final float distanceY = (float) calculateDistance(currentVelocityY, accelerationGravity, changeInTime);
                Log.d("distanceX", String.format("distanceX:%f", distanceX));
                Log.d("distanceY", String.format("distanceY:%f", distanceY));
                Log.d("changeInTime", "changeInTime:" + changeInTime);

                coin.setX(startX + distanceX);
                coin.setY(startY - distanceY);
                coin.requestLayout();
            }
        });
        pathAnimator.setDuration(10000).start();
    }

    private void coinToPosition(final ImageView coin, final int endValueX, final int endValueY) {
        coin.setX(endValueX);
        coin.setY(endValueY);
        coin.requestLayout();
    }

    private void animateCoinToPosition(final ImageView coin, final int endValueY) {
        final int startValueY = (int) coin.getY();
        coin.clearAnimation();
        final Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                coin.setY((int) (startValueY + (endValueY - startValueY) * interpolatedTime));
                coin.requestLayout();
            }
        };
        animation.setDuration(100);
        animation.setInterpolator(new DecelerateInterpolator());
        coin.startAnimation(animation);
    }

    private double calculateDistance (final float velocity, final float acceleration, final long changeInTime) {
        return ((velocity * changeInTime) + (0.5 * acceleration * Math.pow(changeInTime, 2)));
    }

    private boolean isCoinOnScreen(final ImageView imageView) {
        return imageView.getLocalVisibleRect(screenBounds);
    }

    private void isCoinWithinCloud(final MotionEvent motionEvent, final ImageView imageView, final Context context) {
        final Rect imageViewArea = new Rect();
        imageView.getGlobalVisibleRect(imageViewArea);

        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();

        //not working
        if(imageViewArea.contains(x, y)) {
            Log.d("You are within bounds", "");
        }
    }
}
