package com.yuntongxun.youhui.ui.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.yuntongxun.confwrap.WrapManager;
import com.yuntongxun.plugin.common.YTXAppMgr;
import com.yuntongxun.plugin.common.RongXinApplicationContext;
import com.yuntongxun.plugin.common.common.dialog.YTXRXAlertDialog;
import com.yuntongxun.plugin.common.common.helper.RXContentMenuHelper;
import com.yuntongxun.plugin.common.common.menu.YTXActionMenu;
import com.yuntongxun.plugin.common.common.utils.ECPreferences;
import com.yuntongxun.plugin.common.common.utils.LanguageSettingUtil;
import com.yuntongxun.plugin.common.ui.YHCECSuperActivity;
import com.yuntongxun.plugin.common.view.YTXConfSettingItem;
import com.yuntongxun.plugin.conference.manager.YHCConferenceMgr;
import com.yuntongxun.youhui.R;

import java.util.Locale;

import static com.yuntongxun.confwrap.WrapManager.TYPE_EXIT;
import static com.yuntongxun.confwrap.WrapManager.TYPE_LOGOUT;


/**
 * Created by gethin on 2017/9/12.
 */

public class SettingConfActivity extends YHCECSuperActivity implements View.OnClickListener {
    private YTXConfSettingItem selfVoice;
    private YTXConfSettingItem selfVideo;
    private YTXConfSettingItem otherVoice;
    private YTXConfSettingItem otherVideo;
    private YTXConfSettingItem logoutBtn;
    //    ConfSettingItem change_language;
    private YTXRXAlertDialog mLogoutDialog;
    private YTXConfSettingItem layoutMode;
    private YTXConfSettingItem yhc_layout_userinfo;


    @Override
    public int getLayoutId() {
        return R.layout.activity_conf_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarTitle(getString(R.string.yhc_conf_meeting_setting));
        initUI();
        initView();
    }

    private void initUI() {
        selfVoice =  findViewById(R.id.self_voice);
        selfVideo =  findViewById(R.id.self_video);
        otherVoice =  findViewById(R.id.other_voice);
        otherVideo =  findViewById(R.id.other_video);
        logoutBtn = findViewById(R.id.logout_btn);
        layoutMode = findViewById(R.id.yhc_layout_rs);
        yhc_layout_userinfo = findViewById(R.id.yhc_layout_userinfo);
//        change_language = (ConfSettingItem) findViewById(R.id.change_language);
        logoutBtn.isHideCheckBut(true);
        selfVoice.setOnClickListener(this);
        selfVideo.setOnClickListener(this);
        otherVoice.setOnClickListener(this);
        otherVideo.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        layoutMode.setOnClickListener(this);
        setUserInfo();
        layoutMode.isHideCheckBut(true);
        layoutMode.setVisibility(View.GONE);

        setLayoutMode();
    }

    public void setLayoutMode() {
        int mode = ECPreferences.getSharedPreferences().getInt("com.yuntongxun.conf.layout_mode", 1);
        layoutMode.setSubTitleText(mode == 1 ? "容视模式" : "有会模式");
    }

