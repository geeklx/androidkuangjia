package com.geek.appmy;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.geek.appcommon.AppCommonUtils;
import com.geek.appcommon.interfaces.CommonXPopupListener;
import com.geek.appcommon.SlbBase;
import com.geek.appcommon.bean.AuthorStatus;
import com.geek.appcommon.util.ImageLoaderUtils;
import com.geek.appmy.widgets.BottomListPopupView1;
import com.geek.appmy.widgets.XpopOnCancelListener;
import com.geek.appmy.widgets.XpopOnSelectListener;
import com.geek.biz1.bean.FcomBean;
import com.geek.biz1.bean.FconfigBean;
import com.geek.biz1.bean.FgrxxBean;
import com.geek.biz1.presenter.Fconfig1Presenter;
import com.geek.biz1.presenter.Ffile1Presenter;
import com.geek.biz1.presenter.FgrxxPresenter;
import com.geek.biz1.presenter.FtipsPresenter;
import com.geek.biz1.view.Fconfig1View;
import com.geek.biz1.view.Ffile1View;
import com.geek.biz1.view.FgrxxView;
import com.geek.biz1.view.FtipsView;
import com.geek.libglide47.base.GlideImageView;
import com.geek.libglide47.base.ShapeImageView;
import com.geek.libshuiyin.GlideEngine;
import com.geek.libswipebacklayout.SwipeBack;
import com.geek.libutils.SlbLoginUtil;
import com.geek.libutils.app.LocalBroadcastManagers;
import com.geek.libutils.data.MmkvUtils;
import com.luck.picture.lib.app.PictureAppMaster;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.MediaExtraInfo;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.utils.MediaUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.util.XPopupUtils;

import java.io.File;
import java.util.ArrayList;

import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.tool.image.DownloadPictureUtil;
import cc.shinichi.library.tool.ui.ToastUtil;
import cc.shinichi.library.view.listener.OnBigImageClickListener;
import cc.shinichi.library.view.listener.OnBigImageLongClickListener;
import cc.shinichi.library.view.listener.OnBigImagePageChangeListener;
import cc.shinichi.library.view.listener.OnDownloadClickListener;
import cc.shinichi.library.view.listener.OnOriginProgressListener;

@SwipeBack(value = true)
public class MyPersonalDataAct extends SlbBase implements Ffile1View, Fconfig1View, FtipsView, FgrxxView {

    private Activity mActivity;
    private TextView tv_left;//??????
    private TextView tv_center;//????????????
    private GlideImageView iv_headportrait;//??????
    private TextView tv_personalname;//??????txt
    private TextView tv_personalphone;//?????????txt
    private TextView tv_personalsex;//??????txt
    private TextView tv_personalautograph;//????????????txt

    private RelativeLayout rl1;//??????
    private RelativeLayout rl2;//??????
    private RelativeLayout rl3;//?????????
    private RelativeLayout rl4;//??????
    private RelativeLayout rl5;//????????????
    private RelativeLayout rl6;//????????????
    private RelativeLayout rl7;//??????

    private String ImgUrl = "";
    private String url;
    private String url1;
    private Ffile1Presenter ffile1Presenter;
    private Fconfig1Presenter fconfig1Presenter;
    private FtipsPresenter ftipsPresenter;
    private FgrxxPresenter fgrxxPresenter;
    private String sex;
    private AuthorStatus needAuthorConfig = AuthorStatus.DEFAULT;

    @Override
    public void onResume() {
        // ????????????????????????bufen
        if (fconfig1Presenter != null) {
//            fconfig1Presenter.getconfig1(AppCommonUtils.auth_url, "authorized");
            fconfig1Presenter.getconfig1(AppCommonUtils.auth_url, "resource");
        }
        retryData();
        super.onResume();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mypersonal;
    }

