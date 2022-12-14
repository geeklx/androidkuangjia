package com.geek.appsplash;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.geek.biz1.bean.FinitBean;
import com.geek.libbase.base.SlbBaseActivityPermissions;
import com.geek.libbase.widgets.AlphaViewString;
import com.geek.libutils.data.MmkvUtils;

import java.util.ArrayList;
import java.util.List;

public class SplshActivity extends SlbBaseActivityPermissions implements View.OnClickListener {

    private AlphaViewString alphaview;

    /**
     * 加载图片ViewPager1
     */
//    布局设置
//    private Integer[] Layouts = {R.layout.splash_activity_lay1, R.layout.splash_activity_lay2, R.layout.splash_activity_lay3, R.layout.splash_activity_lay4};
    //    private Integer[] strings = {/*R.drawable.guid1,*/ R.drawable.guid2, R.drawable.guid3, R.drawable.guid4};
//    private String[] strings = {
//            "http://cdn2.cdn.haier-jiuzhidao.com/1398.png",
//            "http://cdn2.cdn.haier-jiuzhidao.com/1398.png",
//            "http://cdn2.cdn.haier-jiuzhidao.com/1398.png",
//            "http://cdn2.cdn.haier-jiuzhidao.com/1398.png"
//    };

    private List<String> list1 = new ArrayList<>();
    private String[] strings;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash_act;
    }

    @Override
    protected void setup(@Nullable Bundle savedInstanceState) {
        super.setup(savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      setContentView(R.layout.activity_splash_act);
        FinitBean fconfigBean = MmkvUtils.getInstance().get_common_json("config", FinitBean.class);
        strings = new String[fconfigBean.getGuideimage().size()];
        for (int i = 0; i < fconfigBean.getGuideimage().size(); i++) {
            strings[i] = fconfigBean.getGuideimage().get(i);
        }
        alphaview = findViewById(R.id.alphaview);
        alphaview.setPointGravity(Gravity.END);
        alphaview.setPointVisbile(View.GONE);
        alphaview.setPoint(R.drawable.new_press_dian, R.drawable.new_normal_dian, 50, 50, 30, 40, 30, 1);
        alphaview.setSplashItemOnClick(this, R.layout.splash_activity_lay4, R.id.regist);
        alphaview.setData(strings);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.regist) {
//            startActivity(new Intent("hs.act.slbapp.index"));
//            Toasty.info(this, "注册").show();
            SPUtils.getInstance().put("key_token", 0);
            onBackPressed();
        }
//        else if (i == R.id.regist) {
//            onBackPressed();
//            // 设置权限Android8.o
////            setIsjump(true);
//        }
    }

    @Override
    protected boolean is_android10() {
        // 初始化权限Android8.0
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AppUtils.getAppPackageName() + ".hs.act.slbapp.shouye"));
        finish();
    }
}