package com.geek.appcommon.ytx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.geek.common.R;
import com.geek.libbase.base.SlbBaseLazyFragmentNew;

import com.geek.libbase.widgets.ViewPagerSlide;
import com.geek.tablib.tablayout.SlidingTabLayout;
import com.yuntongxun.plugin.common.ui.tools.YTXStatusBarUtil;
import com.yuntongxun.plugin.conference.manager.YHCConfConstData;

import com.yuntongxun.plugin.conference.view.activity.YHCConfStartAndReserveActivity;
import com.yuntongxun.plugin.conference.view.activity.YHCHistoryListActivity;
import com.yuntongxun.plugin.conference.view.activity.YHCJoinConfActivity;
import com.yuntongxun.youhui.ui.activity.LoginActivity;
import com.yuntongxun.youhui.ui.activity.SettingConfActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 容视首页Fragment
 * 1、初始化SDK
 * 2、SDK初始化后查询正在进行中的会议
 * 3、
 * Created by Gethin
 */

public class YHCReserveListFragment2 extends SlbBaseLazyFragmentNew implements View.OnClickListener {
    private String TAG = "YHCReserveListFragment2";

    private TextView tv_sz1;
    private TextView tv_sz2;
    private TextView entityReserveMeetingOpen, entityCloudMeetingOpen, entityConnectHardwareOpen;
    private SlidingTabLayout spTabLayout;
    private ViewPagerSlide spViewPager;
    private YHCViewPagerAdapter viewPagerAdapter;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private List<String> titlesList = new ArrayList<>();

    public static YHCReserveListFragment2 getInstance(Bundle bundle) {
        YHCReserveListFragment2 mEasyWebFragment = new YHCReserveListFragment2();
        if (bundle != null) {
            mEasyWebFragment.setArguments(bundle);
        }
        return mEasyWebFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public int getLayoutId() {
        return R.layout.ytx_activity_sphy_new;
    }

    @Override
    protected void setup(View rootView, @Nullable Bundle savedInstanceState) {
        super.setup(rootView, savedInstanceState);
        initView(rootView);
    }

    private void initView(View rootView) {

        LinearLayout entityMeeting = rootView.findViewById(R.id.entity_meeting2);
        if (YHCConfConstData.USE_ENTITY_MEETING) {
            entityMeeting.setVisibility(View.VISIBLE);
        } else {
            entityMeeting.setVisibility(View.GONE);
        }

        tv_sz1 = rootView.findViewById(R.id.tv_sz1);
        tv_sz2 = rootView.findViewById(R.id.tv_sz2);
        entityReserveMeetingOpen = rootView.findViewById(R.id.entity_start_meeting_open2);
        entityCloudMeetingOpen = rootView.findViewById(R.id.entity_reser_metting_open2);
        entityConnectHardwareOpen = rootView.findViewById(R.id.entity_enterprise_metting_open2);

        tv_sz1.setOnClickListener(this);
        tv_sz2.setOnClickListener(this);
        entityReserveMeetingOpen.setOnClickListener(this);
        entityCloudMeetingOpen.setOnClickListener(this);
        entityConnectHardwareOpen.setOnClickListener(this);

        spTabLayout = rootView.findViewById(R.id.sp_tab_layout);
        spViewPager = rootView.findViewById(R.id.sp_view_pager);
        spViewPager.setScroll(true);

        mFragments.add(new YHCReserveChildFragment());
        mFragments.add(new YHCLiveFragment());
        titlesList.add("会议");
        titlesList.add("直播");

        if (viewPagerAdapter == null) {
            viewPagerAdapter = new YHCViewPagerAdapter(getChildFragmentManager());
        }

        spViewPager.setAdapter(viewPagerAdapter);
        spTabLayout.setViewPager(spViewPager);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent();
        if (/*id == R.id.entity_reser_metting2 ||*/ id == R.id.entity_reser_metting_open2) {
            intent.setClass(getContext(), YHCConfStartAndReserveActivity.class);
            startActivity(intent);
        } else if (/*id == R.id.entity_start_meeting2 ||*/ id == R.id.entity_start_meeting_open2) {
            intent.setClass(getContext(), YHCJoinConfActivity.class);
            startActivity(intent);
        } else if (/*id == R.id.entity_enterprise_metting2 ||*/ id == R.id.entity_enterprise_metting_open2) {
//            if (!TextUtil.isEmpty(ConferenceService.getInstance().mMeetingNo) && ConferenceService.getInstance().mVoipSmallWindow != null) {
//                ConfToasty.error(getString(R.string.yhc_conf_running_tips));
//                return;
//            }
//            enterConnectHardware(intent);
            intent.setClass(getContext(), YHCHistoryListActivity.class);
            startActivity(intent);
        } /*else if (id == R.id.entity_meeting_scan2 || id == R.id.entity_meeting_scan_open2) {
            if (!TextUtil.isEmpty(ConferenceService.getInstance().mMeetingNo) && ConferenceService.getInstance().mVoipSmallWindow != null) {
                ConfToasty.error(getString(R.string.yhc_conf_running_tips));
                return;
            }
            intent = new Intent();
//            if (EasyPermissionsEx.hasPermissions(getContext(), needPermissionsCamera)) {
//                intent.setClassName(getContext(), "com.yuntongxun.plugin.zxing.activity.YHCQRScanActivity");
//                startActivity(intent);
//            } else {
//                EasyPermissionsEx.requestPermissions(this, rationaleCamera, PERMISSIONS_REQUEST_CAMERA, needPermissionsCamera);
//            }
            if (YHCConferenceMgr.getManager().deviceListener != null) {
                YHCConferenceMgr.getManager().deviceListener.onControlDeviceJoinConf(getActivity(), "", "");
                ECHandlerHelper.postDelayedRunnOnUI(this::finish, 300);
            }
        } else if (id == R.id.entity_meeting_more2 || id == R.id.entity_meeting_more_open2) {
//            controlPlusSubMenu(id == R.id.entity_meeting_more ? 50F : 36.0F);
        }*/ else if (id == R.id.tv_sz1) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else if (id == R.id.tv_sz2) {
            startActivity(new Intent(getActivity(), SettingConfActivity.class));
        }
    }

    private class YHCViewPagerAdapter extends FragmentPagerAdapter {
        public YHCViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titlesList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}

