package com.rongxin.im.dome.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.rongxin.im.dome.R;
import com.yuntongxun.confwrap.WrapManager;

public class ConfActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ytx_activity_contacts_list);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.convert_frame, WrapManager.getInstance().getReserveListFragment(this))
                .commit();
    }

}