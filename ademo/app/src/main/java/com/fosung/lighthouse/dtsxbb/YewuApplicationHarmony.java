/**
 * Copyright 2016,Smart Haier.All rights reserved
 */
package com.fosung.lighthouse.dtsxbb;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.LocaleList;
import android.text.TextUtils;

import androidx.multidex.MultiDex;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.geek.libutils.app.AppUtils;
import com.geek.libutils.app.LocalManageUtil;
import com.geek.libutils.app.MyLogUtil;
import com.geek.libutils.data.MmkvUtils;
import com.geek.libutils.libretrofit.RetrofitNetNew;
import com.geek.libwebview.hois2.HiosHelper;
import com.heytap.msp.push.HeytapPushManager;
import com.huawei.hms.push.HmsMessaging;
import com.mob.MobSDK;
import com.mob.OperationCallback;
import com.mob.pushsdk.MobPush;
import com.mob.pushsdk.MobPushCallback;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMConversationListener;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSDKListener;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.demo.bean.UserInfo;
import com.tencent.qcloud.tim.demo.signature.GenerateTestUserSig;
import com.tencent.qcloud.tim.demo.thirdpush.HUAWEIHmsMessageService;
import com.tencent.qcloud.tim.demo.utils.BrandUtil;
import com.tencent.qcloud.tim.demo.utils.DemoLog;
import com.tencent.qcloud.tim.demo.utils.PrivateConstants;
import com.tencent.qcloud.tim.demo.utils.TUIUtils;
import com.tencent.qcloud.tuicore.util.ToastUtil;
import com.vivo.push.PushClient;
import com.vivo.push.util.VivoPushException;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;
import ohos.abilityshell.HarmonyApplication;

/**
 * <p class="note">File Note</p>
 * created by geek at 2017/6/6
 */
public class YewuApplicationHarmony extends HarmonyApplication {

    private static final String TAG = YewuApplicationHarmony.class.getSimpleName();
    private static YewuApplicationHarmony instance;

    public static YewuApplicationHarmony instance() {
        return instance;
    }

    public static final String DIR_PROJECT = "/geekandroid/app/";
    public static final String DIR_CACHE = DIR_PROJECT + "cache/"; // ??????????????????
    public static final String IMG_CACHE = DIR_PROJECT + "image/"; // image????????????
    public static final String VIDEO_CACHE = DIR_PROJECT + "video/"; // video????????????
    public static final String MUSIC_CACHE = DIR_PROJECT + "music/"; // music????????????

    @Override
    public void onCreate() {
        super.onCreate();
        if (!AppUtils.isProcessAs(getPackageName(), this)) {
            return;
        }
        //TODO commonbufen
        configBugly(BuildConfigyewu.versionNameConfig, "4d63611bdc");
        configHios();
        configmmkv();
        configShipei();
        configRetrofitNet();
        //TODO ??????bufen
        //?????????mob
        configMob();
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

    }

    private void initTencentIMNew() {
        TUIUtils.init(this, GenerateTestUserSig.SDKAPPID, null, null);
        HeytapPushManager.init(this, true);
        if (BrandUtil.isBrandXiaoMi()) {
            // ??????????????????
            MiPushClient.registerPush(this, PrivateConstants.XM_PUSH_APPID, PrivateConstants.XM_PUSH_APPKEY);
        } else if (BrandUtil.isBrandHuawei()) {
            // ???????????????????????????????????????Push???????????????????????????
            HmsMessaging.getInstance(this).turnOnPush().addOnCompleteListener(new com.huawei.hmf.tasks.OnCompleteListener<Void>() {
                @Override
                public void onComplete(com.huawei.hmf.tasks.Task<Void> task) {
                    if (task.isSuccessful()) {
                        DemoLog.e("TencentIM", "huawei turnOnPush Complete");
                    } else {
                        DemoLog.e(TAG, "huawei turnOnPush failed: ret=" + task.getException().getMessage());
                    }
                }
            });
        } else if (BrandUtil.isBrandVivo()) {
            // vivo????????????
            try {
                PushClient.getInstance(getApplicationContext()).initialize();
            } catch (VivoPushException e) {
                e.printStackTrace();
            }
        } else if (HeytapPushManager.isSupportPush()) {
            // oppo???????????????????????????????????????????????????????????????token?????????????????????MainActivity??????
        }
        registerActivityLifecycleCallbacks(new YewuApplicationHarmony.StatisticActivityLifecycleCallback());
        initLoginStatusListener();
    }

    public void initLoginStatusListener() {
        V2TIMManager.getInstance().addIMSDKListener(loginStatusListener);
    }

    private final V2TIMSDKListener loginStatusListener = new V2TIMSDKListener() {
        @Override
        public void onKickedOffline() {
            ToastUtil.toastLongMessage(YewuApplicationHarmony.instance().getString(com.tencent.qcloud.tim.demo.R.string.repeat_login_tip));
            logout();
        }

        @Override
        public void onUserSigExpired() {
            ToastUtil.toastLongMessage(YewuApplicationHarmony.instance().getString(com.tencent.qcloud.tim.demo.R.string.expired_login_tip));
            logout();
        }
    };

