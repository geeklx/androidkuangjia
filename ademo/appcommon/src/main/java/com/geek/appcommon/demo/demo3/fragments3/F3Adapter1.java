package com.geek.appcommon.demo.demo3.fragments3;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.geek.appcommon.AppCommonUtils;
import com.geek.appcommon.wechat.fragment.H5WebFragment;
import com.geek.biz1.bean.BjyyBeanYewu3;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class F3Adapter1<T> extends FragmentPagerAdapter {

    private Context mContext;
    //private List<Fragment> fragmentList;
    private String[] titles;
    private List<T> bean;
    private FragmentManager fm;


    public F3Adapter1(FragmentManager fm, Context context, String[] titles, List<T> bean, int behavior) {
        super(fm, behavior);
        this.fm = fm;
        this.mContext = context;
        this.bean = bean;
        //this.fragmentList = fragmentList;
        this.titles = titles;
    }

    public void setdata(String[] titles, List<T> bean) {
        this.bean = bean;
        this.titles = titles;
    }


    @Override
    public Fragment getItem(int position) {
        BjyyBeanYewu3 fenleiAct1CateBean1 = (BjyyBeanYewu3) bean.get(position);
        String url = fenleiAct1CateBean1.getUrl();
        if (url.contains("http")) {
            Bundle bundle = new Bundle();
            bundle.putString("BjyyBeanYewu3", url);
            return H5WebFragment.getInstance(bundle);

        }
        switch (url.trim()) {
            case AppCommonUtils.TAG_f1:
                //return RecommandFragment.Companion.getInstance(fenleiAct1CateBean1);
                return Demo3ActF3F1.getInstance(fenleiAct1CateBean1);
//            case TAG_DJDT:
//                //return DJDTFragment.Companion.getInstance(fenleiAct1CateBean1);
//                return ShouyeF3.getInstance(fenleiAct1CateBean1);
            default:
                //return RecommandFragment.Companion.getInstance(fenleiAct1CateBean1);
                return Demo3ActF3F1.getInstance(fenleiAct1CateBean1);
        }
    }

    @Override
    public int getCount() {
        return bean.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        ZXFragment2 fragment = (ZXFragment2) super.instantiateItem(container, position);
//        fragment.updateArguments((FenleiAct1CateBean1) bean.get(position));
//        return fragment;
//
//    }


//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        return super.instantiateItem(container, position);
//
//    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        fm.beginTransaction().remove((Fragment) object);
    }

    @Override
    public int getItemPosition(Object object) {
        // 最简单解决 notifyDataSetChanged() 页面不刷新问题的方法
        return POSITION_NONE;
    }

    public FragmentTransaction mCurTransaction;

    public String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ':' + id;
    }

    public final void clear(@NotNull ViewGroup container) {
        if (mCurTransaction == null) {
            mCurTransaction = fm.beginTransaction();
        }
        for (int i = 0; i < bean.size(); ++i) {
            long itemId = getItemId(i);
            String name = makeFragmentName(container.getId(), itemId);
            Fragment fragment = this.fm.findFragmentByTag(name);
            if (fragment != null) {
                mCurTransaction.remove(fragment);
            }
        }
        mCurTransaction.commitNowAllowingStateLoss();
    }

}