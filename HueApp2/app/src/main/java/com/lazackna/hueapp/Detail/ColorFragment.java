package com.lazackna.hueapp.Detail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lazackna.hueapp.CustomJsonArrayRequest;
import com.lazackna.hueapp.Light.ColorLight;
import com.lazackna.hueapp.Light.ColorLightActivity;
import com.lazackna.hueapp.Light.Light;
import com.lazackna.hueapp.R;
import com.lazackna.hueapp.Util.ColorHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ColorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ColorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = ColorLightActivity.class.getName();

    protected SeekBar hue;
    protected SeekBar saturation;
    protected SeekBar brightness;
    protected Button sendButton;
    private TextView stateView;
    private View colorView;
    private TextView nameView;
    private Switch powerSwitch;

    private RequestQueue requestQueue;
    private ColorLight light;
    private Light.PowerState state;
    private String name;
    private String ip;
    private String port;
    private String key;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ColorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ColorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ColorFragment newInstance(String param1, String param2) {
        ColorFragment fragment = new ColorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_color, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());

        colorView = view.findViewById(R.id.color_color);
        nameView = view.findViewById(R.id.color_name);
        stateView = view.findViewById(R.id.color_on);
        hue = (SeekBar) view.findViewById(R.id.hue);
        saturation = (SeekBar) view.findViewById(R.id.saturation);
        brightness = (SeekBar) view.findViewById(R.id.color_brightness);
        powerSwitch = view.findViewById(R.id.color_powerSwitch);
        sendButton = (Button) view.findViewById(R.id.colorSendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG, "Send button onClick");
                sendToHueBridge();
            }
        });


        key = (String) requireArguments().get("login_key");
        ip = (String) requireArguments().get("login_ip");
        port = (String) requireArguments().get("login_port");
        light = (ColorLight) requireArguments().get("light");
        name = light.name;
        nameView.setText(getString(R.string.name) + " " + name);

        state = light.powerState;
        if (state == Light.PowerState.OFF) {
            stateView.setText(getString(R.string.on) + " OFF");
            powerSwitch.setChecked(false);
        } else if (state == Light.PowerState.ON) {
            stateView.setText(getString(R.string.on) + " ON");
            powerSwitch.setChecked(true);
        }


        hue.setProgress(light.hue);
        saturation.setProgress(light.sat);
        brightness.setProgress(light.bri);
        colorView.setBackgroundColor(ColorHelper.hueToColor(light.hue, light.sat, light.bri));
        return view;
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
                            float hue = Float.parseFloat(r.getString("hue"));
                            float sat = Float.parseFloat(r.getString("sat"));
                            float bri = Float.parseFloat(r.getString("bri"));
                            colorView.setBackgroundColor(ColorHelper.hueToColor(hue,sat,bri));

                            Light.PowerState state = Light.PowerState.OFF;
                            if (r.getString("on").equals("true")) {
                                state = Light.PowerState.ON;
                            } else if (r.getString("on").equals("false")) {
                                state = Light.PowerState.OFF;
                            }
                            if (state == Light.PowerState.OFF) {
                                stateView.setText(getString(R.string.on) + " OFF");
                            } else if (state == Light.PowerState.ON) {
                                stateView.setText(getString(R.string.on) + " ON");
                            }

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
                        Log.d(TAG, "Error=" + "VolleyError");
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

    private String buildApiString() {
        String apiString = "/api/" + key + "/lights/" + light.id + "/state";
        Log.i(TAG, "apiString=" + apiString);
        return apiString;
    }

    private JSONObject buildBody() {
        JSONObject body = new JSONObject();
        try {
            body.put("on", powerSwitch.isChecked());
            body.put("hue", hue.getProgress());
            body.put("sat", saturation.getProgress());
            body.put("bri", brightness.getProgress());
        }
        catch (JSONException exception) {
            exception.printStackTrace();
        }
        Log.i(TAG, "JSON body=" + body.toString());
        return body;
    }
}