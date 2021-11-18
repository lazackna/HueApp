package com.lazackna.hueapp.Util;

import android.graphics.Color;

import androidx.annotation.IntRange;

public class ColorHelper {

    public static int hueToColor(float hue, float sat, float val) {
        return Color.HSVToColor(new float[] {(360f / 65535f) * hue,sat,val});
        //(360 / hue.maxvalue) * hue
    }

}
