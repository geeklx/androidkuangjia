package com.sangfor.sdkdemo.wrap;

import android.content.Context;

import com.sangfor.sdk.SFSecuritySDKFactory;
import com.sangfor.sdk.SFUemSDK;
import com.sangfor.sdk.base.SFLaunchReason;
import com.sangfor.sdk.entry.SFLaunchInfo;
import com.sangfor.sdk.utils.SFLogN;

/**
 * 子应用管理类，保持子应用传递过来的信息，主要方便主应用拉起子应用
 */
public class SubAppManager {

    private static final String TAG = "SubAppManager";

    private SFLaunchInfo mSubAppLaunchInfo;
    private static volatile SubAppManager sInstance;

    private SubAppManager() {
    }

    public static SubAppManager getInstance() {
        if (sInstance == null) {
            synchronized (SubAppManager.class) {
                if (sInstance == null) {
                    sInstance = new SubAppManager();
                }
            }
        }

        return sInstance;
    }

    public void setSubAppLaunchInfo(SFLaunchInfo subAppLaunchInfo) {
        this.mSubAppLaunchInfo = subAppLaunchInfo;
    }

    public SFLaunchInfo getSubAppLaunchInfo() {
        return mSubAppLaunchInfo;
    }

    /**
     * 拉回子应用
     * @param context
     */
    public void launcherSubApp(Context context, SFLaunchInfo launchInfo) {
        if (context == null) {
            SFLogN.error2(TAG, "cannot launcherSubApp", "context=null");
            return;
        }
        SFLogN.info(TAG, "will launcherSubApp..." + launchInfo.getSubAppInfo().getSubAppPackageName());
        if (launchInfo != null) {
            SFLogN.info(TAG, "will start sub app...");
            SFUemSDK.getInstance().getSFLaunch().launchSubApp(context, launchInfo);
        } else {
            SFLogN.warn2(TAG, "can not start sub app...", "launchInfo is null");
        }

        // 子应用被拉起后，表示一个流程执行完成，需要清空数据(主应用被子应用拉起-->主应用拉回子应用)
        clearCache();
    }

    public void clearCache() {
        this.mSubAppLaunchInfo = null;
    }

}
