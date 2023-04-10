package com.geek.appcommon.demo.demo3;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.geek.appcommon.AppCommonUtils;
import com.geek.appcommon.SlbBase;
import com.geek.appcommon.demo.demo3.fragments3.Demo3ActF3;
import com.geek.biz1.bean.BjyyBeanYewu3;
import com.geek.common.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Demo3Activity extends SlbBase {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_demo3;
    }

    @Override
    protected void setup(@Nullable Bundle savedInstanceState) {
        super.setup(savedInstanceState);
        findView();
        onclick();
        donetwork(addList1());
    }

    public List<BjyyBeanYewu3> addList1() {
        List<BjyyBeanYewu3> mList = new ArrayList<>();
        mList.add(new BjyyBeanYewu3("id1", getResources().getString(R.string.tips1more1), "", "", "标题1", AppCommonUtils.TAG_f1, false));
        mList.add(new BjyyBeanYewu3("id2", getResources().getString(R.string.tips1more2), "", "", "标题2", AppCommonUtils.TAG_f2, false));
        mList.add(new BjyyBeanYewu3("id3", getResources().getString(R.string.tips1more1), "", "", "标题名字3", AppCommonUtils.TAG_f1, false));
        mList.add(new BjyyBeanYewu3("id4", getResources().getString(R.string.tips1more2), "", "", "标题4", AppCommonUtils.TAG_f2, false));
        mList.add(new BjyyBeanYewu3("id5", getResources().getString(R.string.tips1more1), "", "", "标题5", AppCommonUtils.TAG_f1, false));
        return mList;
    }

    public List<BjyyBeanYewu3> addList2() {
        List<BjyyBeanYewu3> mList = new ArrayList<>();
//        mList.add(new BjyyBeanYewu3("id1", "", "", "", "标题1", AppCommonUtils.TAG_f1, false));
//        mList.add(new BjyyBeanYewu3("id2", "", "", "", "标题2", AppCommonUtils.TAG_f1, false));
//        mList.add(new BjyyBeanYewu3("id3", "", "", "", "标题3", AppCommonUtils.TAG_f1, false));
//        mList.add(new BjyyBeanYewu3("id4", "", "", "", "标题4", AppCommonUtils.TAG_f1, false));
        mList.add(new BjyyBeanYewu3("id51", getResources().getString(R.string.tips1more2), "", "", "测试5", AppCommonUtils.TAG_f2, false));
        mList.add(new BjyyBeanYewu3("id61", getResources().getString(R.string.tips1more1), "", "", "测试6", AppCommonUtils.TAG_f1, false));
        mList.add(new BjyyBeanYewu3("id71", getResources().getString(R.string.tips1more2), "", "", "测试7", AppCommonUtils.TAG_f2, false));
        return mList;
    }

    public List<BjyyBeanYewu3> addList3() {
        List<BjyyBeanYewu3> mList = new ArrayList<>();
        mList.add(new BjyyBeanYewu3("id11", getResources().getString(R.string.tips1more1), "", "", "目录1", AppCommonUtils.TAG_f1, false));
        mList.add(new BjyyBeanYewu3("id21", getResources().getString(R.string.tips1more2), "", "", "目录2", AppCommonUtils.TAG_f2, false));
        mList.add(new BjyyBeanYewu3("id31", getResources().getString(R.string.tips1more1), "", "", "目录3", AppCommonUtils.TAG_f1, false));
        mList.add(new BjyyBeanYewu3("id41", getResources().getString(R.string.tips1more2), "", "", "目录4", AppCommonUtils.TAG_f2, false));
//        mList.add(new BjyyBeanYewu3("id5", "", "", "", "标题5", AppCommonUtils.TAG_f1, false));
//        mList.add(new BjyyBeanYewu3("id5", "", "", "", "标题6", AppCommonUtils.TAG_f1, false));
//        mList.add(new BjyyBeanYewu3("id5", "", "", "", "标题7", AppCommonUtils.TAG_f1, false));
        return mList;
    }

    public List<BjyyBeanYewu3> addList4() {
        List<BjyyBeanYewu3> mList = new ArrayList<>();
        mList.add(new BjyyBeanYewu3("id12", getResources().getString(R.string.tips1more1), "", "", "组件1", AppCommonUtils.TAG_f1, false));
        mList.add(new BjyyBeanYewu3("id22", getResources().getString(R.string.tips1more2), "", "", "组件2", AppCommonUtils.TAG_f2, false));
        mList.add(new BjyyBeanYewu3("id32", getResources().getString(R.string.tips1more1), "", "", "组件3", AppCommonUtils.TAG_f1, false));
        mList.add(new BjyyBeanYewu3("id42", getResources().getString(R.string.tips1more2), "", "", "组件名字4", AppCommonUtils.TAG_f2, false));
        mList.add(new BjyyBeanYewu3("id52", getResources().getString(R.string.tips1more1), "", "", "组件5", AppCommonUtils.TAG_f1, false));
        mList.add(new BjyyBeanYewu3("id52", getResources().getString(R.string.tips1more2), "", "", "组件6", AppCommonUtils.TAG_f2, false));
        mList.add(new BjyyBeanYewu3("id52", getResources().getString(R.string.tips1more1), "", "", "组件7", AppCommonUtils.TAG_f1, false));
        return mList;
    }

    private void findView() {
        findViewById(R.id.tv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donetwork(addList1());
            }
        });
        findViewById(R.id.tv2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donetwork(addList2());
            }
        });
        findViewById(R.id.tv3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donetwork(addList3());
            }
        });
        findViewById(R.id.tv4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donetwork(addList4());
            }
        });
    }

    private void onclick() {

    }

    private void donetwork(List<BjyyBeanYewu3> bjyyBeanYewu3s) {
        Fragment mFragment = null;
        Bundle args = new Bundle();
        args.putSerializable("BjyyBeanYewu3", (Serializable) bjyyBeanYewu3s);
        getSupportFragmentManager().beginTransaction().add(R.id.container_framelayout, mFragment = Demo3ActF3.getInstance(args), Demo3ActF3.class.getName()).show(mFragment).commit();
    }


}
