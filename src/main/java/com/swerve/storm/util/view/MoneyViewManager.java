package com.swerve.storm.util.view;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.animation.*;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.swerve.storm.model.StormCurrency;

public class MoneyViewManager {
    private static final StormCurrency [] STORM_CURRENCY_VALUES = StormCurrency.values();
    private static final int ACTIVE_COIN_INDEX = 1;
    private final Activity activity;
    private final Rect screenBounds;
    private final ViewGroup coinParentView;
    private final RelativeLayout frontWalletRelativeLayout;
    private final ImageView frontWalletImageView;
    private final int screenWidth;
    private final int halfCoinImageWidth;
    private final int coinImageWidth;
    private final int screenHeight;
    private final ImageView [] coins;
    private final ImageButton cloudImage;

    private int indexInCoinArray;
    private boolean impossibleScroll;

    public MoneyViewManager (final Activity activity, final ViewGroup coinParentView, final RelativeLayout frontWalletRelativeLayout, final ImageView frontWalletImageView, final int screenWidth, final int halfCoinImageWidth, final int screenHeight, final ImageView [] coins, final ImageButton cashCloud, final int indexInCoinArray){
        this.activity = activity;
        this.screenBounds = new Rect();
        this.frontWalletRelativeLayout = frontWalletRelativeLayout;
        this.frontWalletImageView = frontWalletImageView;
        coinParentView.getHitRect(screenBounds);
        this.coinParentView = coinParentView;
        this.screenWidth = screenWidth;
        this.halfCoinImageWidth = halfCoinImageWidth;
        this.coinImageWidth = halfCoinImageWidth * 2;
        this.screenHeight = screenHeight;
        this.coins = coins;
        this.cloudImage = cashCloud;
        this.indexInCoinArray = indexInCoinArray;
        this.impossibleScroll = false;
    }

    public void initializeCoinViews() {
        final int coinHeights = screenHeight - coinImageWidth / 2 - frontWalletImageView.getHeight();
        coinToPosition(coins[0], -(halfCoinImageWidth * 2), coinHeights);
        coinToPosition(coins[1], ((screenWidth / 2) - halfCoinImageWidth), coinHeights);
        coinToPosition(coins[2], (screenWidth) - halfCoinImageWidth, coinHeights);
    }

    public void onFling (final float velocityX, final float velocityY) {
        final ImageView coin = coins[ACTIVE_COIN_INDEX];
        final ImageView clone = copyCoin(coin);
        frontWalletRelativeLayout.addView(clone, frontWalletRelativeLayout.indexOfChild(coin));
        coins[ACTIVE_COIN_INDEX] = clone;

        coinToPosition(clone, ((screenWidth / 2) - halfCoinImageWidth), coinParentView.getHeight() + coins[2].getHeight());
        animateCoinToYPosition(clone, (int) coins[2].getY());
        animateFling(coin, velocityX, velocityY);
    }

    public void onScrollActiveCoin(float distanceX, float distanceY) {
        final ImageView coin = coins[ACTIVE_COIN_INDEX];
        coinToPosition(coin, coin.getX() - distanceX, coin.getY() - distanceY);
    }

    public boolean onHorizontalScroll(final boolean scrollingRight, float distanceX) {
        Log.i("CenterLockHorizontalScroll", "onScroll");

        if (scrollingRight && indexInCoinArray == 0 || !scrollingRight && indexInCoinArray >= STORM_CURRENCY_VALUES.length - 1) {
            Log.i("CenterLockHorizontalScroll", "onScroll:noMovement");
            impossibleScroll = true;
            return false;
        }
        impossibleScroll = false;
        for (final ImageView coin: coins) {
            coinToPosition(coin, coin.getX() - distanceX, coin.getY());
        }
        return true;
    }

    public boolean onHorizontalTouchUp(){
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
        animateCoinToXPosition(coins[closestIndex], endValueX);

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

    private int computeDistanceToCenter(final ImageView coin) {
        final int distanceToCenter = (int)((screenWidth / 2) - (coin.getX() + coinImageWidth / 2));
        return Math.abs(distanceToCenter);
    }

    private void rotateRight() {
        indexInCoinArray --;
        //Right coin moves to far left
        coinToPosition(coins[2], 0 - coinImageWidth, coins[2].getY());
        if (indexInCoinArray == 0) {
            coins[2].setBackgroundColor(Color.TRANSPARENT);
        } else {
            final StormCurrency newCurrency = STORM_CURRENCY_VALUES[indexInCoinArray - 1];
            coins[2].setBackgroundResource(newCurrency.getResid());
        }
        //Center coin moves right
        animateCoinToXPosition(coins[1], (int) (screenWidth - (coinImageWidth / 2)));
        rotateImages(2, 0, 1);
    }

    private void centerLock() {
        //Left coin moves left
        animateCoinToXPosition(coins[0], 0 - coinImageWidth);
        //Right coin moves right
        animateCoinToXPosition(coins[2], (int) (screenWidth - (coinImageWidth / 2)));
    }

    private void rotateImages(final int start, final int center, final int end) {
        final ImageView startImage = coins[start];
        final ImageView centerImage = coins[center];
        final ImageView endImage = coins[end];
        coins[0] = startImage;
        coins[1] = centerImage;
        coins[2] = endImage;
    }

    private void rotateLeft () {
        indexInCoinArray ++;
        //Center coin moves left
        animateCoinToXPosition(coins[1], 0 - coinImageWidth);
        //Left coin moves Right
        coinToPosition(coins[0], screenWidth, coins[0].getY());
        if (indexInCoinArray >= STORM_CURRENCY_VALUES.length - 1) {
            coins[0].setBackgroundColor(Color.TRANSPARENT);
        } else {
            final StormCurrency newCurrency = STORM_CURRENCY_VALUES[indexInCoinArray + 1];
            Log.d("newStormResId", String.format("stormCurrencyName:%s", newCurrency.name()));
            coins[0].setBackgroundResource(newCurrency.getResid());
        }
        //Replace image in coin with
        animateCoinToXPosition(coins[0], (screenWidth - (coinImageWidth / 2)));
        rotateImages(1, 2, 0);
    }

    private ImageView copyCoin(final ImageView coin) {
        final ImageView clone = new ImageView(activity);
        clone.setBackgroundResource(STORM_CURRENCY_VALUES[indexInCoinArray].getResid());
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
                } else if (isCoinWithinCloud(coin, cloudImage)) {
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

    private void animateCoinToYPosition(final ImageView coin, final int endValueY) {
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

    private void animateCoinToXPosition(final ImageView coin, final int endValueX) {
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

    private void coinToPosition(final ImageView coin, final float endValueX, final float endValueY) {
        coin.setX(endValueX);
        coin.setY(endValueY);
        coin.requestLayout();
    }

    private double calculateDistance (final float velocity, final float acceleration, final long changeInTime) {
        return ((velocity * changeInTime) + (0.5 * acceleration * Math.pow(changeInTime, 2)));
    }

    private boolean isCoinOnScreen(final ImageView imageView) {
        return imageView.getLocalVisibleRect(screenBounds);
    }

    private boolean isCoinWithinCloud(final ImageView coin, final ImageButton cloud) {
        final Rect imageViewArea = new Rect();
        cloud.getGlobalVisibleRect(imageViewArea);
        return imageViewArea.contains((int) coin.getX(), (int) coin.getY());
    }
}
