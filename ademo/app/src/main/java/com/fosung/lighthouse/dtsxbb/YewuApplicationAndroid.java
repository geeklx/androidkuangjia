package com.fosung.lighthouse.dtsxbb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.LocaleList;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.Utils;
import com.geek.appcommon.service.MOBIDservices;
import com.geek.libbase.AndroidApplication;
import com.geek.libutils.app.AppUtils;
import com.geek.libutils.app.BaseApp;
import com.geek.libutils.app.MyLogUtil;
import com.geek.libutils.data.MmkvUtils;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.mob.MobSDK;
import com.mob.OperationCallback;
import com.mob.pushsdk.MobPush;
import com.mob.pushsdk.MobPushCustomMessage;
import com.mob.pushsdk.MobPushNotifyMessage;
import com.mob.pushsdk.MobPushReceiver;
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
//import com.yuntongxun.confwrap.WrapManager;
//import com.yuntongxun.ecsdk.ECError;
//import com.yuntongxun.ecsdk.SdkErrorCode;
//import com.yuntongxun.plugin.common.YTXSDKCoreHelper;

import java.io.File;
import java.net.Proxy;
import java.util.Locale;

public class YewuApplicationAndroid extends AndroidApplication {

    private static final String TAG = YewuApplicationAndroid.class.getSimpleName();
    private static YewuApplicationAndroid instance;

    public static YewuApplicationAndroid instance() {
        return instance;
    }

    // 客户端版本号，品牌，型号，客户端唯一标识、操作系统名称。版本
    private String versionname = "", brand = "", model = "", serial_no = "", os_ver = "";
    //屏幕宽度、高度
    private int screen_width = 0, screen_height, version = 0;
    public static String sysSpecial = "00";
    public static String blueTooth = "00";
    public static String apkFeature = "00";
    public static String goldFish = "00";
    public static String debugger = "00";

    @Override
    public void onCreate() {
        super.onCreate();
        if (!AppUtils.isProcessAs(getPackageName(), this)) {
            return;
        }
        //TODO commonbufen
        configBugly(BuildConfigyewu.versionNameConfig, "468802b350", "468802b350", "468802b350");
        configHios();
//        HiosHelperNew.config(com.blankj.utilcode.util.AppUtils.getAppPackageName() + ".web.page3.js2");
        configmmkv();
        configShipei();
        configRetrofitNet();
        RetrofitNetNew2.config();
        RetrofitNetNew3.config();
        //TODO 业务bufen
        //初始化mob
        configMob();
        /*消息通知*/
//        mobPush();
        // ndk
        configNDK();
        // pgyer
        initpgyer();
        // 组件快捷方式bufen
        MmkvUtils.getInstance().set_xiancheng("App.isLogined", false);
        others();
        // TencentIM
//        initTencentIM();
        initTencentIMNew();
        // 容联im
//        initrlytx();
        //
        initFileDownLoader();

    }

