package com.lazackna.hueapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.os.Looper;

import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;



/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest{



    @Test
    public void testWithGoodWeather() {
        ActivityScenario.launch(LoginActivity.class).onActivity(activity ->{
            String name = "tester";
            String ip = "192.168.2.12";
            String port = "8000";


            boolean result = activity.infoValid(name, ip, port);

            assertTrue(result);
        });
    }

    @Test
    public void testWithBadWeather() {
        ActivityScenario.launch(LoginActivity.class).onActivity(activity -> {

            String name = "tester";
            String ip = "192.168.2.12";
            String port = "hA";

            boolean result = activity.infoValid(name, ip, port);

            assertFalse(result);

            ip = "102a.172.3.33a";

            result = activity.infoValid(name,ip,port);

            assertFalse(result);
        });
    }
}