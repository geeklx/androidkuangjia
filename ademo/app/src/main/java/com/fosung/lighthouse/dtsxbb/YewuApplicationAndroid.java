package com.fosung.lighthouse.dtsxbb;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebView;

import com.geek.appsplash.SplashInitUtils;
import com.geek.libbase.AndroidApplication;
import com.geek.libbase.utils.CommonUtils;
import com.geek.liblanguage.MultiLanguages;
import com.geek.liblanguage.OnLanguageListener;
import com.geek.libutils.app.AppUtils;
import com.geek.libutils.data.MmkvUtils;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

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
        // 冷启动bufen 放在main或者延迟
        newinit1();
        // 暖启动bufen
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                newinit2();
            }
        }, 2000);
        // 隐私协议bufen
        if (MmkvUtils.getInstance().get_xiancheng_bool(CommonUtils.MMKV_forceLogin)) {
            // 解耦application
            SplashInitUtils.getInstance(this).init();
        }
    }

    private void newinit1() {
        MmkvUtils.getInstance().get("");
//        MmkvUtils.getInstance().get_demo();
        configBugly(BuildConfigyewu.versionNameConfig, "468802b350", "468802b350", "468802b350");
        //
//        configWebView();
        // 修復WebView的多進程加載的bug
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName();
            WebView.setDataDirectorySuffix(processName);
        }
        configHios();
        //
        configShipei();
        //
        configRetrofitNet();
        //
        RetrofitNetNew2.config();
        RetrofitNetNew3.config();
        //

    }

    private void newinit2() {
        // ndk
        configNDK();
        // 为了横屏需求的toast
        com.hjq.toast.ToastUtils.init(this);
        //
        FileDownloader.setupOnApplicationOnCreate(this).connectionCreator(new FileDownloadUrlConnection.Creator(new FileDownloadUrlConnection.Configuration().connectTimeout(60_000) // set connection timeout.
                .readTimeout(60_000) // set read timeout.
                .proxy(Proxy.NO_PROXY) // set proxy
        )).commit();
        FileDownloadUtils.setDefaultSaveRootPath(getExternalFilesDir(null).getAbsolutePath() + File.separator + "lighthouse/download");
//      FileDownloadUtils.setDefaultSaveRootPath(get_file_url(this) + File.separator + "ota/download");
        // 初始化多语种框架
        MultiLanguages.init(this);
        // 设置语种变化监听器
        MultiLanguages.setOnLanguageListener(new OnLanguageListener() {

            @Override
            public void onAppLocaleChange(Locale oldLocale, Locale newLocale) {
                Log.d("MultiLanguages", "监听到应用切换了语种，旧语种：" + oldLocale + "，新语种：" + newLocale);
            }

            @Override
            public void onSystemLocaleChange(Locale oldLocale, Locale newLocale) {
                Log.d("MultiLanguages", "监听到系统切换了语种，旧语种：" + oldLocale + "，新语种：" + newLocale + "，是否跟随系统：" + MultiLanguages.isSystemLanguage());
            }
        });
        // 语言切换
//        LocalManageUtil.setApplicationLanguage(this);
        //
        handleSSLHandshake();
        //
        regActivityLife();
        //

    }


    private void configNDK() {
//        JNIUtils jniUtils = new JNIUtils();
//        MyLogUtil.e("--JNIUtils--", jniUtils.stringFromJNI());
    }


    @Override
    public void onHomePressed() {
        super.onHomePressed();
//        AddressSaver.addr = null;
    }
}
