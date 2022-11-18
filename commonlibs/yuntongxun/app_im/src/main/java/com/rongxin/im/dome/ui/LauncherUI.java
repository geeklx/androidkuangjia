package com.rongxin.im.dome.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.rongxin.im.dome.R;
import com.rongxin.im.dome.fragment.ImMainFragment;
import com.yuntongxun.confwrap.WrapManager;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.plugin.common.YTXAppMgr;
import com.yuntongxun.plugin.common.YTXSDKCoreHelper;
import com.yuntongxun.plugin.common.common.YTXBackwardSupportUtil;
import com.yuntongxun.plugin.common.common.bar.YTXMeizuStatusBarCompat;
import com.yuntongxun.plugin.common.common.bar.YTXStatusBarCompat;
import com.yuntongxun.plugin.common.ui.AbsRongXinActivity;

public class LauncherUI extends AbsRongXinActivity implements YTXSDKCoreHelper.OnConnectStateListener {

    private static final String TAG = "RongXin.LauncherUI";

    private static LauncherUI mLauncherUI;
    private static int mLauncherInstanceCount = 0;
    public View mLauncherView;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private TextView mTitleView;
    private ProgressBar mProgressBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mLauncherUI != null) {
            mLauncherUI.finish();
        }
        mLauncherUI = this;
        mLauncherInstanceCount++;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setIcon(R.mipmap.ytx_theme_icon_launcher);
            mActionBar.setTitle(R.string.im_app_name);
            mActionBar.hide();
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        WrapManager.getInstance().registerPluginReceiver(LauncherUI.this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNecessaryLogin()) {
            return;
        }
        if (mLauncherView == null) {
            WrapManager.getInstance().app_AutoConnect(this);
            initLauncherUI();
        }
    }

    private void initLauncherUI() {
        mLauncherView = getLayoutInflater().inflate(R.layout.ytx_activity_launcher, null);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mLauncherView.findViewById(R.id.activityRootView).setPadding(mLauncherView.getPaddingLeft(),
                    YTXBackwardSupportUtil.getStatusBarHeight(this) + mLauncherView.getPaddingTop()
                    , mLauncherView.getPaddingRight(), mLauncherView.getPaddingBottom());
        }
        setContentView(mLauncherView);
        YTXStatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.action_bar_color));
        if (Build.DISPLAY.startsWith("Flyme")) {
            YTXMeizuStatusBarCompat.setStatusBarDarkIcon(this, true);
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setTitle("");
        }
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayShowTitleEnabled(false);
        }

        setActionBarTitle();
        initHospitalFragment();
    }

    private void setActionBarTitle() {
        if (mToolbar == null) {
            return;
        }
        mTitleView = (TextView) mToolbar.findViewById(R.id.ytx_action_title);
        mProgressBarView = (ProgressBar) mToolbar.findViewById(R.id.ytx_action_progress);
        mProgressBarView.setVisibility(View.GONE);
        if (mTitleView == null) {
            return;
        }
        mTitleView.setText(R.string.im_app_name);
        mTitleView.setOnLongClickListener(v -> false);
        mTitleView.requestLayout();
    }

    private void initHospitalFragment() {
        ImMainFragment fragment = ImMainFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.container11, fragment).commit();
    }


    private boolean isNecessaryLogin() {
        // 这里用于判断当前应用是否已经登陆过了一次
        // 登陆过了一次之后会在配置文件中设置一个标识位
        // 这个标识为用于区分当前应用是否已经登录过,也就是本地数据库已经保存了当前账号登录
        // 鉴权过后服务器返回的个人账号信息
        boolean isActive = YTXAppMgr.isActive();
        // 如果没有登录过客户端，客户端出于退出登录状态
        // 跳转到登录界面进行账号鉴权登录操作
        if (!isActive) {
            // 重置数据库
            if (mLauncherInstanceCount <= 2) {
                startActivity(new Intent(this, LoginActivity.class));
            }
            mLauncherUI = null;
            mLauncherInstanceCount--;
            finish();
            return true;
        }
        return false;
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        WrapManager.getInstance().unregisterPluginReceiver();
        mActionBar = null;
        mLauncherUI = null;
        mLauncherInstanceCount--;
    }

    @Override
    public void onConnectSuccess() {

    }

    @Override
    public void onConnectFailed(ECError error) {
        if (isFinishing()) {
            return;
        }
        //     用户被挤下线
        if (error.errorCode == SdkErrorCode.SDK_KICKED_OFF) {
            WrapManager.getInstance().checkUserEdge(LauncherUI.this, LauncherUI.class);
        }
        //      用户未登陆
        if (error.errorCode == YTXSDKCoreHelper.SDK_UN_LOGIN) {
            startActivity(new Intent(LauncherUI.this,LoginActivity.class));
            finish();
        }
    }

}
