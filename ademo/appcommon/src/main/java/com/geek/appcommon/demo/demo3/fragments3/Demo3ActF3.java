package com.geek.appcommon.demo.demo3.fragments3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ToastUtils;
import com.geek.appcommon.AppCommonUtils;
import com.geek.appcommon.wechat.fragment.H5WebFragment;
import com.geek.biz1.bean.BjyyBeanYewu3;
import com.geek.common.R;
import com.geek.libbase.base.SlbBaseLazyFragmentNew;
import com.geek.libbase.baserecycleview.SlbBaseFragmentViewPagerAdapterlan2;
import com.geek.libbase.widgets.ViewPagerSlide;
import com.geek.libutils.app.LocalBroadcastManagers;
import com.geek.libutils.app.MyLogUtil;
import com.geek.tablib.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

public class Demo3ActF3 extends SlbBaseLazyFragmentNew {

    private String tablayoutId;
    private TextView tv_center_content;
    private MessageReceiverIndex mMessageReceiver;

    public class MessageReceiverIndex extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if ("Demo3ActF3".equals(intent.getAction())) {
                    //TODO 发送广播bufen
                    Intent msgIntent = new Intent();
                    msgIntent.setAction("Demo3ActF3");
                    msgIntent.putExtra("query1", 0);
                    LocalBroadcastManagers.getInstance(getActivity()).sendBroadcast(msgIntent);
                }
            } catch (Exception ignored) {
            }
        }
    }

    private SlbBaseFragmentViewPagerAdapterlan2 fenleiViewPagerAdapter1;
    private SlidingTabLayout tabLayout;
    private ViewPagerSlide viewpager;
    private List<BjyyBeanYewu3> bjyyBeanYewu3;
    private List<Fragment> mFragmentList;

    public static Demo3ActF3 getInstance(BjyyBeanYewu3 bean) {
        Demo3ActF3 fragment = new Demo3ActF3();
        Bundle bundle = new Bundle();
        bundle.putSerializable("feileiBean", bean);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Demo3ActF3 getInstance(Bundle bundle) {
        Demo3ActF3 mEasyWebFragment = new Demo3ActF3();
        if (bundle != null) {
            mEasyWebFragment.setArguments(bundle);
        }
        return mEasyWebFragment;

    }

    @Override
    public void call(Object value) {
        tablayoutId = (String) value;
        ToastUtils.showLong("call->" + tablayoutId);
        MyLogUtil.e("tablayoutId->", "call->" + tablayoutId);

    }

    @Override
    public void onDestroy() {
        LocalBroadcastManagers.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        // 从缓存中拿出头像bufen

        super.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
//        Bundle arg = getArguments();
        if (getArguments() != null) {
            tablayoutId = getArguments().getString("tablayoutId");
            MyLogUtil.e("tablayoutId->", "onCreate->" + tablayoutId);
            bjyyBeanYewu3 = (List<BjyyBeanYewu3>) getArguments().getSerializable("BjyyBeanYewu3");
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_demo3_f1;
    }

    @Override
    protected void setup(final View rootView, @Nullable Bundle savedInstanceState) {
        super.setup(rootView, savedInstanceState);
        tabLayout = rootView.findViewById(R.id.tl_fenlei1);
        viewpager = rootView.findViewById(R.id.vs_fenlei1);
        //
        mMessageReceiver = new MessageReceiverIndex();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction("Demo3ActF3");
        LocalBroadcastManagers.getInstance(getActivity()).registerReceiver(mMessageReceiver, filter);
        //
        donetwork();
//        ShouyeFooterBean bean = new Gson().fromJson("{}", ShouyeFooterBean.class);
//        MyLogUtil.e("sssssssssss", bean.toString());
//        MyLogUtil.e("sssssssssss", bean.getText_id());
//        tv_center_content.setText(bean.getText_id());
    }

    /**
     * 第一次进来加载bufen
     */
    private void donetwork() {
        retryData();
    }

    // 刷新
    private void retryData() {
//        mEmptyView.loading();
//        presenter1.getLBBannerData("0");
//        refreshLayout1.finishRefresh();
//        emptyview1.success();
        setNewData(bjyyBeanYewu3);
    }


    private void setNewData(List<BjyyBeanYewu3> mlist) {
        // 写法1
//        String[] titlesString = new String[mlist.size()];
//        for (int i = 0; i < mlist.size(); i++) {
//            titlesString[i] = mlist.get(i).getName();
//        }
////        if (fenleiViewPagerAdapter1 == null) {
////            viewpager.removeAllViews();
////            fenleiViewPagerAdapter1 = new F3Adapter1<BjyyBeanYewu3>(
//////                    getActivity().getSupportFragmentManager(),
////                    getChildFragmentManager(), getActivity(), titlesString, mlist, F3Adapter1.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
////            viewpager.setOffscreenPageLimit(mlist.size());
////            viewpager.setScroll(true);
////            viewpager.setAdapter(fenleiViewPagerAdapter1);
////        } else {
////            fenleiViewPagerAdapter1.setdata(titlesString, mlist);
////            fenleiViewPagerAdapter1.notifyDataSetChanged();
////        }
//        fenleiViewPagerAdapter1 = new F3Adapter1<BjyyBeanYewu3>(
////                    getActivity().getSupportFragmentManager(),
//                getChildFragmentManager(), getActivity(), titlesString, mlist, F3Adapter1.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        // 写法2
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < mlist.size(); i++) {
            titles.add(mlist.get(i).getName());
        }
        mFragmentList = new ArrayList<>();
        mFragmentList.clear();
        for (int i = 0; i < mlist.size(); i++) {
            String url = mlist.get(i).getUrl();
            if (url.contains("http")) {
                Bundle bundle = new Bundle();
                bundle.putString("BjyyBeanYewu3", url);
                mFragmentList.add(H5WebFragment.getInstance(bundle));
            } else if (TextUtils.equals(url, AppCommonUtils.TAG_f1)) {
                mFragmentList.add(Demo3ActF3F1.getInstance(mlist.get(i)));
            } else if (TextUtils.equals(url, AppCommonUtils.TAG_f2)) {
                mFragmentList.add(Demo3ActF3F2.getInstance(mlist.get(i)));
            } else {
                mFragmentList.add(Demo3ActF3F1.getInstance(mlist.get(i)));
            }
        }
        fenleiViewPagerAdapter1 = new SlbBaseFragmentViewPagerAdapterlan2(
//                    getActivity().getSupportFragmentManager(),
                getChildFragmentManager(), getActivity(), titles, mFragmentList, SlbBaseFragmentViewPagerAdapterlan2.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        //
        fenleiViewPagerAdapter1.clear(viewpager);
        viewpager.setOffscreenPageLimit(mlist.size());
        viewpager.setScroll(true);
        viewpager.setAdapter(fenleiViewPagerAdapter1);
        tabLayout.setViewPager(viewpager);
        viewpager.setCurrentItem(current_pos);
        //        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
//            @Override
//            public void onTabSelect(int position) {
//                current_pos = position;
//            }
//
//            @Override
//            public void onTabReselect(int position) {
//
//            }
//        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                current_pos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * 底部点击bufen
     *
     * @param cateId
     * @param isrefresh
     */
    @Override
    public void getCate(String cateId, boolean isrefresh) {

        if (!isrefresh) {
            // 从缓存中拿出头像bufen

            return;
        }
        ToastUtils.showLong("下拉刷新啦");
    }

    /**
     * 当切换底部的时候通知每个fragment切换的id是哪个bufen
     *
     * @param cateId
     */
    @Override
    public void give_id(String cateId) {
//        ToastUtils.showLong("下拉刷新啦");
        MyLogUtil.e("tablayoutId->", "give_id->" + cateId);
    }

    /**
     * --------------------------------业务逻辑分割线----------------------------------
     */


}
