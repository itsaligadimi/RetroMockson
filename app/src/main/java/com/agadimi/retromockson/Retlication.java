package com.agadimi.retromockson;

import android.app.Application;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

// retrofit + application ===> retlication :D
public class Retlication extends Application
{
    private static Retrofit retrofit;

    @Override
    public void onCreate()
    {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.instagram.com")
                .build();
    }

    public static InstagramService getInstagramService()
    {
        return retrofit.create(InstagramService.class);
    }
}
