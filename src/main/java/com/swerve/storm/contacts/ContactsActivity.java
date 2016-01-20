package com.swerve.storm.contacts;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import com.google.common.util.concurrent.FutureCallback;
import com.swerve.storm.R;
import com.swerve.storm.model.StormContacts;
import com.swerve.storm.service.CachePolicy;
import com.swerve.storm.service.StormServiceAsyncClient;
import com.swerve.storm.util.view.lists.ContactsAdapter;
import lombok.RequiredArgsConstructor;

public class ContactsActivity extends Activity {

    private ListView contactsListView;
    private StormServiceAsyncClient asyncClient;
    private EditText searchEditText;
    private SwipeRefreshLayout contactsRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        searchEditText = (EditText) findViewById(R.id.search_bar);
        contactsListView = (ListView) findViewById(R.id.list_view_contacts);
        contactsRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        asyncClient = StormServiceAsyncClient.create(this);
        asyncClient.retrieveContacts(new ContactsFutureCallback(this, contactsListView), CachePolicy.SERVER);
    }

    @RequiredArgsConstructor(suppressConstructorProperties = true)
    private static class ContactsFutureCallback implements FutureCallback<StormContacts> {

        private final Activity activity;
        private final ListView contactsListView;

        @Override
        public void onSuccess(final StormContacts stormContacts) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    contactsListView.setAdapter(new ContactsAdapter(stormContacts.getContacts(), activity));
                }
            });
        }

        @Override
        public void onFailure(Throwable throwable) {
            Log.e("ContactsFutureCallback", "onFailure", throwable);
        }
    }
}
