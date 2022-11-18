package com.sangfor.sdkdemo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.sangfor.sdk.SFUemSDK;
import com.sangfor.sdk.base.SFBaseMessage;
import com.sangfor.sdk.base.SFConstants;
import com.sangfor.sdk.base.SFLaunchReason;
import com.sangfor.sdk.base.SFLogoutListener;
import com.sangfor.sdk.base.SFLogoutType;
import com.sangfor.sdk.base.SFLaunchReason;
import com.sangfor.sdk.base.SFSDKFlags;
import com.sangfor.sdk.base.SFSDKMode;
import com.sangfor.sdk.base.SFSDKOptions;
import com.sangfor.sdk.entry.SFLaunchEntry;
import com.sangfor.sdk.entry.SFLaunchInfo;
import com.sangfor.sdk.utils.IGeneral;
import com.sangfor.sdk.utils.SFLogN;
import com.sangfor.sdkdemo.primary.GlobalListenerManager;
import com.sangfor.sdkdemo.view.AuthSuccessActivity;
import com.sangfor.sdkdemo.wrap.SubAppManager;

import org.json.JSONObject;

import java.util.Locale;

public class SFApplication extends Application {
    private static final String TAG = "SFApplication";

    private static SFSDKMode mSDKMode;
    private static Context sContext = null;

    public static Context getContext() {
        return sContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sContext = base;

        /**
         * 初始化sdk,推荐在attachBaseContext中调用，因为sdk延后初始化会导致多进程场景下，子进程无法拥有sdk的能力
         */
        initSdk(base);

        /**
         * 初始化注销监听回调
         */
        GlobalListenerManager.getInstance().init(base);
    }

    void initSdk(Context context) {
        SFSDKMode sdkMode = SFSDKMode.MODE_VPN_SANDBOX;                 // 表明同时启用VPN+沙箱功能,详情参考集成指导文档
        switch (sdkMode) {
            case MODE_VPN: {    //只使用VPN功能场景
                int sdkFlags =  SFSDKFlags.FLAGS_HOST_APPLICATION;      //表明是单应用或者是主应用
                sdkFlags |= SFSDKFlags.FLAGS_VPN_MODE_TCP;              //表明使用VPN功能中的TCP模式
                SFUemSDK.getInstance().initSDK(this, sdkMode,sdkFlags,null);
                break;
            }
            case MODE_SANDBOX: {   //只使用安全沙箱功能场景
                int sdkFlags =  SFSDKFlags.FLAGS_HOST_APPLICATION;      //表明是单应用或者是主应用
                SFUemSDK.getInstance().initSDK(this, sdkMode,sdkFlags,null);
                break;
            }
            case MODE_VPN_SANDBOX: {    //同时使用VPN功能+安全沙箱功能场景
                int sdkFlags =  SFSDKFlags.FLAGS_HOST_APPLICATION;      //表明是单应用或者是主应用
                sdkFlags |= SFSDKFlags.FLAGS_VPN_MODE_TCP;              //表明使用VPN功能中的TCP模式
                SFUemSDK.getInstance().initSDK(this, sdkMode,sdkFlags,null);
                break;
            }
            default: {
                Toast.makeText(context, "SDK模式错误", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

}
