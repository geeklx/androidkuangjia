package com.rongxin.im.dome.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.rongxin.im.dome.R;
import com.yuntongxun.confwrap.WrapManager;
import com.yuntongxun.im.ui.SelectContactsUI;
import com.yuntongxun.plugin.common.common.bar.YTXMeizuStatusBarCompat;
import com.yuntongxun.plugin.common.common.bar.YTXStatusBarCompat;
import com.yuntongxun.plugin.common.common.view.CCPCustomViewPager;
import com.yuntongxun.plugin.common.ui.YTXECSuperActivity;
import com.yuntongxun.plugin.common.ui.tools.YTXSystemBarHelper;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends YTXECSuperActivity {

    private TextView unReadMsg;

    private CCPCustomViewPager viewPager;
    private final List<Fragment> arrays = new ArrayList<>();
    /**
     * 应用标题栏
     */
    private ActionBar mActionBar;
    /**
     * 1.应用标题栏
     * 2.头部标题
     */
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        immersiveBar();

        initFragments();
        initView();
        initActionBar();
    }


    private void initView() {

//        RXConfig.isShowAvatarRound = true;
        viewPager = (CCPCustomViewPager) findViewById(R.id.convert_frame);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setSlideEnabled(false);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
    }

    private void initFragments() {
        arrays.clear();
        arrays.add(WrapManager.getInstance().ConversationListFragment());
    }

    @Override
    public boolean hasActionBar() {
        return true;
    }

    private void immersiveBar() {

        YTXStatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));
        YTXSystemBarHelper.immersiveStatusBar(this, 0);

        if (Build.DISPLAY.startsWith("Flyme")) {
            YTXMeizuStatusBarCompat.setStatusBarDarkIcon(this, true);
        }
        View mStatusBarView = findViewById(R.id.ytx_status_bar_view);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, YTXSystemBarHelper.getStatusBarHeight(mStatusBarView.getContext()));
        mStatusBarView.setLayoutParams(lp);
        mStatusBarView.requestLayout();
    }

    private void initActionBar() {
        setActionMenuItem(1, R.mipmap.ytx_title_bar_add, new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent2 = new Intent(ContactActivity.this, SelectContactsUI.class);
                intent2.putExtra("type",  1);
                startActivity(intent2);
                return true;
            }
        });

    }


    public int getLayoutId() {
        return R.layout.ytx_activity_contact;
    }

    private void initSelfStatusBarView(boolean attachToSelfView) {
        View mStatusBarView = findViewById(R.id.ytx_status_bar_view);
        if (mStatusBarView != null) {
            mStatusBarView.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, attachToSelfView ? YTXSystemBarHelper.getStatusBarHeight(mStatusBarView.getContext()) : 0);
            mStatusBarView.setBackgroundColor(getResources().getColor(R.color.white));
            mStatusBarView.setLayoutParams(lp);
            mStatusBarView.requestLayout();
        }
    }

    private class FragmentAdapter extends FragmentStatePagerAdapter {


        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return arrays.get(position);
        }

        @Override
        public int getCount() {
            return arrays.size();
        }
    }


}
