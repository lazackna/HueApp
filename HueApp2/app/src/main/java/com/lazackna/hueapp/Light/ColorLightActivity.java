package com.lazackna.hueapp.Light;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lazackna.hueapp.LightAdapter;
import com.lazackna.hueapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ColorLightActivity extends AppCompatActivity {
    private static final String TAG = ColorLightActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}