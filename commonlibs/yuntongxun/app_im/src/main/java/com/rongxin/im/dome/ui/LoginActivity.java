package com.rongxin.im.dome.ui;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2021-01-04
 * Time            :       4:47 PM
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :
 **/

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rongxin.im.dome.R;
import com.yuntongxun.confwrap.WrapManager;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;
import com.yuntongxun.plugin.common.YTXAppMgr;
import com.yuntongxun.plugin.common.RXConfig;
import com.yuntongxun.plugin.common.YTXSDKCoreHelper;
import com.yuntongxun.plugin.common.common.YTXBackwardSupportUtil;
import com.yuntongxun.plugin.common.common.bar.YTXMeizuStatusBarCompat;
import com.yuntongxun.plugin.common.common.bar.YTXStatusBarCompat;
import com.yuntongxun.plugin.common.common.base.YTXCCPClearEditText;
import com.yuntongxun.plugin.common.common.dialog.YTXRXAlertDialog;
import com.yuntongxun.plugin.common.common.menu.YTXSubMenu;
import com.yuntongxun.plugin.common.common.utils.CheckUtil;
import com.yuntongxun.plugin.common.common.utils.ECPreferenceSettings;
import com.yuntongxun.plugin.common.common.utils.EasyPermissionsEx;
import com.yuntongxun.plugin.common.common.utils.LogUtil;
import com.yuntongxun.plugin.common.common.utils.TextUtil;
import com.yuntongxun.plugin.common.common.utils.ToastUtil;
import com.yuntongxun.plugin.common.ui.YTXPermissionActivity;
import com.yuntongxun.plugin.common.ui.RongXinCompatActivity;
import com.yuntongxun.plugin.common.ui.base.RXDialogMgr;
import com.yuntongxun.plugin.common.ui.tools.YTXSystemBarHelper;
import com.yuntongxun.plugin.login.account.YTXRegisterActivity;
import com.yuntongxun.plugin.login.account.YTXSelectAccoutMenuHelper;
import com.yuntongxun.plugin.login.account.YTXSelectAccoutTypeCallback;
import com.yuntongxun.plugin.login.common.YTXLoginCheckUtils;
import com.yuntongxun.plugin.login.pcenter.YTXSettingPhoneNumInputActivity;
import com.yuntongxun.plugin.login.serverinfo.YTXSettingServerInfoActivity;

import okhttp3.HttpUrl;


/**
 * Created by Jorstin on 2015/3/18.
 */
