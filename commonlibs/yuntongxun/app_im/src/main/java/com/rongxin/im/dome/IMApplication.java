package com.rongxin.im.dome;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.rongxin.im.dome.crash.CrashHandler;
import com.yuntongxun.confwrap.WrapManager;

/**
 * Created by erica on 2016/12/22.
 */

public class IMApplication extends MultiDexApplication {
    private static final String TAG = IMApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        MultiDex.install(this);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        super.onCreate();
        WrapManager.getInstance().app_init(this);
    }

}
