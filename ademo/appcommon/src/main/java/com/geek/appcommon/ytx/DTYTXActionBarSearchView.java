package com.geek.appcommon.ytx;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.geek.common.R;
import com.yuntongxun.plugin.common.common.YTXBackwardSupportUtil;
import com.yuntongxun.plugin.common.common.utils.LogUtil;
import com.yuntongxun.plugin.common.ui.tools.YTXISearch;
import com.yuntongxun.plugin.common.ui.widget.YTXAutoMatchKeywordEditText;

/**
 * @author 容联•云通讯
 * @since 2017/4/15
 */
public class DTYTXActionBarSearchView extends LinearLayout implements YTXISearch {

    private static final String TAG = "RongXin.ActionBarSearchView";

    private View mActionBackLayout;
    private ActionBarEditText mActionBarEditText;
    private ImageButton mClearView;
    private OnFocusChangeListener mOnFocusChangeListener;
    /**搜索状态栏回调接口*/
    private OnActionBarSearchListener mOnActionBarSearchListener;
    /**输入框控件返回回调*/
    private OnActionBarNavigationListener mNavigationListener;

    private final TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            initDelView();
            if(mOnActionBarSearchListener != null) {
                mOnActionBarSearchListener.onTextChanged(s == null ? "" : s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 焦点变化回调接口
     */
    private final OnFocusChangeListener mInnerOnFocusChangeListener
            = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            LogUtil.d(TAG ,  "on edittext focus changed, hasFocus " + hasFocus);
            if(mOnFocusChangeListener != null) {
                mOnFocusChangeListener.onFocusChange(v, hasFocus);
            }
        }
    };

