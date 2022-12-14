package com.geek.appindex.tyxsbofangqi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.geek.biz1.bean.HTyxs1Bean;
import com.geek.biz1.presenter.HTyxs1Presenter;
import com.geek.biz1.presenter.HTyxs2Presenter;
import com.geek.biz1.presenter.HTyxs3Presenter;
import com.geek.biz1.view.Sbtyxs1View;
import com.geek.biz1.view.Sbtyxs2View;
import com.geek.biz1.view.Sbtyxs3View;
import com.geek.libutils.app.MyLogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import xyz.doikki.dkplayer.R;
import xyz.doikki.dkplayer.activity.BaseActivityDk;
import xyz.doikki.dkplayer.util.IntentKeysDk;
import xyz.doikki.dkplayer.util.UtilsDk;
import xyz.doikki.dkplayer.widget.component.DebugInfoViewDk;
import xyz.doikki.videocontroller.StandardVideoController;
import xyz.doikki.videocontroller.component.CompleteView;
import xyz.doikki.videocontroller.component.ErrorView;
import xyz.doikki.videocontroller.component.GestureView;
import xyz.doikki.videocontroller.component.PrepareView;
import xyz.doikki.videocontroller.component.TitleView;
import xyz.doikki.videocontroller.component.VodControlView;
import xyz.doikki.videoplayer.controller.ControlWrapper;
import xyz.doikki.videoplayer.controller.IControlComponent;
import xyz.doikki.videoplayer.player.AbstractPlayer;
import xyz.doikki.videoplayer.player.VideoView;
import xyz.doikki.videoplayer.util.L;

public class VideoPlayerAct extends BaseActivityDk<VideoView<AbstractPlayer>> implements Sbtyxs1View, Sbtyxs2View, Sbtyxs3View {

    private static final String THUMB = "https://cms-bucket.nosdn.127.net/eb411c2810f04ffa8aaafc42052b233820180418095416.jpeg";
    private String url;
    private ScheduledExecutorService mExecutorService;
    private HTyxs1Presenter hTyxs1Presenter;
    private HTyxs2Presenter hTyxs2Presenter;
    private HTyxs3Presenter hTyxs3Presenter;

