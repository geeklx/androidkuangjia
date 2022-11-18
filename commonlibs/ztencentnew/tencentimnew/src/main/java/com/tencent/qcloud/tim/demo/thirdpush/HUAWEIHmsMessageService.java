package com.tencent.qcloud.tim.demo.thirdpush;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;
import com.tencent.qcloud.tim.demo.utils.BrandUtil;
import com.tencent.qcloud.tim.demo.utils.DemoLog;

public class HUAWEIHmsMessageService extends HmsMessageService {

    private static final String TAG = HUAWEIHmsMessageService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage message) {
        DemoLog.e("TencentIM", "onMessageReceived message=" + message);
    }

    @Override
    public void onMessageSent(String msgId) {
        DemoLog.e("TencentIM", "onMessageSent msgId=" + msgId);
    }

    @Override
    public void onSendError(String msgId, Exception exception) {
        DemoLog.e("TencentIM", "onSendError msgId=" + msgId);
    }

    @Override
    public void onNewToken(String token) {
        DemoLog.e("TencentIM", "onNewToken token=" + token);
        ThirdPushTokenMgr.getInstance().setThirdPushToken(token);
        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
    }

    @Override
    public void onTokenError(Exception exception) {
        DemoLog.e("TencentIM", "onTokenError exception=" + exception);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    //    @Override
//    public void onMessageDelivered(String msgId, Exception exception) {
//        DemoLog.e("TencentIM", "onMessageDelivered msgId=" + msgId);
//    }


    public static void updateBadge(final Context context, final int number) {
        if (!BrandUtil.isBrandHuawei()) {
            return;
        }
        DemoLog.e("TencentIM", "huawei badge = " + number);
        try {
            Bundle extra = new Bundle();
            extra.putString("package", "com.fosung.lighthouse.dt2");
            extra.putString("class", "com.geek.appsplash.WelComeActivity");
            extra.putInt("badgenumber", number);
            context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, extra);
        } catch (Exception e) {
            DemoLog.w("TencentIM", "huawei badge exception: " + e.getLocalizedMessage());
        }
    }
}