    @Override
    protected void setup(@Nullable Bundle savedInstanceState) {
        BarUtils.setStatusBarLightMode(this, true);
        super.setup(savedInstanceState);
        mActivity = this;
        findview();
        onclick();
        donetwork();
        fconfig1Presenter = new Fconfig1Presenter();
        fconfig1Presenter.onCreate(this);
        ffile1Presenter = new Ffile1Presenter();
        ffile1Presenter.onCreate(this);
        ftipsPresenter = new FtipsPresenter();
        ftipsPresenter.onCreate(this);
        fgrxxPresenter = new FgrxxPresenter();
        fgrxxPresenter.onCreate(this);
    }

    private void donetwork() {
        retryData();
    }

    // ??????
    private void retryData() {
        tv_center.setText(getApplication().getResources().getString(R.string.appmy30));
        FgrxxBean fgrxxBean = MmkvUtils.getInstance().get_common_json(AppCommonUtils.userInfo, FgrxxBean.class);
        if (fgrxxBean != null) {
            sex = fgrxxBean.getSex() != null && !"".equals(fgrxxBean.getSex()) ? fgrxxBean.getSex() : "1";
            if (TextUtils.equals("0", fgrxxBean.getSex())) {
                iv_headportrait.loadImage(fgrxxBean.getPhoto(), R.drawable.icon_grxx1);
                tv_personalsex.setText("???");
            } else {
                iv_headportrait.loadImage(fgrxxBean.getPhoto(), R.drawable.icon_grxx2);
                tv_personalsex.setText("???");
            }
            if (!TextUtils.isEmpty(fgrxxBean.getPhoto())) {
                ImgUrl = fgrxxBean.getPhoto();
            }
            tv_personalname.setText(fgrxxBean.getName() != null && !"".equals(fgrxxBean.getName()) ? fgrxxBean.getName() : "...");
            tv_personalphone.setText(fgrxxBean.getPhonenum() != null && !"".equals(fgrxxBean.getPhonenum()) ? fgrxxBean.getPhonenum() : "...");
            tv_personalautograph.setText(fgrxxBean.getSignature() != null && !"".equals(fgrxxBean.getSignature()) ? fgrxxBean.getSignature() : "?????????");
        } else {
            iv_headportrait.loadImage("", R.drawable.icon_com_default1);
        }

    }


    private void onclick() {
        //????????????
        tv_left.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                onBackPressed();
            }
        });
        //????????????
        rl1.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {

            }
        });
        rl2.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                //????????????
                xpopwindows("????????????", "??????????????????", "name");
            }
        });
        rl3.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                //???????????????
                xpopwindows("???????????????", "?????????????????????", "phone");
            }
        });

        rl4.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                //????????????
                new XPopup.Builder(mActivity)
                        .isDestroyOnDismiss(true) //???????????????????????????????????????????????????
                        .asCenterList("???????????????", new String[]{"???", "???"},
                                null, Integer.parseInt(sex),
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        FgrxxBean fgrxxBean = MmkvUtils.getInstance().get_common_json(AppCommonUtils.userInfo, FgrxxBean.class);

                                        if (text != null && !"".equals(text)) {
                                            tv_personalsex.setText(text);
                                            if (fgrxxBean != null) {
                                                if (text != null && !"".equals(text)) {
                                                    if (text.equals("???")) {
                                                        sex = "0";
                                                    } else {
                                                        sex = "1";
                                                    }
                                                    //????????????
                                                    ftipsPresenter.gettips_img2(url, fgrxxBean.getId(), "", "", "", "", sex);
                                                } else {
                                                    ToastUtils.showLong("???????????????");
                                                }
                                            }
                                        }
                                    }
                                })
                        .show();

            }
        });
        rl5.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                //????????????
