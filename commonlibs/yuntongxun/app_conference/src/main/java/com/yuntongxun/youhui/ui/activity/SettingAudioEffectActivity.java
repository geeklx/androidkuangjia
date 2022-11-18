package com.yuntongxun.youhui.ui.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yuntongxun.plugin.common.YTXAppMgr;
import com.yuntongxun.plugin.common.recycler.YTXConfBaseQuickAdapter;
import com.yuntongxun.plugin.common.ui.YTXECSuperActivity;
import com.yuntongxun.youhui.R;
import com.yuntongxun.youhui.ui.adapter.CommonSetAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gethin on 2017/12/7.
 */

public class SettingAudioEffectActivity extends YTXECSuperActivity {

    private RecyclerView recyclerView;
    private CommonSetAdapter setAdapter;
    private List<String> list;
    public static final String DATA_TYPE = "SettingAudioEffectActivity_data_type";
    public static final String POSITION_SELECT = "SettingAudioEffectActivity_position_select";
    private int selectPosition;
    private int type;

    @Override
    public int getLayoutId() {
        return R.layout.activity_set_audio_effect;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    private void initData() {
        if (list == null) {
            list = new ArrayList<>();
        }
        type = getIntent().getIntExtra(DATA_TYPE, 0);
        SharedPreferences sp = YTXAppMgr.getSharePreference();
        if (type == 0) {
            list.add("0");
            list.add("1");
            list.add("2");
            list.add("3");
            list.add("4");
            selectPosition = sp.getInt("audio_mode", 0);
        } else if (type == 1) {
            list.add("AGC_Unchanged");
            list.add("AGC_Default");
            list.add("AGC_AdaptiveAnalog");
            list.add("AGC_AdaptiveDigital");
            list.add("AGC_FixedDigital");
            selectPosition = sp.getInt("audio_agc", 3);
        } else if (type == 2) {
            list.add("EC_Unchanged");
            list.add("EC_Default");
            list.add("EC_Conference");
            list.add("EC_Aec");
            list.add("EC_Aecm");
            selectPosition = sp.getInt("audio_ec", 4);
        } else if (type == 3) {
            list.add("NS_Unchanged");
            list.add("NS_Default");
            list.add("NS_Conference");
            list.add("NS_LowSuppression");
            list.add("NS_ModerateSuppression");
            list.add("Ns_HighSuppression");
            list.add("Ns_VeryHighSuppression");
            selectPosition =sp .getInt("audio_ns", 6);
        }
    }

    private String getTitleByType(int type) {
        if (type == 1) {
            return "自动增益设置";
        } else if (type == 2) {
            return "回音消除设置";
        } else if (type == 3) {
            return "静音抑制设置";
        }
        return "声音模式设置";
    }

    private void initView() {
        setActionBarTitle(getTitleByType(type));
        recyclerView = (RecyclerView) findViewById(R.id.audio_effect_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (setAdapter == null) {
            if (list == null) {
                list = new ArrayList<>();
            }
            setAdapter = new CommonSetAdapter(list);
        }
        setAdapter.bindToRecyclerView(recyclerView);
        recyclerView.scrollToPosition(selectPosition);
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (recyclerView.findViewHolderForAdapterPosition(selectPosition) != null) {
                    recyclerView.findViewHolderForAdapterPosition(selectPosition).itemView.performClick();
                }
            }
        }, 50);
        setAdapter.setOnItemChildClickListener(new YTXConfBaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(YTXConfBaseQuickAdapter adapter, View view, int position) {
                hideAdapterAllCheck();
                adapter.getViewByPosition(position, R.id.common_set_check).setVisibility(View.VISIBLE);
                adapter.getViewByPosition(position, R.id.item_root_view).setBackgroundColor(Color.parseColor("#282c49"));
                TextView textView = (TextView) adapter.getViewByPosition(position, R.id.common_set_name);
                if (textView != null) textView.setTextColor(Color.WHITE);
                saveSetData(position);
            }
        });
    }

    private void saveSetData(int position) {
        if (position == selectPosition ) {
            return;
        }
        SharedPreferences.Editor editor = YTXAppMgr.getSharePreferenceEditor();
        String key = null;
        if (type == 1) {
            key = "audio_agc";
        } else if (type == 2) {
            key = "audio_ec";
        } else if (type == 3) {
            key = "audio_ns";
        } else {
            key = "audio_mode";
        }
        editor.putInt(key, position).apply();
        selectPosition = position;
    }

    public void hideAdapterAllCheck() {
        if (setAdapter == null) return;
        for (int i = 0; i < setAdapter.getData().size(); i++) {
            setAdapter.getViewByPosition(i, R.id.common_set_check).setVisibility(View.INVISIBLE);
            setAdapter.getViewByPosition(i, R.id.item_root_view).setBackgroundColor(Color.TRANSPARENT);
            TextView textView = (TextView) setAdapter.getViewByPosition(i, R.id.common_set_name);
            if (textView != null) textView.setTextColor(Color.BLACK);
        }
    }
}
