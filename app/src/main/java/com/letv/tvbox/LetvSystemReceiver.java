package com.letv.tvbox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 乐视系统广播接收器
 * 监听网络变化等系统事件
 * 兼容 Android 4.4
 */
public class LetvSystemReceiver extends BroadcastReceiver {

    private static final String TAG = "LetvSystemReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "System event: " + action);

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            // 兼容Android 4.4的网络变化监听
            NetworkInfo networkInfo = intent.getParcelableExtra(
                    ConnectivityManager.EXTRA_NETWORK_INFO);
            if (networkInfo != null && networkInfo.isConnected()) {
                Log.d(TAG, "Network connected: " + networkInfo.getTypeName());
            } else {
                Log.d(TAG, "Network disconnected");
            }
        }
    }
}
