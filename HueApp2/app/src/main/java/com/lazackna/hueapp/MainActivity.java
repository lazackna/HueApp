package com.lazackna.hueapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lazackna.hueapp.Light.ColorLight;
import com.lazackna.hueapp.Light.ColorLightActivity;
import com.lazackna.hueapp.Light.DimmableLight;
import com.lazackna.hueapp.Light.DimmableLightActivity;
import com.lazackna.hueapp.Light.Light;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LightAdapter.OnItemClickListener, LightListFragment.OnFragmentInteractionListener{
    private static final String TAG = MainActivity.class.getName();

    private ArrayList<Light> lightList;
    private LightAdapter lightAdapter;
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;

    private FragmentContainerView fragmentContainer;
    private FragmentManager manager;

    private SwipeRefreshLayout pullToRefresh;

    private String key;
    private String ip;
    private String port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        fragmentContainer = findViewById(R.id.fragmentContainer);
        lightList = new ArrayList<>();
        //recyclerView = findViewById(R.id.main_recyclerView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       // lightAdapter = new LightAdapter(this.lightList, this);
        //recyclerView.setAdapter(lightAdapter);
        manager = getSupportFragmentManager();
        pullToRefresh = findViewById(R.id.main_refresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetLights();
                pullToRefresh.setRefreshing(false);
            }
        });



        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        key = (String) bundle.get("login_key");
        ip = (String) bundle.get("login_ip");
        port = (String) bundle.get("login_port");
        GetLights();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (manager.findFragmentByTag("list") != null) {
            manager.beginTransaction().remove(manager.findFragmentByTag("list"));
        }
        GetLights();
    }

    private void GetLights() {
        final JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, // Use HTTP GET to retrieve the data from the NASA API
                buildLightsUrl(), // This is the actual URL used to retrieve the data
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    // The callback for handling the response
                    public void onResponse(JSONObject response) {
                        //Log.d(LOGTAG, "Volley response: " + response.toString());
                        lightList.clear();
                        try {
                            for(int i = 1; i <= response.length(); i++) {
                                JSONObject root = response.getJSONObject(i + "");
                                String type = root.getString("type");
                                Light light = null;
                                JSONObject j = root.getJSONObject("state");
                                Light.PowerState state = Light.PowerState.OFF;
                                if (j.getString("on").equals("true")) {
                                    state = Light.PowerState.ON;
                                } else if (j.getString("on").equals("false")) {
                                    state = Light.PowerState.OFF;
                                }
                                String uniqueid = root.getString("uniqueid");

                                String name = root.getString("name");
                                switch(type) {
                                    case "Extended color light":
                                        //JSONObject j = root.getJSONObject("state");
                                        JSONArray array = j.getJSONArray("xy");

                                        if (j.getBoolean("on")) state = Light.PowerState.ON;
                                        light = new ColorLight(
                                                i,
                                                uniqueid,
                                                name,
                                                state,
                                                j.getInt("bri"),
                                                j.getInt("hue"),
                                                j.getInt("sat"),
                                                new double[] { array.getDouble(0), array.getDouble(1) });
                                        break;
                                    case "Dimmable light":
                                        light = new DimmableLight(
                                                i,
                                                uniqueid,
                                                name,
                                                j.getInt("bri"),
                                                state);
                                        break;
                                }

                                if (light == null) return;
                                lightList.add(light);
                            }

                            //lightAdapter.notifyDataSetChanged();
                            makeFragment();
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
                        //Log.e("haha", error.getLocalizedMessage());
                        finish();
                        //listener.onPhotoError(new Error(error.getLocalizedMessage()));
                    }
                }
        );
        requestQueue.add(request);
    }

    private void makeFragment() {

        Bundle bundle = new Bundle();
        bundle.putSerializable("lights", lightList);

        this.manager.beginTransaction()
                .setReorderingAllowed(false)
                .add(R.id.fragmentContainer, LightListFragment.class, bundle, "list")
                .commit();

        //manager.beginTransaction().remove(manager.findFragmentByTag("list"));
    }

    private String buildLightsUrl() {
        return "http://" + ip + ":" + port + "/api/" + key + "/lights";
    }

    @Override
    public void onItemClick(int clickedPosition) {
        Log.d(TAG, "clicked item");
        Light light = lightList.get(clickedPosition);
        Intent intent = null;
        if (light instanceof ColorLight) {
            intent = new Intent(this, ColorLightActivity.class);
            intent.putExtra("light", (ColorLight)light);
        } else if (light instanceof DimmableLight) {
            intent = new Intent(this, DimmableLightActivity.class);
            intent.putExtra("light", (DimmableLight)light);
        }
        if (intent != null) {
            intent.putExtra("login_key", key);
            intent.putExtra("login_ip", ip);
            intent.putExtra("login_port", port);
            startActivity(intent);
        }
    }

    @Override
    public void onFragmentInteraction(Light light) {
        Intent intent = null;
        if (light instanceof ColorLight) {
            intent = new Intent(this, ColorLightActivity.class);
            intent.putExtra("light", (ColorLight)light);
        } else if (light instanceof DimmableLight) {
            intent = new Intent(this, DimmableLightActivity.class);
            intent.putExtra("light", (DimmableLight)light);
        }
        if (intent != null) {
            intent.putExtra("login_key", key);
            intent.putExtra("login_ip", ip);
            intent.putExtra("login_port", port);
            startActivity(intent);
        }
    }
//    private void sendToHueBridge() {
//        // Note that the HUE API expects a JSONObject but returns a JSONArray,
//        // hence the use of this custom Volley class that handles this
//        CustomJsonArrayRequest request = new CustomJsonArrayRequest(
//                Request.Method.PUT,
//                buildUrl(),
//                buildBody(),
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.i(TAG, "Response=" + response.toString());
//                        try {
//                            //resultView.setText(response.toString(4));
//                            Log.d(TAG, response.toString());
//                        } catch (Exception exception) {
//                            exception.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d(TAG, "Error=" + error.getMessage());
//                        //resultView.setText(error.getMessage());
//                    }
//                }
//        );
//        Log.i(TAG, "Sending request");
//        requestQueue.add(request);
//    }

//    private String buildUrl() {
//        String host = String.valueOf("192.168.2.10");
//        int portNr = 8000;
//        String url = "http://" + host + ":" + portNr + buildApiString();
//        Log.i(TAG, "URL=" + url);
//        return url;
//    }
//
//    private String buildApiString() {
//        String apiString = "/api/" + userKey.getText() + "/lights/" + lightNr.getText() + "/state";
//        Log.i(TAG, "apiString=" + apiString);
//        return apiString;
//    }
//
//    private JSONObject buildBody() {
//        JSONObject body = new JSONObject();
//        try {
//            body.put("on", true);
//            body.put("hue", hue.getProgress());
//            body.put("sat", saturation.getProgress());
//            body.put("bri", brightness.getProgress());
//        }
//        catch (JSONException exception) {
//            exception.printStackTrace();
//        }
//        Log.i(TAG, "JSON body=" + body.toString());
//        return body;
//    }
}