package com.swerve.storm.util.storage;

import android.content.Context;
import com.swerve.storm.model.Model;
import com.swerve.storm.util.ModelSerializer;

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

    public void put (final String key, final Object object) {
        final String modelSerialized = mModelSerializer.serialize(object);
        mPreferenceStore.put(key, modelSerialized);
    }

    public boolean hasKey (final String key) {
        return mPreferenceStore.hasKey(key);
    }
}
