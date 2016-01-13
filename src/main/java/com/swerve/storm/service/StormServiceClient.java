package com.swerve.storm.service;

import android.content.Context;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.swerve.storm.model.Credentials;
import com.swerve.storm.model.StormContact;
import com.swerve.storm.model.StormContacts;
import com.swerve.storm.util.ModelSerializer;
import com.swerve.storm.util.storage.PersistenceManager;
import com.swerve.storm.util.storage.StormPersistenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(suppressConstructorProperties = true)
public class StormServiceClient {
    private final OkHttpClient mOkHttpClient;
    private final ModelSerializer mModelSerializer;
    private final StormPersistenceManager persistenceManager;

    public static StormServiceClient create(final Context context) {
        return new StormServiceClient(
                new OkHttpClient(),
                new ModelSerializer(),
                new StormPersistenceManager(new PersistenceManager(context)));
    }

    public Credentials authorize(final String url) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            final Response response = mOkHttpClient.newCall(request).execute();
            if (response.code() != 200) {
                throw new RuntimeException("There was a problem with the http client.");
            }
            final Credentials credentials = mModelSerializer.deserialize(response.body().string(), Credentials.class);
            return credentials;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public StormContact getDefaultContact(){
        final Optional<StormContact> stormContactOptional = persistenceManager.getDefaultContact();
        if (stormContactOptional.isPresent()) {
            return stormContactOptional.get();
        }
        final StormContacts contacts = retrieveContacts(CachePolicy.CACHE_THEN_SERVER);
        if (contacts.getContacts().isEmpty()) {
            throw new RuntimeException("This user has no contacts!");
        }
        final StormContact contact = contacts.getContacts().get(0);
        persistenceManager.saveDefaultContact(contact);
        return contact;
    }

    public StormContacts retrieveContacts(final CachePolicy cachePolicy) {
        if (cachePolicy.equals(CachePolicy.CACHE_THEN_SERVER) && persistenceManager.hasCachedContacts()) {
            final Optional<StormContacts> stormContacts = persistenceManager.getStormContacts();
            if (stormContacts.isPresent()) {
                return stormContacts.get();
            }
        }
        //TODO: insert server call here
        final StormContacts stormContacts = new StormContacts(ImmutableList.of(
                new StormContact(
                        "Pierce Johnson",
                        "http://infamouspr.com/infamouspr/wp-content/gallery/eric-prydz/prydz-eric-5062c957eecd4.jpg"),
                new StormContact(
                        "Ryan Riebling",
                        "https://i.ytimg.com/vi/1GYTk-TBe4g/maxresdefault.jpg"),
                new StormContact(
                        "Zack Pakja",
                        "https://i.ytimg.com/vi/1GYTk-TBe4g/maxresdefault.jpg"),
                new StormContact(
                        "Eric Prydz",
                        "https://i.ytimg.com/vi/1GYTk-TBe4g/maxresdefault.jpg"),
                new StormContact(
                        "Cirez D",
                        "https://i.ytimg.com/vi/1GYTk-TBe4g/maxresdefault.jpg"),
                new StormContact(
                        "Pryda",
                        "https://i.ytimg.com/vi/1GYTk-TBe4g/maxresdefault.jpg"),
                new StormContact(
                        "Zack Pakja",
                        "https://i.ytimg.com/vi/1GYTk-TBe4g/maxresdefault.jpg"),
                new StormContact(
                        "Eric Prydz",
                        "https://i.ytimg.com/vi/1GYTk-TBe4g/maxresdefault.jpg"),
                new StormContact(
                        "Cirez D",
                        "https://i.ytimg.com/vi/1GYTk-TBe4g/maxresdefault.jpg"),
                new StormContact(
                        "Pryda",
                        "https://i.ytimg.com/vi/1GYTk-TBe4g/maxresdefault.jpg")
        ), System.currentTimeMillis());
        persistenceManager.saveContacts(stormContacts);
        return stormContacts;
    }
}
