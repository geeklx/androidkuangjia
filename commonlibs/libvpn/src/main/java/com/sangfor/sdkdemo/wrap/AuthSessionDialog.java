package com.sangfor.sdkdemo.wrap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.sangfor.sdk.entry.SFLaunchInfo;
import com.sangfor.sdkdemo.R;
import com.sangfor.sdkdemo.wrap.AppInfoUtils;


/**
 * 是否给子应用授权弹窗
 */
public class AuthSessionDialog {

    private Activity mActivity;

    private DialogClickListener mClickListener;

    private AlertDialog mDialog;
    private SFLaunchInfo mSFLaunchInfo;

    public void setClickListener(DialogClickListener clickListener) {
        mClickListener = clickListener;
    }

    public AuthSessionDialog(Activity activity, SFLaunchInfo launchInfo) {
        mActivity = activity;
        mSFLaunchInfo = launchInfo;
    }

    public void show() {
        if (mDialog == null) {
            String hostAppName = AppInfoUtils.getApplicationName(mActivity);
            String subAppName = AppInfoUtils.getApplicationName(mActivity, mSFLaunchInfo.getPackageName());
            String dialogMessage = String.format(mActivity.getString(R.string.request_session_can_you_agree), subAppName, hostAppName);

            mDialog = new AlertDialog.Builder(mActivity)
                    .setMessage(dialogMessage)
                    .setCancelable(false)
                    .setPositiveButton(mActivity.getString(R.string.agree), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();

                            if (mClickListener != null) {
                                mClickListener.onPositiveClick(mSFLaunchInfo);
                            }
                        }
                    })
                    .setNegativeButton(mActivity.getString(R.string.disagree), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();

                            if (mClickListener != null) {
                                mClickListener.onNegativeClick(mSFLaunchInfo);
                            }
                        }
                    })
                    .create();
        }

        mDialog.show();
    }

    public void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public boolean isShowing() {
        if (mDialog != null) {
            return mDialog.isShowing();
        }

        return false;
    }

    public SFLaunchInfo getSFLaunchInfo() {
        return mSFLaunchInfo;
    }

    public interface DialogClickListener {
        void onPositiveClick(SFLaunchInfo launchInfo);
        void onNegativeClick(SFLaunchInfo launchInfo);
    }
}
