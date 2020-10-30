package com.peter.study.audio_video.audio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.peter.study.audio_video.R;
import com.peter.study.audio_video.Utils;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class AudioPlayActivity extends AppCompatActivity {

    private static final String TAG = "AudioPlayActivity";


    private int REQUEST_CODE_PERMISSION_STORAGE = 100;

    /**
     * PCM 播放实例
     */
    private AudioTracker mAudioTracker;

    /**
     * 播放 path
     */
    private final String PATH = Environment.getExternalStorageDirectory() + "/_test.pcm";
    /**
     * 采集
     */
    private AudioRecordDemo mAudioRecordDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_play);

        verifyStoragePermissions();

        mAudioTracker = new AudioTracker(this);

        mAudioRecordDemo = new AudioRecordDemo();

        mAudioRecordDemo.setOnAudioFrameCapturedListener(new AudioRecordDemo.OnAudioFrameCapturedListener() {
            @Override
            public void onAudioFrameCaptured(byte[] audioData) {
                Utils.writePCM(audioData);
            }
        });
    }

    //动态获取内存存储权限
    public void verifyStoragePermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            Log.d(TAG, "verifyStoragePermissions: sdk :" + Build.VERSION.SDK_INT);
            String[] permissions = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
            };

            for (String str : permissions) {
                if (ContextCompat.checkSelfPermission(this, str) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION_STORAGE);
                    return;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permisson Granted", Toast.LENGTH_LONG).show();
            } else {
                // Permission Denied
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void playPCM(View view) {
        mAudioTracker.createAudioTrack(PATH);

        mAudioTracker.start();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mAudioTracker.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAudioTracker.release();
        nativeStopPcm();

    }

    public void stopRecord(View view) {
        mAudioRecordDemo.stopCapture();
    }

    public void playRecord(View view) {
        mAudioRecordDemo.startCapture();
    }

    public void OpenSL_Play_PCM(View view) {
        nativePlayPcm(PATH);
    }

    public void OpenSL_Stop_PCM(View view) {
        nativeStopPcm();
    }

    private native void nativePlayPcm(String pcmPath);
    private native void nativeStopPcm();
}
