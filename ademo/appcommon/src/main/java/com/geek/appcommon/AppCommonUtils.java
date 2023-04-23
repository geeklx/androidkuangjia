package com.geek.appcommon;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.geek.appcommon.bean.DwCity;
import com.geek.appcommon.util.PermissionUtlis;
import com.geek.biz1.bean.FgrxxBean;
import com.geek.liblocations.LocationBean;
import com.geek.libutils.app.BaseApp;
import com.geek.libutils.data.MmkvUtils;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class AppCommonUtils {
    public static final String auth_url = "/gwapi/workbenchserver/api/workbench";//   /gwapi/workbenchserver/api/workbench
    public static final String userInfo = "userInfo";
    public static final String userInfo_dt = "userInfo_dt";
    public static final String region = "userregion";
    public static final String zhiwen = "zhiwen";
    public static final String intent_id = "query1";
    public static final String intent_title = "query2";
    public static final String intent_count = "query3";
    public static final String hios_scheme1 = "https://";
    public static final String hios_scheme2 = "http://";
    public static final String hios_scheme3 = "app://";
    public static final String hios_scheme4 = "dataability://";
    public static final String hios_host1 = "cs.znclass.com/";
    //    public static final String hios_path1 = AppUtils.getAppPackageName();
    public static final String hios_path1 = "com.fosung.lighthouse.dtsxbb";

    public static final String TAG_f1 =
            AppCommonUtils.hios_scheme3 +
                    AppCommonUtils.hios_host1 +
                    AppCommonUtils.hios_path1 +
                    ".hs.act.slbapp.shouye";// 首页1
    public static final String TAG_f2 =
            AppCommonUtils.hios_scheme3 +
                    AppCommonUtils.hios_host1 +
                    AppCommonUtils.hios_path1 +
                    ".hs.act.slbapp.yingyong";// 首页2
    public static final String TAG_f3 =
            AppCommonUtils.hios_scheme3 +
                    AppCommonUtils.hios_host1 +
                    AppCommonUtils.hios_path1 +
                    ".hs.act.slbapp.wode";// 首页3
    public static final String TAG_f4 =
            AppCommonUtils.hios_scheme3 +
                    AppCommonUtils.hios_host1 +
                    AppCommonUtils.hios_path1 +
                    ".hs.act.slbapp.other1?condition=login";// 首页4
    public static final String TAG_f5 =
            AppCommonUtils.hios_scheme3 +
                    AppCommonUtils.hios_host1 +
                    AppCommonUtils.hios_path1 +
                    ".hs.act.slbapp.other2?condition=login";// 首页5

    public static void addSeletorFromNet(final String pic1, final String pic2, final ImageView imageView) {
        if (imageView == null || TextUtils.isEmpty(pic1)) {
            return;
        }
        final StateListDrawable drawable = new StateListDrawable();
        Glide.with(BaseApp.get()).asBitmap().load(pic1).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NotNull Bitmap bitmap, Transition<? super Bitmap> transition) {
                Drawable newDraw = new BitmapDrawable(bitmap);
                drawable.addState(new int[]{-android.R.attr.state_pressed}, newDraw);

                Glide.with(BaseApp.get()).asBitmap().load(pic2).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NotNull Bitmap bitmap, Transition<? super Bitmap> transition) {
                        Drawable newDraw = new BitmapDrawable(bitmap);
//                                        drawable.addState(new int[]{android.R.attr.state_pressed}, newDraw);

                        imageView.setImageDrawable(drawable);
                    }
                });

            }
        });
    }

    public static void clearFragments(AppCompatActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        if (fragmentManager != null && fragmentManager.getFragments() != null && fragmentManager.getFragments().size() > 0) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            for (Fragment fragment : fragmentManager.getFragments()) {
                ft.remove(fragment);
            }
            ft.commit();
        }
    }


    public static String get_location_cityname() {
        DwCity bean = MmkvUtils.getInstance().get_common_json("city", DwCity.class);
        String aaaa = "";
        if (bean != null) {
            aaaa = bean.getName();
        } else {
            //
            LocationBean bean2 = MmkvUtils.getInstance().get_common_json("location", LocationBean.class);
            if (bean2 != null) {
                aaaa = bean2.getCity();
            } else {
                aaaa = "济南";
            }
            //
            FgrxxBean fgrxxBean = MmkvUtils.getInstance().get_common_json(AppCommonUtils.userInfo, FgrxxBean.class);
            if (fgrxxBean != null) {
                if (!TextUtils.isEmpty(fgrxxBean.getCode())) {
                    aaaa = fgrxxBean.getCityName();
                } else {
                    aaaa = "济南";
                }
            }
        }
        DwCity bean1 = new DwCity();
        bean1.setName(aaaa);
        MmkvUtils.getInstance().set_common_json2("city", bean1);
        return aaaa;
    }

    public static String[] quanxian1 = new String[]{Manifest.permission.CAMERA};
    public static String[] quanxian2 = new String[]{Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.READ_MEDIA_IMAGES};
    public static String[] quanxian3 = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static String[] quanxian4 = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};


    // 兼容Android13读写权限  MPermissionUtils.INSTANCE.onRequestPermissionsResult(requestCode, permissions, grantResults);
    public static void quanxian_1(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            PermissionUtlis.INSTANCE.checkPermissions(context, quanxian2, () -> {
                String  aaa = "";
                return null;
            }, () -> {
                String  bbb = "";
                return null;
            });

        } else {
            PermissionUtlis.INSTANCE.checkPermissions(context, quanxian3, () -> {
                String  aaa = "";
                return null;
            }, () -> {
                String  bbb = "";
                return null;
            });
        }

    }

    // 相机权限
    public static void quanxian_2(Context context) {
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.Q) {
            PermissionUtlis.INSTANCE.checkPermissions(context, quanxian4, () -> {

                return null;
            }, () -> {
//                initPermission();
                return null;
            });
        } else {
            PermissionUtlis.INSTANCE.checkPermissions(context, quanxian1, () -> {

                return null;
            }, () -> {
//                initPermission();
                return null;
            });
        }
    }


}
