package com.peter.study.audio_video.audio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.peter.study.audio_video.R;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * pengtao add
 */
public class AudioTracker {
    private static final String TAG = "AudioTracker";

    private Context mContext;

    private String mFilePath;

    private int mBufferSizeInBytes = 0;
    // 采样率
    private int SAMPLE_RATE = 44100;
    // 单声道，所有设备支持
    private int CHANNEL = AudioFormat.CHANNEL_OUT_STEREO;
    // 位深16bit，所有设备支持
    private int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    // PCM播放对象
    private AudioTrack mAudioTrack;
    // 状态
    private Status mStatus = Status.STATUS_NO_READY;
    // 单任务线程池
    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    public AudioTracker(Context context) {
        mContext = context;
    }

    /**
     * 创建AudioTack 播放对象
     * @param filepath
     */
    public void createAudioTrack(String filepath) {
        mFilePath = filepath;
        Log.d(TAG, "mFilePath: " + mFilePath);
        mBufferSizeInBytes = AudioTrack.getMinBufferSize(SAMPLE_RATE, CHANNEL, AUDIO_FORMAT);
        if (mBufferSizeInBytes <= 0) {
            throw new IllegalStateException("AudioTrack is not available mBufferSizeInBytes:" + mBufferSizeInBytes);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mAudioTrack = new AudioTrack.Builder()
                    .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setLegacyStreamType(AudioManager.STREAM_MUSIC).build())
                    .setAudioFormat(new AudioFormat.Builder()
                        .setEncoding(AUDIO_FORMAT)
                        .setChannelMask(CHANNEL)
                        .setSampleRate(SAMPLE_RATE).build())
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .setBufferSizeInBytes(mBufferSizeInBytes)
                    .build();
        } else {
            mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, CHANNEL, AUDIO_FORMAT,
                    mBufferSizeInBytes, AudioTrack.MODE_STREAM);
        }
        mStatus = Status.STATUS_READY;
    }

    /**
     * 开始播放
     */
    public void start() {
        if (mStatus == Status.STATUS_NO_READY || mAudioTrack == null) {
            throw new IllegalStateException("播放器尚未初始化");
        }
        if (mStatus == Status.STATUS_START) {
            throw new IllegalStateException("正在播放...");
        }
        Log.d(TAG, "*************start************");
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    playAudioData();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "播放出错", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void playAudioData () throws IOException {
        InputStream dis = null;
        try {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "播放开始", Toast.LENGTH_LONG).show();
                }
            });
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(mFilePath)));
            byte[] bytes = new byte[mBufferSizeInBytes];
            int length;

            if(null != mAudioTrack && mAudioTrack.getState() != AudioTrack.STATE_UNINITIALIZED && mAudioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING)
                mAudioTrack.play();
            // write 是阻塞方法
            while((length = dis.read(bytes)) != -1 && mStatus != Status.STATUS_START) {
                Log.d(TAG, "playAudioData: length = " + length);
                mAudioTrack.write(bytes, 0, length);
            }
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "播放结束", Toast.LENGTH_SHORT).show();
                }
            });
        } finally {
            if (dis != null)
                dis.close();
        }
        mStatus = Status.STATUS_START;
    }

    public void stop() {
        Log.d(TAG, "**************stop*************");
        if(mStatus == Status.STATUS_NO_READY || mStatus == Status.STATUS_READY){
            Log.d(TAG, "播放尚未开始");
        } else {
            mStatus = Status.STATUS_STOP;
            mAudioTrack.stop();
            release();
        }
    }

    public void release() {
        Log.d(TAG, "***************release*************");
        mStatus = Status.STATUS_NO_READY;
        if (mAudioTrack != null) {
            mAudioTrack.release();
            mAudioTrack = null;
        }
    }

    /**
     * 播放对象状态
     */
    public enum Status {
        // 未开始
        STATUS_NO_READY,
        // 预备
        STATUS_READY,
        // 播放
        STATUS_START,
        //停止
        STATUS_STOP,
    }
}
