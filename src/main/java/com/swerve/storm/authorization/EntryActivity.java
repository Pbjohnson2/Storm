package com.swerve.storm.authorization;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.swerve.storm.R;

public class EntryActivity extends Activity {
    private ImageButton mLoginButton;
    private ImageButton mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        mLoginButton = (ImageButton) findViewById(R.id.button_login);
        mRegisterButton = (ImageButton) findViewById(R.id.button_register);


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLoginActivity();
            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegisterActivity();
            }
        });
    }

    private void navigateToLoginActivity() {
        final Intent intent = new Intent(EntryActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void navigateToRegisterActivity() {
        final Intent intent = new Intent(EntryActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }
}
