package com.swerve.storm.authorization;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.swerve.storm.R;
import com.swerve.storm.mainmenu.MainMenuActivity;
import com.swerve.storm.util.storage.PersistenceManager;
import com.swerve.storm.util.storage.StormPersistenceManager;

public class EntryActivity extends Activity {
    private StormPersistenceManager mStormPersistenceManager;
    private ImageButton mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        mRegisterButton = (ImageButton) findViewById(R.id.button_register);

        mStormPersistenceManager = new StormPersistenceManager(new PersistenceManager(this));

        if (mStormPersistenceManager.isUserCached()) {
            navigateToMainMenuActivity();
            finish();
        }

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegisterActivity();
            }
        });
    }

    private void navigateToRegisterActivity() {
        final Intent intent = new Intent(EntryActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    private void navigateToMainMenuActivity() {
        final Intent intent = new Intent(EntryActivity.this, MainMenuActivity.class);
        startActivity(intent);
    }
}
