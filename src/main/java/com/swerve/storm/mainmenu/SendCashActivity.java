package com.swerve.storm.mainmenu;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.swerve.storm.R;
import com.swerve.storm.util.view.MoneyViewManager;
import com.swerve.storm.util.view.horizontal.HorizontalCoinGestureDetector;
import com.swerve.storm.util.view.CoinTouchListener;
import com.swerve.storm.util.view.vertical.VerticalCoinGestureDetector;

public class SendCashActivity extends Activity {
    private ImageView [] coins = new ImageView[3];
    private RelativeLayout parentView;
    private ScrollView verticalCoinFlinger;
    private HorizontalScrollView horizontalCoinScroller;
    private MoneyViewManager moneyViewManager;
    private ImageButton cashCloud;

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
        cashCloud = (ImageButton) findViewById(R.id.image_storm_cloud);
        initializeView();
    }

    private void initializeView() {
        parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                parentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                final int screenWidth = parentView.getWidth();
                final int screenHeight = parentView.getHeight();
                Log.d("SendCashActivity", String.format("onCreate:screenWidth:%d", screenWidth));
                final int halfWidth = ((ViewGroup.MarginLayoutParams) coins[0].getLayoutParams()).width / 2;

                moneyViewManager = new MoneyViewManager(SendCashActivity.this, parentView, screenWidth, halfWidth, screenHeight, coins, cashCloud, 0);
                //Must be initialized here
                moneyViewManager.initializeCoinViews();

                initializeHorizontalCoinScroller();
                initializeVerticalCoinFlinger();
            }
        });
    }

    private void initializeHorizontalCoinScroller() {
        final HorizontalCoinGestureDetector horizontalCoinGestureDetector = new HorizontalCoinGestureDetector(moneyViewManager);
        final GestureDetectorCompat detector = new GestureDetectorCompat(SendCashActivity.this, horizontalCoinGestureDetector);
        final CoinTouchListener touchListener = new CoinTouchListener(detector, horizontalCoinGestureDetector);
        horizontalCoinScroller.setOnTouchListener(touchListener);
    }

    private void initializeVerticalCoinFlinger() {
        final VerticalCoinGestureDetector verticalCoinGestureDetector = new VerticalCoinGestureDetector(moneyViewManager);
        final GestureDetectorCompat detector = new GestureDetectorCompat(SendCashActivity.this, verticalCoinGestureDetector);
        final CoinTouchListener touchListener = new CoinTouchListener(detector, verticalCoinGestureDetector);
        verticalCoinFlinger.setOnTouchListener(touchListener);

    }
}
