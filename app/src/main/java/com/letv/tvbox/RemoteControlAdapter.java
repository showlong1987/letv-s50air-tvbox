package com.letv.tvbox;

import android.content.Context;
import android.view.KeyEvent;
import android.view.InputDevice;
import android.util.Log;

/**
 * 乐视超级遥控器适配器
 * 适配乐视S50 Air的超级遥控器(带充电线和锂电池)
 * 支持体感操控、语音搜索等特性
 */
public class RemoteControlAdapter {

    private static final String TAG = "LetvRemoteAdapter";
    private Context context;

    public RemoteControlAdapter(Context ctx) {
        this.context = ctx;
    }

    /**
     * 处理乐视超级遥控器的按键事件
     * 乐视S50 Air支持: 方向键、确认、返回、菜单、音量、频道切换
     */
    public boolean handleKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        int action = event.getAction();

        if (action != KeyEvent.ACTION_DOWN) {
            return false;
        }

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                Log.d(TAG, "遥控器: 上方向键");
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                Log.d(TAG, "遥控器: 下方向键");
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                Log.d(TAG, "遥控器: 左方向键");
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Log.d(TAG, "遥控器: 右方向键");
                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                Log.d(TAG, "遥控器: 确认键(OK)");
                return true;
            case KeyEvent.KEYCODE_BACK:
                Log.d(TAG, "遥控器: 返回键");
                return false; // 让系统处理返回
            case KeyEvent.KEYCODE_MENU:
                Log.d(TAG, "遥控器: 菜单键");
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                Log.d(TAG, "遥控器: 音量+");
                return false; // 系统处理音量
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                Log.d(TAG, "遥控器: 音量-");
                return false;
            case KeyEvent.KEYCODE_CHANNEL_UP:
                Log.d(TAG, "遥控器: 频道+");
                return true;
            case KeyEvent.KEYCODE_CHANNEL_DOWN:
                Log.d(TAG, "遥控器: 频道-");
                return true;
            case KeyEvent.KEYCODE_SEARCH:
                Log.d(TAG, "遥控器: 搜索键");
                return true;
            default:
                return false;
        }
    }

    /**
     * 检测是否为乐视超级遥控器
     */
    public static boolean isLetvRemote(KeyEvent event) {
        InputDevice device = event.getDevice();
        if (device != null) {
            String name = device.getName();
            return name != null && (name.contains("Letv") || name.contains("乐视") || name.contains("Remote"));
        }
        return false;
    }
}
