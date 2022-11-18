package com.geek.appcommon.ytx;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.im.R;
import com.yuntongxun.im.impl.MeetingCallBackImpl;
import com.yuntongxun.im.ui.group.GroupSelectContacts;
import com.yuntongxun.im.ui.personcard.CardSelectContactsActivity;
import com.yuntongxun.plugin.common.RXConfig;
import com.yuntongxun.plugin.common.RongXinApplicationContext;
import com.yuntongxun.plugin.common.YTXAppMgr;
import com.yuntongxun.plugin.common.common.base.YTXVoipWarningDialog;
import com.yuntongxun.plugin.common.common.helper.YTXAuthTagHelper;
import com.yuntongxun.plugin.common.common.utils.EasyPermissionsEx;
import com.yuntongxun.plugin.common.common.utils.TextUtil;
import com.yuntongxun.plugin.common.common.utils.ToastUtil;
import com.yuntongxun.plugin.common.ui.YTXPermissionActivity;
import com.yuntongxun.plugin.conference.bean.YHCConfInfo;
import com.yuntongxun.plugin.conference.bean.YHCConfMember;
import com.yuntongxun.plugin.conference.manager.YHCConferenceMgr;
import com.yuntongxun.plugin.conference.manager.inter.CONF_TYPE;
import com.yuntongxun.plugin.dial.YTXVoIPImpl;
import com.yuntongxun.plugin.dial.common.utils.YTXNetPhoneUtils;
import com.yuntongxun.plugin.favorite.helper.YTXFavoriteManager;
import com.yuntongxun.plugin.greendao3.helper.RXEmployee;
import com.yuntongxun.plugin.im.dao.bean.RXMessage;
import com.yuntongxun.plugin.im.dao.dbtools.DBECMessageTools;
import com.yuntongxun.plugin.im.manager.YTXIMPanel;
import com.yuntongxun.plugin.im.manager.YTXIMPluginManager;
import com.yuntongxun.plugin.im.manager.YTXOnCallLogClickListener;
import com.yuntongxun.plugin.im.manager.YTXOnIMPanelClickListener;
import com.yuntongxun.plugin.im.manager.YTXOnQueryByDBRXSpecialListener;
import com.yuntongxun.plugin.im.manager.YTXOnQueryEmployeeByAccountOrMtelListener;
import com.yuntongxun.plugin.im.manager.YTXOnReceiverFriendValMsgListener;
import com.yuntongxun.plugin.im.ui.chatting.fragment.YTXChattingFragment;
import com.yuntongxun.plugin.im.ui.chatting.helper.YTXIMChattingHelper;
import com.yuntongxun.plugin.phonemeeting.bean.YTXPhone;
import com.yuntongxun.plugin.phonemeeting.conf.YTXPhoneConfRunningActity;
import com.yuntongxun.plugin.phonemeeting.conf.YTXPhoneMeetingService;
import com.yuntongxun.plugin.rxcontacts.RXContactHelper;
import com.yuntongxun.plugin.rxcontacts.dao.DBSpecialFocusContactTools;
import com.yuntongxun.plugin.rxcontacts.enterprise.SearchEntranceActivity;
import com.yuntongxun.plugin.rxcontacts.myfriend.FriendHelper;
import com.yuntongxun.plugin.voip.YTXVoip;
import com.yuntongxun.plugin.voip.model.YTXCallRecordFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by jorstinchan on 2017/3/26.
 */

public class RXPluginHelper {

    private Context mLauncher;

    public RXPluginHelper(Context context) {
        this.mLauncher = context;
    }

    public void init() {
        // 添加IM扩展内容
        // 个人: 图片、拍照、小视频、阅后即焚、文件
        // 群: 图片、拍照、小视频、文件
        // 文件传输助手: 图片、拍照、小视频、文件
        if (YTXIMPluginManager.panelList != null) {
            YTXIMPluginManager.panelList.clear();
        }
        initRongXin();
    }

