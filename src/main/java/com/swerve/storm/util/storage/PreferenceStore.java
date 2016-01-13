package com.swerve.storm.util.storage;

public interface PreferenceStore {
    void put(final String key, final String value);
    String get(final String key);
    boolean hasKey(final String key);
}
