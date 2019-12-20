package com.agadimi.RetroMocksonTestApp;

import android.app.Application;

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
