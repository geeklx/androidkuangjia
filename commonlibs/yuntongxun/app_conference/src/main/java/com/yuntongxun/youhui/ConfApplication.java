package com.yuntongxun.youhui;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;
import androidx.multidex.MultiDex;

import com.tencent.bugly.crashreport.CrashReport;
import com.yuntongxun.confwrap.WrapManager;
import com.yuntongxun.youhui.common.handler.CrashHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by WJ on 2016/10/18.
 */

public class ConfApplication extends Application  {
    private static final String TAG = ConfApplication.class.getSimpleName();

    private static ConfApplication instance;

    public static ConfApplication getInstance() {
        return instance;
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        instance = this;
        initBugly();
        WrapManager.getInstance().app_init(this);
    }

    /**
     * 初始化Bugly
     */
    private void initBugly() {
        CrashHandler.getInstance().init(this);
        //bugly 初始化
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(context, "a994461ce3", true, strategy);
    }



    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
