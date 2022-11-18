package com.sangfor.sdkdemo.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sangfor.sdk.SFUemSDK;
import com.sangfor.sdk.utils.SFLogN;
import com.sangfor.sdkdemo.R;
import com.sangfor.sdkdemo.utils.SignatureUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AuthSuccessActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AuthSuccessActivity";
    private static String sTestURL = "https://192.168.30.252:443";
    private final int TEST_URL_TIMEOUT_MILLIS = 8 * 1000;// 测试vpn资源的超时时间
    private EditText mEtUrl;
    private FrameLayout mWebViewContainer = null;
    private WebView mWebView = null;
    private RadioGroup mRadioGroup_authMethod = null;
    private RadioButton mRadioButton_selected_authMethod = null;
    private AutoCompleteTextView mAutoCompleteTextView = null;

    private static ThreadPoolExecutor sBackupExecutor =
            new ThreadPoolExecutor(3, 3, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
                private final AtomicInteger mCount = new AtomicInteger(1);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "downloadfile thread #" + mCount.getAndIncrement());
                }
            });

    static {
        sBackupExecutor.allowCoreThreadTimeOut(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate...");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.vpn_activity_auth_success);

        initView();
        initClickEvents();
    }

    private void initView() {
        mEtUrl = findViewById(R.id.et_url);
        mRadioGroup_authMethod = findViewById(R.id.svpn_resource_tabheader);
        mAutoCompleteTextView = findViewById(R.id.autoComTextView_url);
        mWebViewContainer = findViewById(R.id.web_view_container);
        mWebView = new WebView(getApplicationContext());
        mWebViewContainer.addView(mWebView);
        setWebViewSettings();  //设置webview配置参数

        String[] str_extraResource_url = getResources().getStringArray(R.array.extra_resource_url);
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(AuthSuccessActivity.this, R.layout.support_simple_spinner_dropdown_item,
                        str_extraResource_url);
        mAutoCompleteTextView.setAdapter(arrayAdapter);

        mEtUrl.setText(sTestURL);

    }

    //注册监听事件
    private void initClickEvents() {
        findViewById(R.id.btn_file).setOnClickListener(this);
        findViewById(R.id.btn_udp).setOnClickListener(this);
        findViewById(R.id.btn_log).setOnClickListener(this);

        //资源展示按钮变动监听
        mRadioGroup_authMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //监听认证按钮，动态改变布局显示
                int checkedRadioButtonId = mRadioGroup_authMethod.getCheckedRadioButtonId();
                if (checkedRadioButtonId == R.id.svpn_intraResource_tabheader) {
                    findViewById(R.id.et_url).setVisibility(View.VISIBLE);
                    findViewById(R.id.autoComTextView_url).setVisibility(View.GONE);
                } else if (checkedRadioButtonId == R.id.svpn_extraResource_tabheader) {
                    findViewById(R.id.et_url).setVisibility(View.GONE);
                    findViewById(R.id.autoComTextView_url).setVisibility(View.VISIBLE);
                }

            }
        });

        //AutoCompleteTextView控件获取焦点时，展示提示资源
        mAutoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((AutoCompleteTextView) v).showDropDown();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_test_res) {
            doTestResource();
        } else if (id == R.id.btn_logout) {
            doVPNLogout();
        } else if (id == R.id.btn_file) {
            entryFileTestPage();
        } else if (id == R.id.btn_udp) {
            entryUdpPage();
        } else if (id == R.id.btn_log) {
            entryLogTestPage();
        }
    }

    private void entryUdpPage() {
        if (false) {
            startActivity(new Intent(this, UdpTestActivity.class));
        } else {
            Toast.makeText(this, "当前不支持UDP测试", Toast.LENGTH_LONG).show();
        }
    }

    private void entryLogTestPage() {
        startActivity(new Intent(this, LogTestActivity.class));
    }

    private void entryFileTestPage() {
        startActivity(new Intent(this, FileTestActivtiy.class));
    }

    /**
     * 测试资源
     */
    private void doTestResource() {
        //获取选定的认证方式
        mRadioButton_selected_authMethod = (RadioButton) findViewById(mRadioGroup_authMethod.getCheckedRadioButtonId());
        String resourceType = mRadioButton_selected_authMethod.getText().toString().trim();
        final EditText editText =
                resourceType.equals(getString(R.string.str_intranet_resource)) ? mEtUrl : mAutoCompleteTextView;
        LoadPageByWebView(editText.getText().toString());
    }

    /**
     * 注销流程
     */
    private void doVPNLogout() {
        // 注销VPN登录.
        SFUemSDK.getInstance().logout();
        Toast.makeText(AuthSuccessActivity.this, R.string.str_vpn_logout, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void LoadPageByWebView(String url) {
        if (url == null || url.equals("")) {
            SFLogN.info(TAG, "LoadPageByWebView url is wrong!");
            return;
        }
        if (!url.contains("http")) {
            url = "http://" + url;
        }

        mWebView.loadUrl(url);
    }

    /**
     * 获取应用签名，用于设置签名白名单，只有签名白名单内的子应用才能正常和主应用通信
     *
     * @param packageName
     * @return
     */
    private String getSafeAppSignature(String packageName) {
        PackageManager packageManager = getPackageManager();
        ApplicationInfo applicationInfo;

        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            SFLogN.warn2(TAG, "getSafeAppSignature failed", "application not found: " + packageName, e);
            return "";
        }
        String pubkeyInfo = SignatureUtils.getPubKeyInfo(applicationInfo, this);
        if (TextUtils.isEmpty(pubkeyInfo)) {
            SFLogN.warn2(TAG, "getSafeAppSignature failed", "get pubkeyInfo failed: " + packageName);
            return "";
        }
        SFLogN.info(TAG, "pubKeyInfo: " + pubkeyInfo + ",packageName: " + packageName);
        return pubkeyInfo;
    }

    private boolean downloadFile(String url, File dir) {
        try {

            SFLogN.info(TAG, "run startDownload");
            long start = SystemClock.elapsedRealtime();

            URL url1 = new URL(url);
            String decodeUrl = URLDecoder.decode(url1.getFile(), "utf-8");
            SFLogN.info(TAG, "downloadFile decodeUrl:" + decodeUrl);
            File dstFile = new File(dir, decodeUrl);
            try {
                if (!dstFile.exists()) {
                    dstFile.getParentFile().mkdirs();
                    dstFile.createNewFile();
                }
            } catch (Exception e) {
                SFLogN.error(TAG, "downloadFile failed.", e);
            }

            SFLogN.info(TAG, "downloadFile dstFile:" + dstFile.getAbsolutePath() + " exist:" + dstFile.exists());

            URLConnection connection = url1.openConnection();
            connection.connect();
            long contentLen = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                contentLen = connection.getContentLengthLong();
            } else {
                contentLen = connection.getContentLength();
            }
            SFLogN.info(TAG, "downloadFile content len:" + contentLen);

            InputStream inputStream = connection.getInputStream();
            OutputStream outputStream = new FileOutputStream(dstFile);
            byte[] buf = new byte[2048];
            int len = 0;
            long totlen = 0;
            long percent = 1;
            long lastCount = 0, lastTime = SystemClock.elapsedRealtime();
            while ((len = inputStream.read(buf)) >= 0) {
                totlen += len;
                outputStream.write(buf, 0, len);
                if (contentLen > 0 && totlen * 100 > contentLen * percent) {
                    percent++;
                    SFLogN.info(TAG, "downloadFile %d%% speed: %.2fMB/s",
                            (totlen * 100 / contentLen),
                            (totlen - lastCount) / 1024.0 / 1024 * 1000 / (SystemClock.elapsedRealtime() - lastTime));
                    lastCount = totlen;
                    lastTime = SystemClock.elapsedRealtime();
                }
            }
            inputStream.close();
            outputStream.close();

            SFLogN.info(TAG, "downloadFile over,totlen:" + totlen);

            long cost = SystemClock.elapsedRealtime() - start;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AuthSuccessActivity.this, "下載完成!", Toast.LENGTH_SHORT).show();
                }
            });
            SFLogN.info(TAG, "downloadFile cost:%.2fs, speed:%.2fMB/s", (cost / 1000.0f),
                    (totlen / 1024.0 / 1024.0 / cost * 1000));
            return true;
        } catch (Exception e) {
            SFLogN.error(TAG, "downloadFile failed.", e);
        }
        return false;
    }

    private void setWebViewSettings() {
        mWebView.setWebViewClient(new MyWebViewClient());

        WebSettings webSettings = mWebView.getSettings();
        // 不使用缓存，只从网络获取数据。
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        // 开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        // 设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
        webSettings.setUseWideViewPort(true);
        // 设置默认加载的可视范围是大视野范围
        webSettings.setLoadWithOverviewMode(true);
        // 网页中包含JavaScript内容需调用以下方法，参数为true
        webSettings.setJavaScriptEnabled(true);
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype,
                                        long contentLength) {
                SFLogN.info(TAG, "onDownloadStart url:" + url + " len:" + contentLength);
                SFLogN.info(TAG, "onDownloadStart download dir:" + getExternalFilesDir(null).getAbsolutePath());
                Toast.makeText(AuthSuccessActivity.this, String.format("下载地址Url:%s 下载文件路径:%s",
                        url, getExternalFilesDir(null).getAbsolutePath()), Toast.LENGTH_LONG).show();
                sBackupExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        SFLogN.info(TAG, "run startDownload");
                        boolean ret = downloadFile(url, new File(getExternalFilesDir(null) + "/download/"));
                        SFLogN.info(TAG, "run downloadFile ret:" + ret);
                    }
                });
            }
        });
    }

    private class MyWebViewClient extends WebViewClient {

        public MyWebViewClient() {
        }

        @Override
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            SFLogN.info(TAG, "onPageStarted url = " + url);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //清除缓存
            mWebView.clearCache(true);
            mWebView.clearHistory();
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();// 忽略证书错误
        }
    }

    /**
     * WebView的回收销毁，防止内存泄漏
     */
    private void destroyWebView() {
        mWebViewContainer.removeAllViews();
        if (mWebView != null) {
            mWebView.clearHistory();
            mWebView.clearCache(true);
            mWebView.loadUrl("about:blank");
            mWebView.freeMemory();
//            mWebView.pauseTimers();
            mWebView.destroy();
        }
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume...");
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i(TAG, "onNewIntent...");
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        destroyWebView();

        super.onDestroy();
    }
}
