package com.geek.appcommon.huyan;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.blankj.utilcode.util.AppUtils;
import com.geek.common.R;
import com.geek.libutils.app.BaseApp;

public class HuyanservicesBg extends Service {

    public static final String EXTRA_NOTIFICATION_ID = "extra_notification_id_for_huyan";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(intent.getIntExtra(EXTRA_NOTIFICATION_ID, 1), she_notifichanged2());
        stopForeground(true);
        stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }

    public static final String Huyan_CHANNEL_ID = "PHONE_NOTIFY_ID";
    public static final String Huyan_CHANNEL_NAME = "PHONE_NOTIFY_NAME";

    public static Notification she_notifichanged() {
        NotificationManager notificationManager = (NotificationManager) BaseApp.get().getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(Huyan_CHANNEL_ID, Huyan_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
            notification = new NotificationCompat.Builder(BaseApp.get(), Huyan_CHANNEL_ID)
                    .setChannelId(Huyan_CHANNEL_ID)
                    .build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(BaseApp.get(), Huyan_CHANNEL_ID)
                    .setOngoing(true)
                    .setChannelId(Huyan_CHANNEL_ID);//无效
            notification = notificationBuilder.build();
        }
        return notification;
    }

    public  Notification she_notifichanged2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(Huyan_CHANNEL_ID, "DTXZ Foreground Service Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
        Intent notificationIntent = new Intent();
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 123, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 123, notificationIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        }
        Notification notification = new NotificationCompat.Builder(this, Huyan_CHANNEL_ID).setContentTitle(AppUtils.getAppName()).setSmallIcon(R.mipmap.logo).setContentIntent(pendingIntent).build();
        return notification;
    }
}
