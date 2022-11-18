package com.yuntongxun.youhui.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yuntongxun.confwrap.WrapManager;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.plugin.common.YTXPluginUser;
import com.yuntongxun.plugin.common.common.utils.ConfToasty;
import com.yuntongxun.plugin.common.common.utils.EasyPermissionsEx;
import com.yuntongxun.plugin.common.common.utils.LogUtil;
import com.yuntongxun.plugin.common.ui.AbsRongXinActivity;
import com.yuntongxun.plugin.common.ui.YTXPermissionActivity;
import com.yuntongxun.youhui.R;

import static com.yuntongxun.plugin.common.YTXSDKCoreHelper.OnConnectStateListener;

public class LoginActivity extends AbsRongXinActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText et_account, et_name;
    private Button btn_login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youhui_activity_login);
        et_account = (EditText) findViewById(R.id.et_account);
        btn_login = (Button) findViewById(R.id.btn_login);
        et_name = (EditText) findViewById(R.id.et_name);
        btn_login.setOnClickListener(onClickListener);

//        if (YTXAppMgr.getPluginUser() != null) {
//            LogUtil.d(TAG, "SDK auto connect...");
//            WrapManager.getInstance().app_AutoLogin();
//        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!EasyPermissionsEx.hasPermissions(LoginActivity.this, YTXPermissionActivity.needPermissionsJoin)) {
                EasyPermissionsEx.requestPermissions(LoginActivity.this, getString(R.string.rationaleJoin), YTXPermissionActivity.PERMISSIONS_REQUEST_JOIN, YTXPermissionActivity.needPermissionsJoin);
                return;
            }
            String account = et_account.getText().toString();
            String name = et_name.getText().toString();
            if (TextUtils.isEmpty(account) || TextUtils.isEmpty(name)) {
                ConfToasty.error("ID和名字两者都必须填写");
                return;
            }
            if (name.length() > 12) {
                ConfToasty.error(getString(R.string.yhc_str_name_length_range));
                return;
            }
            showPostingDialog();
            /**---------APPKEY,APPTOKEN配置相关--------------*/
            YTXPluginUser user = new YTXPluginUser();
            user.setUserId(account);
            user.setUserName(name);
            user.setAppKey("c086a3af996e11e883536c92bf5b8b62");
            user.setAppToken("cd12eca17f7ac210b8e566e306346c99");
            WrapManager.getInstance().app_Login("server_config_158.xml", user, new OnConnectStateListener() {
                @Override
                public void onConnectSuccess() {
                    Intent action = new Intent(LoginActivity.this, LauncherUI.class);
                    startActivity(action);
                    finish();
                }

                @Override
                public void onConnectFailed(ECError error) {
//                    int error = intent.getIntExtra("error", 0);
                    ConfToasty.error("" + WrapManager.getConnectState());
                    LogUtil.e(TAG, "登入失败"+ error.errorMsg+ error.errorCode);
                }
            });

        }
    };



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
