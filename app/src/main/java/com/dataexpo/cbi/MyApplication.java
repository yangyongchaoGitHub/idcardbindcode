package com.dataexpo.cbi;

import android.app.Application;
import android.content.Context;

import com.dataexpo.cbi.retrofitInf.URLs;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.Executors;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class MyApplication extends Application {

    private static Context context;
    private static Retrofit mRetrofit;
    private static MyApplication myApp;

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        //currentDeviceName = (String) MUtil.getInstance().getSystemProp(MConstant.DeviceCode);//获取设备编号
        context = getApplicationContext();
        createRetrofit();
    }
    public static MyApplication getMyApp() {
        return myApp;
    }


    public static Context getContext() {
        return context;
    }

    public static Retrofit getmRetrofit() {
        return mRetrofit;
    }

    public static void createRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(URLs.baseUrl)
                .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper()))
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .build();
    }
}