    private void initForgetOrBack() {
        com.blankj.utilcode.util.AppUtils.registerAppStatusChangedListener(new Utils.OnAppStatusChangedListener() {
            @Override
            public void onForeground(Activity activity) {
                if (!ServiceUtils.isServiceRunning(MOBIDservices.class)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(new Intent(BaseApp.get(), MOBIDservices.class));
                    } else {
                        startService(new Intent(BaseApp.get(), MOBIDservices.class));
                    }
                }
            }

            @Override
            public void onBackground(Activity activity) {

            }
        });
    }

    private void mobPush() {

        MobPush.setClickNotificationToLaunchMainActivity(false);
        MobPush.addPushReceiver(new MobPushReceiver() {
            @Override
            public void onCustomMessageReceive(Context context, MobPushCustomMessage message) {
                //接收自定义消息(透传)
                System.out.println("MobPush onCustomMessageReceive:" + message.toString());
                Log.e("aaaaaa", "MobPush onCustomMessageReceive: " + message.toString());
            }

            @Override
            public void onNotifyMessageReceive(Context context, MobPushNotifyMessage message) {
                //接收通知消
                System.out.println("MobPush onNotifyMessageReceive:" + message.toString());
                Log.e("aaaaaa", "MobPush onNotifyMessageReceive: " + message.toString());
            }

            @Override
            public void onNotifyMessageOpenedReceive(Context context, MobPushNotifyMessage message) {
                //接收通知消息被点击事件
                Log.e("aaaaaa", message.getContent() + "onNotifyMessageOpenedReceive: " + message.toString());
//                System.out.println("MobPush onNotifyMessageOpenedReceive:" + message.toString());
//                System.out.println("MobPush onNotifyMessageOpenedReceive:" + message.getContent());
                Message msg = new Message();
                msg.what = 2;
                msg.obj = message.getTitle();
                handler.sendMessage(msg);
            }

            @Override
            public void onTagsCallback(Context context, String[] tags, int operation, int errorCode) {
                //接收tags的增改删查操作
                System.out.println("MobPush onTagsCallback:" + operation + "  " + errorCode);
                Log.e("aaaaaa", "MobPush onTagsCallback: " + "  " + operation + "  " + errorCode);
            }

            @Override
            public void onAliasCallback(Context context, String alias, int operation, int errorCode) {
                //接收alias的增改删查操作
                System.out.println("MobPush onAliasCallback:" + alias + "  " + operation + "  " + errorCode);
                Log.e("aaaaaa", "MobPush onAliasCallback: " + alias + "  " + operation + "  " + errorCode);
            }
        });
        handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.obj.toString() != null && !"".equals(msg.obj.toString())) {
                    if (msg.obj.toString().equals("公告推送")) {
                        Log.e("aaaaaa", "公告推送: " + msg.obj.toString());
                        Intent intent = new Intent(com.blankj.utilcode.util.AppUtils.getAppPackageName() + ".hs.act.slbapp.AdNaticeActivity");
                        intent.putExtra("url1", "https://www.baidu.com/");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("content", "公告摘要描述公告摘要描述公告，摘要描述公告摘要描述公告摘要描述公告摘要描述公告摘要描述，公告摘要描述公告摘要描述公告摘要描述公告摘要描述，公告摘要描述公告摘要描述公告摘要描述公告摘要描述");
                        startActivity(intent);
                    } else if (msg.obj.toString().equals("推送通知")) {
                        Log.e("aaaaaa", "推送通知: " + msg.obj.toString());
                        Intent intent = new Intent(com.blankj.utilcode.util.AppUtils.getAppPackageName() + ".hs.act.slbapp.AdPushNaticeActivity");
                        intent.putExtra("url1", "https://www.baidu.com/");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("content", "公告摘要描述公告摘要描述公告，公告摘要描述公告摘要描述公告摘要描述公告摘要描述");
                        startActivity(intent);
                    } else if (msg.obj.toString().equals("业务推送")) {
                        Log.e("aaaaaa", "业务推送: " + msg.obj.toString());
                        Intent intent = new Intent(com.blankj.utilcode.util.AppUtils.getAppPackageName() + ".hs.act.slbapp.AdBusinessNaticeActivity");
                        intent.putExtra("url1", "https://www.baidu.com/");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("content", "公告摘要描述公告摘要描述公告，摘要描述公告摘要描述公告摘要描述公告摘要描述公告摘要描述，公告摘要描述公告摘要描述公告摘要描述公告摘要描述，公告摘要描述公告摘要描述公告摘要描述公告摘要描述");
                        startActivity(intent);
                    }
                }
                return false;
            }
        });
    }

    /**
     * 初始化文件下载
     */
    private void initFileDownLoader() {
        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(60_000) // set connection timeout.
                        .readTimeout(60_000) // set read timeout.
                        .proxy(Proxy.NO_PROXY) // set proxy
                )).commit();
        FileDownloadUtils.setDefaultSaveRootPath(getExternalFilesDir(null).getAbsolutePath() + File.separator + "lighthouse/download");
//      FileDownloadUtils.setDefaultSaveRootPath(get_file_url(this) + File.separator + "ota/download");
    }