public class LoginActivity extends RongXinCompatActivity implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final boolean VERIFYCODE_MUST = false;
    private ScrollView mScrollView;
    private YTXCCPClearEditText passwordEt;
    private TextView forgetPwdTv;
    private View login_num_ll;
    /**
     * 输入邀请码的View
     */
    private YTXCCPClearEditText mInviteCodeView;

    private boolean hasLogin = false;
    private boolean needVerify = true;
    private String previousPhoneNum = "";
    private String passWord = "";
    private String psw = "";

    //    private ImageView avatar;
    private View verifyCodeLl;
    /**
     * 图片验证码
     */
    private YTXCCPClearEditText imgCodeEt;
    private int restTime = 60;
    private YTXCCPClearEditText previousNumTv;
    private TextView password_tv;
    private ImageView login_iv;

    private RelativeLayout sign_re;
    private View login_re;

    View.OnFocusChangeListener mOnFocusChangeListener = (v, hasFocus) -> {
        if (!hasFocus) {
            return;
        }
        doFullScroll();
    };
    private TextView tvSelectAccoutType;
    private YTXSelectAccoutMenuHelper selectAccoutMenuHelper;
    private String historyType;

    @Override
    protected boolean isEnableSwipe() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayHomeAsUpEnabled(false);
        setSingleActionMenuItemVisibility(0, false);

        previousPhoneNum = YTXAppMgr.getHistoryAccount();
        passWord = YTXAppMgr.getPassWd();
        historyType = YTXAppMgr.getHistoryType();
        LogUtil.d(TAG, "[onCreate] previousPhoneNum:" + previousPhoneNum);
        if (!TextUtils.isEmpty(previousPhoneNum)) {
            hasLogin = true;
            needVerify = false;
            setSingleActionMenuItemVisibility(0, true);
        }
        initResourceRefs();
        forgetPwdTv.setOnLongClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, YTXSettingServerInfoActivity.class);
            startActivity(intent);
            return false;
        });
        login_re.setOnLongClickListener(view -> {
            RXDialogMgr.showDialog(LoginActivity.this,
                    getString(R.string.ytx_please_enter_pbs_address),
                    RXConfig.BASE_URL, null,
                    getString(R.string.ytx_please_enter_pbs_address_hint), false,
                    text -> {
                        if (CheckUtil.checkUrl(text.toString()) && HttpUrl.parse(text.toString()) != null && text.toString().endsWith("/")) {
                            YTXAppMgr.savePreference(ECPreferenceSettings.SETTINGS_SERVERIP, text.toString());
                            RXConfig.initBaseIp();
                            ToastUtil.showMessage(R.string.ytx_pbs_address_success);
                        } else {
                            ToastUtil.showMessage(R.string.ytx_pbs_address_error);
                        }
                        return true;
                    });
            return false;
        });
        getInitUpdatePassword();
        immersiveBar();
    }

    private void immersiveBar() {
        YTXStatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.action_bar_color));
        YTXSystemBarHelper.immersiveStatusBar(this, 0);

        if (Build.DISPLAY.startsWith("Flyme")) {
            YTXMeizuStatusBarCompat.setStatusBarDarkIcon(this, true);
        }
        View mStatusBarView = findViewById(R.id.ytx_status_bar_view);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, YTXSystemBarHelper.getStatusBarHeight(mStatusBarView.getContext()));
        mStatusBarView.setLayoutParams(lp);
        mStatusBarView.requestLayout();
    }

    @Override
    public boolean hasActionBar() {
        return false;
    }

    private void getInitUpdatePassword() {
        boolean isUpdatePw = getIntent().getBooleanExtra("update_password", false);
        if (isUpdatePw) {
            passwordEt.setText("");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    YTXRXAlertDialog.Builder alertBuilder = new YTXRXAlertDialog.Builder(LoginActivity.this);
                    alertBuilder.setTitle(getString(R.string.str_modify_pwd))
                            .setMessage(getString(R.string.login_password_has_been_changed_please_log_in_again))
                            .setPositiveButton(com.yuntongxun.plugin.common.R.string.app_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    YTXAppMgr.clearPassword();
                                }
                            })
                            .setCancelable(false);
                    YTXRXAlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                }
            }, 500);
            ToastUtil.showMessage(getString(R.string.login_password_has_been_changed_please_log_in_again));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        login_iv.setImageDrawable(passwordEt.length() > 7 && previousNumTv.getText().length() > 0 ? getResources().getDrawable(R.mipmap.arrow_press) : getResources().getDrawable(R.mipmap.arrow_disable));

    }


    private final String[] needPermissionsInit = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};


    private void initResourceRefs() {
        sign_re = (RelativeLayout) findViewById(R.id.sign_re);
        login_re = findViewById(R.id.login_re);
        login_iv = (ImageView) findViewById(R.id.login_iv);
//        avatar = (ImageView) findViewById(R.id.avatar);
        password_tv = (TextView) findViewById(R.id.password_tv);
        verifyCodeLl = findViewById(R.id.verify_code_ll);
        login_num_ll = findViewById(R.id.login_num_ll);
        previousNumTv =  findViewById(R.id.previous_login_num);
        mInviteCodeView =  findViewById(R.id.invite_code);
        tvSelectAccoutType = findViewById(R.id.tv_select_accout_type);
        if (!TextUtils.isEmpty(historyType)) {
            if (historyType.equals(YTXLoginCheckUtils.PurposeType.Account.value())) {
                tvSelectAccoutType.setText(getString(R.string.app_accout));
            } else if (historyType.equals(YTXLoginCheckUtils.PurposeType.PhoneNumber.value())) {
                tvSelectAccoutType.setText(getString(R.string.app_phone));
            } else if (historyType.equals(YTXLoginCheckUtils.PurposeType.Email.value())) {
                tvSelectAccoutType.setText(getString(R.string.app_email));
            } else if (historyType.equals(YTXLoginCheckUtils.PurposeType.JOBNUMBER.value())) {
                tvSelectAccoutType.setText(getString(R.string.app_job_number));
            }
        }
        tvSelectAccoutType.setOnClickListener(this);
        mInviteCodeView.setHint(getSpannableString(R.string.login_prompt_invite_code_hint));
        mInviteCodeView.setVisibility((TextUtil.isEmpty(previousPhoneNum) && RXConfig.INVITE_CODE) ? View.VISIBLE : View.GONE);

        if (hasLogin) {
            login_num_ll.setVisibility(View.VISIBLE);
            verifyCodeLl.setVisibility(View.GONE);
            sign_re.setVisibility(View.VISIBLE);
            login_re.setVisibility(View.VISIBLE);
        } else {
            if (VERIFYCODE_MUST) {
                verifyCodeLl.setVisibility(View.VISIBLE);
            }
        }


        passwordEt = findViewById(R.id.password);
        previousNumTv.setText(previousPhoneNum);
        passwordEt.setText(passWord);
        login_iv.setOnClickListener(this);

        forgetPwdTv = (TextView) findViewById(R.id.tv_forgetPwd);
        forgetPwdTv.setOnClickListener(this);
        passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                login_iv.setImageDrawable(s.length() > 7 && previousNumTv.getText().length() > 0 ? getResources().getDrawable(R.mipmap.arrow_press) : getResources().getDrawable(R.mipmap.arrow_disable));

            }
        });
        previousNumTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                YTXAppMgr.clearPassword();
                passwordEt.setText("");
            }
        });
        imgCodeEt =  findViewById(R.id.verifyCodeEt);
        mScrollView = (ScrollView) findViewById(R.id.ytx_scrollView);
        passwordEt.setOnFocusChangeListener(mOnFocusChangeListener);
        mInviteCodeView.setOnFocusChangeListener(mOnFocusChangeListener);
        imgCodeEt.setOnFocusChangeListener(mOnFocusChangeListener);
    }

    public SpannableString getSpannableString(int textId) {
        int toPix = YTXBackwardSupportUtil.fromDPToPix(this, (int) 14.0F);
        SpannableString spannableString = YTXBackwardSupportUtil.getText(this, getString(textId), toPix);
        return spannableString;
    }


    @Override
    public int getLayoutId() {
        return R.layout.ytx_activity_login;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_register) {
            startActivity(new Intent(this, YTXRegisterActivity.class));
        } else if (v.getId() == R.id.tv_forgetPwd) {
            //忘记密码?
            YTXAppMgr.clearPassword();
            startActivity(new Intent(this, YTXSettingPhoneNumInputActivity.class));
        } else if (v.getId() == R.id.login_iv) {
            requetLogin();
        } else if (v.getId() == R.id.tv_select_accout_type) {
            //选择账户类型
            try {
                hideSoftKeyboard();
                Thread.sleep(100);
                controlPlusSubMenu();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    private void requetLogin() {
        hideSoftKeyboard();
        if (!EasyPermissionsEx.hasPermissions(this, needPermissionsInit)) {
            EasyPermissionsEx.requestPermissions(LoginActivity.this, getString(R.string.rationale), YTXPermissionActivity.PERMISSIONS_REQUEST_JOIN, needPermissionsInit);
            return;
        }
        if (!EasyPermissionsEx.hasPermissions(this, needPermissionsInit)) {
            EasyPermissionsEx.goSettings2Permissions(this, getString(com.yuntongxun.plugin.common.R.string.goSettingsRationaleInit), getString(com.yuntongxun.plugin.common.R.string.set), RC_SETTINGS_SCREEN);
            return;
        }
        String account = previousNumTv.getText().toString();
        psw = passwordEt.getText().toString().trim();
//        String account = "18965656534";
//        psw = "321@@Qwe";

        if (TextUtils.isEmpty(account)) {
            ToastUtil.showMessage(com.yuntongxun.plugin.login.R.string.input_mobile);
            return;
        }
        if (YTXLoginCheckUtils.checkAcountLength(account)) {
            return;
        }
        if (YTXLoginCheckUtils.checkPsw(psw)) {
            return;
        }
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("需要访问所有文件的权限，但某项权限已被禁止，你可以到设置中更改");
            builder.setPositiveButton("设置", (dialog, which) -> {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            });
            builder.show();
            return;
        }

        showPostingDialog(R.string.login_posting);
        WrapManager.getInstance().app_LoginIM(account, psw, new YTXSDKCoreHelper.OnConnectStateListener() {
            @Override
            public void onConnectSuccess() {
                doLauncherAction(LoginActivity.this, LauncherUI.class);
            }

            @Override
            public void onConnectFailed(ECError error) {
                ToastUtil.showMessage(error.errorMsg);
            }
        });
    }

    /**
     * 控制下拉菜单显示和隐藏
     */
    private void controlPlusSubMenu() {
        if (selectAccoutMenuHelper == null) {
            selectAccoutMenuHelper = new YTXSelectAccoutMenuHelper(this);
            selectAccoutMenuHelper.setSelectAccoutTypeCallback(new YTXSelectAccoutTypeCallback() {
                @Override
                public void OnclickAccout(YTXSubMenu menu) {
                    //得到类型
                    if (!tvSelectAccoutType.getText().toString().equals(menu.getTitle())) {
                        tvSelectAccoutType.setText(menu.getTitle());
                        previousNumTv.setText("");
                    }
                }
            });
        }
        if (selectAccoutMenuHelper.isShowing()) {
            selectAccoutMenuHelper.dismiss();
            return;
        }
        selectAccoutMenuHelper.setOnDismissListener(null);
        selectAccoutMenuHelper.setLacation(tvSelectAccoutType, 0, 0);
        selectAccoutMenuHelper.tryShow();
    }

    public void doLauncherAction(Context ctx, Class<?> a) {
        try {
            // TODO 用户修改密码需要 应该在自己业务做

//            String fmPassword = YTXAppMgr.getPluginUser().getFmpasswd();
//            String inputPwd = passwordEt.getText().toString().trim();
//            if ("1".equals(fmPassword)) {
//                UserManager.doForceModifyPassword(LoginActivity.this, SettingAccountInfoActivity.MODIFY_PASSWORD, inputPwd, -1);
//                finish();
//                return;
//            }
//
//            if (!CheckUtil.checkPswString(inputPwd)) {
//                ToastUtil.showMessage(getString(R.string.input_psw_limit));
//                UserManager.doForceModifyPassword(LoginActivity.this, SettingAccountInfoActivity.MODIFY_PASSWORD, inputPwd, -1);
//                finish();
//                return;
//            }
//            YTXAppMgr.savePreference(ECPreferenceSettings.SETTINGS_LOGINED_PASSWORD, inputPwd);

            Intent intent = new Intent();
            intent.putExtra("launcher_from", 1);
            intent.setClass(ctx, a);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ctx.startActivity(intent);
            if (ctx instanceof Activity) {
                ((Activity) ctx).finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Runnable mScrollToBottom = () -> {
//            mScrollView.scrollTo(0, mobileEt.getTop());
    };

    private void doFullScroll() {

        if (imgCodeEt.isFocused()) {
            findViewById(R.id.fix_bottom_lv).post(() -> mScrollView.smoothScrollBy(0, mScrollView.getHeight()));
            return;
        }
        if (passwordEt.isFocused()) {
            ECHandlerHelper.postRunnOnUI(() -> mScrollView.scrollTo(0, passwordEt.getTop()));
            return;
        }
        if (mInviteCodeView.isFocused()) {
            ECHandlerHelper.postRunnOnUI(() -> mScrollView.scrollTo(0, mInviteCodeView.getTop()));
            return;
        }
        ECHandlerHelper.postRunnOnUI(mScrollToBottom);
    }
}

