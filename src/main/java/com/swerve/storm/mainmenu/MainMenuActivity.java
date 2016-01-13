package com.swerve.storm.mainmenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.swerve.storm.R;
import com.swerve.storm.contacts.ContactsActivity;

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
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void navigateToRetrieveCashActivity() {
        final Intent intent = new Intent(MainMenuActivity.this, ContactsActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void navigateToSettingsActivity() {
        final Intent intent = new Intent(MainMenuActivity.this, SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
