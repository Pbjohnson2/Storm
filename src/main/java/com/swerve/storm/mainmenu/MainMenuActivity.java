package com.swerve.storm.mainmenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.swerve.storm.R;

public class MainMenuActivity extends Activity {
    private ImageButton mSendCashButton;
    private ImageButton mCashCloudButton;
    private ImageButton mSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mSendCashButton = (ImageButton) findViewById(R.id.button_send_cash);
        mCashCloudButton = (ImageButton) findViewById(R.id.button_cash_cloud);
        mSettingsButton = (ImageButton) findViewById(R.id.button_settings);

        mSendCashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSendCashActivity();
            }
        });

        mCashCloudButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRetrieveCashActivity();
            }
        });

        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSettingsActivity();
            }
        });
    }

    private void navigateToSendCashActivity() {
        final Intent intent = new Intent(MainMenuActivity.this, SendCashActivity.class);
        startActivity(intent);
    }

    private void navigateToRetrieveCashActivity() {
        final Intent intent = new Intent(MainMenuActivity.this, RetrieveCashActivity.class);
        startActivity(intent);
    }

    private void navigateToSettingsActivity() {
        final Intent intent = new Intent(MainMenuActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
}
