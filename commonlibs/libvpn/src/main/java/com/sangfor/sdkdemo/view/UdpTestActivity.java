package com.sangfor.sdkdemo.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.sangfor.sdkdemo.R;
import com.sangfor.sdkdemo.udp.UdpMessageObj;
import com.sangfor.sdkdemo.udp.UdpOperator;

import java.lang.ref.WeakReference;

public class UdpTestActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public static final int MSG_SHOW_UDP_RECV_DATA = 100;

    private Button mBtnSendto;
    private EditText mEtIpAddr;
    private EditText mEtPort;
    private EditText mEtSendLen;
    private EditText mEtSendData;
    private TextView mTvRecvData;
    private TextView mTvRecvLen;
    private CheckBox mCbSendLooper;

    private UdpOperator mUdpOperator;
    private UdpHandler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.vpn_activity_udp_test);
        mBtnSendto = findViewById(R.id.btn_sendto);
        mEtIpAddr = findViewById(R.id.et_ip_addr);
        mEtPort = findViewById(R.id.et_port);
        mEtSendLen = findViewById(R.id.et_send_data_len);
        mEtSendData = findViewById(R.id.et_send_data);
        mTvRecvData = findViewById(R.id.tv_recv_data);
        mTvRecvLen = findViewById(R.id.tv_recv_len);
        mCbSendLooper = findViewById(R.id.cb_send_looper);

        mBtnSendto.setOnClickListener(this);

        mHandler = new UdpHandler(this);
        mUdpOperator = UdpOperator.getInstance();
        mUdpOperator.init(mHandler);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_sendto) {
            sendDataBySendTo();
        }
    }

    private void sendDataBySendTo() {
        mUdpOperator.sendDataBySendto(mEtIpAddr.getText().toString(),
                Integer.valueOf(mEtPort.getText().toString()).intValue(),
                mEtSendData.getText().toString(),
                Integer.valueOf(mEtSendLen.getText().toString()).intValue(),
                mCbSendLooper.isChecked());
    }

    public void showRecvData(UdpMessageObj messageObj) {
        if (messageObj == null) {
            Log.i(TAG, "showRecvData failed, messageObj is null");
            return;
        }
        int msgLen = messageObj.data.length();
        StringBuilder stringBuilder = new StringBuilder();
        if (msgLen < 100) {
            mTvRecvData.setText(messageObj.data);
        } else {
            stringBuilder.append(messageObj.data.substring(0, 5));
            stringBuilder.append(messageObj.data.substring(msgLen - 5, msgLen));
            mTvRecvData.setText(stringBuilder.toString());
        }

        mTvRecvLen.setText(String.valueOf(messageObj.dataLen));
    }

    private static class UdpHandler extends Handler {

        private WeakReference<UdpTestActivity> mRef;

        public UdpHandler(UdpTestActivity activity) {
            mRef = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SHOW_UDP_RECV_DATA:
                    showRecvData((UdpMessageObj) msg.obj);
                    break;
            }
        }

        private void showRecvData(UdpMessageObj obj) {
            UdpTestActivity activity = mRef.get();
            if (activity == null) {
                Log.i(TAG, "showRecvData: failed, Ref is null");
                return;
            }
            activity.showRecvData(obj);
        }
    }

}