//package com.geek.appcommon.ytx.im;
//
//import android.os.Bundle;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.blankj.utilcode.util.ToastUtils;
//import com.geek.common.R;
//import com.yuntongxun.im.tools.PlusSubMenuHelper;
//import com.yuntongxun.plugin.common.presentercore.presenter.YTXBasePresenter;
//import com.yuntongxun.plugin.common.ui.CCPFragment;
//import com.yuntongxun.plugin.common.ui.YTXBaseFragment;
//import com.yuntongxun.plugin.common.ui.setting.YTXCustomSettingUtils;
//import com.yuntongxun.plugin.im.common.YTXCustomMsgItem;
//import com.yuntongxun.plugin.im.manager.YTXIMPluginManager;
//
///**
// * @创建人 JiangJun
// * @创建时间 2021/12/15 20:06
// * @描述
// */
//public class RxContactHomeFragment extends CCPFragment {
//
//
//    private Fragment mFragment;
//    private RelativeLayout mLayout;
//    private TextView mTitle;
//    private ImageView mIconLeft;
//    private ImageView mIconRight;
//
//    /**
//     * 应用功能下拉菜单
//     */
//    private PlusSubMenuHelper mPlusSubMenuHelper;
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.rx_fragment_contact_home;
//    }
//
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        mFragment = (YTXBaseFragment) Fragment.instantiate(getContext(), YTXConversationListFragment.class.getName(), null);
//
//        getChildFragmentManager().beginTransaction().add(R.id.convert_frame, mFragment).commit();
//
//        mLayout = (RelativeLayout) findViewById(R.id.rx_layout_bar);
//        mTitle = (TextView) findViewById(R.id.rx_im_title);
//        mIconRight = (ImageView) findViewById(R.id.rx_im_icon_right);
//        mIconLeft = (ImageView) findViewById(R.id.rx_im_icon_left);
//
//
//        //获取设置的图片
//        int topLeftResId = YTXCustomSettingUtils.getInstance().getChattingListTopLeftDrawableResId();
//        int topRightResId = YTXCustomSettingUtils.getInstance().getChattingListTopRightDrawableResId();
//        if (topLeftResId != 0) {
//            mIconLeft.setImageResource(topLeftResId);
//        }
//        if (topRightResId != 0) {
//            mIconRight.setImageResource(topRightResId);
//        }
//
//        //获取回调
//        YTXCustomSettingUtils.OnChattingTopLeftClickListener topLeftClickListener = YTXCustomSettingUtils.getInstance().getOnChattingTopLeftClickListener();
//        YTXCustomSettingUtils.OnChattingTopRightClickListener rightClickListener = YTXCustomSettingUtils.getInstance().getOnChattingTopRightClickListener();
//
//        mIconRight.setOnClickListener(v -> {
//            if (rightClickListener != null) {
//                rightClickListener.onChattingTopRightClick();
//            }
//        });
//        mIconLeft.setOnClickListener(v -> {
//            if (topLeftClickListener != null) {
//                topLeftClickListener.onChattingTopLeftClick();
//            }
//        });
//    }
//
//
//    /**
//     * 控制下拉菜单显示和隐藏
//     */
//    private void controlPlusSubMenu() {
//        if (mPlusSubMenuHelper == null) {
//            mPlusSubMenuHelper = new PlusSubMenuHelper(getActivity());
////            return;
//        }
//        if (mPlusSubMenuHelper.isShowing()) {
//            mPlusSubMenuHelper.dismiss();
//            return;
//        }
//        mPlusSubMenuHelper.setOnDismissListener(null);
//        mPlusSubMenuHelper.tryShow();
//    }
//
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (mPlusSubMenuHelper != null && mPlusSubMenuHelper.isShowing()) {
//            mPlusSubMenuHelper.dismiss();
//        }
//    }
//
//    @Override
//    public YTXBasePresenter getPresenter() {
//        return null;
//    }
//
//
//    public Fragment getFragment() {
//        return mFragment;
//    }
//
//    public RelativeLayout getRelativeLayout() {
//        return mLayout;
//    }
//
//    public TextView getTitleView() {
//        return mTitle;
//    }
//
//    public ImageView getImageView() {
//        return mIconRight;
//    }
//
//
//    public void setTitle(String title) {
//        if (mTitle != null) {
//            mTitle.setText(title);
//        }
//    }
//
//
//    public void setIcon(int icon) {
//        if (mIconRight != null) {
//            mIconRight.setBackgroundResource(icon);
//        }
//    }
//}
