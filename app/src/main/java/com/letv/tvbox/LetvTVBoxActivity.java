package com.letv.tvbox;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.widget.FrameLayout;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;
import android.util.Log;

/**
 * 乐视S50 Air 专用TVBOX套壳Activity
 * 适配乐视LetvUI 3.0 (Android 4.4)
 * 底层使用WebView加载TVBOX Web版界面
 */
public class LetvTVBoxActivity extends Activity {

    private static final String TAG = "LetvTVBox";

    private WebView webView;
    private FrameLayout container;
    private TextView loadingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 乐视S50 Air: 1920x1080 全高清屏幕
        container = new FrameLayout(this);
        container.setBackgroundColor(Color.BLACK);

        // 加载提示
        loadingText = new TextView(this);
        loadingText.setText("乐视TVBOX 加载中...");
        loadingText.setTextColor(Color.WHITE);
        loadingText.setTextSize(24);
        loadingText.setGravity(android.view.Gravity.CENTER);
        FrameLayout.LayoutParams loadingParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        loadingParams.gravity = android.view.Gravity.CENTER;
        loadingText.setLayoutParams(loadingParams);
        container.addView(loadingText);

        // 初始化WebView - 兼容Android 4.4
        webView = new WebView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        webView.setLayoutParams(params);

        // 配置WebView兼容低版本Android
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        // Android 4.4 特殊处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Android 4.4+ WebView基于Chromium, 支持更多特性
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        } else {
            // Android 4.3及以下
            settings.setAllowFileAccess(true);
        }

        // 设置TVBOX风格UI
        webView.setWebViewClient(new TVBoxWebViewClient());
        webView.setBackgroundColor(Color.parseColor("#0D1117"));

        container.addView(webView);
        setContentView(container);

        // 加载内置TVBOX界面
        loadTVBoxUI();
    }

    private void loadTVBoxUI() {
        // 加载本地assets中的TVBOX界面
        webView.loadUrl("file:///android_asset/tvbox_index.html");
    }

    private class TVBoxWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // 页面加载完成, 隐藏加载提示
            loadingText.setVisibility(android.view.View.GONE);
            Log.d(TAG, "TVBOX页面加载完成: " + url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                     String description, String failingUrl) {
            Log.e(TAG, "页面加载错误: " + errorCode + " " + description);
            loadingText.setText("加载失败: " + description);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 处理遥控器按键 - 乐视超级遥控器
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                // 将方向键事件传递给WebView
                return super.onKeyDown(keyCode, event);
            case KeyEvent.KEYCODE_MENU:
                // 菜单键打开设置
                openSettings();
                return true;
            case KeyEvent.KEYCODE_BACK:
                if (webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
                return super.onKeyDown(keyCode, event);
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    private void openSettings() {
        android.content.Intent intent = new android.content.Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
