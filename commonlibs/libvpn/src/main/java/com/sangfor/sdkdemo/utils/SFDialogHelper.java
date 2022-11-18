package com.sangfor.sdkdemo.utils;

import com.sangfor.sdk.base.SFAuthType;
import com.sangfor.sdkdemo.R;

public class SFDialogHelper {
    /**
     * 对话框标题
     *
     * @param authType 认证类型
     * @return 对话框标题
     */
    public static String getDialogTitle(SFAuthType authType) {
        switch (authType) {
            case AUTH_TYPE_PASSWORD:
                return "密码认证";
            case AUTH_TYPE_CERTIFICATE:
                return "证书认证";
            case AUTH_TYPE_SMS:
            case AUTH_TYPE_PRIMARY_SMS:
                return "短信认证";
            case AUTH_TYPE_RADIUS:
                return "挑战认证";
            case AUTH_TYPE_TOKEN:
            case AUTH_TYPE_TOKEN_RADIUS:
            case AUTH_TYPE_TOKEN_TOTP:
            case AUTH_TYPE_TOKEN_HTTPS:
                return "令牌认证";
            case AUTH_TYPE_RAND:
                return "图形校验码";
            case AUTH_TYPE_RENEW_PASSWORD:
                return "修改密码";
            default:
                return "";
        }
    }

    public static int getAuthDialogViewId(SFAuthType authType) {
        switch (authType) {
            case AUTH_TYPE_PASSWORD:
                return R.layout.vpn_dialog_pwd;
            case AUTH_TYPE_CERTIFICATE:
                return R.layout.vpn_dialog_certificate;
            case AUTH_TYPE_SMS:
            case AUTH_TYPE_PRIMARY_SMS:
                return R.layout.vpn_dialog_sms;
            case AUTH_TYPE_RADIUS:
                return R.layout.vpn_dialog_challenge;
            case AUTH_TYPE_TOKEN:
            case AUTH_TYPE_TOKEN_RADIUS:
            case AUTH_TYPE_TOKEN_TOTP:
            case AUTH_TYPE_TOKEN_HTTPS:
                return R.layout.vpn_dialog_token;
            case AUTH_TYPE_RAND:
                return R.layout.vpn_dialog_graph_check;
            case AUTH_TYPE_RENEW_PASSWORD:
                return R.layout.vpn_dialog_force_update_pwd;
            default:
                return -1;
        }
    }
}
