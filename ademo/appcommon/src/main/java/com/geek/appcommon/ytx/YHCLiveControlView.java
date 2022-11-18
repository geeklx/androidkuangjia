package com.geek.appcommon.ytx;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.geek.common.R;

import xyz.doikki.videocontroller.StandardVideoController;
import xyz.doikki.videocontroller.component.TitleView;
import xyz.doikki.videoplayer.controller.ControlWrapper;
import xyz.doikki.videoplayer.controller.GestureVideoController;
import xyz.doikki.videoplayer.controller.IControlComponent;
import xyz.doikki.videoplayer.util.PlayerUtils;

public class YHCLiveControlView extends GestureVideoController {
    public YHCLiveControlView(@NonNull Context context) {
        this(context, (AttributeSet)null);
    }

    public YHCLiveControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YHCLiveControlView(@NonNull Context context, @Nullable AttributeSet attrs,
                              int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_control;
    }

    @Override
    protected void initView() {
        super.initView();
        TextView tvFinish = findViewById(R.id.tv_finish);
        tvFinish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = PlayerUtils.scanForActivity(YHCLiveControlView.this.getContext());
                if (activity != null) {
                    activity.finish();
                }
            }
        });

        TextView tvRefresh = findViewById(R.id.tv_refresh);
        tvRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                YHCLiveControlView.this.mControlWrapper.replay(true);
            }
        });
    }

}
