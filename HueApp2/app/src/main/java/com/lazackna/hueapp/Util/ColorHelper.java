package com.lazackna.hueapp.Util;

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

    public static int hueToColor(float hue, float sat, float val) {

        float red = 0;
        float green = 0;
        float blue = 0;

        if (sat == 0) {
            red = green = blue = val;
        }
        if (sat == 0) return 0;
        hue %= 360;
        float Hi = hue / 60;
        float f = (hue / 60) - Hi;
        float p = val * (1 - sat);
        float q = val * (1 - f * sat);
        float t = val * (1 - (1 - f) * sat);

        switch ((int)Hi) {
            case 0:
                return rgb(val, t, p);
            case 1:
                return rgb(q, val, p);
            case 2:
                return rgb(p, val, t);
            case 3:
                return rgb(p, q, val);
            case 4:
                return rgb(t, p, val);
            case 5:
                return rgb(val, p, q);
            default: return 0;
        }

//        hue %= 360;
//        sat = Math.min(sat, 100) / 100.0f;
//        val = Math.min(val, 100) / 100.0f;
//
//        int h = (int)(hue / 60);
//        float f = h - h;
//        float c = val * sat;
//        float x = c * (1 - (float)Math.abs(((hue / 60.0) % 2 - 1)));
//        float m = (float)val - c;
//
//        c += m;
//        x += m;
//
//        switch (h) {
//            case 0:
//                return Color.rgb(c, x, m);
//            case 1:
//                return Color.rgb(x, c, m);
//            case 2:
//                return Color.rgb(m, c, x);
//            case 3:
//                return Color.rgb(m, x, c);
//            case 4:
//                return Color.rgb(x, m, c);
//            case 5:
//                return Color.rgb(c, m, x);
//            default: return 0;
//        }
//        case 0: return Color.rgb(c, x, m);
//        case 1: return Color.rgb(x, c, m);
//        case 2: result = com.lazackna.hueapp.Util.Color.fromDoubleRgb(m, c, x); break;
//        case 3: result = com.lazackna.hueapp.Util.Color.fromDoubleRgb(m, x, c); break;
//        case 4: result = com.lazackna.hueapp.Util.Color.fromDoubleRgb(x, m, c); break;
//        case 5: result = com.lazackna.hueapp.Util.Color.fromDoubleRgb(c, m, x); break;

    }

    private static int rgb(float r, float g, float b) {
        return Color.rgb((int) (r * 255), (int) (g * 255), (int) (b * 255));
    }

}
