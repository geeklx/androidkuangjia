package com.sangfor.sdkdemo.utils;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SFEditText extends AppCompatEditText implements TextWatcher {

    private Drawable leftDrawable = null;
    private Drawable rightDrawable = null;

    // 控件是否有焦点
    private boolean hasFocus;

    private IMyRightDrawableClick mRightDrawableClick;

    public SFEditText(Context context) {
        super(context);
    }

    public SFEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SFEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, android.R.attr.editTextStyle);
        init();
    }


    private void init() {
        //获取RadioButton的图片集合
        Drawable[] drawables = getCompoundDrawables();

        leftDrawable = drawables[0];
        rightDrawable = drawables[2];

        setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);

        //设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }


    //设置显示图片的大小
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);

        //这里只要改后面两个参数就好了，一个宽一个是高，如果想知道为什么可以查找源码
        if (left != null) {
            left.setBounds(0, 0, 50, 50);
        }
        if (right != null) {
            right.setBounds(0, 0, 50, 50);
        }
        if (top != null) {
            top.setBounds(0, 0, 100, 100);
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, 100, 100);
        }
        setCompoundDrawables(left, top, right, bottom);
    }


    //光标选中时判断
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        this.hasFocus = focused;
        if (focused)
            setImageVisible(getText().length() > 0);
        else
            setImageVisible(false);

    }

    //设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
    protected void setImageVisible(boolean flag) {
        if (getCompoundDrawables()[2] != null) {
            rightDrawable = getCompoundDrawables()[2];
        }
        if (flag) {
            setCompoundDrawables(getCompoundDrawables()[0], null, rightDrawable, null);
        } else {
            setCompoundDrawables(getCompoundDrawables()[0], null, null, null);
        }
    }

    //文本框监听事件
    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {

        if (hasFocus) {
            if (text.length() > 0)
                setImageVisible(true);
            else
                setImageVisible(false);

        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {

                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    if (mRightDrawableClick != null) {
                        //调用点击事件
                        mRightDrawableClick.rightDrawableClick();
                    }
                }
            }
        }

        return super.onTouchEvent(event);
    }

    public void setDrawableClick(IMyRightDrawableClick mMightDrawableClick) {
        this.mRightDrawableClick = mMightDrawableClick;
    }

    public void setRightDrawable(Drawable drawable) {
        rightDrawable = drawable;
        setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, rightDrawable, null);
    }

    //自定义接口（实现右边图片点击事件）
    public interface IMyRightDrawableClick {
        void rightDrawableClick();
    }
}
