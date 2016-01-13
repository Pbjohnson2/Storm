package com.swerve.storm.authorization;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.squareup.okhttp.OkHttpClient;
import com.swerve.storm.R;
import com.swerve.storm.service.StormServiceClient;
import com.swerve.storm.mainmenu.MainMenuActivity;
import com.swerve.storm.model.Credentials;
import com.swerve.storm.util.ModelSerializer;
import com.swerve.storm.util.storage.PersistenceManager;
import com.swerve.storm.util.storage.StormPersistenceManager;

public class RegistrationActivity extends Activity {
    private static final String REDIRECT_URL = "https://ucuyectfop.localtunnel.me/auth/venmo/callback";
    private static final String AUTH_URL = "https://api.venmo.com/v1/oauth/authorize?client_id=2858&scope=make_payments%20access_payment_history%20access_profile%20access_friends%20access_phone%20access_balance&response_type=code";

    private StormServiceClient mStormServiceClient;
    private StormPersistenceManager mStormPersistenceManager;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        WebView.setWebContentsDebuggingEnabled(true);
        mStormServiceClient = StormServiceClient.create(this);
        mStormPersistenceManager = new StormPersistenceManager(new PersistenceManager(this));
        mWebView = (WebView) findViewById(R.id.webview_registration);
        mWebView.setWebViewClient(new RegistrationWebViewClient(mStormServiceClient, mStormPersistenceManager, RegistrationActivity.this));
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(AUTH_URL);
    }

    private static class RegistrationWebViewClient extends WebViewClient {
        private final StormServiceClient stormServiceClient;
        private final StormPersistenceManager stormPersistenceManager;
        private final Activity activity;

        public RegistrationWebViewClient(final StormServiceClient stormServiceClient, final StormPersistenceManager stormPersistenceManager, final Activity activity) {
            this.stormServiceClient = stormServiceClient;
            this.stormPersistenceManager = stormPersistenceManager;
            this.activity = activity;
        }

        @Override
        public void onPageFinished(final WebView view, final String url) {
            view.setVisibility(View.VISIBLE);
        }
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            if (url.startsWith(REDIRECT_URL)) {
                new Thread() {
                    public void run() {
                        final Credentials credentials = stormServiceClient.authorize(url);
                        Log.d("Credentials", "Here are the credentials: " + credentials.toString());
                        stormPersistenceManager.saveCredentials(credentials);
                        navigateToMainMenuActivity();
                    }
                }.start();
                return true;
            }
            return false;
        }

        private void navigateToMainMenuActivity() {
            final Intent intent = new Intent(activity, MainMenuActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }
}
