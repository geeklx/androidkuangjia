package com.geek.appcommon.ytx;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.geek.common.R;
import com.geek.libglide47.base.GlideImageView;

public class YHCLiveAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public YHCLiveAdapter() {
        super(R.layout.item_ytx_live);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        GlideImageView ivLive = helper.itemView.findViewById(R.id.iv_live);
        TextView tvName = helper.itemView.findViewById(R.id.tv_name);
        tvName.setText(item);
    }
}