//    private void initrlytx() {
////        WrapManager.getInstance().app_init(this);
//        MyLogUtil.e("MobPush----getDeviceToken--ronglian", "容联初始化成功");
//        if (TextUtils.equals(BuildConfigyewu.versionNameConfig, "测试")) {
//            WrapManager.getInstance().app_init(this, "119.188.115.248", 7773, 18086);
//        } else if (TextUtils.equals(BuildConfigyewu.versionNameConfig, "线上")) {
//            WrapManager.getInstance().app_init(this, "119.188.115.251", 7773, 18086);
//        }
//        WrapManager.getInstance().setOnConnectStateListener(new YTXSDKCoreHelper.OnConnectStateListener() {
//            @Override
//            public void onConnectSuccess() {
//                //初始化成功
//            }
//
//            @Override
//            public void onConnectFailed(ECError error) {
//                //     用户被挤下线
//                if (error.errorCode == SdkErrorCode.SDK_KICKED_OFF) {
////                    WrapManager.getInstance().onClearUserEdge(getApplicationContext());
////                    //
////                    Intent intent = new Intent(com.blankj.utilcode.util.AppUtils.getAppPackageName() + ".hs.act.slbapp.SlbLoginActivity");
////                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    intent.putExtra("LOGOUT", true);
////                    startActivity(intent);
//                }
//                //      用户未登陆
//                if (error.errorCode == YTXSDKCoreHelper.SDK_UN_LOGIN) {
//
//                }
//            }
//        });
//
//    }

    private void initTencentIMNew() {
        TUIUtils.init(this, 1400598372, null, null);
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
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            private int foregroundActivities = 0;
            private boolean isChangingConfiguration;

            private final V2TIMConversationListener unreadListener = new V2TIMConversationListener() {
                @Override
                public void onTotalUnreadMessageCountChanged(long totalUnreadCount) {
                    HUAWEIHmsMessageService.updateBadge(YewuApplicationAndroid.this, (int) totalUnreadCount);
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
                            DemoLog.e(TAG, "doForeground err = " + code + ", desc = " + desc);
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
                                    DemoLog.e(TAG, "doBackground err = " + code + ", desc = " + desc);
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
//    private void initTencentIM() {
//        TXLiveBase.getInstance().setLicence(this, "licenceUrl", "licenseKey");
//        /**
//         * TUIKit的初始化函数
//         *
//         * @param context  应用的上下文，一般为对应应用的ApplicationContext
//         * @param sdkAppID 您在腾讯云注册应用时分配的sdkAppID
//         * @param configs  TUIKit的相关配置项，一般使用默认即可，需特殊配置参考API文档
//         */
//        TUIKit.init(this, GenerateTestUserSig.SDKAPPID, new ConfigHelper().getConfigs());
//        // oppo
////        HeytapPushManager.init(this, true);
//        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
//            private int foregroundActivities = 0;
//            private boolean isChangingConfiguration;
//            private IMEventListener mIMEventListener = new IMEventListener() {
//                @Override
//                public void onNewMessage(V2TIMMessage msg) {
//                    MessageNotification notification = MessageNotification.getInstance();
//                    notification.notify(msg);
//                }
//            };
//
//            private ConversationManagerKit.MessageUnreadWatcher mUnreadWatcher = new ConversationManagerKit.MessageUnreadWatcher() {
//                @Override
//                public void updateUnread(int count) {
//                    // 华为离线推送角标
//                    HUAWEIHmsMessageService.updateBadge(BaseApp.get(), count);
//                }
//            };
//
//            @Override
//            public void onActivityCreated(Activity activity, Bundle bundle) {
//                DemoLog.i("TUIKit", "onActivityCreated bundle: " + bundle);
//                if (bundle != null) { // 若bundle不为空则程序异常结束
//                    // 重启整个程序
////                    Intent intent = new Intent(activity, SplashActivity.class);
//                    Intent intent = new Intent(com.blankj.utilcode.util.AppUtils.getAppPackageName() + ".hs.act.slbapp.WelComeActivity");
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onActivityStarted(Activity activity) {
//                foregroundActivities++;
//                if (foregroundActivities == 1 && !isChangingConfiguration) {
//                    // 应用切到前台
//                    DemoLog.i("TUIKit", "application enter foreground");
//                    V2TIMManager.getOfflinePushManager().doForeground(new V2TIMCallback() {
//                        @Override
//                        public void onError(int code, String desc) {
//                            DemoLog.e("TUIKit", "doForeground err = " + code + ", desc = " + desc);
//                        }
//
//                        @Override
//                        public void onSuccess() {
//                            DemoLog.i("TUIKit", "doForeground success");
//                        }
//                    });
//                    TUIKit.removeIMEventListener(mIMEventListener);
//                    ConversationManagerKit.getInstance().removeUnreadWatcher(mUnreadWatcher);
//                    MessageNotification.getInstance().cancelTimeout();
//                }
//                isChangingConfiguration = false;
//            }
//
//            @Override
//            public void onActivityResumed(Activity activity) {
//
//            }
//
//            @Override
//            public void onActivityPaused(Activity activity) {
//
//            }
//
//            @Override
//            public void onActivityStopped(Activity activity) {
//                foregroundActivities--;
//                if (foregroundActivities == 0) {
//                    // 应用切到后台
//                    DemoLog.i("", "application enter background");
//                    int unReadCount = ConversationManagerKit.getInstance().getUnreadTotal();
//                    V2TIMManager.getOfflinePushManager().doBackground(unReadCount, new V2TIMCallback() {
//                        @Override
//                        public void onError(int code, String desc) {
//                            DemoLog.e("TUIKit", "doBackground err = " + code + ", desc = " + desc);
//                        }
//
//                        @Override
//                        public void onSuccess() {
//                            DemoLog.i("TUIKit", "doBackground success");
//                        }
//                    });
//                    // 应用退到后台，消息转化为系统通知
//                    TUIKit.addIMEventListener(mIMEventListener);
//                    ConversationManagerKit.getInstance().addUnreadWatcher(mUnreadWatcher);
//                }
//                isChangingConfiguration = activity.isChangingConfigurations();
//            }
//
//            @Override
//            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
//
//            }
//
//            @Override
//            public void onActivityDestroyed(Activity activity) {
//
//            }
//        });
//    }

    private void initpgyer() {
        new PgyerSDKManager.Init()
                .setContext(this) //设置上下问对象
                .setApiKey("8bd21fed5150eab87567cbcfb6ed8a46")
                .setFrontJSToken("d68155b81756a82d2324fe46bff24c45")
                .enable(Features.CHECK_UPDATE)
                .start();
        // 上报异常bufen
//        try {
//
//        } catch (Exception e) {
//            PgyerSDKManager.reportException(e);
//        }
        // 手动更新
//        PgyerSDKManager.checkVersionUpdate((Activity) getApplicationContext(), new CheckoutCallBack() {
//            @Override
//            public void onNewVersionExist(CheckSoftModel model) {
//                //检查版本成功（有新版本）
//
//                /**
//                 *   CheckSoftModel 参数介绍
//                 *
//                 *    private int buildBuildVersion;//蒲公英生成的用于区分历史版本的build号
//                 *     private String forceUpdateVersion;//强制更新版本号（未设置强置更新默认为空）
//                 *     private String forceUpdateVersionNo;//强制更新的版本编号
//                 *     private boolean needForceUpdate;//	是否强制更新
//                 *     private boolean buildHaveNewVersion;//是否有新版本
//                 *     private String downloadURL;//应用安装地址
//                 *     private String buildVersionNo;//上传包的版本编号，默认为1 (即编译的版本号，一般来说，编译一次会
//                 *    变动一次这个版本号, 在 Android 上叫 Version Code。对于 iOS 来说，是字符串类型；对于 Android 来
//                 *    说是一个整数。例如：1001，28等。)
//                 *     private String buildVersion;//版本号, 默认为1.0 (是应用向用户宣传时候用到的标识，例如：1.1、8.2.1等。)
//                 *     private String buildShortcutUrl;//	应用短链接
//                 *     private String buildUpdateDescription;//	应用更新说明
//                 */
//
//            }
//
//            @Override
//            public void onNonentityVersionExist(String string) {
//                //无新版本
//            }
//
//            @Override
//            public void onFail(String error) {
//                //请求异常
//            }
//        });
        // 用户自定义数据(上传数据必须是JSON字符串格式 如：{"userId":"ceshi_001","userName":"ceshi"})
//        PgyerSDKManager.setUserData("{\"userId\":\"ceshi_001\",\"userName\":\"ceshi\"}");
    }

    private void configNDK() {
//        JNIUtils jniUtils = new JNIUtils();
//        MyLogUtil.e("--JNIUtils--", jniUtils.stringFromJNI());
    }

    private Handler handler;

    private void configMob() {
        MobSDK.init(this);
        //隐私协议
        MobSDK.submitPolicyGrantResult(true);
        //
//        MobPush.getRegistrationId(new MobPushCallback<String>() {
//
//            @Override
//            public void onCallback(String rid) {
//                MyLogUtil.e("RegistrationId", "RegistrationId:" + rid);
//                SPUtils.getInstance().put("MOBID", rid);
//
//            }
//        });
//        MobPush.getDeviceToken(new MobPushCallback<String>() {
//            @Override
//            public void onCallback(String s) {
//                MyLogUtil.e("MobPush----getDeviceToken2--", s);
//                SPUtils.getInstance().put("MOBDeviceToken", s);
//                if (!TextUtils.isEmpty(s)) {
//                    ThirdPushTokenMgr.getInstance().setThirdPushToken(s);
//                    ECDevice.reportHuaWeiToken(s);
//                }
//            }
//        });
//        mob_privacy();
//        MobPush.setDeviceTokenByUser(DeviceUtils.getAndroidID());
        //防止多进程注册多次  可以在MainActivity或者其他页面注册MobPushReceiver
//        MobPush.getDeviceToken(new MobPushCallback<String>() {
//            @Override
//            public void onCallback(String s) {
//                MyLogUtil.e("MobPush----getDeviceToken1--", s);
//                SPUtils.getInstance().put("MOBDeviceToken", s);
////                // 腾讯im
////                ThirdPushTokenMgr.getInstance().setThirdPushToken(s);
////                ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
////                // 容联im
////                initrlytx();
////                ECDevice.reportHuaWeiToken(s);
//            }
//        });
//        MobSDK.init(this);
//        //防止多进程注册多次  可以在MainActivity或者其他页面注册MobPushReceiver
//        String processName = getProcessName(this);
//        MobPush.getRegistrationId(new MobPushCallback<String>() {
//
//            @Override
//            public void onCallback(String rid) {
//                System.out.println("MobPush->RegistrationId:" + rid);
//                SPUtils.getInstance().put("MOBID", rid);
//            }
//        });
//        if (getPackageName().equals(processName)) {
//            MobPush.addPushReceiver(new MobPushReceiver() {
//                @Override
//                public void onCustomMessageReceive(Context context, MobPushCustomMessage message) {
//                    //接收自定义消息(透传)
//                    System.out.println("MobPush onCustomMessageReceive:" + message.toString());
//                }
//
//                @Override
//                public void onNotifyMessageReceive(Context context, MobPushNotifyMessage message) {
//                    //接收通知消
//                    System.out.println("MobPush onNotifyMessageReceive:" + message.toString());
//                    Message msg = new Message();
//                    msg.what = 1;
//                    msg.obj = "Message Receive:" + message.toString();
//                    handler.sendMessage(msg);
//
//                }
//
//                @Override
//                public void onNotifyMessageOpenedReceive(Context context, MobPushNotifyMessage message) {
//                    //接收通知消息被点击事件
//                    System.out.println("MobPush onNotifyMessageOpenedReceive:" + message.toString());
//                    Message msg = new Message();
//                    msg.what = 1;
//                    msg.obj = "Click Message:" + message.toString();
//                    handler.sendMessage(msg);
//                }
//
//                @Override
//                public void onTagsCallback(Context context, String[] tags, int operation, int errorCode) {
//                    //接收tags的增改删查操作
//                    System.out.println("MobPush onTagsCallback:" + operation + "  " + errorCode);
//                }
//
//                @Override
//                public void onAliasCallback(Context context, String alias, int operation, int errorCode) {
//                    //接收alias的增改删查操作
//                    System.out.println("MobPush onAliasCallback:" + alias + "  " + operation + "  " + errorCode);
//                }
//            });
//
//            handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
//                @Override
//                public boolean handleMessage(@NonNull Message msg) {
//                    if (msg.what == 1) {
//                        System.out.println("MobPush Callback Data:" + msg.obj);
//                    }
//                    return false;
//                }
//            });
//        }
        // activity获取信息
//        Intent intent = getIntent();
//        Uri uri = intent.getData();
//        if (intent != null) {
//            System.out.println("MobPush linkone intent---" + intent.toUri(Intent.URI_INTENT_SCHEME));
//        }
//        StringBuilder sb = new StringBuilder();
//        if (uri != null) {
//            sb.append(" scheme:" + uri.getScheme() + "\n");
//            sb.append(" host:" + uri.getHost() + "\n");
//            sb.append(" port:" + uri.getPort() + "\n");
//            sb.append(" query:" + uri.getQuery() + "\n");
//        }
//
//        //获取link界面传输的数据
//        JSONArray jsonArray = MobPushUtils.parseSchemePluginPushIntent(intent);
//        if (jsonArray != null){
//            sb.append("\n" + "bundle toString :" + jsonArray.toString());
//        }
//        //通过scheme跳转详情页面可选择此方式
//        JSONArray var = new JSONArray();
//        var =  MobPushUtils.parseSchemePluginPushIntent(intent2);
//        //跳转首页可选择此方式
//        JSONArray var2 = new JSONArray();
//        var2 = MobPushUtils.parseMainPluginPushIntent(intent2);
    }

    public void mob_privacy() {
        // 指定固定Locale
// Locale locale = Locale.CHINA;
// 指定当前系统locale
        Locale locale = null;
        if (Build.VERSION.SDK_INT >= 24) {
            LocaleList localeList = getApplicationContext().getResources().getConfiguration().getLocales();
            if (localeList != null && !localeList.isEmpty()) {
                locale = localeList.get(0);
            }
        } else {
            locale = getApplicationContext().getResources().getConfiguration().locale;
        }

// 同步方法查询隐私,locale可以为null或不设置，默认使用当前系统语言
//        PrivacyPolicy policyUrl = MobSDK.getPrivacyPolicy(MobSDK.POLICY_TYPE_URL, locale);
//        String url = policyUrl.getContent();

// 异步方法查询隐私,locale可以为null或不设置，默认使用当前系统语言
//        MobSDK.getPrivacyPolicyAsync(MobSDK.POLICY_TYPE_URL, new PrivacyPolicy.OnPolicyListener() {
//            @Override
//            public void onComplete(PrivacyPolicy data) {
//                if (data != null) {
//                    // 富文本内容
//                    String text = data.getContent();
//                    MyLogUtil.e("MobPush", "隐私协议地址->" + text);
//                    MobSDK.submitPolicyGrantResult(!TextUtils.isEmpty(text), new OperationCallback<Void>() {
//                        @Override
//                        public void onComplete(Void data) {
//                            MyLogUtil.e("MobPush", "隐私协议授权结果提交：成功");
//                        }
//
//                        @Override
//                        public void onFailure(Throwable t) {
//                            MyLogUtil.e("MobPush", "隐私协议授权结果提交：失败");
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                // 请求失败
//            }
//        });
        MobSDK.submitPolicyGrantResult(true, new OperationCallback<Void>() {
            @Override
            public void onComplete(Void data) {
                MyLogUtil.e("MobPush", "隐私协议授权结果提交：成功");
            }

            @Override
            public void onFailure(Throwable t) {
                MyLogUtil.e("MobPush", "隐私协议授权结果提交：失败");
            }
        });
    }

    @Override
    public void onHomePressed() {
        super.onHomePressed();
//        AddressSaver.addr = null;
    }
}
