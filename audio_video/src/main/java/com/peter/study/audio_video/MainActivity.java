package com.peter.study.audio_video;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.peter.study.audio_video.audio.AudioPlayActivity;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("audiovideo");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void audio_test(View view) {
        startActivity(new Intent(this, AudioPlayActivity.class));
    }

    public void video_test(View view) {
    }
}