    /**
     * 初始化容信面板
     */
    private void initRongXin() {
        boolean openRedPacket = YTXAuthTagHelper.getInstance().isSupportRedPacket() && RXConfig.RED_PACKET;
        // 语音通话(个人)
        if (YTXAuthTagHelper.getInstance().isSupportVoiceCall()) {
            YTXIMPluginManager.getManager().addPanel(buildVoiceCallPanel(2));
        }
        // 视频通话(个人)
        if (YTXAuthTagHelper.getInstance().isSupportVideoCall()) {
            YTXIMPluginManager.getManager().addPanel(buildVideoCallPanel(3));
        }

        // 电话会议(群)
        if (YTXAuthTagHelper.getInstance().isSupportPhoneMeeting()) {
            YTXIMPluginManager.getManager().addPanel(buildPhoneMeetingPanel(2));
        }

        //群内通话(群)
        YTXIMPluginManager.getManager().addPanel(buildGroupCallPanel(4));


        // 个人名片(个人)
        YTXIMPluginManager.getManager().addPanel(buildCardPanel(YTXIMPanel.PANELTYPE.SINGLECHAT, 7));
        // 个人名片(群)
        YTXIMPluginManager.getManager().addPanel(buildCardPanel(YTXIMPanel.PANELTYPE.GROUPCHAT, 6));

        // 收藏(群、个人)
        YTXIMPluginManager.getManager().addPanel(buildFavoritePanel(YTXIMPanel.PANELTYPE.BOTH, 0));
        // 文件助手面板 链接分享 收藏
        YTXIMPluginManager.getManager().addPanel(buildFavoritePanel(YTXIMPanel.PANELTYPE.FILE_TRANSFER, 0));
    }

    /**
     * 构建语音通话面板
     *
     * @param index
     * @return
     */
    private YTXIMPanel buildVoiceCallPanel(int index) {
        return new YTXIMPanel.PanelBuilder(getString(R.string.app_panel_call), R.drawable.ytx_chattingfooter_call_selector,
                YTXIMPanel.PANELTYPE.SINGLECHAT).setOnIMPannelClickListener(new YTXOnIMPanelClickListener() {
            @Override
            public void onIMPanelClick(Context context, String... id) {

                if (inCalling(context)) {
                    return;
                }
                if (RXConfig.isRongXinPlatform()) {
                    YTXNetPhoneUtils.callVoip(context, id[0], id[0], id[0], "@chat");
                } else {
                    if (!getAppOps(context) && !YTXAppMgr.getIsshowDialog()) {
                        YTXVoipWarningDialog.showWarningDialog(context);
                        return;
                    }
                    RXEmployee employee = RXContactHelper.getInstance().getRXEmployee(id[0]);
                    if (employee != null) {
                        Log.e("12121212121212121212", "id="+id[0]+"     mtel="   +employee.getMtel());
                        YTXNetPhoneUtils.callVoip(context, id[0], employee.getMtel(), employee.getUnm(), "@chat");
                    }
                }
            }
        }).panelIndex(index).build();
    }


    /**
     * 构建IM面板视频聊天
     *
     * @param index
     * @return
     */
    private YTXIMPanel buildVideoCallPanel(int index) {
        return new YTXIMPanel.PanelBuilder(getString(R.string.app_panel_p2pvideo), R.drawable.ytx_chattingfooter_video_selector, YTXIMPanel.PANELTYPE.SINGLECHAT)
                .setOnIMPannelClickListener(new YTXOnIMPanelClickListener() {
                    @Override
                    public void onIMPanelClick(Context context, String... id) {
                        videoClick(context, id);
                    }
                }).panelIndex(index).build();
    }

