package com.geek.appindex.polyv;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.geek.appindex.R;
import com.geek.biz1.bean.SPolyvList1Bean1;
import com.geek.libbase.widgets.LxRelativeLayout;
import com.geek.libglide47.base.GlideImageView;

public class PolyvAdapter1 extends BaseQuickAdapter<SPolyvList1Bean1, BaseViewHolder> {

    public PolyvAdapter1() {
        super(R.layout.polyv_fragment1_item1);
    }

    @Override
    protected void convert(BaseViewHolder helper, SPolyvList1Bean1 item) {
        GlideImageView iv1 = helper.itemView.findViewById(R.id.iv1);
        TextView tv1 = helper.itemView.findViewById(R.id.tv1);
        LxRelativeLayout rl1 = helper.itemView.findViewById(R.id.rl1);
        rl1.setTouch(false);
        iv1.loadImage(item.getCoverImg(), R.mipmap.img_bkg);
        tv1.setText(item.getLiveName());
//        helper.addOnClickListener(R.id.rl1);
        helper.addOnClickListener(R.id.iv1);
        helper.addOnClickListener(R.id.tv1);
    }
}