//                xpopwindows("????????????", "??????????????????", "sign");
                startActivity(new Intent(AppUtils.getAppPackageName() + ".hs.act.slbapp.MySettingQmAct"));

            }
        });
        rl6.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                // ????????????
            }
        });

        rl7.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                // ??????
            }
        });
    }

    public void xpopwindows(String title, String hint, String type) {
        new XPopup.Builder(mActivity)
                .maxWidth((int) (XPopupUtils.getWindowWidth(mActivity) * 0.8f))
                .hasStatusBarShadow(false)
                //.dismissOnBackPressed(false)
                .isDestroyOnDismiss(true) //???????????????????????????????????????????????????
                .autoOpenSoftInput(true)
                .isDarkTheme(false)
                .setPopupCallback(new CommonXPopupListener())
//                        .autoFocusEditText(false) //?????????????????????EditText??????????????????????????????true
                //.moveUpToKeyboard(false)   //??????????????????????????????????????????true
                .asInputConfirm(title, null, null, hint,
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String text) {
                                FgrxxBean fgrxxBean = MmkvUtils.getInstance().get_common_json(AppCommonUtils.userInfo, FgrxxBean.class);
                                if (text != null && !"".equals(text)) {
                                    if (type.equals("name")) {
                                        tv_personalname.setText(text);
                                        if (fgrxxBean != null) {
                                            if (text != null && !"".equals(text)) {
                                                //??????????????????
                                                ftipsPresenter.gettips_img2(url, fgrxxBean.getId(), "", "", text, "", "");
                                            } else {
                                                ToastUtils.showLong("??????????????????");
                                            }
                                        }
                                    } else if (type.equals("phone")) {
                                        tv_personalphone.setText(text);
                                        if (fgrxxBean != null) {
                                            if (text != null && !"".equals(text)) {
                                                //???????????????
                                                ftipsPresenter.gettips_img2(url, fgrxxBean.getId(), "", "", "", text, "");
                                            } else {
                                                ToastUtils.showLong("?????????????????????");
                                            }
                                        }
                                    } else if (type.equals("sign")) {
                                        tv_personalautograph.setText(text);
                                        if (fgrxxBean != null) {
                                            if (text != null && !"".equals(text)) {
                                                //????????????
                                                ftipsPresenter.gettips_img2(url, fgrxxBean.getId(), "", text, "", "", "");
                                            } else {
                                                ToastUtils.showLong("??????????????????");
                                            }
                                        }
                                    }
                                }
                            }
                        })
                .show();

    }

    private void findview() {
        tv_left = findViewById(R.id.tv_left);
        tv_center = findViewById(R.id.tv_center);
        iv_headportrait = findViewById(R.id.iv_headportrait);
        tv_personalname = findViewById(R.id.tv_personalname);
        tv_personalphone = findViewById(R.id.tv_personalphone);
        tv_personalsex = findViewById(R.id.tv_personalsex);
        tv_personalautograph = findViewById(R.id.tv_personalautograph);
        rl1 = findViewById(R.id.rl1);
        rl2 = findViewById(R.id.rl2);
        rl3 = findViewById(R.id.rl3);
        rl4 = findViewById(R.id.rl4);
        rl5 = findViewById(R.id.rl5);
        rl6 = findViewById(R.id.rl6);
        rl7 = findViewById(R.id.rl7);

        iv_headportrait.setShapeType(ShapeImageView.ShapeType.RECTANGLE);
        iv_headportrait.setBorderWidth(0);
        iv_headportrait.setRadius(4);
        iv_headportrait.setBorderColor(R.color.transparent20);
//        iv1.loadCircleImage(url1, com.geek.libglide47.R.color.black);
        iv_headportrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ToastUtils.showLong("????????????");
                SlbLoginUtil.get().loginTowhere(mActivity, new Runnable() {
                    @Override
                    public void run() {
//                        set_img();

                        Intent intent = new Intent(AppUtils.getAppPackageName() + ".hs.act.slbapp.MySettingMyPhotoAct");
                        intent.putExtra("imageUrl", ImgUrl);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void set_img() {
        // ?????????????????????
        ImagePreview.getInstance()
                // ?????????????????????activity?????????????????????????????????????????????????????????
                .setContext(mActivity)
                // ????????????????????????????????????0?????????~
                .setIndex(0)

                //=================================================================================================
                // ??????????????????????????????????????????????????????????????????????????????
                // 1?????????????????????imageInfo List
//                        .setImageInfoList(imageInfoList)

                // 2????????????url List
                //.setImageList(List<String> imageList)

                // 3??????????????????????????????????????????????????????????????????url
                .setImage(ImgUrl)
                //=================================================================================================

                // ????????????????????????????????????
//                        .setLoadStrategy(loadStrategy)

                // ?????????????????????????????????Picture??????????????????????????????????????????"BigImageView"?????????Picture????????????BigImageView?????????)
                .setFolderName("GeekApp")

                // ???????????????????????????ms
                .setZoomTransitionDuration(300)

                // ???????????????????????????Toast
                .setShowErrorToast(true)

                // ?????????????????????????????????????????????
                .setEnableClickClose(true)
                // ??????????????????????????????????????????
                .setEnableDragClose(true)
                // ??????????????????????????????????????????
                .setEnableUpDragClose(true)
                // ??????????????????????????????????????????????????????
//                        .setEnableDragCloseIgnoreScale(enableDragIgnoreScale)

                // ?????????????????????????????????????????????????????????????????????
//                        .setShowCloseButton(showCloseButton)
                // ?????????????????????????????????????????????????????????????????????R.drawable.ic_action_close
                .setCloseIconResId(R.drawable.ic_action_close)

                // ????????????????????????????????????????????????????????????
                .setShowDownButton(false)
                // ?????????????????????????????????????????????????????????????????????R.drawable.icon_download_new
                .setDownIconResId(R.drawable.icon_download_new)

                // ???????????????????????????????????????1/9???????????????
                .setShowIndicator(false)
                // ???????????????????????????shape???????????????????????????shape
                .setIndicatorShapeResId(R.drawable.shape_indicator_bg)

                // ???????????????????????????????????????????????????R.drawable.load_failed???????????? 0 ????????????
                .setErrorPlaceHolder(R.drawable.load_failed)

                // ????????????
                .setBigImageClickListener(new OnBigImageClickListener() {
                    @Override
                    public void onClick(Activity activity, View view, int position) {
                        // ...
                        Log.d("TAG", "onClick: ");
                    }
                })
                // ????????????
                .setBigImageLongClickListener(new OnBigImageLongClickListener() {
                    @Override
                    public boolean onLongClick(Activity activity, View view, int position) {
                        // ...
                        BottomListPopupView1 bottomListPopupView1 = new BottomListPopupView1(activity, 0, 0);
                        bottomListPopupView1.setStringData("", new String[]{"??????", "?????????????????????", "????????????"}, null);
                        bottomListPopupView1.setXpopOnSelectListener(new XpopOnSelectListener() {
                            @Override
                            public void onSelect(View textView, int position, String text) {
                                if (TextUtils.equals("??????", text)) {
                                    // ????????????
                                    PictureSelector.create(activity)
                                            .openCamera(SelectMimeType.ofImage())// ?????????????????????????????????????????? ??????????????????????????????or??????
                                            .setCropEngine(new ImageLoaderUtils.ImageFileCropEngine())
                                            .forResult(new OnResultCallbackListener<LocalMedia>() {
                                                @Override
                                                public void onResult(ArrayList<LocalMedia> result) {
                                                    for (LocalMedia media : result) {
                                                        if (media.getWidth() == 0 || media.getHeight() == 0) {
                                                            if (PictureMimeType.isHasImage(media.getMimeType())) {
                                                                MediaExtraInfo imageExtraInfo = MediaUtils.getImageSize(media.getPath());
                                                                media.setWidth(imageExtraInfo.getWidth());
                                                                media.setHeight(imageExtraInfo.getHeight());
                                                            } else if (PictureMimeType.isHasVideo(media.getMimeType())) {
                                                                MediaExtraInfo videoExtraInfo = MediaUtils.getVideoSize(PictureAppMaster.getInstance().getAppContext(), media.getPath());
                                                                media.setWidth(videoExtraInfo.getWidth());
                                                                media.setHeight(videoExtraInfo.getHeight());
                                                            }
                                                        }
                                                        Log.i("TAG", "?????????: " + media.getFileName());
                                                        Log.i("TAG", "????????????:" + media.isCompressed());
                                                        Log.i("TAG", "??????:" + media.getCompressPath());
                                                        Log.i("TAG", "??????:" + media.getPath());
                                                        Log.i("TAG", "????????????:" + media.getRealPath());
                                                        Log.i("TAG", "????????????:" + media.isCut());
                                                        Log.i("TAG", "??????:" + media.getCutPath());
                                                        Log.i("TAG", "??????????????????:" + media.isOriginal());
                                                        Log.i("TAG", "????????????:" + media.getOriginalPath());
                                                        Log.i("TAG", "Android Q ??????Path:" + media.getAvailablePath());
                                                        Log.i("TAG", "??????: " + media.getWidth() + "x" + media.getHeight());
                                                        Log.i("TAG", "Size: " + media.getSize());

                                                        Log.i("TAG", "onResult: " + media.toString());

                                                        // TODO ????????????PictureSelectorExternalUtils.getExifInterface();??????????????????????????????????????????????????????????????????????????????
                                                    }
                                                    ImgUrl = result.get(0).getAvailablePath();
                                                    iv_headportrait.loadImage(ImgUrl, R.drawable.icon_grxx1);
                                                    activity.finish();
                                                    //
                                                    ffile1Presenter.getfile1(url, new File(ImgUrl));
                                                }

                                                @Override
                                                public void onCancel() {

                                                }
                                            });
                                    Log.d("TAG", "onLongClick: ");
                                }
                                if (TextUtils.equals("?????????????????????", text)) {
                                    // ???????????? ??????????????????????????????api????????????
                                    PictureSelector.create(activity)
                                            .openGallery(SelectMimeType.ofImage())
                                            .setImageEngine(GlideEngine.createGlideEngine())
                                            .setSelectionMode(SelectModeConfig.SINGLE)
                                            .setCropEngine(new ImageLoaderUtils.ImageFileCropEngine())
                                            .isPreviewImage(true)// ?????????????????????
                                            .isPreviewVideo(false)// ?????????????????????
                                            .forResult(new OnResultCallbackListener<LocalMedia>() {
                                                @Override
                                                public void onResult(ArrayList<LocalMedia> result) {
                                                    for (LocalMedia media : result) {
                                                        if (media.getWidth() == 0 || media.getHeight() == 0) {
                                                            if (PictureMimeType.isHasImage(media.getMimeType())) {
                                                                MediaExtraInfo imageExtraInfo = MediaUtils.getImageSize(media.getPath());
                                                                media.setWidth(imageExtraInfo.getWidth());
                                                                media.setHeight(imageExtraInfo.getHeight());
                                                            } else if (PictureMimeType.isHasVideo(media.getMimeType())) {
                                                                MediaExtraInfo videoExtraInfo = MediaUtils.getVideoSize(PictureAppMaster.getInstance().getAppContext(), media.getPath());
                                                                media.setWidth(videoExtraInfo.getWidth());
                                                                media.setHeight(videoExtraInfo.getHeight());
                                                            }
                                                        }
                                                        Log.i("TAG", "?????????: " + media.getFileName());
                                                        Log.i("TAG", "????????????:" + media.isCompressed());
                                                        Log.i("TAG", "??????:" + media.getCompressPath());
                                                        Log.i("TAG", "??????:" + media.getPath());
                                                        Log.i("TAG", "????????????:" + media.getRealPath());
                                                        Log.i("TAG", "????????????:" + media.isCut());
                                                        Log.i("TAG", "??????:" + media.getCutPath());
                                                        Log.i("TAG", "??????????????????:" + media.isOriginal());
                                                        Log.i("TAG", "????????????:" + media.getOriginalPath());
                                                        Log.i("TAG", "Android Q ??????Path:" + media.getAvailablePath());
                                                        Log.i("TAG", "??????: " + media.getWidth() + "x" + media.getHeight());
                                                        Log.i("TAG", "Size: " + media.getSize());

                                                        Log.i("TAG", "onResult: " + media.toString());

                                                        // TODO ????????????PictureSelectorExternalUtils.getExifInterface();??????????????????????????????????????????????????????????????????????????????
                                                    }
                                                    ImgUrl = result.get(0).getAvailablePath();
                                                    iv_headportrait.loadImage(ImgUrl, R.drawable.icon_grxx1);
                                                    activity.finish();
                                                    //
                                                    ffile1Presenter.getfile1(url, new File(ImgUrl));
                                                }

                                                @Override
                                                public void onCancel() {

                                                }
                                            });
                                    Log.d("TAG", "onLongClick: ");
                                }
                                if (TextUtils.equals("????????????", text)) {
                                    OnDownloadClickListener downloadClickListener = ImagePreview.getInstance().getDownloadClickListener();
                                    if (downloadClickListener != null) {
                                        boolean interceptDownload = downloadClickListener.isInterceptDownload();
                                        if (interceptDownload) {
                                            // ?????????????????????????????????
                                        } else {
                                            // ??????????????????
                                            checkAndDownload(activity);
                                        }
                                        ImagePreview.getInstance().getDownloadClickListener().onClick(activity, textView, ImagePreview.getInstance().getIndex());
                                    } else {
                                        checkAndDownload(activity);
                                    }
                                }
                            }
                        });
                        bottomListPopupView1.setXpopOnCancelListener(new XpopOnCancelListener() {
                            @Override
                            public void onCancel(View textView) {
                                activity.finish();
                            }
                        });
                        new XPopup.Builder(activity)
                                .autoDismiss(false)
                                .asCustom(bottomListPopupView1)
                                .show();
                        return false;
                    }
                })
                // ??????????????????
                .setBigImagePageChangeListener(new OnBigImagePageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        Log.d("TAG", "onPageScrolled: ");
                    }

                    @Override
                    public void onPageSelected(int position) {
                        Log.d("TAG", "onPageSelected: ");
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        Log.d("TAG", "onPageScrollStateChanged: ");
                    }
                })
                // ?????????????????????????????????????????????????????????????????????????????????????????????
                .setDownloadClickListener(new OnDownloadClickListener() {
                    @Override
                    public void onClick(Activity activity, View view, int position) {
                        // ?????????????????????????????????????????????????????????????????????
                        Log.d("TAG", "onClick: position = " + position);
                    }

                    @Override
                    public boolean isInterceptDownload() {
                        // return true ???, ????????????????????????
                        // return false ???, ??????????????????
                        return false;
                    }
                })

                //=================================================================================================
                // ?????????????????????????????????????????????????????????????????????ImagePreview.PROGRESS_THEME_CIRCLE_TEXT??????????????????
                .setProgressLayoutId(ImagePreview.PROGRESS_THEME_CIRCLE_TEXT, new OnOriginProgressListener() {
                    @Override
                    public void progress(View parentView, int progress) {
                        Log.d("TAG", "progress: " + progress);

                        // ?????????????????????????????????????????????????????????parentView????????????????????????View????????????parentView???????????????
                        ProgressBar progressBar = parentView.findViewById(R.id.sh_progress_view);
                        TextView textView = parentView.findViewById(R.id.sh_progress_text);
                        progressBar.setProgress(progress);
                        String progressText = progress + "%";
                        textView.setText(progressText);
                    }

                    @Override
                    public void finish(View parentView) {
                        Log.d("TAG", "finish: ");
                    }
                })

                // ????????????????????????????????????????????????????????????????????????????????????parentView?????????????????????????????????????????????
                //.setProgressLayoutId(R.layout.image_progress_layout_theme_1, new OnOriginProgressListener() {
                //    @Override public void progress(View parentView, int progress) {
                //        Log.d(TAG, "progress: " + progress);
                //
                //        ProgressBar progressBar = parentView.findViewById(R.id.progress_horizontal);
                //        progressBar.setProgress(progress);
                //    }
                //
                //    @Override public void finish(View parentView) {
                //        Log.d(TAG, "finish: ");
                //    }
                //})
                //=================================================================================================

                // ????????????
                .start();
    }


    @Override
    public void Onfile1Success(FcomBean bean) {
        ImgUrl = bean.getUrl();
//        ftipsPresenter.gettips_img1(url, ImgUrl);
        FgrxxBean fgrxxBean = MmkvUtils.getInstance().get_common_json(AppCommonUtils.userInfo, FgrxxBean.class);
        if (fgrxxBean != null) {
            //????????????
            ftipsPresenter.gettips_img2(url, fgrxxBean.getId(), ImgUrl, "", "", "", "");
        }
    }

    @Override
    public void Onfile1Nodata(String bean) {

    }

    @Override
    public void Onfile1Fail(String msg) {

    }

    //???????????????????????????????????????
    @Override
    public void Onconfig2Success(String authorizedType, FconfigBean bean) {
        //????????????author?????????
        if (needAuthorConfig == AuthorStatus.DEFAULT) {
            url = bean.getIdentity();
            needAuthorConfig = AuthorStatus.Loading;
            fconfig1Presenter.getconfig1(AppCommonUtils.auth_url, "authorized");
        } else if (needAuthorConfig == AuthorStatus.Loading) {
            needAuthorConfig = AuthorStatus.Loaded;
            url1 = bean.getIdentity();
        }
    }

    @Override
    public void Onconfig2Nodata(String bean) {

    }

    @Override
    public void Onconfig2Fail(String msg) {

    }


    @Override
    public void OntipsSuccess(String bean) {
        ToastUtils.showLong(bean);
        if (url1 != null && !TextUtils.isEmpty(url1) && fgrxxPresenter != null) {
            fgrxxPresenter.getgrxx(url1);
        }
    }

    @Override
    public void OntipsNodata(String bean) {

    }

    @Override
    public void OntipsFail(String msg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fconfig1Presenter != null) {
            fconfig1Presenter.onDestory();
        }
        if (ffile1Presenter != null) {
            ffile1Presenter.onDestory();
        }
        if (ftipsPresenter != null) {
            ftipsPresenter.onDestory();
        }
        if (fgrxxPresenter != null) {
            fgrxxPresenter.onDestory();
        }
    }

    /**
     * --------------------------------?????????????????????----------------------------------
     */

    private void checkAndDownload(Activity activity) {
        // ????????????
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // ????????????
                ToastUtil.getInstance().showShort(activity, getString(cc.shinichi.library.R.string.toast_deny_permission_save_failed));
            } else {
                //????????????
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1);
            }
        } else {
            // ??????????????????
            downloadCurrentImg(activity);
        }
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        return super.shouldShowRequestPermissionRationale(permission);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PERMISSION_GRANTED) {
                    downloadCurrentImg(mActivity);
                } else {
                    ToastUtil.getInstance().showShort(mActivity, getString(cc.shinichi.library.R.string.toast_deny_permission_save_failed));
                }
            }
        }
    }

    /**
     * ?????????????????????SD???
     */
    private void downloadCurrentImg(Activity activity) {
        DownloadPictureUtil.INSTANCE.downloadPicture(activity.getApplicationContext(),
                ImagePreview.getInstance().getImageInfoList().get(ImagePreview.getInstance().getIndex()).getOriginUrl());
        activity.finish();
    }


    @Override
    public void OngrxxSuccess(FgrxxBean bean) {
        MmkvUtils.getInstance().set_common_json2(AppCommonUtils.userInfo, bean);
        retryData();
        Intent intent = new Intent();
        intent.setAction("refreshAction");
        LocalBroadcastManagers.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void OngrxxNodata(String bean) {
        ToastUtils.showShort(bean);
    }

    @Override
    public void OngrxxFail(String msg) {
        ToastUtils.showShort(msg);
    }
}
