package com.geek.appcommon.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

/***
 * 该服务只用来让APP重启，生命周期也仅仅是只是重启APP。重启完即自我杀死

 */

public class KillSelfService extends Service {

    /**
     * 关闭应用后多久重新启动
     */
    private static long stopDelayed = 2000;
    private Handler handler;
    private String PackageName;

    public KillSelfService() {
        handler = new Handler(Looper.getMainLooper());
//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        },1000);
    }

    @Override

    public int onStartCommand(final Intent intent, int flags, int startId) {
        stopDelayed = intent.getLongExtra("Delayed", 2000);
        PackageName = intent.getStringExtra("PackageName");
        handler.postDelayed(() -> {
//            Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(PackageName);
//            startActivity(LaunchIntent);
//            Toast.makeText(this,"已切换，生效中", Toast.LENGTH_LONG);
            //
            Intent intent1 = getPackageManager().getLaunchIntentForPackage(getPackageName());
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent1);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            //
            KillSelfService.this.stopSelf();
        }, stopDelayed);
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

}