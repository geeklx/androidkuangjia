package com.sangfor.sdkdemo.view;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Process;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.sangfor.sdk.SFUemSDK;
import com.sangfor.sdk.base.SFAuthResultListener;
import com.sangfor.sdk.base.SFAuthType;
import com.sangfor.sdk.base.SFBaseMessage;
import com.sangfor.sdk.base.SFChangePswMessage;
import com.sangfor.sdk.base.SFConstants;
import com.sangfor.sdk.base.SFRegetRandCodeListener;
import com.sangfor.sdk.base.SFRegetSmsListener;
import com.sangfor.sdk.base.SFSmsMessage;
import com.sangfor.sdk.utils.SFLogN;
import com.sangfor.sdkdemo.R;
import com.sangfor.sdkdemo.utils.CertUtils;
import com.sangfor.sdkdemo.utils.Constants;
import com.sangfor.sdkdemo.utils.PermissionUtil;
import com.sangfor.sdkdemo.utils.SFDialogHelper;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PrimarySmsLoginActivity extends AppCompatActivity implements SFAuthResultListener, SFRegetSmsListener,
        SFRegetRandCodeListener, View.OnClickListener {
    private static final String TAG = "PrimarySmsLoginActivity";
    private static final int PERMISSON_REQUEST_CODE = 32;       //动态权限请求码
    private static final int CERTFILE_REQUEST_CODE = 33;        //当证书认证是主认证时获取证书路径请求码
    private static final int DIALOG_CERTFILE_REQUEST_CODE = 34; //当证书认证是辅助认证时获取证书路径请求码
    private static final int DEFAULT_SMS_COUN_TDOWN_TIME = 30;  //短信验证码默认倒计时时间, 以秒为单位

    private String mVpnAddress = "https://10.242.1.191:443";

    enum SERVER_MODE {
        VPN,
        SDP
    }

    private SERVER_MODE mServerMode = SERVER_MODE.SDP;

    private String mSdpAddress = "https://10.242.4.235:443";
    private String mPhoneNumber = "86-15111081590@sms1";

    //View
    private AlertDialog mAuthDialog = null;
    private EditText mVpnAddressEditText = null;
    private EditText mPhoneNumberEditView = null;
    private EditText mUserPasswordEditView = null;
    private RadioGroup mAuthMethodRadioGroup = null;
    private Button mLoginButton = null;
    private Button mSpaConfigButton = null;

    private View mRandCodeDialogView = null;    //图形校验码对话框视图
    private View mSmsCodeDialogView = null;     //短信验证码对话框视图
    private EditText mCertPathDialogEditView = null;//证书路径输入框
    private ProgressDialog mProgressDialog = null;  // 对话框对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vpn_activity_primary_sms_login);
        //加载UI
        initView();
        //加载上次登录信息
        setLoginInfo();
        /**
         * 设置认证回调,认证结果在SFAuthResultListener的onAuthSuccess、onAuthFailed、onAuthProgress中返回
         * 如果不设置，将接收不到认证结果
         */
        SFUemSDK.getInstance().setAuthResultListener(this);

        if (SFUemSDK.getInstance().startAutoTicket()){
            showToast("免密成功");
            startActivity(new Intent(PrimarySmsLoginActivity.this, AuthSuccessActivity.class));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消认证回调
        SFLogN.info(TAG,"SFUemSDK setAuthResultListener ");
        SFUemSDK.getInstance().setAuthResultListener(null);
    }

    /**
     * 初始化界面元素
     */
    private void initView() {
        mVpnAddressEditText = findViewById(R.id.vpn_addr_editView);
        mPhoneNumberEditView = findViewById(R.id.svpn_username_editView);
        //登录按钮
        mLoginButton = findViewById(R.id.svpn_login_button);
        mLoginButton.setOnClickListener(this);
        //认证UI切换按钮
        mAuthMethodRadioGroup = findViewById(R.id.svpn_auth_tabheader);
        mAuthMethodRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //监听认证按钮，动态改变布局显示
                //UI布局切换到用户名密码认证
                if (checkedId == R.id.svpn_userAuth_tabheader) {
                    findViewById(R.id.svpn_userAuth_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.svpn_certAuth_layout).setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.svpn_login_button) {//开始主认证，用户名密码或者证书认证，在认证回调中返回认证结果
            startPrimaryAuth();
        } else if (id == R.id.set_spa_button) {
            startActivity(new Intent(this, SpaConfigActivity.class));
        }
    }

    /**
     * 开始主认证，用户名密码或者证书认证
     */
    private void startPrimaryAuth() {

        //获取当前选取的认证方式
        int currentAuthViewId = mAuthMethodRadioGroup.getCheckedRadioButtonId();
        if (currentAuthViewId == R.id.svpn_userAuth_tabheader) {
            mVpnAddress = mVpnAddressEditText.getText().toString();
            mPhoneNumber = mPhoneNumberEditView.getText().toString();

            if (TextUtils.isEmpty(mVpnAddress)) {
                showToast("VPN服务器地址不能为空");
                return;
            }

            if (TextUtils.isEmpty(mPhoneNumber)) {
                showToast("手机号码不能为空");
                return;
            }

            showWaitingDialog(false);
            SFUemSDK.getInstance().startPrimarySmsAuth(mVpnAddress, mPhoneNumber);
        }
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
                startActivity(new Intent(PrimarySmsLoginActivity.this, AuthSuccessActivity.class));
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
     * 获取当前应用的签名信息
     */
    public String getCurrentSignature(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

            byte[] cert = info.signatures[0].toByteArray();

            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            return hexString.toString();
        } catch (Exception e) {
            SFLogN.error(TAG, "getCurrentSignature failed!", e.getMessage());
        }
        return null;
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
                mAuthDialog = new AlertDialog.Builder(PrimarySmsLoginActivity.this)
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
            case AUTH_TYPE_SMS:
            case AUTH_TYPE_PRIMARY_SMS: {
                mSmsCodeDialogView = dialogView;
                TextView tvTel = dialogView.findViewById(R.id.tv_tel);
                Button btnGetVerficationCode = dialogView.findViewById(R.id.bt_getVerficationCode);
                btnGetVerficationCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setEnabled(false);
                        //调用重新获取短信接口
                        SFUemSDK.getInstance().getSFAuth().regetSmsCode(PrimarySmsLoginActivity.this);
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
            case AUTH_TYPE_RAND: {
                mRandCodeDialogView = dialogView;// 保存一份图形校验码对话框视图
                final ImageView imgError = dialogView.findViewById(R.id.randcode_imgError);
                final ImageView imgRandCode = dialogView.findViewById(R.id.iv_graphCode);

                // 按图片刷新校验码
                View.OnClickListener refreshListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long lastRefresh = (Long) imgRandCode.getTag();
                        //检查更新间隔，暂定间隔3秒
                        if (System.currentTimeMillis() - lastRefresh >= 3 * 1000) {
                            // 启动刷新任务
                            SFUemSDK.getInstance().getSFAuth().regetRandCode(PrimarySmsLoginActivity.this);
                            imgRandCode.setTag(System.currentTimeMillis());
                        }
                    }
                };

                imgError.setOnClickListener(refreshListener);
                imgRandCode.setOnClickListener(refreshListener);

                SFUemSDK.getInstance().getSFAuth().regetRandCode(PrimarySmsLoginActivity.this); //首次自动触发获取校验码
                imgRandCode.setTag(System.currentTimeMillis());// 记录上次更新时间到imgChecksum控件的tag里，初始化为0，即没有上次更新
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
            case AUTH_TYPE_PRIMARY_SMS: {
                EditText etSmsCode = dialogView.findViewById(R.id.et_verficationCode);
                String smsCode = etSmsCode.getText().toString();

                if (TextUtils.isEmpty(smsCode)) {
                    errorMsg = "短信验证码不能为空";
                    break;
                }

                authParams.put(SFConstants.AUTH_KEY_PRIMARYSMS_CODE, smsCode);
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
            case AUTH_TYPE_TOKEN: {
                EditText etDynamicToken = dialogView.findViewById(R.id.et_dynamicToken);
                String dynamicToken = etDynamicToken.getText().toString();

                if (TextUtils.isEmpty(dynamicToken)) {
                    errorMsg = "动态口令不能为空";
                    break;
                }

                authParams.put(SFConstants.AUTH_KEY_TOKEN, dynamicToken);
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
//        if (mAuthDialog != null && mAuthDialog.isShowing()) {
//            mAuthDialog.dismiss();
//            mAuthDialog = null;
//        }
    }

    /**
     * @param success    true: 成功, false: 失败
     * @param bytes      图片二进制数据，success为true是有效
     * @param byteLength 图片二进制数据长度，success为true是有效
     * @brief SFRegetRandCodeListener获取图形校验码回调
     */
    @Override
    public void onRegetRandCode(boolean success, byte[] bytes, int byteLength) {
        Drawable randCode = null;
        if (success) {
            randCode = Drawable.createFromStream(new ByteArrayInputStream(bytes), "rand_code");
        } else {
            SFLogN.error2(TAG, "get RandCode failed", "maybe network is wrong");
        }

        final Drawable drawable = randCode;
        //显示图形校验码
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mRandCodeDialogView == null) {
                    showToast("图形校验码对话框视图未加载");
                    return;
                }

                final ImageView imgError = mRandCodeDialogView.findViewById(R.id.randcode_imgError);
                final ImageView imgRandCode = mRandCodeDialogView.findViewById(R.id.iv_graphCode);

                imgError.setVisibility(drawable == null ? View.VISIBLE : View.GONE);
                imgRandCode.setVisibility(drawable == null ? View.GONE : View.VISIBLE);

                // 显示图形校验码
                imgRandCode.setImageDrawable(drawable);
                // 更新刷新时间
                imgRandCode.setTag(System.currentTimeMillis());
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //登录界面证书选择
            case CERTFILE_REQUEST_CODE: {
                //获取证书选择器结果
                String certPath = "";
                if (resultCode == Activity.RESULT_OK) {
                    certPath = CertUtils.fromUriGetRealPath(this, data.getData()).trim();
                }
                break;
            }
            case DIALOG_CERTFILE_REQUEST_CODE: {
                //当证书认证是辅助认证时获取证书选择器结果
                String certPathDialog = "";
                if (resultCode == Activity.RESULT_OK) {
                    certPathDialog = CertUtils.fromUriGetRealPath(this, data.getData()).trim();
                }
                mCertPathDialogEditView.setText(certPathDialog);
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 调用系统自带的文件管理器选择证书文件
     */
    private void selectCertFile(int requestCode) {
        if (PermissionUtil.isNeedRequestSDCardPermission(this)) {
            PermissionUtil.requestSDCardPermissions(this, PERMISSON_REQUEST_CODE);
            return;
        }

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, requestCode);
    }

    //显示“请稍候...”提示框
    public void showWaitingDialog(final boolean isCancelable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog == null || !mProgressDialog.isShowing()) {
                    mProgressDialog = new ProgressDialog(PrimarySmsLoginActivity.this);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.setTitle("");
                    mProgressDialog.setMessage(PrimarySmsLoginActivity.this.getString(R.string.waiting));
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
                Dialog messageDialog = new AlertDialog.Builder(PrimarySmsLoginActivity.this)
                        .setTitle(PrimarySmsLoginActivity.this.getString(R.string.info))
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
                Toast.makeText(PrimarySmsLoginActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 权限授权结果回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] paramArrayOfInt) {
        super.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
        if (requestCode == PERMISSON_REQUEST_CODE) {
            //处理权限申请结果
            // do nothing
        }
    }

    /**
     * 设置登录信息
     */
    private void setLoginInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_FILE_NAME, MODE_PRIVATE);
        mVpnAddress = sharedPreferences.getString(Constants.KEY_VPN_ADDRESS, mVpnAddress);
        mPhoneNumber = sharedPreferences.getString(Constants.KEY_PHONE_NUMBER, mPhoneNumber);

        mVpnAddressEditText.setText(mVpnAddress);
        mPhoneNumberEditView.setText(mPhoneNumber);
    }

    /**
     * 恢复保存的登录信息
     */
    private void saveLoginInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_VPN_ADDRESS, mVpnAddress);
        //保存手机号码，真实场景请加密存储
        editor.putString(Constants.KEY_PHONE_NUMBER, mPhoneNumber);
        editor.apply();
    }

    private void killAppProcess() {
        //注意：不能先杀掉主进程，否则逻辑代码无法继续执行，需先杀掉相关进程最后杀掉主进程
        ActivityManager mActivityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> mList = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : mList) {
            SFLogN.info(TAG, "process pid:" + runningAppProcessInfo.pid + " mypid:" + Process.myPid());
            SFLogN.info(TAG, "process uid:" + runningAppProcessInfo.uid + " myuid:" + Process.myUid());
            if (runningAppProcessInfo.pid != Process.myPid() && runningAppProcessInfo.uid == Process.myUid()) {
                SFLogN.info(TAG, "killProcess:" + runningAppProcessInfo.pid);
                Process.killProcess(runningAppProcessInfo.pid);
            }
        }
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

}
