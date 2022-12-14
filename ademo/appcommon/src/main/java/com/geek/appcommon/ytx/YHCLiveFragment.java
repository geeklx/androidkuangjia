//package com.geek.appcommon.ytx;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//
//import androidx.annotation.Nullable;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.blankj.utilcode.util.AppUtils;
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.geek.appcommon.SlbBase;
//import com.geek.biz1.bean.BjyyBeanYewu3;
//import com.geek.biz1.presenter.home.ClassificationByPagePresenter;
//import com.geek.common.R;
//import com.geek.libbase.base.SlbBaseLazyFragmentNew;
//import com.geek.libbase.baserecycleview.SlbBaseFragmentViewPageYewuList;
//import com.geek.libbase.widgets.XRecyclerView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class YHCLiveFragment extends SlbBaseLazyFragmentNew {
//
////    private ClassificationByPagePresenter mPresenter;
//
//    private YHCLiveAdapter mAdapter;
//    private XRecyclerView recyclerView1;
//
//    public static YHCLiveFragment getInstance(BjyyBeanYewu3 bean) {
//        YHCLiveFragment recommandFragment = new YHCLiveFragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("feileiBean", bean);
//        recommandFragment.setArguments(bundle);
//        return recommandFragment;
//    }
//
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.fragmentyewulist1_common2;
//    }
//
//    @Override
//    protected void setup(View rootView, @Nullable Bundle savedInstanceState) {
//        super.setup(rootView, savedInstanceState);
//        recyclerView1 = rootView.findViewById(R.id.baserecycleview_recycler_view1);
//        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
//        List<String> datas =  new ArrayList<>();
//        datas.add("主任选举任职大会");
//        datas.add("第三次村民动员大会");
//        datas.add("网格员调度会");
//        datas.add("重大事项决议会");
//        datas.add("重大事项决议会222");
//
//        mAdapter = new YHCLiveAdapter();
//        mAdapter.setNewData(datas);
//        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(AppUtils.getAppPackageName() + ".hs.act.slbapp.YHCLiveDetailActivity");
//                startActivity(intent);
//            }
//        });
//        recyclerView1.setAdapter(mAdapter);
//    }
////
////    @Override
////    protected void donetworkAdd() {
////        emptyview1.loading();
////        // 业务bufen
////        init_adapter(mAdapter);
////    }
////
////    @Override
////    protected void retryDataAdd() {
////        refreshData();
////    }
////
////    private void refreshData() {
//////        if (mPresenter != null) {
//////            mPresenter.getClassificationDataByPage(mNextRequestPage, PAGE_SIZE, "1003", "1");
//////        }
////
////    }
//
////    @Override
////    protected void onclickAdd() {
////        if (mAdapter != null) {
////            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
////                @Override
////                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//////                    List<ZXConvertData> data = mAdapter.getData();
//////                    if (data != null && data.size() > 0) {
//////                        String url = data.get(position).getData().getUrl();
//////                        Log.e("aaaaaa", url);
//////                        Intent intent = new Intent(AppUtils.getAppPackageName() + ".hs.act.slbapp.WeChatH5JsWebActivity");
//////                        if (url != null && "" != url && "null" != url) {
//////                            intent.putExtra(AgentWebFragment.URL_KEY, url);
//////                            startActivity(intent);
//////                        }
//////
//////                    }
////
////                    //startActivity(Intent(requireActivity(), DKMainActivity::class.java));
//////            HiosHelperNew.resolveAd(
//////                requireActivity(), requireActivity(),
//////                "dataability://${AppUtils.getAppPackageName()}.hs.act.slbapp.TikTokListActivityDK{act}"
//////            )
////
////                }
////            });
////        }
////
////    }
//
////    @Override
////    protected void findviewAdd() {
//////        mPresenter = new ClassificationByPagePresenter();
//////        mPresenter.onCreate(this);
////        recyclerView1.setLayoutManager(new GridLayoutManager(getActivity(), 1, RecyclerView.VERTICAL, false));
////        List<String> datas =  new ArrayList<>();
////        datas.add("主任选举任职大会");
////        datas.add("第三次村民动员大会");
////        datas.add("网格员调度会");
////        datas.add("重大事项决议会");
////
////        mAdapter = new YHCLiveAdapter();
////        recyclerView1.setAdapter(mAdapter);
////
////        OnOrderSuccess(datas);
////
////        emptyview1.notices("暂无数据", "网络出了点问题", "正在加载…", "");
////    }
//
////    @Override
////    protected void emptyviewAdd() {
////        emptyview1.loading();
////        // 业务bufen
////        init_adapter(mAdapter);
////    }
////
////    @Override
////    protected void refreshLayoutAdd() {
////        // 业务bufen
////        init_adapter(mAdapter);
////    }
////
////    @Override
////    public void onDestroy() {
////        super.onDestroy();
//////        if (mPresenter != null) {
//////            mPresenter.onDestory();
//////        }
////    }
//
//
////    @Override
////    public void onClassficationDataSuccess(ClassificationListByPageBean bean) {
////        List<ZXConvertData> list = new ArrayList<ZXConvertData>();
////        if (bean != null) {
////            List<ClassificationBean> datas = bean.getData();
////            if (datas != null) {
////                for (int i = 0; i < datas.size(); i++) {
////                    ClassificationBean classificationBean = datas.get(i);
////                    list.add(new ZXConvertData(classificationBean));
////                }
////            }
////        }
////        OnOrderSuccess(list);
////    }
////
////    @Override
////    public void onClassficationDataNoData(String msg) {
////        OnOrderNodata();
////    }
////
////    @Override
////    public void onClassficationDataFail(String msg) {
////        OnOrderFail();
////    }
//}