    /**
     * 构建IM面板电话会议
     *
     * @param index
     * @return
     */
    private YTXIMPanel buildPhoneMeetingPanel(int index) {
        return new YTXIMPanel.PanelBuilder(getString(R.string.main_plug_phone_meeting), R.drawable.ytx_chattingfooter_call_selector,
                YTXIMPanel.PANELTYPE.GROUPCHAT).setOnIMPannelClickListener(new YTXOnIMPanelClickListener() {
            @Override
            public void onIMPanelClick(Context context, String... ids) {
                if (ids == null || ids.length == 0) {
                    return;
                }
                if (inCalling(context)) {
                    return;
                }
                ArrayList<YTXPhone> users = new ArrayList<YTXPhone>();
                for (String account : ids) {
                    RXEmployee mRXEmployee = RXContactHelper.getInstance().getRXEmployee(account);
                    YTXPhone phone = new YTXPhone();
                    phone.setOutCall(true);
                    phone.setAccount(mRXEmployee.getMtel());
                    users.add(phone);
                }
                if (users.size() > 0) {
                    YTXPhoneMeetingService.startPhoneMeeting(context, users, MeetingCallBackImpl.getInstance());
                }
            }

            @Override
            public int onPanelSelectLimit() {
                return YTXAppMgr.getPluginUser().getPhoneMeetingNum();
            }

            @Override
            public boolean onPanelSelectContacts(Context context) {
                return !inCalling(context);
            }
        }).panelIndex(index).build();
    }




    /**
     * 构建群内通话面板
     */
    private YTXIMPanel buildGroupCallPanel(int index) {
        return new YTXIMPanel.PanelBuilder(getString(R.string.app_panel_group_call), R.drawable.ytx_chattingfooter_video_selector, YTXIMPanel.PANELTYPE.GROUPCHAT)
                .setOnIMPannelClickListener(new YTXOnIMPanelClickListener() {
                    @Override
                    public void onIMPanelClick(Context context, String... ids) {

                    }
                    @Override
                    public boolean onPanelSelectContacts(Context context) {
                        return !inCalling(context);
                    }
                    //群内通话限制9个人
                    @Override
                    public int onPanelSelectLimit() {
                        return 9;
                    }

                    @Override
                    public void onGroupIMPanelClick(Context context, String groupId, String... ids) {
                        //选择到参与人数，跳转到九宫格界面
                        if (ids == null || ids.length == 0) {
                            return;
                        }
                        if (inCalling(context)) {
                            return;
                        }
                        //在插件里面写九宫格界面，然后优化相关逻辑
                        ArrayList<YHCConfMember> confMembers = new ArrayList<>();
                        for (String account : ids) {
                            if (account != null && account.equals(YTXAppMgr.getUserId())) {
                                continue;
                            }
                            YHCConfMember confMember = new YHCConfMember();
                            confMember.setAccount(account);
                            confMember.setOutCall(false);
                            confMembers.add(confMember);
                        }
                        YHCConfInfo info = new YHCConfInfo();
                        info.setConfName(String.format(RongXinApplicationContext.getContext().getString(com.yuntongxun.plugin.phonemeeting.R.string.yhc_str_meeting_name),YTXAppMgr.getUserName()));
                        info.setConfType(CONF_TYPE.CONF_GROUP);
                        info.setMemberList(confMembers);
                        info.setMaxMember(9);
                        //将群组ID也传过去，保证下次在九宫格会议内跳转到邀请群内成员界面
                        info.setGroupId(groupId);
                        YHCConferenceMgr.getManager().startConference(context,
                                info);
                    }
                }).panelIndex(index).build();
    }





    public void initVoIP() {
        YTXCallRecordFactory.getInstance().setCallRecordFactoryListener(new YTXCallRecordFactory.OnCallRecordFactoryListener() {
            @Override
            public void onSendCallRecordMessage(ECMessage message) {
                if (message == null) {
                    return;
                }
                YTXIMChattingHelper.getInstance().sendECMessage(message);
            }

            @Override
            public void onInsertCallRecordMessage(ECMessage message) {
                DBECMessageTools.getInstance().insert(RXMessage.copyForm(message));
            }
        });
    }

