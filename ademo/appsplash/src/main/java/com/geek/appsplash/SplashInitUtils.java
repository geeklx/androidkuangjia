package com.geek.appsplash;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.Utils;
import com.geek.appcommon.service.MOBIDservices;
import com.geek.libutils.app.BaseApp;
import com.mob.MobSDK;
import com.pgyer.pgyersdk.PgyerSDKManager;
import com.pgyer.pgyersdk.pgyerenum.Features;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMConversationListener;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSDKListener;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.demo.thirdpush.HUAWEIHmsMessageService;
import com.tencent.qcloud.tim.demo.utils.DemoLog;
import com.tencent.qcloud.tim.demo.utils.TUIUtils;

public class SplashInitUtils {
    private static volatile SplashInitUtils instance;
    private Context mContext;

    public SplashInitUtils(Context context) {
        mContext = context;
    }

    public static SplashInitUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (SplashInitUtils.class) {
                instance = new SplashInitUtils(context);
            }
        }
        return instance;
    }

    /**
     * 需要隐私协议初始化的bufen
     */
    public void init() {
        //
        Utils.init(BaseApp.get());
        com.blankj.utilcode.util.AppUtils.registerAppStatusChangedListener(new Utils.OnAppStatusChangedListener() {
            @Override
            public void onForeground(Activity activity) {
                if (!ServiceUtils.isServiceRunning(MOBIDservices.class)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        BaseApp.get().startForegroundService(new Intent(BaseApp.get(), MOBIDservices.class));
                    } else {
                        BaseApp.get().startService(new Intent(BaseApp.get(), MOBIDservices.class));
                    }
                }
            }

            @Override
            public void onBackground(Activity activity) {

            }
        });
        //
        new PgyerSDKManager.Init().setContext(BaseApp.get()) //设置上下问对象
                .setApiKey("8bd21fed5150eab87567cbcfb6ed8a46").setFrontJSToken("d68155b81756a82d2324fe46bff24c45").enable(Features.CHECK_UPDATE).start();
        //
        MobSDK.init(BaseApp.get());
        MobSDK.submitPolicyGrantResult(true);
        //
        initTencentIMNew();
    }

    private void initTencentIMNew() {
        TUIUtils.init(BaseApp.get(), 1400598372, null, null);
//        HeytapPushManager.init(this, true);
//        if (BrandUtil.isBrandXiaoMi()) {
//            // 小米离线推送
//            MiPushClient.registerPush(this, PrivateConstants.XM_PUSH_APPID, PrivateConstants.XM_PUSH_APPKEY);
//        } else if (BrandUtil.isBrandHuawei()) {
//            // 华为离线推送，设置是否接收Push通知栏消息调用示例
//            HmsMessaging.getInstance(this).turnOnPush().addOnCompleteListener(new com.huawei.hmf.tasks.OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(com.huawei.hmf.tasks.Task<Void> task) {
//                    if (task.isSuccessful()) {
//                        DemoLog.e("TencentIM", "huawei turnOnPush Complete");
//                    } else {
//                        DemoLog.e(TAG, "huawei turnOnPush failed: ret=" + task.getException().getMessage());
//                    }
//                }
//            });
//        } else if (BrandUtil.isBrandVivo()) {
//            // vivo离线推送
//            PushClient.getInstance(getApplicationContext()).initialize();
//        } else if (HeytapPushManager.isSupportPush()) {
//            // oppo离线推送，因为需要登录成功后向我们后台设置token，所以注册放在MainActivity中做
//        }
        //
        BaseApp.get().registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            private int foregroundActivities = 0;
            private boolean isChangingConfiguration;

            private final V2TIMConversationListener unreadListener = new V2TIMConversationListener() {
                @Override
                public void onTotalUnreadMessageCountChanged(long totalUnreadCount) {
                    HUAWEIHmsMessageService.updateBadge(BaseApp.get(), (int) totalUnreadCount);
                }
            };

            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                DemoLog.e("TencentIM", "onActivityCreated bundle: " + bundle);
                if (bundle != null) { // 若bundle不为空则程序异常结束
                    // 重启整个程序
//                Intent intent = new Intent(activity, SplashActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                foregroundActivities++;
                if (foregroundActivities == 1 && !isChangingConfiguration) {
                    // 应用切到前台
                    DemoLog.e("TencentIM", "application enter foreground");
                    V2TIMManager.getOfflinePushManager().doForeground(new V2TIMCallback() {
                        @Override
                        public void onError(int code, String desc) {
                            DemoLog.e("TAG", "doForeground err = " + code + ", desc = " + desc);
                        }

                        @Override
                        public void onSuccess() {
                            DemoLog.e("TencentIM", "doForeground success");
                        }
                    });

                    V2TIMManager.getConversationManager().removeConversationListener(unreadListener);
                }
                isChangingConfiguration = false;
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                foregroundActivities--;
                if (foregroundActivities == 0) {
                    // 应用切到后台
                    DemoLog.e("TencentIM", "application enter background");
                    V2TIMManager.getConversationManager().getTotalUnreadMessageCount(new V2TIMValueCallback<Long>() {
                        @Override
                        public void onSuccess(Long aLong) {
                            int totalCount = aLong.intValue();
                            V2TIMManager.getOfflinePushManager().doBackground(totalCount, new V2TIMCallback() {
                                @Override
                                public void onError(int code, String desc) {
                                    DemoLog.e("TAG", "doBackground err = " + code + ", desc = " + desc);
                                }

                                @Override
                                public void onSuccess() {
                                    DemoLog.e("TencentIM", "doBackground success");
                                }
                            });
                        }

                        @Override
                        public void onError(int code, String desc) {

                        }
                    });

                    V2TIMManager.getConversationManager().addConversationListener(unreadListener);
                }
                isChangingConfiguration = activity.isChangingConfigurations();
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
        V2TIMManager.getInstance().addIMSDKListener(new V2TIMSDKListener() {
            @Override
            public void onKickedOffline() {
//            ToastUtil.toastLongMessage(YewuApplicationAndroid.instance().getString(com.tencent.qcloud.tim.demo.R.string.repeat_login_tip));
                DemoLog.e("TencentIM", "logout被挤");
//                UserInfo.getInstance().setToken("");
//                UserInfo.getInstance().setAutoLogin(false);
//                Intent intent = new Intent(com.blankj.utilcode.util.AppUtils.getAppPackageName() + ".hs.act.slbapp.SlbLoginActivity");
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("LOGOUT", true);
//                startActivity(intent);
            }

            @Override
            public void onUserSigExpired() {
//            ToastUtil.toastLongMessage(YewuApplicationAndroid.instance().getString(com.tencent.qcloud.tim.demo.R.string.expired_login_tip));
                DemoLog.e("TencentIM", "logout过期");
//                UserInfo.getInstance().setToken("");
//                UserInfo.getInstance().setAutoLogin(false);
//                Intent intent = new Intent(com.blankj.utilcode.util.AppUtils.getAppPackageName() + ".hs.act.slbapp.SlbLoginActivity");
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("LOGOUT", true);
//                startActivity(intent);
            }
        });
    }

}
