package com.geek.appcommon.demo.demo3.fragments3;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.geek.biz1.bean.BjyyBeanYewu3;
import com.geek.common.R;
import com.geek.libbase.base.SlbBaseLazyFragmentNew;
import com.geek.libbase.baserecycleview.SlbBaseActivityViewPageTabBean1;
import com.geek.libbase.baserecycleview.yewu1.YewuList1CommonAdapter;
import com.geek.libbase.emptyview.EmptyViewNewNew;
import com.geek.libbase.widgets.XRecyclerView;
import com.geek.libutils.app.MyLogUtil;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class Demo3ActF3F2 extends SlbBaseLazyFragmentNew {

    private String tablayoutId;
    private TextView tv1;
    private BjyyBeanYewu3 bjyyBeanYewu3;
    //
    protected EmptyViewNewNew emptyview;
    protected SmartRefreshLayout refreshLayout1;
    protected ClassicsHeader smartHeader1;
    protected ClassicsFooter smartFooter1;
    protected NestedScrollView nestedScrollView;
    protected XRecyclerView recyclerView1;
    protected List<SlbBaseActivityViewPageTabBean1> mList1;
    private YewuList1CommonAdapter mAdapter1;


    public static Demo3ActF3F2 getInstance(BjyyBeanYewu3 bean) {
        Demo3ActF3F2 fragment = new Demo3ActF3F2();
        Bundle bundle = new Bundle();
        bundle.putSerializable("BjyyBeanYewu3", bean);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Demo3ActF3F2 getInstance(Bundle bundle) {
        Demo3ActF3F2 mEasyWebFragment = new Demo3ActF3F2();
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
            bjyyBeanYewu3 = (BjyyBeanYewu3) getArguments().getSerializable("BjyyBeanYewu3");
        }

    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        // 公参bufen
        emptyview = rootView.findViewById(R.id.emptyview1);
        refreshLayout1 = rootView.findViewById(R.id.baserecycleview_refreshLayout1);
        smartHeader1 = rootView.findViewById(R.id.baserecycleview_smart_header1);
        smartFooter1 = rootView.findViewById(R.id.baserecycleview_smart_footer1);
        nestedScrollView = rootView.findViewById(R.id.nestedScrollView1);
        // 非公参bufen
        recyclerView1 = rootView.findViewById(R.id.baserecycleview_recycler_view1);
        tv1 = rootView.findViewById(R.id.tv1);
        // 初始化数据bufen
        tv1.setText("");
        emptyview.loading();
    }

    @Override
    protected void initData() {
        super.initData();
        // 接口bufen
//        emptyview.loading();
        retryData();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        emptyview.bind(refreshLayout1).setRetryListener(new EmptyViewNewNew.RetryListener() {
            @Override
            public void retry() {
                // 刷新
                emptyview.loading();
                retryData();
            }
        });
        refreshLayout1.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshLayout) {
                // 刷新
                ToastUtils.showLong("刷新当前页面数据");
                retryData();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_demo3_f1_f2;
    }

    @Override
    protected void setup(final View rootView, @Nullable Bundle savedInstanceState) {
        super.setup(rootView, savedInstanceState);

    }

    private void retryData() {
        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyview.success();
                refreshLayout1.finishRefresh(0);
                //
                emptyview.notices("暂无订单，去选课中心看看吧…", "没有网络了,检查一下吧", "正在加载....", "");
                recyclerView1.setLayoutManager(new GridLayoutManager(getActivity(), 1, RecyclerView.VERTICAL, false));
                mList1 = new ArrayList<>();
                mAdapter1 = new YewuList1CommonAdapter();
                recyclerView1.setAdapter(mAdapter1);
                tv1.setText(bjyyBeanYewu3.getImg());

            }
        }, 3000);

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
