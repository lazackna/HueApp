package com.lazackna.hueapp.Light;

import org.junit.Test;

import static org.junit.Assert.*;

public class ColorLightTest {

    @Test
    public void brightnessGetterTest() {
        int expectedBri = 1;
        ColorLight colorLight = new ColorLight(1, "uniqueId1", "name", Light.PowerState.OFF, 1, 1, 1, new double[]{2.0, 5.0});
        assertEquals(expectedBri, colorLight.getBri());
    }

    @Test
    public void brightnessAlwaysPositive() {
        int expectedBrightness = 0;
        int realBrightness = -1;
        ColorLight colorLight = new ColorLight(1, "uniqueId1", "name", Light.PowerState.OFF, realBrightness, 1, 1, new double[]{2.0, 5.0});
        assertEquals(expectedBrightness, colorLight.getBri());
    }

    @Test
    public void brightnessBelowMax() {
        int expectedBrightness = 254;
        int realBrightness = 260;
        ColorLight colorLight = new ColorLight(1, "uniqueId1", "name", Light.PowerState.OFF, realBrightness, 1, 1, new double[]{2.0, 5.0});
        assertEquals(expectedBrightness, colorLight.getBri());
    }

    @Test
    public void hueGetterTest() {
        int expectedHue = 254;
        ColorLight colorLight = new ColorLight(1, "uniqueId1", "name", Light.PowerState.OFF, 1, expectedHue, 1, new double[]{2.0, 5.0});
        assertEquals(expectedHue, colorLight.getHue());
    }

    @Test
    public void hueAlwaysPositive() {
        int expectedHue = 0;
        int realHue = -1;
        ColorLight colorLight = new ColorLight(1, "uniqueId1", "name", Light.PowerState.OFF, 1, realHue, 1, new double[]{2.0, 5.0});
        assertEquals(expectedHue, colorLight.getHue());
    }

    @Test
    public void hueBelowMax() {
        int expectedHue = 65535;
        int realHue = 65540;
        ColorLight colorLight = new ColorLight(1, "uniqueId1", "name", Light.PowerState.OFF, 1, realHue, 1, new double[]{2.0, 5.0});
        assertEquals(expectedHue, colorLight.getHue());
    }

    @Test
    public void satGetterTest() {
        int expectedSat = 1;
        ColorLight colorLight = new ColorLight(1, "uniqueId1", "name", Light.PowerState.OFF, 1, 1, 1, new double[]{2.0, 5.0});
        assertEquals(expectedSat, colorLight.getSat());
    }

    @Test
    public void satAlwaysPositive() {
        int expectedSat = 0;
        int realSat = -1;
        ColorLight colorLight = new ColorLight(1, "uniqueId1", "name", Light.PowerState.OFF, 1, 1, realSat, new double[]{2.0, 5.0});
        assertEquals(expectedSat, colorLight.getSat());
    }

    @Test
    public void satBelowMax() {
        int expectedSat = 254;
        int realSat = 256;
        ColorLight colorLight = new ColorLight(1, "uniqueId1", "name", Light.PowerState.OFF, 1, 1, realSat, new double[]{2.0, 5.0});
        assertEquals(expectedSat, colorLight.getSat());
    }

    @Test
    public void xyGetterTest() {
        double[] expectedXY = new double[]{1.0, 1.0};
        ColorLight colorLight = new ColorLight(1, "uniqueId1", "name", Light.PowerState.OFF, 1, 1, 1, new double[]{2.0, 5.0});
       // assertEquals(expectedXY, colorLight.getXy());
        assertEquals(expectedXY[0], colorLight.getXy()[0], 0);
        assertEquals(expectedXY[1], colorLight.getXy()[1], 0);
    }
}