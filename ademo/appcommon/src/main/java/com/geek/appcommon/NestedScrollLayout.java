package com.geek.appcommon;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Size;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.math.MathUtils;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;
import com.geek.appcommon.interfaces.OnProgress;
import com.geek.common.R;


public class NestedScrollLayout extends FrameLayout implements NestedScrollingParent2 {
    public static final long ANIM_DURATION_FRACTION = 200L;


    private NestedScrollingParentHelper mParentHelper;


    private float contentTransY;//滑动内容初始化TransY
    private LinearLayout mllLayout;
    private float maxContentTransY = SizeUtils.dp2px(100);
    private float minContentTransY = SizeUtils.dp2px(0);
    private ValueAnimator valueAnimator;
    private OnProgress onProgress;

    public void setOnProgress(OnProgress onProgress) {
        this.onProgress = onProgress;
    }

    public NestedScrollLayout(@NonNull Context context) {
        this(context, null);
    }

    public NestedScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public NestedScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mParentHelper = new NestedScrollingParentHelper(this);
        valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                translation(mllLayout, (float) animation.getAnimatedValue());
                if (onProgress != null) {
                    onProgress.onProgress(getDownConetntClosePro());
                }
            }
        });
        valueAnimator.setDuration(ANIM_DURATION_FRACTION);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mllLayout = findViewById(R.id.ll);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        contentTransY = maxContentTransY;
    }


    //---NestedScrollingParent---//
    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int nestedScrollAxes) {
        return onStartNestedScroll(child, target, nestedScrollAxes, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int nestedScrollAxes) {
        onNestedScrollAccepted(child, target, nestedScrollAxes, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target) {
        onStopNestedScroll(target, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        onNestedPreScroll(target, dx, dy, consumed, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {

        if (velocityY < 0) {
            float translationY = mllLayout.getTranslationY();
            float dy = translationY - velocityY;

            //从展开状态下滑时处理回弹Fling
            if (translationY >= 0 && dy >= maxContentTransY) {
                if (dy < contentTransY) {
                    valueAnimator.setFloatValues(translationY, dy);
                } else if (dy > maxContentTransY) {
                    valueAnimator.setFloatValues(translationY, maxContentTransY);
                    target.scrollTo(0, 0);
                    if (target instanceof RecyclerView) {
                        ((RecyclerView) target).scrollToPosition(0);
                    }
                    valueAnimator.start();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return mParentHelper.getNestedScrollAxes();
    }

    //---NestedScrollingParent2---//
    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes,
                                       int type) {
        //只接受内容View的垂直滑动
        return child.getId() == mllLayout.getId() && axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes,
                                       int type) {
        mParentHelper.onNestedScrollAccepted(child, target, axes, type);

    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        mParentHelper.onStopNestedScroll(target, type);
        //如果不是从初始状态转换到展开状态过程触发返回
        if (mllLayout.getTranslationY() <= contentTransY) {
            return;
        }

    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, int type) {

    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed,
                                  int type) {
        float contentTransY = mllLayout.getTranslationY() - dy;

        //处理上滑
        if (dy > 0) {
            if (contentTransY >= minContentTransY) {
                translationByConsume(mllLayout, contentTransY, consumed, dy);
            } else {
                translationByConsume(mllLayout, minContentTransY, consumed, (mllLayout.getTranslationY() - minContentTransY));
            }
        }

        if (dy < 0 && !target.canScrollVertically(-1)) {
            //下滑时处理Fling,完全折叠时，下滑Recycler(或NestedScrollView) Fling滚动到列表顶部（或视图顶部）停止Fling
            if (type == ViewCompat.TYPE_NON_TOUCH && mllLayout.getTranslationY() == minContentTransY) {
                return;
            }

            //处理下滑
            if (contentTransY >= minContentTransY && contentTransY <= maxContentTransY) {
                translationByConsume(mllLayout, contentTransY, consumed, dy);
            } else {
                translationByConsume(mllLayout, maxContentTransY, consumed, maxContentTransY - mllLayout.getTranslationY());
                if (target instanceof RecyclerView) {
                    ((RecyclerView) target).stopScroll();
                }
                if (target instanceof NestedScrollView) {
                    //模拟DONW事件停止滚动，注意会触发onNestedScrollAccepted()
                    MotionEvent motionEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0);
                    target.onTouchEvent(motionEvent);
                }
            }
        }

        if (onProgress != null) {
            onProgress.onProgress(getDownConetntClosePro());
        }


    }


    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getContext().getResources().getDisplayMetrics());
    }

    private void setAlpha(View view, float alpha) {
        view.setAlpha(alpha);
    }

    private void setScale(View view, float scaleY, float scaleX) {
        view.setScaleY(scaleY);
        view.setScaleX(scaleX);
    }

    private void setScaleAlpha(View view, float scaleY, float scaleX, float alpha) {
        setAlpha(view, alpha);
        setScale(view, scaleY, scaleX);
    }


    private void translationByConsume(View view, float translationY, int[] consumed,
                                      float consumedDy) {
        consumed[1] = (int) consumedDy;
        view.setTranslationY(translationY);
    }

    private void translation(View view, float translationY) {
        view.setTranslationY(translationY);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (valueAnimator.isStarted()) {
            valueAnimator.cancel();
            valueAnimator.removeAllUpdateListeners();
        }
    }

    private void alphaTransView(float transY) {
        float upCollapseTransPro = getUpExpandTransPro();

    }

    private float getUpExpandTransPro() {
        return (contentTransY - MathUtils.clamp(mllLayout.getTranslationY(), 0, contentTransY)) / contentTransY;
    }

    private float getDownConetntClosePro() {
        return mllLayout.getTranslationY() * 1.0f / maxContentTransY;
    }

    public void setMaxContentTransY(float maxContentTransY) {
        this.maxContentTransY = maxContentTransY;
    }

    public void setMinContentTransY(float minContentTransY) {
        this.minContentTransY = minContentTransY;
    }
}


