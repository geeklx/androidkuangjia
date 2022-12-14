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

    // ??????????????????????????????????????????????????????????????????????????????????????????
    private String versionname = "", brand = "", model = "", serial_no = "", os_ver = "";
    //?????????????????????
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
        //TODO ??????bufen
        //?????????mob
        configMob();
        /*????????????*/
//        mobPush();
        // ndk
        configNDK();
        // pgyer
        initpgyer();
        // ??????????????????bufen
        MmkvUtils.getInstance().set_xiancheng("App.isLogined", false);
        others();
        // TencentIM
//        initTencentIM();
        initTencentIMNew();
        // ??????im
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
                //?????????????????????(??????)
                System.out.println("MobPush onCustomMessageReceive:" + message.toString());
                Log.e("aaaaaa", "MobPush onCustomMessageReceive: " + message.toString());
            }

            @Override
            public void onNotifyMessageReceive(Context context, MobPushNotifyMessage message) {
                //???????????????
                System.out.println("MobPush onNotifyMessageReceive:" + message.toString());
                Log.e("aaaaaa", "MobPush onNotifyMessageReceive: " + message.toString());
            }

            @Override
            public void onNotifyMessageOpenedReceive(Context context, MobPushNotifyMessage message) {
                //?????????????????????????????????
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
                //??????tags?????????????????????
                System.out.println("MobPush onTagsCallback:" + operation + "  " + errorCode);
                Log.e("aaaaaa", "MobPush onTagsCallback: " + "  " + operation + "  " + errorCode);
            }

            @Override
            public void onAliasCallback(Context context, String alias, int operation, int errorCode) {
                //??????alias?????????????????????
                System.out.println("MobPush onAliasCallback:" + alias + "  " + operation + "  " + errorCode);
                Log.e("aaaaaa", "MobPush onAliasCallback: " + alias + "  " + operation + "  " + errorCode);
            }
        });
        handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.obj.toString() != null && !"".equals(msg.obj.toString())) {
                    if (msg.obj.toString().equals("????????????")) {
                        Log.e("aaaaaa", "????????????: " + msg.obj.toString());
                        Intent intent = new Intent(com.blankj.utilcode.util.AppUtils.getAppPackageName() + ".hs.act.slbapp.AdNaticeActivity");
                        intent.putExtra("url1", "https://www.baidu.com/");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("content", "???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
                        startActivity(intent);
                    } else if (msg.obj.toString().equals("????????????")) {
                        Log.e("aaaaaa", "????????????: " + msg.obj.toString());
                        Intent intent = new Intent(com.blankj.utilcode.util.AppUtils.getAppPackageName() + ".hs.act.slbapp.AdPushNaticeActivity");
                        intent.putExtra("url1", "https://www.baidu.com/");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("content", "?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
                        startActivity(intent);
                    } else if (msg.obj.toString().equals("????????????")) {
                        Log.e("aaaaaa", "????????????: " + msg.obj.toString());
                        Intent intent = new Intent(com.blankj.utilcode.util.AppUtils.getAppPackageName() + ".hs.act.slbapp.AdBusinessNaticeActivity");
                        intent.putExtra("url1", "https://www.baidu.com/");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("content", "???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
                        startActivity(intent);
                    }
                }
                return false;
            }
        });
    }

    /**
     * ?????????????????????
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
//        MyLogUtil.e("MobPush----getDeviceToken--ronglian", "?????????????????????");
//        if (TextUtils.equals(BuildConfigyewu.versionNameConfig, "??????")) {
//            WrapManager.getInstance().app_init(this, "119.188.115.248", 7773, 18086);
//        } else if (TextUtils.equals(BuildConfigyewu.versionNameConfig, "??????")) {
//            WrapManager.getInstance().app_init(this, "119.188.115.251", 7773, 18086);
//        }
//        WrapManager.getInstance().setOnConnectStateListener(new YTXSDKCoreHelper.OnConnectStateListener() {
//            @Override
//            public void onConnectSuccess() {
//                //???????????????
//            }
//
//            @Override
//            public void onConnectFailed(ECError error) {
//                //     ??????????????????
//                if (error.errorCode == SdkErrorCode.SDK_KICKED_OFF) {
////                    WrapManager.getInstance().onClearUserEdge(getApplicationContext());
////                    //
////                    Intent intent = new Intent(com.blankj.utilcode.util.AppUtils.getAppPackageName() + ".hs.act.slbapp.SlbLoginActivity");
////                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    intent.putExtra("LOGOUT", true);
////                    startActivity(intent);
//                }
//                //      ???????????????
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
//            // ??????????????????
//            MiPushClient.registerPush(this, PrivateConstants.XM_PUSH_APPID, PrivateConstants.XM_PUSH_APPKEY);
//        } else if (BrandUtil.isBrandHuawei()) {
//            // ???????????????????????????????????????Push???????????????????????????
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
//            // vivo????????????
//            PushClient.getInstance(getApplicationContext()).initialize();
//        } else if (HeytapPushManager.isSupportPush()) {
//            // oppo???????????????????????????????????????????????????????????????token?????????????????????MainActivity??????
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
                if (bundle != null) { // ???bundle??????????????????????????????
                    // ??????????????????
//                Intent intent = new Intent(activity, SplashActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                foregroundActivities++;
                if (foregroundActivities == 1 && !isChangingConfiguration) {
                    // ??????????????????
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
                    // ??????????????????
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
                DemoLog.e("TencentIM", "logout??????");
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
                DemoLog.e("TencentIM", "logout??????");
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
//         * TUIKit??????????????????
//         *
//         * @param context  ?????????????????????????????????????????????ApplicationContext
//         * @param sdkAppID ???????????????????????????????????????sdkAppID
//         * @param configs  TUIKit?????????????????????????????????????????????????????????????????????API??????
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
//                    // ????????????????????????
//                    HUAWEIHmsMessageService.updateBadge(BaseApp.get(), count);
//                }
//            };
//
//            @Override
//            public void onActivityCreated(Activity activity, Bundle bundle) {
//                DemoLog.i("TUIKit", "onActivityCreated bundle: " + bundle);
//                if (bundle != null) { // ???bundle??????????????????????????????
//                    // ??????????????????
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
//                    // ??????????????????
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
//                    // ??????????????????
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
//                    // ????????????????????????????????????????????????
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
                .setContext(this) //?????????????????????
                .setApiKey("8bd21fed5150eab87567cbcfb6ed8a46")
                .setFrontJSToken("d68155b81756a82d2324fe46bff24c45")
                .enable(Features.CHECK_UPDATE)
                .start();
        // ????????????bufen
//        try {
//
//        } catch (Exception e) {
//            PgyerSDKManager.reportException(e);
//        }
        // ????????????
//        PgyerSDKManager.checkVersionUpdate((Activity) getApplicationContext(), new CheckoutCallBack() {
//            @Override
//            public void onNewVersionExist(CheckSoftModel model) {
//                //????????????????????????????????????
//
//                /**
//                 *   CheckSoftModel ????????????
//                 *
//                 *    private int buildBuildVersion;//?????????????????????????????????????????????build???
//                 *     private String forceUpdateVersion;//????????????????????????????????????????????????????????????
//                 *     private String forceUpdateVersionNo;//???????????????????????????
//                 *     private boolean needForceUpdate;//	??????????????????
//                 *     private boolean buildHaveNewVersion;//??????????????????
//                 *     private String downloadURL;//??????????????????
//                 *     private String buildVersionNo;//????????????????????????????????????1 (??????????????????????????????????????????????????????
//                 *    ???????????????????????????, ??? Android ?????? Version Code????????? iOS ???????????????????????????????????? Android ???
//                 *    ??????????????????????????????1001???28??????)
//                 *     private String buildVersion;//?????????, ?????????1.0 (?????????????????????????????????????????????????????????1.1???8.2.1??????)
//                 *     private String buildShortcutUrl;//	???????????????
//                 *     private String buildUpdateDescription;//	??????????????????
//                 */
//
//            }
//
//            @Override
//            public void onNonentityVersionExist(String string) {
//                //????????????
//            }
//
//            @Override
//            public void onFail(String error) {
//                //????????????
//            }
//        });
        // ?????????????????????(?????????????????????JSON??????????????? ??????{"userId":"ceshi_001","userName":"ceshi"})
