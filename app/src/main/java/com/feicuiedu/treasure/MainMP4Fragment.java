package com.feicuiedu.treasure;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.feicuiedu.treasure.commons.ActivityUtils;
import com.feicuiedu.treasure.user.login.LoginActivity;
import com.feicuiedu.treasure.user.register.RegisterActivity;

import java.io.FileDescriptor;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainMP4Fragment extends Fragment implements TextureView.SurfaceTextureListener {

    private TextureView textureView;// 用来播放视频的view
    private MediaPlayer mediaPlayer;
    private ActivityUtils activityUtils;

    @Nullable
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        textureView = new TextureView(getContext());
        return textureView;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activityUtils = new ActivityUtils(this);
        textureView.setSurfaceTextureListener(this);
    }

    // 的确准备好了
    @Override public void onSurfaceTextureAvailable(final SurfaceTexture surface, int width, int height) {
        try {
            AssetFileDescriptor afd = getContext().getAssets().openFd("welcome.mp4");
            FileDescriptor fd = afd.getFileDescriptor();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(fd, afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepareAsync(); // 异步
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override public void onPrepared(MediaPlayer mp) {
                    Surface mpSurface = new Surface(surface);
                    mediaPlayer.setSurface(mpSurface);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.seekTo(0);
                    mediaPlayer.start();
                }
            });
        } catch (IOException e) {
            activityUtils.showToast("媒体播放失败");
        }
    }

    @Override public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static class MainActivity extends AppCompatActivity {



        private ActivityUtils activityUtils;

        public static final String ACTION_ENTER_HOME = "action.enter.home";

        // 广播接收器(当登陆和注册成功后，将发送出广播)
        // 接收到后，关闭当前页面
        private final BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            activityUtils = new ActivityUtils(this);
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
            // 注册本地广播接收器
            IntentFilter intentFilter = new IntentFilter(ACTION_ENTER_HOME);
            LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);
        }

        @OnClick({R.id.btn_Login, R.id.btn_Register})
        public void click(View view) {
            switch (view.getId()) {
                case R.id.btn_Login:
                    activityUtils.startActivity(LoginActivity.class);
                    break;
                case R.id.btn_Register:
                    activityUtils.startActivity(RegisterActivity.class);
                    break;
            }
        }
    }
}