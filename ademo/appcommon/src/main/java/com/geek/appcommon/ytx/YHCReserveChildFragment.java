//package com.geek.appcommon.ytx;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Build;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.view.animation.AlphaAnimation;
//import android.view.animation.Animation;
//import android.view.animation.AnimationSet;
//import android.view.animation.TranslateAnimation;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.SimpleItemAnimator;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//
//import com.geek.common.R;
//import com.google.android.material.appbar.AppBarLayout;
//import com.yuntongxun.ecsdk.ECAccountInfo;
//import com.yuntongxun.ecsdk.ECConferenceInfo;
//import com.yuntongxun.ecsdk.ECConferenceManager;
//import com.yuntongxun.ecsdk.ECConferenceMemberInfo;
//import com.yuntongxun.ecsdk.ECDevice;
//import com.yuntongxun.ecsdk.ECError;
//import com.yuntongxun.ecsdk.SdkErrorCode;
//import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;
//import com.yuntongxun.plugin.common.RXConfig;
//import com.yuntongxun.plugin.common.RongXinApplicationContext;
//import com.yuntongxun.plugin.common.YHCCASIntent;
//import com.yuntongxun.plugin.common.YHCDynamicConfig;
//import com.yuntongxun.plugin.common.YTXAppMgr;
//import com.yuntongxun.plugin.common.YTXCASIntent;
//import com.yuntongxun.plugin.common.YTXSDKCoreHelper;
//import com.yuntongxun.plugin.common.common.helper.RXContentMenuHelper;
//import com.yuntongxun.plugin.common.common.menu.YTXActionMenu;
//import com.yuntongxun.plugin.common.common.utils.ClipboardUtils;
//import com.yuntongxun.plugin.common.common.utils.ConfToasty;
//import com.yuntongxun.plugin.common.common.utils.DemoUtils;
//import com.yuntongxun.plugin.common.common.utils.DensityUtil;
//import com.yuntongxun.plugin.common.common.utils.LogUtil;
//import com.yuntongxun.plugin.common.common.utils.NetWorkUtils;
//import com.yuntongxun.plugin.common.common.utils.TextUtil;
//import com.yuntongxun.plugin.common.presentercore.presenter.YTXBasePresenter;
//import com.yuntongxun.plugin.common.recycler.YTXConfBaseQuickAdapter;
//import com.yuntongxun.plugin.common.ui.CCPFragment;
//import com.yuntongxun.plugin.common.ui.tools.YTXAlertDialog;
//import com.yuntongxun.plugin.common.ui.tools.YTXStatusBarUtil;
//import com.yuntongxun.plugin.common.utils.YTXThreadManager;
//import com.yuntongxun.plugin.conference.bean.ConfErrorCode;
//import com.yuntongxun.plugin.conference.bean.NetConference;
//import com.yuntongxun.plugin.conference.bean.YHCConfInfo;
//import com.yuntongxun.plugin.conference.bean.YHCConfMember;
//import com.yuntongxun.plugin.conference.bean.YHCShareInfo;
//import com.yuntongxun.plugin.conference.conf.ConferenceService;
//import com.yuntongxun.plugin.conference.conf.LiveMeetingUtil;
//import com.yuntongxun.plugin.conference.helper.ConfMemberMenuHelper;
//import com.yuntongxun.plugin.conference.helper.YHCConferenceHelper;
//import com.yuntongxun.plugin.conference.manager.YHCConfConstData;
//import com.yuntongxun.plugin.conference.manager.YHCConferenceMgr;
//import com.yuntongxun.plugin.conference.manager.inter.OnActionResultCallBack;
//import com.yuntongxun.plugin.conference.manager.inter.OnGetNameOnlineListener;
//import com.yuntongxun.plugin.conference.manager.inter.OnReturnMemberCallback;
//import com.yuntongxun.plugin.conference.manager.inter.OnReturnMenuCallback;
//import com.yuntongxun.plugin.conference.manager.inter.SHARE_CONF_TYPE;
//import com.yuntongxun.plugin.conference.manager.inter.ShareContent;
//import com.yuntongxun.plugin.conference.view.activity.YHCConfDetailActivity;
//import com.yuntongxun.plugin.conference.view.activity.YHCConfEntityDeatailActivity;
//import com.yuntongxun.plugin.conference.view.activity.YHCConfStartAndReserveActivity;
//import com.yuntongxun.plugin.conference.view.activity.YHCEntityRoomListActivity;
//import com.yuntongxun.plugin.conference.view.activity.YHCHistoryListActivity;
//import com.yuntongxun.plugin.conference.view.activity.YHCJoinConfActivity;
//import com.yuntongxun.plugin.conference.view.adapter.WrapContentLinearLayoutManager;
//import com.yuntongxun.plugin.conference.view.adapter.YTXConfReserveListAdapter;
//import com.yuntongxun.plugin.conference.view.ui.CustomYTXLoadMoreView;
//import com.yuntongxun.plugin.conference.view.widget.Voip;
//import com.yuntongxun.youhui.ui.activity.LoginActivity;
//import com.yuntongxun.youhui.ui.activity.SettingConfActivity;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import io.reactivex.Observable;
//import io.reactivex.ObservableEmitter;
//import io.reactivex.ObservableOnSubscribe;
//import io.reactivex.Observer;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.annotations.NonNull;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.schedulers.Schedulers;
//
//
///**
// * 容视首页Fragment
// * 1、初始化SDK
// * 2、SDK初始化后查询正在进行中的会议
// * 3、
// * Created by Gethin
// */
//
//public class YHCReserveChildFragment extends CCPFragment implements SwipeRefreshLayout.OnRefreshListener,
//        OnReturnMemberCallback, YTXConfBaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener, OnReturnMenuCallback, AppBarLayout.OnOffsetChangedListener {
//    private String TAG = "YHCReserveChildFragment";
//
//    private RecyclerView conf_list_recycler;
//    private YTXConfReserveListAdapter reser_adapter;
//    private List<NetConference> init_list;
//    private List<NetConference> conf_list;
//    private View noDataView;
//    private View errorView;
//    private SwipeRefreshLayout mSwipeRefreshLayout;
//    private LinearLayout meetinglayout;
//
//    private RXContentMenuHelper mMenuHelper;
//    private int maxHeight;
//    private int minHeight;
//    private AppCompatActivity activity;
//    private ConfMemberMenuHelper memberMenuHelper;
//
//    private View popTag;
//    private TextView tv_sz1;
//    private TextView tv_sz2;
//    private TextView entityReserveMeetingOpen, entityCloudMeetingOpen, entityConnectHardwareOpen;
//    private int statusBarHeight;
//    private int dp50;
//    private int screenWidth;
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        initView();
//        registerReceiver(new String[]{YTXSDKCoreHelper.ACTION_SDK_CONNECT, YTXCASIntent.PHONE_CONF_REFRESH_LIST,
//                YTXCASIntent.PHONE_CONF_REFRESH_NOW_LIST, YTXSDKCoreHelper.ACTION_LOGOUT
//                , YHCCASIntent.DELETE_CONF_NOTIFY});
//        initSDK();
//        if (!Voip.isConnected(getContext())) {
//            reser_adapter.setNewData(null);
//            reser_adapter.setEmptyView(errorView);
//            reser_adapter.getEmptyView().setVisibility(View.VISIBLE);
//        }
//        //
//        RongXinApplicationContext.sendBroadcast(YTXCASIntent.PHONE_CONF_REFRESH_LIST);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        connectSDK();
//    }
//
//    private void connectSDK() {
//        if (!YTXSDKCoreHelper.isConnected() && YTXAppMgr.isActive()) {
//            showPostingDialog(R.string.yhc_str_connect);
//            YTXSDKCoreHelper.init(RongXinApplicationContext.getContext());
//        }
//    }
//
//    public void setActivity(AppCompatActivity activity) {
//        this.activity = activity;
//    }
//
//    @Override
//    protected void handleReceiver(Context context, Intent intent) {
//        if (intent == null || intent.getAction() == null) {
//            return;
//        }
//        if (intent.getAction().equals(YTXSDKCoreHelper.ACTION_SDK_CONNECT)) {
//            if (YTXSDKCoreHelper.getConnectState() == ECDevice.ECConnectState.CONNECT_SUCCESS) {
//                //开发者处理连接SDK成功之后的逻辑
//                LogUtil.e("userid = " + YTXAppMgr.getUserId());
//                YHCConferenceHelper.getPluginInfoSetting(getContext(), true);
//            } else {
//                int error = intent.getIntExtra("error", -1);
//                if (error == SdkErrorCode.CONNECTING) {
//                    LogUtil.e(TAG, "sdk connect faild " + error);
//                }
//            }
//        } else if (intent.getAction().equals(YTXCASIntent.PHONE_CONF_REFRESH_LIST) || intent.getAction().equals(YTXCASIntent.PHONE_CONF_REFRESH_NOW_LIST)) {
//            queryConfListDate(false);
//        } else if (intent.getAction().equals(YTXSDKCoreHelper.ACTION_LOGOUT)) {
//            YHCConferenceMgr.getManager().releasePluginConf(getContext());
//        } else if (YHCCASIntent.DELETE_CONF_NOTIFY.equals(intent.getAction())) {
//            String confNo = intent.getStringExtra(ConferenceService.DELETE_CONF_NO);
//            if (!TextUtil.isEmpty(confNo)) {
//                removeAdapterItemByNo(confNo);
//            }
//        }
//    }
//
//    private void initView() {
//        LinearLayout entity_meeting = (LinearLayout) findViewById(R.id.entity_meeting2);
//        if (YHCConfConstData.USE_ENTITY_MEETING) {
//            entity_meeting.setVisibility(View.VISIBLE);
//        } else {
//            entity_meeting.setVisibility(View.GONE);
//        }
//        meetinglayout = (LinearLayout) findViewById(R.id.meetinglayout2);
//        tv_sz1 = (TextView) findViewById(R.id.tv_sz1);
//        tv_sz2 = (TextView) findViewById(R.id.tv_sz2);
//        entityReserveMeetingOpen = (TextView) findViewById(R.id.entity_start_meeting_open2);
//        entityCloudMeetingOpen = (TextView) findViewById(R.id.entity_reser_metting_open2);
//        entityConnectHardwareOpen = (TextView) findViewById(R.id.entity_enterprise_metting_open2);
//
//        meetinglayout = (LinearLayout) findViewById(R.id.meetinglayout2);
//        ViewGroup.LayoutParams layoutParams = meetinglayout.getLayoutParams();
//        maxHeight = layoutParams.height;
//        minHeight = DensityUtil.dip2px(50);
//        tv_sz1.setOnClickListener(this);
//        tv_sz2.setOnClickListener(this);
//        entityReserveMeetingOpen.setOnClickListener(this);
//        entityCloudMeetingOpen.setOnClickListener(this);
//        entityConnectHardwareOpen.setOnClickListener(this);
//        conf_list_recycler = (RecyclerView) findViewById(R.id.ytx_conference_list2);
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout2);
//        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(33, 37, 64));
//        LinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        conf_list_recycler.setLayoutManager(layoutManager);
//        if (init_list == null) {
//            init_list = new ArrayList<>();
//        }
//
//        int centerWidth = DemoUtils.getScreenWidth(getActivity()) - DensityUtil.dip2px(24) * 2;
//        int memberCount = centerWidth / (DensityUtil.dip2px(32) + DensityUtil.dip2px(6));
//        reser_adapter = new YTXConfReserveListAdapter(getContext(), init_list, memberCount);
//        reser_adapter.setOnLoadMoreListener(this, conf_list_recycler);
//        reser_adapter.setLoadMoreView(new CustomYTXLoadMoreView());
//        conf_list_recycler.setAdapter(reser_adapter);
//        ((SimpleItemAnimator) conf_list_recycler.getItemAnimator()).setSupportsChangeAnimations(false);
//        reser_adapter.disableLoadMoreIfNotFullPage();
//        initClickListener();
//
//
//        popTag = findViewById(R.id.conf_reser_pop_tag2);
//        noDataView = getActivity().getLayoutInflater().inflate(R.layout.plugin_ytx_reser_conf_list_empty_view, (ViewGroup) conf_list_recycler.getParent(), false);
//        errorView = getActivity().getLayoutInflater().inflate(R.layout.plugin_ytx_conf_list_no_net_view, (ViewGroup) conf_list_recycler.getParent(), false);
//        if (mMenuHelper == null) {
//            mMenuHelper = new RXContentMenuHelper(getContext());
//        }
//        statusBarHeight = YTXStatusBarUtil.getStatusBarHeight(getContext());
//        screenWidth = DemoUtils.getScreenWidth(getActivity());
//        dp50 = DensityUtil.dip2px(50);
//        noSlideY = statusBarHeight + maxHeight + dp50;
//        noSlideBottomY = DemoUtils.getScreenHeight(getActivity()) - dp50;
//    }
//
//    private void initClickListener() {
//        reser_adapter.setOnItemClickListener(new YTXConfBaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(YTXConfBaseQuickAdapter adapter, View view, int position) {
//                init_list = reser_adapter.getData();
//                if (position > init_list.size() - 1) {
//                    return;
//                }
//                NetConference multiItemEntity = init_list.get(position);
//                if (multiItemEntity != null) {
//                    final NetConference netConference = multiItemEntity;
//                    ECConferenceManager ecConferenceManager = ECDevice.getECConferenceManager();
//                    if (netConference.getItemType() == NetConference.NO_START_CONF || netConference.getItemType() == NetConference.START_CONF) {
//                        ecConferenceManager.getConference(netConference.getConferenceId(), new ECConferenceManager.OnGetConferenceListener() {
//                            @Override
//                            public void onGetConference(ECError ecError, ECConferenceInfo ecConferenceInfo) {
//
//                                if (ecError.errorCode == ConfErrorCode.CONF_NOT_EXIST) {
//                                    ConfToasty.error(netConference.getReserveEnable() == 0 ? getString(R.string.yhc_str_meeting_ended) : getString(R.string.yhc_str_meeting_cancel));
//                                    queryConfListDate(false);
//                                } else {
//                                    enterConfInfo(netConference);
//                                }
//
//                            }
//                        });
//                    } else {
//                        enterConfInfo(netConference);
//                    }
//                }
//            }
//        });
//
//        reser_adapter.setOnItemLongClickListener(new YTXConfBaseQuickAdapter.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(YTXConfBaseQuickAdapter adapter, View view, int position) {
//                if (adapter == null || position < 0 || position >= adapter.getData().size()) {
//                    return false;
//                }
//                NetConference netConference = (NetConference) adapter.getData().get(position);
//                showOperateMenu(netConference, position);
//                return false;
//            }
//        });
//
//
//        YHCConferenceMgr.getManager().setOnGetNameOnlineListener(new OnGetNameOnlineListener() {
//            @Override
//            public void onGetNameOnline(String name, String tag) {
//                if (reser_adapter == null || TextUtil.isEmpty(tag) || !tag.startsWith(YHCConfConstData.REFRESH_CONF_LIST_TAG)) {
//                    return;
//                }
//                int j = 0;
//                for (int i = 0; i < reser_adapter.getData().size(); i++) {
//                    NetConference conference = reser_adapter.getData().get(i);
//                    if (conference == null || conference.getCreatorId() == null) {
//                        continue;
//                    }
//                    String compare = YHCConfConstData.REFRESH_CONF_LIST_TAG + conference.getCreatorId();
//                    if (compare.equals(tag)) {
//                        conference.setCreatorName(name);
//                        j++;
//                    }
//                }
//                if (j > 0) {
//                    reser_adapter.notifyDataSetChanged();
//                }
//            }
//        });
//    }
//
//    private void showOperateMenu(final NetConference netConference, final int menuPosition) {
//        if (mMenuHelper == null) {
//            mMenuHelper = new RXContentMenuHelper(getActivity());
//        }
//        mMenuHelper.setOnCreateActionMenuListener(new YTXActionMenu.OnCreateActionMenuListener() {
//            @Override
//            public void OnCreateActionMenu(YTXActionMenu menu) {
//                ECAccountInfo creator = netConference.getCreator();
//                if (creator == null) {
//                    return;
//                }
//                String caller = YHCConferenceHelper.parser$Account(creator.getAccountId());
//                if (YHCConferenceMgr.getManager().confShareListener != null) {
//                    menu.add(1, RongXinApplicationContext.getContext().getString(R.string.yhc_str_share_meeting));
//                }
//                if (YTXAppMgr.getUserId().equals(caller) && netConference.getDetailType() == NetConference.NO_START_CONF) {
//                    menu.add(3, RongXinApplicationContext.getContext().getString(R.string.yhc_str_cancel_meeting));
//                } else if (YTXAppMgr.getUserId().equals(caller) && netConference.getDetailType() == NetConference.START_CONF) {
//                    menu.add(2, RongXinApplicationContext.getContext().getString(R.string.yhc_str_end_meeting));
//                }
//            }
//        });
//        mMenuHelper.setOnActionMenuItemSelectedListener(new YTXActionMenu.OnActionMenuItemSelectedListener() {
//            @Override
//            public void OnActionMenuSelected(final MenuItem menu, int position) {
//                switch (menu.getItemId()) {
//                    case 1:
//                        showBottomPop(popTag, netConference);
//                        break;
//                    case 2:
//                        YTXAlertDialog alertDialog = new YTXAlertDialog(getContext()).builder().setTitle("");
//                        if (netConference.getConfMembers().size() > 0) {
//                            alertDialog.setMsg(RongXinApplicationContext.getContext().getString(R.string.is_dismiss_conf));
//                        } else {
//                            alertDialog.setMsg(RongXinApplicationContext.getContext().getString(R.string.dismiss_conf));
//                        }
//                        alertDialog.setNegativeButton("", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                            }
//                        });
//                        alertDialog.setPositiveButton("", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                /**
//                                 * fix:23584 【Android容信-容视会议】Android端A创建一个会议然后离开会议，Android端B创建一个会议
//                                 * （会议号：100941 会议时间：17:32）邀请A，A进入B的会议，然后最小化，长按自己之前创的会议，结束掉自己之前的会议，A退出了B的会议
//                                 */
//                                if (ConferenceService.getInstance().mVoipSmallWindow != null
//                                        && !netConference.getConferenceId().equals(ConferenceService.getInstance().mMeetingNo)) {
//                                    disMeeting_WhenSmallWindowIsShow_And_ConferenceId_IsNotEqual_Self(menu, netConference);
//                                } else {
//                                    disMeetingOfSelf(menu, netConference);
//                                }
//                            }
//                        });
//                        alertDialog.show();
//                        break;
//                    case 3:
//                        ConferenceService.disMeeting(netConference.getConferenceId(), false, new OnActionResultCallBack() {
//                            @Override
//                            public void onActionResult(int resultCode) {
//                                if (resultCode == 200) {
//                                    if (isAdded()) {
//                                        ConfToasty.success(menu.getItemId() == 2 ? RongXinApplicationContext.getContext().getString(R.string.string_success_dis_conf) : RongXinApplicationContext.getContext().getString(R.string.string_success_cancel_conf));
//                                    }
//
//                                    removeAdapterItemByNo(netConference.getConferenceId());
//                                } else {
//                                    if (isAdded()) {
//                                        ConfToasty.error(menu.getItemId() == 2 ? RongXinApplicationContext.getContext().getString(R.string.string_fail_dis_conf) : RongXinApplicationContext.getContext().getString(R.string.string_fail_cancel_conf));
//                                    }
//                                }
//                            }
//                        });
//                        break;
//                    default:
//                        break;
//                }
//
//            }
//        });
//        if (mMenuHelper != null) {
//            mMenuHelper.show();
//        }
//    }
//
//    private void disMeetingOfSelf(MenuItem menu, NetConference netConference) {
//        ConferenceService.disMeeting(netConference.getConferenceId(), false, new OnActionResultCallBack() {
//            @Override
//            public void onActionResult(int resultCode) {
//                if (resultCode == 200) {
//                    if (isAdded()) {
//                        ConfToasty.success(menu.getItemId() == 2 ? RongXinApplicationContext.getContext().getString(R.string.string_success_dis_conf) : RongXinApplicationContext.getContext().getString(R.string.string_success_cancel_conf));
//                    }
//                    removeAdapterItemByNo(netConference.getConferenceId());
//                } else {
//                    if (isAdded()) {
//                        ConfToasty.error(menu.getItemId() == 2 ? getString(R.string.string_fail_dis_conf) : getString(R.string.string_fail_cancel_conf));
//                    }
//                }
//            }
//        });
//    }
//
//    private void disMeeting_WhenSmallWindowIsShow_And_ConferenceId_IsNotEqual_Self(MenuItem menu, NetConference netConference) {
//        ConferenceService.getInstance().disMeeting_WhenSmallWindowIsShow_And_ConferenceId_IsNotEqual_Self(netConference.getConferenceId(), false, new OnActionResultCallBack() {
//            @Override
//            public void onActionResult(int resultCode) {
//                if (resultCode == 200) {
//                    if (isAdded()) {
//                        ConfToasty.success(menu.getItemId() == 2 ? RongXinApplicationContext.getContext().getString(R.string.string_success_dis_conf) : RongXinApplicationContext.getContext().getString(R.string.string_success_cancel_conf));
//                    }
//                    removeAdapterItemByNo(netConference.getConferenceId());
//                } else {
//                    if (isAdded()) {
//                        ConfToasty.error(menu.getItemId() == 2 ? RongXinApplicationContext.getContext().getString(R.string.string_fail_dis_conf) : RongXinApplicationContext.getContext().getString(R.string.string_fail_cancel_conf));
//                    }
//                }
//            }
//        });
//    }
//
//
//    private void removeAdapterItem(int menuPosition) {
//        if (reser_adapter != null) {
//            if (menuPosition >= reser_adapter.getData().size()) {
//                return;
//            }
//            reser_adapter.remove(menuPosition);
//            if (reser_adapter.getData().size() == 0) {
//                reser_adapter.setEmptyView(noDataView);
//                reser_adapter.getEmptyView().setVisibility(View.VISIBLE);
//            }
//        }
//    }
//
//    private void removeAdapterItemByNo(String confNo) {
//        if (TextUtil.isEmpty(confNo)) {
//            return;
//        }
//        if (reser_adapter != null) {
//            List<NetConference> data = reser_adapter.getData();
//            for (int i = 0; i < data.size(); i++) {
//                NetConference netConference = data.get(i);
//                if (netConference == null) {
//                    continue;
//                }
//                if (confNo.equals(netConference.getConferenceId())) {
//                    removeAdapterItem(i);
//                    break;
//                }
//            }
//        }
//    }
//
//    /**
//     * 从底部弹出popupwindow
//     */
//    private void showBottomPop(View parent, final NetConference netConference) {
//        new ShareContent().setNetConference(netConference);
//        final View popView = View.inflate(getActivity(), R.layout.yhc_share_bottom_pop_layout, null);
//        TextView popTitle = popView.findViewById(R.id.yhc_pop_title);
//        if (popTitle != null) {
//            popTitle.setText(getString(R.string.yhc_str_share_meeting));
//        }
//        popView.findViewById(com.yuntongxun.plugin.R.id.conf_share).setVisibility(View.VISIBLE);
//        showAnimation(popView);//开启动画
//        String creatorId = TextUtil.isEmpty(netConference.getCreatorId()) ? YTXAppMgr.getUserId() : netConference.getCreatorId();
//        String time = netConference.getReserveEnable() == 1 ? netConference.getReserveStartTime() : netConference.getCreateTime();
//        final YHCShareInfo info = new YHCShareInfo(YTXAppMgr.getUserId(),
//                creatorId, netConference.getConfName(), netConference.getConferenceId(), time);
//        info.setTelNums(netConference.getTelNums());
//        info.setInviterName(YTXAppMgr.getUserName());
//        info.setCreaterName(netConference.getCreatorName());
//        info.setPassword(netConference.getPassword());
//        if (netConference.getRoomInfo() != null) {
//            info.setEntity(YHCConferenceHelper.isEntityConf(netConference));
//            StringBuilder location = new StringBuilder();
//            if (!TextUtils.isEmpty(netConference.getRoomInfo().getCity()) && !TextUtils.isEmpty(netConference.getRoomInfo().getPosition())) {
//                location.append(netConference.getRoomInfo().getCity()).append("-");
//            } else if (!TextUtils.isEmpty(netConference.getRoomInfo().getCity())) {
//                location.append(netConference.getRoomInfo().getCity());
//            }
//            if (!TextUtils.isEmpty(netConference.getRoomInfo().getPosition()) && !TextUtils.isEmpty(netConference.getRoomInfo().getConfRoomName())) {
//                location.append(netConference.getRoomInfo().getPosition()).append("-");
//            }
//            if (!TextUtils.isEmpty(netConference.getRoomInfo().getConfRoomName())) {
//                location.append(netConference.getRoomInfo().getConfRoomName());
//            }
//            if (!TextUtils.isEmpty(location)) {
//                info.setPosotion(location.toString());
//            }
//        }
//
//
//        View copyShare = popView.findViewById(R.id.copy_share);
//        copyShare.setOnClickListener(v -> {
//            ClipboardUtils.copyFromEdit(getContext(), getString(R.string.app_rich), ShareContent.create(requireActivity(), info,
//                    RXConfig.SHARE_CONFERENCE_URL.replace("{confId}", info.getConfId())));
//            ConfToasty.info(R.string.app_copy_ok);
//        });
//        final PopupWindow mPopWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        View weixin = popView.findViewById(R.id.share_wexin);
//        weixin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                YHCConferenceHelper.confShareToOther(getActivity(), info, SHARE_CONF_TYPE.WEIXIN);
//                mPopWindow.dismiss();
//            }
//        });
//        View qq = popView.findViewById(R.id.share_qq);
//        qq.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                YHCConferenceHelper.confShareToOther(getActivity(), info, SHARE_CONF_TYPE.QQ);
//                mPopWindow.dismiss();
//
//
//            }
//        });
//        popView.findViewById(R.id.share_contact).setVisibility(View.GONE);
//        View sms = popView.findViewById(R.id.share_sms);
//        sms.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //                intentState = 1;
//                YHCConferenceHelper.confShareToOther(getActivity(), info, SHARE_CONF_TYPE.SMS);
//                mPopWindow.dismiss();
//            }
//        });
//        TextView share_rongxin = popView.findViewById(R.id.share_rongxin);
//        if (!RXConfig.isRongXin()) {
//            share_rongxin.setVisibility(View.GONE);
//        }
//        share_rongxin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (netConference.getDetailType() == 1) {
//                    info.setStatus(1);
//                }
//                if (netConference.getDetailType() == 2) {
//                    info.setStatus(0);
//                }
//                info.setHostAccount(netConference.getCreatorId());
//                YHCConferenceHelper.confShareToOther(getActivity(), info, SHARE_CONF_TYPE.RONGZIN);
//                mPopWindow.dismiss();
//            }
//        });
//        popView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPopWindow.dismiss();
//            }
//        });
//        View thirdPart = popView.findViewById(R.id.third_part_device);
//        thirdPart.setOnClickListener(v -> {
////            startActivity(new Intent(this, YHCInviteThirdDeviceActivity.class));
//        });
//        weixin.setVisibility(YHCConferenceHelper.isShowWeixinShare() ? View.VISIBLE : View.GONE);
////        qq.setVisibility(YHCConferenceHelper.isShowQQShare() ? View.VISIBLE : View.GONE);
//        sms.setVisibility(YHCConferenceHelper.isShowSMSShare() ? View.VISIBLE : View.GONE);
//        thirdPart.setVisibility(YHCDynamicConfig.USER_INVITE_THIRD_DEVICE ? View.VISIBLE : View.GONE);
//
//        mPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
//            int[] a = new int[2];
//            parent.getLocationOnScreen(a);
//            mPopWindow.showAtLocation(parent, Gravity.NO_GRAVITY, 0, a[1] - DensityUtil.dip2px(170));
//        } else {
//            mPopWindow.showAtLocation(parent,
//                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//        }
//
//        mPopWindow.setOutsideTouchable(true);
//        mPopWindow.setFocusable(true);
//        mPopWindow.update();
//        // 设置背景颜色变暗
//        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//        lp.alpha = 0.7f;
//        getActivity().getWindow().setAttributes(lp);
//        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//
//            @Override
//            public void onDismiss() {
//                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//                lp.alpha = 1f;
//                getActivity().getWindow().setAttributes(lp);
//            }
//        });
//    }
//
//    /**
//     * 给popupwindow添加动画
//     *
//     * @param popView
//     */
//    private void showAnimation(View popView) {
//        AnimationSet animationSet = new AnimationSet(false);
//        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1.0f);
//        alphaAnimation.setDuration(300);
//        TranslateAnimation translateAnimation = new TranslateAnimation(
//                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
//                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f
//        );
//        translateAnimation.setDuration(300);
//        animationSet.addAnimation(alphaAnimation);
//        animationSet.addAnimation(translateAnimation);
//        popView.startAnimation(animationSet);
//    }
//
//    private void enterConfInfo(NetConference netConference) {
//        try {
//            Intent intent;
//            if (netConference.getRoomInfo() != null && YHCConferenceHelper.isEntityConf(netConference)) {
//                intent = new Intent(getActivity(), YHCConfEntityDeatailActivity.class);
//            } else {
//                intent = new Intent(getActivity(), YHCConfDetailActivity.class);
//                intent.putExtra("fromActivity", "YHCReserveListFragment");
//            }
//            intent.putExtra(YHCConfDetailActivity.CONFERENCE_ISHIDEEDIT, true);
//            intent.putExtra(YHCConfDetailActivity.CONFERENCE_INSTANCE, netConference);
//            startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private int pagerNum = 0;
//    private boolean isQuerying;
//
//    private int refrashCount;
//
//    public void queryConfListDate(boolean isPullUp) {
//        mSwipeRefreshLayout.setRefreshing(true);
//        if (reser_adapter.getEmptyView() != null) {
//            reser_adapter.getEmptyView().setVisibility(View.GONE);
//        }
//        if (!NetWorkUtils.IsNetWorkEnable(getContext())) {
//            connectSDK();
//            reser_adapter.setNewData(null);
//            reser_adapter.setEmptyView(errorView);
//            reser_adapter.getEmptyView().setVisibility(View.VISIBLE);
//            mSwipeRefreshLayout.setRefreshing(false);
//            return;
//        }
//
//        if (!isPullUp) {
//            pagerNum = 0;
//        }
//        LogUtil.e(TAG, "queryConfListDate is beginning");
//        refrashCount++;
//
//        if (refrashCount >= 2) {
//            isQuerying = false;
//            refrashCount = 0;
//        }
//
//        if (!isQuerying) {
//            Observable.create(new ObservableOnSubscribe<List<NetConference>>() {
//                @Override
//                public void subscribe(@NonNull ObservableEmitter<List<NetConference>> subscriber) {
//                    isQuerying = true;
//                    ConferenceService.getConferenceListByAccount(YTXAppMgr.getUserId(), pagerNum, 20, new ECConferenceManager.OnGetConferenceListWithCondition() {
//                        @Override
//                        public void OnGetConferenceListWithCondition(ECError ecError, List<ECConferenceInfo> list) {
//                            LogUtil.e(TAG, "getConferenceListByAccount is have");
//                            refrashCount = 0;
//
//                            if (ecError.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
//                                List<NetConference> multiItemEntities = handlerConfList(list);
//                                subscriber.onNext(multiItemEntities);
//                                subscriber.onComplete();
//                            } else {
//                                isQuerying = false;
//                                mSwipeRefreshLayout.setRefreshing(false);
//                                subscriber.onComplete();
//                                LogUtil.e(TAG, "getconflist error is " + ecError.errorCode);
//                                if (isAdded()) {//是否添加到activity上面了
////                                    ConfToasty.error(getString(R.string.yhc_str_list_get_failed));
//                                    //fix:22452 【Android容信】 偶现查询会议历史列表，提示报错“获取列表失败”
//                                    LogUtil.e(TAG, getString(R.string.yhc_str_list_get_failed));
//                                }
//                            }
//                        }
//                    });
//
//                }
//            }).compose(this.bindToLifecycle()).subscribeOn(Schedulers.from(YTXThreadManager.getIO())).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<NetConference>>() {
//                @Override
//                public void onComplete() {
//                    isQuerying = false;
//                    mSwipeRefreshLayout.setRefreshing(false);
//                    if (reser_adapter.getData().size() < 1) {
//                        reser_adapter.setEmptyView(noDataView);
//                        reser_adapter.getEmptyView().setVisibility(View.VISIBLE);
//                    }
//                    LogUtil.d(TAG, "queryConfListDate onCompleted");
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    isQuerying = false;
//                    mSwipeRefreshLayout.setRefreshing(false);
//                    LogUtil.d(TAG, "queryConfListDate " + e.getMessage());
//                    reser_adapter.loadMoreFail();
//                }
//
//                @Override
//                public void onSubscribe(@NonNull Disposable d) {
//
//                }
//
//                @Override
//                public void onNext(List<NetConference> ecConferenceInfos) {
//                    LogUtil.d(TAG, "onnext list.getData" + reser_adapter.getData().size());
//                    if (conf_list_recycler.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || !conf_list_recycler.isComputingLayout()) {
//                        if (pagerNum == 0) {
//                            if (reser_adapter != null) {
//                                reser_adapter.getData().clear();
//                            }
//                        }
//                        List<NetConference> fileterInfos = new ArrayList<>();
//                        for (NetConference conference : ecConferenceInfos) {
//                            LogUtil.e("conferenceonNext", "getAppData" + conference.getAppData() + "getConfName" + conference.getConfName());
//                            if (!LiveMeetingUtil.handlerConferenceType(conference.getAppData(), YHCConfConstData.SafeConf)) {
//                                fileterInfos.add(conference);
//                            }
//                        }
//                        reser_adapter.addData(fileterInfos);
//                    }
//
//                    if (ecConferenceInfos.size() > 0) {
//                        reser_adapter.loadMoreComplete();
//                    } else {
//                        reser_adapter.loadMoreEnd(false);
//                    }
//                    LogUtil.d(TAG, "queryConfListDate onNext");
//                }
//            });
//        }
//    }
//
//    private List<NetConference> handlerConfList(List<ECConferenceInfo> list) {
//        if (conf_list == null) {
//            conf_list = new ArrayList<>();
//        } else {
//            conf_list.clear();
//        }
//        for (ECConferenceInfo ecConferenceInfo : list) {
//            //TODO，做一定的筛选，如果群组会议，那么不显示
//            String chatGroupId = null;
//            if (!TextUtils.isEmpty(ecConferenceInfo.getAppData()) && ecConferenceInfo.getAppData().contains("groupId")) {
//                try {
//                    JSONObject jsonObject = new JSONObject(ecConferenceInfo.getAppData());
//                    if (jsonObject.has("groupId")) {
//                        chatGroupId = jsonObject.getString("groupId");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (!TextUtil.isEmpty(chatGroupId)) {
//                continue;
//            }
//            // TODO: 2019/2/18 转化过程封装起来，先不删除注释掉的代码，验证无误后再删除
//            NetConference netConference = YHCConferenceHelper.copyECConferenceInfo(ecConferenceInfo, false);
//            if (ecConferenceInfo.getMemberInfos() != null) {
//                for (ECConferenceMemberInfo info : ecConferenceInfo.getMemberInfos()) {
//                    YHCConfMember YHCConfMember = new YHCConfMember();
//                    ECAccountInfo member = info.getMember();
//                    if (member == null || info.version == 13 || ConferenceService.memberShouldHide(member.getRoleId())) {
//                        continue;
//                    }
//                    YHCConfMember.setAccount(member.getAccountId());
//                    YHCConfMember.setIdType(member.getEcAccountType().ordinal() + 1);
//                    YHCConfMember.setRoleId(member.getRoleId());
//                    String userName = info.getUserName();
//                    String memberName = member.getUserName();
//                    YHCConfMember.setName(TextUtil.isEmpty(userName) ? memberName : userName);
//                    netConference.getConfMembers().add(YHCConfMember);
//                }
//            }
//            conf_list.add(netConference);
//        }
//        return conf_list;
//    }
//
//
//    public void initSDK() {
//        // 连接SDK
//        if (YTXSDKCoreHelper.isConnected()) {
//            queryConfListDate(false);
//            YHCConferenceMgr.getManager().getConfSetting(null);
//        }
//    }
//
//    @Override
//    public YTXBasePresenter getPresenter() {
//        return null;
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.ytx_activity_sphy2;
//    }
//
//    @Override
//    protected boolean isEnableSwipe() {
//        return false;
//    }
//
//    @Override
//    public boolean hasActionBar() {
//        return false;
//    }
//
//    @Override
//    public void onRefresh() {
//        queryConfListDate(false);
//    }
//
//    @Override
//    public void returnMembers(List<YHCConfMember> YHCConfMemberMembers, int type) {
//
//        if (YHCConfMemberMembers == null || YHCConfMemberMembers.size() < 1) {
//            return;
//        }
//        /**
//         * 传入参数为  PhoneMeetingMember的集合
//         */
//        YHCConfInfo info = new YHCConfInfo();
//        info.setMemberList(YHCConfMemberMembers);
//        YHCConferenceMgr.getManager().startConference(this.getContext(), info);
//    }
//
//    @Override
//    public void onLoadMoreRequested() {
//        pagerNum++;
//        queryConfListDate(true);
//    }
//
//    private float mLastY, mLastX, noSlideY, noSlideBottomY, xDistance, yDistance, downY;
//
//    private int indexConf = 0;
//
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        Intent intent = new Intent();
//        if (/*id == R.id.entity_reser_metting2 ||*/ id == R.id.entity_reser_metting_open2) {
//            intent.setClass(getContext(), YHCConfStartAndReserveActivity.class);
//            startActivity(intent);
//        } else if (/*id == R.id.entity_start_meeting2 ||*/ id == R.id.entity_start_meeting_open2) {
//            intent.setClass(getContext(), YHCJoinConfActivity.class);
//            startActivity(intent);
//        } else if (/*id == R.id.entity_enterprise_metting2 ||*/ id == R.id.entity_enterprise_metting_open2) {
////            if (!TextUtil.isEmpty(ConferenceService.getInstance().mMeetingNo) && ConferenceService.getInstance().mVoipSmallWindow != null) {
////                ConfToasty.error(getString(R.string.yhc_conf_running_tips));
////                return;
////            }
////            enterConnectHardware(intent);
//            intent.setClass(getContext(), YHCHistoryListActivity.class);
//            startActivity(intent);
//        } /*else if (id == R.id.entity_meeting_scan2 || id == R.id.entity_meeting_scan_open2) {
//            if (!TextUtil.isEmpty(ConferenceService.getInstance().mMeetingNo) && ConferenceService.getInstance().mVoipSmallWindow != null) {
//                ConfToasty.error(getString(R.string.yhc_conf_running_tips));
//                return;
//            }
//            intent = new Intent();
////            if (EasyPermissionsEx.hasPermissions(getContext(), needPermissionsCamera)) {
////                intent.setClassName(getContext(), "com.yuntongxun.plugin.zxing.activity.YHCQRScanActivity");
////                startActivity(intent);
////            } else {
////                EasyPermissionsEx.requestPermissions(this, rationaleCamera, PERMISSIONS_REQUEST_CAMERA, needPermissionsCamera);
////            }
//            if (YHCConferenceMgr.getManager().deviceListener != null) {
//                YHCConferenceMgr.getManager().deviceListener.onControlDeviceJoinConf(getActivity(), "", "");
//                ECHandlerHelper.postDelayedRunnOnUI(this::finish, 300);
//            }
//        } else if (id == R.id.entity_meeting_more2 || id == R.id.entity_meeting_more_open2) {
////            controlPlusSubMenu(id == R.id.entity_meeting_more ? 50F : 36.0F);
//        }*/ else if (id == R.id.tv_sz1) {
//            startActivity(new Intent(getActivity(), LoginActivity.class));
//        } else if (id == R.id.tv_sz2) {
//            startActivity(new Intent(getActivity(), SettingConfActivity.class));
//        }
//    }
//
////    private void controlPlusSubMenu(float height) {
////        if (memberMenuHelper == null) {
////            memberMenuHelper = new ConfMemberMenuHelper((AbsRongXinActivity) activity);
////        }
////        if (memberMenuHelper.isShowing()) {
////            memberMenuHelper.dismiss();
////            return;
////        }
////        if (!TextUtil.isEmpty(ConferenceService.getInstance().chatGroupId) && YHCConfConstData.SHOW_CONF_IM) {
////            memberMenuHelper.isAddChat(true);
////        }
////        memberMenuHelper.setHeight(true, height);
////        memberMenuHelper.setType(0);
////        memberMenuHelper.setOnReturnMenuCallback(this);
////        memberMenuHelper.setOnDismissListener(null);
////        memberMenuHelper.tryShow();
////    }
//
//    /**
//     * 通过滑动的偏移量
//     *
//     * @param dy
//     */
//    private void changeTheLayout(float dy) {
//        final ViewGroup.LayoutParams layoutParams = meetinglayout.getLayoutParams();
//        int height = (int) (layoutParams.height - dy);
//        int width;
//        if (height <= maxHeight / 2) {
//        } else {
//        }
//        height = height >= maxHeight ? maxHeight : height;
//        width = height >= maxHeight ? screenWidth : (int) (height * 3.6);
//        if (height <= minHeight) {
//            height = minHeight;
//            width = (int) (minHeight * 3.6);
//        }
//        layoutParams.height = height;
//        layoutParams.width = width;
//        meetinglayout.setLayoutParams(layoutParams);
//    }
//
//
//    @Override
//    public void returnMenu(int menuId) {
//        switch (menuId) {
//            case 1:
//                if (!TextUtil.isEmpty(ConferenceService.getInstance().mMeetingNo) && ConferenceService.getInstance().mVoipSmallWindow != null) {
//                    ConfToasty.error(getString(R.string.yhc_conf_running_tips));
//                    return;
//                }
//                if (YHCConferenceMgr.getManager().deviceListener != null) {
//                    YHCConferenceMgr.getManager().deviceListener.onControlDeviceJoinConf(getActivity(), "", "");
//                    ECHandlerHelper.postDelayedRunnOnUI(this::finish, 300);
//                }
//                break;
//            case 2:
//                startActivity(new Intent(getContext(), YHCEntityRoomListActivity.class));
//                break;
//            case 3:
//                Intent intent = new Intent();
//                intent.setClassName(getContext(), "com.yuntongxun.youhui.ui.activity.SettingConfActivity");
//                startActivity(intent);
//                break;
//        }
//    }
//
//    // TODO: 2018/3/21 此方法只为清除递归清除垃圾会议
////    private void handlerCancelConfs(final List<ECConferenceInfo> list) {
////        if (list == null && list.size() == 0) return;
////        ECConferenceInfo ecConferenceInfo = list.get(0);
////
////        ConferenceService.disMeeting(ecConferenceInfo.getConfId(), false, new OnActionResultCallBack() {
////            @Override
////            public void onActionResult(int resultCode) {
////                list.remove(0);
////                if (list.size() > 0) {
////                    handlerCancelConfs(list);
////                }
////            }
////        });
////    }
//    private AppBarLayout appBar;
//    /**
//     * 大布局背景，遮罩层
//     */
//    private View bgContent;
//    /**
//     * 展开状态下toolbar显示的内容
//     */
//    private View toolbarOpen;
//    /**
//     * 展开状态下toolbar的遮罩层
//     */
//    private View bgToolbarOpen;
//    /**
//     * 收缩状态下toolbar显示的内容
//     */
//    private View toolbarClose;
//    /**
//     * 收缩状态下toolbar的遮罩层
//     */
//    private View bgToolbarClose;
//
//    @Override
//    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//        //垂直方向偏移量
//        int offset = Math.abs(verticalOffset);
//        //最大偏移距离
//        int scrollRange = appBarLayout.getTotalScrollRange();
//        if (offset <= scrollRange / 2) {
//            //当滑动没超过一半，展开状态下toolbar显示内容，根据收缩位置，改变透明值
//            toolbarOpen.setVisibility(View.VISIBLE);
//            toolbarClose.setVisibility(View.GONE);
//            //根据偏移百分比 计算透明值
//            float scale2 = (float) offset / (scrollRange / 2);
//            int alpha2 = (int) (255 * scale2);
////            bgToolbarOpen.setBackgroundColor(Color.argb(alpha2, 41, 84, 184));
//            bgToolbarOpen.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorE60000));
//        } else {
//            //当滑动超过一半，收缩状态下toolbar显示内容，根据收缩位置，改变透明值
//            toolbarClose.setVisibility(View.VISIBLE);
//            toolbarOpen.setVisibility(View.GONE);
//            float scale3 = (float) (scrollRange - offset) / (scrollRange / 2);
//            int alpha3 = (int) (255 * scale3);
////            bgToolbarClose.setBackgroundColor(Color.argb(alpha3, 41, 84, 184));
//            bgToolbarClose.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorE60000));
//        }
//        //根据偏移百分比计算扫一扫布局的透明度值
//        float scale = (float) offset / scrollRange;
//        int alpha = (int) (255 * scale);
//        bgContent.setBackgroundColor(Color.argb(alpha, 41, 84, 184));
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (appBar != null) {
//            appBar.removeOnOffsetChangedListener(this);
//        }
//    }
//}
//
