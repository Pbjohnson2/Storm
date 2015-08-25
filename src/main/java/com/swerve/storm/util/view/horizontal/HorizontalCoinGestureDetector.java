package com.swerve.storm.util.view.horizontal;

import android.graphics.Color;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.*;
import android.widget.ImageView;
import com.swerve.storm.model.StormCurrency;
import com.swerve.storm.util.view.GestureToucher;

public class HorizontalCoinGestureDetector implements GestureDetector.OnGestureListener, GestureToucher {
    private final float screenWidth;
    private final int coinImageWidth;

    //Index in the coin array
    private int indexInCoinArray;
    private ImageView [] coins;

    private boolean impossibleScroll;

    public HorizontalCoinGestureDetector(float screenWidth, int coinImageWidth, final int indexInCoinArray, final ImageView[] coins) {
        this.screenWidth = screenWidth;
        this.coinImageWidth = coinImageWidth;
        this.indexInCoinArray = indexInCoinArray;
        this.coins = coins;
        this.impossibleScroll = false;
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
        if (scrollingRight && indexInCoinArray == 0 || !scrollingRight && indexInCoinArray >= StormCurrency.values().length - 1) {
            Log.i("CenterLockHorizontalScroll", "onScroll:noMovement");
            impossibleScroll = true;
            return false;
        }
        impossibleScroll = false;
        for (final ImageView coin: coins) {
            coin.setX(coin.getX() - distanceX);
            coin.requestLayout();
        }
        return true;
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
        if (impossibleScroll) {
            return false;
        }
        int closestIndex = -1;
        int closestDistance = Integer.MAX_VALUE;
        for (int currentIndex = 0; currentIndex < coins.length ; currentIndex++) {
            final int currentDistance = computeDistanceToCenter(coins[currentIndex]);
            if (closestDistance > currentDistance) {
                closestIndex = currentIndex;
                closestDistance = currentDistance;
            }
        }
        Log.d("HorizontalScrollView", "onTouchUp:closestIndex:" + closestIndex);

        final int endValueX = ((int) screenWidth / 2) - (coinImageWidth / 2);
        animateCoinToPosition(coins[closestIndex], endValueX);

        if (closestIndex == 0) {
            rotateRight();
        } else if (closestIndex == 1) {
            centerLock();
        } else {
            rotateLeft();
        }
        Log.i("CenterLockHorizontalScroll", "onTouchUp:indexInCoinArray" + Integer.toString(indexInCoinArray));
        return true;
    }

    private void rotateRight() {
        indexInCoinArray --;
        //Right coin moves to far left
        coinToPosition(coins[2], 0 - coinImageWidth);
        if (indexInCoinArray == 0) {
            coins[2].setBackgroundColor(Color.TRANSPARENT);
        } else {
            final StormCurrency newCurrency = StormCurrency.values()[indexInCoinArray - 1];
            coins[2].setBackgroundResource(newCurrency.getResid());
        }
        //Center coin moves right
        animateCoinToPosition(coins[1], (int) (screenWidth - (coinImageWidth / 2)));
        rotateImages(2, 0, 1);
    }

    private void centerLock() {
        //Left coin moves left
        animateCoinToPosition(coins[0], 0 - coinImageWidth);
        //Right coin moves right
        animateCoinToPosition(coins[2], (int) (screenWidth - (coinImageWidth / 2)));
    }

    private void rotateLeft () {
        indexInCoinArray ++;
        //Center coin moves left
        animateCoinToPosition(coins[1], 0 - coinImageWidth);
        //Left coin moves Right
        coinToPosition(coins[0], (int) screenWidth);
        if (indexInCoinArray >= StormCurrency.values().length - 1) {
            coins[0].setBackgroundColor(Color.TRANSPARENT);
        } else {
            final StormCurrency newCurrency = StormCurrency.values()[indexInCoinArray + 1];
            coins[0].setBackgroundResource(newCurrency.getResid());
        }
        //Replace image in coin with
        animateCoinToPosition(coins[0], (int) (screenWidth - (coinImageWidth / 2)));
        rotateImages(1, 2, 0);
    }

    private void rotateImages(final int start, final int center, final int end) {
        final ImageView startImage = coins[start];
        final ImageView centerImage = coins[center];
        final ImageView endImage = coins[end];
        coins[0] = startImage;
        coins[1] = centerImage;
        coins[2] = endImage;
    }

    private int computeDistanceToCenter(final ImageView coin) {
        final int distanceToCenter = (int)((screenWidth / 2) - (coin.getX() + coinImageWidth / 2));
        return Math.abs(distanceToCenter);
    }

    private void coinToPosition(final ImageView coin, final int endValueX) {
        coin.setX(endValueX);
        coin.requestLayout();
    }

    private void animateCoinToPosition(final ImageView coin, final int endValueX) {
        final int startValueX = (int) coin.getX();
        coin.clearAnimation();
        final Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                coin.setX((int) (startValueX + (endValueX - startValueX) * interpolatedTime));
                coin.requestLayout();
            }
        };
        animation.setDuration(200);
        animation.setInterpolator(new DecelerateInterpolator());
        coin.startAnimation(animation);
    }
}