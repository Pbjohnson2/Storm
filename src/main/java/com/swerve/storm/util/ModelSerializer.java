package com.swerve.storm.util;

import com.google.gson.Gson;

public class ModelSerializer {
    private static final Gson GSON = new Gson();

    public String serialize(final Object object) {
            return GSON.toJson(object);
    }

    public <T> T deserialize(String json, Class<T> classOfT){
        return GSON.fromJson(json, classOfT);
    }
}
