package com.tencent.qcloud.tim.demo.thirdpush;

import android.text.TextUtils;

import com.mob.pushsdk.MobPush;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMOfflinePushConfig;
import com.tencent.qcloud.tim.demo.utils.BrandUtil;
import com.tencent.qcloud.tim.demo.utils.DemoLog;
import com.tencent.qcloud.tim.demo.utils.PrivateConstants;

/**
 * 用来保存厂商注册离线推送token的管理类示例，当登陆im后，通过 setOfflinePushToken 上报证书 ID 及设备 token 给im后台。开发者可以根据自己的需求灵活实现
 */

public class ThirdPushTokenMgr {

    public static final boolean USER_GOOGLE_FCM = false;
    private static final String TAG = ThirdPushTokenMgr.class.getSimpleName();
    private String mThirdPushToken;

    public static ThirdPushTokenMgr getInstance() {
        return ThirdPushTokenHolder.instance;
    }

    public String getThirdPushToken() {
        return mThirdPushToken;
    }

    public void setThirdPushToken(String mThirdPushToken) {
        this.mThirdPushToken = mThirdPushToken;
    }

    public void setPushTokenToTIM() {
        String token = ThirdPushTokenMgr.getInstance().getThirdPushToken();
        MobPush.setDeviceTokenByUser(token);
        if (TextUtils.isEmpty(token)) {
            DemoLog.e("TencentIM", "setPushTokenToTIM third token is empty");
            return;
        }
        V2TIMOfflinePushConfig v2TIMOfflinePushConfig = null;
        if (BrandUtil.isBrandXiaoMi()) {
            v2TIMOfflinePushConfig = new V2TIMOfflinePushConfig(PrivateConstants.XM_PUSH_BUZID, token);
        } else if (BrandUtil.isBrandHuawei()) {
            v2TIMOfflinePushConfig = new V2TIMOfflinePushConfig(PrivateConstants.HW_PUSH_BUZID, token);
        } else if (BrandUtil.isBrandMeizu()) {
            v2TIMOfflinePushConfig = new V2TIMOfflinePushConfig(PrivateConstants.MZ_PUSH_BUZID, token);
        } else if (BrandUtil.isBrandOppo()) {
            v2TIMOfflinePushConfig = new V2TIMOfflinePushConfig(PrivateConstants.OPPO_PUSH_BUZID, token);
        } else if (BrandUtil.isBrandVivo()) {
            v2TIMOfflinePushConfig = new V2TIMOfflinePushConfig(PrivateConstants.VIVO_PUSH_BUZID, token);
        } else {
            return;
        }

        V2TIMManager.getOfflinePushManager().setOfflinePushConfig(v2TIMOfflinePushConfig, new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                DemoLog.e("TencentIM", "setOfflinePushToken err code = " + code);
            }

            @Override
            public void onSuccess() {
                DemoLog.e("TencentIM", "setOfflinePushToken success");
            }
        });
    }

    private static class ThirdPushTokenHolder {
        private static final ThirdPushTokenMgr instance = new ThirdPushTokenMgr();
    }
}
