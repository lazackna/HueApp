package com.lazackna.hueapp.Util;

import androidx.annotation.IntRange;

public class Color {

    public @IntRange(from = 0, to = 255) int r;
    public @IntRange(from = 0, to = 255) int g;
    public @IntRange(from = 0, to = 255) int b;

    public double h;
    public double s;
    public double v;

    public Color(int r, int g, int b) {
        this.r = r % 256;
        this.g = g % 256;
        this.b = b % 256;
    }

    public Color(double h, double s, double v) {
        this.h = h;
        this.s = s;
        this.v = v;
    }



    public static Color fromRgbInt(int rgb) {
        return new Color(
                (rgb >> 16) & 0xFF,
                (rgb >> 8) & 0xFF,
                rgb & 0xFF
        );
    }

    private static Color fromFloatRgb(float r, float g, float b) {
        return new Color(
                Math.round(r * 255f),
                Math.round(g * 255f),
                Math.round(b * 255f)
        );
    }

    private static Color fromDoubleRgb(double r, double g, double b) {
        return new Color(
                (int)Math.round(r * 255.0),
                (int)Math.round(g * 255.0),
                (int)Math.round(b * 255.0)
        );
    }

}