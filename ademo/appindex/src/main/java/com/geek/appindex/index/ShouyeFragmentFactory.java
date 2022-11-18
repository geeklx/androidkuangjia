package com.geek.appindex.index;

import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;
import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;

import com.geek.appcommon.wechat.fragment.H5WebFragment;
import com.geek.appcommon.ytx.YHCReserveListFragment2;
import com.geek.appcommon.ytx.im.RxContactHomeFragment;
import com.geek.appindex.index.fragment.Shouye10;
import com.geek.appindex.index.fragment.ShouyeF1;
import com.geek.appindex.index.fragment.ShouyeF3;
import com.geek.appindex.index.fragment.ShouyeF5;
import com.geek.appindex.index.fragment.ShouyeF6;
import com.geek.appindex.index.fragment.ShouyeF8;
import com.geek.appindex.index.fragment.ShouyeF9;
import com.geek.biz1.bean.BjyyBeanYewu3;
import com.geek.libutils.data.MmkvUtils;
import com.tencent.qcloud.tuikit.tuicontact.ui.pages.TUIContactFragment;
import com.tencent.qcloud.tuikit.tuiconversation.ui.page.TUIConversationFragment;

import java.util.ArrayList;
import java.util.List;

public class ShouyeFragmentFactory {


    public static final String TAG_shouye = "com.geek.appindex.index.activity.GztFragmentShouyeAct";
    public static final String TAG_xiaoxi = "com.geek.appindex.index.activity.GztFragmentXiaoxiAct?condition=login";
    public static final String TAG_xiaoxi2 = "com.geek.appindex.index.activity.GztFragmentXiaoxiAct2?condition=login";
    public static final String TAG_people = "com.geek.appindex.index.activity.GztFragmentPeopleAct?condition=login";
    public static final String TAG_people2 = "com.geek.appindex.index.activity.GztFragmentPeopleAct2?condition=login";
    public static final String TAG_huiyi = "com.geek.appcommon.ytx.YHCReserveListActivity2?condition=login";
    public static final String TAG_my = "com.geek.appindex.index.activity.GztFragmentMyAct";
    public static final String TAG_kuangjia1 = "com.geek.appindex.index.activity.GztFragmentKuangjia1Act";
    public static final String TAG_kuangjia2 = "com.geek.appindex.index.activity.GztFragmentKuangjia2Act";
    public static final String TAG_zixun = "com.geek.appindex.index.activity.GztFragmentZixunAct";
    public static final String TAG_shuzixiangcun = "com.geek.appindex.index.activity.GztFragmentShuzixiangcunAct";
    public static final String TAG_dangjianyinling = "com.geek.appindex.index.activity.GztFragmentDangjianyinlingAct";


    /**
     * Class初始化
     */
    private final SparseArrayCompat<Class<? extends Fragment>> mFragmentClasses = new SparseArrayCompat<>();
    /**
     * 导航栏数据对象
     */
    private List<BjyyBeanYewu3> mNavigationList = new ArrayList<>();

    public ShouyeFragmentFactory(List<BjyyBeanYewu3> mNavigationList) {
        this.mNavigationList = mNavigationList;
    }

    /**
     * 根据位置获取bean
     *
     * @param position
     * @return
     */
    public BjyyBeanYewu3 getNavigationEntity(int position) {
        if (mNavigationList.size() == 0) {
            return null;
        }
        //如果position是数组越界的，默认取第一个
        if (position < 0 || position >= mNavigationList.size()) {
            position = 0;
        }
        return mNavigationList.get(position);
    }

    /**
     * 获取当前选择position
     *
     * @param navigationId
     * @return
     */
    public int getPositionByNavigationId(String navigationId) {
        int size = mNavigationList.size();
        for (int i = 0; i < size; i++) {
            BjyyBeanYewu3 navigationEntity = mNavigationList.get(i);
            if (TextUtils.equals(navigationId, navigationEntity.getId())) {
                return i;
            }
        }
        return -1;
    }

    public Class<? extends Fragment> getNavigationClass(int id) {
        return mFragmentClasses.get(id);
    }

    /**
     * 更新导航对应的Fragment
     *
     * @param navList 导航集合
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void navTabUpdate(List<BjyyBeanYewu3> navList) {
        mFragmentClasses.clear();
        if (navList == null || navList.isEmpty()) {
            //服务器返回为空,移除所有
            return;
        }
        Class<? extends Fragment> classFragment;
        for (BjyyBeanYewu3 nav : navList) {
            if (nav == null) {
                continue;
            }
            classFragment = getFragmentByNavId(nav.getUrl());
            if (classFragment == null) {
                continue;
            }
            mFragmentClasses.put(Integer.parseInt(nav.getId()), classFragment);
        }
    }

    /**
     * 根据导航url，获取对应的容器Class
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private Class<? extends Fragment> getFragmentByNavId(String url) {
        if (url.contains("http")) {
            return H5WebFragment.class;
        }
        switch (url.trim()) {
            case ShouyeFragmentFactory.TAG_shouye:
                return ShouyeF5.class;
            case ShouyeFragmentFactory.TAG_xiaoxi:
                MmkvUtils.getInstance().set_common("choose_im", ShouyeFragmentFactory.TAG_people);
                return TUIConversationFragment.class;
            case ShouyeFragmentFactory.TAG_xiaoxi2:
                MmkvUtils.getInstance().set_common("choose_im", ShouyeFragmentFactory.TAG_people2);
                return RxContactHomeFragment.class;
            case ShouyeFragmentFactory.TAG_people:
                return TUIContactFragment.class;
            case ShouyeFragmentFactory.TAG_huiyi:
                return YHCReserveListFragment2.class;
            case ShouyeFragmentFactory.TAG_my:
                return ShouyeF6.class;
            case ShouyeFragmentFactory.TAG_kuangjia1:
                return ShouyeF1.class;
            case ShouyeFragmentFactory.TAG_kuangjia2:
                return ShouyeF3.class;
            case ShouyeFragmentFactory.TAG_zixun:
                return Shouye10.class;
            case ShouyeFragmentFactory.TAG_shuzixiangcun:
                return ShouyeF9.class;
            case ShouyeFragmentFactory.TAG_dangjianyinling:
                return ShouyeF8.class;
            default:
                return ShouyeF5.class;
        }
    }


    public Class<? extends Fragment> get(int id) {
        if (mFragmentClasses.indexOfKey(id) < 0) {
            throw new UnsupportedOperationException("cannot find fragment by " + id);
        }
        return mFragmentClasses.get(id);
    }

    public SparseArrayCompat<Class<? extends Fragment>> get() {
        return mFragmentClasses;
    }
}
