package com.geek.appcommon;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.BarUtils;
import com.geek.common.R;
import com.geek.libbase.base.SlbBaseActivity;
import com.just.agentweb.geek.base.BaseAgentWebActivityJs2;

/**
 * 使用此类，布局必须include toorbar_common_dt 或者 自定义相同id的布局
 */
public abstract class SlbBaseToolBar extends SlbBase {

    public int theme;
    public static final String KEY = "current_theme";

    protected ImageView mBackImageView;
    protected ImageView mFinishImageView;
    protected TextView mTitleTextView;
    protected ImageView mMoreImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        BarUtils.setStatusBarLightMode(this, false);
    }

    @Override
    protected void setup(@Nullable Bundle savedInstanceState) {
        super.setup(savedInstanceState);
        initView();
    }

    protected void initView() {
        mBackImageView = findViewById(R.id.iv_back);
        mFinishImageView = findViewById(R.id.iv_finish);
        mTitleTextView = findViewById(R.id.toolbar_title);
        mBackImageView.setOnClickListener(mOnClickListener);
        mFinishImageView.setOnClickListener(mOnClickListener);
        mMoreImageView = findViewById(R.id.iv_more);
        mMoreImageView.setOnClickListener(mOnClickListener);
        pageNavigator(View.GONE);
    }


    protected void pageNavigator(int tag) {
        mBackImageView.setVisibility(tag);
    }

    protected View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.iv_back) {
                finish();
            } else if (id == R.id.iv_finish) {
//                BaseAgentWebActivity.this.finish();
                onclickX(v);
            } else if (id == R.id.iv_more) {
//                showPoPup(v);
                onclickMore(v);
            }
        }

    };

    protected abstract void onclickX(View v);

    protected abstract void onclickMore(View v);

    //    @Override
//    public Resources getResources() {
//        //需要升级到 v1.1.2 及以上版本才能使用 AutoSizeCompat
//        if (Looper.myLooper()==Looper.getMainLooper()){
//            AutoSizeCompat.autoConvertDensityOfGlobal((super.getResources()));//如果没有自定义需求用这个方法
//            AutoSizeCompat.autoConvertDensity((super.getResources()), 667, false);//如果有自定义需求就用这个方法
//        }
//        return super.getResources();
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY, theme);
    }

}
