package com.yuntongxun.youhui.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.yuntongxun.plugin.common.common.utils.ConfToasty;
import com.yuntongxun.plugin.common.common.utils.LogUtil;
import com.yuntongxun.plugin.common.common.utils.TextUtil;
import com.yuntongxun.plugin.common.ui.YHCECSuperActivity;
import com.yuntongxun.plugin.conference.bean.YHCConfMember;
import com.yuntongxun.plugin.conference.manager.YHCConferenceMgr;
import com.yuntongxun.plugin.conference.manager.inter.CONF_TYPE;
import com.yuntongxun.youhui.R;

/**
 * Created by gethin on 2018/5/2.
 */

public class VoipCallMemberActivity extends YHCECSuperActivity implements View.OnClickListener {

    private EditText id1;
    private EditText name1;
    private String TAG = LogUtil.getLogUtilsTag(VoipCallMemberActivity.class);

    @Override
    public int getLayoutId() {
        return R.layout.activity_call_voip_member;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarTitle("点对点呼叫");
        initView();
    }

    private void initView() {
        id1 = (EditText) findViewById(R.id.user_id1);
        name1 = (EditText) findViewById(R.id.user_name1);
        findViewById(R.id.voip_voice_call_btn).setOnClickListener(this);
        findViewById(R.id.voip_video_call_btn).setOnClickListener(this);
    }


    private boolean checkName(String name) {
        if (TextUtil.isEmpty(name)) {
            return true;
        }
        if (name.length() > 12) {
            ConfToasty.error(getString(R.string.yhc_str_name_length_range));
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String userId1 = id1.getText().toString().trim();
        String userName1 = name1.getText().toString().trim();
        if (TextUtil.isEmpty(userId1) || TextUtil.isEmpty(userName1)) {
            return;
        }
        if (!checkName(userName1)) {
            return;
        }

        if (id == R.id.voip_voice_call_btn || id == R.id.voip_video_call_btn) {
            YHCConfMember member = new YHCConfMember();
            member.setAccount(userId1);
            member.setName(userName1);
            YHCConferenceMgr.getManager().startVoipConf(
                    this,
                    member,
                    "voip",
                    id == R.id.voip_video_call_btn ? CONF_TYPE.CONF_VIDEO : CONF_TYPE.CONF_VOICE);
            finish();
        }
    }

}
