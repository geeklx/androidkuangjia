package com.sangfor.sdkdemo.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sangfor.sdk.SFUemSDK;
import com.sangfor.sdk.base.SFSDKFlags;
import com.sangfor.sdk.base.SFSDKMode;
import com.sangfor.sdkdemo.R;
import com.sangfor.sdkdemo.primary.GlobalListenerManager;
import com.sangfor.sdkdemo.primary.PrimaryAuthActivity;
import com.sangfor.sdkdemo.primary.SecondAuthActivity;
import com.sangfor.sdkdemo.primary.SpaAuthActivity;

public class EntryActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mPrimaryAuthButton;
    private Button mSecondAuthButton;
    private Button mSpaAuthButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vpn_activity_entry);
        /**
         * 初始化sdk,推荐在attachBaseContext中调用，因为sdk延后初始化会导致多进程场景下，子进程无法拥有sdk的能力
         */
        initSdk(getApplicationContext());

        /**
         * 初始化注销监听回调
         */
        GlobalListenerManager.getInstance().init(getApplicationContext());
        //加载UI
        initView();
    }

    public void initSdk(Context context) {
        SFSDKMode sdkMode = SFSDKMode.MODE_VPN_SANDBOX;                 // 表明同时启用VPN+沙箱功能,详情参考集成指导文档
        switch (sdkMode) {
            case MODE_VPN: {    //只使用VPN功能场景
                int sdkFlags = SFSDKFlags.FLAGS_HOST_APPLICATION;      //表明是单应用或者是主应用
                sdkFlags |= SFSDKFlags.FLAGS_VPN_MODE_TCP;              //表明使用VPN功能中的TCP模式
                SFUemSDK.getInstance().initSDK(this, sdkMode, sdkFlags, null);
                break;
            }
            case MODE_SANDBOX: {   //只使用安全沙箱功能场景
                int sdkFlags = SFSDKFlags.FLAGS_HOST_APPLICATION;      //表明是单应用或者是主应用
                SFUemSDK.getInstance().initSDK(this, sdkMode, sdkFlags, null);
                break;
            }
            case MODE_VPN_SANDBOX: {    //同时使用VPN功能+安全沙箱功能场景
                int sdkFlags = SFSDKFlags.FLAGS_HOST_APPLICATION;      //表明是单应用或者是主应用
                sdkFlags |= SFSDKFlags.FLAGS_VPN_MODE_TCP;              //表明使用VPN功能中的TCP模式
                SFUemSDK.getInstance().initSDK(this, sdkMode, sdkFlags, null);
                break;
            }
            default: {
                Toast.makeText(context, "SDK模式错误", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    /**
     * 初始化界面元素
     */
    private void initView() {
        mPrimaryAuthButton = findViewById(R.id.primary_auth_button);
        mPrimaryAuthButton.setOnClickListener(this);

        mSecondAuthButton = findViewById(R.id.second_auth_button);
        mSecondAuthButton.setOnClickListener(this);

        mSpaAuthButton = findViewById(R.id.spa_auth_button);
        mSpaAuthButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.primary_auth_button) {
            gotoPrimaryAuthActivity();
        } else if (id == R.id.second_auth_button) {
            gotoSecondAuthActivity();
        } else if (id == R.id.spa_auth_button) {
            gotoSpaAuthActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 跳转用户名密码认证页面
     */
    void gotoPrimaryAuthActivity() {
        startActivity(new Intent(this, PrimaryAuthActivity.class));
    }

    /**
     * 跳转用户名密码+短信验证码认证页面
     */
    void gotoSecondAuthActivity() {
        startActivity(new Intent(this, SecondAuthActivity.class));
    }

    /**
     * 跳转启用SPA认证页面
     */
    void gotoSpaAuthActivity() {
        startActivity(new Intent(this, SpaAuthActivity.class));
    }
}