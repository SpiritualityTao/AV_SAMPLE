package com.peter.ffmpegstudy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvPrint;

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvPrint = findViewById(R.id.tvPrint);
        tvPrint.setText(printFFmpegVersion());
    }

    public native String printFFmpegVersion();
}
