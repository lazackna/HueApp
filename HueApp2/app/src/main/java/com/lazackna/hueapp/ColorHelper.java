package com.lazackna.hueapp;

import android.graphics.Color;

import androidx.annotation.IntRange;

public class ColorHelper {

    public static int xyToColor(double x, double y, int bri) {
        int[] rgb = xyToRGB(x,y,bri);
        float[] hsv = new float[3];
        Color.RGBToHSV(rgb[0],rgb[1],rgb[2],hsv);
        int color = Color.HSVToColor(hsv);
        return color;
    }

    public static int[] xyToRGB(double x, double y, @IntRange(from = 1, to = 254) int bri) {
        double z = 1.0 - x - y;
        double Y = bri / 255.0; // Brightness of lamp
        double X = (Y / y) * x;
        double Z = (Y / y) * z;
        double r = X * 1.612 - Y * 0.203 - Z * 0.302;
        double g = -X * 0.509 + Y * 1.412 + Z * 0.066;
        double b = X * 0.026 - Y * 0.072 + Z * 0.962;
        r = r <= 0.0031308 ? 12.92 * r : (1.0 + 0.055) * Math.pow(r, (1.0 / 2.4)) - 0.055;
        g = g <= 0.0031308 ? 12.92 * g : (1.0 + 0.055) * Math.pow(g, (1.0 / 2.4)) - 0.055;
        b = b <= 0.0031308 ? 12.92 * b : (1.0 + 0.055) * Math.pow(b, (1.0 / 2.4)) - 0.055;
        double maxValue = Double.max(r,g);
        maxValue = Double.max(maxValue, b);
        r /= maxValue;
        g /= maxValue;
        b /= maxValue;
        r = r * 255;   if (r < 0) { r = 255;}
        g = g * 255;   if (g < 0) { g = 25;}
        b = b * 255;   if (b < 0) { b = 255;}
        @IntRange(from = 0, to = 255) int red = (int) r;
        @IntRange(from = 0, to = 255) int green = (int) g;
        @IntRange(from = 0, to = 255) int blue = (int) b;
        return new int[] {red, green ,blue};
    }
}
