package com.sangfor.sdkdemo.primary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.sangfor.sdk.SFUemSDK;
import com.sangfor.sdk.base.SFAuthStatus;
import com.sangfor.sdk.base.SFBaseMessage;
import com.sangfor.sdk.base.SFLaunchReason;
import com.sangfor.sdk.base.SFLogoutListener;
import com.sangfor.sdk.base.SFLogoutType;
import com.sangfor.sdk.entry.SFLaunchEntry;
import com.sangfor.sdk.entry.SFLaunchInfo;
import com.sangfor.sdk.utils.SFLogN;
import com.sangfor.sdkdemo.view.EntryActivity;
import com.sangfor.sdkdemo.wrap.AuthSessionDialog;
import com.sangfor.sdkdemo.wrap.SubAppManager;

public class GlobalListenerManager {
    private static final String TAG = "GlobalListenerManager";

    private Context mAppContext;

    private AuthSessionDialog mAuthSessionDialog = null;

    public static GlobalListenerManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(Context context) {
        mAppContext = context.getApplicationContext() == null ? context : context.getApplicationContext();
        addSFListener();
    }

    /**
     * 增加sdk事件监听
     */
    private void addSFListener() {

        /**
         * 注销事件监听回调，推荐在Application里面监听, 避免出现认证开始了还未监听的问题
         */
        SFUemSDK.getInstance().registerLogoutListener(new SFLogoutListener() {
            @Override
            public void onLogout(SFLogoutType type, SFBaseMessage message) {
                SFLogN.info(TAG, "onLogout, message: " + message);
                /**
                 * 收到注销事件，demo的处理是跳转到认证前的入口页面
                 */
                Intent intent = new Intent(mAppContext, EntryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mAppContext.startActivity(intent);
            }
        });

        /**
         * 主从场景需要
         * 子应用拉起事件监听回调，存在主从场景时才需要使用，推荐在Application里面监听，避免出现子应用拉起事件发生还未监听的问题
         */
        SFUemSDK.getInstance().getSFLaunch().setAppLaunchListener(new SFLaunchEntry.SFAppLaunchListener() {
            @Override
            public void onAppLaunched(SFLaunchInfo launchInfo) {
                SFLogN.info(TAG, "setAppLaunchListener onAppLaunched, launchInfo is :" + launchInfo);

                if (!SFLaunchEntry.isSubApp()) {

                    /**
                     * 只处子应用拉起获取登录凭据事件
                     */
                    if (launchInfo.getLaunchReason() == SFLaunchReason.Launch_HOSTAPP_AUTH_AUTHORIZATION) {

                        /**
                         * 如果主应用当前是未认证成功状态，无法进行登陆授权，主应用需要先进行认证
                         */
                        if (SFUemSDK.getInstance().getAuthStatus() != SFAuthStatus.AuthStatusAuthOk) {
                            SFLogN.info(TAG, "host app not auth ok, igore it");
                            Toast.makeText(mAppContext, "主应用尚未认证成功，请先认证", Toast.LENGTH_SHORT);
                            return;
                        }

                        /**
                         * sdk内部会确保主应用进入前台后才回调onAppLaunched,且会在launchInfo中提供当前主应用前台的activity获取方式
                         * ，方便用户可以直接使用此activity做弹框交互
                         */
                        Activity activity = launchInfo.getForegroundActivity();

                        /**
                         * 这里弹框让用户确认是否要给子应用授权，如果需求上不需要做弹框让用户确认，也可以直接在此处调用
                         * launcherSubApp拉回子应用
                         */
                        showAuthSessionDialog(activity, launchInfo);

                    }
                }
            }
        });
    }

    private void showAuthSessionDialog(Activity activity, SFLaunchInfo sfLaunchInfo) {
        if (mAuthSessionDialog != null) {
            mAuthSessionDialog.dismiss();
            mAuthSessionDialog = null;
        }

        mAuthSessionDialog = new AuthSessionDialog(activity, sfLaunchInfo);
        mAuthSessionDialog.setClickListener(new AuthSessionDialog.DialogClickListener() {
            @Override
            public void onPositiveClick(SFLaunchInfo launchInfo) {
                SFLogN.info(TAG, "AuthSessionDialog positiveClick.");
                /**
                 * 拉回到子应用进行授权
                 */
                SubAppManager.getInstance().launcherSubApp(mAppContext, launchInfo);
            }

            @Override
            public void onNegativeClick(SFLaunchInfo launchInfo) {
                SFLogN.info(TAG, "AuthSessionDialog negativeClick.");
            }
        });

        SFLogN.info(TAG, "showAuthSessionDialog.");
        mAuthSessionDialog.show();
    }

    private GlobalListenerManager() {

    }

    private final static class SingletonHolder {
        private static final GlobalListenerManager INSTANCE = new GlobalListenerManager();
    }
}
