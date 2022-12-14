//package com.geek.appcommon.ytx;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.Menu;
//import android.widget.FrameLayout;
//
//import androidx.fragment.app.FragmentManager;
//
//import com.yuntongxun.plugin.common.YTXAppMgr;
//import com.yuntongxun.plugin.common.ui.YTXECSuperActivity;
//import com.yuntongxun.plugin.common.ui.tools.YTXSearchViewHelper;
//import com.yuntongxun.plugin.common.view.drawable.YTXWaterMarkUtils;
//import com.yuntongxun.plugin.rxcontacts.R;
//import com.yuntongxun.plugin.rxcontacts.search.OnlineSearchResultFragment;
//import com.yuntongxun.plugin.rxcontacts.search.SearchResultFragment;
//
//
//public class DTRXEmployeeSearchActivity extends YTXECSuperActivity {
//
//	private SearchResultFragment searchResultFragment;
//	private OnlineSearchResultFragment onlineSearchResultFragment;
//	private DTYTXSearchViewHelper mSearchViewHelper;
//	private FrameLayout root;
//	private String keyword;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		keyword = getIntent().getStringExtra("keyword");
//		initActionBar();
//		initEvent();
//	}
//
//	private void initActionBar() {
//		mSearchViewHelper = new DTYTXSearchViewHelper(false);
//		mSearchViewHelper.doExpandActionView(true);
//		mSearchViewHelper.mOnSearchViewListener = new DTYTXSearchViewHelper.OnSearchViewListener() {
//			@Override
//			public void startCollapseView() {
//				hideSoftKeyboard();
//				finish();
//			}
//
//			@Override
//			public void startExpandSearchView() {
//
//			}
//
//			@Override
//			public void onSearchTextChange(String keyword) {
//				if (!YTXAppMgr.isOuter()){
//					if (TextUtils.isEmpty(keyword)) {
////						getSupportFragmentManager().beginTransaction().hide(searchResultFragment).commit();
//					} else {
////						getSupportFragmentManager().beginTransaction().show(searchResultFragment).commit();
////                        searchResultFragment.doSearch(keyword);
//					}
//				}
//			}
//
//			@Override
//			public void onSearchClear() {
//				if (YTXAppMgr.isOuter()){
//					onlineSearchResultFragment.clear();
//				} else{
////					getSupportFragmentManager().beginTransaction().hide(searchResultFragment).commit();
//				}
//			}
//
//			@Override
//			public boolean startSearch(String keyword) {
//				if (YTXAppMgr.isOuter() && onlineSearchResultFragment != null) {
//					if (TextUtils.isEmpty(keyword)) {
//						onlineSearchResultFragment.clear();
//					} else{
//						hideSoftKeyboard();
//						onlineSearchResultFragment.doSearch(keyword);
//					}
//				}
//
//				if (!YTXAppMgr.isOuter()){
//					if (TextUtils.isEmpty(keyword)) {
//						getSupportFragmentManager().beginTransaction().hide(searchResultFragment).commit();
//					} else {
//						getSupportFragmentManager().beginTransaction().show(searchResultFragment).commit();
//						searchResultFragment.doSearch(keyword);
//					}
//				}
//
//				return true;
//			}
//		};
//		root=(FrameLayout)findViewById(R.id.ccp_root_view3);
//		if(root !=null){
//			root.setBackground(YTXWaterMarkUtils.getWaterMark());
//		}
//	}
//	private void initEvent() {
//		addNewFragment();
//	}
//
//	private void addNewFragment() {
//		FragmentManager fm = getSupportFragmentManager();
//		if (fm.findFragmentById(R.id.ccp_root_view3) == null) {
//			if (YTXAppMgr.isOuter()) {
//				onlineSearchResultFragment = OnlineSearchResultFragment.newInstance();
//				fm.beginTransaction().add(R.id.ccp_root_view3, onlineSearchResultFragment).commit();
//			} else{
//				searchResultFragment = SearchResultFragment.newInstance();
//				fm.beginTransaction().add(R.id.ccp_root_view3, searchResultFragment).hide(searchResultFragment).commit();
//			}
//		}
//	}
//
//	@Override
//	public int getLayoutId() {
//		return R.layout.activity_employee_search;
//	}
//
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		if(mSearchViewHelper != null) {
//			mSearchViewHelper.clearFocus();
//		}
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		if(mSearchViewHelper != null) {
//			mSearchViewHelper.onCreateOptionsMenu(this , menu);
//		}
//		mSearchViewHelper.setHint(getString(R.string.ytx_search_entrance));
//		mSearchViewHelper.mSearchImpl.setText(keyword);
////		mSearchViewHelper.clearFocus();
//
//		if (searchResultFragment != null) {
//			getSupportFragmentManager().beginTransaction().show(searchResultFragment).commit();
//			searchResultFragment.doSearch(keyword);
//		}
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	@Override
//	public boolean onPrepareOptionsMenu(Menu menu) {
//		if(mSearchViewHelper != null) {
//			mSearchViewHelper.onPrepareOptionsMenu(this , menu);
//		}
//		return super.onPrepareOptionsMenu(menu);
//	}
//}
