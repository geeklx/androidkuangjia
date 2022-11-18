package com.yuntongxun.youhui.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yuntongxun.plugin.common.YTXAppMgr;
import com.yuntongxun.plugin.common.common.utils.ConfToasty;
import com.yuntongxun.plugin.common.common.utils.LogUtil;
import com.yuntongxun.plugin.common.common.utils.TextUtil;
import com.yuntongxun.plugin.common.ui.YHCECSuperActivity;
import com.yuntongxun.plugin.conference.bean.YHCConfMember;
import com.yuntongxun.youhui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gethin on 2018/5/2.
 */

public class ConfReturnMemberActivity extends YHCECSuperActivity implements View.OnClickListener {

    private EditText id1;
    private EditText name1;
    private EditText phone1;
    private EditText id2;
    private EditText name2;
    private EditText phone2;
    private EditText id3;
    private EditText name3;
    private EditText phone3;
    public static final String SELECT_TYPE = "ConfReturnMemberActivity.select_type";
    public static final String LIMIT_COUNT = "max_limit_num";

    private java.lang.String TAG = LogUtil.getLogUtilsTag(ConfReturnMemberActivity.class);
    private int mSelectType;
    private int max_limit_count;

    @Override
    public int getLayoutId() {
        return R.layout.activity_conf_return_member;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSelectType = getIntent().getIntExtra(SELECT_TYPE, 0);
        setActionBarTitle(mSelectType == 0 ? "添加成员" : "添加电话成员");
        initView();
        max_limit_count = getIntent().getIntExtra(LIMIT_COUNT, 50);

    }

    private void initView() {
        id1 = (EditText) findViewById(R.id.user_id1);
        name1 = (EditText) findViewById(R.id.user_name1);
        phone1 = (EditText) findViewById(R.id.user_phone1);

        id2 = (EditText) findViewById(R.id.user_id2);
        name2 = (EditText) findViewById(R.id.user_name2);
        phone2 = (EditText) findViewById(R.id.user_phone2);

        id3 = (EditText) findViewById(R.id.user_id3);
        name3 = (EditText) findViewById(R.id.user_name3);
        phone3 = (EditText) findViewById(R.id.user_phone3);

        if (mSelectType == 1) {
            viewControlVisible(true,phone1,phone2,phone3);
            viewControlVisible(false,id1,id2,id3);
        }

        Button returnMember = (Button) findViewById(R.id.sure_btn);
        returnMember.setOnClickListener(this);
    }

    private void viewControlVisible(boolean isVisible,View... views) {
        if (views == null) {
            return;
        }
        for (View view : views) {
            if (view == null) continue;
            view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
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
        String userId2 = id2.getText().toString().trim();
        String userId3 = id3.getText().toString().trim();

        String userName1 = name1.getText().toString().trim();
        String userName2 = name2.getText().toString().trim();
        String userName3 = name3.getText().toString().trim();

        String phoneNum1 = phone1.getText().toString().trim();
        String phoneNum2 = phone2.getText().toString().trim();
        String phoneNum3 = phone3.getText().toString().trim();
        if (!checkName(userName1) || !checkName(userName2) || !checkName(userName3)) {
            return;
        }


        List<YHCConfMember> members = new ArrayList<>();
        if (id == R.id.sure_btn) {
            String userIdSelf = YTXAppMgr.getUserId();
            if(userIdSelf != null && (userIdSelf.equals(userId1) || userIdSelf.equals(userId2) || userIdSelf.equals(userId3))){
                ConfToasty.normal("限制自己邀请自己");
                return;
            }
            if (max_limit_count == 0) {
                ConfToasty.success(getString(R.string.comm_reach_upper_limit_person));
                return;
            }
            boolean isAdd = addMembers(userId1, userId2, userId3, userName1, userName2, userName3, phoneNum1, phoneNum2, phoneNum3, members);
            if (isAdd) {
//                ConfImpl.returnMembers(members);
                finish();
            } else {
                ConfToasty.info(mSelectType == 0 ? "userId不能为空" : "电话号不能为空");
            }
        }
    }

    private boolean isParamLegal(String param1, String param2, String param3) {
        if (TextUtil.isEmpty(param1) && TextUtil.isEmpty(param2) && TextUtil.isEmpty(param3)) {
            return false;
        }
        return true;
    }

    private boolean addMembers(
            String userId1, String userId2, String userId3,
            String userName1, String userName2, String userName3,
            String userPhone1, String userPhone2, String userPhone3,
            List<YHCConfMember> members) {
        String param1;
        String param2;
        String param3;
        if (mSelectType == 1) {
            param1 = userPhone1;
            param2 = userPhone2;
            param3 = userPhone3;
        } else {
            param1 = userId1;
            param2 = userId2;
            param3 = userId3;
        }
        boolean paramLegal = isParamLegal(param1, param2, param3);
        if (!paramLegal) {
            return false;
        }

        if (!TextUtil.isEmpty(param1))
            members.add(getMemberByType(userId1, userName1, userPhone1));
        if (!TextUtil.isEmpty(param2))
            members.add(getMemberByType(userId2, userName2, userPhone2));
        if (!TextUtil.isEmpty(param3))
            members.add(getMemberByType(userId3, userName3, userPhone3));

        return true;
    }

    private YHCConfMember getMemberByType(String userId, String name, String phoneNum) {
        YHCConfMember member = new YHCConfMember();
        if (!TextUtil.isEmpty(name))
            member.setName(name);
        if (mSelectType == 0) {
            member.setAccount(userId);
            if (TextUtil.isEmpty(name)) {
                member.setName(userId);
            }
        } else if (mSelectType == 1) {
            member.setPhoneNum(phoneNum);
            member.setOutCall(true);
            if (TextUtil.isEmpty(name)) {
                member.setName(phoneNum);
            }
        }
        return member;
    }
}
