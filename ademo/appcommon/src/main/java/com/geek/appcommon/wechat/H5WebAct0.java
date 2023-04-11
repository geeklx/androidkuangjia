package com.geek.appcommon.wechat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.AppUtils;
import com.geek.appcommon.SlbBase;
import com.geek.common.R;
import com.just.agentweb.geek.fragment.AgentWebFragment;
import com.just.agentweb.geek.hois3.HiosHelperNew;

/**
 * @author fosung
 */
public class H5WebAct0 extends SlbBase {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hios_h5web;
    }

    @Override
    protected void setup(@Nullable Bundle savedInstanceState) {
        super.setup(savedInstanceState);
        findViewById(R.id.tv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HiosHelperNew.resolveAd(H5WebAct0.this, H5WebAct0.this, "http://www.baidu.com/");
            }
        });
        findViewById(R.id.tv2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppUtils.getAppPackageName() + ".hs.act.slbapp.WeChatWebActivity"));
            }
        });
        findViewById(R.id.tv3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppUtils.getAppPackageName() + ".hs.act.slbapp.WeChatH5JsWebActivity");
                intent.putExtra(AgentWebFragment.URL_KEY, "http://www.baidu.com/");
                intent.putExtra("isHideBar", false);
                startActivity(intent);
            }
        });
        findViewById(R.id.tv4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppUtils.getAppPackageName() + ".hs.act.slbapp.H5WebAct");
                intent.putExtra(AgentWebFragment.URL_KEY, "http://www.baidu.com/");
                intent.putExtra("isHideBar", false);
                startActivity(intent);
            }
        });
        findViewById(R.id.tv5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppUtils.getAppPackageName() + ".hs.act.slbapp.H5JsWebActivity"));
            }
        });
    }


}
