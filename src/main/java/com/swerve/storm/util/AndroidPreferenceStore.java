package com.swerve.storm.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.squareup.picasso.Picasso;

public class AndroidPreferenceStore implements PreferenceStore{
    private static final String PREFERENCE_KEY_FILE = "vendi_shared_prefs";
    private final SharedPreferences mSharedPreferences;

    public AndroidPreferenceStore(final Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFERENCE_KEY_FILE, Context.MODE_PRIVATE);
    }

    @Override
    public void put (final String key, final String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    @Override
    public String get (final String key) {
        return mSharedPreferences.getString(key, "");
    }

    @Override
    public boolean hasKey(final String key) {
        final String value = mSharedPreferences.getString(key, null);
        if(value == null) {
            return false;
        }
        return true;
    }
}
