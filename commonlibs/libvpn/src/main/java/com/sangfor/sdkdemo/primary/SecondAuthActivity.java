package com.sangfor.sdkdemo.primary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sangfor.sdk.SFUemSDK;
import com.sangfor.sdk.base.SFAuthResultListener;
import com.sangfor.sdk.base.SFAuthType;
import com.sangfor.sdk.base.SFBaseMessage;
import com.sangfor.sdk.base.SFChangePswMessage;
import com.sangfor.sdk.base.SFConstants;
import com.sangfor.sdk.base.SFRegetSmsListener;
import com.sangfor.sdk.base.SFSmsMessage;
import com.sangfor.sdk.utils.SFLogN;
import com.sangfor.sdkdemo.R;
import com.sangfor.sdkdemo.utils.Constants;
import com.sangfor.sdkdemo.utils.SFDialogHelper;
import com.sangfor.sdkdemo.view.AuthSuccessActivity;

import java.util.HashMap;
import java.util.Map;

public class SecondAuthActivity extends AppCompatActivity implements SFAuthResultListener, SFRegetSmsListener, View.OnClickListener {
    private static final String TAG = "SecondAuthActivity";
    private static final int DEFAULT_SMS_COUN_TDOWN_TIME = 30;  //短信验证码默认倒计时时间, 以秒为单位

    private String mServerAddress = "https://10.242.4.236";  //服务器地址
    private String mUserName = "xxb";                        //用户名
    private String mUserPassword = "test@1234";               //密码

    private EditText mServerAddressEditText = null;
    private EditText mUserNameEditView = null;
    private EditText mUserPasswordEditView = null;
    private Button mLoginButton = null;


