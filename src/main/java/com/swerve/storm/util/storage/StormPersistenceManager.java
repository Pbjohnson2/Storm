package com.swerve.storm.util.storage;

import android.util.Log;

import com.google.common.base.Optional;
import com.swerve.storm.model.Credentials;
import com.swerve.storm.model.StormContact;
import com.swerve.storm.model.StormContacts;

import java.io.IOException;

import lombok.AllArgsConstructor;

@AllArgsConstructor(suppressConstructorProperties=true)
public class StormPersistenceManager {
    private static final String CREDENTIALS_KEY = "storm_credentials";
    private static final String CONTACTS_KEY = "storm_contacts";
    private static final String DEFAULT_CONTACT_KEY = "storm_default_contact";

    private final PersistenceManager mPersistenceManager;

    /**
     * Credential management
     */
    public boolean isUserCached() {
        return mPersistenceManager.hasKey(CREDENTIALS_KEY);
    }

    public Optional<Credentials> getCredentials() {
        return retrieveKey(CREDENTIALS_KEY, Credentials.class);
    }

    public void saveCredentials(final Credentials credentials) {
        mPersistenceManager.put(CREDENTIALS_KEY, credentials);
    }

    /**
     * Contacts management
     */
    public boolean hasCachedContacts() {
        return mPersistenceManager.hasKey(DEFAULT_CONTACT_KEY);
    }

    public Optional<StormContacts> getStormContacts() {
        return retrieveKey(CONTACTS_KEY, StormContacts.class);
    }

    public void saveContacts(final StormContacts contacts) {
        mPersistenceManager.put(CONTACTS_KEY, contacts);
    }

    /**
     * Default contacts management
     */
    public void saveDefaultContact(final StormContact stormContact) {
        mPersistenceManager.put(DEFAULT_CONTACT_KEY, stormContact);

    }

    public Optional<StormContact> getDefaultContact() {
        return retrieveKey(DEFAULT_CONTACT_KEY, StormContact.class);
    }

    private <T> Optional<T> retrieveKey(final String key, final Class<T> classOfT) {
        try {
            return Optional.fromNullable(mPersistenceManager.get(key, classOfT));
        } catch (IOException e) {
            Log.d("StormPersistenceManager", "Unable to retrieve credentials");
        }
        return Optional.absent();
    }
}
