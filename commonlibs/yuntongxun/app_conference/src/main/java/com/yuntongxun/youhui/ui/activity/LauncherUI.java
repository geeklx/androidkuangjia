package com.yuntongxun.youhui.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.yuntongxun.confwrap.WrapManager;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.plugin.common.YTXAppMgr;
import com.yuntongxun.plugin.common.YTXCASIntent;
import com.yuntongxun.plugin.common.YTXPluginUser;
import com.yuntongxun.plugin.common.RongXinApplicationContext;
import com.yuntongxun.plugin.common.YTXSDKCoreHelper;
import com.yuntongxun.plugin.common.common.YTXBackwardSupportUtil;
import com.yuntongxun.plugin.common.common.utils.ECPreferenceSettings;
import com.yuntongxun.plugin.common.common.utils.ECPreferences;
import com.yuntongxun.plugin.common.common.utils.LogUtil;
import com.yuntongxun.plugin.common.common.view.CCPCustomViewPager;
import com.yuntongxun.plugin.common.ui.AbsRongXinActivity;
import com.yuntongxun.plugin.common.ui.tools.YTXStatusBarUtil;
import com.yuntongxun.youhui.R;
import com.yuntongxun.youhui.common.helper.APPCreateHelper;
import com.yuntongxun.youhui.ui.adapter.ConfListPagerAdapter;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.List;


/**
 * 主界面
 *
 * @author 容联•云通讯
 */
public class LauncherUI extends AbsRongXinActivity implements YTXSDKCoreHelper.OnConnectStateListener {

    private static final String TAG = "LauncherUI";
    private LauncherUI mLauncherUI;

    /**
     * 应用主界面
     */
    public View mLauncherView;
    /**
     * 应用主界面导航是否初始化
     */
    private boolean mTabViewInit = false;
    /**
     * 应用滑动页
     */
    private CCPCustomViewPager mCustomViewPager;
    /**
     * 1.应用标题栏
     * 2.头部标题
     */
    private Toolbar mToolbar;

    private boolean mScrolling;
    private int currentConfPosition;

    /**
     * 延缓加载主界面
     * 1、可以优先显示欢迎界面
     * 2、可以等待界面资源初始化完成之后设置成主界面
     */
    private MessageQueue.IdleHandler mQueueIdleHandler = new MessageQueue.IdleHandler() {
        @Override
        public boolean queueIdle() {
            LogUtil.e(TAG, " quereIdle ");
            if (!mTabViewInit) {
                // 初始化
                launcherInit();
                initLauncherUI();
            }
            // 检测是否第一个用户登录，用于数据初始化加载
            checkFirstUse();
            // 加载完一次以后不在加载
            mQueueIdleHandler = null;
            return false;
        }
    };