    public static void start(Context context, String url, String title, boolean isLive) {
        Intent intent = new Intent(context, VideoPlayerAct.class);
        intent.putExtra(IntentKeysDk.URL, url);
        intent.putExtra(IntentKeysDk.IS_LIVE, isLive);
        intent.putExtra(IntentKeysDk.TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected void onResume() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (hTyxs1Presenter != null) {
            hTyxs1Presenter.onDestory();
        }
        if (hTyxs2Presenter != null) {
            hTyxs2Presenter.onDestory();
        }
        if (hTyxs3Presenter != null) {
            hTyxs3Presenter.onDestory();
        }
        if (mExecutorService != null) {
            mExecutorService.shutdown();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void updateUI(String mMediaProjection) {
        if (mMediaProjection != null) {
            MyLogUtil.e("ssssss", mMediaProjection);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_playerdk;
    }

    @Override
    protected void initView() {
        super.initView();
        mVideoView = findViewById(R.id.player);
        //??????????????????
        EditText etOtherVideo = findViewById(R.id.et_other_video);
        findViewById(R.id.btn_start_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.release();
                mVideoView.setUrl(etOtherVideo.getText().toString());
                mVideoView.start();
            }
        });
        //
        EventBus.getDefault().post("????????????");
        //
        hTyxs1Presenter = new HTyxs1Presenter();
        hTyxs1Presenter.onCreate(this);
        hTyxs2Presenter = new HTyxs2Presenter();
        hTyxs2Presenter.onCreate(this);
        hTyxs3Presenter = new HTyxs3Presenter();
        hTyxs3Presenter.onCreate(this);
        //
        SPUtils.getInstance().put("accessSecret", "B0548940A4131F7D0C82F6B45AE848E0");
        SPUtils.getInstance().put("accessKey", "3F1AE863EF8BC2B9251A5526FE1C26BC");
        userId = "1001805878294925312";
        courseCode = "3501385409340901813";
        source_sys = "EDU_NET_COLLEGE";
        orgType = "0";
        actionCode = "COURSE_STUDY";
        hTyxs1Presenter.getHTyxs1Presenter(userId, courseCode, source_sys, orgType, actionCode);
        //??????
//        HTyxs1Bean hTyxs1Bean = new HTyxs1Bean();
//        hTyxs1Bean.setDrag(true);
//        hTyxs1Bean.setStudyStatus("?????????");
//        hTyxs1Bean.setStudyTimes(SPUtils.getInstance().getLong("xianzhi_times",0)+"");
//        setvideo(hTyxs1Bean);

    }

    private VodControlView vodControlView = null;

    private void setvideo(HTyxs1Bean versionInfoBean) {
        Intent intent = getIntent();
        if (intent != null) {
            StandardVideoController controller = new StandardVideoController(this);
            //??????????????????????????????/????????????
            controller.setEnableOrientation(true);
            PrepareView prepareView = new PrepareView(this);//??????????????????
            ImageView thumb = prepareView.findViewById(R.id.thumb);//?????????
            Glide.with(this).load(THUMB).into(thumb);
            controller.addControlComponent(prepareView);
            controller.addControlComponent(new CompleteView(this));//????????????????????????
            controller.addControlComponent(new ErrorView(this));//????????????
            TitleView titleView = new TitleView(this);//?????????
            controller.addControlComponent(titleView);
            //???????????????????????????????????????????????????
            boolean isLive = intent.getBooleanExtra(IntentKeysDk.IS_LIVE, false);
            vodControlView = new VodControlView(this);//???????????????
            //??????????????????????????????????????????
            vodControlView.showBottomProgress(true);
            controller.addControlComponent(vodControlView);
            GestureView gestureControlView = new GestureView(this);//??????????????????
            controller.addControlComponent(gestureControlView);
            //?????????????????????????????????????????????????????????
            controller.setCanChangePosition(!isLive);
            //????????????
            String title = intent.getStringExtra(IntentKeysDk.TITLE);
            titleView.setTitle(title);
            //?????????????????????????????????
            controller.addControlComponent(new DebugInfoViewDk(this));
            //???LogCat??????????????????
//            controller.addControlComponent(new PlayerMonitorDk());
            controller.addControlComponent(new IControlComponent() {
                @Override
                public void attach(@NonNull ControlWrapper controlWrapper) {

                }

                @Override
                public View getView() {
                    return null;
                }

                @Override
                public void onVisibilityChanged(boolean isVisible, Animation anim) {

                }

                @Override
                public void onPlayStateChanged(int playState) {

                }

                @Override
                public void onPlayerStateChanged(int playerState) {

                }

                @SuppressLint("LongLogTag")
                @Override
                public void setProgress(int duration, int position) {
//                    Log.e("VideoPlayerAct-mVideoViewsetProgress", duration + "");
                    Log.e("VideoPlayerAct-mVideoViewsetProgress-position", position + "");
                    long max = Long.parseLong(SPUtils.getInstance().getString("xianzhi_times", "0"));
                    Log.e("VideoPlayerAct-mVideoViewsetProgress-max", max + "");
                    //
                    if (position != 0) {
                        // ??????pos=0?????????????????????
                        SPUtils.getInstance().put(String.valueOf(url.hashCode()), max + "");
                    }
                    if (position > max) {
                        SPUtils.getInstance().put("xianzhi_times", (long) position + "");
                    }
                }

                @Override
                public void onLockStateChanged(boolean isLocked) {

                }
            });
//            //?????????????????????????????????????????????bufen
//            if (versionInfoBean.isDrag()) {
//                // ??????
//                Log.e("VideoPlayerAct-??????????????????", versionInfoBean.getStudyTimes());
//
//                if (Long.parseLong(SPUtils.getInstance().getString(String.valueOf(url.hashCode()), "0")) <
//                        (long) (Integer.parseInt(versionInfoBean.getStudyTimes())) * 1000) {
//                } else {
//                    SPUtils.getInstance().put(String.valueOf(url.hashCode()), (long) (Integer.parseInt(versionInfoBean.getStudyTimes())) * 1000 + "");
//                }
//            }
            // ??????????????????MAX
            vodControlView.setmIsxianzhi(versionInfoBean.isDrag());
            //??????????????????
            mVideoView.setProgressManager(new ProgressManagerImplDk1());
            //??????????????????UI??????????????????????????????
            mVideoView.setVideoController(controller);

            url = intent.getStringExtra(IntentKeysDk.URL);

            //??????????????????????????????????????????DKPlayer??????????????????????????????
            if (TextUtils.isEmpty(url)
                    && Intent.ACTION_VIEW.equals(intent.getAction())) {
                //??????intent??????????????????
                url = UtilsDk.getFileFromContentUri(this, intent.getData());
            }
            mVideoView.setUrl(url);
            //??????????????????
            mVideoView.addOnStateChangeListener(mOnStateChangeListener);
            //??????????????????
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("mVideoView", mVideoView.getCurrentPosition() + "");// ???????????????
                    Log.e("mVideoView2", Long.parseLong(SPUtils.getInstance().getString(String.valueOf(url.hashCode()), "0")) + "");// ????????????
                }
            }, 1000);

            //????????????????????????????????????????????????VideoConfig???????????????MyApplication
            //??????IjkPlayer??????
//            mVideoView.setPlayerFactory(IjkPlayerFactory.create());
            //??????ExoPlayer??????
//            mVideoView.setPlayerFactory(ExoMediaPlayerFactory.create());
            //??????MediaPlayer??????
//            mVideoView.setPlayerFactory(AndroidMediaPlayerFactory.create());

            //??????????????????
//            mVideoView.setMute(true);
            mVideoView.start();
            //??????????????????bufen
            setTime(versionInfoBean);

        }
    }

    private String userId;
    private String courseCode;
    private String source_sys;
    private String orgType;
    private String actionCode;

    private VideoView.OnStateChangeListener mOnStateChangeListener = new VideoView.SimpleOnStateChangeListener() {
        @Override
        public void onPlayerStateChanged(int playerState) {
            switch (playerState) {
                case VideoView.PLAYER_NORMAL://??????
                    break;
                case VideoView.PLAYER_FULL_SCREEN://??????
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPlayStateChanged(int playState) {
            switch (playState) {
                case VideoView.STATE_IDLE:
                    break;
                case VideoView.STATE_PREPARING:
                    break;
                case VideoView.STATE_PREPARED:
                    break;
                case VideoView.STATE_PLAYING:
                    //??????????????????????????????
                    int[] videoSize = mVideoView.getVideoSize();
                    L.d("????????????" + videoSize[0]);
                    L.d("????????????" + videoSize[1]);

                    break;
                case VideoView.STATE_PAUSED:
                    break;
                case VideoView.STATE_BUFFERING:
                    break;
                case VideoView.STATE_BUFFERED:
                    break;
                case VideoView.STATE_PLAYBACK_COMPLETED:
                    // ????????????
                    Log.e("VideoPlayerAct-", "????????????");
                    if (mExecutorService != null) {
                        mExecutorService.shutdown();
                    }
//                    hTyxs1Presenter.getHTyxs1Presenter(userId, courseCode, source_sys, orgType, actionCode);
//                    hTyxs2Presenter.getHTyxs2Presenter(userId, courseCode, 15 + "");
                    hTyxs3Presenter.getHTyxs3Presenter(userId, courseCode, source_sys, orgType, actionCode);
                    break;
                case VideoView.STATE_ERROR:
                    break;
                default:
                    break;
            }
        }
    };

    private static int COMPLETED = 1;

    private void setTime(HTyxs1Bean versionInfoBean) {
        if (!versionInfoBean.isDrag()) {
            return;
        }
        mExecutorService = Executors.newScheduledThreadPool(1);
        mExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = COMPLETED;
                handler.sendMessage(message);

            }
        }, 0, 5000, TimeUnit.MILLISECONDS);
    }

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                // ??????????????????bufen
                long second = Long.parseLong(SPUtils.getInstance().getString(String.valueOf(url), "0")) / 1000;
                int seconds = Integer.parseInt(second + "");
                Log.e("VideoPlayerAct-??????????????????", seconds + "");
                hTyxs2Presenter.getHTyxs2Presenter(userId, courseCode, seconds + "");

            }
        }
    };

    private int i = 0;

    public void onButtonClick(View view) {
        int id = view.getId();
        if (id == R.id.scale_default) {
            mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_DEFAULT);
        } else if (id == R.id.scale_169) {
            mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_16_9);
        } else if (id == R.id.scale_43) {
            mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_4_3);
        } else if (id == R.id.scale_original) {
            mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_ORIGINAL);
        } else if (id == R.id.scale_match_parent) {
            mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_MATCH_PARENT);
        } else if (id == R.id.scale_center_crop) {
            mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_CENTER_CROP);
        } else if (id == R.id.speed_0_5) {
            mVideoView.setSpeed(0.5f);
        } else if (id == R.id.speed_0_75) {
            mVideoView.setSpeed(0.75f);
        } else if (id == R.id.speed_1_0) {
            mVideoView.setSpeed(1.0f);
        } else if (id == R.id.speed_1_5) {
            mVideoView.setSpeed(1.5f);
        } else if (id == R.id.speed_2_0) {
            mVideoView.setSpeed(2.0f);
        } else if (id == R.id.screen_shot) {
            ImageView imageView = findViewById(R.id.iv_screen_shot);
            Bitmap bitmap = mVideoView.doScreenShot();
            imageView.setImageBitmap(bitmap);
        } else if (id == R.id.mirror_rotate) {
            mVideoView.setMirrorRotation(i % 2 == 0);
            i++;
        } else if (id == R.id.btn_mute) {
            mVideoView.setMute(!mVideoView.isMute());
        }
    }

    @Override
    public void OnSbtyxs1Success(HTyxs1Bean versionInfoBean) {
        setvideo(versionInfoBean);
    }

    @Override
    public void OnSbtyxs1Nodata(String bean) {

    }

    @Override
    public void OnSbtyxs1Fail(String msg) {

    }

    @Override
    public void OnSbtyxs2Success(String versionInfoBean) {

    }

    @Override
    public void OnSbtyxs2Nodata(String bean) {

    }

    @Override
    public void OnSbtyxs2Fail(String msg) {

    }

    @Override
    public void OnSbtyxs3Success(String versionInfoBean) {

    }

    @Override
    public void OnSbtyxs3Nodata(String bean) {

    }

    @Override
    public void OnSbtyxs3Fail(String msg) {

    }

    @Override
    public String getIdentifier() {
        return null;
    }
}