    public void initCallLogClick() {
        YTXIMPluginManager.getManager().setOnCallLogClickListener(new YTXOnCallLogClickListener() {
            @Override
            public void onVoiceCallLogClick(Context mContext, String session) {
                if (YTXIMPluginManager.getManager().isVideoMetting()) {
                    return;
                }
                if (YTXIMPluginManager.getManager().isVocieMetting()) {
                    return;
                }
                String displayName = RXContactHelper.getInstance().getRXEmployee(session).getUnm();
                if (EasyPermissionsEx.hasPermissions(mContext, YTXPermissionActivity.needPermissionsVoiceExternal)) {
                    YTXVoip.startCallAction(mContext, ECVoIPCallManager.CallType.values()[0], displayName,
                            session, session, "@chat", false, YTXVoIPImpl.getInstance());
                } else {
                    EasyPermissionsEx.requestPermissions(mContext, mContext.getString(com.yuntongxun.plugin.dial.R.string.rationaleVoiceExternal),
                            YTXPermissionActivity.PERMISSIONS_REQUEST_VOICE_EXTERNAL, YTXPermissionActivity.needPermissionsVoiceExternal);
                }
            }

            @Override
            public void onVideoCallLogClick(Context mContext, String session) {
                if (!YTXAuthTagHelper.getInstance().isSupportVideoCall()) {
                    ToastUtil.showMessage(R.string.ytx_video_not_permission);
                    return;
                }
                if (YTXIMPluginManager.getManager().isVideoMetting()) {
                    return;
                }
                if (YTXIMPluginManager.getManager().isVocieMetting()) {
                    return;
                }
                String displayName = RXContactHelper.getInstance().getRXEmployee(session).getUnm();
                if (EasyPermissionsEx.hasPermissions(mContext, YTXPermissionActivity.needPermissionsCameraExternal)) {
                    YTXNetPhoneUtils.callVideo(mContext, session, TextUtil.isEmpty(displayName) ? session : displayName, "@chat");
                } else {
                    EasyPermissionsEx.requestPermissions(mContext, mContext.getString(R.string.rationaleCameraExternal),
                            YTXPermissionActivity.PERMISSIONS_REQUEST_CAMERA_EXTERNAL, YTXPermissionActivity.needPermissionsCameraExternal);
                }
            }
        });

        YTXIMPluginManager.getManager().setOnQueryByDBRXSpecialListener(new YTXOnQueryByDBRXSpecialListener() {
            @Override
            public boolean onSpecialFocusQuery(String MTel) {
                return DBSpecialFocusContactTools.getInstance().isSpecialFocusByPhoneNum(MTel);
            }
        });

        YTXIMPluginManager.getManager().setOnReceiverFriendValMsgListener(new YTXOnReceiverFriendValMsgListener() {
            @Override
            public int onReceiverFriendValMsg(ECMessage message) {
                return FriendHelper.getInstance().handlePushFriendMessage(message);
            }
        });

        YTXIMPluginManager.getManager().setOnQueryEmployeeByAccountOrMtelListener(new YTXOnQueryEmployeeByAccountOrMtelListener() {
            @Override
            public RXEmployee onGetEmployee(String accountorMtel) {
                //这里不直接读缓存，如果账号被冻结后，这个状态只能从数据库查询
//                return DBRXEmployeeTools.getInstance().queryEmployeeByAccountOrMTel(accountorMtel);
                return RXContactHelper.getInstance().getRXEmployee(accountorMtel, true);
            }
        });


    }


    private String getString(int res) {
        return mLauncher.getString(res);
    }

    private static boolean inCalling(Context context) {
        return inCalling(context, 0);
    }

    /**
     * 是否正在通话
     * type 0 音视频 1 视频会议
     *
     * @return
     */
    private static boolean inCalling(Context context, int type) {
        if (YTXVoip.getCallService().isHoldingCall()) {
            ToastUtil.showMessage(R.string.tip_in_call_progress);
            return true;
        }
//        if (VoiceMeetingService.inMeeting()) {
//            ToastUtil.showMessage(R.string.tip_in_voice_conference_progress);
//            return true;
//        }
//        if (type != 1 && (VideoMeetingService.inMeeting() || ConferenceService.isInMeeting())) {
//            ToastUtil.showMessage(R.string.tip_in_video_conference_progress);
//            return true;
//        }
        if (YTXPhoneMeetingService.inMeeting()) {
            ToastUtil.showMessage(R.string.tip_in_talk_room_progress);
            context.startActivity(new Intent(context, YTXPhoneConfRunningActity.class));
            return true;
        }

        return false;
    }

