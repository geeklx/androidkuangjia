package com.yuntongxun.youhui.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 *
 */
public class ContactAllListPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments;

    public ContactAllListPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0) {
            return "好友";
        } else if (position== 1) {
            return "群组";
        } else {
            return "好友";
        }
    }
}
