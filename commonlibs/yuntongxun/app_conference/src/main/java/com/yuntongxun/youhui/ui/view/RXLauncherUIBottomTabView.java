package com.yuntongxun.youhui.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuntongxun.plugin.common.common.helper.YTXAuthTagHelper;
import com.yuntongxun.plugin.common.common.utils.LogUtil;
import com.yuntongxun.plugin.common.common.utils.ResourceHelper;
import com.yuntongxun.youhui.R;

/**
 * 应用主界面底部导航控件
 *
 * @author 容联•云通讯
 */
public class RXLauncherUIBottomTabView extends RelativeLayout {

    private static final String TAG = "RongXin.RXLauncherUIBottomTabView";

    public static final int INDEX_CONF = 1;
    public static final int INDEX_CONTACTS = 2;
    public static final int INDEX_NEWS = 3;
    public static final int INDEX_SETTING = 4;

    private OnUITabViewClickListener rootView;
    private long mClickTime = 0L;
    private boolean mShowTimeLinedot = false;
    private boolean mShowSettingdot = false;
    private TabViewHolder mConfTabView;
    private TabViewHolder mNewsTabView;
    private TabViewHolder mContactTabView;
    private TabViewHolder mSettingTabView;

    private int mMainTabUnread;
    private int mTimeLineTabUnread;
    private int mContactTabUnread;
    private int mSettingTabUnread;

    private int mFocusColor = 0;
    private int mFocusColor_R;
    private int mFocusColor_G;
    private int mFocusColor_B;
    private int mNormalColor = 0;
    private int NormalColor_R;
    private int NormalColor_G;
    private int NormalColor_B;
    private int mColor_R;
    private int mColor_G;
    private int mColor_B;
    private int mCurrentSlideIndex = -1;
    protected int mToSlideIndex = 0;


    /**
     * 初始化方法
     * @param context 上下文
     */
    public RXLauncherUIBottomTabView(Context context) {
        super(context);
        init();
    }

