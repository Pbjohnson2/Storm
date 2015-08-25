package com.swerve.storm.authorization;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.swerve.storm.R;

public class RegistrationActivity extends Activity {
    private Button mContinueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mContinueButton = (Button) findViewById(R.id.button_continue);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToPaymentRegistrationActivity();
            }
        });
    }

    private void navigateToPaymentRegistrationActivity() {
        final Intent intent = new Intent(RegistrationActivity.this, PaymentRegistrationActivity.class);
        startActivity(intent);
    }
}
