package com.example.commons.app;

import android.content.Context;

import com.example.commons.utils.SharePrefUtils;

/**
 * Created by 阿龙 on 2017/2/27.
 */

public class Application extends android.app.Application {

    private static Application myApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        SharePrefUtils.getInstance(getApplicationContext());//初始化SharedPreferences
    }
    /**
     * 获取application
     *
     * @return
     */
    public static Context getApplication() {
        return myApplication;
    }
}
