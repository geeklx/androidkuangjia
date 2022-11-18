package com.geek.appcommon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.BarUtils;
import com.geek.libbase.base.SlbBaseActivity;
import com.geek.libutils.SlbLoginUtil;
import com.geek.libutils.app.LocalBroadcastManagers;

import me.jessyan.autosize.AutoSizeCompat;

public abstract class SlbBase extends SlbBaseActivity {

    public int theme;
    public static final String KEY = "current_theme";
    public static final String TAG_TOKEN_ACTION = "TAG_TOKEN_ACTION";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        BarUtils.setStatusBarLightMode(this, false);
    }

    @Override
    protected void setup(@Nullable Bundle savedInstanceState) {
        super.setup(savedInstanceState);

    }

//    @Override
//    public Resources getResources() {
//        //需要升级到 v1.1.2 及以上版本才能使用 AutoSizeCompat
//        if (Looper.myLooper() == Looper.getMainLooper()) {
//            AutoSizeCompat.autoConvertDensityOfGlobal((super.getResources()));//如果没有自定义需求用这个方法
//            AutoSizeCompat.autoConvertDensity((super.getResources()), 667, false);//如果有自定义需求就用这个方法
//        }
//        return super.getResources();
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY, theme);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
