package com.lazackna.hueapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = LoginActivity.class.getName();

    private EditText nameText;
    private EditText ipText;
    private EditText portText;
    private Button loginButton;
    private Button loginAutoButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nameText = findViewById(R.id.login_name);
        loginButton = findViewById(R.id.loginButton);
        loginAutoButton = findViewById(R.id.loginAuto);
        ipText = findViewById(R.id.login_hostIP);
        portText = findViewById(R.id.login_port);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = ipText.getText().toString();
                String name = nameText.getText().toString();
                String port = portText.getText().toString();
                if (!infoValid(name,port,ip)) return;


                login();
            }
        });
        loginAutoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(Settings.PREFERENCES, MODE_PRIVATE);
                String name = sharedPreferences.getString(Settings.SELECTEDUSER, "");
                String ip = sharedPreferences.getString(Settings.SELECTEDIP, "");
                String key = sharedPreferences.getString(Settings.SELECTEDBRIDGE, "");
                String port = sharedPreferences.getString(Settings.SELECTEDPORT, "80");
                if (infoValid(name, port, ip) && key != null && !key.equals("")) {
                    nameText.setText(name);
                    ipText.setText(ip);

                    openMainActivity(key, ip, port);
                }
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(Settings.PREFERENCES, MODE_PRIVATE);
        String port = sharedPreferences.getString(Settings.SELECTEDPORT, "80");
        portText.setText(port);
    }
    private RequestQueue requestQueue;
    private void login() {
        requestQueue = Volley.newRequestQueue(this);
        String requestString = buildLoginString(nameText.getText().toString());
        CustomJsonArrayRequest request = new CustomJsonArrayRequest(
                Request.Method.POST,
                buildUrlString(ipText.getText().toString(), portText.getText().toString()),
                buildLoginObject(nameText.getText().toString()),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject root = response.getJSONObject(0);
                            JSONObject succes = root.getJSONObject("success");
                            String key = succes.getString("username");
                            openMainActivity(key, ipText.getText().toString(), portText.getText().toString());
                        } catch (JSONException exception) {
                            exception.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error=" + error.getMessage());
                        //resultView.setText(error.getMessage());
                    }
                }
        );
        requestQueue.add(request);
        //Intent intent = new Intent(this, MainActivity.class);
    }

    private void openMainActivity(String key, String ip, String port) {
        //Log.d(TAG, Light.hueToHex(0.346, 0.3568, 254));
        SharedPreferences.Editor editor = getSharedPreferences(Settings.PREFERENCES, MODE_PRIVATE).edit();
        editor.putString(Settings.SELECTEDBRIDGE, key);
        editor.putString(Settings.SELECTEDUSER, nameText.getText().toString());
        editor.putString(Settings.SELECTEDIP, ipText.getText().toString());
        editor.putString(Settings.SELECTEDPORT, portText.getText().toString());
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("login_key", key);
        intent.putExtra("login_ip", ip);
        intent.putExtra("login_port", port);
        startActivity(intent);
    }

    private String buildLoginString(String name) {
        return "{\n" +
                "    \"devicetype\":\"HueApp#" + name +"\"\n" +
                "}";
    }

    private String buildUrlString(String ip, String port) {
        return "http://" + ip + ":" + port + "/api";
    }

    private JSONObject buildLoginObject(String deviceName) {
        JSONObject body = new JSONObject();
        try {
            body.put("devicetype", deviceName);
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
        return body;
    }

    public boolean infoValid(String name, String port, String ip) {
        String portRegex = "^(0|[1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
        String ipRegex = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$";
        return name != null && !name.equals("")
                && port != null && !port.equals("") && port.matches(portRegex)
                && ip != null && !ip.equals("") && ip.matches(ipRegex);
    }


}
