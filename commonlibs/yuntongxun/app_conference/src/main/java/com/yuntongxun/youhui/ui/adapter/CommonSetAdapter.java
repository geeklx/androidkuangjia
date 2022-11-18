package com.yuntongxun.youhui.ui.adapter;

import androidx.annotation.Nullable;

import com.yuntongxun.plugin.common.recycler.YTXBaseViewHolder;
import com.yuntongxun.plugin.common.recycler.YTXConfBaseQuickAdapter;
import com.yuntongxun.youhui.R;

import java.util.List;

/**
 * Created by gethin on 2017/11/21.
 */

public class CommonSetAdapter extends YTXConfBaseQuickAdapter<String,YTXBaseViewHolder> {

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    private int itemType = 1;

    public CommonSetAdapter(@Nullable List<String> data) {

        super(R.layout.item_common_set, data);
    }



    @Override
    protected void convert(YTXBaseViewHolder helper, String item) {
        helper.setText(R.id.common_set_name, item)
        .addOnClickListener(R.id.item_root_view);
    }
}