    /**
     * 发起群聊->选择联系人
     *
     * @param context
     */
    public static void startGroupSelectContacts(Context context) {
        Intent intent = new Intent(context, GroupSelectContacts.class);
        intent.putExtra(GroupSelectContacts.TITLE, /*context.getString(R.string.main_plus_chat)*/"");
        intent.putExtra(GroupSelectContacts.FLAG_GROUP_TYPE, 0);//0表示创建群 1邀请成员进群
        intent.putExtra(GroupSelectContacts.LIMIT_COUNT, 50);//
        intent.putExtra(GroupSelectContacts.LIMIT_TIPS, context.getString(R.string.ytx_new_chat_select_limit));
        intent.putExtra(GroupSelectContacts.FLAG_TYPE, 1);
        context.startActivity(intent);
    }


    /**
     * 打开添加朋友
     *
     * @param context
     */
    public static void startFriend(Context context) {
        Intent intent = new Intent(context, SearchEntranceActivity.class);
        context.startActivity(intent);
    }



    /**
     * 视频通话点击事件处理
     *
     * @param context
     * @param id
     */
    private void videoClick(final Context context, final String... id) {
        if (context == null) {
            return;
        }
        if (inCalling(context)) {
            return;
        }

        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return;
        }
        if (!YTXAuthTagHelper.getInstance().isSupportVideoCall()) {
            ToastUtil.showMessage(R.string.ytx_video_not_permission);
            return;
        }


        if (!getAppOps(context) && !YTXAppMgr.getIsshowDialog()) {
            YTXVoipWarningDialog.showWarningDialog(context);
            return;
        }

        if (RXConfig.isRongXinPlatform()) {
            YTXNetPhoneUtils.callVideo(context, id[0], id[0], "@chat");
        } else {
            final RXEmployee employee = RXContactHelper.getInstance().getRXEmployee(id[0]);
            if (employee != null) {
                YTXNetPhoneUtils.callVideo(context, id[0], employee.getUnm(), "@chat");
            }
        }

    }
    /**
     * 判断 悬浮窗口权限是否打开
     *
     * @param context 上下文
     * @return true 允许  false禁止
     */
    public static boolean getAppOps(Context context) {

        final int version = Build.VERSION.SDK_INT;
        if (version < 19) {
            return false;
        }
        try {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class clazz = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = clazz.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] obj = new Object[3];
            obj[0] = 24;
            obj[1] = Binder.getCallingUid();
            obj[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, obj)).intValue();
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {
        }
        return false;
    }

    /**
     * 构建个人名片面板
     *
     * @param index
     * @return
     */
    private YTXIMPanel buildCardPanel(YTXIMPanel.PANELTYPE type, int index) {
        return new YTXIMPanel.PanelBuilder(getString(R.string.app_panel_personcard), R.drawable.ytx_chattingfooter_personcard_selector, type)
                .setOnIMPannelClickListener(new YTXOnIMPanelClickListener() {
                    @Override
                    public void onIMPanelClick(Context context, String... id) {
                        Intent intent = new Intent();
                        intent.setClass(context, CardSelectContactsActivity.class);
                        intent.putExtra(YTXChattingFragment.RECIPIENTS, id[0]);
                        intent.putExtra(YTXChattingFragment.LIMIT_STATE, true);
                        context.startActivity(intent);
                    }
                })
                .panelIndex(index).build();
    }


    /**
     * 构建收藏面板
     *
     * @param index
     * @return
     */
    private YTXIMPanel buildFavoritePanel(final YTXIMPanel.PANELTYPE type, int index) {
        return new YTXIMPanel.PanelBuilder(getString(R.string.app_panel_favorite), R.drawable.ytx_app_favorite, type)
                .setOnIMPannelClickListener(new YTXOnIMPanelClickListener() {
                    @Override
                    public void onIMPanelClick(Context context, String... id) {
                        YTXFavoriteManager.startSelectFavorite(context, id[0], type == YTXIMPanel.PANELTYPE.FILE_TRANSFER);
                    }
                }).panelIndex(index).build();
    }



}