//        PgyerSDKManager.setUserData("{\"userId\":\"ceshi_001\",\"userName\":\"ceshi\"}");
    }

    private void configNDK() {
//        JNIUtils jniUtils = new JNIUtils();
//        MyLogUtil.e("--JNIUtils--", jniUtils.stringFromJNI());
    }

    private Handler handler;

    private void configMob() {
        MobSDK.init(this);
        //????????????
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
        //???????????????????????????  ?????????MainActivity????????????????????????MobPushReceiver
//        MobPush.getDeviceToken(new MobPushCallback<String>() {
//            @Override
//            public void onCallback(String s) {
//                MyLogUtil.e("MobPush----getDeviceToken1--", s);
//                SPUtils.getInstance().put("MOBDeviceToken", s);
////                // ??????im
////                ThirdPushTokenMgr.getInstance().setThirdPushToken(s);
////                ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
////                // ??????im
////                initrlytx();
////                ECDevice.reportHuaWeiToken(s);
//            }
//        });
//        MobSDK.init(this);
//        //???????????????????????????  ?????????MainActivity????????????????????????MobPushReceiver
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
//                    //?????????????????????(??????)
//                    System.out.println("MobPush onCustomMessageReceive:" + message.toString());
//                }
//
//                @Override
//                public void onNotifyMessageReceive(Context context, MobPushNotifyMessage message) {
//                    //???????????????
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
//                    //?????????????????????????????????
//                    System.out.println("MobPush onNotifyMessageOpenedReceive:" + message.toString());
//                    Message msg = new Message();
//                    msg.what = 1;
//                    msg.obj = "Click Message:" + message.toString();
//                    handler.sendMessage(msg);
//                }
//
//                @Override
//                public void onTagsCallback(Context context, String[] tags, int operation, int errorCode) {
//                    //??????tags?????????????????????
//                    System.out.println("MobPush onTagsCallback:" + operation + "  " + errorCode);
//                }
//
//                @Override
//                public void onAliasCallback(Context context, String alias, int operation, int errorCode) {
//                    //??????alias?????????????????????
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
        // activity????????????
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
//        //??????link?????????????????????
//        JSONArray jsonArray = MobPushUtils.parseSchemePluginPushIntent(intent);
//        if (jsonArray != null){
//            sb.append("\n" + "bundle toString :" + jsonArray.toString());
//        }
//        //??????scheme????????????????????????????????????
//        JSONArray var = new JSONArray();
//        var =  MobPushUtils.parseSchemePluginPushIntent(intent2);
//        //??????????????????????????????
//        JSONArray var2 = new JSONArray();
//        var2 = MobPushUtils.parseMainPluginPushIntent(intent2);
    }

    public void mob_privacy() {
        // ????????????Locale
// Locale locale = Locale.CHINA;
// ??????????????????locale
        Locale locale = null;
        if (Build.VERSION.SDK_INT >= 24) {
            LocaleList localeList = getApplicationContext().getResources().getConfiguration().getLocales();
            if (localeList != null && !localeList.isEmpty()) {
                locale = localeList.get(0);
            }
        } else {
            locale = getApplicationContext().getResources().getConfiguration().locale;
        }

// ????????????????????????,locale?????????null?????????????????????????????????????????????
//        PrivacyPolicy policyUrl = MobSDK.getPrivacyPolicy(MobSDK.POLICY_TYPE_URL, locale);
//        String url = policyUrl.getContent();

// ????????????????????????,locale?????????null?????????????????????????????????????????????
//        MobSDK.getPrivacyPolicyAsync(MobSDK.POLICY_TYPE_URL, new PrivacyPolicy.OnPolicyListener() {
//            @Override
//            public void onComplete(PrivacyPolicy data) {
//                if (data != null) {
//                    // ???????????????
//                    String text = data.getContent();
//                    MyLogUtil.e("MobPush", "??????????????????->" + text);
//                    MobSDK.submitPolicyGrantResult(!TextUtils.isEmpty(text), new OperationCallback<Void>() {
//                        @Override
//                        public void onComplete(Void data) {
//                            MyLogUtil.e("MobPush", "???????????????????????????????????????");
//                        }
//
//                        @Override
//                        public void onFailure(Throwable t) {
//                            MyLogUtil.e("MobPush", "???????????????????????????????????????");
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                // ????????????
//            }
//        });
        MobSDK.submitPolicyGrantResult(true, new OperationCallback<Void>() {
            @Override
            public void onComplete(Void data) {
                MyLogUtil.e("MobPush", "???????????????????????????????????????");
            }

            @Override
            public void onFailure(Throwable t) {
                MyLogUtil.e("MobPush", "???????????????????????????????????????");
            }
        });
    }

    @Override
    public void onHomePressed() {
        super.onHomePressed();
//        AddressSaver.addr = null;
    }
}
