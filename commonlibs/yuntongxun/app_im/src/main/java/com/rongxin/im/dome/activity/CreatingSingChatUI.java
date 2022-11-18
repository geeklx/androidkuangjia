package com.rongxin.im.dome.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.rongxin.im.dome.R;
import com.yuntongxun.plugin.common.common.utils.LogUtil;
import com.yuntongxun.plugin.common.common.utils.ToastUtil;
import com.yuntongxun.plugin.im.manager.YTXIMPluginManager;

/**
 * 邀请群聊  一个or多个
 * 一个为点对点
 * 多个为群聊
 */
public class CreatingSingChatUI extends AppCompatActivity {
    private EditText etAccount;
    private Button btnCancle,btnChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ytx_activity_create_singlechat);
        initView();

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(!TextUtils.isEmpty(etAccount.getText().toString())){
                   etAccount.setText("");
               }
            }
        });
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commit();
            }
        });
    }



    private void commit() {
        if(TextUtils.isEmpty(etAccount.getText().toString())){
            ToastUtil.showMessage("请输入对方账号");
            return;
        }
        YTXIMPluginManager.getManager().startSingChat(this, etAccount.getText().toString(),false);

    }


    private void initView() {
        etAccount = (EditText) findViewById(R.id.et_account);
        btnCancle = (Button) findViewById(R.id.btn_cancle);
        btnChat = (Button) findViewById(R.id.btn_chat);
    }

}
