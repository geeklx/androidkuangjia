///*
// *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
// *
// *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
// *  that can be found in the LICENSE file in the root of the web site.
// *
// *   http://www.yuntongxun.com
// *
// *  An additional intellectual property rights grant can be found
// *  in the file PATENTS.  All contributing project authors may
// *  be found in the AUTHORS file in the root of the source tree.
// */
//package com.geek.appcommon.ytx.im;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Rect;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.ContextMenu;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import androidx.localbroadcastmanager.content.LocalBroadcastManager;
//
//import com.alibaba.fastjson.JSONObject;
//import com.blankj.utilcode.util.ToastUtils;
//import com.geek.common.R;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.yuntongxun.ecsdk.ECDevice;
//import com.yuntongxun.ecsdk.ECDeviceType;
//import com.yuntongxun.ecsdk.ECError;
//import com.yuntongxun.ecsdk.ECMultiDeviceState;
//import com.yuntongxun.ecsdk.ECUserState;
//import com.yuntongxun.ecsdk.SdkErrorCode;
//import com.yuntongxun.ecsdk.im.ECGroup;
//import com.yuntongxun.ecsdk.im.ECGroupOption;
//import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;
//import com.yuntongxun.plugin.common.YTXAppMgr;
//import com.yuntongxun.plugin.common.YTXCASIntent;
//import com.yuntongxun.plugin.common.RXConfig;
//import com.yuntongxun.plugin.common.RongXinApplicationContext;
//import com.yuntongxun.plugin.common.YTXSDKCoreHelper;
//import com.yuntongxun.plugin.common.adapter.YTXAbsAdapter;
//import com.yuntongxun.plugin.common.common.YTXBackwardSupportUtil;
//import com.yuntongxun.plugin.common.common.helper.RXContentMenuHelper;
//import com.yuntongxun.plugin.common.common.menu.YTXActionMenu;
//import com.yuntongxun.plugin.common.common.utils.DemoUtils;
//import com.yuntongxun.plugin.common.common.utils.ECPreferenceSettings;
//import com.yuntongxun.plugin.common.common.utils.ECPreferences;
//import com.yuntongxun.plugin.common.common.utils.GlideHelper;
//import com.yuntongxun.plugin.common.common.utils.LogUtil;
//import com.yuntongxun.plugin.common.common.utils.TextUtil;
//import com.yuntongxun.plugin.common.common.utils.ToastUtil;
//import com.yuntongxun.plugin.common.common.utils.UserData;
//import com.yuntongxun.plugin.common.common.utils.WXShareUtils;
//import com.yuntongxun.plugin.common.link.RxAppLinkHelper;
//import com.yuntongxun.plugin.common.presentercore.presenter.YTXBasePresenter;
//import com.yuntongxun.plugin.common.ui.YTXARouterPathUtil;
//import com.yuntongxun.plugin.common.ui.YTXUITabFragment;
//import com.yuntongxun.plugin.common.ui.setting.YTXCustomSettingUtils;
//import com.yuntongxun.plugin.common.ui.tools.YTXListViewUtils;
//import com.yuntongxun.plugin.common.view.YTXRoundImageView;
//import com.yuntongxun.plugin.common.view.draggable.YTXDraggableFlagView;
//import com.yuntongxun.plugin.common.view.drawable.YTXWaterMark;
//import com.yuntongxun.plugin.common.view.drawable.YTXWaterMarkUtils;
//import com.yuntongxun.plugin.emoji.YTXCCPTextView;
//import com.yuntongxun.plugin.im.common.YTXCustomIntent;
//import com.yuntongxun.plugin.im.common.YTXCustomMsgItem;
//import com.yuntongxun.plugin.im.dao.bean.RXConversation;
//import com.yuntongxun.plugin.im.dao.bean.RXUserSetting;
//import com.yuntongxun.plugin.im.dao.dbtools.DBECGroupTools;
//import com.yuntongxun.plugin.im.dao.dbtools.DBRXConversationTools;
//import com.yuntongxun.plugin.im.dao.dbtools.DBRXGroupNoticeTools;
//import com.yuntongxun.plugin.im.dao.dbtools.DBRXUserSettingTools;
//import com.yuntongxun.plugin.im.housekeeper.ui.YTXHouseKeeperListActivity;
//import com.yuntongxun.plugin.im.manager.YTXIMPluginManager;
//import com.yuntongxun.plugin.im.manager.YTXMiniAppHelper;
//import com.yuntongxun.plugin.im.manager.YTXOnUpdateMsgUnreadCountsListener;
//import com.yuntongxun.plugin.im.net.YTXIMRequestUtils;
//import com.yuntongxun.plugin.im.presentercore.presenter.YTXChattingPresenter;
//import com.yuntongxun.plugin.im.ui.YTXCCPListAdapter;
//import com.yuntongxun.plugin.im.ui.YTXChattingContextMenuId;
//import com.yuntongxun.plugin.im.ui.chatting.YTXMultiLoginActivity;
//import com.yuntongxun.plugin.im.ui.chatting.fragment.YTXToDoListMsgDialogFragment;
//import com.yuntongxun.plugin.im.ui.chatting.helper.YTXIMChattingHelper;
//import com.yuntongxun.plugin.im.ui.conversation.YTXConversationAdapter;
//import com.yuntongxun.plugin.im.ui.conversation.YTXNetRemindActivity;
//import com.yuntongxun.plugin.im.ui.group.YTXGroupNoticeHelper;
//import com.yuntongxun.plugin.im.ui.group.model.YTXGroupService;
//import com.yuntongxun.plugin.im.ui.notify.YTXMsgNotifyHelper;
//import com.yuntongxun.plugin.im.view.YTXLoginRemindView;
//import com.yuntongxun.plugin.im.view.YTXNetWarnBannerView;
//
//import java.io.InvalidClassException;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//
///**
// * 会话界面 Created by Jorstin on 2015/3/18.
// */
//public class YTXConversationListFragment extends YTXUITabFragment implements YTXActionMenu.OnActionMenuItemSelectedListener,
//        YTXCCPListAdapter.OnListAdapterCallBackListener {
//
//    private static final String TAG = "RongXin.ConversationListFragment";
//
//    /**
//     * 会话消息列表ListView
//     */
//    private ListView mListView;
//    private View mSearchPlaceholder;
//    private View im_msg_bus_head;
//    private YTXNetWarnBannerView mBannerView;
//    private YTXNetWarnBannerView mBottomBannerView;
//    private YTXLoginRemindView mLoginRemindView;
//    private YTXLoginRemindView mBottomLoginRemindView;
//    private YTXConversationAdapter mAdapter;
//    private YTXOnUpdateMsgUnreadCountsListener mAttachListener;
//    private View emptyView;
//    private RXContentMenuHelper mMenuHelper;
//    private String currentDeviceName = null;
//    private HashMap<String, String> isOnline;
//
//    //搜索下面副标题一栏
//    private TextView im_all_msg_tv;
//    private View im_collect_msg_fy;
//    private ImageView im_collect_msg_iv;
//    private View im_at_msg_fy;
//    private ImageView im_at_msg_iv;
//    private View im_todolist_msg_fy;
//    private ImageView im_todolist_msg_iv;
//    private View im_file_fy;
//    private ImageView im_file_iv;
//
//    final private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
//
//        @Override
//        public void onItemClick(AdapterView<?> parent, View visew,
//                                int position, long id) {
//
//            if (mAdapter != null) {
//                int headerViewsCount = mListView.getHeaderViewsCount();
//                if (position < headerViewsCount) {
//                    return;
//                }
//                int _position = position - headerViewsCount;
//
//                if (mAdapter == null || mAdapter.getItem(_position) == null) {
//                    return;
//                }
//                //每一条联系消息的实体类，可以获取到未读消息条数之类的
//                RXConversation conversation = mAdapter.getItem(_position);
//                if (DBRXGroupNoticeTools.CONTACT_ID.equals(conversation.getSessionId())) {
//                    //群组系统消息
//                    Intent intent = new Intent();
//                    intent.setClassName(getActivity(), "com.yuntongxun.rongxin.ui.group.GroupNoticeActivity");
//                    startActivity(intent);
//                    return;
//                }
//                if (UserData.NEW_FRIEND_SENDER.equals(conversation.getSessionId())) {
//                    //新联系人消息
//                    Intent intent = new Intent();
//                    intent.setClassName(getActivity(), "com.yuntongxun.rongxin.ui.friend.NewFriendsActivity");
//                    startActivity(intent);
//                    return;
//                }
//                if (YTXIMChattingHelper.FILE_TRANSFER_SESSION_ID.equals(conversation.getSessionId())) {
//                    YTXIMPluginManager.getManager().startSingChat(getContext(), /*AppMgr.getUserId()*/"~ytxfa", true);
//                    return;
//                }
//                if (YTXIMChattingHelper.OFFICIAL_ACCOUNT_SESSION_ID.equals(conversation.getSessionId())) {
//                    Intent intent = RxAppLinkHelper.getInstance(getContext(), YTXARouterPathUtil.pathOfficialAccountSessionListActivity).getIntent();
//                    if (presenter != null) {
//                        presenter.handleSendReadMessage(conversation.getSessionId());
//                    }
//                    startActivity(intent);
//                    return;
//                } else if (YTXIMChattingHelper.GROUP_HELPER_ID.equals(conversation.getSessionId())) {
//                    YTXIMPluginManager.getManager().startSingChat(getContext(), conversation.getSessionId(), getString(R.string.ytx_group_helper), false);
//                    return;
//                } else if (YTXIMChattingHelper.ATTEN_DANCE_SESSION_ID.equals(conversation.getSessionId())) {
//                    Intent intent = new Intent();
//                    intent.setClassName(getContext(), "com.yuntongxun.plugin.workattendance.AttenDanceMsgListActivity");
//                    startActivity(intent);
//                    return;
//                } else if (YTXIMChattingHelper.MONITOR_PLATFORM_SESSION_ID.equals(conversation.getSessionId())) {
//                    Intent intent = new Intent();
//                    intent.setClassName(getContext(), "com.yuntongxun.plugin.monitor.MonitorMessageListActivity");
//                    startActivity(intent);
//                    return;
//                } else if (conversation.getSessionId().startsWith(YTXIMChattingHelper.MINIAPP_PUSH_ACCOUNT)) {
//                    //判断是否需要验证密码锁
//                    if (conversation.getSessionId().contains(YTXIMChattingHelper.MINIAPP_PUSH_ACCOUNT + "dianpiaoxitong")) {
//                        YTXMiniAppHelper.getInstance().miniAppPwdLockAuth(YTXConversationListFragment.this, conversation.getSessionId() + "", 1);
//                        return;
//                    }
//                    YTXMiniAppHelper.getInstance().startOpenWorkUI(YTXConversationListFragment.this, conversation.getSessionId() + "", 1);
//                    return;
//                } else if (YTXMsgNotifyHelper.getInstance().isMsgNotify(conversation.getSessionId())) {
//                    YTXMsgNotifyHelper.getInstance().startActivity(getContext(), conversation.getSessionId(), conversation.getUsername());
//                    return;
//                } else if (conversation.getType() == 1) {
//
//                    Intent intent = RxAppLinkHelper.getInstance(getContext(), YTXARouterPathUtil.pathOfficialAccountMainMsgListActivity).getIntent();
//
//                    intent.putExtra("pn_id", conversation.getSessionId());
//                    intent.putExtra("pn_name", conversation.getUsername());
//                    startActivity(intent);
//                    return;
//                } else if (conversation.getSessionId().equals(YTXIMChattingHelper.MEETING_NEWS_SESSION_ID)) {
//                    Intent intent = new Intent();
//                    intent.setClassName(getContext(), "com.yuntongxun.plugin.meeting.news.ui.MeetingNewsListActivity");
//                    startActivity(intent);
//                    return;
//                } else if (conversation.getSessionId().equals(YTXIMChattingHelper.HOUSE_KEEPER_SESSION_ID)) {
//                    Intent intent = new Intent(getContext(), YTXHouseKeeperListActivity.class);
//                    intent.putExtra("sid", conversation.getId());
//                    startActivity(intent);
//                    return;
//                } else if (conversation.getSessionId().equals(YTXIMChattingHelper.OA_SESSION_ID)) {
//
//                    RxAppLinkHelper.getInstance(getActivity(), YTXARouterPathUtil.pathOAListActivity)
//                            .putExtra("oa_sid", conversation.getId())
//                            .startActivity(getActivity(),0,0);
//                    return;
//                } else if (conversation.getSessionId().equals(YTXIMChattingHelper.EHR_SESSION_ID)) {
//
//                    RxAppLinkHelper.getInstance(getActivity(), YTXARouterPathUtil.pathEHRListActivity)
//                            .putExtra("ehr_sid", conversation.getId())
//                            .startActivity(getActivity(),0,0);
//                    return;
//                }
//                //跳转到聊天界面
//                YTXIMChattingHelper.getInstance().startConversationActivity(getActivity(), conversation.getSessionId(), conversation.getUsername(), conversation.getUnreadCount(), isOnline.get(conversation.getSessionId()));
//            }
//        }
//    };
//
//    private final AdapterView.OnItemLongClickListener mOnLongClickListener = new AdapterView.OnItemLongClickListener() {
//        @Override
//        public boolean onItemLongClick(AdapterView<?> parent, View view,
//                                       int position, long id) {
//
//            if (position < mListView.getHeaderViewsCount()) {
//                LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "on long click header view");
//                return true;
//            }
//            mMenuHelper.registerForContextMenu(view, position, id, YTXConversationListFragment.this, YTXConversationListFragment.this);
//            return true;
//        }
//    };
//    private CustomReceiver customReceiver;
//    private View ytx_conversation_custom_msg;
//    private YTXRoundImageView custom_avatar_iv;
//    private TextView timeTv;
//    private TextView nickName;
//    private YTXCCPTextView lastMsg;
//    private YTXDraggableFlagView tipcnt_tv;
//    private ImageView prospect_iv;
//    private ImageView image_mute_iv;
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        initView();
////        getMultiDeviceStatus();
//        registerReceiver(new String[]{
//                YTXCASIntent.ACTION_SYNC_GROUP,
//                YTXCASIntent.ACTION_CONTACT_SYNC_COMPLETE,
//                YTXCASIntent.ACTION_SESSION_DEL,
//                YTXSDKCoreHelper.ACTION_SDK_CONNECT,
//                YTXCASIntent.DOWNLOAD_ENTERPRISE_SUCCESS,
//                YTXCASIntent.ACTION_SESSION_CHANGE,
//                YTXCASIntent.ACTION_MULTI_DEVICE_STATE,
//                YTXCASIntent.ACTION_MULTI_DEVICE_ONOFFLINE,
//                YTXCASIntent.ACTION_MULTI_DEVICE_LOGIN_OUT,
//                YTXCASIntent.ACTION_STICKY_ON_TOP,
//                YTXCASIntent.ACTION_SET_STICKY_ON_TOP,
//                YTXCASIntent.ACTION_MSG_NOTICE_SET_MUTE,
//                YTXCASIntent.ACTION_INIT_BATCH_EMEPLOYEE_INFO,
//                YTXCASIntent.ACTION_USER_STATE,
//                YTXCASIntent.DOWNLOAD_WARTER_SUCCESS,
//                //群删除广播
//                YTXCASIntent.ACTION_GROUP_DEL,
//                YTXCASIntent.ACTION_MSG_BUS_ICON_SWITCH
//        });
//
//        LogUtil.d(TAG, "ConversationListFragment onCreate...");
//        //注册监听
//        customReceiver = new CustomReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(YTXCustomIntent.ACTION_SHOW_CUSTOM_MSG);
//        intentFilter.addAction(YTXCustomIntent.ACTION_HIDE_CUSTOM_MSG);
//        intentFilter.addAction(YTXCustomIntent.ACTION_UPDATE_CUSTOM_MSG);
//        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(customReceiver,intentFilter);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        DBRXConversationTools.getInstance().registerObserver(mAdapter);
//        mAdapter.notifyChange();
//        ECHandlerHelper.postDelayedRunnOnUI(new Runnable() {
//            @Override
//            public void run() {
//                getMeetingInfoByUser();
//            }
//        }, 800);
//    }
//
//
//    @Override
//    public void onPause() {
//        super.onPause();
////        DBRXConversationTools.getInstance().unregisterObserver(mAdapter);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        DBRXConversationTools.getInstance().unregisterObserver(mAdapter);
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        try {
//            // 注销广播监听器
//            RongXinApplicationContext.unregisterReceiver(customReceiver);
//        } catch (Exception e) {
//        }
//
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof YTXOnUpdateMsgUnreadCountsListener) {
//            mAttachListener = (YTXOnUpdateMsgUnreadCountsListener) context;
//        }
//    }
//
//    @Override
//    protected void lazyLoad() {
//        super.lazyLoad();
//        if (mMenuHelper != null) {
//            mMenuHelper.dismiss();
//        }
//        if (!YTXAppMgr.isActive()) {
//            return;
//        }
////            updateOnline();
//            getSubscribeUser();
//            getVoipUserInfo();
//            getMultiDeviceStatus();
//            YTXIMChattingHelper.getInstance().reSetStickyTopStatus("3");
//    }
//
//
//
//
////    /**
////     * 订阅或者取消用户
////     */
////    private void getSubscribeUser() {
////        String[] account = DBRXConversationTools.getInstance().getRecentCursorSingleAccount();
//////        if (account != null && account.length > 100) {
//////            ToastUtil.showMessage("最多订阅用户100人");
//////            return;
//////        }
////        IMRequestUtils.setSubScribeUser(RXConfig.REST_HOST, RXConfig.APP_ID,
////                RXConfig.APP_TOKEN, AppMgr.getUserId(), "0", account, new Callback<JSONObject>() {
////                    @Override
////                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
////                        if (response.isSuccessful() && response.body().get("statusCode").equals("000000")) {
////                            updateOnline();
////                        }
////
////                    }
////
////                    @Override
////                    public void onFailure(Call<JSONObject> call, Throwable t) {
////                    }
////                });
////    }
//
//
//    private List<String> orignSubscribeList;
//
//
//    private void newMessageComing(String tempSessionId) {
//        if (!isSingleAccoutSessionId(tempSessionId)) {
//            return;
//        }
//        if (orignSubscribeList == null) {
//            return;
//        }
//        if (orignSubscribeList.contains(tempSessionId)) {
//            return;
//        }
//        String[] account = DBRXConversationTools.getInstance().getRecentCursorSingleAccount();
//        List<String> accounList = Arrays.asList(account);
//        orignSubscribeList = accounList;
//        if (account != null) {
//            if (account.length < MAX_HANDLE_NUM) {
//                subscribeUser(account);
//            } else {
//                caculateBatchHandle(accounList);
//            }
//        }
//    }
//
//
//    /**
//     * 订阅或者取消用户
//     */
//    private void getSubscribeUser() {
//        String[] account = DBRXConversationTools.getInstance().getRecentCursorSingleAccount();
//        List<String> accounList = Arrays.asList(account);
//        orignSubscribeList = accounList;
//
//        if (account != null) {
//            if (account.length < MAX_HANDLE_NUM) {
//                subscribeUser(account);
//            } else {
//                caculateBatchHandle(accounList);
//            }
//        }
//    }
//
//
//    private boolean isSingleAccoutSessionId(String tempSessionId) {
//
//        //过滤空消息
//        if (TextUtil.isEmpty(tempSessionId)) {
//            return false;
//        }
//        //过滤群组消息消息
//        if (tempSessionId.toUpperCase().startsWith("G")) {
//            return false;
//        }
//
//        //过滤服务号通知消息
//        if (tempSessionId.equals(YTXIMChattingHelper.OFFICIAL_ACCOUNT_SESSION_ID)) {
//            return false;
//        }
//
//        //过滤集成监控通知消息
//        if (tempSessionId.equals(YTXIMChattingHelper.MONITOR_PLATFORM_SESSION_ID)) {
//            return false;
//        }
//
//        //过滤考勤通知消息
//        if (tempSessionId.equals(YTXIMChattingHelper.ATTEN_DANCE_SESSION_ID)) {
//            return false;
//        }
//
//        //过滤文件助手通知消息
//        if (tempSessionId.equals(YTXIMChattingHelper.FILE_TRANSFER_SESSION_ID)) {
//            return false;
//        }
//
//        return true;
//
//    }
//
////    private void getVoipUserBySessionID(String sessonid) {
////        if (!isSingleAccoutSessionId(sessonid)) {
////            return;
////        }
////        RXEmployee rxEmployee = IMPluginManager.getManager().queryEmployeeByAccount(sessonid);
////        if (rxEmployee != null) {
////            return;
////        }
////        getVoipUserInfo();
////    }
//
//    private final static int MAX_HANDLE_NUM = 100;
//
//    /**
//     * 分批处理
//     *
//     * @param dataList
//     */
//    public void caculateBatchHandle(List<String> dataList) {
//        int pointsDataLimit = MAX_HANDLE_NUM;
//        List<String> newList = new ArrayList<String>();
//        for (int i = 0; i < dataList.size(); i++) {//分批次处理
//            newList.add(dataList.get(i));
//
//            if (pointsDataLimit == newList.size() || i == dataList.size() - 1) {
//                arrayConvertString(newList);
//                newList.clear();
//            }
//        }
//    }
//
//    /**
//     * array转成接口需要的int[]
//     *
//     * @param subList
//     */
//    private void arrayConvertString(List<String> subList) {
//
//        if (subList != null) {
//            String[] accountArray = new String[subList.size()];
//            for (int i = 0; i < subList.size(); i++) {
//                accountArray[i] = subList.get(i);
//            }
//            subscribeUser(accountArray);
//        }
//    }
//
//    /**
//     * 订阅用户
//     *
//     * @param account
//     */
//    private void subscribeUser(String[] account) {
//        LogUtil.e(TAG, "subscribeUser");
//        if (account == null || account.length == 0) {
//            LogUtil.e(TAG, "subscribeUser account length is 0");
//            return;
//        }
//        updateOnline();
//        //避免静态变量有时初始化为空
//        String REST_HOST = ECPreferences.getSharedPreferences().getString(RXConfig.CONFIG_REST_HOST, "");
//        String APP_ID = ECPreferences.getSharedPreferences().getString(RXConfig.CONFIG_APPID, "");
//        String APP_TOKEN = ECPreferences.getSharedPreferences().getString(RXConfig.CONFIG_APPTOKEN, "");
//        YTXIMRequestUtils.setSubScribeUser(REST_HOST, APP_ID,
//                APP_TOKEN, YTXAppMgr.getUserId(), "0", account, new Callback<JSONObject>() {
//                    @Override
//                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
//                        if (response.isSuccessful() && response.body().get("statusCode").equals("000000")) {
////                            updateOnline();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<JSONObject> call, Throwable t) {
//                    }
//                });
//    }
//
//
//    private void setAccountToMap(String[] account) {
//        if (null != isOnline) {
//            isOnline.clear();
//        } else {
//            isOnline = new HashMap<>();
//        }
//        for (int i = 0; i < account.length; i++) {
//            isOnline.put(account[i], "0");
//        }
//    }
//
//    private void updateOnline() {
//        final String[] account = DBRXConversationTools.getInstance().getRecentCursorSingleAccount();
//        if (account == null) {
//            return;
//        }
//        ECDevice.getUsersState(account, new ECDevice.OnGetUsersStateListener() {
//            @Override
//            public void onGetUsersState(ECError ecError, ECUserState... ecUserStates) {
//                LogUtil.i(TAG, "initUserState " + ecError.errorCode);
//                setAccountToMap(account);
//                if (ecError.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
//                    if (ecUserStates != null && ecUserStates.length == 0) {
//                        for (int i = 0; i < account.length; i++) {
//                            isOnline.put(account[i], "0");
//                        }
//                        mAdapter.notifyChange();
//                        return;
//                    }
//                    if (ecUserStates.length != 0 && account.length != ecUserStates.length) {
//                        List<String> accoutList = Arrays.asList(account);
//                        for (ECUserState ecUserState : ecUserStates) {
//                            if (accoutList.contains(ecUserState.getUserId())) {//在线
//                                String subTile = DemoUtils.getDeviceWithType(ecUserState.getDeviceType())
//                                        + DemoUtils.getNetWorkWithType(ecUserState.getNetworkType());
//                                isOnline.put(ecUserState.getUserId(), subTile);
//                            } else {
//                                isOnline.put(ecUserState.getUserId(), "0");
//                            }
//                        }
//                        mAdapter.notifyChange();
//                        return;
//                    }
//                    if (ecUserStates.length != 0 && account.length == ecUserStates.length) {
//                        for (ECUserState ecUserState : ecUserStates) {
//                            if (ecUserState.isOnline()) {
//                                String subTile = DemoUtils.getDeviceWithType(ecUserState.getDeviceType())
//                                        + DemoUtils.getNetWorkWithType(ecUserState.getNetworkType());
//                                isOnline.put(ecUserState.getUserId(), subTile);
//                            } else {
//                                isOnline.put(ecUserState.getUserId(), "0");
//                            }
//                        }
//                        mAdapter.notifyChange();
//                        return;
//                    }
//
//                }
//            }
//        });
//    }
//
//
//    /**
//     * 监听会话列表变化 当有新的聊天会话进入会话列表时 重新订阅
//     */
//    private void setDataBaseChangeListener() {
//        if (mAdapter != null) {
//            mAdapter.setOnCursorChangeListener(new YTXAbsAdapter.OnCursorChangeListener() {
//                @Override
//                public void onCursorChangeBefore(String sessionId) {
//                }
//
//                @Override
//                public void onCursorChangeAfter(String sessionId) {
//                    LogUtil.e(TAG, "onCursorChangeAfter");
//                    newMessageComing(sessionId);
////                    getVoipUserBySessionID(sessionId);
//                }
//            });
//
//        }
//
//    }
//
//
//    private void getVoipUserInfo() {
//        RongXinApplicationContext.sendBroadcast(YTXCASIntent.ACTION_CONTACT_SYNC);
//    }
//
//    /**
//     * 初始化View
//     */
//    private void initView() {
//        if (mListView != null) {
//            mListView.setAdapter(null);
//            if (mBannerView != null) {
//                mListView.removeHeaderView(mBannerView);
//            }
//            if (mSearchPlaceholder != null) {
//                mListView.removeHeaderView(mSearchPlaceholder);
//            }
////            if (mSearchPlaceholder != null) {
////                mListView.removeHeaderView(mSearchPlaceholder);
////            }
//            if (mLoginRemindView != null) {
//                mListView.removeHeaderView(mLoginRemindView);
//            }
//        }
//        isOnline = new HashMap<>();
//        mListView = (ListView) findViewById(R.id.pull_refresh_list);
//        //水印
//        YTXWaterMark waterMark = YTXWaterMarkUtils.getHighWaterMark(getContext(), getResources().getColor(R.color.water_mark_low_bg), getResources().getColor(R.color.water_mark_low_content), (int) getResources().getDimension(R.dimen.size_big), (int) getResources().getDimension(R.dimen.watermark_hor_padding), (int) getResources().getDimension(R.dimen.watermark_ver_padding));
//        if (waterMark != null) {
//            mListView.setBackground(waterMark.getBitmapDrawable());
//        }
//        mMenuHelper = new RXContentMenuHelper(getActivity());
//        mListView.setDrawingCacheEnabled(false);
//        mListView.setScrollingCacheEnabled(false);
//        emptyView = findViewById(R.id.empty_conversation_tv);
//        mBottomBannerView = (YTXNetWarnBannerView) findViewById(R.id.connect_view);
//        mBottomBannerView.setVisibility(View.GONE);
//        mBottomBannerView.hideWarnBannerView();
//        mBottomLoginRemindView = (YTXLoginRemindView) findViewById(R.id.login_remind);
//        mListView.setEmptyView(emptyView);
//        mListView.setOnItemLongClickListener(mOnLongClickListener);
//        mListView.setOnItemClickListener(mItemClickListener);
//        if (mLoginRemindView == null) {
//            mLoginRemindView = new YTXLoginRemindView(getContext());
//            mLoginRemindView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (TextUtils.isEmpty(currentDeviceName)) {
//                        return;
//                    }
//                    Intent intent = new Intent(getContext(), YTXMultiLoginActivity.class);
//                    intent.putExtra("deviceName", currentDeviceName);
//                    startActivity(intent);
//                }
//            });
//        }
//        if (mBannerView == null) {
//            mBannerView = new YTXNetWarnBannerView(getContext());
//            mBannerView.setVisibility(View.GONE);
//            mBannerView.hideWarnBannerView();
//            mBannerView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    reTryConnect();
//                }
//            });
//        }
//        if (mSearchPlaceholder == null) {
//            mSearchPlaceholder = LayoutInflater.from(getContext()).inflate(R.layout.ytx_conversation_search_holder2, null);
//            mSearchPlaceholder.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    doSearchAction(v);
//                }
//            });
//        }
//        // TODO: 2017/7/27 添加头部在setadapter前添加，适用于api 17
////        mListView.addHeaderView(mSearchPlaceholder);
////        mListView.addHeaderView(mSearchPlaceholder);
////        if (im_msg_bus_head == null){
////            im_msg_bus_head = LayoutInflater.from(getContext()).inflate(R.layout.ytx_conversation_head_3, null);
////            initMsgBusView(im_msg_bus_head);
////
////        }
//        initMsgBusView();
//
//        mListView.addHeaderView(mBannerView);
//        mListView.addHeaderView(mLoginRemindView);
//        mAdapter = new YTXConversationAdapter(this.getActivity(), isOnline, this);
//        mListView.setAdapter(mAdapter);
//        presenter = new YTXChattingPresenter();
//
//        registerForContextMenu(mListView);
//
//        findViewById(R.id.search_ll).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doSearchAction(v);
//            }
//        });
//        mBottomBannerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                reTryConnect();
//            }
//        });
//        mBottomLoginRemindView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (TextUtils.isEmpty(currentDeviceName)) {
//                    return;
//                }
//                Intent intent = new Intent(getContext(), YTXMultiLoginActivity.class);
//                intent.putExtra("deviceName", currentDeviceName);
//                startActivity(intent);
//            }
//        });
//
//        ytx_conversation_custom_msg = LayoutInflater.from(requireContext()).inflate(R.layout.ytx_custom_msg_item, null);
//
//        ytx_conversation_custom_msg.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                mMenuHelper.registerForContextMenu(ytx_conversation_custom_msg, -1, ytx_conversation_custom_msg.getId(), YTXConversationListFragment.this, YTXConversationListFragment.this);
//                return true;
//            }
//        });
//
//        ytx_conversation_custom_msg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                YTXIMPluginManager.YTXOnCustomClickListener ytxOnCustomClickListener = YTXIMPluginManager.getManager().getYtxOnCustomClickListener();
//                if (ytxOnCustomClickListener!=null){
//                    ytxOnCustomClickListener.onItemClick();
//                }
//            }
//        });
//        custom_avatar_iv = ytx_conversation_custom_msg.findViewById(R.id.custom_avatar_iv);
//        timeTv = ytx_conversation_custom_msg.findViewById(R.id.custom_update_time_tv);
//        nickName = ytx_conversation_custom_msg.findViewById(R.id.custom_nickname_tv);
//        lastMsg = ytx_conversation_custom_msg.findViewById(R.id.custom_last_msg_tv);
//        tipcnt_tv = ytx_conversation_custom_msg.findViewById(R.id.custom_tipcnt_tv);
//        prospect_iv = ytx_conversation_custom_msg.findViewById(R.id.custom_avatar_prospect_iv);
//        image_mute_iv = ytx_conversation_custom_msg.findViewById(R.id.custom_image_mute);
//        setDataBaseChangeListener();
//
//
//    }
//
//    class CustomReceiver extends BroadcastReceiver{
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            switch (action){
//                case YTXCustomIntent.ACTION_HIDE_CUSTOM_MSG:
//                    if (ytx_conversation_custom_msg!=null) {
//                        mListView.removeHeaderView(ytx_conversation_custom_msg);
//                    }
//                    break;
//                case YTXCustomIntent.ACTION_SHOW_CUSTOM_MSG:
//                    int headerViewsCount = mListView.getHeaderViewsCount();
//                    if (headerViewsCount < 3){
//                        mListView.addHeaderView(ytx_conversation_custom_msg,null,false);
//                    }
//
//                    break;
//                case YTXCustomIntent.ACTION_UPDATE_CUSTOM_MSG:
//                    YTXCustomMsgItem ytxCustomMsgItem = (YTXCustomMsgItem) intent.getSerializableExtra(YTXIMPluginManager.YTXCustomMsgItem);
//                    int unReadNum = ytxCustomMsgItem.getUnReadNum();
//                    boolean notice = ytxCustomMsgItem.isNotice();
//                    YTXCustomMsgItem.YTXCustomMsg msg = ytxCustomMsgItem.getMsg();
//                    String avatarUrl = msg.getAvatarUrl();
//                    String title = msg.getTitle();
//                    String content = msg.getContent();
//                    String time = msg.getTime();
//
//                    GlideHelper.displayNormalPhoto(context, avatarUrl, custom_avatar_iv, R.mipmap.ytx_icon_app_official_account);
//                    nickName.setText(title);
//                    lastMsg.setText(content);
//                    timeTv.setText(time);
//                    setConversationUnread(unReadNum,notice);
//                    break;
//
//                default:
//                    break;
//
//            }
//
//        }
//    }
//    /**
//     * 设置未读图片显示规则
//     */
//    private void setConversationUnread(int count, boolean notice) {
//        String msgCount = count > 99 ? "99+" : String.valueOf(count);
//         tipcnt_tv.setText(msgCount);
//         tipcnt_tv.setEnableDraggable(false);
//         image_mute_iv.setVisibility(notice? View.GONE : View.VISIBLE);
//
//        if (count == 0) {
//            tipcnt_tv.setVisibility(View.GONE);
//            prospect_iv.setVisibility(View.GONE);
//        } else if (notice) {
//            tipcnt_tv.setVisibility(View.VISIBLE);
//            prospect_iv.setVisibility(View.GONE);
//        } else {
//            prospect_iv.setVisibility(View.VISIBLE);
//            tipcnt_tv.setVisibility(View.GONE);
//        }
//    }
//
//    private YTXToDoListMsgDialogFragment toDoListMsgDialogFragment;
//
//    /**
//     * 消息直通车相关功能
//     */
//    private void initMsgBusView() {
//        im_all_msg_tv = (TextView) findViewById(R.id.im_all_msg_tv);
//
//        im_collect_msg_fy = findViewById(R.id.im_collect_msg_fy);
//        im_collect_msg_iv = (ImageView) findViewById(R.id.im_collect_msg_iv);
//
//        im_at_msg_fy = findViewById(R.id.im_at_msg_fy);
//        im_at_msg_iv = (ImageView) findViewById(R.id.im_at_msg_iv);
//
//        im_todolist_msg_fy = findViewById(R.id.im_todolist_msg_fy);
//        im_todolist_msg_iv = (ImageView) findViewById(R.id.im_todolist_msg_iv);
//
//        im_file_fy = findViewById(R.id.im_file_fy);
//        im_file_iv = (ImageView) findViewById(R.id.im_file_iv);
//
//        //默认选中全部
//        setSubTitleStatus(0);
//
//        im_collect_msg_fy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setSubTitleStatus(1);
//            }
//        });
//
//        im_at_msg_fy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toDoListMsgDialogFragment = YTXToDoListMsgDialogFragment.newInstance(2);
//                toDoListMsgDialogFragment.show(getFragmentManager(), "ToDoListMsgDialogFragment");
//
//                setSubTitleStatus(2);
//            }
//        });
//
//        im_todolist_msg_fy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toDoListMsgDialogFragment = YTXToDoListMsgDialogFragment.newInstance(1);
//                toDoListMsgDialogFragment.show(getFragmentManager(), "ToDoListMsgDialogFragment");
//
//                setSubTitleStatus(3);
//            }
//        });
//
//        im_file_fy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                setSubTitleStatus(4);
//            }
//        });
//
//        im_all_msg_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (toDoListMsgDialogFragment != null) {
//                    toDoListMsgDialogFragment.dismiss();
//                }
//
//                setSubTitleStatus(0);
//            }
//        });
//    }
//
//    private void setSubTitleStatus(int pos) {
//        im_all_msg_tv.setSelected(false);
//        im_collect_msg_iv.setSelected(false);
//        im_at_msg_iv.setSelected(false);
//        im_todolist_msg_iv.setSelected(false);
//        im_file_iv.setSelected(false);
//        switch (pos) {
//            case 0:
//                im_all_msg_tv.setSelected(true);
//                break;
//
//            case 1:
//                im_collect_msg_iv.setSelected(true);
//                break;
//
//            case 2:
//                im_at_msg_iv.setSelected(true);
//                break;
//
//            case 3:
//                im_todolist_msg_iv.setSelected(true);
//                break;
//
//            case 4:
//                im_file_iv.setSelected(true);
//                break;
//        }
//    }
//
//    private YTXChattingPresenter presenter;
//
//    /**
//     * 跳转搜索
//     */
//    private void doSearchAction(View view) {
////        Intent action = new Intent();
////        action.setClassName(getActivity(), "com.yuntongxun.plugin.rxcontacts.search.SearchActivity");
////        action.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
////        action.putExtra("FROM", 1);
////        startActivity(action);
////        overridePendingTransition(0, 0);
//
////        Intent intent = new Intent();
////        intent.setClassName(getActivity(), "com.yuntongxun.rongxin.lite.ui.search.SearchUI");
//        Rect rect = new Rect();
//        view.getGlobalVisibleRect(rect);
//
//        RxAppLinkHelper.getInstance(getActivity(), YTXARouterPathUtil.pathSearchUI)
//                .putExtra("FROM", 1)
//                .putExtra("rectTop",rect.top)
//                .putExtra("searchMode", YTXAppMgr.getSearchMode())
//                .startActivity(getActivity(),0,0);
//    }
//
//
//    /**
//     * 点击进行重连
//     */
//    private void reTryConnect() {
//        ECDevice.ECConnectState connectState = YTXSDKCoreHelper.getConnectState();
//        if (connectState == null || connectState == ECDevice.ECConnectState.CONNECT_FAILED) {
//            YTXSDKCoreHelper.init(getActivity());
//            Intent intent = new Intent();
//            intent.setClass(getActivity(), YTXNetRemindActivity.class);
//            startActivity(intent);
//        }
//    }
//
//    public void updateConnectState(ECDevice.ECConnectState connect) {
//        if (!isAdded()) {
//            return;
//        }
//        ECDevice.ECConnectState newConnect = YTXSDKCoreHelper.getConnectState();
//        if (newConnect == ECDevice.ECConnectState.CONNECT_FAILED) {
//            mBannerView.setNetWarnText(getString(R.string.im_connect_server_error));
//            mBannerView.reconnect(false);
//            mBannerView.setVisibility(View.VISIBLE);
//            mBottomBannerView.setNetWarnText(getString(R.string.im_connect_server_error));
//            mBottomBannerView.reconnect(false);
//            mBottomBannerView.setVisibility(View.VISIBLE);
//        } else if (newConnect == ECDevice.ECConnectState.CONNECT_SUCCESS) {
//            mBannerView.setVisibility(View.GONE);
//            mBannerView.hideWarnBannerView();
//            mBottomBannerView.setVisibility(View.GONE);
//            mBottomBannerView.hideWarnBannerView();
//        }
//        LogUtil.d(TAG, "updateConnectState connect :" + connect.name());
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.ytx_conversation;
//    }
//
//
//    @Override
//    public void OnListAdapterCallBack() {
//        if (mAttachListener != null) {
//            mAttachListener.OnUpdateMsgUnreadCounts();
//        }
//    }
//
//    @Override
//    public YTXBasePresenter getPresenter() {
//        return null;
//    }
//
//    @Override
//    public void onTabFragmentClick() {
//        LogUtil.v(TAG, "request to top");
//        if (mListView == null) {
//            return;
//        }
//        YTXListViewUtils.requestListViewToTop(mListView);
//    }
//
//    @Override
//    public void onTabFragmentDoubleClick() {
//        int postion = findReadPosition();
//        //移动到未读消息位置
//        smoothMoveToPosition(postion);
//        super.onTabFragmentDoubleClick();
//    }
//
//    @Override
//    public void onReleaseTabUI() {
//
//    }
//
//    /**
//     * 广播接收
//     *
//     * @param context
//     * @param intent
//     */
//    @Override
//    protected void handleReceiver(Context context, Intent intent) {
//        super.handleReceiver(context, intent);
//        LogUtil.d(TAG, "action : %s ", intent.getAction());
//        if (YTXCASIntent.ACTION_SYNC_GROUP.equals(intent.getAction())
//                || YTXCASIntent.ACTION_CONTACT_SYNC_COMPLETE.equals(intent.getAction())
//                || YTXCASIntent.ACTION_SESSION_DEL.equals(intent.getAction())
//                || YTXCASIntent.DOWNLOAD_ENTERPRISE_SUCCESS.equals(intent.getAction())
//                || YTXCASIntent.ACTION_SESSION_CHANGE.equals(intent.getAction())
//                || YTXCASIntent.ACTION_INIT_BATCH_EMEPLOYEE_INFO.equals(intent.getAction())) {
//            dismissDialog();
//            if (mAdapter != null) {
//                mAdapter.notifyChange();
//            }
//        } else if (YTXSDKCoreHelper.ACTION_SDK_CONNECT.equals(intent.getAction())) {
////            updateConnectState(ECDevice.ECConnectState.values()[intent.getIntExtra("state", 1)]);
//            if (YTXSDKCoreHelper.getConnectState() == ECDevice.ECConnectState.CONNECT_SUCCESS) {
//                getMultiDeviceStatus();
//                YTXIMChattingHelper.getInstance().reSetStickyTopStatus("3");
//            }
//        } else if (YTXCASIntent.ACTION_MULTI_DEVICE_LOGIN_OUT.equals(intent.getAction())) {
//            setmLoginRemindView("", false);
//        } else if (YTXCASIntent.ACTION_MULTI_DEVICE_STATE.equals(intent.getAction()) || YTXCASIntent.ACTION_MULTI_DEVICE_ONOFFLINE.equals(intent.getAction())) {
//            getMultiDeviceStatus();
//        } else if (YTXCASIntent.ACTION_STICKY_ON_TOP.equals(intent.getAction())) {
//            intent.putExtra("type", "2");
//            updateStickyTopStatus(intent, false);
//        } else if (YTXCASIntent.ACTION_SET_STICKY_ON_TOP.equals(intent.getAction())) {
//            updateStickyTopStatus(intent, true);
//        } else if (YTXCASIntent.ACTION_MSG_NOTICE_SET_MUTE.equals(intent.getAction())) {
//            String userName = intent.getStringExtra("userName");
//            if (TextUtil.isEmpty(userName)) {
//                return;
//            }
//            boolean isMute = intent.getBooleanExtra("isMute", false);
//            if (YTXBackwardSupportUtil.isPeerChat(userName)) {
//                YTXGroupService.setGroupMsgNoticeSetMute(isMute, userName);
//            } else {
//                RXUserSetting userSetting = DBRXUserSettingTools.getInstance().
//                        getUserSetting(userName);
//                userSetting.setNewMsgNotification(!isMute);
//                userSetting.setUsername(userName);
//                DBRXUserSettingTools.getInstance().update(userSetting);
//            }
//            if (mAdapter != null) {
//                mAdapter.notifyChange();
//            }
//        } else if (YTXCASIntent.ACTION_USER_STATE.equals(intent.getAction())) {
//            handleSubscribeUserState(intent);
//        } else if (YTXCASIntent.DOWNLOAD_WARTER_SUCCESS.equals(intent.getAction())) {
//            //水印
//            YTXWaterMark waterMark = YTXWaterMarkUtils.getHighWaterMark(getContext(), getResources().getColor(R.color.water_mark_low_bg), getResources().getColor(R.color.water_mark_low_content), (int) getResources().getDimension(R.dimen.size_big), (int) getResources().getDimension(R.dimen.watermark_hor_padding), (int) getResources().getDimension(R.dimen.watermark_ver_padding));
//            if (waterMark != null) {
//                mListView.setBackground(waterMark.getBitmapDrawable());
//            }
//        } else if (YTXCASIntent.ACTION_MSG_BUS_ICON_SWITCH.equals(intent.getAction())) {
//            setSubTitleStatus(0);
//
//        }
//
//    }
//
//    /**
//     * 收到服务器推送的上下线消息 在该方法里处理数据
//     *
//     * @param intent
//     */
//    private void handleSubscribeUserState(Intent intent) {
//        ArrayList<ECUserState> ecUserStates = intent.getParcelableArrayListExtra(YTXCASIntent.KEY_SUBSCRIBE_USER_STATE);
//        for (ECUserState ecUserState : ecUserStates) {
//            String userId = DemoUtils.getUserIdFromSdk(ecUserState.getUserId());
//            if (isOnline.containsKey(userId)) {
//                if (ecUserState.isOnline()) {
//                    String subTile = DemoUtils.getDeviceWithType(ecUserState.getDeviceType())
//                            + DemoUtils.getNetWorkWithType(ecUserState.getNetworkType());
//                    isOnline.put(userId, subTile);
//                } else {
//                    isOnline.put(userId, "0");
//                }
//            }
//        }
//        mAdapter.notifyChange();
//    }
//
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        if (ytx_conversation_custom_msg.equals(v)){
//            if (YTXIMPluginManager.getManager().getYtxCustomMsgItem().isNotice()) {
//                menu.add(R.string.im_menu_mute_notify , R.string.im_menu_mute_notify , Menu.NONE , getString(R.string.im_menu_mute_notify));
//            }else {
//                menu.add(R.string.im_menu_notify , R.string.im_menu_notify , Menu.NONE , getString(R.string.im_menu_notify));
//            }
//            menu.add(R.string.im_main_delete , R.string.im_main_delete , Menu.NONE , getString(R.string.im_main_delete));
//        }else {
//            AdapterView.AdapterContextMenuInfo menuInfoIn;
//            try {
//                menuInfoIn = (AdapterView.AdapterContextMenuInfo) menuInfo;
//            } catch (ClassCastException e) {
//                LogUtil.e("bad menuInfoIn--->", e.getMessage());
//                return;
//            }
//            int headerViewsCount = mListView.getHeaderViewsCount();
//            if (menuInfoIn.position < headerViewsCount) {
//                return;
//            }
//            int _position = menuInfoIn.position - headerViewsCount;
//            if (mAdapter != null) {
//                RXConversation conversation = mAdapter.getItem(_position);
//                if (conversation == null) {
//                    return;
//                }
//                menu.add(menuInfoIn.position, R.string.im_main_delete, Menu.NONE, R.string.im_main_delete);
//
//
//                if (getIsChar(conversation)) {
//                    if (conversation.getSessionId().toLowerCase().startsWith("g")) {
//                        ECGroup ecGroup = DBECGroupTools.getInstance().getECGroup(conversation.getSessionId());
//                        if (ecGroup != null && ecGroup.isNotice()) {
//                            menu.add(menuInfoIn.position, R.string.im_menu_mute_notify, Menu.NONE, R.string.im_menu_mute_notify);
//                        } else {
//                            menu.add(menuInfoIn.position, R.string.im_menu_notify, Menu.NONE, R.string.im_menu_notify);
//                        }
//                    } else {
//                        RXUserSetting userSetting = DBRXUserSettingTools.getInstance().
//                                getUserSetting(conversation.getSessionId());
//                        if (userSetting != null && userSetting.getNewMsgNotification()) {
//                            menu.add(menuInfoIn.position, R.string.im_menu_mute_notify, Menu.NONE, R.string.im_menu_mute_notify);
//                        } else {
//                            menu.add(menuInfoIn.position, R.string.im_menu_notify, Menu.NONE, R.string.im_menu_notify);
//                        }
//                    }
//
//
//                }
//
//                if (conversation.isStickyTop()) {
//                    menu.add(menuInfoIn.position, R.string.im_main_conversation_longclick_unplacedtop, Menu.NONE, R.string.im_main_conversation_longclick_unplacedtop);
//                } else {
//                    menu.add(menuInfoIn.position, R.string.im_main_conversation_longclick_placedtop, Menu.NONE, R.string.im_main_conversation_longclick_placedtop);
//                }
////            if (conversation.getUnreadCount() > 0) {
////                menu.add(menuInfoIn.position, R.string.im_main_conversation_longclick_markRead, Menu.NONE, R.string.im_main_conversation_longclick_markRead);
////            } else {
////                menu.add(menuInfoIn.position, R.string.im_main_conversation_longclick_setUnRead, Menu.NONE, R.string.im_main_conversation_longclick_setUnRead);
////
////            }
//            }
//        }
//
//    }
//
//    private boolean getIsChar(RXConversation conversation) {
//        if (DBRXGroupNoticeTools.CONTACT_ID.equals(conversation.getSessionId())) {
//            //群组系统消息
//            return false;
//        } else if (UserData.NEW_FRIEND_SENDER.equals(conversation.getSessionId())) {
//            //新联系人消息
//            return false;
//        } else if (YTXIMChattingHelper.FILE_TRANSFER_SESSION_ID.equals(conversation.getSessionId())) {
//            return false;
//        } else if (YTXIMChattingHelper.OFFICIAL_ACCOUNT_SESSION_ID.equals(conversation.getSessionId())) {
//            return false;
//        } else if (YTXIMChattingHelper.GROUP_HELPER_ID.equals(conversation.getSessionId())) {
//            return false;
//        } else if (YTXIMChattingHelper.ATTEN_DANCE_SESSION_ID.equals(conversation.getSessionId())) {
//            return false;
//        } else if (YTXIMChattingHelper.MONITOR_PLATFORM_SESSION_ID.equals(conversation.getSessionId())) {
//            return false;
//        } else if (conversation.getSessionId().startsWith(YTXIMChattingHelper.MINIAPP_PUSH_ACCOUNT)) {
//            return false;
//        } else if (YTXMsgNotifyHelper.getInstance().isMsgNotify(conversation.getSessionId())) {
//            return false;
//        } else if (conversation.getType() == 1) {
//            return false;
//        } else if (conversation.getSessionId().equals(YTXIMChattingHelper.MEETING_NEWS_SESSION_ID)) {
//            return false;
//        } else if (conversation.getSessionId().equals(YTXIMChattingHelper.HOUSE_KEEPER_SESSION_ID)) {
//            return false;
//        } else if (conversation.getSessionId().equals(YTXIMChattingHelper.OA_SESSION_ID)) {
//            return false;
//        } else if (conversation.getSessionId().equals(YTXIMChattingHelper.EHR_SESSION_ID)) {
//            return false;
//        }
//
//        return true;
//    }
//
//    @Override
//    public void OnActionMenuSelected(MenuItem menu, int position) {
//        if (menu.getGroupId()==R.string.im_menu_mute_notify){
//            YTXCustomMsgItem ytxCustomMsgItem = YTXIMPluginManager.getManager().getYtxCustomMsgItem();
//            ytxCustomMsgItem.setNotice(false);
//            YTXIMPluginManager.getManager().updateCustomMsgItem(ytxCustomMsgItem);
//        }else if (menu.getGroupId()==R.string.im_menu_notify){
//            YTXCustomMsgItem ytxCustomMsgItem = YTXIMPluginManager.getManager().getYtxCustomMsgItem();
//            ytxCustomMsgItem.setNotice(true);
//            YTXIMPluginManager.getManager().updateCustomMsgItem(ytxCustomMsgItem);
//        }else if (menu.getGroupId()==R.string.im_main_delete){
//            YTXCustomMsgItem ytxCustomMsgItem = YTXIMPluginManager.getManager().getYtxCustomMsgItem();
//            ytxCustomMsgItem.setVisible(false);
//            YTXIMPluginManager.getManager().updateCustomMsgItem(ytxCustomMsgItem);
//        }
//
//        AdapterView.AdapterContextMenuInfo menuInfo;
//        try {
//            menuInfo = (AdapterView.AdapterContextMenuInfo) menu.getMenuInfo();
//        } catch (ClassCastException e) {
//            LogUtil.e("bad menuInfoIn--->", e.getMessage());
//            return;
//        }
//        int headerViewsCount = mListView.getHeaderViewsCount();
//        if (menuInfo==null||menuInfo.position < headerViewsCount) {
//            return;
//        }
//        int _position = menuInfo.position - headerViewsCount;
//        if (mAdapter == null) {
//            return;
//        }
//        final RXConversation conversation = mAdapter.getItem(_position);
//        if (conversation == null) {
//            return;
//        }
//        if (menu.getItemId() == R.string.im_main_delete) {
//            showPostingDialog(R.string.im_clear_chat);
//
//            DBRXConversationTools.getInstance().deleteChatting(conversation.getSessionId(), false);
//            ECPreferences.getSharedPreferencesEditor().putBoolean(ECPreferenceSettings.SETTINGS_IS_SHOW_MONITOR.getId(), false);
//            LogUtil.d(TAG, "[OnActionMenuSelected] subscribe call ThreadName " + Thread.currentThread().getName());
//            getSubscribeUser();
//            dismissDialog();
//            mAdapter.notifyChange();
//        } else if (menu.getItemId() == R.string.im_menu_mute_notify ||
//                menu.getItemId() == R.string.im_menu_notify) {
//
//            showPostingDialog();
//
//
//            if (conversation.getSessionId().toLowerCase().startsWith("g")) {
//                final boolean notify = DBECGroupTools.getInstance().isGroupNotify(conversation.getSessionId());
//                ECGroupOption option = new ECGroupOption();
//                option.setGroupId(conversation.getSessionId());
//                option.setRule(notify ? ECGroupOption.Rule.SILENCE : ECGroupOption.Rule.NORMAL);
//                YTXGroupService.setGroupMessageOption(option, new YTXGroupService.GroupOptionCallback() {
//                    @Override
//                    public void onComplete(String groupId) {
//                        if (mAdapter != null) {
//                            mAdapter.notifyChange();
//                        }
//                        ToastUtil.showMessage(notify ? R.string.im_new_msg_mute_notify :
//                                R.string.im_new_msg_notify);
//                        dismissDialog();
//                    }
//
//                    @Override
//                    public void onError(ECError error) {
//                        dismissDialog();
//                        ToastUtil.showMessage(R.string.im_setting_error);
//                    }
//                });
//            } else {
//
//                RXUserSetting userSetting = DBRXUserSettingTools.getInstance().
//                        getUserSetting(conversation.getSessionId());
//                final boolean notify = userSetting.getNewMsgNotification();
//                ECDevice.setMuteNotification(conversation.getSessionId(), notify, new ECDevice.OnSetDisturbListener() {
//                    @Override
//                    public void onResult(ECError ecError) {
//                        dismissDialog();
//                        if (ecError.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
//
//                            //更新新消息通知状态
//                            RXUserSetting userSetting = DBRXUserSettingTools.getInstance().
//                                    getUserSetting(conversation.getSessionId());
//                            userSetting.setNewMsgNotification(!notify);
//                            userSetting.setUsername(conversation.getSessionId());
//                            DBRXUserSettingTools.getInstance().update(userSetting);
//
//                            if (mAdapter != null) {
//                                mAdapter.notifyChange();
//                            }
//                            if (!RXConfig.MSG_NOTICE_SET_SINGLE_MUTE) {
//                                return;
//                            }
//                            YTXIMChattingHelper.getInstance().sendMsgNoticeSetMute(conversation.getSessionId(), notify);
//                            return;
//                        }
//                        ToastUtil.showMessage(R.string.comm_operation_failed);
//                    }
//                });
//            }
//
//        } else if (menu.getItemId() == R.string.im_main_conversation_longclick_unplacedtop
//                || menu.getItemId() == R.string.im_main_conversation_longclick_placedtop) {
//            // 置顶、未置顶
//            final boolean isStickyTop = !conversation.isStickyTop();
//            YTXIMChattingHelper.getInstance().setStickyTopStatus(isStickyTop, conversation.getSessionId(), "2");
//        } else if (menu.getItemId() == R.string.im_main_conversation_longclick_markRead
//                || menu.getItemId() == R.string.im_main_conversation_longclick_setUnRead) {
//
//            // 已读、未读
//            if (conversation.getUnreadCount() > 0) {
//                conversation.setUnreadCount(0);
//            } else {
//                conversation.setUnreadCount(1);
//            }
//            DBRXConversationTools.getInstance().update(conversation);
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 0x1 && data != null) {
//            String appId = data.getStringExtra("mini_app_code");
//            if (!TextUtil.isEmpty(appId)) {
//                YTXMiniAppHelper.getInstance().startOpenWorkUI(YTXConversationListFragment.this, appId, 1);
//            }
//        }
//    }
//
//    /**
//     * 设置用户是否置顶状态
//     *
//     * @param isStickyTop 是否置顶状态
//     * @param userName    用户账号
//     */
//    private void markUserSettingStickyTop(boolean isStickyTop, String userName, boolean showTips) {
//        RXUserSetting userSetting = DBRXUserSettingTools.getInstance().getUserSetting(userName);
//        if (!YTXBackwardSupportUtil.isNullOrNil(userSetting.getUsername())) {
//            userSetting.setStickyTop(isStickyTop);
//            userSetting.setUpdateTime(System.currentTimeMillis() + "");
//            DBRXUserSettingTools.getInstance().update(userSetting);
//        } else {
//            userSetting = new RXUserSetting();
//            userSetting.setUsername(userName);
//            userSetting.setStickyTop(isStickyTop);
//            userSetting.setUpdateTime(System.currentTimeMillis() + "");
//            DBRXUserSettingTools.getInstance().insert(userSetting);
//        }
//        if (mAdapter != null) {
//            mAdapter.notifyChange();
//        }
//
//        if (!showTips) {
//            LogUtil.e(TAG, "No need for toast prompt for top sync info.");
//            return;
//        }
//        if (isStickyTop) {
//            ToastUtil.showMessage(R.string.im_main_conversation_placedtop_tips);
//        } else {
//            ToastUtil.showMessage(R.string.im_main_conversation_unplacedtop_tips);
//        }
//    }
//
//    public void getMultiDeviceStatus() {
//        ECDevice.ECConnectState newConnect = YTXSDKCoreHelper.getConnectState();
//        if (newConnect != ECDevice.ECConnectState.CONNECT_SUCCESS) {
//            setmLoginRemindView("", false);
//            return;
//        }
//        ECDevice.getOnlineMultiDevice(new ECDevice.OnGetOnlineMultiDeviceListener() {
//            @Override
//            public void onGetOnlineMultiDevice(ECError ecError, ECMultiDeviceState... ecMultiDeviceStates) {
//                if (ecError.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
//                    if (ecMultiDeviceStates.length <= 0) {
//                        setmLoginRemindView("", false);
//                        return;
//                    }
//                    ECMultiDeviceState deviceState = ecMultiDeviceStates[0];
//                    if (deviceState != null && deviceState.isOnline()) {
//                        ECDeviceType deviceType = deviceState.getDeviceType();
//                        if (deviceType == ECDeviceType.IPHONE || deviceType == ECDeviceType.ANDROID_PHONE) {
//                            setmLoginRemindView("", false);
//                        } else {
//                            String deviceName = DemoUtils.getDeviceWithType(deviceType);
//                            setmLoginRemindView(deviceName, true);
//                        }
//                    } else {
//                        setmLoginRemindView("", false);
//                    }
//                } else {
//                    setmLoginRemindView("", false);
//                }
//            }
//        });
//    }
//
//    public void setmLoginRemindView(String deviceName, boolean isOnLine) {
//        currentDeviceName = TextUtil.isEmpty(deviceName) ? null : deviceName;
//        if (mLoginRemindView != null) {
//            if (isOnLine) {
//                mLoginRemindView.setMultiLoginRemind(deviceName, false);
//                mLoginRemindView.show();
//                getMuteState(deviceName);
//            } else {
//                updateSettingState(ECPreferenceSettings.SETTINGS_MUTE_SHOW_NOTIFY, true);
//                mLoginRemindView.hide();
//            }
//        }
//        if (mBottomLoginRemindView != null) {
//            if (isOnLine) {
//                mBottomLoginRemindView.setVisibility(View.VISIBLE);
//                mBottomLoginRemindView.setMultiLoginRemind(deviceName, false);
//                mBottomLoginRemindView.show();
//            } else {
//                mBottomLoginRemindView.setVisibility(View.GONE);
//                mBottomLoginRemindView.hide();
//            }
//        }
//    }
//
//    private void getMuteState(final String deviceName) {
//        //避免静态变量有时初始化为空
//        String REST_HOST = ECPreferences.getSharedPreferences().getString(RXConfig.CONFIG_REST_HOST, "");
//        String APP_ID = ECPreferences.getSharedPreferences().getString(RXConfig.CONFIG_APPID, "");
//        String APP_TOKEN = ECPreferences.getSharedPreferences().getString(RXConfig.CONFIG_APPTOKEN, "");
//        YTXIMRequestUtils.getMuteState(REST_HOST, APP_ID, APP_TOKEN, new Callback<JSONObject>() {
//            @Override
//            public void onResponse(Call<JSONObject> call, retrofit2.Response<com.alibaba.fastjson.JSONObject> response) {
//                if (response.isSuccessful() && response.body().get("statusCode").equals("000000")) {
//                    String state = response.body().getString("state");
//                    Boolean is_mute_on = state != null && state.equals("1") ? true : false;
//                    updateSettingState(ECPreferenceSettings.SETTINGS_MUTE_SHOW_NOTIFY, !is_mute_on);
//                    mLoginRemindView.setMultiLoginRemind(deviceName, is_mute_on);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<com.alibaba.fastjson.JSONObject> call, Throwable t) {
//
//            }
//        });
//    }
//
//    public void updateSettingState(ECPreferenceSettings preferenceSettings, Object value) {
//        try {
//            ECPreferences.savePreference(preferenceSettings, value, true);
//        } catch (InvalidClassException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 更新账号置顶状态
//     *
//     * @param intent   更新intent
//     * @param showTips 是否Toast提示
//     */
//    private void updateStickyTopStatus(Intent intent, boolean showTips) {
//        String type = intent.getStringExtra("type");
//        if (TextUtil.isEmpty(type)) {
//            return;
//        }
//        if (type.equals("3")) {
//            if (mAdapter != null) {
//                mAdapter.notifyChange();
//            }
//        } else if (type.equals("2")) {
//            boolean checked = intent.getBooleanExtra("checked", false);
//            String userName = intent.getStringExtra("userName");
//            markUserSettingStickyTop(checked, userName, showTips);
//        }
//    }
//
//
//    private void getMeetingInfoByUser() {
//        YTXIMRequestUtils.getMeetingInfoByUser(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                JsonObject body = response.body();
//                if (body == null) {
//                    return;
//                }
//                String statusCode = body.get("statusCode").getAsString();
//                if (response.isSuccessful() && ("000000").equals(statusCode)) {
//                    YTXGroupNoticeHelper.crateConfGroup.clear();
//                    for (JsonElement meets : body.getAsJsonArray("meets")) {
//                        int count = meets.getAsJsonObject().get("memberCount").getAsInt();
//                        String mGroupId = meets.getAsJsonObject().get("groupId").getAsString();
//                        if (count > 0) {
//                            YTXGroupNoticeHelper.crateConfGroup.add(mGroupId);
//                        }
//                    }
//                    if (mAdapter != null) {
//                        mAdapter.notifyChange();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
//    }
//
//
//    /**
//     * 查找未读item的位置
//     */
//
//    public int findReadPosition() {
//        int position = 0;
//        int headerViewsCount = mListView.getHeaderViewsCount();
//
//        int size = mListView.getCount() - headerViewsCount;
//        /** 列表中第一个可见的item position */
//        int firstPosition = mListView.getFirstVisiblePosition();
//        int lastPosition = mListView.getLastVisiblePosition();
//        /** 定位第一个未读消息的position */
//        int lastNumPosition = -1, firstNumPosition = 0;
//        for (int i = size - 1; i >= 0; i--) {
//            if (mAdapter.getItem(i).getUnreadCount() > 0) {
//                lastNumPosition = i;
//                break;
//            }
//        }
//        for (int i = 0; i < size; i++) {
//            if (mAdapter.getItem(i).getUnreadCount() > 0) {
//                firstNumPosition = i;
//                break;
//            }
//        }
//        /**************** end *****************/
//        for (int i = 0; i < size; i++) {
//            int num = mAdapter.getItem(i).getUnreadCount();
//            /** 从可见的position开始往下查找 */
//            if (i + headerViewsCount > firstPosition && num > 0 && lastPosition != size - 1) {
//                position = i + headerViewsCount;
//                break;
//            } else if (lastNumPosition == i) {
//                //如果是已经定位到最后一条未读position了，返回第一条未读position
//                position = firstNumPosition;
//            }
//        }
//        return position;
//    }
//
//
//    private void smoothMoveToPosition(final int position) {
//        YTXListViewUtils.smoothScrollToPosition(mListView, position);
//    }
//
//
//}
