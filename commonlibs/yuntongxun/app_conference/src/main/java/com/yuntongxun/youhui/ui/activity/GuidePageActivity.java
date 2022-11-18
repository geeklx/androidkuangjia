package com.yuntongxun.youhui.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.Button;

import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.plugin.common.YTXSDKCoreHelper;
import com.yuntongxun.plugin.common.ui.AbsRongXinActivity;
import com.yuntongxun.plugin.common.ui.tools.YTXSystemBarHelper;
import com.yuntongxun.youhui.R;
import com.yuntongxun.youhui.ui.adapter.GuideViewPagerAdapter;
import com.yuntongxun.youhui.ui.fragment.GuildPageFragment1;
import com.yuntongxun.youhui.ui.fragment.GuildPageFragment2;
import com.yuntongxun.youhui.ui.fragment.GuildPageFragment3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gethin on 2017/9/19.
 */

public class GuidePageActivity extends AbsRongXinActivity implements View.OnClickListener {



    private GuideViewPagerAdapter adapter;
//    private Button join_conf;
    private Button login;
    private ViewPager confGuideViewpager;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_page);
        initView();
    }

    private void initView() {
        YTXSystemBarHelper.immersiveStatusBar(this,0);
        YTXSystemBarHelper.setStatusBarDarkMode(this);
        confGuideViewpager = (ViewPager) findViewById(R.id.conf_guide_viewpager);
        login = (Button) findViewById(R.id.guide_login);
        login.setOnClickListener(this);
        initPager();
    }

    private void initPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new GuildPageFragment1());
        fragments.add(new GuildPageFragment2());
        fragments.add(new GuildPageFragment3());
        if (adapter == null)
        adapter = new GuideViewPagerAdapter(this,fragments, 3);
        confGuideViewpager.setAdapter(adapter);
        confGuideViewpager.setOffscreenPageLimit(3);
        confGuideViewpager.setCurrentItem(0);
    }

    private int noEnterJoin;


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.guide_login) {
            Intent intent = new Intent(GuidePageActivity.this, LoginActivity.class);
            startActivityForResult(intent,0x100);
            noEnterJoin = 0;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x100) {
            noEnterJoin = 0;
        }
    }

    @Override
    protected String[] getReceiverAction() {
        return new String[]{YTXSDKCoreHelper.ACTION_SDK_CONNECT};
    }

    @Override
    protected void onHandleReceiver(Context context, Intent intent) {
        super.onHandleReceiver(context, intent);
        if (intent == null) {
            return;
        }

        if (YTXSDKCoreHelper.ACTION_SDK_CONNECT.equals(intent.getAction())) {
            // 初始注册结果，成功或者失败
            int error = intent.getIntExtra("error", -1);
            if (noEnterJoin == 0 && YTXSDKCoreHelper.isConnected() && error == SdkErrorCode.REQUEST_SUCCESS) {
                finish();
            }
        }
    }
}
