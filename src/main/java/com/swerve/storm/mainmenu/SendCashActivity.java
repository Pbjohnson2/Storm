package com.swerve.storm.mainmenu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.gesture.Gesture;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import com.google.common.util.concurrent.FutureCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.swerve.storm.R;
import com.swerve.storm.contacts.ContactsActivity;
import com.swerve.storm.model.StormContact;
import com.swerve.storm.service.StormServiceAsyncClient;
import com.swerve.storm.util.view.GestureToucher;
import com.swerve.storm.util.view.MoneyViewManager;
import com.swerve.storm.util.view.WindmillProgressBar;
import com.swerve.storm.util.view.horizontal.HorizontalCoinGestureDetector;
import com.swerve.storm.util.view.CoinTouchListener;
import com.swerve.storm.util.view.transformer.CircleTransform;
import com.swerve.storm.util.view.vertical.VerticalCoinGestureDetector;

import java.util.Objects;

import lombok.RequiredArgsConstructor;

public class SendCashActivity extends Activity {
    private ImageView [] coins = new ImageView[3];
    private RelativeLayout parentView;
    private ScrollView verticalCoinFlinger;
    private HorizontalScrollView horizontalCoinScroller;
    private MoneyViewManager moneyViewManager;
    private ImageButton cashCloud;
    private RelativeLayout frontWalletRelativeLayout;
    private ImageView backWalletImageView;
    private ImageView frontWalletImageView;
    private EditText paymentDescription;
    private ImageButton contactImage;
    private StormServiceAsyncClient stormServiceClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("SendCashActivity", "onCreate");
        setContentView(R.layout.activity_send_cash);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        stormServiceClient = StormServiceAsyncClient.create(this);
        initializeViews();
        initializeWalletView();
        initializeMoneyManager();
        initializeContactView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        initializeContactView();
    }

    private void initializeViews(){
        coins[0] = (ImageView) findViewById(R.id.left_coin);
        coins[1] = (ImageView) findViewById(R.id.center_coin);
        coins[2] = (ImageView) findViewById(R.id.right_coin);
        parentView = (RelativeLayout) findViewById(R.id.send_cash_relative_view);
        frontWalletRelativeLayout = (RelativeLayout) findViewById(R.id.relative_layout_front_wallet);
        frontWalletImageView = (ImageView) findViewById(R.id.wallet_front);
        backWalletImageView = (ImageView) findViewById(R.id.wallet_back);
        verticalCoinFlinger = (ScrollView) findViewById(R.id.vertical_coin_flinger);
        horizontalCoinScroller = (HorizontalScrollView) findViewById(R.id.horizontal_coin_scroller);
        cashCloud = (ImageButton) findViewById(R.id.image_storm_cloud);
        contactImage = (ImageButton) findViewById(R.id.image_contact);
        paymentDescription = (EditText) findViewById(R.id.edit_text_pay_description_cloud);
    }

    private void initializeWalletView() {
        initializeWalletImage(backWalletImageView, R.drawable.wallet_half_back);
        initializeWalletImage(frontWalletImageView, R.drawable.wallet_half_front);
    }

    private void initializeContactView() {
        stormServiceClient.retrieveDefaultContact(new DefaultContactCallback(this));
    }

    private void initializeWalletImage(final ImageView walletImage, final int walletResId){
        final ViewTreeObserver walletViewTreeObserver = walletImage.getViewTreeObserver();
        walletViewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                walletImage.getViewTreeObserver().removeOnPreDrawListener(this);
                final int height = walletImage.getMeasuredHeight();
                final int width = walletImage.getWidth();
                int size = (int) Math.ceil(Math.sqrt(width * height));
                Log.d("SendCashActivity", "walletStats: " + height + ":" + width);
                Picasso.with(SendCashActivity.this)
                        .load(walletResId)
                        .transform(new ScaledBitmapTransform(width, height))
                        .resize(size, size)
                        .centerInside()
                        .noFade()
                        .into(walletImage);
                return true;
            }
        });
    }

    private void initializeMoneyManager() {
        parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                parentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                final int screenWidth = parentView.getWidth();
                final int screenHeight = parentView.getHeight();
                Log.d("SendCashActivity", String.format("onCreate:screenWidth:%d", screenWidth));
                Log.d("SendCashActivity", String.format("onCreate:screenHeight:%d", screenHeight));
                final int halfWidth = ((ViewGroup.MarginLayoutParams) coins[0].getLayoutParams()).width / 2;

                moneyViewManager = new MoneyViewManager(
                        SendCashActivity.this,
                        parentView,
                        frontWalletRelativeLayout,
                        frontWalletImageView,
                        screenWidth,
                        halfWidth,
                        screenHeight,
                        coins,
                        cashCloud,
                        0);
                //Must be initialized here
                moneyViewManager.initializeCoinViews();

                createFlingers();
            }
        });
    }

    private void createFlingers() {
        instantiateFlinger(new HorizontalCoinGestureDetector(moneyViewManager), horizontalCoinScroller);
        instantiateFlinger(new VerticalCoinGestureDetector(moneyViewManager), verticalCoinFlinger);
    }

    private void instantiateFlinger(final GestureToucher gestureToucher, final FrameLayout frameLayout) {
        final GestureDetectorCompat detector = new GestureDetectorCompat(SendCashActivity.this, gestureToucher);
        final CoinTouchListener touchListener = new CoinTouchListener(detector, gestureToucher);
        frameLayout.setOnTouchListener(touchListener);
    }

    private class ScaledBitmapTransform implements Transformation {
        private final int maxWidth;
        private final int maxHeight;

        public ScaledBitmapTransform(int maxWidth, int maxHeight) {
            this.maxWidth = maxWidth;
            this.maxHeight = maxHeight;
        }

        @Override
        public Bitmap transform(final Bitmap source) {
            final Bitmap result = Bitmap.createScaledBitmap(source, maxWidth, maxHeight, false);
            if (!Objects.equals(result, source)) {
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return maxWidth + "x" + maxHeight;
        }
    }

    @RequiredArgsConstructor(suppressConstructorProperties = true)
    private class DefaultContactCallback implements FutureCallback<StormContact> {
        private final Activity activity;

        @Override
        public void onSuccess(final StormContact stormContact) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(SendCashActivity.this)
                            .load(stormContact.getAvatarLink())
                            .transform(new CircleTransform())
                            .into(contactImage);
                    contactImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Intent intent = new Intent(activity, ContactsActivity.class);
                            activity.startActivityForResult(intent, 1);
                        }
                    });
                }
            });
        }

        @Override
        public void onFailure(final Throwable throwable) {
            Log.e("DefaultContactCallback", "onFailure", throwable);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //TODO Display error dialog
                    activity.finish();
                }
            });
        }
    }
}