    private void setUserInfo() {
        if (yhc_layout_userinfo != null) {
            yhc_layout_userinfo.setSubTitleText(YTXAppMgr.getUserId());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        initSetting();
    }

    private void initSetting() {
        selfVoice.setCheck(YHCConferenceMgr.getManager().getSelfConfVoice());
        selfVideo.setCheck(YHCConferenceMgr.getManager().getSelfConfVideo());
        otherVoice.setCheck(YHCConferenceMgr.getManager().getOtherConfVoice());
        otherVideo.setCheck(YHCConferenceMgr.getManager().getOtherConfVideo());
    }

    private void initView() {
        selfVoice.setDoggleChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /**
                 * 加入会议后自己是否开启语音
                 * @param boolean
                 */
                YHCConferenceMgr.getManager().setSelfConfVoice(selfVoice.isChecked());
            }
        });
        selfVideo.setDoggleChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /**
                 * 加入会议后自己是否开启视频
                 * @param boolean
                 */
                YHCConferenceMgr.getManager().setSelfConfVideo(selfVideo.isChecked());
            }
        });
        otherVoice.setDoggleChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /**
                 * 加入会议后他人是否开启语音
                 * @param boolean
                 */
                YHCConferenceMgr.getManager().setOtherConfVoice(otherVoice.isChecked());
            }
        });

        otherVideo.setDoggleChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /**
                 * 加入会议后他人是否开启视频
                 * @param boolean
                 */
                YHCConferenceMgr.getManager().setOtherConfVideo(otherVideo.isChecked());
            }
        });

    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        Intent intent = new Intent(SettingConfActivity.this, SettingAudioEffectActivity.class);
        intent.putExtra(SettingAudioEffectActivity.POSITION_SELECT, 2);
        if (i == R.id.logout_btn) {
            showLogoutDialog();
        } else if (i == R.id.yhc_layout_rs) {
            switchLayoutMode();
        }
    }

    private void showLogoutDialog() {
        YTXRXAlertDialog.Builder alertBuilder = new YTXRXAlertDialog.Builder(SettingConfActivity.this);
        View logoutView = View.inflate(this, R.layout.logout_menu_view, null);
        logoutView.findViewById(R.id.menu_logout).setOnClickListener(mLogoutClickListener);
        logoutView.findViewById(R.id.menu_exit).setOnClickListener(mExitClickListener);
//        logoutView.findViewById(R.id.menu_exit).setVisibility(View.GONE);
        alertBuilder.setSubCustomView(logoutView);
        mLogoutDialog = alertBuilder.create();
        mLogoutDialog.show();
    }

    private int mExitType = 0;

    /**
     * 注销或切换至其他的账号
     */
    final private View.OnClickListener mLogoutClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //退出重新登入
            handleLogout(TYPE_LOGOUT);
        }
    };

    /**
     * 退出应用，不退出账号
     */
    final private View.OnClickListener mExitClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            handleLogout(TYPE_EXIT);
        }
    };


    /**
     * 处理应用退出账号，区分应用是否切换账号退出类型
     *
     * @param type 退出类型
     */
    private void handleLogout(int type) {
        if (mLogoutDialog != null && mLogoutDialog.isShowing()) {
            mLogoutDialog.dismiss();
        }
        showPostingDialog(R.string.posting_logout);
        WrapManager.getInstance().app_Logout(type, () -> handleLogoutReceiver());
    }


    /**
     * 处理应用退出
     */
    private void handleLogoutReceiver() {
        Intent action = new Intent();
        action.setClassName(SettingConfActivity.this, "com.yuntongxun.youhui.ui.activity.LauncherUI");
        action.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(action);
        finish();

    }

    private void showChangeLanguage() {
        RXContentMenuHelper menuHelper = new RXContentMenuHelper(this);
        menuHelper.setOnCreateActionMenuListener(new YTXActionMenu.OnCreateActionMenuListener() {
            @Override
            public void OnCreateActionMenu(YTXActionMenu menu) {
                menu.add(1, "简体中文");
                menu.add(2, "English");
            }
        });
        menuHelper.setOnActionMenuItemSelectedListener(new YTXActionMenu.OnActionMenuItemSelectedListener() {
            @Override
            public void OnActionMenuSelected(MenuItem menu, int position) {
                switch (menu.getItemId()) {
                    case 1:
//                        ECPreferences.getSharedPreferencesEditor().putString("app_language", "zh").commit();
//                        LanguageSettingUtil.changeAppLanguage(SettingConfActivity.this,Locale.CHINA);
                        LanguageSettingUtil.saveLanguageSetting(SettingConfActivity.this, Locale.CHINA);
                        reStart();
                        break;
                    case 2:
//                        ECPreferences.getSharedPreferencesEditor().putString("app_language", "en").commit();
//                        LanguageSettingUtil.changeAppLanguage(SettingConfActivity.this,Locale.US);
                        LanguageSettingUtil.saveLanguageSetting(SettingConfActivity.this, Locale.US);

                        reStart();
                        break;
                }
            }
        });
        menuHelper.show();
    }

    private void reStart() {
        Intent mStartActivity = new Intent(SettingConfActivity.this, LauncherUI.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(SettingConfActivity.this, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) RongXinApplicationContext.getContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, mPendingIntent);
        System.exit(0);
    }

    /**
     * 切换会议模式
     */
    private void switchLayoutMode() {
        RXContentMenuHelper helper = new RXContentMenuHelper(this);
        helper.setOnCreateActionMenuListener(new YTXActionMenu.OnCreateActionMenuListener() {
            @Override
            public void OnCreateActionMenu(YTXActionMenu menu) {
                menu.add(1, "容视模式");
                menu.add(2, "有会模式");
            }
        });
        helper.setOnActionMenuItemSelectedListener(new YTXActionMenu.OnActionMenuItemSelectedListener() {
            @Override
            public void OnActionMenuSelected(MenuItem menu, int position) {
                ECPreferences.getSharedPreferencesEditor().putInt("com.yuntongxun.conf.layout_mode", menu.getItemId()).apply();
                setLayoutMode();
            }
        });
        helper.show();
    }
}
