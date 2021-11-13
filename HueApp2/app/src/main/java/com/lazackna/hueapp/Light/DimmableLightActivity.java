package com.lazackna.hueapp.Light;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.lazackna.hueapp.CustomJsonArrayRequest;
import com.lazackna.hueapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DimmableLightActivity extends AppCompatActivity {
    private static final String TAG = DimmableLightActivity.class.getName();

    protected SeekBar brightness;
    protected Button sendButton;

    private RequestQueue requestQueue;
    private DimmableLight light;
    private String ip;
    private String port;
    private String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dimmablelight);

        requestQueue = Volley.newRequestQueue(this);
        brightness = (SeekBar) findViewById(R.id.dimmable_brightness);
        sendButton = (Button) findViewById(R.id.dimmableSendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG, "Send button onClick");
                sendToHueBridge();
            }
        });

        Bundle bundle = getIntent().getExtras();
        key = (String) bundle.get("login_key");
        ip = (String) bundle.get("login_ip");
        port = (String) bundle.get("login_port");
        light = (DimmableLight) bundle.get("light");
        brightness.setProgress(light.bri);
    }

    private void sendToHueBridge() {
        // Note that the HUE API expects a JSONObject but returns a JSONArray,
        // hence the use of this custom Volley class that handles this
        CustomJsonArrayRequest request = new CustomJsonArrayRequest(
                Request.Method.PUT,
                buildUrl(),
                buildBody(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "Response=" + response.toString());
                        try {
                            //resultView.setText(response.toString(4));
                            String temp = response.toString(4);
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
        Log.i(TAG, "Sending request");
        requestQueue.add(request);
    }

    private String buildUrl() {
        String host = String.valueOf(ip);
        int portNr = Integer.parseInt(String.valueOf(port));
        String url = "http://" + host + ":" + portNr + buildApiString();
        Log.i(TAG, "URL=" + url);
        return url;
    }

    private String buildApiString() {
        String apiString = "/api/" + key + "/lights/" + light.id + "/state";
        Log.i(TAG, "apiString=" + apiString);
        return apiString;
    }

    private JSONObject buildBody() {
        JSONObject body = new JSONObject();
        try {
            body.put("on", true);
            body.put("bri", brightness.getProgress());
        }
        catch (JSONException exception) {
            exception.printStackTrace();
        }
        Log.i(TAG, "JSON body=" + body.toString());
        return body;
    }
}