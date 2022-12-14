package com.tencent.qcloud.tim.demo.main;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;
import com.heytap.msp.push.HeytapPushManager;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.tencent.imsdk.v2.V2TIMConversationListener;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.demo.R;
import com.tencent.qcloud.tim.demo.TencentIMApp;
import com.tencent.qcloud.tim.demo.bean.CallModel;
import com.tencent.qcloud.tim.demo.bean.OfflineMessageBean;
import com.tencent.qcloud.tim.demo.profile.ProfileFragment;
import com.tencent.qcloud.tim.demo.thirdpush.HUAWEIHmsMessageService;
import com.tencent.qcloud.tim.demo.thirdpush.OPPOPushImpl;
import com.tencent.qcloud.tim.demo.thirdpush.OfflineMessageDispatcher;
import com.tencent.qcloud.tim.demo.thirdpush.ThirdPushTokenMgr;
import com.tencent.qcloud.tim.demo.utils.BrandUtil;
import com.tencent.qcloud.tim.demo.utils.DemoLog;
import com.tencent.qcloud.tim.demo.utils.PrivateConstants;
import com.tencent.qcloud.tim.demo.utils.TUIUtils;
import com.tencent.qcloud.tuicore.component.activities.BaseLightActivity;
import com.tencent.qcloud.tuicore.util.ToastUtil;
import com.tencent.qcloud.tuikit.tuicontact.ui.pages.TUIContactFragment;
import com.tencent.qcloud.tuikit.tuiconversation.ui.page.TUIConversationFragment;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseLightActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView mConversationBtn;
    private TextView mContactBtn;
    private TextView mProfileSelfBtn;
    private TextView mMsgUnread;
    private View mLastTab;

    private ViewPager2 mainViewPager;
    List<Fragment> fragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        DemoLog.e("TencentIM", "onCreate");
        super.onCreate(savedInstanceState);
        prepareThirdPushToken();
        initView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        DemoLog.e("TencentIM", "onNewIntent");
        setIntent(intent);
        prepareThirdPushToken();
    }

    private void prepareThirdPushToken() {
        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();

        if (BrandUtil.isBrandHuawei()) {
            // ??????????????????
            new Thread() {
                @Override
                public void run() {
                    try {
                        // read from agconnect-services.json
                        String appId = AGConnectServicesConfig.fromContext(MainActivity.this).getString("client/app_id");
                        String token = HmsInstanceId.getInstance(MainActivity.this).getToken(appId, "HCM");
                        DemoLog.e("TencentIM", "huawei get token:" + token);
                        if (!TextUtils.isEmpty(token)) {
                            ThirdPushTokenMgr.getInstance().setThirdPushToken(token);
                            ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
                        }
                    } catch (ApiException e) {
                        DemoLog.e(TAG, "huawei get token failed, " + e);
                    }
                }
            }.start();
        } else if (BrandUtil.isBrandVivo()) {
            // vivo????????????
            DemoLog.e("TencentIM", "vivo support push: " + PushClient.getInstance(getApplicationContext()).isSupport());
            PushClient.getInstance(getApplicationContext()).turnOnPush(new IPushActionListener() {
                @Override
                public void onStateChanged(int state) {
                    if (state == 0) {
                        String regId = PushClient.getInstance(getApplicationContext()).getRegId();
                        DemoLog.e("TencentIM", "vivopush open vivo push success regId = " + regId);
                        ThirdPushTokenMgr.getInstance().setThirdPushToken(regId);
                        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
                    } else {
                        // ??????vivo?????????????????????state = 101 ?????????vivo???????????????????????????vivo??????????????????https://dev.vivo.com.cn/documentCenter/doc/156
                        DemoLog.e("TencentIM", "vivopush open vivo push fail state = " + state);
                    }
                }
            });
        } else if (HeytapPushManager.isSupportPush()) {
            // oppo????????????
            OPPOPushImpl oppo = new OPPOPushImpl();
            oppo.createNotificationChannel(this);
            // oppo??????????????????????????????????????????init(...)????????????????????????????????????
            HeytapPushManager.init(this, false);
            HeytapPushManager.register(this, PrivateConstants.OPPO_PUSH_APPKEY, PrivateConstants.OPPO_PUSH_APPSECRET, oppo);
        }
    }

    private void initView() {
        setContentView(R.layout.main_activity);
        mConversationBtn = findViewById(R.id.conversation);
        mContactBtn = findViewById(R.id.contact);
        mProfileSelfBtn = findViewById(R.id.mine);
        mMsgUnread = findViewById(R.id.msg_total_unread);

        fragments = new ArrayList<>();
        fragments.add(new TUIConversationFragment());
        fragments.add(new TUIContactFragment());
        fragments.add(new ProfileFragment());

        mainViewPager = findViewById(R.id.view_pager);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(this);
        fragmentAdapter.setFragmentList(fragments);
        // ??????????????????????????????
        mainViewPager.setUserInputEnabled(false);
        // ?????????????????????4 ??????????????????
        mainViewPager.setOffscreenPageLimit(4);
        mainViewPager.setAdapter(fragmentAdapter);
        mainViewPager.setCurrentItem(0, false);

        if (mLastTab == null) {
            mLastTab = findViewById(R.id.conversation_btn_group);
        } else {
            // ???????????????????????????tab?????????????????????
            View tmp = mLastTab;
            mLastTab = null;
            tabClick(tmp);
            mLastTab = tmp;
        }
    }

    public void tabClick(View view) {

        DemoLog.e("TencentIM", "tabClick last: " + mLastTab + " current: " + view);
        if (mLastTab != null && mLastTab.getId() == view.getId()) {
            return;
        }
        mLastTab = view;
        resetMenuState();
        int id = view.getId();
        if (id == R.id.conversation_btn_group) {
            mainViewPager.setCurrentItem(0, false);
            mConversationBtn.setTextColor(getResources().getColor(R.color.tab_text_selected_color));
            mConversationBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.conversation_selected), null, null);
        } else if (id == R.id.contact_btn_group) {
            mainViewPager.setCurrentItem(1, false);
            mContactBtn.setTextColor(getResources().getColor(R.color.tab_text_selected_color));
            mContactBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.contact_selected), null, null);
        } else if (id == R.id.myself_btn_group) {
            if (fragments.size() != 4) {
                mainViewPager.setCurrentItem(2, false);
            } else {
                mainViewPager.setCurrentItem(3, false);
            }
            mProfileSelfBtn.setTextColor(getResources().getColor(R.color.tab_text_selected_color));
            mProfileSelfBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.myself_selected), null, null);
        }

    }

    private void resetMenuState() {
        mConversationBtn.setTextColor(getResources().getColor(R.color.tab_text_normal_color));
        mConversationBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.conversation_normal), null, null);
        mContactBtn.setTextColor(getResources().getColor(R.color.tab_text_normal_color));
        mContactBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.contact_normal), null, null);
        mProfileSelfBtn.setTextColor(getResources().getColor(R.color.tab_text_normal_color));
        mProfileSelfBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.myself_normal), null, null);
    }


    private final V2TIMConversationListener unreadListener = new V2TIMConversationListener() {
        @Override
        public void onTotalUnreadMessageCountChanged(long totalUnreadCount) {
            if (totalUnreadCount > 0) {
                mMsgUnread.setVisibility(View.VISIBLE);
            } else {
                mMsgUnread.setVisibility(View.GONE);
            }
            String unreadStr = "" + totalUnreadCount;
            if (totalUnreadCount > 100) {
                unreadStr = "99+";
            }
            mMsgUnread.setText(unreadStr);
            // ????????????????????????
            HUAWEIHmsMessageService.updateBadge(MainActivity.this, (int) totalUnreadCount);
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onStart() {
        DemoLog.e("TencentIM", "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        DemoLog.e("TencentIM", "onResume");
        super.onResume();
        registerUnreadListener();

        handleOfflinePush();
    }

    private void registerUnreadListener() {
        V2TIMManager.getConversationManager().addConversationListener(unreadListener);
        V2TIMManager.getConversationManager().getTotalUnreadMessageCount(new V2TIMValueCallback<Long>() {
            @Override
            public void onSuccess(Long aLong) {
                runOnUiThread(() -> unreadListener.onTotalUnreadMessageCountChanged(aLong));
            }

            @Override
            public void onError(int code, String desc) {

            }
        });
    }

    private void handleOfflinePush() {
        if (V2TIMManager.getInstance().getLoginStatus() == V2TIMManager.V2TIM_STATUS_LOGOUT) {
            Bundle bundle = new Bundle();
            if (getIntent() != null && getIntent().getExtras() != null) {
                bundle.putAll(getIntent().getExtras());
            }
            TUIUtils.startActivity("SplashActivity", bundle);
            finish();
            return;
        }

        final OfflineMessageBean bean = OfflineMessageDispatcher.parseOfflineMessage(getIntent());
        if (bean != null) {
            setIntent(null);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.cancelAll();
            }

            if (bean.action == OfflineMessageBean.REDIRECT_ACTION_CHAT) {
                if (TextUtils.isEmpty(bean.sender)) {
                    return;
                }
                TUIUtils.startChat(bean.sender, bean.nickname, bean.chatType);
            } else if (bean.action == OfflineMessageBean.REDIRECT_ACTION_CALL) {
                handleOfflinePushCall(bean);
            }
        }
    }

    void handleOfflinePushCall(OfflineMessageBean bean) {
        if (bean == null || bean.content == null) {
            return;
        }
        final CallModel model = new Gson().fromJson(bean.content, CallModel.class);
        DemoLog.e("TencentIM", "bean: " + bean + " model: " + model);
        if (model != null) {
            long timeout = V2TIMManager.getInstance().getServerTime() - bean.sendTime;
            if (timeout >= model.timeout) {
                ToastUtil.toastLongMessage(TencentIMApp.get().getString(R.string.call_time_out));
            } else {
                TUIUtils.startCall(bean.sender, bean.content);
            }
        }
    }

    @Override
    protected void onPause() {
        DemoLog.e("TencentIM", "onPause");
        super.onPause();
        V2TIMManager.getConversationManager().removeConversationListener(unreadListener);
    }

    @Override
    protected void onStop() {
        DemoLog.e("TencentIM", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        DemoLog.e("TencentIM", "onDestroy");
        mLastTab = null;
        super.onDestroy();
    }

}
