package com.yuntongxun.youhui.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.yuntongxun.plugin.common.common.YTXBackwardSupportUtil;
import com.yuntongxun.plugin.common.common.utils.ResourceHelper;

/**
 * 主界面底部图标
 * @author 容联•云通讯
 */
public class RXTabIconView extends ImageView {

    private Paint mPaint;
    private int mAlpha = 0;
    private Bitmap fromBitmap;
    private Bitmap middleBitmap;
    private Bitmap toBitmap;
    private Rect fromRect;
    private Rect middleRect;
    private Rect toRect;

    public RXTabIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RXTabIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    /**
     * 初始化图片
     * @param from 开始图片
     * @param middle 过度图片
     * @param to 结束图片
     */
    public final void init(int from, int middle, int to) {

        this.fromBitmap = ResourceHelper.getBitmap(getContext() , from);
        this.middleBitmap = ResourceHelper.getBitmap(getContext() , middle);
        this.toBitmap = ResourceHelper.getBitmap(getContext() , to);

        int width = YTXBackwardSupportUtil.px2dip(getContext(), 32.0F);
        int height = YTXBackwardSupportUtil.px2dip(getContext(), 28.0F);

        int left = width / 2 - this.fromBitmap.getWidth() / 2;
        int top = height / 2 - this.fromBitmap.getHeight() / 2;
        this.fromRect = new Rect(left, top, this.fromBitmap.getWidth() + left , this.fromBitmap.getHeight() + top);
        this.middleRect = new Rect(left, top, this.middleBitmap.getWidth() + left, this.middleBitmap.getHeight() + top);
        this.toRect = new Rect(left, top, this.toBitmap.getWidth()  + left, this.toBitmap.getHeight() + top);
        this.mPaint = new Paint(1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(this.mPaint != null) {
            if(this.mAlpha < 128) {
                this.mPaint.setAlpha(255 - this.mAlpha * 2);
                canvas.drawBitmap(this.fromBitmap, null, this.fromRect, this.mPaint);
                this.mPaint.setAlpha(this.mAlpha * 2);
                canvas.drawBitmap(this.middleBitmap, null, this.middleRect, this.mPaint);
            } else {
                this.mPaint.setAlpha(255 - this.mAlpha * 2);
                canvas.drawBitmap(this.middleBitmap, null, this.middleRect, this.mPaint);
                this.mPaint.setAlpha(this.mAlpha * 2);
                canvas.drawBitmap(this.toBitmap, null, this.toRect, this.mPaint);
            }
        }
    }

    public void setIconAlpha(int alpha) {
        mAlpha = alpha;
        invalidate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }
}
