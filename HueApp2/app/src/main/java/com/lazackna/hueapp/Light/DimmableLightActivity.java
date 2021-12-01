package com.lazackna.hueapp.Light;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lazackna.hueapp.CustomJsonArrayRequest;
import com.lazackna.hueapp.R;
import com.lazackna.hueapp.Util.ColorHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DimmableLightActivity extends AppCompatActivity {
    private static final String TAG = DimmableLightActivity.class.getName();

    protected SeekBar brightness;
    protected Button sendButton;
    private View view;
    private TextView stateView;
    private TextView nameView;

    private RequestQueue requestQueue;
    private DimmableLight light;
    private Light.PowerState state;
    private String name;
    private String ip;
    private String port;
    private String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dimmablelight);
        view = findViewById(R.id.dimmable_light);
        stateView = findViewById(R.id.dimmable_state);
        nameView = findViewById(R.id.dimmable_name);
        requestQueue = Volley.newRequestQueue(this);
        brightness = (SeekBar) findViewById(R.id.dimmablebri);
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

        name = light.name;
        nameView.setText(getString(R.string.name) + " " + name);

        state = light.powerState;
        if (state == Light.PowerState.OFF) {
            stateView.setText(getString(R.string.on) + " OFF");
        } else if (state == Light.PowerState.ON) {
            stateView.setText(getString(R.string.on) + " ON");
        }

        view.setBackgroundColor(ColorHelper.briToColor(light.bri));
        brightness.setProgress(light.bri);
    }

    private void updateLight() {
        final JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, // Use HTTP GET to retrieve the data from the NASA API
                buildLightUrl(), // This is the actual URL used to retrieve the data
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    // The callback for handling the response
                    public void onResponse(JSONObject response) {
                        //Log.d(LOGTAG, "Volley response: " + response.toString());
                        try {
                            JSONObject r = response.getJSONObject("state");
                            float bri = Float.parseFloat(r.getString("bri"));
                            Light.PowerState state = Light.PowerState.valueOf(r.getString("on"));
                            if (state == Light.PowerState.OFF) {
                                stateView.setText(getString(R.string.on) + " OFF");
                            } else if (state == Light.PowerState.ON) {
                                stateView.setText(getString(R.string.on) + " ON");
                            }
                            view.setBackgroundColor(ColorHelper.briToColor(bri));

                        } catch (Exception exception) {
                            // Make sure to handle any errors, at least provide a log entry
                            Log.d("haha", "test");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // The callback for handling a transmission error
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Log.e("haha", "Volley Error");
                        //finish();
                        //listener.onPhotoError(new Error(error.getLocalizedMessage()));
                    }
                }
        );
        requestQueue.add(request);
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
                            updateLight();
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

    private void powerToBridge() {
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
                            updateLight();
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

    private String buildLightUrl() {
        String host = String.valueOf(ip);
        int portNr = Integer.parseInt(String.valueOf(port));
        String url = "http://" + host + ":" + portNr + buildLightApiString();
        Log.i(TAG, "URL=" + url);
        return url;
    }

    private String buildLightApiString() {
        String apiString = "/api/" + key + "/lights/" + light.id;
        Log.i(TAG, "apiString=" + apiString);
        return apiString;
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