package com.swerve.storm.service;

import android.content.Context;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.swerve.storm.model.StormContact;
import com.swerve.storm.model.StormContacts;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(suppressConstructorProperties = true)
public class StormServiceAsyncClient {
    private final ListeningExecutorService executor;
    private final StormServiceClient stormServiceClient;

    public static StormServiceAsyncClient create(final Context context){
        return new StormServiceAsyncClient(
                MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10)),
                StormServiceClient.create(context));
    }

    public void retrieveDefaultContact(final FutureCallback<StormContact> defaultContactCallback) {
        addCallback(
                defaultContactCallback,
                new Callable<StormContact>() {
                    @Override
                    public StormContact call() throws Exception {
                        return stormServiceClient.getDefaultContact();
                    }
                });
    }

    public void retrieveContacts(final FutureCallback<StormContacts> stormContactsCallback, final CachePolicy cachePolicy) {
        addCallback(
                stormContactsCallback,
                new Callable<StormContacts>() {
                    @Override
                    public StormContacts call() throws Exception {
                        return stormServiceClient.retrieveContacts(cachePolicy);
                    }
                });
    }

    private <T> void addCallback (final FutureCallback<T> callback, final Callable<T> callable) {
        final ListenableFuture<T> listenableFuture = executor.submit(callable);
        Futures.addCallback(listenableFuture, callback);
    }
}
