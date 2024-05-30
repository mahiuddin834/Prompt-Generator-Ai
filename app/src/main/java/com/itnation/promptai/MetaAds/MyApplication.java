package com.itnation.promptai.MetaAds;

import android.app.Application;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        AudienceNetworkAds.initialize(this);
        AdSettings.addTestDevice("d28cb4fb-00d8-495e-b3e6-8778847d5f36");

    }

}