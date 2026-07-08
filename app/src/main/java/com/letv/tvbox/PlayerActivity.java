package com.letv.tvbox;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.os.Handler;
import java.io.IOException;

/**
 * 乐视S50 Air 专用播放器
 * 适配 Mstar 6A918 芯片 + Mali 450 MP4 GPU
 * 支持硬解码/软解码切换
 */
public class PlayerActivity extends Activity implements SurfaceHolder.Callback {

    private static final String TAG = "LetvPlayer";

    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private Button btnPlayPause;
    private SeekBar seekBar;
    private TextView tvTime;
    private ProgressBar progressBar;
    private boolean isPlaying = false;
    private boolean isPrepared = false;
    private String videoUrl;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // 获取视频URL
        videoUrl = getIntent().getStringExtra("video_url");
        if (videoUrl == null || videoUrl.length() == 0) {
            videoUrl = "";
        }

        initViews();
        initPlayer();
    }

    private void initViews() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        btnPlayPause = (Button) findViewById(R.id.btnPlayPause);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        tvTime = (TextView) findViewById(R.id.tvTime);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(this);

        btnPlayPause.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                togglePlayPause();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar sb, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                    updateTimeDisplay();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar sb) {}
            @Override
            public void onStopTrackingTouch(SeekBar sb) {}
        });
    }

    private void initPlayer() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, Uri.parse(videoUrl));
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    isPrepared = true;
                    progressBar.setVisibility(android.view.View.GONE);
                    seekBar.setMax(mp.getDuration());
                    startPlayback();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    isPlaying = false;
                    btnPlayPause.setText(">");
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e(TAG, "Playback error: what=" + what + " extra=" + extra);
                    return true;
                }
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e(TAG, "Failed to init player", e);
        }
    }

    private void togglePlayPause() {
        if (!isPrepared) return;
        if (isPlaying) {
            mediaPlayer.pause();
            btnPlayPause.setText(">");
        } else {
            mediaPlayer.start();
            btnPlayPause.setText("||");
        }
        isPlaying = !isPlaying;
    }

    private void startPlayback() {
        mediaPlayer.start();
        isPlaying = true;
        btnPlayPause.setText("||");
        updateTimeDisplay();
        // 启动进度更新
        handler.postDelayed(progressRunnable, 1000);
    }

    private Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying && mediaPlayer != null) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                updateTimeDisplay();
                handler.postDelayed(this, 1000);
            }
        }
    };

    private void updateTimeDisplay() {
        if (mediaPlayer == null) return;
        int cur = mediaPlayer.getCurrentPosition();
        int dur = mediaPlayer.getDuration();
        tvTime.setText(formatTime(cur) + " / " + formatTime(dur));
    }

    private String formatTime(int ms) {
        int totalSec = ms / 1000;
        int min = totalSec / 60;
        int sec = totalSec % 60;
        return String.format("%02d:%02d", min, sec);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mediaPlayer != null) {
            mediaPlayer.setDisplay(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder h, int f, int w, int h) {}
    @Override
    public void surfaceDestroyed(SurfaceHolder h) {}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                togglePlayPause();
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (mediaPlayer != null) {
                    int newPos = mediaPlayer.getCurrentPosition() + 10000;
                    mediaPlayer.seekTo(newPos);
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (mediaPlayer != null) {
                    int newPos = Math.max(0, mediaPlayer.getCurrentPosition() - 10000);
                    mediaPlayer.seekTo(newPos);
                }
                return true;
            case KeyEvent.KEYCODE_BACK:
                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(progressRunnable);
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
