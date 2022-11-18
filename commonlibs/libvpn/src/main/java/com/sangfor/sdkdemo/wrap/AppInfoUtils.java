package com.sangfor.sdkdemo.wrap;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * 获取app信息的公共类
 */
public class AppInfoUtils {

    private static String TAG = "AppInfoUtils";

    /**
     * 获取当前应用名称
     *
     * @param activity
     * @return
     */
    public static String getApplicationName(Activity activity) {
        PackageManager packageManager;
        ApplicationInfo applicationInfo;
        try {
            packageManager = activity.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(activity.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.i(TAG,"PackageManager.NameNotFoundException");
            return "";
        }
        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        if (TextUtils.isEmpty(applicationName)) {
            Log.i(TAG,"applicationName is Empty");
            return "";
        } else {
            return applicationName;
        }
    }

    /**
     * 获取当前应用名称
     *
     * @param activity
     * @return
     */
    public static String getApplicationName(Activity activity, String packAgeName) {
        PackageManager packageManager;
        ApplicationInfo applicationInfo;
        try {
            packageManager = activity.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(packAgeName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.i(TAG,"PackageManager.NameNotFoundException");
            return "";
        }

        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        if (TextUtils.isEmpty(applicationName)) {
            Log.i(TAG,"applicationName is Empty");
            return "";
        } else {
            return applicationName;
        }
    }

}