    public void logout() {
        DemoLog.e("TencentIM", "logout");
        UserInfo.getInstance().setToken("");
        UserInfo.getInstance().setAutoLogin(false);
        Intent intent = new Intent(com.blankj.utilcode.util.AppUtils.getAppPackageName() + ".hs.act.slbapp.SlbLoginActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("LOGOUT", true);
        startActivity(intent);
    }

    class StatisticActivityLifecycleCallback implements ActivityLifecycleCallbacks {
        private int foregroundActivities = 0;
        private boolean isChangingConfiguration;

        private final V2TIMConversationListener unreadListener = new V2TIMConversationListener() {
            @Override
            public void onTotalUnreadMessageCountChanged(long totalUnreadCount) {
                HUAWEIHmsMessageService.updateBadge(YewuApplicationHarmony.this, (int) totalUnreadCount);
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


    @Override
    protected void attachBaseContext(Context base) {
        //????????????????????????
        LocalManageUtil.saveSystemCurrentLanguage(base);
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //????????????????????????
        LocalManageUtil.onConfigurationChanged(this);
    }

    protected void configBugly(String banben_comm, String buglykey) {
        // ?????????????????????
//         String channel = WalleChannelReader.getChannel(this);
//         Bugly.set(getApplicationContext(), channel);
        if (TextUtils.equals(banben_comm, "??????")) {
            MyLogUtil.on(true);
            //TODO test
//            if (LeakCanary.isInAnalyzerProcess(this)) {
//                // This process is dedicated to LeakCanary for heap analysis.
//                // You should not init your app in this process.
//                return;
//            }
//            LeakCanary.install(this);
        } else if (TextUtils.equals(banben_comm, "?????????")) {
            MyLogUtil.on(true);
        } else if (TextUtils.equals(banben_comm, "??????")) {
            MyLogUtil.on(true);
        }
        CrashReport.initCrashReport(getApplicationContext(), buglykey, true);
    }

    /**
     * ??????https???????????????
     * ??????Glide??????https???????????????
     * javax.net.ssl.SSLHandshakeException: java.security.cert.CertPathValidatorException: Trust anchor for certification path not found.
     */
    protected void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            // trustAllCerts?????????????????????
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

    protected void configHios() {
//        HiosRegister.load();// ?????????????????? ?????????
        HiosHelper.config(com.blankj.utilcode.util.AppUtils.getAppPackageName() + ".ad.web.page", com.blankj.utilcode.util.AppUtils.getAppPackageName() + ".web.page");
    }

    protected void others() {
        Utils.init(this);// com.blankj:utilcode:1.17.3
        // ?????????????????????toast
        com.hjq.toast.ToastUtils.init(this);
        // ????????????
        LocalManageUtil.setApplicationLanguage(this);
        handleSSLHandshake();
        regActivityLife();
    }

    protected void configmmkv() {
        MmkvUtils.getInstance().get("");
        MmkvUtils.getInstance().get_demo();
    }

    protected void configShipei() {
        AutoSizeConfig.getInstance().getUnitsManager()
                .setSupportDP(true)
                .setSupportSubunits(Subunits.MM);
        AutoSize.initCompatMultiProcess(this);
    }

    protected int mFinalCount;

    protected void regActivityLife() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            this.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                }

                @Override
                public void onActivityStarted(Activity activity) {
                    mFinalCount++;
                    //??????mFinalCount ==1??????????????????????????????
                    if (mFinalCount == 1) {
                        //??????????????????????????????
                    }
                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {
                    mFinalCount--;
                    //??????mFinalCount == 0???????????????????????????
                    if (mFinalCount == 0) {
                        //??????????????????????????????
                        //                    Toast.makeText(MyApplication.this, "?????????????????????????????????", Toast.LENGTH_LONG).show();
                        ToastUtils.showLong(com.blankj.utilcode.util.AppUtils.getAppName() + "?????????????????????");
                    }
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {

                }
            });
        }
    }

    protected void configRetrofitNet() {
        String cacheDir = getExternalFilesDir(null) + DIR_CACHE;
        // https://api-cn.faceplusplus.com/
//        RetrofitNet.config();
        RetrofitNetNew.config();
    }


    private void initpgyer() {
//        new PgyerSDKManager.Init()
//                .setContext(this) //?????????????????????
//                .start();
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
        JNIUtils jniUtils = new JNIUtils();
        MyLogUtil.e("--JNIUtils--", jniUtils.stringFromJNI());
    }

    private Handler handler;

    private void configMob() {
        MobSDK.init(this);
        //
//        MobPush.setDeviceTokenByUser(DeviceUtils.getAndroidID());
        //???????????????????????????  ?????????MainActivity????????????????????????MobPushReceiver
        MobPush.getRegistrationId(new MobPushCallback<String>() {

            @Override
            public void onCallback(String rid) {
                MyLogUtil.e("MobPush", "RegistrationId:" + rid);
                SPUtils.getInstance().put("MOBID", rid);
                //TODO MOBID TEST
//                startService(new Intent(BaseApp.get(), MOBIDservices.class));
            }
        });
        MobPush.getDeviceToken(new MobPushCallback<String>() {
            @Override
            public void onCallback(String s) {
                MyLogUtil.e("MobPush----getDeviceToken--", s);
            }
        });
        //????????????
        mob_privacy();
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
        //
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

    private void configTuisong() {
//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(this);
//        MyLogUtil.e("jiguang->", JPushInterface.getRegistrationID(this));
//        SPUtils.getInstance().put("MOBID", JPushInterface.getRegistrationID(this));
    }

    private void configShare() {
//        JShareInterface.setDebugMode(true);
//        PlatformConfig platformConfig = new PlatformConfig()
//                .setWechat(JPushShareUtils.APP_ID, JPushShareUtils.APP_KEY)// wxa3fa50c49fcd271c 746c2cd0f414de2c256c4f2095316bd0
//                .setQQ("1106011004", "YIbPvONmBQBZUGaN")
//                .setSinaWeibo("374535501", "baccd12c166f1df96736b51ffbf600a2", "https://www.jiguang.cn");
//        JShareInterface.init(this, platformConfig);// android 10??????
    }

    private void configTongji() {
        // ??????????????????,????????????????????????
//        JAnalyticsInterface.setDebugMode(true);
//        JAnalyticsInterface.init(this);
    }

}
