package com.document.officereader;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.amazic.ads.util.AdsApplication;
import com.amazic.ads.util.AppOpenManager;
import com.document.officereader.activity.SplashScreenActivity;

import java.util.List;

public class MyApplication extends AdsApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            builder.detectFileUriExposure();
            StrictMode.setVmPolicy(builder.build());
        }
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashScreenActivity.class);

    }

    @Override
    public boolean enableAdsResume() {
        return true;
    }

    @Override
    public List<String> getListTestDeviceId() {
        return null;
    }

    @Override
    public String getResumeAdId() {
        return getString(R.string.appopen_resume);
    }
}
