package com.geek.appcommon;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.BarUtils;
import com.geek.libbase.base.SlbBaseActivity;
import com.geek.libbase.netstate.NetconListener;

public abstract class SlbBase extends SlbBaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        BarUtils.setStatusBarLightMode(this, false);
//        netState.setNetStateListener(new NetconListener() {
//            @Override
//            public void net_con_none() {
//
//            }
//
//            @Override
//            public void net_con_success() {
//
//            }
//
//            @Override
//            public void showNetPopup() {
//
//            }
//        }, this);
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
