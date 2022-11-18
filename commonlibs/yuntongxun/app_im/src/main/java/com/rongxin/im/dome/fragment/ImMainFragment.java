package com.rongxin.im.dome.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rongxin.im.dome.R;
import com.rongxin.im.dome.activity.ConfActivity;
import com.rongxin.im.dome.ui.LauncherUI;
import com.rongxin.im.dome.ui.LoginActivity;
import com.yuntongxun.confwrap.WrapManager;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;
import com.rongxin.im.dome.activity.ContactActivity;
import com.rongxin.im.dome.activity.ContactsListActivity;
import com.rongxin.im.dome.activity.CreatingSingChatUI;
import com.rongxin.im.dome.activity.SettingCommonActivity;
import com.yuntongxun.plugin.common.YTXAppMgr;
import com.yuntongxun.plugin.common.RongXinApplicationContext;
import com.yuntongxun.plugin.common.common.dialog.YTXRXAlertDialog;
import com.yuntongxun.plugin.common.common.utils.ConfToasty;
import com.yuntongxun.plugin.common.common.utils.ECPreferenceSettings;
import com.yuntongxun.plugin.common.common.utils.LogUtil;
import com.yuntongxun.plugin.common.presentercore.presenter.YTXBasePresenter;
import com.yuntongxun.plugin.common.ui.CCPFragment;
import com.yuntongxun.plugin.common.ui.base.RXDialogMgr;
import com.yuntongxun.plugin.im.common.YTXBusinessNotification;
import com.yuntongxun.plugin.im.dao.bean.RXMessage;
import com.yuntongxun.plugin.im.dao.dbtools.DBECMessageTools;
import com.yuntongxun.plugin.im.manager.YTXIMPluginManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImMainFragment extends CCPFragment implements View.OnClickListener {
    private Button oneBtn, groupBtn, myChatBtn, mLogoutView, setting;
    private Button main_set_mute_btn, main_close_mute_btn, main_at_msg_btn,conf_btn;
    private Button getFriend_btn, addFriend_btn, agreeFriend_btn, deleteFriend_btn, groupAvater_btn;

    private TextView textView;
    private Button main_open_btn;

    public ImMainFragment() {
    }


    public static ImMainFragment newInstance() {
        ImMainFragment fragment = new ImMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (textView != null) {
            textView.setText("已登录：" + YTXAppMgr.getUserId());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        textView = (TextView) findViewById(R.id.tv_show_id);
        textView.setText("已登录：" + YTXAppMgr.getUserId());

        TextView version_tv = (TextView) findViewById(R.id.version_tv);
        version_tv.setText("版本号: " + RongXinApplicationContext.getVersion());

        groupBtn = (Button) findViewById(R.id.chattinggroupBtn);
        myChatBtn = (Button) findViewById(R.id.myChatting);
        oneBtn = (Button) findViewById(R.id.chattingOneBtn);
        mLogoutView = (Button) findViewById(R.id.logout);
        setting = (Button) findViewById(R.id.setting);
        main_at_msg_btn = (Button) findViewById(R.id.main_at_msg_btn);
        conf_btn = (Button) findViewById(R.id.conf_btn);
        main_open_btn = (Button) findViewById(R.id.main_open_btn);


        mLogoutView.setOnClickListener(this);
        groupBtn.setOnClickListener(this);
        oneBtn.setOnClickListener(this);
        myChatBtn.setOnClickListener(this);
        setting.setOnClickListener(this);
        main_at_msg_btn.setOnClickListener(this);
        conf_btn.setOnClickListener(this);
        main_open_btn.setOnClickListener(this);


//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(YTXSDKCoreHelper.ACTION_SDK_CONNECT);// SDK状态广播
//        intentFilter.addAction(YTXSDKCoreHelper.ACTION_KICK_OFF);// 账号异地登入广播
//        RongXinApplicationContext.registerReceiver(mSDKNotifyReceiver, intentFilter);
        /**
         * 此方法可主动调用，用于获取全部未读消息的数量
         * @return 类型：int 作用：可提示的全部未读信息的数量
         */
        ECHandlerHelper.postDelayedRunnOnUI(new Runnable() {
            @Override
            public void run() {
                int unReadMsgCount = YTXIMPluginManager.getUnReadMsgCount();
                YTXBusinessNotification.setAppBadgeCount(getContext(), null, unReadMsgCount, unReadMsgCount);
            }
        }, 20);
        YTXAppMgr.savePreference(ECPreferenceSettings.SETTINGS_SEARCHMODE, 2);//设置为仅搜索IM消息
    }


    @Override
    public int getLayoutId() {
        return R.layout.ytx_im_activity_main;
    }

    @Override
    public YTXBasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.chattingOneBtn) {//发起点对点聊天
            Intent intent1 = new Intent(getActivity(), CreatingSingChatUI.class);
            startActivity(intent1);
        } else if (id == R.id.myChatting) {//我的沟通页面
            Intent intent = new Intent(getActivity(), ContactActivity.class);
            startActivity(intent);
        } else if (id == R.id.main_open_btn) {
            Intent intent = new Intent(getActivity(), ContactsListActivity.class);
            startActivity(intent);
        }  else if (id == R.id.conf_btn) {
            Intent intent4 = new Intent(getActivity(), ConfActivity.class);
            startActivity(intent4);
        }else if (id == R.id.setting) {
            Intent intent4 = new Intent(getActivity(), SettingCommonActivity.class);
            startActivity(intent4);
        } else if (id == R.id.chattinggroupBtn) {//邀请群组并会话
            WrapManager.getInstance().createGroupUI(getActivity());
        }  else if (id == R.id.chattinggroupBtn) {//邀请群组并会话
            WrapManager.getInstance().createGroupUI(getActivity());
        }else if (id == R.id.logout) {
            WrapManager.getInstance().app_Logout(WrapManager.TYPE_LOGOUT, new ECDevice.OnLogoutListener() {
                @Override
                public void onLogout() {
                    //退出到登陆界面
                    Intent action = new Intent(getActivity(), LoginActivity.class);
                    action.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    action.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    RongXinApplicationContext.getContext().startActivity(action);
                }
            });
        } else if (id == R.id.main_at_msg_btn) {
            long st = System.currentTimeMillis();
            long et = st - 7 * 24 * 60 * 60 * 1000;
            List<RXMessage> rxMessageList = DBECMessageTools.getInstance().getAtMessages(YTXAppMgr.getUserId(), st, et);
            if (rxMessageList != null) {
                for (RXMessage rxMessage : rxMessageList) {
                    LogUtil.d(LauncherUI.class.getSimpleName(), rxMessage.toString());
                    LogUtil.d(LauncherUI.class.getSimpleName(), rxMessage.getText());
                }
                ConfToasty.success("查询到" + rxMessageList.size() + "条");
            }
        }
    }

    private void showResultDialog(String content) {
        YTXRXAlertDialog dialog = RXDialogMgr.showDialog(getActivity(), "接口返回结果",
                content, getString(R.string.app_ok), null);
        if (dialog != null) {
            dialog.show();
        }
    }

}