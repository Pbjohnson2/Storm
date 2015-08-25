package com.swerve.storm.mainmenu;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import com.swerve.storm.R;
import com.swerve.storm.util.view.horizontal.HorizontalCoinGestureDetector;
import com.swerve.storm.util.view.CoinTouchListener;
import com.swerve.storm.util.view.vertical.VerticalCoinGestureDetector;

public class SendCashActivity extends Activity {
    private ImageView [] coins = new ImageView[3];
    private RelativeLayout parentView;
    private ScrollView verticalCoinFlinger;
    private HorizontalScrollView horizontalCoinScroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("SendCashActivity", "onCreate");
        setContentView(R.layout.activity_send_cash);
        coins[0] = (ImageView) findViewById(R.id.left_coin);
        coins[1] = (ImageView) findViewById(R.id.center_coin);
        coins[2] = (ImageView) findViewById(R.id.right_coin);
        parentView = (RelativeLayout) findViewById(R.id.send_cash_relative_view);
        verticalCoinFlinger = (ScrollView) findViewById(R.id.vertical_coin_flinger);
        horizontalCoinScroller = (HorizontalScrollView) findViewById(R.id.horizontal_coin_scroller);
        initializeView();
    }

    private void initializeView() {
        parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                parentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                final int screenWidth = parentView.getWidth();
                final int screenHeight = verticalCoinFlinger.getHeight();
                Log.d("SendCashActivity", String.format("onCreate:screenWidth:%d", screenWidth));
                final int halfWidth = ((ViewGroup.MarginLayoutParams) coins[0].getLayoutParams()).width / 2;

                initializeCoinViews(screenWidth, halfWidth, screenHeight);
                initializeHorizontalCoinScroller(screenWidth, halfWidth * 2);
                initializeVerticalCoinFlinger(screenWidth, halfWidth, screenHeight);
            }
        });
    }

    private void initializeCoinViews(final int screenWidth, final int halfWidth, final int screenHeight) {
        coinToPosition(coins[0], -(halfWidth * 2), screenHeight / 3);
        coinToPosition(coins[1], ((screenWidth / 2) - halfWidth), screenHeight / 3);
        coinToPosition(coins[2], (screenWidth) - halfWidth, screenHeight / 3);
    }

    private void initializeHorizontalCoinScroller(final int screenWidth, final int imageWidth) {
        final HorizontalCoinGestureDetector horizontalCoinGestureDetector = new HorizontalCoinGestureDetector(screenWidth, imageWidth, 0, coins);
        final GestureDetectorCompat detector = new GestureDetectorCompat(SendCashActivity.this, horizontalCoinGestureDetector);
        final CoinTouchListener touchListener = new CoinTouchListener(detector, horizontalCoinGestureDetector);
        horizontalCoinScroller.setOnTouchListener(touchListener);
    }

    private void initializeVerticalCoinFlinger(final int screenWidth, final int imageWidth, final float heightPosition) {
        final VerticalCoinGestureDetector verticalCoinGestureDetector = new VerticalCoinGestureDetector(coins, screenWidth, imageWidth, heightPosition, verticalCoinFlinger, SendCashActivity.this);
        final GestureDetectorCompat detector = new GestureDetectorCompat(SendCashActivity.this, verticalCoinGestureDetector);
        final CoinTouchListener touchListener = new CoinTouchListener(detector, verticalCoinGestureDetector);
        verticalCoinFlinger.setOnTouchListener(touchListener);

    }

    private void coinToPosition(final ImageView coin, final int endValueX, final int endValueY) {
        coin.setX(endValueX);
        coin.setY(endValueY);
        coin.requestLayout();
    }
}
