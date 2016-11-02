package com.scripthelper;

import android.app.Application;
import android.content.Context;

import script.helper.uiauto.UiAuto;


public class MyApplication extends Application {
    public static Context context;
    public static UiAuto auto;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        auto=new UiAuto(this);
    }
}