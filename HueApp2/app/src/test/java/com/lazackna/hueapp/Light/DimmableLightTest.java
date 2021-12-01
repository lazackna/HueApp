package com.lazackna.hueapp.Light;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DimmableLightTest {

    @Test
    public void brightnessGetterTest() {
        int expectedBri = 1;
        DimmableLight colorLight = new DimmableLight(1, "id", "name", expectedBri, Light.PowerState.OFF);
        assertEquals(expectedBri, colorLight.getBri());
    }

    @Test
    public void brightnessAlwaysPositive() {
        int expectedBrightness = 0;
        int realBrightness = -1;
        DimmableLight colorLight = new DimmableLight(1, "id", "name", realBrightness, Light.PowerState.OFF);
        assertEquals(expectedBrightness, colorLight.getBri());
    }

    @Test
    public void brightnessBelowMax() {
        int expectedBrightness = 254;
        int realBrightness = 260;
        DimmableLight colorLight = new DimmableLight(1, "id", "name", realBrightness, Light.PowerState.OFF);
        assertEquals(expectedBrightness, colorLight.getBri());
    }
}