package com.yuntongxun.youhui.ui.activity;

import android.os.Bundle;

import com.yuntongxun.plugin.common.ui.YHCECSuperActivity;
import com.yuntongxun.plugin.conference.view.fragment.YHCHistroyListFragment;
import com.yuntongxun.youhui.R;

public class MeetingHistoryActivity extends YHCECSuperActivity {

    private YHCHistroyListFragment yhcHistroyListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarTitle(R.string.ytx_str_history_conference_list);
        initView();
    }

    private void initView() {
        if (yhcHistroyListFragment==null) {
            yhcHistroyListFragment = new YHCHistroyListFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.meeting_history_container, yhcHistroyListFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_meeting_history;
    }
}
