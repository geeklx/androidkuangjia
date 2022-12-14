//package com.geek.appcommon.ytx;
//
//
//import android.content.res.Resources;
//import android.os.Build;
//import android.os.Looper;
//import android.view.View;
//import android.view.WindowManager;
//
//import me.jessyan.autosize.AutoSizeCompat;
//import xyz.doikki.dkplayer.activity.BaseActivityDk;
//import xyz.doikki.videoplayer.player.AbstractPlayer;
//import xyz.doikki.videoplayer.player.VideoView;
//
//
//public class YHCLiveDetailActivity extends BaseActivityDk<VideoView<AbstractPlayer>> /*implements CustomAdapt */ {
//    private YHCLiveControlView mController;
//
//    public YHCLiveDetailActivity() {
//    }
//
//    //    @Override
////    protected void onDestroy() {
////        super.onDestroy();
////        AutoSizeCompat.cancelAdapt(super.getResources());
////    }
////
////    @Override
////    public boolean isBaseOnWidth() {
////        return false;
////    }
////
////    @Override
////    public float getSizeInDp() {
////        return 375;
////    }
//
//    @Override
//    public Resources getResources() {
//        //需要升级到 v1.1.2 及以上版本才能使用 AutoSizeCompat
//        if (Looper.myLooper() == Looper.getMainLooper()) {
//            AutoSizeCompat.autoConvertDensityOfGlobal((super.getResources()));//如果没有自定义需求用这个方法
//            AutoSizeCompat.autoConvertDensity((super.getResources()), 375, false);//如果有自定义需求就用这个方法
//        }
//        return super.getResources();
//    }
//
//    @Override
//    protected View getContentView() {
//        this.mVideoView = new VideoView(this);
//        adaptCutoutAboveAndroidP();
//        return this.mVideoView;
//    }
//
//    @Override
//    protected void initView() {
//        super.initView();
//        this.mVideoView.startFullScreen();
////        this.mVideoView.setUrl("http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4");
////        this.mVideoView.setUrl("rtmp://114.116.195.91:1935/live/123");
////        this.mVideoView.setUrl("http://114.116.195.91:8080/live/123.m3u8");
////        this.mVideoView.setUrl("rtmp://114.116.195.91:1935/live/123");
//        this.mVideoView.setUrl("http://114.116.195.91:8080/live/123.m3u8");
//        this.mController = new YHCLiveControlView(this);
//        this.mVideoView.setVideoController(this.mController);
//        mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_16_9);
//        this.mVideoView.start();
//    }
//
//
//    private void adaptCutoutAboveAndroidP() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            WindowManager.LayoutParams lp = getWindow().getAttributes();
//            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
//            getWindow().setAttributes(lp);
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        this.finish();
//    }
//
////    @Override
////    protected void onDestroy() {
////        super.onDestroy();
////        AutoSizeCompat.cancelAdapt(super.getResources());
////    }
//}
