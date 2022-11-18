package com.sangfor.sdkdemo.primary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sangfor.sdk.SFUemSDK;
import com.sangfor.sdk.base.SFAuthResultListener;
import com.sangfor.sdk.base.SFAuthType;
import com.sangfor.sdk.base.SFBaseMessage;
import com.sangfor.sdk.utils.SFLogN;
import com.sangfor.sdkdemo.R;
import com.sangfor.sdkdemo.utils.Constants;
import com.sangfor.sdkdemo.view.AuthSuccessActivity;


public class PrimaryAuthActivity extends AppCompatActivity implements SFAuthResultListener, View.OnClickListener {
    private static final String TAG = "PrimaryAuthActivity";

    private String mServerAddress = "https://124.128.52.10:8443";  //服务器地址
    private String mUserName = "test2";                        //用户名
    private String mUserPassword = "Passw0rd.2022";               //密码

    private EditText mServerAddressEditText = null;
    private EditText mUserNameEditView = null;
    private EditText mUserPasswordEditView = null;
    private Button mLoginButton = null;

    private ProgressDialog mProgressDialog = null;  // 进度条对话框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vpn_activity_primary_auth);
        //加载UI
        initView();
        //加载上次登录信息
        setLoginInfo();
        /**
         * 设置认证回调,认证结果在SFAuthResultListener的onAuthSuccess、onAuthFailed、onAuthProgress中返回
         * 如果不设置，将接收不到认证结果回调
         */
        SFUemSDK.getInstance().setAuthResultListener(this);

        //免密认证
        startAutoTicket();
    }

    private void startAutoTicket() {
        /**
         * 这里是自动免密认证接口，返回true表示认证成功，此时用户就可以进行资源访问了，
         * 如果返回false,表示当前不满足自动免密条件，需要用户主动调用用户名密码认证接口
         */
        if (SFUemSDK.getInstance().startAutoTicket()){
            showToast("免密成功");
            startActivity(new Intent(PrimaryAuthActivity.this, AuthSuccessActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        Log.i(TAG,"onResume... ");
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i(TAG, "onNewIntent...");
        super.onNewIntent(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //只有走回收流程的时候的那种onPause，isFinishing才为true
        if (isFinishing()) {
            //取消认证回调
            SFLogN.info(TAG,"SFUemSDK setAuthResultListener null");
            /**
             * 注意： 清除回调建议放到onPause()方法而不是onDestroy()中，
             * 避免出现onDestroy()在onCreate()之后执行，onCreate注册的认证回调被onDestory清空的问题
             */
            SFUemSDK.getInstance().setAuthResultListener(null);
        }
    }

    // 初始化界面元素
    private void initView() {
        mServerAddressEditText = findViewById(R.id.vpn_addr_editView);
        mUserNameEditView = findViewById(R.id.svpn_username_editView);
        mUserPasswordEditView = findViewById(R.id.svpn_userPassword_editView);
        //登录按钮
        mLoginButton = findViewById(R.id.svpn_login_button);
        mLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.svpn_login_button) {
            mServerAddress = mServerAddressEditText.getText().toString();
            mUserName = mUserNameEditView.getText().toString();
            mUserPassword = mUserPasswordEditView.getText().toString();

            //开始主认证
            startPrimaryAuth();
        }
    }

    //开始用户名密码认证
    private void startPrimaryAuth() {

        if (TextUtils.isEmpty(mServerAddress)) {
            showToast("VPN服务器地址不能为空");
            return;
        }

        if (TextUtils.isEmpty(mUserName)) {
            showToast("用户名不能为空");
            return;
        }

        showWaitingDialog(false);

        /**
         * 开始用户名密码认证，认证结果会在认证回调onAuthSuccess,onAuthFailed,onAuthProgress中返回
         */
        SFUemSDK.getInstance().startPasswordAuth(mServerAddress, mUserName, mUserPassword);

    }

    /**
     * 认证成功
     *
     * @param message 认证成功信息
     */
    @Override
    public void onAuthSuccess(final SFBaseMessage message) {
        SFLogN.info(TAG, "auth success");

        //关闭进度框
        dismissWaitingDialog();

        saveLoginInfo(); //保存登录信息

        //认证完成，跳转认证成功界面，可以开始访问资源
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(getString(R.string.auth_success));
                startActivity(new Intent(PrimaryAuthActivity.this, AuthSuccessActivity.class));
                finish();
            }
        });
    }

    /**
     * 认证失败
     *
     * @param message  认证失败信息
     */
    @Override
    public void onAuthFailed(final SFBaseMessage message) {
        SFLogN.error2(TAG, "auth failed", "errMsg: " + message.mErrStr);

        //关闭进度框
        dismissWaitingDialog();

        //认证失败
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showErrorMessage(message.mErrStr);
            }
        });
    }

    /**
     * 主认证成功，但需要辅助认证（下一步认证）
     *
     * @param nextAuthType 下一步认证类型
     * @param message      下一步认证信息
     */
    @Override
    public void onAuthProgress(SFAuthType nextAuthType, SFBaseMessage message) {
        SFLogN.info(TAG, "need next auth, authType: " + nextAuthType.name());
        dismissWaitingDialog();

        /**
         * 服务端配置了首次登陆强制修改密码，或者其他二次认证时，认证时会回调此方法,
         * 此时如果不打算适配二次认证，建议给用户提示，让管理员调整配置
         */
        showErrorMessage("暂不支持此种认证类型(" + nextAuthType.toString() + ")");
    }


    //显示“请稍候...”提示框
    public void showWaitingDialog(final boolean isCancelable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog == null || !mProgressDialog.isShowing()) {
                    mProgressDialog = new ProgressDialog(PrimaryAuthActivity.this);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.setTitle("");
                    mProgressDialog.setMessage(PrimaryAuthActivity.this.getString(R.string.waiting));
                    mProgressDialog.setCancelable(isCancelable);
                    mProgressDialog.show();
                }
            }
        });
    }

    //取消“请稍候...”提示框
    public void dismissWaitingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        });
    }

    //显示错误提示对话框
    public void showErrorMessage(final String errMsg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dialog messageDialog = new AlertDialog.Builder(PrimaryAuthActivity.this)
                        .setTitle(PrimaryAuthActivity.this.getString(R.string.info))
                        .setMessage(errMsg)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();

                messageDialog.show();
            }
        });
    }

    //封装Toast
    public void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PrimaryAuthActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }


    //恢复登录信息，重启应用后记录原来的登陆信息
    private void setLoginInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_FILE_NAME, MODE_PRIVATE);
        mServerAddress = sharedPreferences.getString(Constants.KEY_VPN_ADDRESS, mServerAddress);
        mUserName = sharedPreferences.getString(Constants.KEY_USER_NAME, mUserName);
        mUserPassword = sharedPreferences.getString(Constants.KEY_USER_PASSWORD, mUserPassword);

        mServerAddressEditText.setText(mServerAddress);
        mUserNameEditView.setText(mUserName);
        mUserPasswordEditView.setText(mUserPassword);
    }

    //更新保存的登录信息
    private void saveLoginInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_VPN_ADDRESS, mServerAddress);
        //保存用户名和密码，真实场景请加密存储
        editor.putString(Constants.KEY_USER_NAME, mUserName);
        editor.putString(Constants.KEY_USER_PASSWORD, mUserPassword);
        editor.apply();
    }

}
