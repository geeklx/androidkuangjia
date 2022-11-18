package com.rongxin.im.dome.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.rongxin.im.dome.R;
import com.yuntongxun.confwrap.WrapManager;
import com.yuntongxun.plugin.common.RXConfig;

public class ContactsListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ytx_activity_contacts_list);
        RXConfig.isShowRecentContacts = false;//是否显示最近联系人
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.convert_frame, WrapManager.getInstance().getTabContactFragment(this))
                .commit();
    }

}


