package com.sangfor.sdkdemo.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sangfor.sdk.SFUemSDK;
import com.sangfor.sdk.base.SFBaseMessage;
import com.sangfor.sdk.base.SFSetSpaConfigListener;
import com.sangfor.sdkdemo.R;

public class SpaConfigActivity extends AppCompatActivity {

    private static final String TAG = "SpaConfigActivity";
    private EditText mEtSpaConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vpn_activity_set_spa_config);

        initView();
    }

    private void initView() {
        mEtSpaConfig = findViewById(R.id.spa_config_text);
        findViewById(R.id.set_spa_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String config = mEtSpaConfig.getText().toString();
                SFUemSDK.setSpaConfig(config, new SFSetSpaConfigListener() {
                    @Override
                    public void onSetSpaConfig(String result, SFBaseMessage error) {
                        Log.i(TAG, "spa result:"+ result + ", error:" + error);
                        if (error.mErrCode != 0) {
                            Toast.makeText(SpaConfigActivity.this,
                                    "SPA设置失败"+ ", Error Message:" + error.toString(),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(SpaConfigActivity.this, "SPA设置成功， result:" + result,
                                    Toast.LENGTH_SHORT)
                                    .show();
                            finish();
                        }
                    }
                });
            }
        });
    }
}