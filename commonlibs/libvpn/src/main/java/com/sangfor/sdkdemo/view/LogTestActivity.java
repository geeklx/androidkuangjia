package com.sangfor.sdkdemo.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.sangfor.sdk.SFMobileSecuritySDK;
import com.sangfor.sdk.utils.SFLogN;
import com.sangfor.sdkdemo.R;
import com.sangfor.sdkdemo.utils.PermissionUtil;

/**
 * sf log 日志相关测试
 */
public class LogTestActivity extends AppCompatActivity {

    private static final int SDCARD_PERMISSION_CODE = 101;

    private Button mBtnEnableDebugLog;
    private TextView mTvShowInfo;

    private boolean mEnableDebugLog = false;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, LogTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vpn_activity_log_test);

        init();
        initView();
    }

    private void init(){
        SFMobileSecuritySDK.getInstance().enableDebugLog(mEnableDebugLog);
    }

    private void initView(){
        mBtnEnableDebugLog = findViewById(R.id.btn_enableDebugLog);
        mTvShowInfo = findViewById(R.id.tv_showInfo);

        mBtnEnableDebugLog.setText(getBtnEnableDebugLogTxt());
    }

    private String getBtnEnableDebugLogTxt(){
        if(mEnableDebugLog){
            return "设置debug日志开关, 当前状态: 开";
        }else{
            return "设置debug日志开关, 当前状态: 关";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SDCARD_PERMISSION_CODE) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    mTvShowInfo.setText("授权sdcard权限失败!");
                }
            }
        }
    }

    public void onClickApplySdCardPermission(View view) {
        PermissionUtil.requestSDCardPermissions(this, SDCARD_PERMISSION_CODE);
    }

    /**
     * 设置debug日志开关
     * @param view
     */
    public void onClickEnableDebugLog(View view) {
        mEnableDebugLog = !mEnableDebugLog;
        SFMobileSecuritySDK.getInstance().enableDebugLog(mEnableDebugLog);
        //开启控制打印
        SFLogN.updateLogMode(this, mEnableDebugLog ? SFLogN.MODE_ALL : SFLogN.MODE_FILE);

        mBtnEnableDebugLog.setText(getBtnEnableDebugLogTxt());
    }

    public void onClickGetSDKLogDir(View view) {
        String logDir = SFMobileSecuritySDK.getInstance().getSDKLogDir();
        if(TextUtils.isEmpty(logDir)){
            logDir = "";
        }
        mTvShowInfo.setText("返回日志路径: "+logDir);
    }
}