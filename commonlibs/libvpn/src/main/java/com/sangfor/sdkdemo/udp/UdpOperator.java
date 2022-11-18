package com.sangfor.sdkdemo.udp;

import static com.sangfor.sdkdemo.view.UdpTestActivity.MSG_SHOW_UDP_RECV_DATA;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;


public class UdpOperator {
    private static final String TAG = "UdpOperator";
    private static final UdpOperator ourInstance = new UdpOperator();

    public static UdpOperator getInstance() {
        return ourInstance;
    }

    private WeakReference<Handler> mHandlerRef;

    private UdpOperator() {
    }

    public void init(Handler handler)
    {
        mHandlerRef = new WeakReference<>(handler);
    }

    public void sendDataBySendto(String ip, int port, String data, int dataLen, boolean looper)
    {
        nativeSendTo(ip, port, data, dataLen, looper);
    }

    public void notifyRecvData(String ip, int port, String data, int dataLen)
    {
        UdpMessageObj messageObj = new UdpMessageObj();
        messageObj.ip = ip;
        messageObj.port = port;
        messageObj.data = data;
        messageObj.dataLen = dataLen;
        if (mHandlerRef == null)
        {
            Log.i(TAG, "notifyRecvData failed, not init yet");
            return;
        }
        Handler handler = mHandlerRef.get();
        if (handler == null)
        {
            Log.i(TAG, "notifyRecvData failed, ref is null");
            return;
        }
        Message msg = new Message();
        msg.what = MSG_SHOW_UDP_RECV_DATA;
        msg.obj = messageObj;
        handler.sendMessage(msg);
    }

    public static void recvfromCallback(String ip, int port, String data, int dataLen)
    {
        UdpOperator.getInstance().notifyRecvData(ip, port, data, dataLen);
    }

    public native static void nativeSendTo(String ip, int port, String data, int dataLen, boolean looper);


}
