package com.geek.appindex.index.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.geek.appcommon.SlbBase;
import com.geek.appindex.R;
import com.tencent.qcloud.tuikit.tuicontact.ui.pages.TUIContactFragment;

public class GztFragmentPeopleAct extends SlbBase {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_gztfragmentshouye;
    }

    @Override
    protected void setup(@Nullable Bundle savedInstanceState) {
        super.setup(savedInstanceState);
        Fragment mFragment = null;
        String namef = "";
        mFragment = TUIContactFragment.getInstance(new Bundle());
        namef = TUIContactFragment.class.getName();
        getSupportFragmentManager().beginTransaction().add(R.id.container_framelayout, mFragment, namef).show(mFragment).commit();
    }
}