    /**
     *
     */
    private final OnClickListener mOnInnerClickListener =
            new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTextNil(true);
                    if(mOnActionBarSearchListener != null) {
                        mOnActionBarSearchListener.onSearchClear();
                    }
                }
            };


    /**
     * 搜索输入框状态栏返回
     */
    private final OnClickListener mOnBackListener
            = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mNavigationListener != null) {
                mNavigationListener.onNavigation();
            }
        }
    };

    public DTYTXActionBarSearchView(Context context) {
        super(context);
        init();
    }

    public DTYTXActionBarSearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化删除按钮
     */
    private void initDelView() {
        if(mActionBarEditText.getEditableText() != null && !YTXBackwardSupportUtil.isNullOrNil(mActionBarEditText.getEditableText().toString())) {
            setDelImageResource(R.drawable.ytx_ic_clear_24, getResources().getDimensionPixelSize(R.dimen.LargestPadding));
        } else {
            setDelImageResource(0, 0);
        }

    }

    /**
     * 设置清空按钮资源
     * @param resId 资源
     * @param width 大小
     */
    private void setDelImageResource(int resId, int width) {
        mClearView.setImageResource(resId);
        mClearView.setBackgroundResource(0);
        mClearView.setContentDescription("了解更多");
        ViewGroup.LayoutParams params = mClearView.getLayoutParams();
        params.width = width;
        mClearView.setLayoutParams(params);
    }

    /**
     * 初始化
     */
    private void init() {
        ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.ytx_layout_actionbar_search_view1, this, true);
        mActionBackLayout = findViewById(R.id.ytx_actionbar_back_ll);
        mActionBackLayout.setOnClickListener(mOnBackListener);
        mActionBarEditText = (ActionBarEditText) findViewById(R.id.ytx_search_view);
        mActionBarEditText.mSearchView = this;
        mActionBarEditText.post(new Runnable() {
            @Override
            public void run() {
               DTYTXActionBarSearchView.this.mActionBarEditText.setText("");
            }
        });
        mClearView = (ImageButton) findViewById(R.id.ytx_del);
        mActionBarEditText.addTextChangedListener(mTextWatcher);
        mActionBarEditText.setOnFocusChangeListener(mInnerOnFocusChangeListener);
        mClearView.setOnClickListener(mOnInnerClickListener);
        initDelView();
    }

    /**
     * 设置输入框文本
     * @param text 输入框文本
     */
    @Override
    public void setText(String text) {
        text = YTXBackwardSupportUtil.nullAsNil(text);
        mActionBarEditText.setText(text);
        mActionBarEditText.setSelection(text.length());
    }

    @Override
    public void setOnActionBarSearchListener(OnActionBarSearchListener l) {
        mOnActionBarSearchListener = l;
    }

    @Override
    public void setOnActionBarNavigationListener(OnActionBarNavigationListener l) {
        mNavigationListener = l;
    }

    /**
     * 清空输入框焦点
     */
    @Override
    public void clearInputFocus() {
        mActionBarEditText.clearFocus();
    }

    /**
     * 输入框是否获取焦点
     * @return 是否获取焦点
     */
    @Override
    public boolean hasInputFocus() {
        return (mActionBarEditText != null) && mActionBarEditText.hasFocus();

    }

    /**
     * 获取焦点
     * @return 是否成功
     */
    @Override
    public boolean requestInputFocus() {
        return (mActionBarEditText != null) && mActionBarEditText.requestFocus();
    }

    /**
     * 返回输入框文本
     * @return 输入框文本
     */
    @Override
    public String getInputText() {
        return (mActionBarEditText.getEditableText() != null) ? mActionBarEditText.getEditableText().toString() : "";
    }

    /**
     * 设置输入框是否可用
     * @param enabled 是否可用
     */
    @Override
    public void setInputEnabled(boolean enabled) {
        mActionBarEditText.setEnabled(enabled);
    }

    /**
     * 设置清除按钮是否可用
     * @param enabled 是否可用
     */
    @Override
    public void setDelEnabled(boolean enabled) {
        mClearView.setEnabled(enabled);
    }

    /**
     * 设置输入框文本内容为空
     * @param ignoreNotify 是否需要监听输入框变化回调
     */
    @Override
    public void setTextNil(boolean ignoreNotify) {
        if(!ignoreNotify) {
            mActionBarEditText.removeTextChangedListener(mTextWatcher);
            mActionBarEditText.setText("");
            mActionBarEditText.addTextChangedListener(mTextWatcher);
        } else {
            mActionBarEditText.setText("");
        }

    }

    /**
     * 设置输入框默认提醒文字
     * @param text 文字
     */
    @Override
    public  void setHint(CharSequence text) {
        mActionBarEditText.setHint(text);
    }

    /**
     * 设置输入框输入按键事件监听
     * @param l 监听
     */
    @Override
    public  void setOnEditorActionListener(TextView.OnEditorActionListener l) {
        mActionBarEditText.setOnEditorActionListener(l);
    }

    @Override
    public void setOnSearchFocusChangeListener(OnFocusChangeListener l) {
        mOnFocusChangeListener = l;
    }

    /**
     * 设置输入框左边图标
     * @param resId 图标资源
     */
    @Override
    public void setDrawableLeft(int resId) {
        if(mActionBarEditText != null) {
            mActionBarEditText.setCompoundDrawables(getResources().getDrawable(resId), null, null, null);
        }
    }

    /**
     * 状态栏输入框
     */
    public static class ActionBarEditText extends androidx.appcompat.widget.AppCompatEditText {

        /**标题栏搜索框*/
        DTYTXActionBarSearchView mSearchView;

        public ActionBarEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public ActionBarEditText(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        public boolean onKeyPreIme(int keyCode, KeyEvent event) {
            LogUtil.d(TAG , "on onKeyPreIme");
            if(keyCode == KeyEvent.KEYCODE_BACK) {
                KeyEvent.DispatcherState mDispatcherState;
                if(event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                    LogUtil.v(TAG, "on onKeyPreIme action down");
                    mDispatcherState = this.getKeyDispatcherState();
                    if(mDispatcherState != null) {
                        mDispatcherState.startTracking(event, this);
                    }
                    return true;
                }
                if(event.getAction() == KeyEvent.ACTION_UP) {
                    LogUtil.v(TAG, "on onKeyPreIme action up");
                    mDispatcherState = this.getKeyDispatcherState();
                    if(mDispatcherState != null) {
                        mDispatcherState.handleUpEvent(event);
                    }

                    if(event.isTracking() && !event.isCanceled()) {
                        LogUtil.v(TAG, "on onKeyPreIme action up is tracking");
                        mSearchView.clearFocus();
                        InputMethodManager inputMgr = (InputMethodManager)this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if(inputMgr != null) {
                            inputMgr.hideSoftInputFromWindow(this.getWindowToken(), 0);
                        }
                        return true;
                    }
                }
            }
            return super.onKeyPreIme(keyCode, event);
        }
    }

}
