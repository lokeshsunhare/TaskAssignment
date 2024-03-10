package com.example.taskproject.api;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApI {

    private final static ReentrantLock lock = new ReentrantLock();
    private static RestApI mInstance;

    private RestApI() {
    }

    public static RestApI getInstance() {
        // Critical section of code
        // Only one thread can execute this block at a time
        if (mInstance == null) {
            lock.lock();
            try {
                synchronized (RestApI.class) {
                    if (mInstance == null) {
                        mInstance = new RestApI();
                    }
                }
            } finally {
                lock.unlock();
            }
        }
        return mInstance;
    }

    public NetworkService getRetrofitApi() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.MINUTES) // connect timeout
                .readTimeout(10, TimeUnit.MINUTES) // read timeout
                .writeTimeout(10, TimeUnit.MINUTES) // write timeout
//                .certificatePinner(certificatePinner)
                .build();

        // OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        NetworkService api = retrofit.create(NetworkService.class);
        return api;
    }

}
