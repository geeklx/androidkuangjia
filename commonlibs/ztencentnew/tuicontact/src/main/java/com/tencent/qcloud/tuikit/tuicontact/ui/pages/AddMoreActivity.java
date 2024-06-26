package com.tencent.qcloud.tuikit.tuicontact.ui.pages;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;


import com.blankj.utilcode.util.ActivityUtils;
import com.tencent.qcloud.tuicore.component.TitleBarLayout;
import com.tencent.qcloud.tuicore.component.activities.BaseLightActivity;
import com.tencent.qcloud.tuicore.component.interfaces.ITitleBarLayout;
import com.tencent.qcloud.tuicore.util.SoftKeyBoardUtil;
import com.tencent.qcloud.tuikit.tuicontact.R;
import com.tencent.qcloud.tuikit.tuicontact.TUIContactConstants;
import com.tencent.qcloud.tuikit.tuicontact.presenter.AddMorePresenter;
import com.tencent.qcloud.tuikit.tuicontact.ui.interfaces.IAddMoreActivity;

public class AddMoreActivity extends BaseLightActivity implements IAddMoreActivity {

    private static final String TAG = AddMoreActivity.class.getSimpleName();

    private TitleBarLayout mTitleBar;
    private EditText mUserID;
    private EditText mAddWording;
    private boolean mIsGroup;

    private AddMorePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String userId = "";
        if (getIntent() != null && getIntent().getExtras() != null) {
            mIsGroup = getIntent().getExtras().getBoolean(TUIContactConstants.GroupType.GROUP, false);
            userId = getIntent().getExtras().getString(TUIContactConstants.GroupType.USERID, "");
        }

        presenter = new AddMorePresenter();
        presenter.setAddMoreActivity(this);

        setContentView(R.layout.contact_add_activity);

        mTitleBar = findViewById(R.id.add_friend_titlebar);
        mTitleBar.setTitle(mIsGroup ? getResources().getString(R.string.add_group) : getResources().getString(R.string.add_friend), ITitleBarLayout.Position.LEFT);
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar.getRightGroup().setVisibility(View.GONE);

        mUserID = findViewById(R.id.user_id);
        mAddWording = findViewById(R.id.add_wording);

        mUserID.setText(userId);
    }

    public void add(View view) {
        if (mIsGroup) {
            addGroup(view);
        } else {
            addFriend(view);
        }
    }

    public void addFriend(View view) {
        String id = mUserID.getText().toString();
        if (TextUtils.isEmpty(id)) {
            return;
        }
        String addWording = mAddWording.getText().toString();
        presenter.addFriend(id, addWording);
        jumpIndex();
    }

    public void addGroup(View view) {
        String id = mUserID.getText().toString();
        if (TextUtils.isEmpty(id)) {
            return;
        }
        String addWording = mAddWording.getText().toString();
        presenter.joinGroup(id, addWording);
        jumpIndex();
    }

    private void jumpIndex() {
        try {
            Class<? extends Activity> klass = (Class<? extends Activity>) Class.forName("com.geek.appindex.index.ShouyeActivity");
            ActivityUtils.finishToActivity(klass, false, true);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        SoftKeyBoardUtil.hideKeyBoard(mUserID.getWindowToken());
        //

    }
//    @Override
//    public void finish() {
//        super.finish();
//        SoftKeyBoardUtil.hideKeyBoard(mUserID.getWindowToken());
//    }
}
