package com.letv.tvbox;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * 乐视TVBOX 设置页面
 * 适配乐视S50 Air 遥控器操作 + Android 4.4
 */
public class SettingsActivity extends Activity {

    private static final String TAG = "LetvSettings";

    private EditText etApiUrl;
    private EditText etPlayerType;
    private EditText etEpgUrl;
    private Button btnSave;
    private Button btnCancel;
    private android.widget.CheckBox cbBoot;
    private SharedPreferences prefs;

    // 预置接口源列表
    private static final String[] DEFAULT_APIS = {
            "http://www.fantaiying.com/tv/",
            "http://cdn.qiaoji8.com/tvbox.json",
            "http://drpy.site/js1",
            "https://yydsys.top/duo"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("tvbox_prefs", MODE_PRIVATE);

        etApiUrl = (EditText) findViewById(R.id.etApiUrl);
        etPlayerType = (EditText) findViewById(R.id.etPlayerType);
        etEpgUrl = (EditText) findViewById(R.id.etEpgUrl);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        cbBoot = (android.widget.CheckBox) findViewById(R.id.cbBoot);

        // 恢复开机自启设置
        cbBoot.setChecked(prefs.getBoolean("boot_enabled", true));

        // 加载已保存的设置
        String savedApi = prefs.getString("api_url", "");
        if (savedApi.length() == 0) {
            // 默认使用第一个接口
            savedApi = DEFAULT_APIS[0];
        }
        etApiUrl.setText(savedApi);
        etPlayerType.setText(prefs.getString("player_type", "hard"));
        etEpgUrl.setText(prefs.getString("epg_url", "https://epg.112114.xyz/"));

        btnSave.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                saveSettings();
            }
        });

        btnCancel.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                finish();
            }
        });

        Log.d(TAG, "Settings activity created");
    }

    private void saveSettings() {
        String apiUrl = etApiUrl.getText().toString().trim();
        String playerType = etPlayerType.getText().toString().trim();
        String epgUrl = etEpgUrl.getText().toString().trim();

        if (apiUrl.length() == 0) {
            Toast.makeText(this, "请输入接口地址", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("api_url", apiUrl);
        editor.putString("player_type", playerType);
        editor.putString("epg_url", epgUrl);
        editor.putBoolean("boot_enabled", cbBoot.isChecked());
        editor.commit(); // 使用commit()而非apply(), 确保同步写入

        Toast.makeText(this, "设置已保存", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Settings saved: api=" + apiUrl);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
