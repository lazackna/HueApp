package com.lazackna.hueapp.Light;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.lazackna.hueapp.R;

public class DimmableLightActivity extends AppCompatActivity {
    private static final String TAG = DimmableLightActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}