    private View mSmsCodeDialogView = null;     //短信验证码对话框视图
    private ProgressDialog mProgressDialog = null;  // 对话框对象
    private AlertDialog mAuthDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vpn_activity_primary_and_sms_auth);
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
        if (SFUemSDK.getInstance().startAutoTicket()){
            showToast("免密成功");
            startActivity(new Intent(SecondAuthActivity.this, AuthSuccessActivity.class));
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

    /**
     * 把回收资源也同时放到onPause方法中，
     * 避免出现destroy()在onCreate()之后执行，AuthResultListener为空的情况
     */
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

    /**
     * 初始化界面元素
     */
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


    /**
     * 开始主认证，用户名密码或者证书认证
     */
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
        dismissWaitingDialog();

        saveLoginInfo(); //保存登录信息

        //认证完成，跳转认证成功界面，可以开始访问资源
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(getString(R.string.auth_success));
                startActivity(new Intent(SecondAuthActivity.this, AuthSuccessActivity.class));
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
        //显示下一步认证UI界面
        showAuthDialog(nextAuthType, message);
    }

    /**
     * 显示下一步认证UI界面
     */
    public void showAuthDialog(final SFAuthType authType, final SFBaseMessage message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeDialog();
                //获取认证类型对应的标题
                String dialogTitle = SFDialogHelper.getDialogTitle(authType);
                if (TextUtils.isEmpty(dialogTitle)) {
                    showErrorMessage("暂不支持此种认证类型(" + authType.toString() + ")");
                    return;
                }
                //获取认证类型对应的layout布局
                int viewLayoutId = SFDialogHelper.getAuthDialogViewId(authType);
                //创建认证类型对应的对话框显示视图
                final View dialogView = createDialogView(authType, viewLayoutId, message);
                mAuthDialog = new AlertDialog.Builder(SecondAuthActivity.this)
                        .setTitle(dialogTitle)
                        .setView(dialogView)
                        .setCancelable(false)
                        .setPositiveButton(R.string.str_commit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                closeDialog();
                                //构建辅助认证类型所需参数
                                Map<String, String> authParams = new HashMap<String, String>();
                                String errorMsg = buildAuthParams(authType, dialogView, authParams);
                                //检查参数构建是否成功
                                if (TextUtils.isEmpty(errorMsg)) {
                                    showWaitingDialog(false);
                                    //开始辅助认证
                                    SFUemSDK.getInstance().doSecondaryAuth(authType, authParams);
                                } else {
                                    //参数构建失败，进行提示
                                    showErrorMessage(errorMsg);
                                }
                                return;
                            }
                        })
                        .setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //放弃继续认证
                                closeDialog();
                                return;
                            }
                        })
                        .create();
                //显示认证对话框
                mAuthDialog.show();
            }
        });
    }

    /**
     * 创建认证对话框中间显示的视图
     *
     * @param sfAuthType 认证类型
     * @param layoutId   要加载的视图的布局ID
     * @param message    认证附加信息
     * @return 认证对话框视图
     */
    private View createDialogView(SFAuthType sfAuthType, int layoutId, final SFBaseMessage message) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(layoutId, null);
        switch (sfAuthType) {
            case AUTH_TYPE_SMS: {
                mSmsCodeDialogView = dialogView;
                TextView tvTel = dialogView.findViewById(R.id.tv_tel);
                Button btnGetVerficationCode = dialogView.findViewById(R.id.bt_getVerficationCode);
                btnGetVerficationCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setEnabled(false);
                        //调用重新获取短信接口
                        SFUemSDK.getInstance().getSFAuth().regetSmsCode(SecondAuthActivity.this);
                    }
                });

                SFSmsMessage smsMessage = ((SFSmsMessage) message);
                //获取手机号码
                String smsPhoneNum = smsMessage.getPhoneNum();
                if (TextUtils.isEmpty(smsPhoneNum)) {
                    tvTel.setText(R.string.get_phone_number_failed);
                } else {
                    tvTel.setText(getString(R.string.phone_number) + smsPhoneNum);
                }
                //上次发送的短信验证码是否在有效期
                if (smsMessage.isStillValid()) {
                    showToast("上次发送的短信验证码仍在有效期");
                }
                //开启短信码倒计时
                smsCountDownTimer(btnGetVerficationCode, smsMessage.getCountDown());
                break;
            }
            case AUTH_TYPE_RENEW_PASSWORD: {
                TextView tvPolicy = (TextView) dialogView.findViewById(R.id.tv_policy);
                String policy = "";
                //获取密码策略
                if (message instanceof SFChangePswMessage) {
                    policy = ((SFChangePswMessage) message).policyMsg;
                }
                if (TextUtils.isEmpty(policy)) {
                    tvPolicy.setText(R.string.str_no_policy);
                } else {
                    tvPolicy.setText(getString(R.string.str_policy_hint) + "\n" + policy);
                }
                break;
            }
            default:
                break;
        }

        return dialogView;
    }

    /**
     * 短信验证码倒计时器
     *
     * @param button 显示计时的按钮控件
     */
    private void smsCountDownTimer(final Button button, final int countDown) {
        int smsRefreshTime = countDown < 0 ? DEFAULT_SMS_COUN_TDOWN_TIME : countDown;
        //开启短信验证码倒计时，第一个参数为倒计时时间（毫秒），第二个为时间间隔
        new CountDownTimer(smsRefreshTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                button.setText(millisUntilFinished / 1000 + getString(R.string.after_time_resend));
                button.setTextColor(Color.parseColor("#708090"));
                button.setEnabled(false);
            }

            @Override
            public void onFinish() {
                button.setText(R.string.str_resend);
                button.setTextColor(Color.parseColor("#000000"));
                button.setEnabled(true);
            }
        }.start();
    }

    /**
     * 构建辅助认证所需参数
     *
     * @param sfAuthType 认证类型
     * @param dialogView 认证视图
     * @param authParams 保存认证参数
     * @return 参数错误信息
     */
    private String buildAuthParams(SFAuthType sfAuthType, View dialogView, Map<String, String> authParams) {
        String errorMsg = "";
        switch (sfAuthType) {
            case AUTH_TYPE_PASSWORD: {
                EditText etUserName = dialogView.findViewById(R.id.et_username);
                EditText etPwd = dialogView.findViewById(R.id.et_password);
                mUserName = etUserName.getText().toString();
                mUserPassword = etPwd.getText().toString();

                if (TextUtils.isEmpty(mUserName)) {
                    errorMsg = "用户名不能为空";
                    break;
                }

                authParams.put(SFConstants.AUTH_KEY_USERNAME, mUserName);
                authParams.put(SFConstants.AUTH_KEY_PASSWORD, mUserPassword);
                break;
            }

            case AUTH_TYPE_SMS: {
                EditText etSmsCode = dialogView.findViewById(R.id.et_verficationCode);
                String smsCode = etSmsCode.getText().toString();

                if (TextUtils.isEmpty(smsCode)) {
                    errorMsg = "短信验证码不能为空";
                    break;
                }

                authParams.put(SFConstants.AUTH_KEY_SMS, smsCode);
                break;
            }
            case AUTH_TYPE_RADIUS: {
                EditText etRadiusAnswer = dialogView.findViewById(R.id.et_authAnswer);
                String radiusAnswer = etRadiusAnswer.getText().toString();

                if (TextUtils.isEmpty(radiusAnswer)) {
                    errorMsg = "认证答案不能为空";
                    break;
                }

                authParams.put(SFConstants.AUTH_KEY_RADIUS_CODE, radiusAnswer);
                break;
            }
            case AUTH_TYPE_TOKEN:
            case AUTH_TYPE_TOKEN_TOTP:
            case AUTH_TYPE_TOKEN_HTTPS:
            case AUTH_TYPE_TOKEN_RADIUS: {
                EditText etDynamicToken = dialogView.findViewById(R.id.et_dynamicToken);
                String dynamicToken = etDynamicToken.getText().toString();

                if (TextUtils.isEmpty(dynamicToken)) {
                    errorMsg = "动态口令不能为空";
                    break;
                }

                authParams.put(SFConstants.AUTH_KEY_TOKEN, dynamicToken);
                break;
            }
            case AUTH_TYPE_RAND: {
                authParams.put(SFConstants.AUTH_KEY_USERNAME, mUserName);
                authParams.put(SFConstants.AUTH_KEY_PASSWORD, mUserPassword);

                EditText etRandCode = dialogView.findViewById(R.id.et_graphCode);
                String randCode = etRandCode.getText().toString();

                if (TextUtils.isEmpty(randCode)) {
                    errorMsg = "图形校验码不能为空";
                    break;
                }

                authParams.put(SFConstants.AUTH_KEY_RANDCODE, randCode);
                break;
            }
            case AUTH_TYPE_RENEW_PASSWORD: {
                EditText etNewPwd = dialogView.findViewById(R.id.et_newpwd);
                EditText etOldPwd = dialogView.findViewById(R.id.et_oldpwd);
                EditText etReNewPwd = dialogView.findViewById(R.id.et_renewpwd);
                String oldPwd = etOldPwd.getText().toString();
                String newPwd = etNewPwd.getText().toString();
                String reNewPwd = etReNewPwd.getText().toString();

                if (TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(reNewPwd)) {
                    errorMsg = "新密码不能为空";
                    break;
                }

                if (!newPwd.equals(reNewPwd)) {
                    errorMsg = "新密码与确认密码不一致";
                    break;
                }

                authParams.put(SFConstants.AUTH_KEY_RENEW_OLD_PASSWORD, oldPwd);
                authParams.put(SFConstants.AUTH_KEY_REWNEW_NEW_PASSWORD, newPwd);
                break;
            }
            default:
                errorMsg = "暂不支持的认证类型";
                break;
        }

        return errorMsg;
    }

    /**
     * 关闭对话框
     */
    private void closeDialog() {
        if (mAuthDialog != null && mAuthDialog.isShowing()) {
            mAuthDialog.dismiss();
            mAuthDialog = null;
        }
    }

    /**
     * 重新获取短信验证码回调
     *
     * @param success 是否成功获取，true表示成功，false表示失败
     * @param message 包含短信相关信息
     */
    @Override
    public void onRegetSmsCode(final boolean success, final SFSmsMessage message) {
        SFLogN.info(TAG, "onRegetSmsCode result: " + success + ", msg: " + message.mErrStr);

        //更新短信验证码信息
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSmsCodeDialogView == null) {
                    showToast("短信验证码对话框视图未加载");
                    return;
                }

                final Button btnGetVerficationCode = mSmsCodeDialogView.findViewById(R.id.bt_getVerficationCode);
                if (success) {
                    //更新倒计时
                    smsCountDownTimer(btnGetVerficationCode, message.getCountDown());
                    showToast(getString(R.string.reget_sms_code_success));
                } else {
                    btnGetVerficationCode.setEnabled(true);
                    showToast(getString(R.string.reget_sms_code_failed) + message.mErrStr);
                }
            }
        });
    }


    //显示“请稍候...”提示框
    public void showWaitingDialog(final boolean isCancelable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog == null || !mProgressDialog.isShowing()) {
                    mProgressDialog = new ProgressDialog(SecondAuthActivity.this);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.setTitle("");
                    mProgressDialog.setMessage(SecondAuthActivity.this.getString(R.string.waiting));
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
                Dialog messageDialog = new AlertDialog.Builder(SecondAuthActivity.this)
                        .setTitle(SecondAuthActivity.this.getString(R.string.info))
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
                Toast.makeText(SecondAuthActivity.this, text, Toast.LENGTH_SHORT).show();
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
