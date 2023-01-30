package com.geek.biz1;

import android.content.Context;
import android.content.Intent;

import java.util.logging.Handler;

/**
 * 此工具类用来重启APP，只是单纯的重启，不做任何处理。
 * <p>
 * Created by 13itch on 2022/8/5.
 */

public class RestartAPPTool {

    /**
     * 重启整个APP
     *
     * @param context
     * @param Delayed 延迟多少毫秒
     */

    public static void restartAPP(Context context, long Delayed) {
        /**开启一个新的服务，用来重启本APP*/
        Intent intent1 = new Intent(context, KillSelfService.class);
        intent1.putExtra("PackageName", context.getPackageName());
        intent1.putExtra("Delayed", Delayed);
        context.startService(intent1);
        /**杀死整个进程**/
//        android.os.Process.killProcess(android.os.Process.myPid());

    }

    /***重启整个APP*/
    public static void restartAPP(Context context) {
        restartAPP(context, 2000);
    }

}