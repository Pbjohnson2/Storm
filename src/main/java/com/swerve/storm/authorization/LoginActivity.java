package com.swerve.storm.authorization;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.swerve.storm.R;
import com.swerve.storm.mainmenu.MainMenuActivity;

public class LoginActivity extends Activity {
    private Button mContinueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContinueButton = (Button) findViewById(R.id.button_continue);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMainMenu();
            }
        });
    }

    private void navigateToMainMenu() {
        final Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }
}