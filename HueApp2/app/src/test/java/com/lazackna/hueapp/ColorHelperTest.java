package com.lazackna.hueapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ColorHelperTest {
    @Test
    public void xyToRGB_Correct() {
        int[] rgb = ColorHelper.xyToRGB(0.5015, 0.3530, 254);
        int[] expected = new int[] {255,160,122};
        assertArrayEquals(rgb, expected);
    }
}