package com.yuntongxun.youhui.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yuntongxun.youhui.R;

/**
 * Created by zhangtao on 16/8/4.
 */
public class ConfListTabView extends FrameLayout {

    TextView mLaberTV;

    public ConfListTabView(Context context) {
        super(context);
        init(context);
    }

    public ConfListTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View inflate = View.inflate(context, R.layout.view_douban_movie_tab, this);
        mLaberTV = (TextView) inflate.findViewById(R.id.tab_laber_tv);
    }

    public void setData(String laber) {
        mLaberTV.setText(laber);
    }
}
