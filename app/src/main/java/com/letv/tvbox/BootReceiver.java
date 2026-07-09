package com.letv.tvbox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 开机自启广播接收器
 * 适配乐视S50 Air 开机启动
 * 兼容 Android 4.4
 */
public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "LetvBootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "Received broadcast: " + action);

        if (Intent.ACTION_BOOT_COMPLETED.equals(action) ||
            Intent.ACTION_USER_PRESENT.equals(action)) {

            // 检查用户是否禁用了开机自启
            android.content.SharedPreferences prefs =
                    context.getSharedPreferences("tvbox_prefs", Context.MODE_PRIVATE);
            boolean bootEnabled = prefs.getBoolean("boot_enabled", true);

            if (bootEnabled) {
                Intent serviceIntent = new Intent(context, LetvTVBoxActivity.class);
                serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(serviceIntent);
                Log.d(TAG, "LetvTVBOX auto-started");
            } else {
                Log.d(TAG, "Boot auto-start is disabled");
            }
        }
    }
}
