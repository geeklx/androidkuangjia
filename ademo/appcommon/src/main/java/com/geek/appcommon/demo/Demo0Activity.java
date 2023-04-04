package com.geek.appcommon.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.geek.appcommon.SlbBase;
import com.geek.appcommon.demo.demo1.Demo1Activity;
import com.geek.appcommon.demo.demo2.Demo2Activity;
import com.geek.appcommon.demo.demo3.Demo3Activity;
import com.geek.common.R;

/**
 * @author: lhw
 * @date: 2022/7/18
 * @desc
 */
public class Demo0Activity extends SlbBase {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_demo0;
    }

    @Override
    protected void setup(@Nullable Bundle savedInstanceState) {
        super.setup(savedInstanceState);
        findViewById(R.id.tv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                startActivity(new Intent(Demo0Activity.this, Demo1Activity.class));
            }
        });
        findViewById(R.id.tv2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                startActivity(new Intent(Demo0Activity.this, Demo2Activity.class));
            }
        });
        findViewById(R.id.tv3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                startActivity(new Intent(Demo0Activity.this, Demo3Activity.class));
            }
        });
    }


}