    private SlidingTabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (mLauncherUI != null) {
            mLauncherUI.finish();
        }
        mLauncherUI = this;
        super.onCreate(savedInstanceState);
        APPCreateHelper.getHelper().initWindow(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        // 设置页面默认为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }



    @Override
    protected void onResume() {
        super.onResume();
        if (WrapManager.getInstance().checkUserEdge(LauncherUI.this,LauncherUI.class)) {
            LogUtil.v(TAG, "onResume app kickoff  " );
            return;
        }
        if (YTXAppMgr.getPluginUser() == null) {
            startActivity(new Intent(LauncherUI.this, GuidePageActivity.class));
            mLauncherUI = null;
            finish();
            return;
        }
        if (mQueueIdleHandler != null) {
            Looper.myQueue().addIdleHandler(mQueueIdleHandler);
        }
        LogUtil.i(TAG, "onResume end");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCustomViewPager = null;
        mLauncherUI = null;
    }

    /**
     * 应用启动的时候，重置应用退出设置的标识符
     */
    private void launcherUIOnCreate() {
        try {
            ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_FULLY_EXIT, false, true);
        } catch (InvalidClassException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 主界面资源的初始化操作
     */
    private void launcherInit() {
        WrapManager.getInstance().app_AutoConnect(this);
        launcherUIOnCreate();
    }


    public void onInit() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initWelcomeSelectView();
            }
        });
    }

    public boolean initWelcomeSelectView() {
        YTXPluginUser pluginUser = YTXAppMgr.getPluginUser();
        if (pluginUser != null) {
            return false;
        }
        Intent intent = new Intent(this, GuidePageActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }


    @Override
    protected boolean isEnableSwipe() {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        LogUtil.d(TAG, " onKeyDown");
        // 这里可以进行设置全局性的menu菜单的判断
        if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK)
                && event.getAction() == KeyEvent.ACTION_DOWN && mCustomViewPager != null) {
            moveTaskToBack(true);
        }
        try {
            return super.dispatchKeyEvent(event);
        } catch (Exception e) {
            LogUtil.e(TAG, "dispatch key event catch exception " + e.getMessage());
        }
        return false;
    }

    /**
     * 初始化应用主界面导航TAB
     */
    private void initLauncherUI() {
        mLauncherView = getLayoutInflater().inflate(R.layout.new_main_tab, null);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mLauncherView.findViewById(R.id.activityRootView).setPadding(mLauncherView.getPaddingLeft(),
                    YTXBackwardSupportUtil.getStatusBarHeight(this) + mLauncherView.getPaddingTop()
                    , mLauncherView.getPaddingRight(), mLauncherView.getPaddingBottom());
        }
        setContentView(mLauncherView);
        YTXStatusBarUtil.setColor(this, getResources().getColor(R.color.yhc_action_bar_color));
        mTabViewInit = true;
        initToolBar();
        mCustomViewPager = (CCPCustomViewPager) findViewById(R.id.viewpager);
        addConfViewpager();
        mCustomViewPager.setCurrentItem(0);
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.view_toolbar);
        if (tabLayout == null)
            tabLayout = (SlidingTabLayout) mToolbar.findViewById(R.id.tab_layout);
        tabLayout.setVisibility(View.VISIBLE);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setTitle("");
            findViewById(R.id.yhc_voip_call).setVisibility(View.INVISIBLE);
            mToolbar.findViewById(R.id.yhc_voip_call).setOnClickListener(v -> startActivity(new Intent(LauncherUI.this, VoipCallMemberActivity.class)));
            mToolbar.findViewById(R.id.left_setting).setOnClickListener(v -> startActivity(new Intent(LauncherUI.this, SettingConfActivity.class)));
        }
    }

    public void addConfViewpager() {
        if (tabLayout == null || mCustomViewPager == null || mToolbar == null) {
            return;
        }
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(WrapManager.getInstance().getReserveListFragment(LauncherUI.this));
        ConfListPagerAdapter pagerAdapter = new ConfListPagerAdapter(this.getSupportFragmentManager(), fragments);
        mCustomViewPager.setAdapter(pagerAdapter);
        mCustomViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LogUtil.e(TAG, "onPageScrolled  position is " + position + " positionOffset is " + positionOffset + " positionOffsetPixels is " + positionOffsetPixels);
                if (positionOffset != 0.0F) {
                    mScrolling = true;
                }
            }

            @Override
            public void onPageSelected(int position) {
                currentConfPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                LogUtil.e(TAG, "onPageScrollStateChanged state is " + state);
                if (state != ViewPager.SCROLL_STATE_IDLE) {
                    return;
                }
                mScrolling = false;
            }
        });
    }

    /**
     * 检查App是不是第一次登录客户端
     */
    private void checkFirstUse() {
        if (isFirstUse()) {
            YTXAppMgr.savePreference(ECPreferenceSettings.SETTINGS_FIRST_USE, Boolean.FALSE);
        }
    }

    /**
     * 检查App是不是第一次登录客户端
     */
    private boolean isFirstUse() {
        SharedPreferences sp = ECPreferences.getSharedPreferences();
        return sp != null && sp.getBoolean(ECPreferenceSettings.SETTINGS_FIRST_USE.getId(), (Boolean) ECPreferenceSettings.SETTINGS_FIRST_USE.getDefaultValue());
    }

    @Override
    protected boolean onDoubleTapEnable() {
        return false;
    }
    @Override
    public void onConnectSuccess() {
        dismissDialog();
        RongXinApplicationContext.sendBroadcast(YTXCASIntent.PHONE_CONF_REFRESH_LIST);
    }

    @Override
    public void onConnectFailed(ECError error) {
        if (error.errorCode == SdkErrorCode.SDK_KICKED_OFF) {
            WrapManager.getInstance().checkUserEdge(LauncherUI.this, LauncherUI.class);
        }
    }
}
