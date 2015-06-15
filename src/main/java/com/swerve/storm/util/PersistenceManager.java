package com.swerve.storm.util;

import android.content.Context;
import com.swerve.storm.model.Model;

import java.io.IOException;

public class PersistenceManager {
    private final PreferenceStore mPreferenceStore;
    private final ModelSerializer mModelSerializer;

    public PersistenceManager(final Context context) {
        mPreferenceStore = new AndroidPreferenceStore(context);
        mModelSerializer = new ModelSerializer();
    }

    public <T> T get(final String key, final Class<T> classOfT) throws IOException {
        final String modelSerialized = mPreferenceStore.get(key);
        if (modelSerialized == null)
        {
            throw new IOException(null, null);
        }
        return mModelSerializer.deserialize(modelSerialized, classOfT);
    }

    public void put (final String key, final Model model) {
        final String modelSerialized = mModelSerializer.serialize(model);
        mPreferenceStore.put(key, modelSerialized);
    }
}
