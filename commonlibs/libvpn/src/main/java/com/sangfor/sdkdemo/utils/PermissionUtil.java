package com.sangfor.sdkdemo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sangfor.sdk.utils.SFLogN;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {
    private static final String TAG = "PermissionUtil";

    public static boolean isNeedRequestSDCardPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有权限
            //context.checkSelfPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            if (!Environment.isExternalStorageManager()) {
                return true;
            }
        } else {
            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }


    public static void requestSDCardPermissions(Activity activity, int requestCode) {
        if (activity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //android 11
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, requestCode);
            Toast.makeText(activity, "请授予所有文件管理权限～", Toast.LENGTH_SHORT).show();
        } else {
            List<String> permissionsList = new ArrayList<String>();
            if (ContextCompat.checkSelfPermission(activity.getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                SFLogN.info(TAG,"No WRITE_EXTERNAL_STORAGE permission");
                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (ContextCompat.checkSelfPermission(activity.getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                SFLogN.info(TAG,"No READ_EXTERNAL_STORAGE permission");
                permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (permissionsList.isEmpty()) {
                return;
            }
            String[] permissions = permissionsList.toArray(new String[permissionsList.size()]);
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        }

    }

    /**
     * 跳转至系统设置内的应用详情页面
     * @param context
     */
    public static void gotoAppPermissionManageActivity(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
