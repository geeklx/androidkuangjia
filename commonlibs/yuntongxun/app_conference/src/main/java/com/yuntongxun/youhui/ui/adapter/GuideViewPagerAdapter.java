package com.yuntongxun.youhui.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Gethin on 2017/8/21 0021.
 */

public class GuideViewPagerAdapter extends FragmentPagerAdapter {
    private int tab = 3;
    private List<Fragment> fragments;
    public GuideViewPagerAdapter(FragmentActivity activity, List<Fragment> fragments,int tab) {
        super(activity.getSupportFragmentManager());
        this.tab = tab;
        this.fragments = fragments;
    }

    public void setTabCount(int tab) {
        this.tab = tab;
    }

    public int getTab() {
        return tab;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return tab;
    }

}