    /**
     * 初始化方法
     */
    public RXLauncherUIBottomTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化方法
     */
    public RXLauncherUIBottomTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 内部处理tab 单击事件
     */
    private OnClickListener mTabViewOnClickListener =
            new OnClickListener() {
                private final long DOUBLE_CLICK = 300L;
                @Override
                public void onClick(View v) {
                    int index = (Integer) v.getTag();
                    if(mCurrentSlideIndex == index && index == 0 && System.currentTimeMillis() - mClickTime <= DOUBLE_CLICK) {
                        LogUtil.v(TAG, "onMainTabDoubleClick");
                        mHandler.removeMessages(0);
                        mClickTime = System.currentTimeMillis();
                        mCurrentSlideIndex = index;
                    } else {
                        if(rootView != null) {
                            if(index != 0 || mCurrentSlideIndex != 0) {
                                LogUtil.v(TAG, "directly dispatch tab click event");
                                mClickTime = System.currentTimeMillis();
                                mCurrentSlideIndex = index;
                                dispatchTabClick(index);
                                return;
                            }

                            LogUtil.v(TAG, "do double click check");
                            mHandler.sendEmptyMessageDelayed(0, DOUBLE_CLICK);
                        }

                        mClickTime = System.currentTimeMillis();
                        mCurrentSlideIndex = index;
                        LogUtil.w(TAG, "on tab click, index " + v.getTag() + ", but listener is null");
                    }

                }
            };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            LogUtil.v(TAG, "onMainTabClick");
            dispatchTabClick(INDEX_CONF);
        }
    };

    /**
     * 转发tab被单击事件处理
     * @param index 被单击的tab
     */
    private void dispatchTabClick(Integer index) {
        LogUtil.v(TAG, "dispatchTabClick index " + index);
        if(rootView != null) {
            rootView.onTabClick(index - 1);
        }

    }


    public void setOnUITabViewClickListener(OnUITabViewClickListener listener) {
        this.rootView = listener;
    }

    /**
     * 创建一个子TAB
     * @param index 标识
     * @param parent 添加到父控件
     * @return TAB
     */
    private TabViewHolder createTabView(int index , ViewGroup parent) {
        TabViewHolder tabView = new TabViewHolder();
        tabView.rootView = LayoutInflater.from(getContext()).inflate(R.layout.ytx_single_tab, parent, false);
        tabView.mTabIconView = (RXTabIconView) tabView.rootView.findViewById(R.id.tab_view_iv);
        tabView.mTabCountTips = (TextView) tabView.rootView.findViewById(R.id.unread_count);
        tabView.mTabDesc = (TextView) tabView.rootView.findViewById(R.id.tab_text_tv);
        tabView.mTabCountTips.setBackgroundResource(R.drawable.ytx_unread_bg);
        tabView.mTabUnreadTips = (ImageView) tabView.rootView.findViewById(R.id.unread_dot);
        tabView.rootView.setTag(index);
        tabView.rootView.setOnClickListener(this.mTabViewOnClickListener);
        return tabView;
    }

    /**
     * 初始化
     */
    private void init(){
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(linearLayout ,  new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT  ,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

        TabViewHolder tabView = createTabView(INDEX_CONF, linearLayout);
        tabView.mTabDesc.setText("会议");
        tabView.mTabDesc.setTextColor(this.getResources().getColor(R.color.darkGrey));
        tabView.mTabIconView.init(R.drawable.yh_icon_conf , R.drawable.yh_icon_conf_on , R.drawable.yh_icon_conf_on);
        tabView.mTabCountTips.setVisibility(View.GONE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,
                ResourceHelper.getDimensionPixelSize(this.getContext(), R.dimen.ytx_bottom_tab_bar_height));
        layoutParams.weight = 1.0F;
        linearLayout.addView(tabView.rootView, layoutParams);
        mConfTabView = tabView;

        tabView = createTabView(INDEX_CONTACTS, linearLayout);
        tabView.mTabDesc.setText("通讯录");
        tabView.mTabDesc.setTextColor(this.getResources().getColor(R.color.darkGrey));
        tabView.mTabIconView.init(R.drawable.yh_icon_contact , R.drawable.yh_icon_contact_on , R.drawable.yh_icon_contact_on);
        tabView.mTabCountTips.setVisibility(View.GONE);
        layoutParams = new LinearLayout.LayoutParams(0,
                ResourceHelper.getDimensionPixelSize(this.getContext(), R.dimen.ytx_bottom_tab_bar_height));
        layoutParams.weight = 1.0F;
        linearLayout.addView(tabView.rootView, layoutParams);
        mContactTabView = tabView;

        tabView = createTabView(INDEX_NEWS, linearLayout);
        tabView.mTabDesc.setText("消息");
        tabView.mTabDesc.setTextColor(this.getResources().getColor(R.color.darkGrey));
        tabView.mTabIconView.init(R.drawable.yh_icon_news, R.drawable.yh_icon_new_on , R.drawable.yh_icon_new_on);
        tabView.mTabCountTips.setVisibility(View.GONE);
        layoutParams = new LinearLayout.LayoutParams(0,
                ResourceHelper.getDimensionPixelSize(this.getContext(), R.dimen.ytx_bottom_tab_bar_height));
        layoutParams.weight = 1.0F;
        linearLayout.addView(tabView.rootView, layoutParams);
        mNewsTabView = tabView;

        tabView = createTabView(INDEX_SETTING, linearLayout);
        tabView.mTabDesc.setText("个人中心");
        tabView.mTabDesc.setTextColor(this.getResources().getColor(R.color.darkGrey));
        tabView.mTabIconView.init(R.drawable.yh_icon_mine , R.drawable.yh_icon_mine_on, R.drawable.yh_icon_mine_on);

        tabView.mTabCountTips.setVisibility(View.GONE);
        layoutParams = new LinearLayout.LayoutParams(0, ResourceHelper.getDimensionPixelSize(this.getContext(), R.dimen.ytx_bottom_tab_bar_height));
        layoutParams.weight = 1.0F;
        linearLayout.addView(tabView.rootView, layoutParams);
        mSettingTabView = tabView;

        this.mFocusColor = this.getResources().getColor(R.color.yhc_conf_main_color);
        this.mFocusColor_R = (this.mFocusColor & 16711680) >> 16;
        this.mFocusColor_G = (this.mFocusColor & '\uff00') >> 8;
        this.mFocusColor_B = this.mFocusColor & 0xFF;
        this.mNormalColor = this.getResources().getColor(R.color.darkGrey);
       // this.mNormalColor = Color.parseColor("#edf1f3");
        this.NormalColor_R = (this.mNormalColor & 16711680) >> 16;
        this.NormalColor_G = (this.mNormalColor & '\uff00') >> 8;
        this.NormalColor_B = this.mNormalColor & 0xFF;
        this.mColor_R = this.mFocusColor_R - this.NormalColor_R;
        this.mColor_G = this.mFocusColor_G - this.NormalColor_G;
        this.mColor_B = this.mFocusColor_B - this.NormalColor_B;
    }

    /**
     * 获取聊天页面tab未读消息数
     * @return 未读消息数量
     */
    public final int getMainTabUnread() {
        return this.mMainTabUnread;
    }

    /**
     * 获取当前滑动目标的index
     * @return 滑动目标的index
     */
    public final int getToSlideIndex() {
        return this.mToSlideIndex;
    }

    /**
     * 处理当前页面滑动事件
     * @param position 开始位置
     * @param offset 偏移量
     */
    public final void onTabScrolled(int position, float offset) {
        if(Math.abs(mToSlideIndex - position) == 1) {

        }
        LogUtil.d(TAG , "onTabScrolled position " + position + " , offset " + offset);
        int toAlpha = (int)(255.0F * offset);
        int fromAlpha = 255 - toAlpha;
        int toColor = ((int)((float)this.mColor_R * offset + (float)this.NormalColor_R) << 16) + ((int)((float)this.mColor_G * offset + (float)this.NormalColor_G) << 8) + (int)((float)this.mColor_B * offset + (float)this.NormalColor_B) - 16777216;
        int fromColor = ((int)((float)this.mColor_R * (1.0F - offset) + (float)this.NormalColor_R) << 16) + ((int)((float)this.mColor_G * (1.0F - offset) + (float)this.NormalColor_G) << 8) + (int)((float)this.mColor_B * (1.0F - offset) + (float)this.NormalColor_B) - 16777216;
        switch(position) {
            case 0:
                // 从沟通页面滑向同事圈页面
                this.mConfTabView.mTabIconView.setIconAlpha(fromAlpha);
                this.mNewsTabView.mTabIconView.setIconAlpha(toAlpha);
                this.mConfTabView.mTabDesc.setTextColor(fromColor);
                this.mNewsTabView.mTabDesc.setTextColor(toColor);
                break;
            case 1:
                // 从同事圈页面滑向联系人页面
                this.mNewsTabView.mTabIconView.setIconAlpha(fromAlpha);
                this.mContactTabView.mTabIconView.setIconAlpha(toAlpha);
                this.mNewsTabView.mTabDesc.setTextColor(fromColor);
                this.mContactTabView.mTabDesc.setTextColor(toColor);
                break;
//            case 2:
//                // 从工作页面滑向联系人页面
//                this.mSettingTabView.mTabIconView.setIconAlpha(fromAlpha);
//                this.mContactTabView.mTabIconView.setIconAlpha(toAlpha);
//                this.mWorkTabView.mTabDesc.setTextColor(fromColor);
//                mContactTabView.mTabDesc.setTextColor(toColor);
//                break;
            case 2:
                // 从联系人面滑向设置页面
                mContactTabView.mTabIconView.setIconAlpha(fromAlpha);
                mSettingTabView.mTabIconView.setIconAlpha(toAlpha);
                mContactTabView.mTabDesc.setTextColor(fromColor);
                mSettingTabView.mTabDesc.setTextColor(toColor);
        }

    }

    /**
     * 同事圈是否显示小红点
     * @param showDot 是否显示
     */
    public final void showTimeLineUnreadDot(boolean showDot) {
        mShowTimeLinedot = showDot;
        mNewsTabView.mTabCountTips.setVisibility(View.INVISIBLE);
        ImageView dotView = mNewsTabView.mTabUnreadTips;
        dotView.setVisibility(showDot && YTXAuthTagHelper.getInstance().isSupportCircle() ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 设置是否显示小红点
     * @param showDot 是否显示
     */
    public final void showSettingUnreadDot(boolean showDot) {
        mShowSettingdot = showDot;
        mSettingTabView.mTabCountTips.setVisibility(View.INVISIBLE);
        ImageView dotView = mSettingTabView.mTabUnreadTips;
        dotView.setVisibility(showDot ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 处理滑动结束tab事件
     * @param index 当前滑动到的位置
     */
    public final void onTabSelect(int index) {
        LogUtil.d(TAG , "onTabSelect index " + index);
        mToSlideIndex = index;
        switch(index) {
            case 0:
                // 沟通页面
                mConfTabView.mTabIconView.setIconAlpha(255);
                mContactTabView.mTabIconView.setIconAlpha(0);
                mNewsTabView.mTabIconView.setIconAlpha(0);
                mSettingTabView.mTabIconView.setIconAlpha(0);

                mConfTabView.mTabDesc.setTextColor(mFocusColor);
                mContactTabView.mTabDesc.setTextColor(mNormalColor);
                mNewsTabView.mTabDesc.setTextColor(mNormalColor);
                mSettingTabView.mTabDesc.setTextColor(mNormalColor);
                break;
            case 1:
                // 联系人页面
                mConfTabView.mTabIconView.setIconAlpha(0);
                mContactTabView.mTabIconView.setIconAlpha(255);
                mNewsTabView.mTabIconView.setIconAlpha(0);
                mSettingTabView.mTabIconView.setIconAlpha(0);

                mConfTabView.mTabDesc.setTextColor(mNormalColor);
                mContactTabView.mTabDesc.setTextColor(mFocusColor);
                mNewsTabView.mTabDesc.setTextColor(mNormalColor);
                mSettingTabView.mTabDesc.setTextColor(mNormalColor);
                break;
            case 2:
                // 消息页面
                mConfTabView.mTabIconView.setIconAlpha(0);
                mContactTabView.mTabIconView.setIconAlpha(0);
                mNewsTabView.mTabIconView.setIconAlpha(255);
                mSettingTabView.mTabIconView.setIconAlpha(0);

                mConfTabView.mTabDesc.setTextColor(mNormalColor);
                mContactTabView.mTabDesc.setTextColor(mNormalColor);
                mNewsTabView.mTabDesc.setTextColor(mFocusColor);
                mSettingTabView.mTabDesc.setTextColor(mNormalColor);
                break;
            case 3:
                // 设置页面
                mConfTabView.mTabIconView.setIconAlpha(0);
                mContactTabView.mTabIconView.setIconAlpha(0);
                mNewsTabView.mTabIconView.setIconAlpha(0);
                mSettingTabView.mTabIconView.setIconAlpha(255);

                mConfTabView.mTabDesc.setTextColor(mNormalColor);
                mContactTabView.mTabDesc.setTextColor(mNormalColor);
                mNewsTabView.mTabDesc.setTextColor(mNormalColor);
                mSettingTabView.mTabDesc.setTextColor(mFocusColor);
        }

        mClickTime = System.currentTimeMillis();
        mCurrentSlideIndex = mToSlideIndex;
    }


    /**
     * 更新沟通页面未读消息数量
     * @param count 未读消息数量
     */
    public final void updateMainTabUnread(int count) {
        LogUtil.d(TAG, "updateMainTabUnread " + count );
        mMainTabUnread = count;
        if(!mNewsTabView.isReady()) {
            return ;
        }
        if(count > 0) {
            if(count > 99) {
                mNewsTabView.mTabCountTips.setText(getContext().getString(R.string.ytx_unread_count_overt_100));
                mNewsTabView.mTabCountTips.setVisibility(View.VISIBLE);
                mNewsTabView.mTabUnreadTips.setVisibility(View.INVISIBLE);
            } else {
                mNewsTabView.mTabCountTips.setText(String.valueOf(count));
                mNewsTabView.mTabCountTips.setVisibility(View.VISIBLE);
                mNewsTabView.mTabUnreadTips.setVisibility(View.INVISIBLE);
            }
        } else {
            mNewsTabView.mTabCountTips.setText("");
            mNewsTabView.mTabCountTips.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 更新消息页面未读消息数量
     * @param count 未读消息数量
     */
    public final void updateMeetingNewsTabUnread(int count) {
        mTimeLineTabUnread = count;
        LogUtil.d(TAG, "updateTimeLineTabUnread " + mTimeLineTabUnread );

        if(!mNewsTabView.isReady()) {
            return ;
        }
        if(count > 0) {
            if(count > 99) {
                mNewsTabView.mTabCountTips.setText(getContext().getString(R.string.ytx_unread_count_overt_100));
                mNewsTabView.mTabCountTips.setVisibility(View.VISIBLE);
                mNewsTabView.mTabUnreadTips.setVisibility(View.INVISIBLE);
            } else {
                mNewsTabView.mTabCountTips.setText(String.valueOf(count));
                mNewsTabView.mTabCountTips.setVisibility(View.VISIBLE);
                mNewsTabView.mTabUnreadTips.setVisibility(View.INVISIBLE);
            }
        } else {
            mNewsTabView.mTabCountTips.setText("");
            mNewsTabView.mTabCountTips.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 更新联系人页面未读消息数量
     * @param count 未读消息数量
     */
    public final void updateContactTabUnread(int count) {
        mContactTabUnread = count;
        LogUtil.d(TAG, "updateContactTabUnread " + mContactTabUnread );
        if(!mContactTabView.isReady()) {
            return ;
        }
        if(count > 0) {
            if(count > 99) {
                mContactTabView.mTabCountTips.setText(getContext().getString(R.string.ytx_unread_count_overt_100));
                mContactTabView.mTabCountTips.setVisibility(View.VISIBLE);
                mContactTabView.mTabUnreadTips.setVisibility(View.INVISIBLE);
            } else {
                mContactTabView.mTabCountTips.setText(String.valueOf(count));
                mContactTabView.mTabCountTips.setVisibility(View.VISIBLE);
                mContactTabView.mTabUnreadTips.setVisibility(View.INVISIBLE);
            }
        } else {
            mContactTabView.mTabCountTips.setText("");
            mContactTabView.mTabCountTips.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 更新设置页面未读消息数量
     * @param count 未读消息数量
     */
    public final void updateSettingTabUnread(int count) {
        mSettingTabUnread = count;
        LogUtil.d(TAG, "updateSettingTabUnread " + mSettingTabUnread );
        if(!mSettingTabView.isReady()) {
           return ;
        }
        if(count > 0) {
            if(count > 99) {
                mSettingTabView.mTabCountTips.setText(getContext().getString(R.string.ytx_unread_count_overt_100));
                mSettingTabView.mTabCountTips.setVisibility(View.VISIBLE);
                mSettingTabView.mTabUnreadTips.setVisibility(View.INVISIBLE);
            } else {
                mSettingTabView.mTabCountTips.setText(String.valueOf(count));
                mSettingTabView.mTabCountTips.setVisibility(View.VISIBLE);
                mSettingTabView.mTabUnreadTips.setVisibility(View.INVISIBLE);
            }
        } else {
            mSettingTabView.mTabCountTips.setText("");
            mSettingTabView.mTabCountTips.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * Tab对象
     */
    class TabViewHolder {
        /**tab页面布局*/
        View rootView;
        /**tab页面图标*/
        RXTabIconView mTabIconView;
        /**tab页面描述*/
        TextView mTabDesc;
        /**页面未读数量*/
        TextView mTabCountTips;
        /**未读红点*/
        ImageView mTabUnreadTips;

        /**
         * 当前tab是否初始化完成
         * @return 是否初始化完成
         */
        public boolean isReady() {
            return rootView != null ;
        }
    }


    /**
     * tab单击回调
     */
    public interface OnUITabViewClickListener {

        /**
         * 在tab被单击的时候触发
         * @param tabIndex 被单击的tab index
         */
        void onTabClick(int tabIndex);

//        /**
//         * 工作tab被单击触发回调接口
//         */
//        void onWorkTabClick();
    }
}
